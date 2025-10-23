package excepciones;

import excepciones.BoletaMasterException.BoletamasterException;

public class CodigoTiqueteDuplicadoException extends BoletamasterException {
    private static final long serialVersionUID = 1L;

	public CodigoTiqueteDuplicadoException(String codigo) {
        super("Ya existe un tiquete con el código '" + codigo + "'.");
    }
}
