package pe.idat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Sort; // Importante para el orden

import pe.idat.dto.CompraDTO;
import pe.idat.service.CompraService;
import pe.idat.service.ProductoService;
import pe.idat.service.ProveedorService;
import pe.idat.repository.CompraRepository;

@Controller
@RequestMapping("/compras")
public class CompraController {

    @Autowired
    private CompraService compraService;

    @Autowired
    private ProveedorService proveedorService;

    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private CompraRepository compraRepository;

    @GetMapping
    public String listar(Model model) {
        // Listar ordenado por ID descendente (lo último primero)
        model.addAttribute("listaCompras", compraRepository.findAll(Sort.by(Sort.Direction.DESC, "compraId"))); 
        return "compra/listar";
    }

    @GetMapping("/nueva")
    public String nuevaCompra(Model model) {
        model.addAttribute("listaProveedores", proveedorService.listarTodos());
        model.addAttribute("listaProductos", productoService.listarProductos());
        return "compra/formulario";
    }

    @PostMapping("/guardar")
    @ResponseBody
    public String guardarCompra(@RequestBody CompraDTO compraDTO, Authentication auth) {
        try {
            compraService.registrarCompra(compraDTO, auth.getName());
            return "ok";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
    
    @GetMapping("/recepcionar/{id}")
    public String recepcionar(@PathVariable Integer id, Authentication auth, RedirectAttributes flash) {
        try {
            compraService.confirmarRecepcionMercaderia(id, auth.getName());
            flash.addFlashAttribute("success", "¡Mercadería recibida! Stock actualizado correctamente.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al recepcionar: " + e.getMessage());
        }
        return "redirect:/compras";
    }

    // === NUEVO: GUARDAR FACTURA ===
    @PostMapping("/facturar")
    public String guardarFactura(@RequestParam Integer idCompra, @RequestParam String numFactura, RedirectAttributes flash) {
        try {
            compraService.registrarFacturaFisica(idCompra, numFactura);
            flash.addFlashAttribute("success", "Factura " + numFactura + " registrada correctamente.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al registrar factura: " + e.getMessage());
        }
        return "redirect:/compras";
    }
}