package Evento;

import java.util.Collection;
import java.util.HashMap;


import Finanzas.EstadosFinancieros;
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
	
	
	
	
	public Evento(String nombre,String fecha, String hora, HashMap<String, Tiquete> tiquetesDisponibles2, Venue venueAsociado,
			String loginOrganizador) {
		this.setNombre(nombre);
		this.setFecha(fecha);
		this.setHora(hora);
		this.venueAsociado = venueAsociado;
		tiquetesDisponibles = new HashMap<String, Tiquete>();
		this.loginOrganizador = loginOrganizador;
		this.cancelado = false;
		this.estadoFinanciero = new EstadosFinancieros();
	}
	
	public void setCancelado(boolean estado) {
		this.cancelado = estado;
	}
	
	public boolean getCancelado() {
		return this.cancelado;
	}
	
	public String getLoginOrganizador() {
		return this.loginOrganizador;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String entrada) {
		this.nombre = entrada;
	}
	
	public void agregarTiquete(Tiquete tiquete) {
        tiquetesDisponibles.put(tiquete.getId(), tiquete);
    }
	
	public Collection<Tiquete> getTiquetesDisponibles( )
    {
        return tiquetesDisponibles.values( );
    }
	
	
	public HashMap<String, Tiquete> getTiquetes( )
    {
        return tiquetesDisponibles;
    }
	
	public Tiquete getTiquetePorId(String id) {
        return tiquetesDisponibles.get(id);
    }

    
    public void venderTiquete(String id) {
    	Tiquete t = tiquetesDisponibles.remove(id); 
        if (t != null) {
            t.setTransferido(true);
        }
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



	public void setCancelado() {
		this.cancelado = true;
	}

	
	
	
}
