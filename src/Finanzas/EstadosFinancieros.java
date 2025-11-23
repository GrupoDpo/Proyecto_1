package Finanzas;

import java.io.Serializable;

public class EstadosFinancieros implements Serializable  {
	private static final long serialVersionUID = 1L;

	public double preciosSinRecargos;
	public double ganancias;
	public double costoProduccion;
	
	public EstadosFinancieros(double preciosSinRecargos, double ganancias, double costoProduccion) {
		this.preciosSinRecargos = preciosSinRecargos;
		this.ganancias = ganancias;
		this.costoProduccion = costoProduccion;
	}
	
	public EstadosFinancieros() {
		this(0.0, 0.0, 0.0);
	}
	
	public double getGanancias() {
		
		return this.ganancias;
	}
	
	public double getCostoProduccion() {
		return this.costoProduccion;
	}
	
	
	public double getPreciosSinRecargos() {
		return this.preciosSinRecargos;
	}
	
	public void acumularVenta(double precioSinRecargos, double ingresoAdministrador) {
        this.preciosSinRecargos += precioSinRecargos;
        this.ganancias += ingresoAdministrador;
    }



} 
