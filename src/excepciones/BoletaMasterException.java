package excepciones;

public class BoletaMasterException {
	public class BoletamasterException extends Exception {
	    private static final long serialVersionUID = 1L;
		public BoletamasterException() { super(); }
	    public BoletamasterException(String message) { super(message); }
	    public BoletamasterException(String message, Throwable cause) { super(message, cause); }
	    public BoletamasterException(Throwable cause) { super(cause); }
	}

}
