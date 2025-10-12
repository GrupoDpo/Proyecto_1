package Finanzas;
import java.time.LocalDate;

public class Oferta {
	public int id;
	public double porcentaje;
	public String fechaInicio;
	public String fechaFinal;
	
	public Oferta(int id, double porcentaje, String fechaInicio,
			String fechaFinal) {
		
		this.id = id;
		this.porcentaje = porcentaje;
		this.fechaInicio = fechaInicio;
		this.fechaFinal = fechaFinal;
		
		
	}
	
	public int getId() {
	    return this.id;
	}

	public double getPorcentaje() {
	    return this.porcentaje;
	}

	public String getFechaInicio() {
	    return this.fechaInicio;
	}

	public String getFechaFinal() {
	    return this.fechaFinal;
	}
	
	public boolean esVigente(String fechaActual) {
		LocalDate fechaIn = LocalDate.parse(getFechaInicio());
		LocalDate fechaFin = LocalDate.parse(getFechaFinal());
		LocalDate fechaAct = LocalDate.parse(fechaActual);
		
		if (fechaAct.isBefore(fechaFin) && fechaAct.isAfter(fechaIn)) {
			return true;
		} else {
			return false;
		}
	}
	
	

	
	
	

}
