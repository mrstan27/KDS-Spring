<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Movimientos de Almacén</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/registro-estilo.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <div class="registro-card wide" style="width: 95%; max-width: 1300px; margin: 30px auto;">
        
        <div style="margin-bottom: 15px;">
            <a href="${pageContext.request.contextPath}/login/menu" style="color: #c0392b; text-decoration: none; font-weight: bold;">
                <i class="fa-solid fa-arrow-left"></i> Volver al Menú
            </a>
        </div>

        <div class="registro-header">
            <h2><i class="fa-solid fa-boxes-stacked"></i> Kardex / Movimientos de Almacén</h2>
            <p class="text-muted">Historial completo de entradas y salidas de inventario.</p>
            <hr class="header-separator">
        </div>

        <div class="tabla-container">
            <table class="tabla-estilo table-hover table-striped">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Fecha y Hora</th>
                        <th>Tipo</th>
                        <th>Responsable</th>
                        <th>Motivo / Referencia</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${listaMovimientos}" var="m">
                        <tr>
                            <td>#${m.movimientoId}</td>
                            <td>
                                <%-- Formato de fecha legible --%>
                                ${m.fechaMovimiento.toLocalDate()} <br>
                                <small class="text-muted">${m.fechaMovimiento.toLocalTime().toString().substring(0,8)}</small>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${m.tipoMovimiento == 'ENTRADA'}">
                                        <span class="badge bg-success"><i class="fa-solid fa-arrow-down"></i> ENTRADA</span>
                                    </c:when>
                                    <c:when test="${m.tipoMovimiento == 'SALIDA'}">
                                        <span class="badge bg-danger"><i class="fa-solid fa-arrow-up"></i> SALIDA</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-secondary">${m.tipoMovimiento}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                ${m.usuario.nombre} ${m.usuario.apellido}<br>
                                <small class="text-muted">(${m.usuario.rol.nombreRol})</small>
                            </td>
                            <td>
                                ${m.motivo}
                                <c:if test="${not empty m.compraReferencia}">
                                    <br><small class="text-primary">Ref: Orden #${m.compraReferencia.compraId}</small>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    
                    <c:if test="${empty listaMovimientos}">
                        <tr>
                            <td colspan="5" class="text-center p-4">
                                <i class="fa-solid fa-box-open fa-2x text-muted mb-2"></i><br>
                                No hay movimientos registrados aún.
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>

</body>
</html>