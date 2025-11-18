package usuario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import Evento.Evento;
import Evento.Venue;
import Persistencia.SistemaPersistencia;
import tiquete.Tiquete;

public class Promotor extends Usuario implements IDuenoTiquetes {
	
	private double saldo;
	private ArrayList<Tiquete> tiquetes;
	private List<HashMap<Tiquete, String>> listaOfertas;
	private List<String> idsTiquetes = new ArrayList<>();

	public Promotor(String login, String password, double saldo, String tipoUsuario) {
		super(login, password, tipoUsuario);
		this.saldo = saldo;
		this.tiquetes = new ArrayList<Tiquete>();
		this.idsTiquetes = new ArrayList<String>();
	}
	
	public double getSaldo() {
		return this.saldo;
	}
	
	public void verGanancias(double cobroEmision) {
	    double gananciaTotal = 0.0;
	    int totalVendidos = 0;
	    int totalDisponibles = 0;

	    HashMap<Evento, Double> gananciasPorConcierto = new HashMap<>();
	    HashMap<Evento, Integer> vendidosPorConcierto = new HashMap<>();
	    HashMap<Evento, Integer> disponiblesPorConcierto = new HashMap<>();

	    for (Tiquete t : tiquetes) {
	        Evento evento = t.getEvento();
	        double precioBase = t.calcularPrecio(cobroEmision);

	        // Si está vendido, sumamos a ganancias
	        if (t.isTransferido()) {
	            gananciaTotal += precioBase;
	            totalVendidos++;

	            gananciasPorConcierto.put(evento,
	                gananciasPorConcierto.getOrDefault(evento, 0.0) + precioBase);
	            vendidosPorConcierto.put(evento,
	                vendidosPorConcierto.getOrDefault(evento, 0) + 1);
	        } else {
	            totalDisponibles++;
	            disponiblesPorConcierto.put(evento,
	                disponiblesPorConcierto.getOrDefault(evento, 0) + 1);
	        }
	    }

	    double porcentajeGlobal = 0.0;
	    if (totalVendidos + totalDisponibles > 0) {
	        porcentajeGlobal = (double) totalVendidos / (totalVendidos + totalDisponibles) * 100;
	    }

	    System.out.println("===== ESTADO FINANCIERO DEL PROMOTOR =====");
	    System.out.println("Ganancia total: $" + gananciaTotal);
	    System.out.println("Porcentaje de ventas global: " + String.format("%.2f", porcentajeGlobal) + "%");
	    System.out.println();

	    System.out.println("--- Detalle por concierto ---");
	    for (Evento evento : gananciasPorConcierto.keySet()) {
	        double g = gananciasPorConcierto.get(evento);
	        int vendidos = vendidosPorConcierto.getOrDefault(evento, 0);
	        int disponibles = disponiblesPorConcierto.getOrDefault(evento, 0);

	        double porcentaje = 0.0;
	        if (vendidos + disponibles > 0) {
	            porcentaje = (double) vendidos / (vendidos + disponibles) * 100;
	        }

	        System.out.println("Evento: " + evento);
	        System.out.println("  Ganancia: $" + g);
	        System.out.println("  Porcentaje de venta: " + String.format("%.2f", porcentaje) + "%");
	        System.out.println();
	    }
	}

	
	
	
	public double getPorcentajeVentaLocalidad(String identificadorEvento, String nombreLocalidad) {
		return saldo;
		
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

	@Override
	public String getTipoUsuario() {
		return "PROMOTOR";
	}
	
	
	@Override
	public void actualizarSaldo(double precioNuevo) {
		this.saldo = precioNuevo;
		
	}

	

	@Override
	public List<HashMap<Tiquete, String>> getListaOfertas() {
		return listaOfertas;
	}

	public void sugerirVenue(Venue venue, String mensaje,SistemaPersistencia sistema) {
		 Administrador admin = sistema.getAdministrador();
	    HashMap<Venue, String> solicitud = new HashMap<>();
	    solicitud.put(venue, mensaje);

	   
	    if (admin != null) {
	        admin.getSolicitudesVenue().add(solicitud);

	        sistema.guardarTodo();

	        System.out.println("Solicitud enviada correctamente al administrador.");
	    } else {
	        System.out.println("Error: no se encontró al administrador en el sistema.");
	    }
	}
	
	
	
}
