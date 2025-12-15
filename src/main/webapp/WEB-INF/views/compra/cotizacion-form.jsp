<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Nueva Cotización</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/registro-estilo.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <div class="registro-card wide" style="max-width: 1000px; margin: 30px auto;">
        
        <div class="registro-header">
            <h2><i class="fa-solid fa-file-contract"></i> Generar Cotización</h2>
            <p class="text-muted">Seleccione un proveedor para ver sus productos disponibles.</p>
            <hr class="header-separator">
        </div>

        <div class="row mb-4">
            <div class="col-md-8">
                <label class="form-label fw-bold fs-5">1. Seleccione Proveedor:</label>
                <select id="proveedorSelect" class="form-select form-select-lg" onchange="cargarProductosDelProveedor()">
                    <option value="" disabled selected>-- Elija un Proveedor --</option>
                    <c:forEach items="${listaProveedores}" var="p">
                        <option value="${p.proveedorId}">${p.razonSocial} - Rubro: ${p.rubro}</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="card bg-light mb-4 shadow-sm" id="bloqueProductos" style="display:none;">
            <div class="card-header fw-bold bg-secondary text-white">2. Agregar Productos al Pedido</div>
            <div class="card-body">
                <div class="row align-items-end">
                    <div class="col-md-6">
                        <label>Producto (Filtrado por proveedor):</label>
                        <select id="productoSelect" class="form-select">
                            <option value="">-- Seleccione proveedor primero --</option>
                        </select>
                    </div>
                    <div class="col-md-2">
                        <label>Costo Unit.:</label>
                        <input type="number" id="precioInput" class="form-control" placeholder="0.00" step="0.01">
                    </div>
                    <div class="col-md-2">
                        <label>Cantidad:</label>
                        <input type="number" id="cantidadInput" class="form-control" value="10" min="1">
                    </div>
                    <div class="col-md-2">
                        <button type="button" class="btn btn-success w-100" onclick="agregarItem()">
                            <i class="fa-solid fa-plus"></i> Añadir
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <table class="table table-bordered table-hover">
            <thead class="table-dark">
                <tr>
                    <th>Producto</th>
                    <th>Costo Unit.</th>
                    <th>Cantidad</th>
                    <th>Subtotal</th>
                    <th></th>
                </tr>
            </thead>
            <tbody id="tablaDetalle" class="bg-white"></tbody>
            <tfoot>
                <tr>
                    <td colspan="3" class="text-end fw-bold">TOTAL ESTIMADO:</td>
                    <td class="fw-bold fs-5">S/ <span id="totalCotizacion">0.00</span></td>
                    <td></td>
                </tr>
            </tfoot>
        </table>

        <div class="d-flex justify-content-between mt-4">
            <a href="${pageContext.request.contextPath}/compras/cotizaciones" class="btn btn-secondary">Cancelar</a>
            <button type="button" class="btn btn-primary btn-lg" onclick="guardarCotizacion()">
                <i class="fa-solid fa-save"></i> Guardar Cotización
            </button>
        </div>
    </div>

    <script>
        let items = [];

        function cargarProductosDelProveedor() {
            let proveedorId = document.getElementById("proveedorSelect").value;
            let comboProductos = document.getElementById("productoSelect");
            let bloque = document.getElementById("bloqueProductos");
            
            comboProductos.innerHTML = '<option value="">Cargando productos...</option>';
            items = []; 
            renderTabla();

            // AJAX call
            fetch('${pageContext.request.contextPath}/productos/api/listarPorProveedor/' + proveedorId)
                .then(response => response.json())
                .then(data => {
                    comboProductos.innerHTML = '<option value="">-- Seleccione Producto --</option>';
                    
                    if(data.length === 0) {
                         comboProductos.innerHTML = '<option value="">Este proveedor no tiene productos registrados</option>';
                    }
                    
                    data.forEach(prod => {
                        let option = document.createElement("option");
                        option.value = prod.productoId;
                        option.text = prod.nombre;
                        comboProductos.appendChild(option);
                    });
                    
                    bloque.style.display = 'block';
                })
                .catch(err => {
                    console.error(err);
                    alert("Error al cargar productos del proveedor");
                });
        }

        function agregarItem() {
            let select = document.getElementById("productoSelect");
            if(select.selectedIndex <= 0) { alert("Seleccione un producto"); return; }
            
            let id = select.value;
            let nombre = select.options[select.selectedIndex].text;
            let precio = parseFloat(document.getElementById("precioInput").value);
            let cantidad = parseInt(document.getElementById("cantidadInput").value);

            if(isNaN(precio) || precio <= 0) { alert("Ingrese un costo válido"); return; }
            if(isNaN(cantidad) || cantidad < 1) { alert("Ingrese una cantidad válida"); return; }

            items.push({ productoId: id, nombre: nombre, precio: precio, cantidad: cantidad });
            renderTabla();
            
            // Reset fields
            document.getElementById("precioInput").value = "";
            document.getElementById("cantidadInput").value = "10";
            select.value = "";
        }

        function renderTabla() {
            let tbody = document.getElementById("tablaDetalle");
            tbody.innerHTML = "";
            let total = 0;
            items.forEach((i, idx) => {
                let sub = i.precio * i.cantidad;
                total += sub;
                tbody.innerHTML += `<tr>
                    <td>\${i.nombre}</td>
                    <td>S/ \${i.precio.toFixed(2)}</td>
                    <td>\${i.cantidad}</td>
                    <td>S/ \${sub.toFixed(2)}</td>
                    <td><button class='btn btn-danger btn-sm' onclick='borrar(\${idx})'><i class="fa-solid fa-trash"></i></button></td>
                </tr>`;
            });
            document.getElementById("totalCotizacion").innerText = total.toFixed(2);
        }

        function borrar(idx) {
            items.splice(idx, 1);
            renderTabla();
        }

        function guardarCotizacion() {
            if(items.length === 0) { alert("Debe agregar al menos un producto a la cotización"); return; }
            
            let data = {
                proveedorId: document.getElementById("proveedorSelect").value,
                total: parseFloat(document.getElementById("totalCotizacion").innerText),
                items: items
            };

            $.ajax({
                type: "POST",
                url: "${pageContext.request.contextPath}/compras/cotizaciones/guardar",
                contentType: "application/json",
                data: JSON.stringify(data),
                success: function(res) {
                    if(res === "ok") {
                        alert("✅ Cotización registrada correctamente");
                        window.location.href = "${pageContext.request.contextPath}/compras/cotizaciones";
                    } else {
                        alert("Error: " + res);
                    }
                },
                error: function() {
                    alert("Error de conexión con el servidor");
                }
            });
        }
    </script>
</body>
</html>