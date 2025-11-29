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

        // bloque [ { ... }, { ... } ]
        String bloque = TextoUtils.obtenerBloque(json, "\"paquetesDeluxe\"");
        List<String> objetos = TextoUtils.dividirBloques(bloque);

        for (String obj : objetos) {
        	String mercancia = TextoUtils.obtener(obj, "mercanciaYBeneficios");
        	String precioStr = TextoUtils.obtener(obj, "precioPaquete");
        	String anuladoStr = TextoUtils.obtener(obj, "anulado");

        	double precio = 0.0;
        	boolean anulado = false;

        	try {
        	    if (precioStr != null && !precioStr.isBlank()) {
        	        precio = Double.parseDouble(precioStr);
        	    }
        	} catch (NumberFormatException e) {
        	    precio = 0.0;
        	}

        	if (anuladoStr != null && !anuladoStr.isBlank()) {
        	    anulado = Boolean.parseBoolean(anuladoStr);
        	}

        	PaqueteDeluxe paquete = new PaqueteDeluxe(mercancia, precio);

        	paquete.setAnulado(anulado);

            // ---- IDs de tiquetes principales ----
            String bloqueIdsPrincipales = TextoUtils.obtenerBloque(obj, "\"idsTiquetes\"");
            List<String> idsPrincipales = TextoUtils.dividirArraySimple(bloqueIdsPrincipales);

            for (String literal : idsPrincipales) {
                String id = TextoUtils.obtener(literal, null); // valor literal del string
                Tiquete encontrado = buscarTiquetePorId(todosTiquetes, id);
                if (encontrado != null && paquete.getTiquetes() instanceof ArrayList) {
                    ((ArrayList<Tiquete>) paquete.getTiquetes()).add(encontrado);
                }
            }

            // ---- IDs de tiquetes adicionales ----
            String bloqueIdsAdicionales = TextoUtils.obtenerBloque(obj, "\"idsTiquetesAdicionales\"");
            List<String> idsAdicionales = TextoUtils.dividirArraySimple(bloqueIdsAdicionales);

            for (String literal : idsAdicionales) {
                String id = TextoUtils.obtener(literal, null);
                Tiquete encontrado = buscarTiquetePorId(todosTiquetes, id);
                if (encontrado != null) {
                    // en tu clase, agregarTiquete mete en la lista de adicionales
                    paquete.agregarTiquete(encontrado);
                }
            }

            paquetes.add(paquete);
        }

        System.out.println("Total paquetes deluxe reconstruidos: " + paquetes.size());
        return paquetes;
    }

    private Tiquete buscarTiquetePorId(List<Tiquete> todos, String id) {
        if (id == null) return null;
        for (Tiquete t : todos) {
            if (id.equals(t.getId())) return t;
        }
        return null;
    }
}
