package excepciones;

public class IDNoEncontrado extends Exception {
	private static final long serialVersionUID = 1L;

	public IDNoEncontrado (String mensaje) {
		super(mensaje);
	}

}
