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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
                          @RequestParam("proveedorId") Integer proveedorId,
                          @RequestParam("categoriaId") Integer categoriaId,
                          RedirectAttributes flash) {
        try {
            if (!imagen.isEmpty()) {
                String nombreImagen = UUID.randomUUID().toString() + "_" + imagen.getOriginalFilename();
                Path rutaImagen = Paths.get("src//main//webapp//images//productos//" + nombreImagen);
                Files.createDirectories(rutaImagen.getParent());
                Files.write(rutaImagen, imagen.getBytes());
                producto.setImagenUrl(nombreImagen);
            } 
            
            Proveedor prov = new Proveedor();
            prov.setProveedorId(proveedorId);
            producto.setProveedor(prov);
            
            Categoria cat = new Categoria();
            cat.setCategoriaId(categoriaId);
            producto.setCategoria(cat);

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

    @GetMapping("/categoria/{nombreCategoria}")
    public String listarPorCategoria(@PathVariable String nombreCategoria, Model model) {
        String nombreBuscado = nombreCategoria.replace("-", " ").toUpperCase();
        List<Producto> productos = productoService.listarPorNombreCategoria(nombreBuscado);
        
        model.addAttribute("listaProductos", productos);
        model.addAttribute("tituloCategoria", nombreBuscado);
        
        return "producto/catalogo";
    }

    // --- NUEVO: VISTA DE DETALLE DEL PRODUCTO ---
    @GetMapping("/detalle/{id}")
    public String verDetalle(@PathVariable Integer id, Model model) {
        Producto p = productoService.obtenerProductoPorId(id);
        
        if (p == null) {
            return "redirect:/index";
        }
        
        model.addAttribute("p", p);
        
        // Lógica de Productos Sugeridos (Misma categoría, excluyendo el actual)
        List<Producto> sugeridos = productoService.listarPorNombreCategoria(p.getCategoria().getNombreCategoria());
        
        // Filtramos para que no salga el mismo producto que estamos viendo
        List<Producto> filtrados = sugeridos.stream()
                .filter(prod -> !prod.getProductoId().equals(id))
                .limit(4) // Máximo 4 sugerencias
                .collect(Collectors.toList());
        
        model.addAttribute("sugeridos", filtrados);
        
        return "producto/detalle";
    }

    @GetMapping("/api/listarPorProveedor/{id}")
    @ResponseBody
    public List<Producto> listarPorProveedorAPI(@PathVariable Integer id) {
        return productoRepository.findByProveedor_ProveedorId(id);
    }
}