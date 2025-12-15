<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Punto de Venta | POS</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/registro-estilo.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <div class="registro-card wide" style="width: 95%; max-width: 1200px; margin: 20px auto;">
        
        <div class="d-flex justify-content-between align-items-center mb-3">
            <a href="${pageContext.request.contextPath}/login/menu" class="text-decoration-none text-danger fw-bold">
                <i class="fa-solid fa-arrow-left"></i> Volver al Menú
            </a>
            <span class="badge bg-secondary">Vendedor: ${pageContext.request.userPrincipal.name}</span>
        </div>

        <div class="registro-header">
            <h2><i class="fa-solid fa-cash-register"></i> Nueva Venta en Tienda</h2>
            <hr class="header-separator">
        </div>

        <div class="card bg-light mb-4 border-0">
            <div class="card-body">
                <div class="row align-items-end">
                    <div class="col-md-8">
                        <label class="form-label fw-bold">Cliente:</label>
                        <select id="clienteSelect" class="form-select">
                            <option value="" disabled selected>-- Seleccione Cliente --</option>
                            <c:forEach items="${listaClientes}" var="c">
                                <option value="${c.clienteId}">
                                    ${c.numeroDocumento} - ${c.nombre} ${c.apellido}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-4 text-end">
                        <a href="${pageContext.request.contextPath}/cliente/nuevo" class="btn btn-outline-primary btn-sm" target="_blank">
                            <i class="fa-solid fa-user-plus"></i> Registrar Nuevo Cliente
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label">Producto:</label>
                <select id="productoSelect" class="form-select">
                    <option value="" disabled selected>-- Buscar Producto --</option>
                    <c:forEach items="${listaProductos}" var="p">
                        <c:if test="${p.stockActual > 0}">
                            <option value="${p.productoId}" 
                                    data-precio="${p.precioVenta}"
                                    data-stock="${p.stockActual}"
                                    data-nombre="${p.nombre}">
                                ${p.nombre} | Stock: ${p.stockActual} | S/ ${p.precioVenta}
                            </option>
                        </c:if>
                    </c:forEach>
                </select>
            </div>
            <div class="col-md-2">
                <label class="form-label">Cantidad:</label>
                <input type="number" id="cantidadInput" class="form-control" value="1" min="1">
            </div>
            <div class="col-md-2">
                <label class="form-label">Stock Disp:</label>
                <input type="text" id="stockDisplay" class="form-control" disabled readonly>
            </div>
            <div class="col-md-2 d-grid">
                <label class="form-label">&nbsp;</label>
                <button type="button" class="btn btn-success" onclick="agregarProducto()">
                    <i class="fa-solid fa-plus"></i> AGREGAR
                </button>
            </div>
        </div>

        <div class="table-responsive mb-4">
            <table class="table table-hover table-bordered">
                <thead class="table-dark">
                    <tr>
                        <th>Producto</th>
                        <th class="text-end">Precio Unit.</th>
                        <th class="text-center">Cantidad</th>
                        <th class="text-end">Subtotal</th>
                        <th class="text-center">Acción</th>
                    </tr>
                </thead>
                <tbody id="tablaDetalle">
                    </tbody>
                <tfoot class="table-light">
                    <tr>
                        <td colspan="3" class="text-end fw-bold fs-5">TOTAL A PAGAR:</td>
                        <td class="text-end fw-bold fs-5 text-danger">S/ <span id="totalVenta">0.00</span></td>
                        <td></td>
                    </tr>
                </tfoot>
            </table>
        </div>

        <div class="d-grid gap-2">
            <button type="button" class="btn btn-primary btn-lg" onclick="procesarVenta()">
                <i class="fa-solid fa-check-circle"></i> CONFIRMAR VENTA
            </button>
        </div>
    </div>

    <script>
        let itemsVenta = [];

        // Actualizar visualización de stock al cambiar producto
        document.getElementById("productoSelect").addEventListener("change", function() {
            let option = this.options[this.selectedIndex];
            let stock = option.getAttribute("data-stock");
            document.getElementById("stockDisplay").value = stock;
            document.getElementById("cantidadInput").max = stock;
            document.getElementById("cantidadInput").value = 1;
        });

        function agregarProducto() {
            let select = document.getElementById("productoSelect");
            
            if (select.selectedIndex <= 0) {
                alert("Seleccione un producto.");
                return;
            }

            let option = select.options[select.selectedIndex];
            let id = parseInt(option.value);
            let nombre = option.getAttribute("data-nombre");
            let precio = parseFloat(option.getAttribute("data-precio"));
            let stock = parseInt(option.getAttribute("data-stock"));
            let cantidad = parseInt(document.getElementById("cantidadInput").value);

            if (isNaN(cantidad) || cantidad < 1) {
                alert("Cantidad inválida.");
                return;
            }

            // Verificar si ya existe en la lista para sumar cantidad
            let existente = itemsVenta.find(i => i.productoId === id);
            let cantidadAcumulada = existente ? existente.cantidad + cantidad : cantidad;

            if (cantidadAcumulada > stock) {
                alert("Error: Stock insuficiente. Disponible: " + stock + ", Intentas vender: " + cantidadAcumulada);
                return;
            }

            if (existente) {
                existente.cantidad += cantidad;
            } else {
                itemsVenta.push({
                    productoId: id,
                    nombre: nombre,
                    precio: precio,
                    cantidad: cantidad
                });
            }

            renderTabla();
            
            // Reset inputs
            document.getElementById("cantidadInput").value = 1;
            select.value = "";
            document.getElementById("stockDisplay").value = "";
        }

        function renderTabla() {
            let tbody = document.getElementById("tablaDetalle");
            tbody.innerHTML = "";
            let total = 0;

            itemsVenta.forEach((item, index) => {
                let subtotal = item.cantidad * item.precio;
                total += subtotal;
                
                let row = `<tr>
                    <td>\${item.nombre}</td>
                    <td class="text-end">S/ \${item.precio.toFixed(2)}</td>
                    <td class="text-center">\${item.cantidad}</td>
                    <td class="text-end">S/ \${subtotal.toFixed(2)}</td>
                    <td class="text-center">
                        <button class='btn btn-danger btn-sm' onclick='eliminar(\${index})'>
                            <i class="fa-solid fa-trash"></i>
                        </button>
                    </td>
                </tr>`;
                tbody.innerHTML += row;
            });
            document.getElementById("totalVenta").innerText = total.toFixed(2);
        }

        function eliminar(index) {
            itemsVenta.splice(index, 1);
            renderTabla();
        }

        function procesarVenta() {
            let clienteId = document.getElementById("clienteSelect").value;
            
            if (!clienteId) {
                alert("Debe seleccionar un cliente.");
                return;
            }
            if (itemsVenta.length === 0) {
                alert("La venta debe tener al menos un producto.");
                return;
            }

            if(!confirm("¿Confirmar la venta por S/ " + document.getElementById("totalVenta").innerText + "?")) {
                return;
            }

            let venta = {
                clienteId: parseInt(clienteId),
                total: parseFloat(document.getElementById("totalVenta").innerText),
                items: itemsVenta
            };

            $.ajax({
                type: "POST",
                url: "${pageContext.request.contextPath}/ventas/guardar",
                contentType: "application/json",
                data: JSON.stringify(venta),
                success: function(resp) {
                    if (resp === "ok") {
                        alert("✅ Venta registrada correctamente.");
                        // Recargar para limpiar y actualizar stock en el select
                        window.location.reload(); 
                    } else {
                        alert("❌ " + resp);
                    }
                },
                error: function() {
                    alert("❌ Error de comunicación con el servidor.");
                }
            });
        }
    </script>
</body>
</html>