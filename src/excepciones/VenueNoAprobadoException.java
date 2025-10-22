package excepciones;

import excepciones.BoletaMasterException.BoletamasterException;

public class VenueNoAprobadoException extends BoletamasterException {
    private static final long serialVersionUID = 1L;

	public VenueNoAprobadoException(String venueNombre) {
        super("El venue '" + venueNombre + "' no ha sido aprobado por el administrador.");
    }
}