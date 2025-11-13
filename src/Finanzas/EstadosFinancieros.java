package Finanzas;


<<<<<<< HEAD


public class EstadosFinancieros   {
=======
// PROMOTOR: Revisa porcentaje de ventas y ganancias 
// 


public class EstadosFinancieros {
>>>>>>> branch 'master' of https://github.com/GrupoDpo/Proyecto_1.git
	public double preciosSinRecargos;
	public double ganancias;
	public double costoProduccion;
	
	public EstadosFinancieros(double preciosSinRecargos, double ganancias, double costoProduccion) {
		this.preciosSinRecargos = preciosSinRecargos;
		this.ganancias = ganancias;
		this.costoProduccion = costoProduccion;
	}
	
	public double getGanancias() {
		
		return this.ganancias;
	}
	
	public double getCostoProduccion() {
		return this.costoProduccion;
	}
	
	public double getGananciasEvento() {
		return (getGanancias() - getCostoProduccion());
	}
	
	public double getPreciosSinRecargos() {
		return this.preciosSinRecargos;
	}

<<<<<<< HEAD
	
=======

>>>>>>> branch 'master' of https://github.com/GrupoDpo/Proyecto_1.git

}
