package pe.idat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pe.idat.entity.Cliente;
import pe.idat.service.ClienteService;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // --- 1. LISTAR (Para el Admin) ---
    @GetMapping("/listar")
    public String listarClientes(Model model) {
        model.addAttribute("listaClientes", clienteService.listar());
        return "cliente/cliente-list";
    }

    // --- 2. MOSTRAR FORMULARIO DE REGISTRO ---
    @GetMapping("/nuevo")
    public String nuevoCliente(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("titulo", "Registrar Cliente");
        return "cliente/cliente-form";
    }

    // --- 3. GUARDAR (LA LÓGICA IMPORTANTE) ---
    @PostMapping("/guardar")
    public String guardarCliente(@ModelAttribute("cliente") Cliente cliente, 
                                 Model model,
                                 RedirectAttributes flash) { // 'flash' sirve para pasar mensajes entre redirecciones

        // A) VALIDACIÓN: Verificar si el DNI ya existe (para evitar duplicados)
        Cliente existente = clienteService.buscarPorNumeroDocumento(cliente.getNumeroDocumento());
        if (existente != null && !existente.getClienteId().equals(cliente.getClienteId())) {
            model.addAttribute("error", "El número de documento ya está registrado.");
            model.addAttribute("titulo", cliente.getClienteId() == null ? "Registrar Cliente" : "Editar Cliente");
            return "cliente/cliente-form";
        }

        // B) LÓGICA DE GUARDADO
        if (cliente.getClienteId() == null) {
            // CASO 1: ES UN REGISTRO NUEVO (Público)
            
            // 1. Encriptamos la contraseña
            String passEncriptada = passwordEncoder.encode(cliente.getPassword());
            cliente.setPassword(passEncriptada);
            
            // 2. Estado inicial
            cliente.setEstado("Activo");
            
            // 3. Guardamos
            clienteService.guardar(cliente);
            
            // 4. PREPARAMOS EL MENSAJE DE BIENVENIDA (Para el Confeti 🎉)
            flash.addFlashAttribute("nombreRegistro", cliente.getNombre());
            
            // 5. Redirigimos al Login de Clientes
            return "redirect:/login/logincliente";
            
        } else {
            // CASO 2: ES UNA EDICIÓN (Probablemente el Admin editando datos)
            
            // Recuperamos el cliente original de la BD para no perder la contraseña si viene vacía
            Cliente clienteOriginal = clienteService.buscarPorId(cliente.getClienteId());
            
            if (cliente.getPassword() == null || cliente.getPassword().isEmpty()) {
                // Si no escribió nueva contraseña, mantenemos la anterior
                cliente.setPassword(clienteOriginal.getPassword());
            } else {
                // Si escribió una nueva, la encriptamos
                cliente.setPassword(passwordEncoder.encode(cliente.getPassword()));
            }
            
            // Mantenemos el estado original si no se envió en el form
            if (cliente.getEstado() == null) {
                cliente.setEstado(clienteOriginal.getEstado());
            }

            clienteService.guardar(cliente);
            
            // Admin vuelve a la lista
            flash.addFlashAttribute("success", "Cliente actualizado correctamente.");
            return "redirect:/cliente/listar";
        }
    }

    // --- 4. EDITAR (Vista Admin) ---
    @GetMapping("/editar/{id}")
    public String editarCliente(@PathVariable("id") Integer id, Model model) {
        Cliente cliente = clienteService.buscarPorId(id);

        if (cliente == null) {
            return "redirect:/cliente/listar";
        }

        // Limpiamos la password para que no aparezca encriptada en el formulario
        cliente.setPassword(""); 
        
        model.addAttribute("cliente", cliente);
        model.addAttribute("titulo", "Editar Cliente");
        return "cliente/cliente-form";
    }

    // --- 5. ELIMINAR (Vista Admin) ---
    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable("id") Integer id, RedirectAttributes flash) {
        clienteService.eliminar(id);
        flash.addFlashAttribute("success", "Cliente eliminado correctamente.");
        return "redirect:/cliente/listar";
    }
}
