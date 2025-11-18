package excepciones;


public class ConflictoAgendaVenueException extends Exception {
    private static final long serialVersionUID = 1L;

	public ConflictoAgendaVenueException(String venueNombre, String fecha) {
        super("Conflicto de agenda en venue '" + venueNombre + "' para la fecha " + fecha + ".");
    }
}
