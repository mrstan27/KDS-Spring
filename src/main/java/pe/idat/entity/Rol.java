package pe.idat.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "roles")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rol_id")
    private Integer rolId;

    @Column(name = "nombre_rol", length = 50, nullable = false)
    private String nombreRol;

    @OneToMany(mappedBy = "rol")
    private List<Usuario> usuarios;

    public Rol() {
    }

    public Rol(Integer rolId, String nombreRol) {
        this.rolId = rolId;
        this.nombreRol = nombreRol;
    }

    // GETTERS Y SETTERS (Obligatorios para que funcione JPA)
    public Integer getRolId() { return rolId; }
    public void setRolId(Integer rolId) { this.rolId = rolId; }

    public String getNombreRol() { return nombreRol; }
    public void setNombreRol(String nombreRol) { this.nombreRol = nombreRol; }

    public List<Usuario> getUsuarios() { return usuarios; }
    public void setUsuarios(List<Usuario> usuarios) { this.usuarios = usuarios; }
}