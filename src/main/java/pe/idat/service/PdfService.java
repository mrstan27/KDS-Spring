package pe.idat.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;
import pe.idat.entity.Compra;
import pe.idat.entity.DetalleCompra;
import pe.idat.entity.Producto;
import pe.idat.entity.Venta;

import jakarta.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfService {

    // --- 1. ORDEN DE COMPRA (Existente) ---
    public void exportarOrdenCompra(HttpServletResponse response, Compra compra) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, new Color(192, 57, 43));
        Font fuenteNegrita = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.BLACK);
        Font fuenteNormal = FontFactory.getFont(FontFactory.HELVETICA, 11, Color.BLACK);
        Font fuenteBlanca = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.WHITE);

        Paragraph titulo = new Paragraph("ORDEN DE COMPRA N° " + String.format("%06d", compra.getCompraId()), fuenteTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(20);
        document.add(titulo);

        PdfPTable tablaDatos = new PdfPTable(2);
        tablaDatos.setWidthPercentage(100);
        tablaDatos.setSpacingAfter(20);
        
        PdfPCell celdaEmp = new PdfPCell();
        celdaEmp.setBorder(Rectangle.NO_BORDER);
        celdaEmp.addElement(new Paragraph("KIDS MADE HERE S.A.C.", fuenteNegrita));
        celdaEmp.addElement(new Paragraph("RUC: 20123456789", fuenteNormal));
        celdaEmp.addElement(new Paragraph("Av. Principal 123, Lima", fuenteNormal));
        tablaDatos.addCell(celdaEmp);

        PdfPCell celdaProv = new PdfPCell();
        celdaProv.setBorder(Rectangle.NO_BORDER);
        celdaProv.addElement(new Paragraph("PROVEEDOR:", fuenteNegrita));
        celdaProv.addElement(new Paragraph(compra.getProveedor().getRazonSocial(), fuenteNormal));
        celdaProv.addElement(new Paragraph("RUC: " + compra.getProveedor().getRuc(), fuenteNormal));
        
        String fechaFormateada = compra.getFechaCompra().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        celdaProv.addElement(new Paragraph("Fecha: " + fechaFormateada, fuenteNormal));
        
        tablaDatos.addCell(celdaProv);
        document.add(tablaDatos);

        PdfPTable tablaProd = new PdfPTable(4);
        tablaProd.setWidthPercentage(100);
        tablaProd.setWidths(new float[]{4f, 1f, 2f, 2f});

        String[] headers = {"Producto / Descripción", "Cant.", "Precio Unit.", "Subtotal"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, fuenteBlanca));
            cell.setBackgroundColor(new Color(44, 62, 80));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(8);
            tablaProd.addCell(cell);
        }

        DecimalFormat df = new DecimalFormat("S/ #,##0.00");
        double totalCalculado = 0;

        for (DetalleCompra det : compra.getDetalle()) {
            PdfPCell celdaNombre = new PdfPCell(new Phrase(det.getProducto().getNombre(), fuenteNormal));
            celdaNombre.setPadding(6);
            tablaProd.addCell(celdaNombre);

            PdfPCell celdaCant = new PdfPCell(new Phrase(String.valueOf(det.getCantidad()), fuenteNormal));
            celdaCant.setHorizontalAlignment(Element.ALIGN_CENTER);
            celdaCant.setPadding(6);
            tablaProd.addCell(celdaCant);

            PdfPCell celdaPrecio = new PdfPCell(new Phrase(df.format(det.getPrecioCompra()), fuenteNormal));
            celdaPrecio.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdaPrecio.setPadding(6);
            tablaProd.addCell(celdaPrecio);

            double subtotal = det.getCantidad() * det.getPrecioCompra();
            totalCalculado += subtotal;
            
            PdfPCell celdaSub = new PdfPCell(new Phrase(df.format(subtotal), fuenteNormal));
            celdaSub.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdaSub.setPadding(6);
            tablaProd.addCell(celdaSub);
        }

        document.add(tablaProd);

        Paragraph totalP = new Paragraph("TOTAL A PAGAR: " + df.format(totalCalculado), fuenteTitulo);
        totalP.setAlignment(Element.ALIGN_RIGHT);
        totalP.setSpacingBefore(15);
        document.add(totalP);

        document.close();
    }

    // --- 2. REPORTE DE STOCK (NUEVO) ---
    public void exportarReporteStock(HttpServletResponse response, List<Producto> productos) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, new Color(192, 57, 43));
        Font fuenteHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
        Font fuenteData = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);

        Paragraph titulo = new Paragraph("REPORTE DE STOCK ACTUAL", fuenteTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(20);
        document.add(titulo);
        
        Paragraph fecha = new Paragraph("Generado el: " + java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        fecha.setAlignment(Element.ALIGN_RIGHT);
        fecha.setSpacingAfter(10);
        document.add(fecha);

        PdfPTable tabla = new PdfPTable(5);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{1f, 4f, 2f, 2f, 1.5f});

        String[] headers = {"ID", "Producto", "Categoría", "Precio", "Stock"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, fuenteHeader));
            cell.setBackgroundColor(new Color(44, 62, 80));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            tabla.addCell(cell);
        }

        DecimalFormat df = new DecimalFormat("S/ #,##0.00");

        for (Producto p : productos) {
            tabla.addCell(new Phrase(p.getProductoId().toString(), fuenteData));
            tabla.addCell(new Phrase(p.getNombre(), fuenteData));
            tabla.addCell(new Phrase(p.getCategoria().getNombreCategoria(), fuenteData));
            
            PdfPCell cellPrecio = new PdfPCell(new Phrase(df.format(p.getPrecioVenta()), fuenteData));
            cellPrecio.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cellPrecio);
            
            PdfPCell cellStock = new PdfPCell(new Phrase(p.getStockActual().toString(), fuenteData));
            cellStock.setHorizontalAlignment(Element.ALIGN_CENTER);
            
            if(p.getStockActual() < 5) {
                cellStock.setBackgroundColor(new Color(231, 76, 60, 50));
            }
            tabla.addCell(cellStock);
        }

        document.add(tabla);
        document.close();
    }

    // --- 3. REPORTE DE VENTAS (NUEVO) ---
    public void exportarReporteVentas(HttpServletResponse response, List<Venta> ventas) throws IOException {
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, new Color(192, 57, 43));
        Font fuenteHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
        Font fuenteData = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);

        Paragraph titulo = new Paragraph("REPORTE HISTÓRICO DE VENTAS", fuenteTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(20);
        document.add(titulo);

        PdfPTable tabla = new PdfPTable(6);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{1f, 2f, 3f, 2f, 2f, 2f});

        String[] headers = {"ID", "Fecha", "Cliente", "Vendedor", "Estado", "Total"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, fuenteHeader));
            cell.setBackgroundColor(new Color(44, 62, 80));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            tabla.addCell(cell);
        }

        DecimalFormat df = new DecimalFormat("S/ #,##0.00");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        double granTotal = 0;

        for (Venta v : ventas) {
            tabla.addCell(new Phrase(v.getVentaId().toString(), fuenteData));
            tabla.addCell(new Phrase(v.getFechaVenta().format(dtf), fuenteData));
            tabla.addCell(new Phrase(v.getCliente().getNombre() + " " + v.getCliente().getApellido(), fuenteData));
            
            String nombreVendedor = (v.getVendedor() != null) ? v.getVendedor().getNombre() : "Web/Automático";
            tabla.addCell(new Phrase(nombreVendedor, fuenteData));
            
            tabla.addCell(new Phrase(v.getEstado(), fuenteData));
            
            PdfPCell cellTotal = new PdfPCell(new Phrase(df.format(v.getMontoTotal()), fuenteData));
            cellTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cellTotal);
            
            if("COMPLETADA".equals(v.getEstado())) {
                granTotal += v.getMontoTotal();
            }
        }

        document.add(tabla);

        Paragraph totalP = new Paragraph("TOTAL RECAUDADO (COMPLETADAS): " + df.format(granTotal), fuenteTitulo);
        totalP.setAlignment(Element.ALIGN_RIGHT);
        totalP.setSpacingBefore(15);
        document.add(totalP);

        document.close();
    }
}