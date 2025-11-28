package pe.idat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import pe.idat.entity.Usuario;
import pe.idat.service.UsuarioService;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("listaUsuarios", usuarioService.listarUsuarios());
        return "usuario/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        Usuario usuario = new Usuario();
        model.addAttribute("usuario", usuario);
        model.addAttribute("listaRoles", usuarioService.listarRoles());
        return "usuario/formulario";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute("usuario") Usuario usuario) {
        
        // === CORRECCIÓN ===
        // Si el ID viene como cadena vacía "" (desde el formulario), 
        // lo convertimos a NULL para que Hibernate sepa que es un INSERT (nuevo)
        // y no un UPDATE (editar).
        if (usuario.getUsuarioId() != null && usuario.getUsuarioId().isEmpty()) {
            usuario.setUsuarioId(null);
        }
        
        usuarioService.guardarUsuario(usuario);
        return "redirect:/usuarios";
    }

    // === CAMBIO AQUÍ: Integer id -> String id ===
    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable String id, Model model) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
        
        // TRUCO VISUAL: Limpiamos la contraseña para que el campo aparezca vacío en el HTML
        usuario.setPasswordHash(""); 
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("listaRoles", usuarioService.listarRoles());
        return "usuario/formulario";
    }

    // === CAMBIO AQUÍ: Integer id -> String id ===
    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable String id) {
        usuarioService.eliminarUsuario(id);
        return "redirect:/usuarios";
    }
}