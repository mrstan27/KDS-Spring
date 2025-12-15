package pe.idat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import pe.idat.dto.VentaDTO;
import pe.idat.service.ClienteService;
import pe.idat.service.ProductoService;
import pe.idat.service.VentaService;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;
    
    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private ProductoService productoService;

    // 1. Mostrar la Pantalla del Punto de Venta (POS)
    @GetMapping("/nueva")
    public String nuevaVenta(Model model) {
        // Cargar listas necesarias para los selectores
        model.addAttribute("listaClientes", clienteService.listar());
        
        // Solo cargamos productos que tengan stock disponible para evitar errores visuales
        // (Aunque el backend valida igual, es mejor no mostrar lo que no se puede vender)
        model.addAttribute("listaProductos", productoService.listarProductos());
        
        return "venta/formulario"; // La vista JSP que crearemos abajo
    }

    // 2. Procesar la Venta (Recibe JSON desde el JavaScript del POS)
    @PostMapping("/guardar")
    @ResponseBody
    public String guardarVenta(@RequestBody VentaDTO ventaDTO, Authentication auth) {
        try {
            // El usuario autenticado (Juan) es el vendedor responsable
            ventaService.registrarVenta(ventaDTO, auth.getName());
            return "ok";
        } catch (RuntimeException e) {
            return "Error de lógica: " + e.getMessage(); // Ej: Stock insuficiente
        } catch (Exception e) {
            e.printStackTrace();
            return "Error crítico en el servidor: " + e.getMessage();
        }
    }
}