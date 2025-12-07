package Persistencia;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import Finanzas.EstadosFinancieros;

/**
 * Maneja la persistencia de estados financieros en formato JSON
 * Cada usuario (Administrador, Promotor, Organizador) tiene sus propios estados
 */
public class PersistenciaEstadosFinancieros {

    private static final String RUTA = "data/estadosFinancieros.json";

    public PersistenciaEstadosFinancieros() {
        File f = new File(RUTA);
        if (!f.exists()) {
            try {
                f.getParentFile().mkdirs();
                FileWriter fw = new FileWriter(f);
                fw.write("{\n  \"estadosFinancieros\": []\n}");
                fw.close();
            } catch (Exception e) {
                System.out.println("ERROR creando estadosFinancieros.json: " + e.getMessage());
            }
        }
    }

    /**
     * Carga el contenido JSON del archivo
     */
    public String cargar() {
        try {
            return TextoUtils.cargarTexto(RUTA);
        } catch (Exception e) {
            System.out.println("ERROR cargando estados financieros: " + e.getMessage());
            return "";
        }
    }

    /**
     * Guarda el JSON en el archivo
     */
    public void guardar(String json) {
        try {
            Writer w = new OutputStreamWriter(new FileOutputStream(RUTA), StandardCharsets.UTF_8);
            w.write(json);
            w.close();
        } catch (Exception e) {
            System.out.println("ERROR guardando estados financieros: " + e.getMessage());
        }
    }

    /**
     * Reconstruye el mapa de estados financieros desde JSON
     * @param json contenido del archivo
     * @return HashMap con login -> EstadosFinancieros
     */
    public HashMap<String, EstadosFinancieros> reconstruir(String json) {
        
        HashMap<String, EstadosFinancieros> mapa = new HashMap<>();

        if (json == null || json.isEmpty()) {
            return mapa;
        }

        // Obtener el bloque de estadosFinancieros
        String bloque = TextoUtils.obtenerBloque(json, "\"estadosFinancieros\"");
        List<String> objetos = TextoUtils.dividirBloques(bloque);

        for (String obj : objetos) {
            
            // Extraer datos del objeto
            String login = TextoUtils.obtener(obj, "login");
            String preciosSinRecargosStr = TextoUtils.obtener(obj, "preciosSinRecargos");
            String gananciasStr = TextoUtils.obtener(obj, "ganancias");
            String costoProduccionStr = TextoUtils.obtener(obj, "costoProduccion");

            // Parsear valores
            double preciosSinRecargos = 0.0;
            double ganancias = 0.0;
            double costoProduccion = 0.0;

            try {
                if (preciosSinRecargosStr != null && !preciosSinRecargosStr.isEmpty()) {
                    preciosSinRecargos = Double.parseDouble(preciosSinRecargosStr);
                }
            } catch (NumberFormatException e) {
                preciosSinRecargos = 0.0;
            }

            try {
                if (gananciasStr != null && !gananciasStr.isEmpty()) {
                    ganancias = Double.parseDouble(gananciasStr);
                }
            } catch (NumberFormatException e) {
                ganancias = 0.0;
            }

            try {
                if (costoProduccionStr != null && !costoProduccionStr.isEmpty()) {
                    costoProduccion = Double.parseDouble(costoProduccionStr);
                }
            } catch (NumberFormatException e) {
                costoProduccion = 0.0;
            }

            // Crear objeto EstadosFinancieros
            EstadosFinancieros ef = new EstadosFinancieros(preciosSinRecargos, ganancias, costoProduccion);

            // Agregar al mapa
            mapa.put(login, ef);
        }

        System.out.println("Estados financieros cargados: " + mapa.size() + " usuarios");
        return mapa;
    }
}