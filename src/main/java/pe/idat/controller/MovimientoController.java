package pe.idat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pe.idat.entity.Usuario;
import pe.idat.repository.MovimientoAlmacenRepository;
import pe.idat.repository.UsuarioRepository;

@Controller
@RequestMapping("/movimientos")
public class MovimientoController {

    @Autowired
    private MovimientoAlmacenRepository movimientoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public String listarMovimientos(Model model, Authentication auth) {
        // Enviar datos del usuario logueado para la cabecera/menú
        if(auth != null) {
            Usuario usuario = usuarioRepository.findByEmail(auth.getName()).orElse(null);
            model.addAttribute("usuario", usuario);
        }
        
        // Listar movimientos ordenados por fecha
        model.addAttribute("listaMovimientos", 
            movimientoRepository.findAll(Sort.by(Sort.Direction.DESC, "fechaMovimiento")));
        
        return "movimiento/listar";
    }
}