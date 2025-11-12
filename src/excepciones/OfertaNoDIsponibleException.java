package excepciones;


public class OfertaNoDIsponibleException extends Exception {
    private static final long serialVersionUID = 1L;

	public OfertaNoDIsponibleException(String mensaje) {
        super(mensaje);
    }
}
