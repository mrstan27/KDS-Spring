package pe.idat.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "proveedores")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proveedor_id")
    private Integer proveedorId;

    @Column(name = "razon_social", nullable = false)
    private String razonSocial;

    @Column(name = "ruc", nullable = false, unique = true)
    private String ruc;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "correo")
    private String correo;
    
    @Column(name = "rubro")
    private String rubro; // <-- CAMPO AGREGADO

    @Column(name = "estado")
    private String estado; // ACTIVO, INACTIVO

    public Proveedor() {
    }

    // Getters y Setters
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
}