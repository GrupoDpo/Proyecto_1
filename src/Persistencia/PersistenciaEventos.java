package Persistencia;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import Evento.Evento;
import Evento.Venue;

public class PersistenciaEventos {

    private static final String RUTA = "data/eventos.json";

    public PersistenciaEventos() {
        File f = new File(RUTA);
        if (!f.exists()) {
            try {
                f.getParentFile().mkdirs();
                FileWriter fw = new FileWriter(f);
                fw.write("{\n  \"eventos\": []\n}");
                fw.close();
            } catch (Exception e) {}
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

    public List<Evento> reconstruir(String json) {

        List<Evento> lista = new ArrayList<>();

        String bloque = TextoUtils.obtenerBloque(json, "\"eventos\"");
        List<String> objetos = TextoUtils.dividirBloques(bloque);

        for (String obj : objetos) {

            String nombre = TextoUtils.obtener(obj, "nombre");
            String fecha = TextoUtils.obtener(obj, "fecha");
            String hora = TextoUtils.obtener(obj, "hora");
            String loginOrg = TextoUtils.obtener(obj, "loginOrganizador");
            String canceladoStr = TextoUtils.obtener(obj, "cancelado");

            boolean cancelado = Boolean.parseBoolean(canceladoStr);

            // Venue
            String venuUb = TextoUtils.obtener(obj, "venueUbicacion");
            String venuCapStr = TextoUtils.obtener(obj, "venueCapMax");
            String venuAprobStr = TextoUtils.obtener(obj, "venueAprobado");

            Venue v = null;
            if (!venuUb.equals("")) {
                int cap = Integer.parseInt(venuCapStr);
                boolean aprobado = Boolean.parseBoolean(venuAprobStr);
                v = new Venue(venuUb, cap, aprobado);
            }

            Evento e = new Evento(nombre, fecha, hora, new HashMap<>(), v, loginOrg);
            e.setCancelado(cancelado);

            lista.add(e);
        }

        return lista;
    }
}
