package pe.idat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // IMPORTANTE
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // IMPORTANTE

import pe.idat.entity.Cliente;
import pe.idat.service.ClienteService;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // Inyectamos el encriptador de Spring Security
    @Autowired
    private PasswordEncoder passwordEncoder;

    // LISTAR (Solo para admin)
    @GetMapping("/listar")
    public String listarClientes(Model model) {
        model.addAttribute("listaClientes", clienteService.listar());
        return "cliente/cliente-list";
    }

    // NUEVO CLIENTE (Público)
    @GetMapping("/nuevo")
    public String nuevoCliente(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("titulo", "Registrar Cliente");
        return "cliente/cliente-form";
    }

    // GUARDAR O ACTUALIZAR
    @PostMapping("/guardar")
    public String guardarCliente(@ModelAttribute("cliente") Cliente cliente, 
                                 Model model,
                                 RedirectAttributes flash) { // Usamos flash para mensajes

        // 1. Validación de Documento Duplicado
        Cliente existenteDoc = clienteService.buscarPorNumeroDocumento(cliente.getNumeroDocumento());
        if (existenteDoc != null && !existenteDoc.getClienteId().equals(cliente.getClienteId())) {
            model.addAttribute("error", "El número de documento ya está registrado.");
            return "cliente/cliente-form";
        }
        
        // 2. Lógica de Contraseña
        if (cliente.getClienteId() == null) {
            // A) ES NUEVO REGISTRO
            // Encriptamos la contraseña que viene del formulario
            String passEncriptada = passwordEncoder.encode(cliente.getPassword());
            cliente.setPassword(passEncriptada);
            cliente.setEstado("Activo"); // Aseguramos estado activo
            
            clienteService.guardar(cliente);
            
            // Redirigimos al LOGIN avisando que fue exitoso
            flash.addFlashAttribute("success", "¡Registro exitoso! Por favor inicia sesión.");
            return "redirect:/login/logincliente"; // Te manda al login
            
        } else {
            // B) ES ACTUALIZACIÓN (Edición por Admin o el mismo cliente)
            // Recuperamos el cliente original de la BD para no perder la contraseña si no la envió
            Cliente clienteOriginal = clienteService.buscarPorId(cliente.getClienteId());
            
            // Si el campo password viene vacío en el form, mantenemos la antigua
            if (cliente.getPassword() == null || cliente.getPassword().isEmpty()) {
                cliente.setPassword(clienteOriginal.getPassword());
            } else {
                // Si escribió nueva contraseña, la encriptamos
                cliente.setPassword(passwordEncoder.encode(cliente.getPassword()));
            }
            
            clienteService.guardar(cliente);
            return "redirect:/cliente/listar"; // Si editó un admin, vuelve a la lista
        }
    }

    // ... tus otros métodos (editar, eliminar) siguen igual ...
    // EDITAR
    @GetMapping("/editar/{id}")
    public String editarCliente(@PathVariable("id") Integer id, Model model) {
        Cliente cliente = clienteService.buscarPorId(id);
        if (cliente == null) return "redirect:/cliente/listar";

        // Limpiamos la password para que no se vea en el formulario de edición
        cliente.setPassword(""); 
        
        model.addAttribute("cliente", cliente);
        model.addAttribute("titulo", "Editar Cliente");
        return "cliente/cliente-form";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable("id") Integer id) {
        clienteService.eliminar(id);
        return "redirect:/cliente/listar";
    }
}

