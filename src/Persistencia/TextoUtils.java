package Persistencia;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class TextoUtils {

    // ==========================
    // Leer archivo completo
    // ==========================
    public static String cargarTexto(String ruta) throws IOException {
        File f = new File(ruta);
        if (!f.exists()) return "";

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }

    // ==========================
    // Escapar caracteres especiales
    // ==========================
    public static String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    public static String unescape(String s) {
        if (s == null) return "";
        return s.replace("\\\"", "\"").replace("\\\\", "\\");
    }

    // ==========================
    // Obtener valor de clave simple
    // ==========================
    public static String obtener(String raw, String clave) {
        if (raw == null) return "";

        if (clave != null) {
            String patron = "\"" + clave + "\"";
            int i = raw.indexOf(patron);
            if (i == -1) return "";

            // Buscar el ":"
            int posDospuntos = raw.indexOf(":", i);
            if (posDospuntos == -1) return "";

            // Saltar espacios después de ":"
            int ini = posDospuntos + 1;
            while (ini < raw.length() && (raw.charAt(ini) == ' ' || raw.charAt(ini) == '\t' || raw.charAt(ini) == '\n'))
                ini++;

            if (ini >= raw.length()) return "";

            // Si el valor empieza con comilla → es un string
            if (raw.charAt(ini) == '\"') {
                ini++; // saltar la comilla inicial
                int fin = raw.indexOf("\"", ini);
                if (fin == -1) return "";
                return unescape(raw.substring(ini, fin));
            }

            // Si no, es número, booleano, null o array
            int fin = ini;
            while (fin < raw.length() && ",}\n\t\r ]".indexOf(raw.charAt(fin)) == -1) {
                fin++;
            }

            return raw.substring(ini, fin).trim();

        } else {
            // clave == null → obtener valor literal (para arrays)
            int start = 0;
            int end = raw.length();
            // quitar comillas si existen
            if (raw.startsWith("\"") && raw.endsWith("\"")) {
                start = 1;
                end = raw.length() - 1;
            }
            return unescape(raw.substring(start, end).trim());
        }
    }

    // ==========================
    // Obtener bloque JSON [{...}, {...}]
    // ==========================
    public static String obtenerBloque(String texto, String etiqueta) {
        int i = texto.indexOf(etiqueta);
        if (i == -1) return "";

        i = texto.indexOf("[", i); // inicio de lista
        if (i == -1) return "";

        int stack = 0;
        int inicio = i;
        int fin = -1;

        for (int j = i; j < texto.length(); j++) {
            char c = texto.charAt(j);
            if (c == '[') stack++;
            if (c == ']') stack--;

            if (stack == 0) {
                fin = j;
                break;
            }
        }

        if (fin == -1) return "";
        return texto.substring(inicio, fin + 1);
    }

    // ==========================
    // Dividir array JSON en objetos
    // ==========================
    public static List<String> dividirBloques(String lista) {
        List<String> bloques = new ArrayList<>();
        if (lista == null || lista.isEmpty()) return bloques;

        int stack = 0;
        int inicio = -1;

        for (int i = 0; i < lista.length(); i++) {
            char c = lista.charAt(i);

            if (c == '{') {
                if (stack == 0) inicio = i;
                stack++;
            }

            if (c == '}') {
                stack--;
                if (stack == 0 && inicio != -1) {
                    bloques.add(lista.substring(inicio, i + 1));
                }
            }
        }
        return bloques;
    }

    // ==========================
    // Dividir array simple de strings ["id1","id2"]
    // ==========================
    public static List<String> dividirArraySimple(String lista) {
        List<String> res = new ArrayList<>();
        if (lista == null || lista.isEmpty()) return res;

        // Quitar corchetes
        lista = lista.trim();
        
        if (lista.startsWith("[")) lista = lista.substring(1);
        if (lista.endsWith("]")) lista = lista.substring(0, lista.length() - 1);
        
        if (lista.isEmpty()) return res;

        String[] partes = lista.split(",");
        for (String p : partes) {
            p = p.trim();
            if (p.startsWith("\"") && p.endsWith("\"")) {
                p = p.substring(1, p.length() - 1);
            }
            if (!p.isEmpty()) res.add(unescape(p));
        }
        return res;
    }
}
