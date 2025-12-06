package interfaz;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.time.LocalDate;

import tiquete.Tiquete;

/**
 * Generador simple de códigos QR para tiquetes
 */
public class GeneradorQR {

    public static BufferedImage generarQR(Tiquete tiquete, int tamaño) {
        if (tiquete == null || tamaño <= 0) {
            return null;
        }

        try {
            // Crear el contenido del QR con la información requerida
            String contenido = construirContenidoQR(tiquete);

            // Generar la matriz del QR
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(contenido, BarcodeFormat.QR_CODE, tamaño, tamaño);

            // Crear la imagen
            BufferedImage imagen = new BufferedImage(tamaño, tamaño, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = (Graphics2D) imagen.getGraphics();
            
            // Fondo blanco
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, tamaño, tamaño);
            
            // Píxeles negros del QR
            graphics.setColor(Color.BLACK);
            for (int x = 0; x < tamaño; x++) {
                for (int y = 0; y < tamaño; y++) {
                    if (matrix.get(x, y)) {
                        graphics.fillRect(x, y, 1, 1);
                    }
                }
            }

            return imagen;

        } catch (WriterException e) {
            System.err.println("Error generando QR: " + e.getMessage());
            return null;
        }
    }

  
    
    private static String construirContenidoQR(Tiquete tiquete) {
        StringBuilder contenido = new StringBuilder();

        // Fecha de expedición (hoy)
        String fechaExpedicion = LocalDate.now().toString();
        contenido.append("Fecha Expedición: ").append(fechaExpedicion).append("\n");

        // Fecha del evento
        if (tiquete.getEvento() != null) {
            contenido.append("Fecha Evento: ").append(tiquete.getEvento().getFecha()).append("\n");
        } else {
            contenido.append("Fecha Evento: No disponible\n");
        }

        // ID del tiquete
     // ID del tiquete
        contenido.append("ID: ").append(tiquete.getId()).append("\n");  // ← Agregué \n aquí
        
        // Nombre del evento
        if (tiquete.getEvento() != null) {
            contenido.append("Evento: ").append(tiquete.getEvento().getNombre());
        } else {
            contenido.append("Evento: No disponible");
        }

        return contenido.toString();
    }
}