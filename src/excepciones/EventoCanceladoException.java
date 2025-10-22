package excepciones;

import excepciones.BoletaMasterException.BoletamasterException;

public class EventoCanceladoException extends BoletamasterException {
    private static final long serialVersionUID = 1L;

	public EventoCanceladoException(String eventoId) {
        super("El evento '" + eventoId + "' fue cancelado. La operación no es posible.");
    }
}