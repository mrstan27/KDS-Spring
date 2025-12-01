package pe.idat.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List; // Importante: Necesario para manejar la lista de imágenes

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "producto_id")
    private Integer productoId;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "precio_venta", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioVenta;

    @Column(name = "stock_actual", nullable = false)
    private Integer stockActual;

    // Esta queda como tu "Foto de Portada" (la que se ve en el catálogo)
    @Column(name = "imagen_url", length = 255)
    private String imagenUrl;

    // Relación con Categoría (Muchos productos -> Una Categoría)
    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    // NUEVO: Galería de fotos adicionales
    // "mappedBy" indica que la dueña de la relación es la clase Imagen (campo 'producto')
    // CascadeType.ALL permite que si borras el producto, se borren sus fotos automáticamente.
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Imagen> imagenes;

    // Constructor vacío requerido por JPA
    public Producto() {}

    // --- GETTERS Y SETTERS ---

    public Integer getProductoId() { return productoId; }
    public void setProductoId(Integer productoId) { this.productoId = productoId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(BigDecimal precioVenta) { this.precioVenta = precioVenta; }

    public Integer getStockActual() { return stockActual; }
    public void setStockActual(Integer stockActual) { this.stockActual = stockActual; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    // Getters y Setters de la nueva lista
    public List<Imagen> getImagenes() { return imagenes; }
    public void setImagenes(List<Imagen> imagenes) { this.imagenes = imagenes; }
}