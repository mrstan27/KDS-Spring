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

    private List<Categoria> todasLasCategorias;
    private Proveedor proveedorPrincipal; 

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
            p.setRubro("Moda"); // Dato específico añadido
            p.setEstado("ACTIVO");
            
            this.proveedorPrincipal = proveedorRepository.save(p);
            System.out.println("🏭 Proveedor creado: Nuevo Munmdo (RUC: " + rucNuevoMundo + ")");
        } else {
            this.proveedorPrincipal = existente;
            System.out.println("☑️ Proveedor 'Nuevo Munmdo' ya existe.");
        }

        // --- 5. CREAR PRODUCTOS (Vinculados a Nuevo Munmdo) ---
        // A. CATEGORÍA JEANS
        crearProductosDetallados("JEANS", "jeans", 
            new String[][] {
                {"Jean Skinny Blue", "Jean ajustado color azul clásico, tela stretch premium.", "89.90"},
                {"Jean Cargo Black", "Estilo urbano con bolsillos laterales funcionales.", "119.90"},
                {"Jean Mom Fit Roto", "Corte alto y relajado con detalles rasgados.", "99.90"},
                {"Jean Wide Leg Ice", "Pantalón de pierna ancha color hielo, tendencia 2025.", "109.90"},
                {"Jogger Denim", "La comodidad de un buzo con el estilo de un jean.", "79.90"},
                {"Jean Carpenter", "Estilo carpintero con costuras visibles y corte recto.", "129.90"},
                {"Jean Baggy 90s", "Estilo retro ancho, lavado oscuro vintage.", "109.90"},
                {"Jean Slim Negro", "Básico indispensable, corte slim fit color negro sólido.", "89.90"},
                {"Jean Acid Wash", "Lavado ácido ochentero, corte relajado.", "95.90"},
                {"Jean Recto Clásico", "El corte que nunca pasa de moda, azul medio.", "79.90"}
            }
        );

        // B. CATEGORÍA OUTERWEAR
        crearProductosDetallados("OUTERWEAR", "outerwear", 
            new String[][] {
                {"Top con Botones Rosado", "Top tejido con botones frontales, manga corta.", "45.90"},
                {"Sudadera 'Catch Latte'", "Sudadera blanca oversized con estampado de texto.", "85.90"},
                {"Blusa Hombros Caídos Pink", "Escote Bardot, tejido acanalado suave.", "59.90"},
                {"Sudadera Navy Estampada", "Sudadera azul marino con texto blanco y hombros caídos.", "79.90"},
                {"Crop Top Rayas Baby Blue", "Top corto a rayas horizontales, manga 3/4.", "49.90"},
                {"Camisa Oversize Blanca", "Camisa de botones que puede usarse como chaqueta ligera.", "95.90"},
                {"Chaqueta de Mezclilla Camuflaje", "Cazadora denim con patrón de camuflaje.", "135.90"},
                {"Blusa Hombros Caídos Blanca", "Escote Bardot, tejido ligero.", "59.90"},
                {"Top Crochet Manga Larga", "Top tejido a mano con diseño de flores, ajuste slim.", "89.90"},
                {"Top Acanalado Celeste", "Top de tirantes finos con tejido rib, escote cuadrado.", "39.90"},
                {"Chaqueta Negra con Capucha", "Sudadera con cremallera y capucha, estilo deportivo.", "99.90"},
                {"Jersey Blanco Calado", "Jersey de punto con patrón de encaje, manga larga.", "110.90"},
                {"Sudadera 'Love' con Capucha", "Sudadera beige con estampado frontal y gorro.", "85.90"},
                {"Blazer de Verano Azul Marino", "Chaqueta formal ligera, ideal para un look casual.", "129.90"},
                {"Top Cruzado Beige Tejido", "Top corto tejido a crochet, cierre frontal cruzado.", "75.90"}
            }
        );

        // C. CATEGORÍA NITE OUT
        crearProductosDetallados("NITE OUT", "nite_out", 
            new String[][] {
                {"Vestido Mini Satín Negro", "Vestido corto de tirantes finos, acabado satinado, perfecto para noche.", "135.90"},
                {"Top Halter de Malla Negro", "Top tipo halter, tejido de red con forro, ajuste al cuello.", "79.90"},
                {"Bralette Encaje Vino", "Top de lencería en color vino con detalles de encaje.", "65.90"},
                {"Top Crop de Lino Blanco", "Top básico corto, ideal para combinar con jeans de tiro alto.", "45.90"},
                {"Top con Nudo en Espalda Negro", "Top corto con diseño de lazo o nudo en la espalda.", "69.90"},
                {"Minifalda de Volantes Negra", "Falda corta de tiro alto con mini volantes en el bajo.", "75.90"},
                {"Vestido Tubo Corsé Negro", "Vestido corto ajustado estilo corsé, silueta ceñida.", "149.90"},
                {"Top Bandeau Botones Negro", "Top sin tirantes con botones frontales decorativos, estilo bandeau.", "59.90"},
                {"Vestido Mini Asimétrico Negro", "Vestido corto con un solo tirante, corte ajustado.", "129.90"},
                {"Top Crop Tirantes Spaghetti", "Top corto básico de tirantes muy finos, color negro.", "39.90"},
                {"Top Espalda Descubierta Rosa", "Top corto con escote pronunciado en la espalda, tejido suave.", "55.90"},
                {"Vestido Blazer Vino", "Vestido de corte blazer con solapas, cruzado en color vino.", "179.90"},
                {"Vestido Cut Out con Cadena", "Vestido negro con aberturas y detalle de cadena metálica en el cuello.", "165.90"},
                {"Top Corsé Manga Larga Vino", "Top estilo corsé en tela de red, manga larga y color vino.", "85.90"},
                {"Top Crop Acanalado Gris", "Top corto acanalado de manga corta, cuello redondo.", "42.90"}
            }
        );

        // D. CATEGORÍA POLOS
        crearProductosDetallados("POLOS", "polos", 
            new String[][] {
                {"Polo Gráfico 'Goth'", "Polo negro corto con estampado de calavera/texto en blanco.", "49.90"},
                {"Polo Gráfico '55' Brillante", "Polo crop con gráfico '55' y detalles brillantes en el frente.", "55.90"},
                {"Polo Hombros Caídos Menta", "Polo manga corta, escote Bardot, color verde menta.", "45.90"},
                {"Polo Tirantes Fruncido Amarillo", "Top de tirantes finos con detalle fruncido en el escote, color pastel.", "42.90"},
                {"Polo Hombros Caídos Marrón", "Polo manga corta, escote Bardot, color chocolate.", "45.90"},
                {"Polo Hombros Descubiertos Negro", "Top negro de corte asimétrico con un hombro descubierto.", "47.90"},
                {"Polo Gráfico 'Snake' Negro", "Polo negro holgado con estampado gráfico de serpiente.", "59.90"},
                {"Polo Gráfico 'See You in Rome'", "Polo blanco con estampado de texto y cuello redondo.", "51.90"},
                {"Polo Gráfico 'California'", "Polo blanco con estampado de universidad/destino, corte crop.", "54.90"},
                {"Polo Crop 'Deer' Blanco", "Polo corto blanco con pequeño estampado animal y texto.", "48.90"},
                {"Polo Gráfico 'New York'", "Polo negro con letras en color mostaza, estilo urbano.", "56.90"},
                {"Polo Rib con Ribete Navy", "Polo ajustado acanalado, cuello y mangas con borde en color oscuro.", "43.90"},
                {"Top Asimétrico Blanco Simple", "Top básico de un solo hombro, tirante ancho.", "39.90"},
                {"Polo Gráfico Minimalista", "Polo blanco simple con un pequeño gráfico o texto bordado.", "46.90"},
                {"Polo 'Back Design'", "Polo que destaca por un estampado grande en la espalda.", "59.90"}
            }
        );
        
        // E. CATEGORÍA NEW IN
        crearProductosDetallados("NEW IN", "new_in", 
            new String[][] {
                {"Top Crop Floral", "Top blanco con estampado floral, manga corta y cuello redondo.", "45.90"},
                {"Top de Tirantes Rosa Liso", "Top lencero de tirantes finos, cuello V y ajuste holgado.", "39.90"},
                {"Top Fruncido con Lazo", "Top corto rosa de tirantes con ajuste fruncido frontal y lazo.", "49.90"},
                {"Bralette Negro Básico", "Top corto negro de tirantes anchos, ideal para capas.", "29.90"},
                {"Bralette Negro Tirante Fino", "Top corto negro de tirantes muy finos, ajuste ceñido.", "27.90"},
                {"T-shirt Crop Vintage", "Camiseta corta blanca con estampado de texto en rojo.", "55.90"},
                {"Top Banda de Botones", "Top tipo banda con botones frontales, estilo corset ligero.", "42.90"},
                {"Top Lencero de Espalda Cruzada", "Top de tirantes finos con detalles cruzados en la espalda.", "35.90"},
                {"Minifalda Volantes Azul", "Falda corta con volantes y patrón discreto, estilo juvenil.", "65.90"},
                {"T-shirt Gráfico Rock", "Camiseta negra estampada con motivos musicales/gráficos.", "59.90"},
                {"T-shirt Crop Militar", "Camiseta corta con estampado de camuflaje y cuello redondo.", "57.90"},
                {"Top Hombros Caídos Amarillo", "Top con detalle de volantes en el hombro, color pastel.", "48.90"},
                {"Jeans Rectos Lavado Claro", "Pantalón jean de corte recto, lavado muy claro.", "109.90"},
                {"Top Hombros Descubiertos Marrón", "Top acanalado de manga corta, escote Bardot, color tierra.", "44.90"},
                {"Top Hombros Descubiertos Negro", "Top acanalado de manga corta, escote Bardot, color negro.", "44.90"}
            }
        );

        // F. CATEGORÍA ROPA
        crearProductosDetallados("ROPA", "ropa", 
            new String[][] {
                {"Short de Jean Tiro Alto Claro", "Pantalón corto de mezclilla tiro alto, lavado claro.", "69.90"},
                {"Short de Lino Crema", "Pantalón corto de tela de lino, cordón ajustable en la cintura.", "55.90"},
                {"Short de Jean Clásico Azul", "Pantalón corto de mezclilla, corte recto y tiro medio.", "65.90"},
                {"Short de Jean Ultra Corto", "Pantalón corto de mezclilla muy corto, estilo playero.", "59.90"},
                {"Minifalda Negra de Tubo", "Falda muy corta, ajustada y lisa, ideal para salir.", "75.90"},
                {"Top con Espalda Abierta", "Top básico que destaca por su diseño de lazo o abertura en la espalda.", "49.90"},
                {"Falda Midi Negra", "Falda de largo medio, corte fluido y color negro.", "89.90"},
                {"Top Bandeau con Cadena", "Top strapless negro con detalle de cadena en el abdomen.", "62.90"},
                {"Polo Gráfico 'Rock Music'", "Polo negro con estampado de banda, corte crop.", "59.90"},
                {"Polo Crop Gráfico '55'", "Polo corto blanco con diseño deportivo '55'.", "55.90"},
                {"Top Hombros Caídos Verde Menta", "Top acanalado de manga corta, escote Bardot.", "48.90"},
                {"Top Lencero Drapeado Menta", "Top de tirantes finos con detalle drapeado frontal.", "51.90"},
                {"Top Tejido con Nudo Frontal", "Top de tirantes color rosa viejo, con lazo o nudo ajustable.", "53.90"},
                {"Top Lencero Básico Beige", "Top de tirantes finos, acabado en satín o seda, color nude.", "40.90"},
                {"Polo Crop Estampado Sencillo", "Polo blanco corto con estampado o logo discreto.", "49.90"}
            }
        );
        
        // G. CATEGORÍA ACCESORIOS
        crearProductosDetallados("ACCESORIOS", "accesorios", 
            new String[][] {
                {"Bolso Bandolera Vinotinto", "Bolso pequeño con tachuelas y correa larga.", "69.90"},
                {"Cinturón Estampado Piel", "Cinturón de cuero con diseño de animales.", "45.90"},
                {"Cinturón Delgado Beige", "Cinturón fino de cuero sintético con hebilla dorada.", "35.90"},
                {"Collar Cadena Fina Simple", "Cadena delicada dorada con dije minimalista.", "29.90"},
                {"Minibolso Blanco Cruzado", "Bolso pequeño de hombro, ideal para el día a día.", "59.90"},
                {"Pulsera Cadena Esclava", "Pulsera fina de cadena en plata/acero.", "25.90"},
                {"Collar Cuentas Blancas", "Collar de cuentas pequeñas, estilo veraniego.", "32.90"},
                {"Set Anillos Finos Dorados", "Anillos apilables con diseños sencillos.", "49.90"},
                {"Anillo de Sello Simple", "Anillo grueso con diseño geométrico minimalista.", "39.90"},
                {"Pulsera Trenzada Fina", "Pulsera de hilo con detalle metálico.", "22.90"},
                {"Pendientes Botón Dorado", "Aretes pequeños de botón, elegantes y versátiles.", "19.90"},
                {"Flor para el Cabello (Clavel)", "Accesorio floral para peinados, color amarillo.", "15.90"},
                {"Cinturón Ancho de Piel", "Cinturón grande para ceñir vestidos o camisas.", "55.90"},
                {"Llavero Navaja Rosa", "Llavero decorativo con forma de navaja plegable.", "24.90"},
                {"Gorro Beanie de Punto", "Gorro de lana suave en color crema o beige.", "39.90"}
            }
        );

        // H. CATEGORÍA BLACK SUNDAY
        crearProductosDetallados("BLACK SUNDAY", "rebajas", 
            new String[][] {
                {"Polo Manga Larga Celeste", "Top acanalado de manga larga, cuello redondo y ajuste slim, color pastel.", "49.90"},
                {"Top Hombros Caídos Negro", "Top acanalado de manga larga, escote Bardot, color negro sólido.", "54.90"},
                {"Vestido Maxi Tubo Navy", "Vestido largo ajustado, hombros caídos, ideal para un look casual.", "119.90"},
                {"Chaqueta Corta Denim", "Chaqueta vaquera corta, con botones, estilo juvenil.", "99.90"},
                {"Pantalón Ancho Beige Lino", "Pantalón palazzo de tiro alto, caída fluida, tejido de lino.", "85.90"},
                {"Jean Flare Clásico", "Jean ajustado hasta la rodilla y acampanado en el bajo, lavado azul medio.", "105.90"},
                {"Pantalón Slim Vinotinto", "Pantalón ajustado de color borgoña, ideal para looks de oficina.", "79.90"},
                {"Top Fruncido Manga Larga", "Top blanco acanalado, fruncido en el abdomen con lazos.", "45.90"},
                {"Chaqueta Puffer Azul Marino", "Abrigo acolchado y voluminoso, cuello alto, ideal para invierno.", "149.90"},
                {"Pantalón Campana Negro", "Pantalón ajustado, acampanado, tejido elástico negro.", "89.90"},
                {"Chaqueta Tipo Sudadera Azul", "Chaqueta casual con cremallera y capucha, color azul marino.", "75.90"},
                {"Falda Midi Asimétrica Verde", "Falda larga con volantes y corte asimétrico, estilo bohemio.", "95.90"},
                {"Jean Skinny Etiqueta Roja", "Jean ajustado con etiqueta decorativa visible.", "79.90"},
                {"Minifalda Negra Eco-Cuero", "Falda muy corta de tiro alto, acabado en polipiel.", "69.90"},
                {"Top Hombros Caídos Mostaza", "Top de manga larga con escote Bardot, color amarillo mostaza.", "52.90"}
            }
        );
        
        System.out.println("🎉 Todos los productos de prueba fueron cargados detalladamente. Total: " + productoRepository.count());
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

    private Categoria buscarCategoria(String nombre) {
        return this.todasLasCategorias.stream()
            .filter(c -> c.getNombreCategoria().equals(nombre))
            .findFirst()
            .orElse(null);
    }

    private void crearProductosDetallados(String nombreCategoria, String subDirectorio, String[][] productosData) {
        
        if (!productoRepository.findByCategoria_NombreCategoria(nombreCategoria).isEmpty()) {
            return; 
        }
        
        Categoria categoria = buscarCategoria(nombreCategoria);
        if (categoria == null) {
            System.err.println("❌ ERROR: Categoría '" + nombreCategoria + "' no encontrada.");
            return;
        }
        
        for (int i = 0; i < productosData.length; i++) {
            String[] datos = productosData[i];
            
            Producto p = new Producto();
            p.setNombre(datos[0]);
            p.setDescripcion(datos[1]);
            p.setPrecioVenta(new BigDecimal(datos[2])); 
            p.setStockActual(0); 
            p.setCategoria(categoria);
            p.setProveedor(this.proveedorPrincipal); // <-- ASIGNACIÓN VINCULANTE
            
            String prefijoImagen;
            int numItem = i + 1;
            String carpetaReal = subDirectorio; 

            switch (subDirectorio) {
                case "outerwear": prefijoImagen = "out"; break;
                case "nite_out": prefijoImagen = "nite"; break;
                case "new_in": prefijoImagen = "new"; break;
                case "jeans": prefijoImagen = "jean"; break;
                case "rebajas": prefijoImagen = "rebajas"; break;
                default: prefijoImagen = subDirectorio; break;
            }

            String extension = ".webp"; 
            if (subDirectorio.equals("outerwear") && numItem <= 14) extension = ".jpg"; 
            // ... (resto de tu lógica de extensiones)
            if (subDirectorio.equals("polos") && (numItem == 1 || numItem == 2 || numItem == 3 || numItem == 4 || numItem == 5 || numItem == 7 || numItem == 8 || numItem == 9 || numItem == 11 || numItem == 14)) extension = ".jpg";
            if (subDirectorio.equals("rebajas") && (numItem == 1 || numItem == 2 || numItem == 4 || numItem == 8 || numItem == 10 || numItem == 14)) extension = ".jpg";
            if (subDirectorio.equals("ropa") && numItem <= 4) extension = ".jpg";
            if (subDirectorio.equals("ropa") && numItem >= 6 && numItem <= 14) extension = ".jpg";
            if (subDirectorio.equals("accesorios") && (numItem == 2 || numItem == 3 || numItem == 5 || numItem == 8)) extension = ".jpg";
            if (subDirectorio.equals("nite_out") && (numItem == 2 || numItem == 3 || numItem == 4 || numItem == 5 || numItem == 8 || numItem == 10 || numItem == 11 || numItem == 15)) extension = ".jpg";
            if (subDirectorio.equals("jeans") && (numItem == 3 || numItem == 4)) extension = ".jpg";
            if (subDirectorio.equals("new_in") && numItem >= 3 && numItem <= 14) extension = ".jpg";

            p.setImagenUrl(carpetaReal + "/" + prefijoImagen + numItem + extension); 
            
            productoRepository.save(p);
        }
        System.out.println("✅ Productos de " + nombreCategoria + " insertados.");
    } 
}