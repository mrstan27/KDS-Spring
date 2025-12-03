<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Nueva Orden de Compra</title> <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/registro-estilo.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <style>
        .panel-compra { display: flex; gap: 20px; flex-wrap: wrap; }
        .panel-izq { flex: 1; min-width: 300px; }
        .panel-der { flex: 2; min-width: 400px; }
        
        .caja-total {
            background-color: #2c3e50;
            color: white;
            padding: 15px;
            border-radius: 10px;
            text-align: right;
            font-size: 24px;
            font-weight: bold;
            margin-top: 20px;
        }
        
        .tabla-detalle { width: 100%; border-collapse: collapse; margin-top: 10px; font-size: 14px;}
        .tabla-detalle th { background: #c0392b; color: white; padding: 8px; }
        .tabla-detalle td { border-bottom: 1px solid #ddd; padding: 8px; }
    </style>
</head>
<body>

<div class="registro-card wide">
    
    <div style="margin-bottom: 15px;">
        <a href="${pageContext.request.contextPath}/login/menu" style="color: #c0392b; text-decoration: none; font-weight: bold;">
            <i class="fa-solid fa-arrow-left"></i> Volver al Menú
        </a>
    </div>

    <div class="registro-header">
        <h2>Generar Orden de Compra</h2>
        <p style="text-align: center; color: #666; font-size: 14px;">Este proceso crea un pedido. El stock NO aumentará hasta confirmar la recepción.</p>
        <hr class="header-separator">
    </div>

    <div class="form-group">
        <label class="form-label">Proveedor:</label>
        <select id="proveedorSelect" class="form-input">
            <option value="">-- Seleccione Proveedor --</option>
            <c:forEach items="${listaProveedores}" var="p">
                <option value="${p.proveedorId}">${p.razonSocial} (RUC: ${p.ruc})</option>
            </c:forEach>
        </select>
    </div>

    <hr style="margin: 20px 0; border: 0; border-top: 1px dashed #ccc;">

    <div class="panel-compra">
        <div class="panel-izq" style="background: #f9f9f9; padding: 20px; border-radius: 10px;">
            <h4>Agregar Producto</h4>
            
            <div class="form-group">
                <label class="form-label">Producto:</label>
                <select id="productoSelect" class="form-input">
                    <option value="">-- Seleccione --</option>
                    <c:forEach items="${listaProductos}" var="prod">
                        <option value="${prod.productoId}" data-nombre="${prod.nombre}">
                            ${prod.nombre} (Stock: ${prod.stockActual})
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label class="form-label">Costo Unitario (S/):</label>
                <input type="number" id="costoInput" class="form-input" placeholder="0.00" step="0.01">
            </div>

            <div class="form-group">
                <label class="form-label">Cantidad a Pedir:</label> <input type="number" id="cantidadInput" class="form-input" placeholder="0">
            </div>

            <button type="button" class="btn-registrar" onclick="agregarItem()" style="width: 100%; margin-top: 10px;">
                <i class="fa-solid fa-plus"></i> Agregar a la Lista
            </button>
        </div>

        <div class="panel-der">
            <h4>Detalle del Pedido</h4>
            <table class="tabla-detalle">
                <thead>
                    <tr>
                        <th>Producto</th>
                        <th>Cant.</th>
                        <th>Costo</th>
                        <th>Subtotal</th>
                        <th>Acción</th>
                    </tr>
                </thead>
                <tbody id="tablaCuerpo"></tbody>
            </table>
            
            <div id="mensajeVacio" style="text-align: center; color: #999; margin-top: 20px;">
                No hay productos agregados.
            </div>

            <div class="caja-total">
                Total Estimado: S/ <span id="totalTexto">0.00</span>
            </div>
        </div>
    </div>

   <div style="text-align: center; margin-top: 30px; margin-bottom: 30px;">
        <button type="button" class="btn-registrar" onclick="guardarCompraFinal()" style="padding: 15px 40px; font-size: 18px;">
            <i class="fa-solid fa-file-invoice"></i>
        </button>
    </div>
</div> 

   <script>
    // Variable global para guardar la lista de productos en memoria
    let listaItems = []; 

    function agregarItem() {
        console.log("Botón presionado..."); // Para depurar en consola (F12)

        // 1. Obtener los elementos del HTML
        let prodSelect = document.getElementById("productoSelect");
        let costoInput = document.getElementById("costoInput");
        let cantInput = document.getElementById("cantidadInput");

        // 2. Validar que los campos existan (Seguridad)
        if (!prodSelect || !costoInput || !cantInput) {
            console.error("Error: No encuentro los campos del formulario.");
            return;
        }

        // 3. Obtener valores
        let idProd = prodSelect.value;
        let costo = parseFloat(costoInput.value);
        let cant = parseInt(cantInput.value);

        // 4. Validaciones de Datos
        if (!idProd || idProd === "") {
            alert("⚠️ Por favor, selecciona un Producto.");
            return;
        }
        if (isNaN(costo) || costo <= 0) {
            alert("⚠️ El costo debe ser un valor válido mayor a 0.");
            return;
        }
        if (isNaN(cant) || cant <= 0) {
            alert("⚠️ La cantidad debe ser un número entero mayor a 0.");
            return;
        }

        // 5. Obtener el nombre del producto (truco del atributo data-nombre)
        let opcion = prodSelect.options[prodSelect.selectedIndex];
        let nombreProd = opcion.getAttribute("data-nombre") || opcion.text;

        // 6. Crear el objeto item
        let subtotal = costo * cant;
        let item = {
            productoId: parseInt(idProd),
            nombre: nombreProd,
            precio: costo,
            cantidad: cant,
            subtotal: subtotal
        };

        // 7. Guardar y Actualizar Tabla
        listaItems.push(item);
        actualizarTabla();

        // 8. Limpiar formulario para el siguiente
        costoInput.value = "";
        cantInput.value = "";
        prodSelect.value = "";
        prodSelect.focus();
    }

    function eliminarItem(indice) {
        listaItems.splice(indice, 1); // Borra el elemento del array
        actualizarTabla(); // Redibuja la tabla
    }

    function actualizarTabla() {
        let cuerpo = document.getElementById("tablaCuerpo");
        let mensaje = document.getElementById("mensajeVacio");
        let totalSpan = document.getElementById("totalTexto");
        
        // Limpiamos la tabla visualmente
        cuerpo.innerHTML = ""; 
        let sumaTotal = 0;

        if (listaItems.length === 0) {
            mensaje.style.display = "block";
        } else {
            mensaje.style.display = "none";
            
            // Recorremos la lista y creamos las filas HTML
            listaItems.forEach((item, index) => {
                sumaTotal += item.subtotal;
                
                let fila = `
                    <tr>
                        <td>\${item.nombre}</td>
                        <td style="text-align: center;">\${item.cantidad}</td>
                        <td style="text-align: right;">S/ \${item.precio.toFixed(2)}</td>
                        <td style="text-align: right;">S/ \${item.subtotal.toFixed(2)}</td>
                        <td style="text-align: center;">
                            <button type="button" onclick="eliminarItem(\${index})" 
                                    style="background: #e74c3c; color: white; border: none; padding: 5px 10px; border-radius: 5px; cursor: pointer;">
                                <i class="fa-solid fa-trash"></i>
                            </button>
                        </td>
                    </tr>
                `;
                cuerpo.innerHTML += fila;
            });
        }
        
        // Actualizamos el total global
        totalSpan.innerText = sumaTotal.toFixed(2);
    }

    function guardarCompraFinal() {
        // Validar antes de enviar
        let idProv = document.getElementById("proveedorSelect").value;
        if (!idProv) {
            alert("⚠️ Debe seleccionar un Proveedor.");
            return;
        }
        if (listaItems.length === 0) {
            alert("⚠️ La lista de productos está vacía.");
            return;
        }

        // Armar el paquete de datos (JSON)
        let compraDTO = {
            proveedorId: parseInt(idProv),
            total: parseFloat(document.getElementById("totalTexto").innerText),
            items: listaItems // Java espera una lista de DetalleDTO
        };

        // Enviar al servidor (AJAX)
        fetch('${pageContext.request.contextPath}/compras/guardar', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                // El token CSRF es necesario si Spring Security está activo y validando CSRF
                'X-CSRF-TOKEN': '${_csrf.token}' 
            },
            body: JSON.stringify(compraDTO)
        })
        .then(response => response.text())
        .then(texto => {
            if (texto === "ok") {
                alert("✅ ¡Orden Generada Exitosamente!\nEl stock no cambiará hasta confirmar la recepción.");
                window.location.href = "${pageContext.request.contextPath}/compras";
            } else {
                alert("❌ Error al guardar la orden. Intente nuevamente.");
            }
        })
        .catch(err => {
            console.error(err);
            alert("Error de conexión con el servidor.");
        });
    }
</script>
   </body>