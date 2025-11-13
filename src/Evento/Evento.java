package Evento;

import java.util.Collection;
import java.util.HashMap;


import Finanzas.EstadosFinancieros;
import Persistencia.TextoUtils;
import tiquete.Tiquete;
import usuario.Organizador;

import tiquete.Tiquete;

public class Evento  {
	private String nombre;
	private String fecha;
	private String hora;
	private HashMap<String, Tiquete> tiquetesDisponibles;
	private Venue venueAsociado;
	private String loginOrganizador;
	private EstadosFinancieros estadoFinanciero;
	private boolean cancelado;
	
	
	public Evento(String entrada,String fecha, String hora, HashMap<String, Tiquete> tiquetesDisponibles2, Venue venueAsociado,
			String loginOrganizador, EstadosFinancieros estadoFinanciero, boolean cancelado ) {
		this.setEntrada(entrada);
		this.setFecha(fecha);
		this.setHora(hora);
		this.venueAsociado = venueAsociado;
		tiquetesDisponibles = new HashMap<String, Tiquete>();
		this.loginOrganizador = loginOrganizador;
		this.estadoFinanciero = estadoFinanciero;
		this.cancelado = cancelado;
		
		
		
	
		
	}
	
	
	
	public String getLoginOrganizador() {
		return this.loginOrganizador;
	}
	
	public String getEntrada() {
		return nombre;
	}
	public void setEntrada(String entrada) {
		this.nombre = entrada;
	}
	
	public void agregarTiquete(Tiquete tiquete) {
        tiquetesDisponibles.put(tiquete.getId(), tiquete);
    }
	
	public Collection<Tiquete> getTiquetesDisponibles( )
    {
        return tiquetesDisponibles.values( );
    }
	
	public HashMap<String, Tiquete> getMapaTiquetes( )
    {
        return tiquetesDisponibles;
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
	
	public void setVenueAsociado(Venue venue) {
		this.venueAsociado = venue;
	}





	public String getFecha() {
		return fecha;
	}





	public void setFecha(String fecha) {
		this.fecha = fecha;
	}



	public void setCancelado(boolean b) {
		// TODO Auto-generated method stub
		
	}



	public void setLoginOrganizador(String login) {
		this.loginOrganizador = login;
		
	}

	
	
	
}
