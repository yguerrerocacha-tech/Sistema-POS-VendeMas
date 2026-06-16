package sistema_yhor;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTable;

public class BoletaVenta {

  
    public String obtenerTextoTicket(String cliente, double subtotal, double total, JTable tabla) {
        double montoDescuento = subtotal - total;
        if (montoDescuento < 0) montoDescuento = 0.0;

        String fechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        StringBuilder ticket = new StringBuilder();
        
        ticket.append("=================================\n");
        ticket.append("          MINIMARKET VENDEMÁS    \n");
        ticket.append("      SAN MIGUEL - Lima, Perú    \n");
        ticket.append("=================================\n");
        ticket.append("Fecha/Hora: ").append(fechaHora).append("\n");
        ticket.append("Cliente: ").append(cliente.toUpperCase()).append("\n");
        ticket.append("---------------------------------\n");
        ticket.append(String.format("%-16s %-4s %-9s\n", "PRODUCTO", "CAN", "TOTAL"));
        ticket.append("---------------------------------\n");

        for (int i = 0; i < tabla.getRowCount(); i++) {
            String desc = tabla.getValueAt(i, 1).toString();
            String cant = tabla.getValueAt(i, 2).toString();
            String itemTotal = tabla.getValueAt(i, 4).toString();

            if (desc.length() > 14) {
                desc = desc.substring(0, 12) + "..";
            }
            ticket.append(String.format("%-16s %-4s S/.%-9s\n", desc, cant, itemTotal));
        }

        ticket.append("---------------------------------\n");
        ticket.append(String.format("%-20s S/. %-8.2f\n", "SUBTOTAL BRUTO:", subtotal));
        ticket.append(String.format("%-20s S/. %-8.2f\n", "DESCUENTO APLICADO:", montoDescuento));
        ticket.append("---------------------------------\n");
        ticket.append(String.format("%-20s S/. %-8.2f\n", "TOTAL NETO A PAGAR:", total));
        ticket.append("=================================\n");
        ticket.append("    ¡Gracias por su compra!      \n");
        ticket.append("       Vuelve pronto :)          \n");
        ticket.append("=================================\n");

        return ticket.toString();
    }

   
    public void abrirEnBlocDeNotas(String textoTicket, String cliente) {
        try {
            File carpeta = new File("tickets_txt");
            if (!carpeta.exists()) {
                carpeta.mkdir();
            }

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String nombreArchivo = "tickets_txt/Ticket_" + timestamp + "_" + cliente.replace(" ", "_") + ".txt";
            File archivoTxt = new File(nombreArchivo);
            
            FileWriter fw = new FileWriter(archivoTxt);
            PrintWriter pw = new PrintWriter(fw);
            pw.print(textoTicket);
            pw.close();
            
          
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(archivoTxt);
            }
        } catch (Exception ex) {
            System.out.println("Error al abrir Bloc de notas: " + ex.getMessage());
        }
    }
}