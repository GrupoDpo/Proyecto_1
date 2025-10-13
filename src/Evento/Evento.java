package Evento;

import java.util.Collection;
import java.util.HashMap;

import tiquete.Tiquete;

public class Evento {
	private String entrada;
	private String fecha;
	private String hora;
	private HashMap<String, Tiquete> tiquetesDisponibles;
	private Venue venueAsociado;
	
	
	public Evento(String entrada,String fecha, String hora, HashMap<String, Tiquete> tiquetesDisponibles2, Venue venueAsociado) {
		this.setEntrada(entrada);
		this.setFecha(fecha);
		this.setHora(hora);
		this.venueAsociado = venueAsociado;
		tiquetesDisponibles = new HashMap<String, Tiquete>();
	
		
	}
	
	
	
	
	
	public String getEntrada() {
		return entrada;
	}
	public void setEntrada(String entrada) {
		this.entrada = entrada;
	}
	
	public void agregarTiquete(Tiquete tiquete) {
        tiquetesDisponibles.put(tiquete.getId(), tiquete);
    }
	
	public Collection<Tiquete> getTiquetesDisponibles( )
    {
        return tiquetesDisponibles.values( );
    }
	
	public Tiquete getTiquetePorId(String id) {
        return tiquetesDisponibles.get(id);
    }

    
    public void venderTiquete(String id) {
        tiquetesDisponibles.remove(id);
    }
	public Venue getVenueAsociado() {
		return venueAsociado;
	}
	public String getHora() {
		return hora;
	}
	public void setHora(String hora) {
		this.hora = hora;
	}





	public String getFecha() {
		return fecha;
	}





	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	
	
}
