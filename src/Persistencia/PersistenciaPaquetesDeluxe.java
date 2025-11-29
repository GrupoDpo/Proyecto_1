package Persistencia;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import tiquete.PaqueteDeluxe;
import tiquete.Tiquete;

public class PersistenciaPaquetesDeluxe {

    private static final String RUTA = "data/paquetesDeluxe.json";

    public PersistenciaPaquetesDeluxe() {
        File f = new File(RUTA);
        if (!f.exists()) {
            try {
                f.getParentFile().mkdirs();
                Writer w = new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_8);
                w.write("{\n  \"paquetesDeluxe\": []\n}");
                w.close();
            } catch (Exception e) {
                System.out.println("ERROR creando paquetesDeluxe.json: " + e.getMessage());
            }
        }
    }

    public String cargar() {
        try {
            return TextoUtils.cargarTexto(RUTA);
        } catch (Exception e) {
            System.out.println("ERROR cargando paquetes deluxe: " + e.getMessage());
            return "{ \"paquetesDeluxe\": [] }";
        }
    }

    public void guardar(String json) {
        try {
            Writer w = new OutputStreamWriter(new FileOutputStream(RUTA), StandardCharsets.UTF_8);
            w.write(json);
            w.close();
        } catch (Exception e) {
            System.out.println("ERROR guardando paquetes deluxe: " + e.getMessage());
        }
    }

    public List<PaqueteDeluxe> reconstruir(String json, List<Tiquete> todosTiquetes) {
        List<PaqueteDeluxe> paquetes = new ArrayList<>();

        if (json == null || json.isBlank()) {
            return paquetes;
        }

        // 1) Obtener bloque principal: "paquetesDeluxe": [ {...}, {...} ]
        String bloque = TextoUtils.obtenerBloque(json, "\"paquetesDeluxe\"");
        if (bloque == null || bloque.isBlank()) {
            return paquetes;
        }

        // 2) Dividir cada objeto paquete
        List<String> objetos = TextoUtils.dividirBloques(bloque);

        for (String obj : objetos) {

            // ---- Atributos básicos del paquete ----
            String mercancia   = TextoUtils.obtener(obj, "mercanciaYBeneficios");
            String precioStr   = TextoUtils.obtener(obj, "precioPaquete");
            String anuladoStr  = TextoUtils.obtener(obj, "anulado");

            if (mercancia == null) mercancia = "";

            double precio = 0.0;
            boolean anulado = false;

            try {
                if (precioStr != null && !precioStr.isBlank()) {
                    precio = Double.parseDouble(precioStr);
                }
            } catch (NumberFormatException e) {
                precio = 0.0;
            }

            try {
                if (anuladoStr != null && !anuladoStr.isBlank()) {
                    anulado = Boolean.parseBoolean(anuladoStr);
                }
            } catch (Exception e) {
                anulado = false;
            }

            // Crear el paquete con mercancia y precio
            PaqueteDeluxe paquete = new PaqueteDeluxe(mercancia, precio);
            // Si tienes setter de anulado, úsalo:
            // paquete.setAnulado(anulado);

            // ---- Reconstruir TIQUETES PRINCIPALES ----
            String bloqueIds = TextoUtils.obtenerBloque(obj, "\"idsTiquetes\"");
            if (bloqueIds != null && !bloqueIds.isBlank()) {

                List<String> ids = TextoUtils.dividirArraySimple(bloqueIds);

                for (String idRef : ids) {
                    // Igual que en PersistenciaUsuarios: obtener valor literal del string
                    String id = TextoUtils.obtener(idRef, null);
                    if (id == null || id.isBlank()) continue;

                    Tiquete encontrado = buscarTiquetePorId(todosTiquetes, id);
                    if (encontrado != null && paquete.getTiquetes() instanceof ArrayList) {
                        ((ArrayList<Tiquete>) paquete.getTiquetes()).add(encontrado);
                    }
                }
            }

            // ---- Reconstruir TIQUETES ADICIONALES ----
            String bloqueIdsAd = TextoUtils.obtenerBloque(obj, "\"idsTiquetesAdicionales\"");
            if (bloqueIdsAd != null && !bloqueIdsAd.isBlank()) {

                List<String> idsAd = TextoUtils.dividirArraySimple(bloqueIdsAd);

                for (String idRef : idsAd) {
                    String id = TextoUtils.obtener(idRef, null);
                    if (id == null || id.isBlank()) continue;

                    Tiquete encontrado = buscarTiquetePorId(todosTiquetes, id);
                    if (encontrado != null && paquete.getTiquetesAdicionales() instanceof ArrayList) {
                        ((ArrayList<Tiquete>) paquete.getTiquetesAdicionales()).add(encontrado);
                    }
                }
            }

            paquetes.add(paquete);
        }

        System.out.println("Total paquetes deluxe reconstruidos: " + paquetes.size());
        return paquetes;
    }

    // Helper privado dentro de PersistenciaPaquetesDeluxe
    private Tiquete buscarTiquetePorId(List<Tiquete> todos, String id) {
        if (id == null) return null;
        for (Tiquete t : todos) {
            if (id.equals(t.getId())) {
                return t;
            }
        }
        return null;
    }
}
