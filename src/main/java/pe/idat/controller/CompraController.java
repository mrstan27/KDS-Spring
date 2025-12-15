package pe.idat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletResponse; 
import pe.idat.dto.CompraDTO;
import pe.idat.entity.Compra;
import pe.idat.entity.Usuario; // Importar
import pe.idat.repository.CompraRepository;
import pe.idat.repository.UsuarioRepository; // Importar
import pe.idat.service.CompraService;
import pe.idat.service.PdfService; 
import pe.idat.service.ProductoService;
import pe.idat.service.ProveedorService;

import java.io.IOException;

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
    @Autowired
    private PdfService pdfService; 
    
    // --- CORRECCIÓN: Inyectamos UsuarioRepository ---
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public String listar(Model model, Authentication auth) {
        // 1. Obtener el usuario autenticado para enviarlo a la vista
        if (auth != null) {
            String email = auth.getName();
            Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
            model.addAttribute("usuario", usuario); // ¡CRÍTICO PARA LOS BOTONES DEL ALMACENERO!
        }

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
    
    @GetMapping("/pdf/{id}")
    public void descargarPdf(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        Compra compra = compraRepository.findById(id).orElse(null);
        if(compra != null) {
            response.setContentType("application/pdf");
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=Orden_" + id + ".pdf";
            response.setHeader(headerKey, headerValue);
            
            pdfService.exportarOrdenCompra(response, compra);
        }
    }
    
    // --- ENDPOINTS DE APROBACIÓN (Ahora protegidos en SecurityConfig) ---
    
    @GetMapping("/aprobar/{id}")
    public String aprobar(@PathVariable Integer id, RedirectAttributes flash) {
        try {
            compraService.aprobarOrdenCompra(id);
            flash.addFlashAttribute("success", "Orden #" + id + " APROBADA. Ahora puede ser recepcionada.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al aprobar: " + e.getMessage());
        }
        return "redirect:/compras";
    }

    @GetMapping("/rechazar/{id}")
    public String rechazar(@PathVariable Integer id, RedirectAttributes flash) {
        try {
            compraService.rechazarOrdenCompra(id);
            flash.addFlashAttribute("error", "Orden #" + id + " RECHAZADA. Se mantendrá en el historial.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al rechazar: " + e.getMessage());
        }
        return "redirect:/compras";
    }

    // ==========================================
    //        CONTROLADOR DE COTIZACIONES
    // ==========================================

    @GetMapping("/cotizaciones")
    public String listarCotizaciones(Model model) {
        model.addAttribute("listaCotizaciones", compraService.listarCotizaciones());
        return "compra/cotizacion-listar"; 
    }

    @GetMapping("/cotizaciones/nueva")
    public String nuevaCotizacion(Model model) {
        model.addAttribute("listaProveedores", proveedorService.listarTodos());
        model.addAttribute("listaProductos", productoService.listarProductos());
        return "compra/cotizacion-form"; 
    }

    @PostMapping("/cotizaciones/guardar")
    @ResponseBody
    public String guardarCotizacion(@RequestBody CompraDTO compraDTO, Authentication auth) {
        try {
            compraService.registrarCotizacion(compraDTO, auth.getName());
            return "ok";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/cotizaciones/aprobar/{id}")
    public String aprobarCotizacion(@PathVariable Integer id, RedirectAttributes flash) {
        try {
            // Esto solo convierte a Orden PENDIENTE. La aprobación final es del Admin.
            compraService.convertirCotizacionAOrden(id);
            flash.addFlashAttribute("success", "Cotización convertida. La Orden #" + id + " está PENDIENTE de aprobación administrativa.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al procesar: " + e.getMessage());
        }
        return "redirect:/compras"; 
    }
}