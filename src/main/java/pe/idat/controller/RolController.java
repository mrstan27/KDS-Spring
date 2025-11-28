package pe.idat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pe.idat.entity.Rol;
import pe.idat.service.RolService;

@Controller
@RequestMapping("/roles")
public class RolController {

    @Autowired
    private RolService rolService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("listaRoles", rolService.listarRoles());
        return "rol/listar";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("rol", new Rol());
        return "rol/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("rol") Rol rol) {
        rolService.guardarRol(rol);
        return "redirect:/roles";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        model.addAttribute("rol", rolService.obtenerRolPorId(id));
        return "rol/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        rolService.eliminarRol(id);
        return "redirect:/roles";
    }
}