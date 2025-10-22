package excepciones;

import excepciones.BoletaMasterException.BoletamasterException;

public class ConflictoAgendaVenueException extends BoletamasterException {
    private static final long serialVersionUID = 1L;

	public ConflictoAgendaVenueException(String venueNombre, String fecha) {
        super("Conflicto de agenda en venue '" + venueNombre + "' para la fecha " + fecha + ".");
    }
}
