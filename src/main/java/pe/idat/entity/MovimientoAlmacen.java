package pe.idat.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos_almacen")
public class MovimientoAlmacen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movimiento_id")
    private Integer movimientoId;

    @Column(name = "fecha_movimiento", nullable = false)
    private LocalDateTime fechaMovimiento = LocalDateTime.now();

    @Column(name = "tipo_movimiento", length = 20, nullable = false)
    private String tipoMovimiento; // 'ENTRADA' (Compra) o 'SALIDA' (Venta)

    @Column(name = "motivo", length = 100)
    private String motivo; // Ej: "Compra ID 5", "Venta ID 10"

    // Relación opcional con la Compra (para trazabilidad)
    @OneToOne
    @JoinColumn(name = "compra_id_referencia", nullable = true)
    private Compra compraReferencia;

    // Relación con el usuario que recepcionó
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public MovimientoAlmacen() {}
    
    // Getters y Setters...
    public Integer getMovimientoId() { return movimientoId; }
    public void setMovimientoId(Integer movimientoId) { this.movimientoId = movimientoId; }
    public LocalDateTime getFechaMovimiento() { return fechaMovimiento; }
    public void setFechaMovimiento(LocalDateTime fechaMovimiento) { this.fechaMovimiento = fechaMovimiento; }
    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public Compra getCompraReferencia() { return compraReferencia; }
    public void setCompraReferencia(Compra compraReferencia) { this.compraReferencia = compraReferencia; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}