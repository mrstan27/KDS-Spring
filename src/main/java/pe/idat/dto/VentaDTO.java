package pe.idat.dto;

import java.math.BigDecimal;
import java.util.List;

public class VentaDTO {
    
    private Integer clienteId;
    private BigDecimal total;
    private List<DetalleDTO> items; // Reutilizamos la clase DetalleDTO que ya tienes para Compras

    public VentaDTO() {
    }

    // Getters y Setters
    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<DetalleDTO> getItems() {
        return items;
    }

    public void setItems(List<DetalleDTO> items) {
        this.items = items;
    }
}