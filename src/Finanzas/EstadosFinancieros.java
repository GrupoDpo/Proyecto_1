package Finanzas;

import Persistencia.IFormateo;
import Persistencia.TextoUtils;

public class EstadosFinancieros implements IFormateo {
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

	@Override
	public String formatear() {
		String formatJson = String.format("  {\n    \"preciosSinRecargos\": \"%s\",\n    \"ganancias\": \"%s\",\n    \"costoProduccion\": \"%s\"\n  }"
				, TextoUtils.escape(this.preciosSinRecargos), TextoUtils.escape(this.ganancias), TextoUtils.escape(this.costoProduccion));
		
		return formatJson;
	}

}
