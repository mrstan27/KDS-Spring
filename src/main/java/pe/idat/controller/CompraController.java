package pe.idat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // Para mensajes flash

import pe.idat.dto.CompraDTO;
import pe.idat.service.CompraService;
import pe.idat.service.ProductoService;
import pe.idat.service.ProveedorService;
import pe.idat.repository.CompraRepository; // Necesario para listar rápido

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
    private CompraRepository compraRepository; // Inyección directa para listar

    // 1. LISTAR COMPRAS (HISTORIAL)
    @GetMapping
    public String listar(Model model) {
        // Ordenamos por fecha descendente (lo más nuevo arriba)
        // Nota: Asegúrate de importar Sort de org.springframework.data.domain.Sort
        model.addAttribute("listaCompras", compraRepository.findAll()); 
        return "compra/listar";
    }

    // 2. FORMULARIO NUEVA COMPRA
    @GetMapping("/nueva")
    public String nuevaCompra(Model model) {
        model.addAttribute("listaProveedores", proveedorService.listarTodos());
        model.addAttribute("listaProductos", productoService.listarProductos());
        return "compra/formulario";
    }

    // 3. GUARDAR ORDEN (Backend API para el JS)
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
    
    // 4. RECEPCIONAR MERCADERÍA (Guía de Entrada)
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
}