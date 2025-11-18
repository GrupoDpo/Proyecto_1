package Persistencia;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import tiquete.*;
import Evento.Evento;
import Evento.Localidad;

public class PersistenciaTiquetes {

    private static final String RUTA = "data/tiquetes.json";

    public PersistenciaTiquetes() {
        File f = new File(RUTA);
        if (!f.exists()) {
            try {
                f.getParentFile().mkdirs();
                FileWriter fw = new FileWriter(f);
                fw.write("{\n  \"tiquetes\": []\n}");
                fw.close();
            } catch (IOException e) {}
        }
    }

    public String cargar() {
        try {
            return TextoUtils.cargarTexto(RUTA);
        } catch (Exception e) {
            return "";
        }
    }

    public void guardar(String json) {
        try {
            Writer w = new OutputStreamWriter(new FileOutputStream(RUTA), StandardCharsets.UTF_8);
            w.write(json);
            w.close();
        } catch (Exception e) {}
    }

    /**
     * Reconstruye todos los tiquetes del sistema
     * @param json contenido del archivo
     * @param eventos lista de eventos reconstruidos antes
     */
    public List<Tiquete> reconstruir(String json, List<Evento> eventos) {

        List<Tiquete> lista = new ArrayList<>();

        String bloque = TextoUtils.obtenerBloque(json, "\"tiquetes\"");
        List<String> objetos = TextoUtils.dividirBloques(bloque);

        // ===== PRIMERA PASADA: Crear todos los tiquetes simples =====
        for (String obj : objetos) {

            String tipo = TextoUtils.obtener(obj, "tipoTiquete");
            String id = TextoUtils.obtener(obj, "id");
            String fecha = TextoUtils.obtener(obj, "fechaExpiracion");
            String precioStr = TextoUtils.obtener(obj, "precio");
            String nombre = TextoUtils.obtener(obj, "nombre");
            String transferidoStr = TextoUtils.obtener(obj, "transferido");
            String anuladoStr = TextoUtils.obtener(obj, "anulado");
            String recargoStr = TextoUtils.obtener(obj, "recargo");
            String idEvento = TextoUtils.obtener(obj, "eventoAsociado");

            // LOCALIDAD DATOS
            String locNombre = TextoUtils.obtener(obj, "localidadNombre");
            String locPrecioStr = TextoUtils.obtener(obj, "localidadPrecio");
            String locCapStr = TextoUtils.obtener(obj, "localidadCapacidad");
            String locTipoStr = TextoUtils.obtener(obj, "localidadTipo");
            
            


            double precio = 0;
            double recargo = 0;
            boolean transferido = false;
            boolean anulado = false;

            try { precio = Double.parseDouble(precioStr); } catch (Exception e) {}
            try { recargo = Double.parseDouble(recargoStr); } catch (Exception e) {}
            try { transferido = Boolean.parseBoolean(transferidoStr); } catch (Exception e) {}
            try { anulado = Boolean.parseBoolean(anuladoStr); } catch (Exception e) {}
            

            Evento evento = buscarEvento(eventos, idEvento);

            Localidad loc = null;

            if (!locNombre.isEmpty()) {
                try {
                    double locPrecio = Double.parseDouble(locPrecioStr);
                    int locCap = (int) Double.parseDouble(locCapStr);
                    int locTipo = (int) Double.parseDouble(locTipoStr);

                    loc = new Localidad(locNombre, locPrecio, locCap, locTipo);
                } catch (Exception e) {}
            }

            Tiquete t = null;

            // Solo crear tiquetes SIMPLES en la primera pasada
            if (tipo.equalsIgnoreCase("SIMPLE")) {
                t = new TiqueteSimple(
                        tipo,
                        id,
                        fecha,
                        precio,
                        nombre,
                        transferido,
                        anulado,
                        evento,
                        recargo,
                        loc
                );
                lista.add(t);
            }
        }

        // ===== SEGUNDA PASADA: Crear tiquetes múltiples con referencias =====
        for (String obj : objetos) {

            String tipo = TextoUtils.obtener(obj, "tipoTiquete");

            if (!tipo.equalsIgnoreCase("MULTIPLE")) continue;

            String id = TextoUtils.obtener(obj, "id");
            String fecha = TextoUtils.obtener(obj, "fechaExpiracion");
            String precioStr = TextoUtils.obtener(obj, "precio");
            String nombre = TextoUtils.obtener(obj, "nombre");
            String transferidoStr = TextoUtils.obtener(obj, "transferido");
            String anuladoStr = TextoUtils.obtener(obj, "anulado");
            String recargoStr = TextoUtils.obtener(obj, "recargo");
            String idEvento = TextoUtils.obtener(obj, "eventoAsociado");

            String locNombre = TextoUtils.obtener(obj, "localidadNombre");
            String locPrecioStr = TextoUtils.obtener(obj, "localidadPrecio");
            String locCapStr = TextoUtils.obtener(obj, "localidadCapacidad");
            String locTipoStr = TextoUtils.obtener(obj, "localidadTipo");
            
            

            double precio = 0;
            double recargo = 0;
            boolean transferido = false;
            boolean anulado = false;

            try { precio = Double.parseDouble(precioStr); } catch (Exception e) {}
            try { recargo = Double.parseDouble(recargoStr); } catch (Exception e) {}
            try { transferido = Boolean.parseBoolean(transferidoStr); } catch (Exception e) {}
            try { anulado = Boolean.parseBoolean(anuladoStr); } catch (Exception e) {}
            
            
            

            Evento evento = buscarEvento(eventos, idEvento);

            Localidad loc = null;

            if (!locNombre.isEmpty()) {
                try {
                    double locPrecio = Double.parseDouble(locPrecioStr);
                    int locCap = (int) Double.parseDouble(locCapStr);
                    int locTipo = (int) Double.parseDouble(locTipoStr);

                    loc = new Localidad(locNombre, locPrecio, locCap, locTipo);
                } catch (Exception e) {}
            }

            // ===== RECONSTRUIR LISTA DE TIQUETES INTERNOS =====
            ArrayList<TiqueteSimple> tiquetesInternos = new ArrayList<>();

            String bloqueIds = TextoUtils.obtenerBloque(obj, "\"idsTiquetesInternos\"");
            
            if (!bloqueIds.isEmpty()) {
                List<String> ids = TextoUtils.dividirArraySimple(bloqueIds);

                for (String idInterno : ids) {
                    // Buscar el tiquete en la lista ya creada
                    for (Tiquete t : lista) {
                        if (t.getId().equals(idInterno)) {
                            tiquetesInternos.add((TiqueteSimple) t);
                            break;
                        }
                    }
                }
            }

            // Crear el tiquete múltiple
            TiqueteMultiple tm = new TiqueteMultiple(
                    tipo,
                    id,
                    fecha,
                    precio,
                    nombre,
                    transferido,
                    anulado,
                    evento,
                    recargo,
                    loc,
                    tiquetesInternos // ✅ Pasar la lista reconstruida
            );

            lista.add(tm);
        }

        return lista;
    }

    private Evento buscarEvento(List<Evento> eventos, String nombre) {
        if (nombre == null) return null;
        for (int i = 0; i < eventos.size(); i++) {
            Evento e = eventos.get(i);
            if (e.getNombre().equals(nombre)) {
                return e;
            }
        }
        return null;
    }
}
