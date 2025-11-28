package pe.idat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pe.idat.entity.Usuario;
import pe.idat.repository.UsuarioRepository; // Usamos el repo directo para buscar rápido


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

    @GetMapping("/menu")
    public String mostrarMenu(Authentication auth, Model model) {
        // 'auth' tiene el email del usuario logueado gracias a Spring Security
        if (auth != null) {
            String email = auth.getName();
            
            // Buscamos los datos completos (Nombre, Rol) usando el email
            Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
            
            model.addAttribute("usuario", usuario);
        }
        return "principal/menu";
    }
}