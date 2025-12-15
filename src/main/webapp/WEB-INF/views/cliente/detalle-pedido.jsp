<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Detalle Pedido #${venta.ventaId}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body class="bg-light">

    <div class="container mt-5" style="max-width: 800px;">
        
        <div class="mb-3">
            <a href="${pageContext.request.contextPath}/mi-cuenta/pedidos" class="text-decoration-none text-muted">
                <i class="fa-solid fa-arrow-left"></i> Volver a Mis Pedidos
            </a>
        </div>

        <div class="card border-0 shadow-sm">
            <div class="card-header bg-white py-3 border-bottom">
                <div class="d-flex justify-content-between align-items-center">
                    <h5 class="m-0 fw-bold">Detalle Pedido #${venta.ventaId}</h5>
                    <span class="badge bg-success">${venta.estado}</span>
                </div>
            </div>
            <div class="card-body p-4">
                
                <div class="row mb-4 small">
                    <div class="col-md-6 mb-2">
                        <small class="text-muted d-block">Fecha y Hora</small>
                        <strong><fmt:formatDate value="${venta.fechaVenta}" pattern="dd/MM/yyyy HH:mm"/></strong>
                    </div>
                    <div class="col-md-6 mb-2">
                        <small class="text-muted d-block">Cliente</small>
                        <strong>${venta.cliente.nombre} ${venta.cliente.apellido} (${venta.cliente.correo})</strong>
                    </div>
                    <div class="col-md-6 mb-2">
                         <small class="text-muted d-block">Total Pagado</small>
                        <strong class="fs-5 text-danger">S/ ${venta.montoTotal}</strong>
                    </div>
                     <div class="col-md-6 mb-2">
                        <small class="text-muted d-block">Canal de Venta</small>
                        <strong>${venta.tipoVenta}</strong>
                    </div>
                </div>

                <h6 class="text-muted text-uppercase small fw-bold mb-3 border-top pt-3">Productos</h6>
                <div class="table-responsive">
                    <table class="table table-borderless align-middle">
                        <thead class="table-light">
                            <tr>
                                <th>Producto</th>
                                <th class="text-center">Cant.</th>
                                <th class="text-end">Precio Unit.</th>
                                <th class="text-end">Subtotal</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${venta.detalle}" var="d">
                                <tr>
                                    <td>
                                        <div class="d-flex align-items-center">
                                            <img src="${pageContext.request.contextPath}/images/productos/${d.producto.imagenUrl}" 
                                                 class="rounded me-2" style="width: 40px; height: 40px; object-fit: cover;"
                                                 onerror="this.src='https://via.placeholder.com/40'">
                                            <span style="font-size: 0.9em;">${d.producto.nombre}</span>
                                        </div>
                                    </td>
                                    <td class="text-center">${d.cantidad}</td>
                                    <td class="text-end">S/ ${d.precioUnitario}</td>
                                    <td class="text-end fw-bold">S/ ${d.subtotal}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                        <tfoot class="border-top">
                            <tr>
                                <td colspan="3" class="text-end fw-bold">TOTAL PAGADO</td>
                                <td class="text-end fw-bold text-danger">S/ ${venta.montoTotal}</td>
                            </tr>
                        </tfoot>
                    </table>
                </div>

            </div>
            <div class="card-footer bg-white text-center py-3">
                <button class="btn btn-dark rounded-0" onclick="alert('Funcionalidad de impresión simulada.')">
                    <i class="fa-solid fa-print"></i> Imprimir Comprobante
                </button>
            </div>
        </div>
    </div>
</body>
</html>