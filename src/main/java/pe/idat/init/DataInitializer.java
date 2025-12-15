package pe.idat.init;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import pe.idat.entity.Categoria;
import pe.idat.entity.Proveedor;
import pe.idat.entity.Rol;
import pe.idat.entity.Usuario;
import pe.idat.repository.CategoriaRepository;
import pe.idat.repository.ProveedorRepository;
import pe.idat.repository.RolRepository;
import pe.idat.repository.UsuarioRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;
    
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private ProveedorRepository proveedorRepository;

    @Override
    public void run(String... args) throws Exception {
        
        // --- 1. CREAR ROLES ---
        if (rolRepository.count() == 0) {
            crearRol("Administrador");
            crearRol("Vendedor");
            crearRol("Almacenero");
            crearRol("Compras");
            crearRol("Soporte");
            System.out.println("✅ Roles creados correctamente.");
        }

        // Cargar roles
        List<Rol> roles = rolRepository.findAll();
        Rol rolAdmin = buscarRol(roles, "Administrador");
        Rol rolVendedor = buscarRol(roles, "Vendedor");
        Rol rolAlmacenero = buscarRol(roles, "Almacenero");
        Rol rolCompras = buscarRol(roles, "Compras");
        Rol rolSoporte = buscarRol(roles, "Soporte");

        // --- 2. CREAR USUARIOS ---
        crearUsuarioSiNoExiste("admin@admin.com", "Super", "Admin", "123456", rolAdmin);
        crearUsuarioSiNoExiste("ventas@kds.com", "Juan", "Vendedor", "123456", rolVendedor);
        crearUsuarioSiNoExiste("almacen@kds.com", "Pedro", "Almacen", "123456", rolAlmacenero);
        crearUsuarioSiNoExiste("compras@kds.com", "Maria", "Compras", "123456", rolCompras);
        crearUsuarioSiNoExiste("soporte@kds.com", "Tecnico", "Soporte", "123456", rolSoporte);

        // --- 3. CREAR CATEGORÍAS ---
        if (categoriaRepository.count() == 0) {
            List<String> nombresCategorias = Arrays.asList(
                "NEW IN", "NITE OUT", "OUTERWEAR", "ROPA", "JEANS", "POLOS", "BLACK SUNDAY", "ACCESORIOS"
            );
            nombresCategorias.forEach(nombre -> {
                Categoria cat = new Categoria();
                cat.setNombreCategoria(nombre);
                categoriaRepository.save(cat);
            });
            System.out.println("👕 Categorías creadas.");
        }
        
        // --- 4. CREAR PROVEEDOR INICIAL ---
        // (Necesario para poder registrar el primer producto sin que falle la app)
        String rucNuevoMundo = "12345678912";
        if (proveedorRepository.findByRuc(rucNuevoMundo) == null) {
            Proveedor p = new Proveedor();
            p.setRazonSocial("Nuevo Munmdo");
            p.setRuc(rucNuevoMundo);
            p.setDireccion("Jiron Abilo 156");
            p.setTelefono("94655428");
            p.setCorreo("mundo@nuevo.com");
            p.setRubro("Moda");
            p.setEstado("ACTIVO");
            proveedorRepository.save(p);
            System.out.println("🏭 Proveedor inicial creado: Nuevo Munmdo");
        }

        System.out.println("🚀 Sistema inicializado. Base de datos de productos LIMPIA (manualmente gestionada).");
    }

    // --- MÉTODOS AUXILIARES ---
    private void crearRol(String nombreRol) {
        Rol rol = new Rol();
        rol.setNombreRol(nombreRol);
        rolRepository.save(rol);
    }

    private Rol buscarRol(List<Rol> roles, String nombreRol) {
        return roles.stream().filter(r -> r.getNombreRol().equals(nombreRol)).findFirst().orElse(null);
    }

    private void crearUsuarioSiNoExiste(String email, String nombre, String apellido, String password, Rol rol) {
        if (usuarioRepository.findByEmail(email).isEmpty() && rol != null) {
            Usuario usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setEmail(email);
            usuario.setPasswordHash(passwordEncoder.encode(password));
            usuario.setActivo(true);
            usuario.setRol(rol);
            usuarioRepository.save(usuario);
            System.out.println("👤 Usuario creado: " + email + " (" + rol.getNombreRol() + ")");
        }
    }
}