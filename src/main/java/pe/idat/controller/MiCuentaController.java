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
        if (auth == null) return "redirect:/login/logincliente";

        // 1. Identificar al cliente logueado
        String email = auth.getName();
        Cliente cliente = clienteRepository.findByCorreo(email);
        
        if (cliente == null) return "redirect:/logout"; // Seguridad paranoica

        // 2. Traer sus ventas
        model.addAttribute("misVentas", ventaRepository.findByCliente_ClienteIdOrderByFechaVentaDesc(cliente.getClienteId()));
        model.addAttribute("cliente", cliente);
        
        return "cliente/mis-pedidos";
    }
    
    @GetMapping("/pedidos/detalle/{id}")
    public String detallePedido(@PathVariable Integer id, Authentication auth, Model model) {
        if (auth == null) return "redirect:/login/logincliente";
        
        String email = auth.getName();
        // Buscar la venta
        Venta venta = ventaRepository.findById(id).orElse(null);
        
        // Seguridad: Verificar que la venta exista Y pertenezca al cliente logueado
        // (Evita que un cliente vea los pedidos de otro cambiando el ID en la URL)
        if (venta != null && venta.getCliente().getCorreo().equals(email)) {
            model.addAttribute("venta", venta);
            return "cliente/detalle-pedido";
        }
        
        return "redirect:/mi-cuenta/pedidos?error=no_autorizado";
    }
}