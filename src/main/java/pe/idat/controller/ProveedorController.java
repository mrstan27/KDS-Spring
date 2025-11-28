package pe.idat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import pe.idat.entity.Proveedor;
import pe.idat.service.ProveedorService;

@Controller
@RequestMapping("/proveedor")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    // LISTAR
    @GetMapping("/lista")
    public String listarProveedores(Model model) {
        model.addAttribute("listaProveedores", proveedorService.listarTodos());
        return "proveedor/proveedor-list";
    }

    // NUEVO
    @GetMapping("/nuevo")
    public String nuevoProveedor(Model model) {
        model.addAttribute("proveedor", new Proveedor());
        model.addAttribute("titulo", "Registrar Proveedor");
        return "proveedor/proveedor-form";
    }

    // GUARDAR / ACTUALIZAR
    @PostMapping("/guardar")
    public String guardarProveedor(@ModelAttribute("proveedor") Proveedor proveedor, Model model) {

        Proveedor existente = proveedorService.buscarPorRuc(proveedor.getRuc());

        if (existente != null && !existente.getProveedorId().equals(proveedor.getProveedorId())) {
            model.addAttribute("error", "El RUC ya está registrado.");
            model.addAttribute("titulo", proveedor.getProveedorId() == null ? "Registrar Proveedor" : "Editar Proveedor");
            return "proveedor/proveedor-form";
        }

        proveedorService.guardar(proveedor);
        return "redirect:/proveedor/lista";
    }

    // EDITAR
    @GetMapping("/editar/{id}")
    public String editarProveedor(@PathVariable("id") Integer id, Model model) {

        Proveedor proveedor = proveedorService.buscarPorId(id);

        if (proveedor == null) {
            return "redirect:/proveedor/lista";
        }

        model.addAttribute("proveedor", proveedor);
        model.addAttribute("titulo", "Editar Proveedor");

        return "proveedor/proveedor-form";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminarProveedor(@PathVariable("id") Integer id) {
        proveedorService.eliminar(id);
        return "redirect:/proveedor/lista";
    }
}
