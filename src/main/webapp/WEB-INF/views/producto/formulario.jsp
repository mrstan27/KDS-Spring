<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Producto</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <div class="card">
            <div class="card-header bg-dark text-white">
                <h4>Registro de Producto (Catálogo)</h4>
            </div>
            <div class="card-body">
                
                <form action="${pageContext.request.contextPath}/productos/guardar" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="productoId" value="${producto.productoId}">
                    
                    <c:if test="${producto.productoId != null}">
                        <input type="hidden" name="stockActual" value="${producto.stockActual}">
                    </c:if>

                    <div class="mb-3">
                        <label>Nombre de Prenda:</label>
                        <input type="text" name="nombre" value="${producto.nombre}" class="form-control" required>
                    </div>

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label>Categoría:</label>
                            <select name="categoriaId" class="form-select" required>
                                <c:forEach items="${listaCategorias}" var="cat">
                                    <option value="${cat.categoriaId}" ${producto.categoria.categoriaId == cat.categoriaId ? 'selected' : ''}>
                                        ${cat.nombreCategoria}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        
                        <div class="col-md-6 mb-3">
                            <label class="text-danger fw-bold">Proveedor (Fabricante):</label>
                            <select name="proveedorId" class="form-select" required>
                                <option value="">-- Seleccione Quién lo Fabrica --</option>
                                <c:forEach items="${listaProveedores}" var="prov">
                                    <option value="${prov.proveedorId}" ${producto.proveedor.proveedorId == prov.proveedorId ? 'selected' : ''}>
                                        ${prov.razonSocial}
                                    </option>
                                </c:forEach>
                            </select>
                            <small class="text-muted">Este producto solo podrá comprarse a este proveedor.</small>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label>Precio Venta (Público):</label>
                            <input type="number" name="precioVenta" value="${producto.precioVenta}" step="0.01" class="form-control" required>
                        </div>
                        
                        <div class="col-md-6 mb-3">
                            <label>Imagen:</label>
                            <input type="file" name="file" class="form-control">
                        </div>
                    </div>
                    
                    <div class="mb-3">
                        <label>Descripción:</label>
                        <textarea name="descripcion" class="form-control" rows="3">${producto.descripcion}</textarea>
                    </div>

                    <div class="alert alert-warning">
                        <i class="fa-solid fa-box"></i> 
                        El <strong>Stock Inicial</strong> será 0. Para añadir stock, debe generar una Orden de Compra y recepcionarla en Almacén.
                    </div>

                    <button type="submit" class="btn btn-primary">Guardar Producto</button>
                    <a href="${pageContext.request.contextPath}/productos" class="btn btn-secondary">Cancelar</a>
                </form>
            </div>
        </div>
    </div>
</body>
</html>