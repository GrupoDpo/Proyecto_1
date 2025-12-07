package Evento;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase que representa una solicitud de cancelación de evento
 * enviada por un Organizador y revisada por el Administrador
 */
public class SolicitudCancelacion {
    
    private Evento evento;
    private String loginOrganizador;
    private String motivo;
    private String estado; // "pendiente", "cancelado", "negado"
    private String fechaSolicitud;
    
    /**
     * Constructor de una solicitud de cancelación
     * @param evento Evento que se desea cancelar
     * @param loginOrganizador Login del organizador que solicita la cancelación
     * @param motivo Razón por la cual se solicita la cancelación
     */
    public SolicitudCancelacion(Evento evento, String loginOrganizador, String motivo) {
        this.evento = evento;
        this.loginOrganizador = loginOrganizador;
        this.motivo = motivo;
        this.estado = "pendiente";
        
        // Registrar fecha y hora de la solicitud
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.fechaSolicitud = LocalDateTime.now().format(formatter);
    }
    
    // ========================================
    // GETTERS
    // ========================================
    
    public Evento getEvento() {
        return evento;
    }
    
    public String getLoginOrganizador() {
        return loginOrganizador;
    }
    
    public String getMotivo() {
        return motivo;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public String getFechaSolicitud() {
        return fechaSolicitud;
    }
    
    // ========================================
    // SETTERS
    // ========================================
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    /**
     * Verifica si la solicitud está pendiente
     * @return true si está pendiente, false en caso contrario
     */
    public boolean isPendiente() {
        return "pendiente".equals(estado);
    }
    
    /**
     * Verifica si la solicitud fue aprobada (evento cancelado)
     * @return true si fue aprobada, false en caso contrario
     */
    public boolean isCancelado() {
        return "cancelado".equals(estado);
    }
    
    /**
     * Verifica si la solicitud fue negada
     * @return true si fue negada, false en caso contrario
     */
    public boolean isNegado() {
        return "negado".equals(estado);
    }
    
    @Override
    public String toString() {
        return String.format(
            "Solicitud[Evento: %s, Organizador: %s, Estado: %s, Fecha: %s]",
            evento != null ? evento.getNombre() : "N/A",
            loginOrganizador,
            estado,
            fechaSolicitud
        );
    }
}