package pe.idat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import pe.idat.entity.Producto;
import pe.idat.service.ProductoService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // --- SECCIÓN 1: MÉTODOS PARA EL ADMINISTRADOR (CRUD) ---

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("listaProductos", productoService.listarProductos());
        return "producto/listar";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("listaCategorias", productoService.listarCategorias());
        model.addAttribute("titulo", "Nuevo Producto");
        return "producto/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Producto producto, 
                          @RequestParam("file") MultipartFile imagen) {
        
        if (!imagen.isEmpty()) {
            // Recuerda: ruta absoluta funciona en local, en prod se usa otra estrategia
            Path directorioImagenes = Paths.get("src//main//webapp//images//productos");
            String rutaAbsoluta = directorioImagenes.toFile().getAbsolutePath();

            try {
                byte[] bytesImg = imagen.getBytes();
                Path rutaCompleta = Paths.get(rutaAbsoluta + "//" + imagen.getOriginalFilename());
                Files.write(rutaCompleta, bytesImg);
                
                producto.setImagenUrl(imagen.getOriginalFilename());
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        productoService.guardarProducto(producto);
        return "redirect:/productos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Producto p = productoService.obtenerProductoPorId(id);
        model.addAttribute("producto", p);
        model.addAttribute("listaCategorias", productoService.listarCategorias());
        model.addAttribute("titulo", "Editar Producto");
        return "producto/formulario";
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        productoService.eliminarProducto(id);
        return "redirect:/productos";
    }

    // --- SECCIÓN 2: MÉTODOS PARA LA TIENDA PÚBLICA (CATÁLOGO) ---

    @GetMapping("/categoria/{nombreCategoria}")
    public String verCatalogoPorCategoria(@PathVariable("nombreCategoria") String categoria, Model model) {
        
        String nombreBusqueda = categoria.replace("-", " ").toUpperCase();
        
        model.addAttribute("listaProductos", productoService.listarPorNombreCategoria(nombreBusqueda));
        model.addAttribute("tituloCategoria", nombreBusqueda);
        
        return "producto/catalogo"; 
    }
}