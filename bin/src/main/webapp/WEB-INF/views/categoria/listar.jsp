<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestión de Categorías</title>
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
            <h2>Gestión de Categorías</h2>
            <hr class="header-separator">
        </div>

        <a href="${pageContext.request.contextPath}/categorias/nuevo" class="btn-nuevo">
            <i class="fa-solid fa-tags"></i> Nueva Categoría
        </a>

        <div class="tabla-container">
            <table class="tabla-estilo">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nombre de Categoría</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${listaCategorias}" var="c">
                        <tr>
                            <td>${c.categoriaId}</td>
                            <td style="font-weight: bold;">${c.nombreCategoria}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/categorias/editar/${c.categoriaId}" class="btn-accion btn-editar">
                                    <i class="fa-solid fa-pen"></i>
                                </a>
                                <a href="${pageContext.request.contextPath}/categorias/eliminar/${c.categoriaId}" class="btn-accion btn-eliminar"
                                   onclick="return confirm('¿Eliminar esta categoría?')">
                                    <i class="fa-solid fa-trash"></i>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>