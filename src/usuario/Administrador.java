package usuario;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import Evento.Evento;
import Evento.Venue;
import tiquete.Tiquete;

public class Administrador extends Usuario {

    private double porcentajeAdicional;
    private double cobroEmision;
    private double ganancias;
    private Queue<String> rembolsosSolicitados;

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
    
   
    
   
   public void verSolicitud() {
	   for (i = 0)
	   
   }
    
   public String aprobarRembolso(Tiquete tiqueteRembolso) { 
	   
   }


	public void aprobarORechazarVenue(Object object, boolean b) {
		// TODO Auto-generated method stub
		
	}


	public void verLogReventas() {
		// TODO Auto-generated method stub
		
	}
}
