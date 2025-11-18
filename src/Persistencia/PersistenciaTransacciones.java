package Persistencia;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

import Finanzas.Registro;
import Finanzas.Transaccion;
import usuario.Usuario;
import tiquete.Tiquete;

public class PersistenciaTransacciones {

    private static final String RUTA = "data/transacciones.json";

    public PersistenciaTransacciones() {
        File f = new File(RUTA);
        if (!f.exists()) {
            try {
                f.getParentFile().mkdirs();
                FileWriter fw = new FileWriter(f);
                fw.write("{\n  \"transacciones\": []\n}");
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

    public List<Transaccion> reconstruir(String json, List<Usuario> usuarios, List<Tiquete> tiquetes) {

        List<Transaccion> lista = new ArrayList<>();

        String bloque = TextoUtils.obtenerBloque(json, "\"transacciones\"");
        List<String> objetos = TextoUtils.dividirBloques(bloque);

        for (String obj : objetos) {

            String tipo = TextoUtils.obtener(obj, "tipo");
            String idTiq = TextoUtils.obtener(obj, "idTiquete");
            String loginDueno = TextoUtils.obtener(obj, "dueno");
            String fechaStr = TextoUtils.obtener(obj, "fecha");
            String valorStr = TextoUtils.obtener(obj, "valor");

            double valor = Double.parseDouble(valorStr);

            Usuario dueno = buscarUsuario(usuarios, loginDueno);
            Tiquete tiq = buscarTiquete(tiquetes, idTiq);

            LocalDateTime fecha = LocalDateTime.parse(fechaStr);

            Registro r = new Registro(dueno, tiq, null);
            Transaccion t = new Transaccion(tipo, tiq, dueno, fecha, r, valor);

            lista.add(t);
        }

        return lista;
    }

    private Usuario buscarUsuario(List<Usuario> us, String login) {
        for (int i = 0; i < us.size(); i++) {
            if (us.get(i).getLogin().equals(login)) return us.get(i);
        }
        return null;
    }

    private Tiquete buscarTiquete(List<Tiquete> ts, String id) {
        for (int i = 0; i < ts.size(); i++) {
            if (ts.get(i).getId().equals(id)) return ts.get(i);
        }
        return null;
    }
}
