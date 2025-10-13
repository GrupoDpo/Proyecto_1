package tiquete;

public class TiqueteSimple extends Tiquete {
	
	
	
	public TiqueteSimple(String tipoTiquete, double cargoPorcentual, double cuotaAdicional, String identificador, 
			String fechaExpiracion, int precio, String nombre,boolean transferido) {
		super("SIMPLE",cargoPorcentual,cuotaAdicional,identificador,fechaExpiracion,precio,nombre, transferido);
		
	}

	

	

	@Override
	//Se cobra el precio base más los cargos adicionales establecidos por el admin
	public double calcularPrecio() {
		return (precio + (precio * cargoPorcentual/100) + cuotaAdicional);
	}
	
	
	
	
	

}
