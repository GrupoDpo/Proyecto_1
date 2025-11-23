package Persistencia;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import Evento.Venue;

public class PersistenciaVenues {

    private static final String RUTA = "data/venues.json";

    public PersistenciaVenues() {
        File f = new File(RUTA);
        if (!f.exists()) {
            try {
                if (f.getParentFile() != null) {
                    f.getParentFile().mkdirs();
                }
                try (Writer w = new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_8)) {
                    w.write("{\n  \"venues\": []\n}");
                }
            } catch (IOException e) {
                System.out.println("ERROR creando venues.json: " + e.getMessage());
            }
        }
    }

    /** Lee el JSON completo de venues.json */
    public String cargar() {
        try {
            // Igual que en PersistenciaUsuarios.cargar()
            return TextoUtils.cargarTexto(RUTA);
        } catch (Exception e) {
            System.out.println("ERROR cargando venues: " + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }

    /** Escribe el JSON completo en venues.json */
    public void guardar(String json) {
        try (Writer w = new OutputStreamWriter(new FileOutputStream(RUTA), StandardCharsets.UTF_8)) {
            w.write(json);
        } catch (Exception e) {
            System.out.println("ERROR guardando venues: " + e.getMessage());
        }
    }

    /** Reconstruye objetos Venue a partir del JSON */
    public List<Venue> reconstruir(String json) {
        List<Venue> venues = new ArrayList<>();

        if (json == null || json.isBlank()) {
            return venues;
        }

        // Bloque "venues": [ { ... }, { ... } ]
        String bloque = TextoUtils.obtenerBloque(json, "\"venues\"");
        List<String> objetos = TextoUtils.dividirBloques(bloque);

        for (String obj : objetos) {
            String ubicacion   = TextoUtils.obtener(obj, "ubicacion");
            String capStr      = TextoUtils.obtener(obj, "capacidadMax");
            String aprobadoStr = TextoUtils.obtener(obj, "aprobado");

            int capacidad = 0;
            try {
                if (capStr != null && !capStr.isBlank()) {
                    capacidad = Integer.parseInt(capStr);
                }
            } catch (NumberFormatException e) {
                capacidad = 0;
            }

            boolean aprobado = "true".equalsIgnoreCase(aprobadoStr) || "1".equals(aprobadoStr);

            Venue v = new Venue(ubicacion, capacidad, aprobado);
            // El constructor hoy pone aprobado en false siempre, así que lo forzamos:
            v.setAprobado(aprobado);

            venues.add(v);
        }

        return venues;
    }
}