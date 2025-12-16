<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Menú Principal</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/menu-estilo.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <div class="dashboard-header">
        <div class="user-info">
            <h2>Hola, ${usuario.nombre} ${usuario.apellido}</h2>
            <span>Rol: <strong>${usuario.rol.nombreRol}</strong></span>
        </div>
        <a href="${pageContext.request.contextPath}/logout" class="btn-logout">
            <i class="fa-solid fa-power-off"></i> Cerrar Sesión
        </a>
    </div>

    <div class="menu-grid">
        
        <%-- 1. GESTIÓN DE USUARIOS (Solo Admin) --%>
        <c:if test="${usuario.rol.nombreRol == 'Administrador'}">
            <a href="${pageContext.request.contextPath}/usuarios" class="card-menu">
                <div class="card-icon"><i class="fa-solid fa-users-gear"></i></div>
                <div class="card-title">Usuarios</div>
                <div class="card-desc">Gestionar accesos y cuentas</div>
            </a>
            
            <a href="${pageContext.request.contextPath}/roles" class="card-menu">
                <div class="card-icon"><i class="fa-solid fa-id-badge"></i></div>
                <div class="card-title">Roles</div>
                <div class="card-desc">Configurar permisos</div>
            </a>
        </c:if>

        <%-- 2. GESTIÓN COMERCIAL (Admin y Compras) --%>
        <c:if test="${usuario.rol.nombreRol == 'Administrador' || usuario.rol.nombreRol == 'Compras'}">
            <a href="${pageContext.request.contextPath}/productos" class="card-menu">
                <div class="card-icon"><i class="fa-solid fa-shirt"></i></div>
                <div class="card-title">Productos</div>
                <div class="card-desc">Catálogo maestro</div>
            </a>
            
            <a href="${pageContext.request.contextPath}/categorias" class="card-menu">
                <div class="card-icon"><i class="fa-solid fa-tags"></i></div>
                <div class="card-title">Categorías</div>
                <div class="card-desc">Familias de prendas</div>
            </a>

            <a href="${pageContext.request.contextPath}/proveedor/lista" class="card-menu">
                <div class="card-icon"><i class="fa-solid fa-truck"></i></div>
                <div class="card-title">Proveedores</div>
                <div class="card-desc">Cartera de fabricantes</div>
            </a>
            
            <a href="${pageContext.request.contextPath}/compras/cotizaciones" class="card-menu">
                <div class="card-icon"><i class="fa-solid fa-file-invoice-dollar"></i></div>
                <div class="card-title">Cotizaciones</div>
                <div class="card-desc">Generar pedidos a proveedor</div>
            </a>
        </c:if>

        <%-- 3. ALMACÉN Y RECEPCIÓN (Admin y Almacenero) --%>
        <c:if test="${usuario.rol.nombreRol == 'Administrador' || usuario.rol.nombreRol == 'Almacenero'}">
            <a href="${pageContext.request.contextPath}/compras" class="card-menu">
                <div class="card-icon"><i class="fa-solid fa-boxes-packing"></i></div>
                <div class="card-title">Recepcionar Pedidos</div>
                <div class="card-desc">Ingresar stock de facturas</div>
            </a>

            <a href="${pageContext.request.contextPath}/movimientos/listar" class="card-menu">
                <div class="card-icon"><i class="fa-solid fa-dolly"></i></div>
                <div class="card-title">Kardex</div>
                <div class="card-desc">Historial de movimientos</div>
            </a>
        </c:if>

        <%-- 4. VENTAS (Admin y Vendedor) --%>
        <c:if test="${usuario.rol.nombreRol == 'Administrador' || usuario.rol.nombreRol == 'Vendedor'}">
            <a href="${pageContext.request.contextPath}/ventas/nueva" class="card-menu">
                <div class="card-icon"><i class="fa-solid fa-cash-register"></i></div>
                <div class="card-title">Punto de Venta</div>
                <div class="card-desc">Venta física en tienda</div>
            </a>
            
            <a href="${pageContext.request.contextPath}/cliente/listar" class="card-menu">
                <div class="card-icon"><i class="fa-solid fa-user-group"></i></div>
                <div class="card-title">Clientes</div>
                <div class="card-desc">Cartera de clientes</div>
            </a>
        </c:if>

        <%-- 5. REPORTES (Admin, Vendedor, Almacenero) --%>
        <c:if test="${usuario.rol.nombreRol == 'Administrador' || usuario.rol.nombreRol == 'Almacenero' || usuario.rol.nombreRol == 'Vendedor'}">
            <a href="${pageContext.request.contextPath}/reportes" class="card-menu">
                <div class="card-icon">
                    <i class="fa-solid fa-file-pdf"></i>
                </div>
                <div class="card-title">Reportes</div>
                <div class="card-desc">Ventas y Stock PDF</div>
            </a>
        </c:if>

        <%-- 6. SISTEMA BACKUP (Admin y Soporte) --%>
        <c:if test="${usuario.rol.nombreRol == 'Administrador' || usuario.rol.nombreRol == 'Soporte'}">
            <a href="${pageContext.request.contextPath}/backup/exportar" class="card-menu" style="border-left: 5px solid #27ae60;">
                <div class="card-icon" style="color: #27ae60;"><i class="fa-solid fa-download"></i></div>
                <div class="card-title">Backup</div>
                <div class="card-desc">Descargar copia .sql</div>
            </a>

            <a href="${pageContext.request.contextPath}/backup/restaurar" class="card-menu" style="border-left: 5px solid #e74c3c;">
                <div class="card-icon" style="color: #e74c3c;"><i class="fa-solid fa-rotate-left"></i></div>
                <div class="card-title">Restaurar</div>
                <div class="card-desc">Recuperar datos antiguos</div>
            </a>
        </c:if>

    </div>

</body>
</html>