package Evento;
import java.io.Serializable;
import java.time.LocalDateTime;

public class SolicitudCancelacion implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String loginOrganizador;
    private final String motivo;
    private final Evento evento;          
    private final LocalDateTime fecha;  
    private String estado;         

    public SolicitudCancelacion(String loginOrganizador, Evento evento, String motivo) {
        this.loginOrganizador = loginOrganizador;
        this.evento = evento;
        this.motivo = motivo;
        this.fecha = LocalDateTime.now();
        this.estado = "pendiente";
    }

    public String getLoginOrganizador() { 
    	return loginOrganizador; 
    	}
    public String getMotivo() { 
    	return motivo;
    	}
    public Evento getEvento() { 
    	return evento; 
    	}
    public LocalDateTime getFecha() { 
    	return fecha; 
    	}
    public String getEstado() { 
    	return estado;
    	}
    public void setEstado(String estado) {
    	this.estado = estado; 
    	}

	
}