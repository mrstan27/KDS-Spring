package pe.idat.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoria_id")
    private Integer categoriaId;

    @Column(name = "nombre_categoria", length = 100, nullable = false)
    private String nombreCategoria;

    @OneToMany(mappedBy = "categoria")
    private List<Producto> productos;

    public Categoria() {}

    public Categoria(Integer categoriaId, String nombreCategoria) {
        this.categoriaId = categoriaId;
        this.nombreCategoria = nombreCategoria;
    }

    public Integer getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Integer categoriaId) { this.categoriaId = categoriaId; }

    public String getNombreCategoria() { return nombreCategoria; }
    public void setNombreCategoria(String nombreCategoria) { this.nombreCategoria = nombreCategoria; }
    
    public List<Producto> getProductos() { return productos; }
    public void setProductos(List<Producto> productos) { this.productos = productos; }
}