package pe.idat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import pe.idat.dto.ItemCarrito;
import pe.idat.entity.Producto;
import pe.idat.service.ProductoService;
import pe.idat.service.VentaService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private VentaService ventaService;

    // 1. VER CARRITO
    @GetMapping
    public String verCarrito(HttpSession session, Model model) {
        List<ItemCarrito> carrito = obtenerCarrito(session);
        double total = carrito.stream().mapToDouble(ItemCarrito::getSubtotal).sum();
        
        model.addAttribute("carrito", carrito);
        model.addAttribute("total", total);
        return "producto/carrito";
    }

    // 2. AGREGAR AL CARRITO (Con Cantidad Personalizada y "Stay on Page")
    @GetMapping("/agregar/{id}")
    public String agregarItem(@PathVariable Integer id, 
                              @RequestParam(required = false, defaultValue = "1") Integer cantidad, // NUEVO: Recibe cantidad
                              HttpSession session, 
                              RedirectAttributes flash,
                              HttpServletRequest request) {
        
        Producto p = productoService.obtenerProductoPorId(id);
        
        // Preparamos la URL de retorno
        String referer = request.getHeader("Referer");
        String urlRetorno = (referer != null && !referer.isEmpty()) ? referer : "/productos/catalogo";

        if (p != null) {
            // Validación inicial de stock
            if (p.getStockActual() <= 0) {
                flash.addFlashAttribute("error", "Producto agotado.");
                return "redirect:" + urlRetorno;
            }

            List<ItemCarrito> carrito = obtenerCarrito(session);
            boolean existe = false;
            
            for (ItemCarrito item : carrito) {
                if (item.getProductoId().equals(id)) {
                    // Validamos si la suma de lo que ya tengo + lo nuevo supera el stock
                    if (item.getCantidad() + cantidad <= p.getStockActual()) {
                        item.setCantidad(item.getCantidad() + cantidad);
                        flash.addFlashAttribute("success", "Se agregaron " + cantidad + " unidades.");
                    } else {
                        flash.addFlashAttribute("error", "Stock insuficiente para esa cantidad.");
                    }
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                // Validamos que la cantidad inicial no supere el stock
                if (cantidad <= p.getStockActual()) {
                    carrito.add(new ItemCarrito(p.getProductoId(), p.getNombre(), p.getImagenUrl(), p.getPrecioVenta().doubleValue(), cantidad));
                    flash.addFlashAttribute("success", "Producto añadido.");
                } else {
                    flash.addFlashAttribute("error", "No hay suficiente stock disponible.");
                }
            }
        }
        
        return "redirect:" + urlRetorno;
    }

    // 3. ELIMINAR DEL CARRITO
    @GetMapping("/eliminar/{id}")
    public String eliminarItem(@PathVariable Integer id, HttpSession session) {
        List<ItemCarrito> carrito = obtenerCarrito(session);
        carrito.removeIf(i -> i.getProductoId().equals(id));
        return "redirect:/carrito";
    }

    // 4. CHECKOUT
    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model, Authentication auth) {
        if (auth == null) {
            return "redirect:/login/logincliente";
        }
        
        List<ItemCarrito> carrito = obtenerCarrito(session);
        if (carrito.isEmpty()) return "redirect:/carrito";

        double total = carrito.stream().mapToDouble(ItemCarrito::getSubtotal).sum();
        model.addAttribute("carrito", carrito);
        model.addAttribute("total", total);
        
        return "producto/checkout";
    }

    // 5. PROCESAR PAGO
    @PostMapping("/procesar-pago")
    public String procesarPago(HttpSession session, Authentication auth, RedirectAttributes flash) {
        try {
            List<ItemCarrito> carrito = obtenerCarrito(session);
            if (carrito.isEmpty()) return "redirect:/carrito";

            ventaService.registrarVentaWeb(carrito, auth.getName());

            session.removeAttribute("carrito");
            
            flash.addFlashAttribute("mensajeBienvenida", "¡Pago exitoso! Tu pedido está en proceso.");
            return "redirect:/index";
            
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al procesar pago: " + e.getMessage());
            return "redirect:/carrito/checkout";
        }
    }

    @SuppressWarnings("unchecked")
    private List<ItemCarrito> obtenerCarrito(HttpSession session) {
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
            session.setAttribute("carrito", carrito);
        }
        return carrito;
    }
}