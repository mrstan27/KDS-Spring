<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestión de Producto</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body class="bg-light">
    <div class="container mt-5">
        <div class="card shadow-sm">
            <div class="card-header bg-dark text-white d-flex justify-content-between align-items-center">
                <h4 class="mb-0">
                    <i class="fa-solid ${producto.productoId != null ? 'fa-pen-to-square' : 'fa-plus-circle'}"></i> 
                    ${producto.productoId != null ? 'Editar Producto' : 'Registrar Nuevo Producto'}
                </h4>
                <a href="${pageContext.request.contextPath}/productos" class="btn btn-sm btn-light">Volver</a>
            </div>
            <div class="card-body p-4">
                
                <form action="${pageContext.request.contextPath}/productos/guardar" method="post" enctype="multipart/form-data">
                    
                    <input type="hidden" name="productoId" value="${producto.productoId}">
                    
                    <%-- IMPORTANTE: Mantener valores existentes al editar --%>
                    <c:if test="${producto.productoId != null}">
                        <input type="hidden" name="stockActual" value="${producto.stockActual}">
                        <input type="hidden" name="imagenUrl" value="${producto.imagenUrl}">
                        
                        <div class="alert alert-info py-2 mb-4">
                             <i class="fa-solid fa-info-circle"></i> Editando: <strong>${producto.nombre}</strong> (Stock actual: ${producto.stockActual})
                        </div>
                    </c:if>

                    <div class="row">
                        <div class="col-md-8 mb-3">
                            <label class="form-label fw-bold">Nombre de la Prenda:</label>
                            <input type="text" name="nombre" value="${producto.nombre}" class="form-control" required placeholder="Ej. Polo Piqué Rojo">
                        </div>
                        <div class="col-md-4 mb-3">
                            <label class="form-label fw-bold">Precio Venta (S/):</label>
                            <div class="input-group">
                                <span class="input-group-text">S/</span>
                                <input type="number" name="precioVenta" value="${producto.precioVenta}" step="0.01" class="form-control" required>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label fw-bold">Categoría:</label>
                            <select name="categoriaId" class="form-select" required>
                                <option value="">-- Seleccione --</option>
                                <c:forEach items="${listaCategorias}" var="cat">
                                    <option value="${cat.categoriaId}" ${producto.categoria.categoriaId == cat.categoriaId ? 'selected' : ''}>
                                        ${cat.nombreCategoria}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        
                        <div class="col-md-6 mb-3">
                            <label class="form-label fw-bold text-primary">Proveedor (Origen):</label>
                            <select name="proveedorId" class="form-select" required>
                                <option value="">-- Seleccione Fabricante --</option>
                                <c:forEach items="${listaProveedores}" var="prov">
                                    <option value="${prov.proveedorId}" ${producto.proveedor.proveedorId == prov.proveedorId ? 'selected' : ''}>
                                        ${prov.razonSocial} (Rubro: ${prov.rubro})
                                    </option>
                                </c:forEach>
                            </select>
                            <div class="form-text">Define qué proveedor suministra este producto para las cotizaciones.</div>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Imagen:</label>
                        <input type="file" name="file" class="form-control" accept="image/*">
                        <c:if test="${producto.imagenUrl != null}">
                            <small class="text-muted">Imagen actual: ${producto.imagenUrl}</small>
                        </c:if>
                    </div>
                    
                    <div class="mb-3">
                        <label class="form-label">Descripción Detallada:</label>
                        <textarea name="descripcion" class="form-control" rows="3">${producto.descripcion}</textarea>
                    </div>

                    <div class="d-grid gap-2 d-md-flex justify-content-md-end mt-4">
                        <a href="${pageContext.request.contextPath}/productos" class="btn btn-secondary me-md-2">Cancelar</a>
                        <button type="submit" class="btn btn-primary px-5"><i class="fa-solid fa-save"></i> Guardar</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
</html>