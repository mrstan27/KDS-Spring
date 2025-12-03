<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Registrar Cotización</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/registro-estilo.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        .panel-compra { display: flex; gap: 20px; flex-wrap: wrap; }
        .panel-izq { flex: 1; min-width: 300px; }
        .panel-der { flex: 2; min-width: 400px; }
        .caja-total { background-color: #2c3e50; color: white; padding: 15px; border-radius: 10px; text-align: right; font-size: 24px; font-weight: bold; margin-top: 20px; }
        .tabla-detalle { width: 100%; border-collapse: collapse; margin-top: 10px; font-size: 14px;}
        .tabla-detalle th { background: #3498db; color: white; padding: 8px; } /* Azul para diferenciar */
        .tabla-detalle td { border-bottom: 1px solid #ddd; padding: 8px; }
    </style>
</head>
<body>

<div class="registro-card wide">
    <div class="registro-header">
        <h2 style="color: #3498db;">Registrar Cotización</h2>
        <p style="text-align: center; color: #666;">Registre los precios propuestos. Esto NO afecta el stock.</p>
        <hr class="header-separator" style="background: #3498db;">
    </div>

    <div class="form-group">
        <label class="form-label">Proveedor:</label>
        <select id="proveedorSelect" class="form-input">
            <option value="">-- Seleccione --</option>
            <c:forEach items="${listaProveedores}" var="p">
                <option value="${p.proveedorId}">${p.razonSocial}</option>
            </c:forEach>
        </select>
    </div>
    <hr>

    <div class="panel-compra">
        <div class="panel-izq">
            <h4>Agregar Item</h4>
            <div class="form-group">
                <label>Producto:</label>
                <select id="productoSelect" class="form-input">
                    <option value="">-- Seleccione --</option>
                    <c:forEach items="${listaProductos}" var="prod">
                        <option value="${prod.productoId}" data-nombre="${prod.nombre}">${prod.nombre}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group"><label>Costo (S/):</label><input type="number" id="costoInput" class="form-input"></div>
            <div class="form-group"><label>Cantidad:</label><input type="number" id="cantidadInput" class="form-input"></div>
            <button onclick="agregarItem()" class="btn-registrar" type="button" style="background:#3498db;">+ Agregar</button>
        </div>
        <div class="panel-der">
            <table class="tabla-detalle">
                <thead><tr><th>Producto</th><th>Cant.</th><th>Costo</th><th>Subtotal</th><th>x</th></tr></thead>
                <tbody id="tablaCuerpo"></tbody>
            </table>
            <div class="caja-total">Total: S/ <span id="totalTexto">0.00</span></div>
        </div>
    </div>

    <div style="text-align: center; margin-top: 30px; margin-bottom: 30px;">
        <button type="button" class="btn-registrar" onclick="guardarCotizacion()" style="background: #3498db; padding: 15px 40px;">
            <i class="fa-solid fa-save"></i> GUARDAR COTIZACIÓN
        </button>
    </div>
</div>

<script>
    // MISMO SCRIPT QUE EN COMPRAS, SOLO CAMBIA LA URL DE GUARDADO
    let listaItems = [];
    function agregarItem() { /* ... Copia el mismo código de agregarItem y actualizarTabla del otro JSP ... */ 
        // (Por brevedad, usa la misma lógica de validación y push al array que ya tienes)
        // ...
        // ...
        
        // SIMPLIFICADO PARA EL EJEMPLO:
        let prodSelect = document.getElementById("productoSelect");
        let costoInput = document.getElementById("costoInput");
        let cantInput = document.getElementById("cantidadInput");
        
        let item = {
            productoId: parseInt(prodSelect.value),
            nombre: prodSelect.options[prodSelect.selectedIndex].text,
            precio: parseFloat(costoInput.value),
            cantidad: parseInt(cantInput.value),
            subtotal: parseFloat(costoInput.value) * parseInt(cantInput.value)
        };
        listaItems.push(item);
        actualizarTabla();
    }
    
    function actualizarTabla() {
        let cuerpo = document.getElementById("tablaCuerpo");
        let totalSpan = document.getElementById("totalTexto");
        cuerpo.innerHTML = "";
        let total = 0;
        listaItems.forEach((i, idx) => {
            total += i.subtotal;
            cuerpo.innerHTML += `<tr><td>\${i.nombre}</td><td>\${i.cantidad}</td><td>\${i.precio}</td><td>\${i.subtotal}</td><td><button onclick='listaItems.splice(\${idx},1);actualizarTabla()'>x</button></td></tr>`;
        });
        totalSpan.innerText = total.toFixed(2);
    }

    function guardarCotizacion() {
        let idProv = document.getElementById("proveedorSelect").value;
        let compraDTO = {
            proveedorId: parseInt(idProv),
            total: parseFloat(document.getElementById("totalTexto").innerText),
            items: listaItems
        };

        // URL DIFERENTE: /cotizaciones/guardar
        fetch('${pageContext.request.contextPath}/compras/cotizaciones/guardar', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'X-CSRF-TOKEN': '${_csrf.token}' },
            body: JSON.stringify(compraDTO)
        }).then(r => r.text()).then(t => {
            if(t==="ok") {
                alert("Cotización guardada.");
                window.location.href = "${pageContext.request.contextPath}/compras/cotizaciones";
            }
        });
    }
</script>
</body>
</html>