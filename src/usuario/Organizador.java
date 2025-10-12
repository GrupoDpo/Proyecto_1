package usuario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import Evento.Evento;
import Evento.Venue;
import Finanzas.Oferta;
import tiquete.Tiquete;

public class Organizador extends Usuario {
	private double saldo;
	private ArrayList<Tiquete> tiquetes;

	public Organizador(String login, String password, double saldo) {
		super(login, password);
		this.saldo = saldo;
	}
	
	public Collection<Tiquete> getTiquetes() {
		return this.tiquetes;
	}
	
	public double getSaldo() {
		return this.saldo;
	}
	
	public Evento crearEvento(String Entrada, String fecha, String hora, 
			HashMap<Tiquete, Integer> tiquetesDisponibles) {
		Evento newEvento = new Evento(Entrada, fecha, hora, tiquetesDisponibles);
		return newEvento;
	}
	
	public Venue crearVenue(String ubicacion, int capacidadMax, boolean aprobado) {
        return new Venue(ubicacion, capacidadMax, aprobado);
    }
	
	public void definirHoraEvento(Evento evento, String nuevaHora) {
	        
	     evento.hora = nuevaHora;
	}
	
	public void definirFechaEvento(Evento evento, String nuevaFecha) {
        
	     evento.fecha = nuevaFecha;
	}
	
	public Oferta generarOferta(int id, double porcentaje, String fechaInicio,
			String fechaFinal) {
		Oferta newOferta  = new Oferta(id, porcentaje, fechaInicio,
			 fechaFinal);
		
		return newOferta;
	}

	@Override
	public String getTipoUsuario() {
		// TODO Auto-generated method stub
		return "ORGANIZADOR";
	}
	
	
	

}
