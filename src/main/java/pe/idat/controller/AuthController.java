package pe.idat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pe.idat.entity.Usuario;
import pe.idat.repository.UsuarioRepository;

@Controller
@RequestMapping("/login")
public class AuthController {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/loginusuario")
    public String mostrarLoginUsuario() {
        return "auth/login";
    }
    
    @GetMapping("/logincliente")
    public String mostrarLoginCliente() {
        return "cliente/cliente-login";
    }

    // --- CEREBRO DE REDIRECCIÓN ---
    @GetMapping("/login-success")
    public String loginSuccess(Authentication auth, RedirectAttributes flash) {
        
        // 1. Verificamos si es un CLIENTE
        boolean esCliente = auth.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("CLIENTE"));

        if (esCliente) {
            // CASO CLIENTE: Alerta de Bienvenida + Index
            String correo = auth.getName();
            // Mensaje personalizado
            flash.addFlashAttribute("mensajeBienvenida", "¡Qué gusto verte de nuevo, " + correo + "!");
            
            return "redirect:/index";
        }
        
        // CASO ADMIN/VENDEDOR: Dashboard
        return "redirect:/login/menu";
    }

    @GetMapping("/menu")
    public String mostrarMenu(Authentication auth, Model model) {
        if (auth != null) {
            String email = auth.getName();
            Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
            model.addAttribute("usuario", usuario);
        }
        return "principal/menu";
    }
}