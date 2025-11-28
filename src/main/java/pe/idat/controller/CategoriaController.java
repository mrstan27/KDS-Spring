package pe.idat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pe.idat.entity.Categoria;
import pe.idat.service.CategoriaService;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("listaCategorias", categoriaService.listarCategorias());
        return "categoria/listar";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("categoria", new Categoria());
        model.addAttribute("titulo", "Nueva Categoría");
        return "categoria/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Categoria categoria) {
        categoriaService.guardarCategoria(categoria);
        return "redirect:/categorias";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        model.addAttribute("categoria", categoriaService.obtenerCategoriaPorId(id));
        model.addAttribute("titulo", "Editar Categoría");
        return "categoria/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        try {
            categoriaService.eliminarCategoria(id);
        } catch (Exception e) {
            // Si falla (por ejemplo si tiene productos asociados), simplemente recargamos
            // Podrías agregar un mensaje de error aquí si quisieras
            System.out.println("No se puede eliminar: " + e.getMessage());
        }
        return "redirect:/categorias";
    }
}