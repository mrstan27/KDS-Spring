<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

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

    <form:form action="${pageContext.request.contextPath}/categorias/guardar" method="post" modelAttribute="categoria">
        <form:hidden path="categoriaId"/>

        <div class="form-group">
            <label class="form-label">Nombre de Categoría</label>
            <form:input path="nombreCategoria" cssClass="form-input" required="true" placeholder="Ej: Poleras, Jeans..."/>
        </div>

        <button type="submit" class="btn-registrar">Guardar</button>
        
        <div style="text-align: center; margin-top: 15px;">
            <a href="${pageContext.request.contextPath}/categorias" style="color: #888; text-decoration: none;">Cancelar</a>
        </div>
    </form:form>
</div>

</body>
</html>