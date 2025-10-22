package excepciones;

import excepciones.BoletaMasterException.BoletamasterException;

public class OfertaFueraDeVentanaException extends BoletamasterException {
    private static final long serialVersionUID = 1L;

	public OfertaFueraDeVentanaException(String ofertaId) {
        super("La oferta '" + ofertaId + "' no aplica en la fecha/hora solicitada (fuera de ventana).");
    }
}
