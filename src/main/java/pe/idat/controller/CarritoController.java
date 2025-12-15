package pe.idat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    // 2. AGREGAR AL CARRITO (Valida Stock)
    @GetMapping("/agregar/{id}")
    public String agregarItem(@PathVariable Integer id, HttpSession session, RedirectAttributes flash) {
        Producto p = productoService.obtenerProductoPorId(id);
        
        if (p != null) {
            if (p.getStockActual() <= 0) {
                flash.addFlashAttribute("error", "Producto agotado.");
                return "redirect:/index"; // O a donde estaba
            }

            List<ItemCarrito> carrito = obtenerCarrito(session);
            boolean existe = false;
            
            for (ItemCarrito item : carrito) {
                if (item.getProductoId().equals(id)) {
                    if (item.getCantidad() + 1 <= p.getStockActual()) {
                        item.setCantidad(item.getCantidad() + 1);
                        flash.addFlashAttribute("success", "Cantidad actualizada.");
                    } else {
                        flash.addFlashAttribute("error", "Stock máximo alcanzado.");
                    }
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                carrito.add(new ItemCarrito(p.getProductoId(), p.getNombre(), p.getImagenUrl(), p.getPrecioVenta().doubleValue(), 1));
                flash.addFlashAttribute("success", "Producto añadido.");
            }
        }
        return "redirect:/carrito";
    }

    // 3. ELIMINAR DEL CARRITO
    @GetMapping("/eliminar/{id}")
    public String eliminarItem(@PathVariable Integer id, HttpSession session) {
        List<ItemCarrito> carrito = obtenerCarrito(session);
        carrito.removeIf(i -> i.getProductoId().equals(id));
        return "redirect:/carrito";
    }

    // 4. CHECKOUT (Requiere Login)
    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model, Authentication auth) {
        // Verificar si hay usuario logueado
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

            // Registrar venta web real
            ventaService.registrarVentaWeb(carrito, auth.getName());

            // Limpiar carrito
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