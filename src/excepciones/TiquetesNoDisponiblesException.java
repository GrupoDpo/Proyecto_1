package excepciones;

public class TiquetesNoDisponiblesException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public TiquetesNoDisponiblesException (String mensaje) {
		super(mensaje);
	}

}
