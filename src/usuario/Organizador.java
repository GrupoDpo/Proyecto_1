package usuario;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import Evento.Evento;
import Evento.Venue;
import Finanzas.Oferta;
import Persistencia.PersistenciaEventos;
import Persistencia.PersistenciaUsuarios;
import excepciones.VenueNoPresente;
import tiquete.Tiquete;
import Evento.RegistroEventos;
import Evento.SolicitudCancelacion;

public class Organizador extends Usuario implements IDuenoTiquetes {
	private double saldo;
	private List<Tiquete> tiquetes;
	private List<Evento> eventos;
	private List<HashMap<Tiquete, String>> listaOfertas;

	public Organizador(String login, String password, double saldo, String tipoUsuario) {
		super(login, password, tipoUsuario);
		this.saldo = saldo;
		this.eventos = new ArrayList<>();
	}
	
	public Collection<Tiquete> getTiquetes() {
		return this.tiquetes;
	}
	
	public void agregarTiquete(Tiquete tiquete) {
        tiquetes.add(tiquete);
    }
	public void eliminarTiquete(Tiquete tiquete) {
        tiquetes.remove(tiquete);
    }
	
	public double getSaldo() {
		return this.saldo;
	}
	
	public Evento crearEvento(String Entrada, String fecha, String hora, 
			HashMap<String, Tiquete> tiquetesDisponibles, Venue venueAsociado, String login) throws VenueNoPresente {
		
		if (venueAsociado == null) {
			throw new VenueNoPresente("ERROR: El evento debe tener un Venue asociado");
		}
		
		Evento newEvento = new Evento(Entrada, fecha, hora, tiquetesDisponibles,venueAsociado, login);
		eventos.add(newEvento);
		RegistroEventos.agregarEventoGlobal(newEvento);
		
		PersistenciaEventos persistencia = new PersistenciaEventos();
		persistencia.agregar(newEvento);
		
		
		return newEvento;
	}
	
	
	
	public void definirHoraEvento(Evento evento, String nuevaHora) {
	       evento.setHora(nuevaHora);
	}
	
	public void definirFechaEvento(Evento evento, String nuevaFecha) {
        
	     evento.setFecha(nuevaFecha);
	}
	
	public Oferta generarOferta(int id, double porcentaje, String fechaInicio,
			String fechaFinal) {
		Oferta newOferta  = new Oferta(id, porcentaje, fechaInicio,
			 fechaFinal);
		
		return newOferta;
	}

	@Override
	public String getTipoUsuario() {
		return "ORGANIZADOR";
	}

	@Override
	public void actualizarSaldo(double precioNuevo) {
		this.saldo = precioNuevo;
		
	}

	@Override
	public List<HashMap<Tiquete, String>> getListaOfertas() {
		return listaOfertas;
	}
	
	public void solicitarCancelacioDeEvento(Evento evento, String motivo, PersistenciaEventos persistenciaEventos) {
		PersistenciaUsuarios perUsuarios = new PersistenciaUsuarios();
		
		Administrador admin = perUsuarios.recuperarAdministrador();
		
	
	
	    if (evento == null) {
	        System.out.println("ERROR: Evento nulo.");
	        return;
	    }
	    if (!this.getLogin().equals(evento.getLoginOrganizador())) {
	        System.out.println("ERROR: Este evento no pertenece a este organizador.");
	        return;
	    }
	    if (evento.getCancelado()) {
	        System.out.println("El evento ya está cancelado.");
	        return;
	    }
	    if (motivo == null || motivo.isBlank()) {
	        System.out.println("Debe indicar un motivo para la cancelación.");
	        return;
	    }

	    // Crear solicitud (estado = "pendiente")
	    SolicitudCancelacion sol = new SolicitudCancelacion(this.getLogin(), evento, motivo);
	    
	    admin.recibirSolicitudCancelacionEvento(sol);

	    System.out.println("Solicitud de cancelación enviada. Estado: " + sol.getEstado());
	}
	

}
