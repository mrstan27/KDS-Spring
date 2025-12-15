package pe.idat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import pe.idat.entity.Cliente;
import pe.idat.entity.Venta;
import pe.idat.repository.ClienteRepository;
import pe.idat.repository.VentaRepository;

@Controller
@RequestMapping("/mi-cuenta")
public class MiCuentaController {

    @Autowired
    private VentaRepository ventaRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping("/pedidos")
    public String misPedidos(Authentication auth, Model model) {
        if (auth == null || !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("CLIENTE"))) {
             // Si no está autenticado o no es cliente, lo manda al login
            return "redirect:/login/logincliente";
        }

        // 1. Identificar al cliente logueado por su correo (el username en Spring Security)
        String email = auth.getName();
        Cliente cliente = clienteRepository.findByCorreo(email);
        
        if (cliente == null) {
            // Esto solo debería pasar si la base de datos está corrupta
            return "redirect:/logout"; 
        }

        // 2. Traer sus ventas ordenadas por fecha (más recientes primero)
        model.addAttribute("misVentas", ventaRepository.findByCliente_ClienteIdOrderByFechaVentaDesc(cliente.getClienteId()));
        model.addAttribute("cliente", cliente);
        
        return "cliente/mis-pedidos"; // Vista para el listado
    }
    
    @GetMapping("/pedidos/detalle/{id}")
    public String detallePedido(@PathVariable Integer id, Authentication auth, Model model) {
        if (auth == null) return "redirect:/login/logincliente";
        
        String email = auth.getName();
        
        Venta venta = ventaRepository.findById(id).orElse(null);
        
        // Seguridad: Verificar que la venta exista Y pertenezca al cliente logueado
        if (venta != null && venta.getCliente().getCorreo().equals(email)) {
            model.addAttribute("venta", venta);
            return "cliente/detalle-pedido"; // Vista para el detalle
        }
        
        return "redirect:/mi-cuenta/pedidos?error=No tiene permisos o el pedido no existe";
    }
}