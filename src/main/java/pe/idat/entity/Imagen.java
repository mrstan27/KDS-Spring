package pe.idat.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "imagenes")
public class Imagen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // ID propio de la imagen

    @Column(name = "url", nullable = false)
    private String url;

    // RELACIÓN: Muchas imágenes pertenecen a Un Producto
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false) // Esta será la llave foránea en la BD
    private Producto producto;

    public Imagen() {}

    // Constructor útil para guardar rápido
    public Imagen(String url, Producto producto) {
        this.url = url;
        this.producto = producto;
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
}