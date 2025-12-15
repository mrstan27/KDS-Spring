<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Mis Pedidos | KIDS</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    </head>
<body class="bg-light">

    <nav class="navbar navbar-light bg-white border-bottom mb-4">
        <div class="container">
            <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/">
                <i class="fa-solid fa-store text-danger"></i> KIDS MADE HERE
            </a>
            <div class="d-flex align-items-center gap-3">
                <span class="text-muted small">Hola, ${cliente.nombre}</span>
                <a href="${pageContext.request.contextPath}/carrito" class="btn btn-outline-secondary btn-sm me-2">
                    <i class="fa-solid fa-cart-shopping"></i> Carrito
                </a>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-danger btn-sm">Cerrar Sesión</a>
            </div>
        </div>
    </nav>

    <div class="container" style="max-width: 900px;">
        <h3 class="mb-4 fw-bold"><i class="fa-solid fa-box-open"></i> Mi Historial de Pedidos</h3>

        <c:if test="${not empty param.error}">
            <div class="alert alert-danger">${param.error}</div>
        </c:if>

        <c:choose>
            <c:when test="${empty misVentas}">
                <div class="alert alert-info text-center py-5">
                    <i class="fa-regular fa-face-sad-tear fa-2x mb-3"></i><br>
                    Aún no tienes pedidos registrados en tu cuenta.<br>
                    <a href="${pageContext.request.contextPath}/" class="btn btn-danger mt-3 rounded-0">Comenzar a Comprar</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="card border-0 shadow-sm">
                    <div class="list-group list-group-flush">
                        <c:forEach items="${misVentas}" var="v">
                            <div class="list-group-item p-4">
                                <div class="d-flex justify-content-between align-items-center flex-wrap">
                                    <div>
                                        <h5 class="mb-1 fw-bold">Pedido #${v.ventaId}</h5>
                                        <p class="mb-1 text-muted small">
                                            <i class="fa-regular fa-calendar"></i> 
                                            <fmt:formatDate value="${v.fechaVenta}" pattern="dd/MM/yyyy HH:mm"/> &nbsp;|&nbsp; 
                                            Tipo: ${v.tipoVenta}
                                        </p>
                                        <span class="badge bg-success">${v.estado}</span>
                                    </div>
                                    <div class="text-end mt-2 mt-md-0">
                                        <h5 class="fw-bold text-danger mb-2">Total: S/ ${v.montoTotal}</h5>
                                        <a href="${pageContext.request.contextPath}/mi-cuenta/pedidos/detalle/${v.ventaId}" 
                                           class="btn btn-sm btn-outline-primary rounded-0">
                                            Ver Detalles <i class="fa-solid fa-chevron-right"></i>
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>