package Persistencia;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import Finanzas.marketPlaceReventas;
import tiquete.Tiquete;

public class PersistenciaMarketplace {

    private String ruta = "./data/marketplace.json";

    public PersistenciaMarketplace() {
        File f = new File(ruta);
        if (!f.exists()) {
            try {
                f.getParentFile().mkdirs();
                f.createNewFile();

                // marketplace vacío
                guardar(new marketPlaceReventas());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // =====================================================
    // GUARDAR
    // =====================================================
    public void guardar(marketPlaceReventas market) {
        try {
            String json = generarJson(market);
            FileOutputStream fos = new FileOutputStream(ruta);
            fos.write(json.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =====================================================
    // CARGAR
    // =====================================================
    public marketPlaceReventas cargar() {
        try {
            String raw = TextoUtils.cargarTexto(ruta);
            if (raw == null || raw.isEmpty()) {
                return new marketPlaceReventas();
            }

            return reconstruir(raw);

        } catch (Exception e) {
            e.printStackTrace();
            return new marketPlaceReventas();
        }
    }

    // ==============================================================  
    // GENERAR JSON DEL MARKETPLACE
    // ==============================================================  
    public String generarJson(marketPlaceReventas market) {

        StringBuilder sb = new StringBuilder();
        sb.append("{\n");

        // ---- ofertas ----
        sb.append("  \"ofertas\": [\n");

        Queue<HashMap<Tiquete,String>> ofertas = market.getOfertas();
        int contador = 0;

        for (HashMap<Tiquete,String> mapa : ofertas) {
            for (Map.Entry<Tiquete,String> entry : mapa.entrySet()) {

                sb.append("    {\n");
                sb.append("      \"tiqueteId\": \"" + entry.getKey().getId() + "\",\n");
                sb.append("      \"info\": \"" + TextoUtils.escape(entry.getValue()) + "\"\n");
                sb.append("    }");

                contador++;
                if (contador < ofertas.size()) sb.append(",");
                sb.append("\n");
            }
        }

        sb.append("  ],\n");

        // ---- logEventos ----
        sb.append("  \"logEventos\": [\n");
        List<String> logs = market.getLogEventos();

        for (int i = 0; i < logs.size(); i++) {
            sb.append("    \"" + TextoUtils.escape(logs.get(i)) + "\"");
            if (i < logs.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("  ]\n");

        sb.append("}\n");
        return sb.toString();
    }

    // ==============================================================  
    // RECONSTRUIR MARKETPLACE DESDE JSON
    // ==============================================================  
    public marketPlaceReventas reconstruir(String raw) {

        marketPlaceReventas market = new marketPlaceReventas();

        // reconstruir ofertas
        String bloqueOfertas = TextoUtils.obtenerBloque(raw, "\"ofertas\"");
        List<String> bloques = TextoUtils.dividirBloques(bloqueOfertas);
        

        for (String b : bloques) {

            String id = TextoUtils.obtener(b, "tiqueteId");
            String info = TextoUtils.obtener(b, "info");

            if (id == null || id.isEmpty()) continue;

            // Tiquete placeholder
            Tiquete tiquetePlaceholder = new TiquetePlaceholder(id);

            HashMap<Tiquete,String> map = new HashMap<>();
            map.put(tiquetePlaceholder, TextoUtils.unescape(info)); // ✅ Unescape aquí también

            market.getOfertas().add(map);
        }

        // ✅ CORREGIDO: reconstruir logEventos
        String bloqueLogs = TextoUtils.obtenerBloque(raw, "\"logEventos\"");
        
        if (!bloqueLogs.isEmpty()) {
            // Usar dividirArraySimple para arrays de strings simples
            List<String> listaLogs = TextoUtils.dividirArraySimple(bloqueLogs);

            for (String log : listaLogs) {
                if (log != null && !log.isEmpty()) {
                    market.getLogEventos().add(log); // Ya viene sin escape de dividirArraySimple
                }
            }
        }
        


        return market;
    }

    // -----------------------------------------------------
    // Placeholder mínimo (solo ID)
    // -----------------------------------------------------
    static class TiquetePlaceholder extends Tiquete {
        public TiquetePlaceholder(String id) {
            super("PLACEHOLDER", id, "2000-01-01", 0, "null",
                    false, false, null, 0, null);
        }

        @Override
        public String getTipoTiquete() { return "PLACEHOLDER"; }

        @Override
        public double calcularPrecio(double cobro) { return 0; }
    }

}
