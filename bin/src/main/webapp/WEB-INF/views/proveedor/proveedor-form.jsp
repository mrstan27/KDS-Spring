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
        <div style="color: red; text-align:center; margin-bottom: 15px;">
            ${error}
        </div>
    </c:if>

    <form:form action="${pageContext.request.contextPath}/proveedor/guardar" 
                method="post" 
                modelAttribute="proveedor">

        <form:hidden path="proveedorId"/>

        <div class="form-group">
            <label class="form-label">Razón Social</label>
            <form:input path="razonSocial" cssClass="form-input" required="true"/>
        </div>

        <div class="form-group">
            <label class="form-label">RUC</label>
            <form:input path="ruc" cssClass="form-input" maxlength="11" required="true"/>
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
            <form:input path="correo" cssClass="form-input" type="email"/>
        </div>

        <div class="form-group">
            <label class="form-label">Rubro</label>
            <form:input path="rubro" cssClass="form-input"/>
        </div>

        <button type="submit" class="btn-registrar">
            ${proveedor.proveedorId != null ? 'Actualizar' : 'Registrar'}
        </button>

    </form:form>

</div>

</body>
</html>
