package pe.idat.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import pe.idat.entity.Categoria;
import pe.idat.entity.Producto;
import pe.idat.entity.Rol;
import pe.idat.entity.Usuario;
import pe.idat.repository.CategoriaRepository;
import pe.idat.repository.ProductoRepository;
import pe.idat.repository.RolRepository;
import pe.idat.repository.UsuarioRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

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
    
    @Override
    public void run(String... args) throws Exception {
        
        // --- 1. CREAR ROLES ---
        // Validamos si existen roles, si no, los creamos.
        Rol rolAdmin = null;
        List<Rol> roles = rolRepository.findAll();
        
        if (roles.isEmpty()) {
            rolAdmin = new Rol(); rolAdmin.setNombreRol("Administrador");
            rolRepository.save(rolAdmin);
            
            Rol rolVendedor = new Rol(); rolVendedor.setNombreRol("Vendedor");
            rolRepository.save(rolVendedor);
            
            Rol rolAlmacenero = new Rol(); rolAlmacenero.setNombreRol("Almacenero");
            rolRepository.save(rolAlmacenero);
            
            System.out.println("✅ Roles creados correctamente.");
        } else {
            // Buscamos el rol Administrador para usarlo abajo
            rolAdmin = roles.stream()
                    .filter(r -> r.getNombreRol().equals("Administrador"))
                    .findFirst()
                    .orElse(null);
            System.out.println("☑️ Los roles ya existen.");
        }

        // --- 2. CREAR USUARIO ADMIN ---
        // Validamos si existe el email, si no, lo creamos.
        if (usuarioRepository.findByEmail("admin@admin.com").isEmpty() && rolAdmin != null) {
            Usuario admin = new Usuario();
            admin.setNombre("Super");
            admin.setApellido("Admin");
            admin.setEmail("admin@admin.com");
            admin.setPasswordHash(passwordEncoder.encode("123456"));
            admin.setActivo(true);
            admin.setRol(rolAdmin);

            usuarioRepository.save(admin);
            System.out.println("😎 Usuario ADMIN creado: admin@admin.com / 123456");
        } else {
            // AQUÍ ESTÁ EL MENSAJE QUE FALTABA
            System.out.println("☑️ El usuario ADMIN ya existe.");
        }

        // --- 3. CREAR CATEGORÍAS (KIDS MADE HERE) ---
        // Validamos si hay categorías, si la tabla está vacía, las creamos.
        if (categoriaRepository.count() == 0) {
            List<String> nombresCategorias = Arrays.asList(
                "NEW IN", 
                "NITE OUT", 
                "OUTERWEAR", 
                "ROPA", 
                "JEANS", 
                "POLOS", 
                "BLACK SUNDAY", 
                "ACCESORIOS"
            );

            for (String nombre : nombresCategorias) {
                Categoria cat = new Categoria();
                cat.setNombreCategoria(nombre);
                categoriaRepository.save(cat);
            }
            System.out.println("👕 Categorías de KIDS creadas automáticamente.");
        } else {
            System.out.println("☑️ Las categorías ya existen.");
        }
        
        if (productoRepository.count() == 0) {
            
            // Buscamos la categoría JEANSs
            Categoria catJeans = categoriaRepository.findAll().stream()
                .filter(c -> c.getNombreCategoria().equals("JEANS")).findFirst().orElse(null);

            if (catJeans != null) {
                // DATOS: { "Nombre", "Descripción", "Precio", "NombreArchivo" }
                String[][] misJeans = {
                    {"Jean Skinny Blue", "Jean ajustado color azul clásico, tela stretch premium.", "89.90", "jean1.webp"},
                    {"Jean Cargo Black", "Estilo urbano con bolsillos laterales funcionales.", "119.90", "jean2.webp"},
                    {"Jean Mom Fit Roto", "Corte alto y relajado con detalles rasgados.", "99.90", "jean3.jpg"},
                    {"Jean Wide Leg Ice", "Pantalón de pierna ancha color hielo, tendencia 2025.", "109.90", "jean4.jpg"},
                    {"Jogger Denim", "La comodidad de un buzo con el estilo de un jean.", "79.90", "jean5.webp"},
                    {"Jean Carpenter", "Estilo carpintero con costuras visibles y corte recto.", "129.90", "jean6.webp"},
                    {"Jean Baggy 90s", "Estilo retro ancho, lavado oscuro vintage.", "109.90", "jean7.webp"},
                    {"Jean Slim Negro", "Básico indispensable, corte slim fit color negro sólido.", "89.90", "jean8.webp"},
                    {"Jean Acid Wash", "Lavado ácido ochentero, corte relajado.", "95.90", "jean9.webp"},
                    {"Jean Recto Clásico", "El corte que nunca pasa de moda, azul medio.", "79.90", "jean10.webp"}
                };

                for (String[] datos : misJeans) {
                    Producto p = new Producto();
                    // Usamos tus Setters exactos
                    p.setNombre(datos[0]);
                    p.setDescripcion(datos[1]);
                    p.setPrecioVenta(new BigDecimal(datos[2])); // Convertimos String a BigDecimal
                    p.setStockActual(50); 
                    p.setCategoria(catJeans);
                    
                    // Aquí guardamos la ruta relativa
                    p.setImagenUrl("jeans/" + datos[3]); 
                    
                    productoRepository.save(p);
                }
                System.out.println("👖 Se cargaron los 10 JEANS de prueba exitosamente.");
            }
            
            
            
        } else {
             System.out.println("☑️ Los productos ya existen en la BD.");
        }
    }
 }
