package pe.idat.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import pe.idat.entity.Rol;
import pe.idat.entity.Usuario;
import pe.idat.repository.RolRepository;
import pe.idat.repository.UsuarioRepository;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        
        // 1. CREAR ROLES SI NO EXISTEN
        // Buscamos si ya existen los roles, si no, los creamos.
        Rol rolAdmin = null;
        
        // Verificamos si la tabla roles está vacía o si falta el Admin
        List<Rol> roles = rolRepository.findAll();
        if (roles.isEmpty()) {
            rolAdmin = new Rol();
            rolAdmin.setNombreRol("Administrador");
            rolRepository.save(rolAdmin);
            
            Rol rolVendedor = new Rol();
            rolVendedor.setNombreRol("Vendedor");
            rolRepository.save(rolVendedor);
            
            Rol rolAlmacenero = new Rol();
            rolAlmacenero.setNombreRol("Almacenero");
            rolRepository.save(rolAlmacenero);
            
            System.out.println("✅ Roles creados correctamente.");
        } else {
            // Si ya existen, recuperamos el rol Administrador para asignarlo
            // (Asumiendo lógica simple: buscamos por nombre o tomamos el primero si es demo)
            // Aquí un stream simple para buscar el objeto Rol "Administrador"
            rolAdmin = roles.stream()
                    .filter(r -> r.getNombreRol().equals("Administrador"))
                    .findFirst()
                    .orElse(null);
        }

        // 2. CREAR USUARIO ADMIN SI NO EXISTE
        // Buscamos si existe alguien con el correo admin
        if (usuarioRepository.findByEmail("admin@admin.com").isEmpty() && rolAdmin != null) {
            
            Usuario admin = new Usuario();
            admin.setNombre("Super");
            admin.setApellido("Admin");
            admin.setEmail("admin@admin.com");
            admin.setPasswordHash(passwordEncoder.encode("123456")); // Contraseña encriptada
            admin.setActivo(true);
            admin.setRol(rolAdmin); // Asignamos el rol recuperado o creado

            usuarioRepository.save(admin);
            
            System.out.println("😎 Usuario ADMIN creado automáticamente.");
            System.out.println("📧 User: admin@admin.com");
            System.out.println("🔑 Pass: 123456");
        } else {
            System.out.println("☑️ El usuario ADMIN ya existe.");
        }
    }
}