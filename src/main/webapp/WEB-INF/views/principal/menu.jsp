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

        <%-- 1. GESTIÓN DE USUARIOS Y ROLES (Solo Admin) --%>
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

        <%-- 2. COMPRAS Y COTIZACIONES (Admin y Compras) --%>
        <%-- Nota: Almacenero NO entra aquí, él solo recepciona --%>
        <c:if test="${usuario.rol.nombreRol == 'Administrador' || usuario.rol.nombreRol == 'Compras' || usuario.rol.nombreRol == 'Almacenero'}">
             <a href="${pageContext.request.contextPath}/compras" class="card-menu">
                <div class="card-icon"><i class="fa-solid fa-cart-shopping"></i></div>
                <div class="card-title">Ordenes de Compra</div>
                <div class="card-desc">Seguimiento y Recepción</div>
            </a>
        </c:if>
        
        <c:if test="${usuario.rol.nombreRol == 'Administrador' || usuario.rol.nombreRol == 'Compras'}">
            <a href="${pageContext.request.contextPath}/compras/cotizaciones" class="card-menu">
                <div class="card-icon"><i class="fa-solid fa-file-contract"></i></div>
                <div class="card-title">Cotizaciones</div>
                <div class="card-desc">Generar nuevos pedidos</div>
            </a>
        </c:if>

        <%-- 3. PRODUCTOS Y PROVEEDORES --%>
        <c:if test="${usuario.rol.nombreRol == 'Administrador' || usuario.rol.nombreRol == 'Almacenero' || usuario.rol.nombreRol == 'Compras'}">
            <a href="${pageContext.request.contextPath}/productos" class="card-menu">
                <div class="card-icon"><i class="fa-solid fa-shirt"></i></div>
                <div class="card-title">Productos</div>
                <div class="card-desc">Catálogo e Inventario</div>
            </a>

            <a href="${pageContext.request.contextPath}/proveedor/lista" class="card-menu">
                <div class="card-icon"><i class="fa-solid fa-truck"></i></div>
                <div class="card-title">Proveedores</div>
                <div class="card-desc">Cartera de proveedores</div>
            </a>
        </c:if>
        
        <%-- 4. MOVIMIENTOS (Solo Admin y Almacenero) --%>
        <c:if test="${usuario.rol.nombreRol == 'Administrador' || usuario.rol.nombreRol == 'Almacenero'}">
            <a href="${pageContext.request.contextPath}/movimientos" class="card-menu">
                <div class="card-icon"><i class="fa-solid fa-boxes-stacked"></i></div>
                <div class="card-title">Kardex / Movimientos</div>
                <div class="card-desc">Historial de Entradas/Salidas</div>
            </a>
        </c:if>

        <%-- 5. VENTAS Y CLIENTES (Admin y Vendedor) --%>
        <c:if test="${usuario.rol.nombreRol == 'Administrador' || usuario.rol.nombreRol == 'Vendedor'}">
            <a href="#" class="card-menu">
                <div class="card-icon"><i class="fa-solid fa-cash-register"></i></div>
                <div class="card-title">Punto de Venta</div>
                <div class="card-desc">Registrar salida de mercadería</div>
            </a>

            <a href="${pageContext.request.contextPath}/cliente/listar" class="card-menu">
                <div class="card-icon"><i class="fa-solid fa-user-group"></i></div>
                <div class="card-title">Clientes</div>
                <div class="card-desc">Cartera de clientes</div>
            </a>
        </c:if>

        <%-- 6. SOPORTE / BACKUP (Admin y Soporte) --%>
        <c:if test="${usuario.rol.nombreRol == 'Administrador' || usuario.rol.nombreRol == 'Soporte'}">
            <a href="#" class="card-menu" style="border-left: 5px solid #e74c3c;">
                <div class="card-icon"><i class="fa-solid fa-database"></i></div>
                <div class="card-title">Backup</div>
                <div class="card-desc">Copia de seguridad del sistema</div>
            </a>
        </c:if>

    </div>

</body>
</html>