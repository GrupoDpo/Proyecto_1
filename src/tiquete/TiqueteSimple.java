package tiquete;


public class TiqueteSimple extends Tiquete {
	
	
	
	public TiqueteSimple(String tipoTiquete, double cargoPorcentual, double cuotaAdicional, String identificador, 
			String localDate, double d, String nombre,boolean transferido, boolean anulado) {
		super(tipoTiquete,cargoPorcentual,cuotaAdicional,identificador,localDate,d,nombre, transferido, anulado);
		
	}

	

	

	@Override
	//Se cobra el precio base más los cargos adicionales establecidos por el admin
	public double calcularPrecio() {
		return (precio + (precio * cargoPorcentual/100) + cuotaAdicional);
	}





	@Override
	public String getTipoTiquete() {
		return "SIMPLE";
	}
	
	
	
	
	

}
