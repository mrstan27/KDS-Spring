package pe.idat.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;
import pe.idat.entity.Compra;
import pe.idat.entity.DetalleCompra;

import jakarta.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {

    public void exportarOrdenCompra(HttpServletResponse response, Compra compra) throws IOException {
        // 1. Crear Documento A4
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        // 2. Fuentes y Estilos
        Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, new Color(192, 57, 43)); // Rojo Corporativo
        Font fuenteNegrita = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.BLACK);
        Font fuenteNormal = FontFactory.getFont(FontFactory.HELVETICA, 11, Color.BLACK);
        Font fuenteBlanca = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.WHITE);

        // 3. Cabecera (Logo y Título)
        Paragraph titulo = new Paragraph("ORDEN DE COMPRA N° " + String.format("%06d", compra.getCompraId()), fuenteTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(20);
        document.add(titulo);

        // 4. Datos Generales (Tabla Invisible de 2 columnas)
        PdfPTable tablaDatos = new PdfPTable(2);
        tablaDatos.setWidthPercentage(100);
        tablaDatos.setSpacingAfter(20);
        
        // Lado Izquierdo: Empresa
        PdfPCell celdaEmp = new PdfPCell();
        celdaEmp.setBorder(Rectangle.NO_BORDER);
        celdaEmp.addElement(new Paragraph("KIDS MADE HERE S.A.C.", fuenteNegrita));
        celdaEmp.addElement(new Paragraph("RUC: 20123456789", fuenteNormal));
        celdaEmp.addElement(new Paragraph("Av. Principal 123, Lima", fuenteNormal));
        tablaDatos.addCell(celdaEmp);

        // Lado Derecho: Proveedor y Fecha
        PdfPCell celdaProv = new PdfPCell();
        celdaProv.setBorder(Rectangle.NO_BORDER);
        celdaProv.addElement(new Paragraph("PROVEEDOR:", fuenteNegrita));
        celdaProv.addElement(new Paragraph(compra.getProveedor().getRazonSocial(), fuenteNormal));
        celdaProv.addElement(new Paragraph("RUC: " + compra.getProveedor().getRuc(), fuenteNormal));
        
        String fechaFormateada = compra.getFechaCompra().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        celdaProv.addElement(new Paragraph("Fecha: " + fechaFormateada, fuenteNormal));
        
        tablaDatos.addCell(celdaProv);
        document.add(tablaDatos);

        // 5. Tabla de Productos (Detalle)
        PdfPTable tablaProd = new PdfPTable(4);
        tablaProd.setWidthPercentage(100);
        tablaProd.setWidths(new float[]{4f, 1f, 2f, 2f}); // Anchos de columnas

        // Encabezados
        String[] headers = {"Producto / Descripción", "Cant.", "Precio Unit.", "Subtotal"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, fuenteBlanca));
            cell.setBackgroundColor(new Color(44, 62, 80)); // Azul oscuro
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(8);
            tablaProd.addCell(cell);
        }

        // Filas
        DecimalFormat df = new DecimalFormat("S/ #,##0.00");
        double totalCalculado = 0;

        for (DetalleCompra det : compra.getDetalle()) {
            // Nombre
            PdfPCell celdaNombre = new PdfPCell(new Phrase(det.getProducto().getNombre(), fuenteNormal));
            celdaNombre.setPadding(6);
            tablaProd.addCell(celdaNombre);

            // Cantidad
            PdfPCell celdaCant = new PdfPCell(new Phrase(String.valueOf(det.getCantidad()), fuenteNormal));
            celdaCant.setHorizontalAlignment(Element.ALIGN_CENTER);
            celdaCant.setPadding(6);
            tablaProd.addCell(celdaCant);

            // Precio
            PdfPCell celdaPrecio = new PdfPCell(new Phrase(df.format(det.getPrecioCompra()), fuenteNormal));
            celdaPrecio.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdaPrecio.setPadding(6);
            tablaProd.addCell(celdaPrecio);

            // Subtotal
            double subtotal = det.getCantidad() * det.getPrecioCompra();
            totalCalculado += subtotal;
            
            PdfPCell celdaSub = new PdfPCell(new Phrase(df.format(subtotal), fuenteNormal));
            celdaSub.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdaSub.setPadding(6);
            tablaProd.addCell(celdaSub);
        }

        document.add(tablaProd);

        // 6. Pie de Página (Total)
        Paragraph totalP = new Paragraph("TOTAL A PAGAR: " + df.format(totalCalculado), fuenteTitulo);
        totalP.setAlignment(Element.ALIGN_RIGHT);
        totalP.setSpacingBefore(15);
        document.add(totalP);

        document.close();
    }
}