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

    @Column(name = "fecha_movimiento")
    private LocalDateTime fechaMovimiento;

    @Column(name = "tipo_movimiento")
    private String tipoMovimiento; // ENTRADA, SALIDA

    @Column(name = "motivo")
    private String motivo;

    // CAMBIO CRÍTICO: nullable = true para permitir movimientos automáticos del sistema (Web)
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = true) 
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "compra_id", nullable = true)
    private Compra compraReferencia;

    // Getters y Setters
    public Integer getMovimientoId() { return movimientoId; }
    public void setMovimientoId(Integer movimientoId) { this.movimientoId = movimientoId; }
    public LocalDateTime getFechaMovimiento() { return fechaMovimiento; }
    public void setFechaMovimiento(LocalDateTime fechaMovimiento) { this.fechaMovimiento = fechaMovimiento; }
    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Compra getCompraReferencia() { return compraReferencia; }
    public void setCompraReferencia(Compra compraReferencia) { this.compraReferencia = compraReferencia; }
}