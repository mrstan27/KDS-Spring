package pe.idat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // IMPORTANTE: Para pasar el mensaje

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

    // --- NUEVO MÉTODO: CEREBRO DE REDIRECCIÓN ---
    // Este método recibe al usuario justo después de loguearse exitosamente
    @GetMapping("/login-success")
    public String loginSuccess(Authentication auth, RedirectAttributes flash) {
        
        // 1. Verificamos si es un CLIENTE
        // Buscamos en sus roles si tiene la autoridad "CLIENTE"
        boolean esCliente = auth.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("CLIENTE"));

        if (esCliente) {
            // CASO CLIENTE:
            // Preparamos el mensaje para la ventana emergente
            String correo = auth.getName();
            flash.addFlashAttribute("mensajeBienvenida", "¡Qué bueno verte de nuevo, " + correo + "!");
            
            // Lo mandamos a la página principal (Index)
            return "redirect:/index";
        }
        
        // CASO ADMIN O VENDEDOR:
        // Lo mandamos al Dashboard
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