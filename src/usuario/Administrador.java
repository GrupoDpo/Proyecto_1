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
import Evento.Venue;
import tiquete.Tiquete;

public class Administrador extends Usuario {

    private double porcentajeAdicional;
    private double cobroEmision;
    private double ganancias;
    private Queue<HashMap<Tiquete, String>> rembolsosSolicitados;

    public Administrador(String login, String password, String tipoUsuario) {
        super(login, password, tipoUsuario);
        this.porcentajeAdicional = 0.0;
        this.cobroEmision = 0.0;
        this.ganancias = 0.0;
        this.rembolsosSolicitados = new LinkedList<>();
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

        System.out.println("El evento '" + evento.getEntrada() + "' ha sido cancelado junto con sus tiquetes.");
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
    
    public double getGanancias() {
    	return this.ganancias;
    }
    
    public void setGanancias(double newGanancias) {
    	this.ganancias = newGanancias;
    }
    
   
    
   
  
    public void verSolicitud(Usuario dueno) {
        try (Scanner sc = new Scanner(System.in)) {
			List<HashMap<Tiquete, String>> procesados = new ArrayList<>();

			for (HashMap<Tiquete, String> mapa : rembolsosSolicitados) {
			    for (Entry<Tiquete, String> entry : mapa.entrySet()) {
			        Tiquete tiqueteRembolso = entry.getKey();
			        String motivo = entry.getValue();

			        System.out.println("--- Solicitud de reembolso ---");
			        System.out.println("Tiquete: " + tiqueteRembolso.getId());
			        System.out.println("Motivo: " + motivo);
			        System.out.println("[1] Aceptar");
			        System.out.println("[2] Rechazar");
			        System.out.print("Seleccione una opción: ");

			        int opcion = sc.nextInt(); 
			        sc.nextLine(); 

			        if (opcion == 1) {
			            if (dueno instanceof IDuenoTiquetes) {
			                IDuenoTiquetes duenoTiquete = (IDuenoTiquetes) dueno;
			                double dineroReembolso = tiqueteRembolso.calcularPrecio()
			                        - (tiqueteRembolso.calcularPrecio() * porcentajeAdicional)
			                        - cobroEmision;
			                duenoTiquete.actualizarSaldo(dineroReembolso);
			                duenoTiquete.eliminarTiquete(tiqueteRembolso);
			                System.out.println("Reembolso aceptado. Dinero devuelto: $" + dineroReembolso);
			            }
			        } else if (opcion == 2) {
			            System.out.println("Reembolso rechazado.");
			        }

			        procesados.add(mapa);
			    }
			}

			rembolsosSolicitados.removeAll(procesados);
		}
        System.out.println("No hay más solicitudes pendientes.");
    }
    
    
    public Queue<HashMap<Tiquete, String>> getSolicitudes() {
    	return this.rembolsosSolicitados;
    }
   


	public void aprobarORechazarVenue(Object object, boolean b) {
		// TODO Auto-generated method stub
		
	}


	public void verLogReventas() {
		// TODO Auto-generated method stub
		
	}
}
