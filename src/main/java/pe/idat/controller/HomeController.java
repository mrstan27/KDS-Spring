package pe.idat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pe.idat.repository.ProductoRepository;

@Controller
@RequestMapping({"/", "/index"})
public class HomeController {

    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping
    public String mostrarIndex(Model model) {
        // CORRECCIÓN: Mostrar los productos más recientes creados, sin importar la categoría.
        // Esto garantiza que lo que acabas de agregar al inventario aparezca en la portada.
        model.addAttribute("productosDestacados", 
            productoRepository.findAll(Sort.by(Sort.Direction.DESC, "productoId")));
            
        return "index";   
    }
}