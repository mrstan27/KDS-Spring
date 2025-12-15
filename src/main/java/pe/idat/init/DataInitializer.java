package pe.idat.init;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import pe.idat.entity.Categoria;
import pe.idat.entity.Producto;
import pe.idat.entity.Proveedor;
import pe.idat.entity.Rol;
import pe.idat.entity.Usuario;
import pe.idat.repository.CategoriaRepository;
import pe.idat.repository.ProductoRepository;
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
    private ProductoRepository productoRepository;
    
    @Autowired
    private ProveedorRepository proveedorRepository;

    // Variables para caché temporal
    private List<Categoria> todasLasCategorias;
    private Proveedor proveedorPrincipal; 

    @Override
    public void run(String... args) throws Exception {
        
        System.out.println("⏳ Iniciando carga de datos segura...");

        // --- 1. CREAR ROLES (UNO POR UNO) ---
        // Esto asegura que si falta uno, se cree, aunque existan otros.
        Rol rolAdmin = asegurarRol("Administrador");
        Rol rolVendedor = asegurarRol("Vendedor");
        Rol rolAlmacenero = asegurarRol("Almacenero");
        Rol rolCompras = asegurarRol("Compras");
        Rol rolSoporte = asegurarRol("Soporte");

        // --- 2. CREAR USUARIOS PREDETERMINADOS ---
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
        this.todasLasCategorias = categoriaRepository.findAll();
        
        // --- 4. CREAR PROVEEDOR "Nuevo Munmdo" ---
        String rucNuevoMundo = "12345678912";
        Proveedor existente = proveedorRepository.findByRuc(rucNuevoMundo);
        
        if (existente == null) {
            Proveedor p = new Proveedor();
            p.setRazonSocial("Nuevo Munmdo");
            p.setRuc(rucNuevoMundo);
            p.setDireccion("Jiron Abilo 156");
            p.setTelefono("94655428");
            p.setCorreo("mundo@nuevo.com");
            p.setRubro("Moda"); 
            p.setEstado("ACTIVO");
            
            this.proveedorPrincipal = proveedorRepository.save(p);
            System.out.println("🏭 Proveedor creado: Nuevo Munmdo");
        } else {
            this.proveedorPrincipal = existente;
            // Aseguramos que tenga el rubro si estaba nulo antes
            if (existente.getRubro() == null) {
                existente.setRubro("Moda");
                proveedorRepository.save(existente);
            }
        }
        
        // --- 5. LOG DE CONFIRMACIÓN ---
        System.out.println("✅ Sistema listo. Usuario Compras: compras@kds.com / 123456");
    }

    // --- MÉTODOS AUXILIARES ROBUSTOS ---
    
    private Rol asegurarRol(String nombreRol) {
        // Busca el rol, si no existe, lo crea.
        return rolRepository.findByNombreRol(nombreRol)
                .orElseGet(() -> {
                    Rol nuevo = new Rol();
                    nuevo.setNombreRol(nombreRol);
                    System.out.println("➕ Rol creado: " + nombreRol);
                    return rolRepository.save(nuevo);
                });
    }

    private void crearUsuarioSiNoExiste(String email, String nombre, String apellido, String password, Rol rol) {
        if (usuarioRepository.findByEmail(email).isEmpty()) {
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