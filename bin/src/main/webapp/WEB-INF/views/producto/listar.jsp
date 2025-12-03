<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Catálogo de Productos</title>
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
            <h2>Catálogo de Productos</h2>
            <hr class="header-separator">
        </div>
        
        <a href="${pageContext.request.contextPath}/productos/nuevo" class="btn-nuevo">
            <i class="fa-solid fa-shirt"></i> Nuevo Producto
        </a>

        <div class="tabla-container">
            <table class="tabla-estilo">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Imagen</th>
                        <th>Producto</th>
                        <th>Categoría</th>
                        <th>Precio</th>
                        <th>Stock</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${listaProductos}" var="p">
                        <tr>
                            <td>${p.productoId}</td>
                            <td>
                                <c:if test="${not empty p.imagenUrl}">
                                    <img src="${pageContext.request.contextPath}/images/productos/${p.imagenUrl}" 
                                         style="width: 50px; height: 50px; object-fit: cover; border-radius: 5px;">
                                </c:if>
                                <c:if test="${empty p.imagenUrl}">
                                    <span style="color: #ccc;"><i class="fa-solid fa-image"></i></span>
                                </c:if>
                            </td>
                            <td style="font-weight: bold;">${p.nombre}</td>
                            <td>${p.categoria.nombreCategoria}</td>
                            <td style="color: #27ae60;">S/ ${p.precioVenta}</td>
                            <td style="${p.stockActual < 10 ? 'color: red; font-weight: bold;' : ''}">
                                ${p.stockActual} u.
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/productos/editar/${p.productoId}" class="btn-accion btn-editar">
                                    <i class="fa-solid fa-pen"></i>
                                </a>
                                <a href="${pageContext.request.contextPath}/productos/eliminar/${p.productoId}" class="btn-accion btn-eliminar" 
                                   onclick="return confirm('¿Eliminar producto?')">
                                    <i class="fa-solid fa-trash"></i>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        
        <div style="text-align: center; margin-top: 20px;">
            <a href="${pageContext.request.contextPath}/login/menu" style="color: #888; text-decoration: none;">Volver al Menú</a>
        </div>
    </div>
</body>
</html>