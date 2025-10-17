package Persistencia;

public class TextoUtils {
	public static String escape(Object valor) {
	    if (valor instanceof String texto) {
	        return "\"" + texto.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
	    }
	    return valor.toString();
	}



}
