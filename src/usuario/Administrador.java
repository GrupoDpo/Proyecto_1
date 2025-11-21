package usuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Map.Entry;

import Evento.Evento;
import Evento.SolicitudCancelacion;
import Evento.Venue;
import Persistencia.PersistenciaEventos;
import Persistencia.SistemaPersistencia;
import excepciones.TransferenciaNoPermitidaException;
import tiquete.Tiquete;

public class Administrador extends Usuario {

    private double porcentajeAdicional;
    private double cobroEmision;
    private double ganancias;
    private Queue<HashMap<Tiquete, String>> rembolsosSolicitados;
    private Queue<HashMap<Venue, String>> solicitudesVenue;
    private Queue<SolicitudCancelacion> solicitudesCancelacionEvento;

    public Administrador(String login, String password, String tipoUsuario) {
        super(login, password, tipoUsuario);
        this.porcentajeAdicional = 0.0;
        this.cobroEmision = 0.0;
        this.ganancias = 0.0;
        this.rembolsosSolicitados = new LinkedList<>();
        this.solicitudesVenue = new LinkedList<>();
        this.solicitudesCancelacionEvento = new LinkedList<>();
    }
   

 
    public void cobrarPorcentajeAdicional(double porcentaje) {
        if (porcentaje < 0) {
            System.out.println("El porcentaje adicional no puede ser negativo.");
        } else {
            this.porcentajeAdicional = porcentaje;
            System.out.println("Se ha establecido un porcentaje adicional de " + porcentaje + "%.");
        }
    }

    
    public void fijarCobroEmisionImpresion(double cobroEmision) {
        if (cobroEmision < 0) {
            System.out.println("El cobro de emisión no puede ser negativo.");
        } else {
            this.cobroEmision = cobroEmision;
            System.out.println("Cobro de emisión/impresión fijado en $" + cobroEmision);
        }
    }

   
    public void cancelarEvento(Evento evento) {
        if (evento == null) {
            System.out.println("El evento no existe o es nulo.");
            return;
        }

        evento.setCancelado(true); 
        ArrayList<Tiquete> tiquetes = new ArrayList<>(evento.getTiquetesDisponibles());

        for (Tiquete t : tiquetes) {
            t.setAnulado(true); 
        }

        System.out.println("El evento '" + evento.getNombre() + "' ha sido cancelado junto con sus tiquetes.");
    }

    public double getPorcentajeAdicional() {
        return porcentajeAdicional;
    }

    public double getCobroEmision() {
        return cobroEmision;
    }

    @Override
    public String getTipoUsuario() {
        return "ADMINISTRADOR";
    }
    
    public Venue crearVenue(String ubicacion, int capacidadMax, boolean aprobado) {
        return new Venue(ubicacion, capacidadMax, aprobado);
    }
    
   

    
    public void setGanancias(double newGanancias) {
    	this.ganancias = newGanancias;
    }
    
   
    
   
  
    public void verSolicitudReembolso(Usuario dueno, Tiquete tiqueteReembolso, SistemaPersistencia sistema, boolean aceptada) throws TransferenciaNoPermitidaException {
       
    	if (!(dueno instanceof IDuenoTiquetes)) {
            throw new TransferenciaNoPermitidaException("El usuario no puede tener tiquetes.");
        }
    	IDuenoTiquetes duenoTiquete = (IDuenoTiquetes) dueno;
    	
    	Tiquete tiqueteReal = sistema.buscarTiquetePorId(tiqueteReembolso.getId());
    	
    	if (tiqueteReal == null) {
            throw new TransferenciaNoPermitidaException("Tiquete no encontrado.");
        }
    	
    	boolean esDueno = false;
        for (Tiquete t : duenoTiquete.getTiquetes()) {
            if (t.getId().equals(tiqueteReal.getId())) {
                esDueno = true;
                break;
            }
        }

        if (!esDueno) {
            throw new TransferenciaNoPermitidaException(
                "El usuario no es dueño de este tiquete.");
        }
    	
    	
    		duenoTiquete.actualizarSaldo(duenoTiquete.getSaldo() + tiqueteReal.getPrecioBaseSinCalcular());
    		duenoTiquete.eliminarTiquete(tiqueteReal);
    	
    	
    		HashMap<Tiquete,String> solicitudEliminar = null;
   
    		for (HashMap<Tiquete,String> solicitud : rembolsosSolicitados) {
    	        for (Map.Entry<Tiquete,String> entry : solicitud.entrySet()) {
    	            if (entry.getKey().getId().equals(tiqueteReal.getId())) {
    	                solicitudEliminar = solicitud;
    	                break;
    	            }
    	        }
    	        if (solicitudEliminar != null) break;
    	    }

    	    if (solicitudEliminar == null) {
    	        throw new TransferenciaNoPermitidaException(
    	            "No se encontró la solicitud de reembolso.");
    	    }
        
    	    if (aceptada) {
    
    	        
    	        double montoReembolso = tiqueteReal.getPrecioBaseSinCalcular();
    	        
    	        duenoTiquete.actualizarSaldo(duenoTiquete.getSaldo() + montoReembolso);
    	        

    	        duenoTiquete.eliminarTiquete(tiqueteReal);
    	        
    
    	        if (tiqueteReal.getEvento() != null) {
    	            tiqueteReal.setTransferido(false); 
    	            tiqueteReal.getEvento().agregarTiquete(tiqueteReal);
    	        }
    	        
    	        System.out.println("Reembolso ACEPTADO");
    	        System.out.println("Monto devuelto: $" + String.format("%.2f", montoReembolso));
    	        System.out.println("Nuevo saldo: $" + String.format("%.2f", duenoTiquete.getSaldo()));
    	        
    	    } else {
    
    	        System.out.println("✗ Reembolso RECHAZADO");
    	        System.out.println("  El tiquete permanece con el usuario.");
    	    }

    	    rembolsosSolicitados.remove(solicitudEliminar);

    	    sistema.guardarTodo();
    	}
    
    
    public Queue<HashMap<Tiquete, String>> getSolicitudes() {
    	return this.rembolsosSolicitados;
    }
    
    public Queue<HashMap<Venue, String>> getSolicitudesVenue() {
    	return this.solicitudesVenue;
    }
   


    public void verSolicitudVenue() {
        if (solicitudesVenue == null || solicitudesVenue.isEmpty()) {
            System.out.println("No hay solicitudes pendientes de aprobación de venues.");
            return;
        }

        try (Scanner sc = new Scanner(System.in)) {
            List<HashMap<Venue, String>> procesados = new ArrayList<>();

            for (HashMap<Venue, String> mapa : solicitudesVenue) {
                for (Entry<Venue, String> entry : mapa.entrySet()) {
                    Venue venueSolicitado = entry.getKey();
                    String mensaje = entry.getValue();

                    System.out.println("\n--- Solicitud de Venue ---");
                    System.out.println("Venue: " + venueSolicitado.getUbicacion());
                    System.out.println("Capacidad: " + venueSolicitado.getCapacidadMax());
                    System.out.println("Mensaje: " + mensaje);
                    System.out.println("[1] Aceptar");
                    System.out.println("[2] Rechazar");
                    System.out.print("Seleccione una opción: ");

                    int opcion = sc.nextInt();
                    sc.nextLine();

                    if (opcion == 1) {
                        venueSolicitado.setAprobado(true);
                        System.out.println("Venue aprobado: " + venueSolicitado.getUbicacion());
                    } else if (opcion == 2) {
                        venueSolicitado.setAprobado(false);
                        System.out.println("Venue rechazado: " + venueSolicitado.getUbicacion());
                    } else {
                        System.out.println("Opción no válida, se omite esta solicitud.");
                    }

                    procesados.add(mapa);
                }
            }

            solicitudesVenue.removeAll(procesados);
        }

        System.out.println("\nNo hay más solicitudes pendientes.");
    }



	
	public void verGananciasAdministrador(List<Tiquete> todosLosTiquetes) {
	    double total = calcularGananciaTotal(todosLosTiquetes);
	    HashMap<String, Double> porFecha = calcularGananciasPorFecha(todosLosTiquetes);
	    HashMap<Evento, Double> porEvento = calcularGananciasPorEvento(todosLosTiquetes);
	    HashMap<String, Double> porOrganizador = calcularGananciasPorOrganizador(todosLosTiquetes);

	    System.out.println("==== GANANCIAS DE LA TIQUETERA ====");
	    System.out.println("Ganancia total: $" + total);
	    System.out.println("Por fecha: " + porFecha);
	    System.out.println("Por evento: " + porEvento);
	    System.out.println("Por organizador: " + porOrganizador);
	}
	
	private double calcularGananciaTotal(List<Tiquete> tiquetes) {
	    double total = 0.0;
	    for (Tiquete t : tiquetes) {
	        if (t.isTransferido()) {
	            total += t.getRecargo() + cobroEmision;
	        }
	    }
	    setGanancias(total);
	    return total;
	}
	
	private HashMap<String, Double> calcularGananciasPorFecha(List<Tiquete> tiquetes) {
	    HashMap<String, Double> mapa = new HashMap<>();
	    for (Tiquete t : tiquetes) {
	        if (t.isTransferido()) {
	            String fecha = t.getEvento().getFecha();
	            mapa.put(fecha, mapa.getOrDefault(fecha, 0.0) + t.getRecargo() + cobroEmision);
	        }
	    }
	    return mapa;
	}
	
	private HashMap<Evento, Double> calcularGananciasPorEvento(List<Tiquete> tiquetes) {
	    HashMap<Evento, Double> mapa = new HashMap<>();
	    for (Tiquete t : tiquetes) {
	        if (t.isTransferido()) {
	            Evento evento = t.getEvento();
	            mapa.put(evento, mapa.getOrDefault(evento, 0.0) + t.getRecargo() + cobroEmision);
	        }
	    }
	    return mapa;
	}
	
	private HashMap<String, Double> calcularGananciasPorOrganizador(List<Tiquete> tiquetes) {
	    HashMap<String, Double> mapa = new HashMap<>();
	    for (Tiquete t : tiquetes) {
	        if (t.isTransferido()) {
	            String organizador = t.getEvento().getLoginOrganizador();
	            mapa.put(organizador, mapa.getOrDefault(organizador, 0.0) + t.getRecargo() + cobroEmision);
	        }
	    }
	    return mapa;
	}
	
	public void recibirSolicitudCancelacionEvento(SolicitudCancelacion solicitud) {
	    if (solicitud == null) return;
	    solicitudesCancelacionEvento.add(solicitud);
	    System.out.println("Admin: solicitud recibida para el evento '" 
	            + solicitud.getEvento().getNombre() + "' del organizador " 
	            + solicitud.getLoginOrganizador() + ".");
	}
	
	public void verSolicitudCancelacionEvento(SistemaPersistencia sistema) {
	    if (solicitudesCancelacionEvento.isEmpty()) {
	        System.out.println("No hay solicitudes de cancelación pendientes.");
	        return;
	    }

	    try (Scanner sc = new Scanner(System.in)) {
	        List<SolicitudCancelacion> procesadas = new ArrayList<>();
	        

	        for (SolicitudCancelacion sol : solicitudesCancelacionEvento) {
	            Evento ev = sol.getEvento();

	            System.out.println("\n--- Solicitud de Cancelación ---");
	            System.out.println("Evento: " + ev.getNombre());
	            System.out.println("Fecha: " + ev.getFecha() + "  Hora: " + ev.getHora());
	            System.out.println("Organizador: " + sol.getLoginOrganizador());
	            System.out.println("Motivo: " + sol.getMotivo());
	            System.out.println("Estado actual: " + sol.getEstado());
	            System.out.println("[1] Aprobar (CANCELAR evento)");
	            System.out.println("[2] Negar");
	            System.out.print("Seleccione una opción: ");

	            int opcion = sc.nextInt();
	            sc.nextLine(); // limpiar

	            if (opcion == 1) {
	       
	                ev.setCancelado();
	                ArrayList<Tiquete> tiqs = new ArrayList<>(ev.getTiquetesDisponibles());
	                for (Tiquete t : tiqs) {
	                    t.setAnulado(true);
	                }
	                
	                List<Evento> lista = sistema.getEventos();
	                for (int i = 0; i < lista.size(); i++) {
	                    if (lista.get(i).getNombre().equals(ev.getNombre())) {
	                        lista.set(i, ev);
	                        break;
	                    }
	                }

	                sol.setEstado("cancelado");
	                System.out.println("Solicitud aprobada. Evento CANCELADO y persistido.");
	                
	                
	                sistema.guardarTodo();
	                
	            } else if (opcion == 2) {
	                sol.setEstado("negado");
	                System.out.println("Solicitud NEGADA.");
	            } else {
	                System.out.println("Opción inválida. Se deja pendiente.");
	                continue; 
	            }

	            procesadas.add(sol);
	        }

	      
	        solicitudesCancelacionEvento.removeAll(procesadas);
	    }

	    System.out.println("No hay más solicitudes de cancelación.");



}
	
}
