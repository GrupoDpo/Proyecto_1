package Finanzas;

import Persistencia.IFormateo;
import Persistencia.TextoUtils;
import tiquete.Tiquete;
import usuario.Usuario;

public class Registro implements IFormateo {
	
	private Usuario duenio;
    private Tiquete tiquete;
    private Usuario receptor;

    public Registro(Usuario duenio, Tiquete tiquete, Usuario receptor) {
        this.duenio = duenio;
        this.tiquete = tiquete;
        this.receptor = receptor;
    }

    @Override
    public String formatear() {
    	String formatJson = String.format("  {\n    \"dueno\": \"%s\",\n    \"receptor\": \"%s\",\n    \"tiquete\": \"%s\"\n  }"
				, TextoUtils.escape(this.duenio), TextoUtils.escape(this.receptor), TextoUtils.escape(this.tiquete));
    	
    	return formatJson;
     
    }

}
