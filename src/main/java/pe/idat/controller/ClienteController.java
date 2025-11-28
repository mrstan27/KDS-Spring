package pe.idat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import pe.idat.entity.Cliente;
import pe.idat.service.ClienteService;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // LISTAR
    @GetMapping("/listar")
    public String listarClientes(Model model) {
        model.addAttribute("listaClientes", clienteService.listar());
        return "cliente/cliente-list";
    }

    // NUEVO CLIENTE
    @GetMapping("/nuevo")
    public String nuevoCliente(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("titulo", "Registrar Cliente");
        return "cliente/cliente-form";
    }

    // GUARDAR O ACTUALIZAR
    @PostMapping("/guardar")
    public String guardarCliente(@ModelAttribute("cliente") Cliente cliente, Model model) {

        // Validación por documento único (DNI/RUC)
        Cliente existente = clienteService.buscarPorNumeroDocumento(cliente.getNumeroDocumento());

        if (existente != null && !existente.getClienteId().equals(cliente.getClienteId())) {
            model.addAttribute("error", "El número de documento ya está registrado.");
            model.addAttribute("titulo", cliente.getClienteId() == null ? "Registrar Cliente" : "Editar Cliente");
            return "cliente/cliente-form";
        }

        clienteService.guardar(cliente);
        return "redirect:/cliente/listar"; // ← corregido
    }

    // EDITAR
    @GetMapping("/editar/{id}")
    public String editarCliente(@PathVariable("id") Integer id, Model model) {

        Cliente cliente = clienteService.buscarPorId(id);

        if (cliente == null) {
            return "redirect:/cliente/listar"; // ← corregido
        }

        model.addAttribute("cliente", cliente);
        model.addAttribute("titulo", "Editar Cliente");

        return "cliente/cliente-form";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable("id") Integer id) {
        clienteService.eliminar(id);
        return "redirect:/cliente/listar"; // ← corregido
    }
}

