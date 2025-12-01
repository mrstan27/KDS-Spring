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

    <c:if test="${not empty error}">
        <div style="color:red; text-align:center; margin-bottom:15px;">
            ${error}
        </div>
    </c:if>

    <form:form action="${pageContext.request.contextPath}/cliente/guardar"
               method="post"
               modelAttribute="cliente">

        <form:hidden path="clienteId"/>

        <div class="form-group">
            <label class="form-label">Nombre</label>
            <form:input path="nombre" cssClass="form-input" required="true"/>
        </div>

        <div class="form-group">
            <label class="form-label">Apellido</label>
            <form:input path="apellido" cssClass="form-input" required="true"/>
        </div>

        <div class="form-group">
            <label class="form-label">Tipo Documento</label>
            <form:select path="tipoDocumento" cssClass="form-input">
                <form:option value="">Seleccione</form:option>
                <form:option value="DNI">DNI</form:option>
                <form:option value="CE">CE</form:option>
                <form:option value="PAS">Pasaporte</form:option>
            </form:select>
        </div>

        <div class="form-group">
            <label class="form-label">Número Documento</label>
            <form:input path="numeroDocumento" cssClass="form-input"/>
        </div>

        <div class="form-group">
            <label class="form-label">Dirección</label>
            <form:input path="direccion" cssClass="form-input"/>
        </div>

        <div class="form-group">
            <label class="form-label">Teléfono</label>
            <form:input path="telefono" cssClass="form-input"/>
        </div>

        <div class="form-group">
            <label class="form-label">Correo</label>
            <form:input path="correo" cssClass="form-input" type="email" required="true"/>
        </div>

        <div class="form-group">
            <label class="form-label">Crear Contraseña</label>
            <form:password path="password" cssClass="form-input" required="true" placeholder="******"/>
        </div>

        <button type="submit" class="btn-registrar">
            ${cliente.clienteId != null ? 'Actualizar' : 'Registrar'}
        </button>

    </form:form>

</div>

</body>
</html>
