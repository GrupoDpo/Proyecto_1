package excepciones;

import excepciones.BoletaMasterException.BoletamasterException;

/** Reglas de transferencia violadas (p.ej. paquetes Deluxe no transferibles o condiciones de paquetes múltiples). */
public class TransferenciaNoPermitidaException extends BoletamasterException {

	private static final long serialVersionUID = 1L;

	public TransferenciaNoPermitidaException(String motivo) {
        super("Transferencia no permitida: " + motivo);
    }
}
