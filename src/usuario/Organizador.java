package usuario;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import Evento.Evento;
import Evento.Venue;
import Finanzas.Oferta;
import Persistencia.SistemaPersistencia;
import excepciones.VenueNoPresente;
import tiquete.Tiquete;
import Evento.SolicitudCancelacion;

public class Organizador extends Usuario implements IDuenoTiquetes {
	private double saldo;
	private List<Tiquete> tiquetes;
	private List<Evento> eventos;
	private List<HashMap<Tiquete, String>> listaOfertas;
	private List<String> idsTiquetes = new ArrayList<>();

	public Organizador(String login, String password, double saldo, String tipoUsuario) {
		super(login, password, tipoUsuario);
		this.saldo = saldo;
		this.eventos = new ArrayList<>();
		this.tiquetes = new ArrayList<Tiquete>();
		this.idsTiquetes = new ArrayList<String>();
		this.listaOfertas = new ArrayList<>();
	}
	
	public Collection<Tiquete> getTiquetes() {
		return this.tiquetes;
	}
	
	public void agregarTiquete(Tiquete t) {
        tiquetes.add(t);
        agregarIdTiquete(t.getId());
    }

    public void eliminarTiquete(Tiquete t) {
        tiquetes.remove(t);
        eliminarIdTiquete(t.getId());
    }

    @Override
    public List<String> getIdsTiquetes() {
        return idsTiquetes;
    }

    @Override
    public void setIdsTiquetes(List<String> ids) {
        this.idsTiquetes = ids;
    }

    @Override
    public void agregarIdTiquete(String id) {
        if (!idsTiquetes.contains(id)) idsTiquetes.add(id);
    }

    @Override
    public void eliminarIdTiquete(String id) {
        idsTiquetes.remove(id);
    }
	
	public double getSaldo() {
		return this.saldo;
	}
	
	public Evento crearEvento(String Entrada, String fecha, String hora, 
			HashMap<String, Tiquete> tiquetesDisponibles, Venue venueAsociado, String login,SistemaPersistencia sistema) throws VenueNoPresente {
		
		if (venueAsociado == null) {
			throw new VenueNoPresente("ERROR: El evento debe tener un Venue asociado");
		}
		
		Evento newEvento = new Evento(Entrada, fecha, hora, tiquetesDisponibles,venueAsociado, login);
		eventos.add(newEvento);

	    sistema.getEventos().add(newEvento);

	
	    sistema.guardarTodo();
		
		
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
	
	public void solicitarCancelacioDeEvento(Evento evento, String motivo, SistemaPersistencia sistema) {

	    Administrador admin = sistema.getAdministrador();

	    if (admin == null) {
	        System.out.println("ERROR: No existe un administrador en el sistema.");
	        return;
	    }

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

	    // Crear solicitud
	    SolicitudCancelacion sol = new SolicitudCancelacion(this.getLogin(), evento, motivo);

	    admin.recibirSolicitudCancelacionEvento(sol);

	    sistema.guardarTodo();
	}

	
	

}
