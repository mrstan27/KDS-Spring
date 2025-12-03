<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestión de Cotizaciones</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/registro-estilo.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <div class="registro-card wide">
        <div style="margin-bottom: 15px;">
            <a href="${pageContext.request.contextPath}/login/menu" style="color: #c0392b; text-decoration: none; font-weight: bold;">
                <i class="fa-solid fa-arrow-left"></i> Volver al Menú
            </a>
        </div>

        <div class="registro-header">
            <h2>Cotizaciones de Proveedores</h2>
            <hr class="header-separator">
        </div>
        
        <a href="${pageContext.request.contextPath}/compras/cotizaciones/nueva" class="btn-nuevo" style="background-color: #3498db;">
            <i class="fa-solid fa-file-signature"></i> Nueva Cotización
        </a>

        <div class="tabla-container">
            <table class="tabla-estilo">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Fecha</th>
                        <th>Proveedor</th>
                        <th>Monto Propuesto</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${listaCotizaciones}" var="c">
                        <tr>
                            <td><b>#${c.compraId}</b></td>
                            <td>${c.fechaCompra.toLocalDate()}</td>
                            <td>${c.proveedor.razonSocial}</td>
                            <td style="font-weight: bold;">S/ ${c.montoTotal}</td>
                            <td><span class="badge bg-secondary" style="background:#95a5a6; color:white; padding:4px; border-radius:4px;">Borrador</span></td>
                            <td>
                                <a href="${pageContext.request.contextPath}/compras/cotizaciones/aprobar/${c.compraId}" 
                                   class="btn-accion" style="background-color: #2ecc71; color: white; text-decoration: none; padding: 5px 10px; border-radius: 5px;"
                                   onclick="return confirm('¿Aprobar esta cotización y convertirla en Orden de Compra?')">
                                    <i class="fa-solid fa-check"></i> Aprobar
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <c:if test="${empty listaCotizaciones}">
                <div style="text-align: center; padding: 20px; color: #777;">No hay cotizaciones pendientes.</div>
            </c:if>
        </div>
    </div>
</body>
</html>