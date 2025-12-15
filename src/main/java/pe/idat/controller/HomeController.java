package pe.idat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pe.idat.service.ProductoService;

@Controller
@RequestMapping({"/", "/index"}) // Maneja la raíz y /index
public class HomeController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public String mostrarIndex(Model model) {
        // Traemos productos de "NEW IN" para mostrarlos como destacados en la portada
        // Esto asegura que la portada esté sincronizada con la base de datos
        model.addAttribute("productosDestacados", productoService.listarPorNombreCategoria("NEW IN"));
        return "index";   
    }
}