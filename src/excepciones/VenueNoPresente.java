package excepciones;


public class VenueNoPresente extends Exception {
    private static final long serialVersionUID = 1L;

	public VenueNoPresente(String mensaje) {
        super(mensaje);
    }
}
