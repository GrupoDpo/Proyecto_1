package tiquete;

import java.util.ArrayList;
import java.util.Collection;

public class TiqueteMultiple extends Tiquete{
	private ArrayList<Tiquete> tiquetesInternos;

	public TiqueteMultiple(String tipoTiquete, double cargoPorcentual, double cuotaAdicional, String identificador,
			String fechaExpiracion, int precio, String nombre) {
		super("MULTIPLE", cargoPorcentual, cuotaAdicional, identificador, fechaExpiracion, precio, nombre);
		this.tiquetesInternos = new ArrayList<>();
	}

	

	@Override
	//Se permite que tenga un descuento o un precio especial sobre la suma
	public double calcularPrecio() {
		int cantidadTiquetes = 0;
		for(Tiquete t:tiquetesInternos) {
			cantidadTiquetes += t.calcularPrecio();
		}
		double precioAgrupado = cantidadTiquetes * 0.9;  
		return  (precioAgrupado + (precioAgrupado * cargoPorcentual/100) + cuotaAdicional);
	}
	
	public Collection<Tiquete> getTiquetes( )
    {
        return tiquetesInternos;
	
	
    }
	
	public void agregarTiquete(Tiquete tiquete) {
		tiquetesInternos.add(tiquete);
    }
}
