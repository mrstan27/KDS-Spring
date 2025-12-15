<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Tu Carrito | KIDS</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body class="bg-light">

    <nav class="navbar navbar-light bg-white border-bottom">
        <div class="container">
            <a class="navbar-brand text-decoration-none text-dark fw-bold" href="${pageContext.request.contextPath}/">
                <i class="fa-solid fa-chevron-left"></i> SEGUIR COMPRANDO
            </a>
            <span class="fw-bold">CARRITO DE COMPRAS</span>
        </div>
    </nav>

    <div class="container mt-5">
        <c:if test="${not empty success}">
            <div class="alert alert-success py-2">${success}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-danger py-2">${error}</div>
        </c:if>

        <c:choose>
            <c:when test="${empty carrito}">
                <div class="text-center py-5">
                    <i class="fa-solid fa-cart-shopping fa-3x text-muted mb-3"></i>
                    <h3>Tu carrito está vacío</h3>
                    <a href="${pageContext.request.contextPath}/" class="btn btn-dark mt-3 rounded-0 px-4">IR A LA TIENDA</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="row">
                    <div class="col-md-8">
                        <div class="card shadow-sm border-0">
                            <div class="card-body p-0">
                                <table class="table mb-0">
                                    <thead class="bg-white">
                                        <tr>
                                            <th class="ps-4">PRODUCTO</th>
                                            <th>PRECIO</th>
                                            <th>CANT.</th>
                                            <th>TOTAL</th>
                                            <th></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${carrito}" var="item">
                                            <tr class="align-middle">
                                                <td class="ps-4">
                                                    <div class="d-flex align-items-center">
                                                        <img src="${pageContext.request.contextPath}/images/productos/${item.imagenUrl}" 
                                                             style="width: 60px; height: 75px; object-fit: cover;" class="me-3 bg-light"
                                                             onerror="this.src='https://via.placeholder.com/60x75'">
                                                        <span class="fw-bold text-uppercase" style="font-size: 0.9rem;">${item.nombre}</span>
                                                    </div>
                                                </td>
                                                <td>S/ ${item.precio}</td>
                                                <td>${item.cantidad}</td>
                                                <td class="fw-bold">S/ ${item.subtotal}</td>
                                                <td>
                                                    <a href="${pageContext.request.contextPath}/carrito/eliminar/${item.productoId}" class="text-secondary hover-danger">
                                                        <i class="fa-solid fa-xmark"></i>
                                                    </a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="card shadow-sm border-0">
                            <div class="card-body p-4">
                                <h5 class="card-title fw-bold mb-4">RESUMEN</h5>
                                <div class="d-flex justify-content-between mb-2">
                                    <span>Subtotal</span>
                                    <span>S/ ${total}</span>
                                </div>
                                <div class="d-flex justify-content-between mb-3">
                                    <span>Envío</span>
                                    <span class="text-success fw-bold">GRATIS</span>
                                </div>
                                <hr>
                                <div class="d-flex justify-content-between mb-4 fs-5 fw-bold">
                                    <span>TOTAL</span>
                                    <span>S/ ${total}</span>
                                </div>
                                <a href="${pageContext.request.contextPath}/carrito/checkout" class="btn btn-dark w-100 py-3 rounded-0 fw-bold">
                                    PROCESAR COMPRA
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>