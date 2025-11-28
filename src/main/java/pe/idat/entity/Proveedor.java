package pe.idat.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "proveedores")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proveedor_id")
    private Integer proveedorId;

    @Column(name = "razon_social", length = 100, nullable = false)
    private String razonSocial;

    @Column(name = "ruc", length = 11, nullable = false, unique = true)
    private String ruc;

    @Column(name = "direccion", length = 255)
    private String direccion;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "correo", length = 100)
    private String correo;

    @Column(name = "rubro", length = 100)
    private String rubro;

    @Column(name = "estado", length = 20)
    private String estado = "Activo";  // valor inicial

    // Relación con compras
    //@OneToMany(mappedBy = "proveedor")
    //private List<CompraCabecera> compras;

    public Proveedor() {}

    public Integer getProveedorId() { return proveedorId; }
    public void setProveedorId(Integer proveedorId) { this.proveedorId = proveedorId; }

    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }

    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getRubro() { return rubro; }
    public void setRubro(String rubro) { this.rubro = rubro; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    //public List<CompraCabecera> getCompras() { return compras; }
    //public void setCompras(List<CompraCabecera> compras) { this.compras = compras; }
}
