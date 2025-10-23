package excepciones;

import excepciones.BoletaMasterException.BoletamasterException;

public class ReembolsoNoAutorizadoException extends BoletamasterException {
    private static final long serialVersionUID = 1L;

	public ReembolsoNoAutorizadoException(String motivo) {
        super("Reembolso no autorizado: " + motivo);
    }
}
