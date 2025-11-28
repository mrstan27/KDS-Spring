<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${titulo}</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/registro-estilo.css">
</head>
<body>

<div class="registro-card">

    <div class="registro-header">
        <h2>${titulo}</h2>
        <hr class="header-separator">
    </div>

    <form:form action="${pageContext.request.contextPath}/productos/guardar" 
               method="post" 
               modelAttribute="producto" 
               enctype="multipart/form-data">
        
        <form:hidden path="productoId"/>
        <form:hidden path="imagenUrl"/>

        <div class="form-group">
            <label class="form-label">Nombre del Producto</label>
            <form:input path="nombre" cssClass="form-input" required="true"/>
        </div>

        <div class="form-group">
            <label class="form-label">Descripción</label>
            <form:textarea path="descripcion" cssClass="form-input" rows="3"/>
        </div>

        <div class="form-group">
            <label class="form-label">Categoría</label>
            <form:select path="categoria.categoriaId" cssClass="form-select" required="true">
                <form:option value="" label="Seleccione..."/>
                <form:options items="${listaCategorias}" itemValue="categoriaId" itemLabel="nombreCategoria"/>
            </form:select>
        </div>

        <div class="form-group">
            <label class="form-label">Precio de Venta (S/)</label>
            <form:input path="precioVenta" cssClass="form-input" type="number" step="0.01" required="true"/>
        </div>

        <div class="form-group">
            <label class="form-label">Stock Inicial</label>
            <form:input path="stockActual" cssClass="form-input" type="number" required="true"/>
        </div>
        
        <div class="form-group">
            <label class="form-label">Imagen del Producto</label>
            <input type="file" name="file" class="form-input" accept="image/*"/>
        </div>

        <button type="submit" class="btn-registrar">Guardar Producto</button>
        
        <div style="text-align: center; margin-top: 15px;">
            <a href="${pageContext.request.contextPath}/productos" style="color: #888; text-decoration: none;">Cancelar</a>
        </div>
        
    </form:form>
</div>

</body>
</html>