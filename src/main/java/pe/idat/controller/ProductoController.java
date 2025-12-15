package pe.idat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pe.idat.entity.Producto;
import pe.idat.entity.Proveedor;
import pe.idat.entity.Categoria;
import pe.idat.service.CategoriaService;
import pe.idat.service.ProductoService;
import pe.idat.service.ProveedorService;
import pe.idat.repository.ProductoRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private CategoriaService categoriaService;
    
    @Autowired
    private ProveedorService proveedorService; 
    
    @Autowired
    private ProductoRepository productoRepository; 

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("listaProductos", productoService.listarProductos());
        return "producto/listar";
    }
    
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("listaCategorias", categoriaService.listarCategorias());
        // IMPORTANTE: Enviamos la lista de proveedores a la vista
        model.addAttribute("listaProveedores", proveedorService.listarTodos());
        return "producto/formulario";
    }
    
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Producto p = productoService.obtenerProductoPorId(id);
        if (p != null) {
            model.addAttribute("producto", p);
            model.addAttribute("listaCategorias", categoriaService.listarCategorias());
            model.addAttribute("listaProveedores", proveedorService.listarTodos());
            return "producto/formulario";
        }
        return "redirect:/productos";
    }
    
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Producto producto, 
                          @RequestParam("file") MultipartFile imagen,
                          @RequestParam("proveedorId") Integer proveedorId, // Recibe el ID del select
                          @RequestParam("categoriaId") Integer categoriaId, // Recibe el ID del select
                          RedirectAttributes flash) {
        try {
            // 1. Imagen
            if (!imagen.isEmpty()) {
                String nombreImagen = UUID.randomUUID().toString() + "_" + imagen.getOriginalFilename();
                Path rutaImagen = Paths.get("src//main//webapp//images//productos//" + nombreImagen);
                Files.createDirectories(rutaImagen.getParent()); // Asegura que la carpeta exista
                Files.write(rutaImagen, imagen.getBytes());
                producto.setImagenUrl(nombreImagen);
            } else {
                if (producto.getProductoId() == null) producto.setImagenUrl("default.jpg");
                // Si es editar, el hidden field en el JSP debe mantener la URL, o JPA lo maneja si el objeto está atado
            }

            // 2. Vincular Proveedor (OBLIGATORIO)
            Proveedor prov = new Proveedor();
            prov.setProveedorId(proveedorId);
            producto.setProveedor(prov);
            
            // 3. Vincular Categoría
            Categoria cat = new Categoria();
            cat.setCategoriaId(categoriaId);
            producto.setCategoria(cat);

            // 4. Stock Inicial (Siempre 0 al crear)
            if(producto.getProductoId() == null) {
                producto.setStockActual(0);
            }

            productoService.guardarProducto(producto);
            flash.addFlashAttribute("success", "Producto guardado con éxito.");
            
        } catch (Exception e) {
            e.printStackTrace();
            flash.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
            return "redirect:/productos/nuevo";
        }
        
        return "redirect:/productos";
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes flash) {
        try {
            productoService.eliminarProducto(id);
            flash.addFlashAttribute("success", "Producto eliminado.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "No se puede eliminar: el producto tiene ventas o compras asociadas.");
        }
        return "redirect:/productos";
    }

    // API para el AJAX de Cotizaciones
    @GetMapping("/api/listarPorProveedor/{id}")
    @ResponseBody
    public List<Producto> listarPorProveedorAPI(@PathVariable Integer id) {
        return productoRepository.findByProveedor_ProveedorId(id);
    }
}