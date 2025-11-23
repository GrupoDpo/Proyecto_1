package Persistencia;

import java.util.*;
import java.time.LocalDateTime;

import usuario.Usuario;
import usuario.IDuenoTiquetes;
import usuario.Cliente;
import usuario.Promotor;
import usuario.Organizador;
import usuario.Administrador;

import Evento.Evento;
import Evento.Venue;
import Evento.Localidad;
import tiquete.PaqueteDeluxe;
import tiquete.Tiquete;
import tiquete.TiqueteSimple;
import tiquete.TiqueteMultiple;

import Finanzas.Transaccion;
import Finanzas.marketPlaceReventas;

/**
 * SistemaPersistencia FINAL
 * Maneja TODA la persistencia centralizada:
 *  - usuarios
 *  - eventos
 *  - tiquetes
 *  - transacciones
 *  - marketplace
 *
 * TODA la app trabaja a través de este sistema.
 */
public class SistemaPersistencia {
	
	private int contadorTiquetes = 0;

    private PersistenciaUsuarios perUsuarios;
    private PersistenciaEventos perEventos;
    private PersistenciaTiquetes perTiquetes;
    private PersistenciaTransacciones perTrans;
    private PersistenciaMarketplace perMarket;
    private PersistenciaVenues perVenues;

    // Datos en memoria
    private List<Usuario> usuarios;
    private List<Evento> eventos;
    private List<Tiquete> tiquetes;
    private List<Transaccion> transacciones;
    private marketPlaceReventas marketplace;
    private List<Venue> venues;

    public SistemaPersistencia() {
    	
    	
        perUsuarios = new PersistenciaUsuarios();
        perEventos = new PersistenciaEventos();
        perTiquetes = new PersistenciaTiquetes();
        perTrans = new PersistenciaTransacciones();
        perMarket = new PersistenciaMarketplace();
        perVenues  = new PersistenciaVenues();

        usuarios = new ArrayList<>();
        eventos = new ArrayList<>();
        tiquetes = new ArrayList<>();
        transacciones = new ArrayList<>();
        marketplace = new marketPlaceReventas();
        venues = new ArrayList<>();
        
    }

    // ===============================================================
    //                 CARGA COMPLETA DEL SISTEMA
    // ===============================================================
    public void cargarTodo() {
    	
    	String rawVenues = perVenues.cargar();
        venues = perVenues.reconstruir(rawVenues);


        // 1) Eventos
        String rawEventos = perEventos.cargar();
        eventos = perEventos.reconstruir(rawEventos);

        // 2) Tiquetes (requiere eventos)
        String rawTiq = perTiquetes.cargar();
        tiquetes = perTiquetes.reconstruir(rawTiq, eventos);
        
        
        
    
        System.out.println("Total tiquetes en sistema: " + tiquetes.size());
        
        // Contar por evento
        HashMap<String, Integer> porEvento = new HashMap<>();
        HashMap<String, Integer> vendidosPorEvento = new HashMap<>();
        
        for (Tiquete t : tiquetes) {
            String nombreEvento = t.getEvento() != null ? t.getEvento().getNombre() : "Sin evento";
            
            porEvento.put(nombreEvento, porEvento.getOrDefault(nombreEvento, 0) + 1);
            
            if (t.isTransferido()) {
                vendidosPorEvento.put(nombreEvento, vendidosPorEvento.getOrDefault(nombreEvento, 0) + 1);
            }
        }
        
        System.out.println("\nPor evento:");
        for (String ev : porEvento.keySet()) {
            int total = porEvento.get(ev);
            int vendidos = vendidosPorEvento.getOrDefault(ev, 0);
            int disponibles = total - vendidos;
            System.out.println("  " + ev + ": " + total + " total (" + disponibles + " disponibles, " + vendidos + " vendidos)");
        }
        System.out.println("====================================\n");
        

        // 3) Usuarios (requiere tiquetes)
        String rawUsers = perUsuarios.cargar();
        usuarios = perUsuarios.reconstruir(rawUsers, tiquetes);

 
        // SEGUNDO: Agregar solo tiquetes no vendidos
   

     for (Tiquete t : tiquetes) {
         Evento ev = t.getEvento();
         if (ev != null) {
             try {
                 // ✅ CRÍTICO: Solo agregar si NO está transferido
                 if (!t.isTransferido() && !t.isAnulado()) {
                     ev.agregarTiquete(t);
                 
                 } else {
                 }
             } catch (Exception e) {
                 System.out.println("Error agregando " + t.getId() + ": " + e.getMessage());
             }
         }
     }


     
      
        for (Tiquete t : tiquetes) {
            String id = t.getId();
            
            if (id.startsWith("TIQ-")) {
                try {
                    String numStr = id.substring(4);
                    int num = Integer.parseInt(numStr);
                    if (num > contadorTiquetes) {
                        contadorTiquetes = num;
                    }
                } catch (Exception e) {}
            } else {
                // IDs viejos con formato "Evento-Localidad-NUMERO"
                // Extraer el último número
                String[] partes = id.split("-");
                if (partes.length > 0) {
                    try {
                        String ultimaParte = partes[partes.length - 1];
                        int num = Integer.parseInt(ultimaParte);
                        if (num > contadorTiquetes) {
                            contadorTiquetes = num;
                        }
                    } catch (Exception e) {}
                }
            }
        }


        // 5) Transacciones (requiere usuarios y tiquetes)
        String rawTrans = perTrans.cargar();
        transacciones = perTrans.reconstruir(rawTrans, usuarios, tiquetes);

        // 6) Marketplace
        marketPlaceReventas cargado = perMarket.cargar();
        if (cargado != null) marketplace = cargado;

        // Vincular ofertas del marketplace a sus tiquetes reales
        reintegrarTiquetesMarketplace();
    }

    // ===============================================================
    //       REEMPLAZAR TIQUETES SOMBRA POR TIQUETES REALES
    // ===============================================================
    private void reintegrarTiquetesMarketplace() {

        Queue<HashMap<Tiquete,String>> nuevas = new LinkedList<>();

        for (HashMap<Tiquete,String> mapa : marketplace.getOfertas()) {

            HashMap<Tiquete,String> nuevoMapa = new HashMap<>();

            for (Map.Entry<Tiquete,String> e : mapa.entrySet()) {

                String id = e.getKey().getId();
                Tiquete real = buscarTiquetePorId(id);

                if (real != null) nuevoMapa.put(real, e.getValue());
            }

            if (!nuevoMapa.isEmpty())
                nuevas.add(nuevoMapa);
        }

        marketplace.getOfertas().clear();
        for (HashMap<Tiquete,String> m : nuevas) marketplace.getOfertas().add(m);
    }

    // ===============================================================
    //                   GUARDAR TODO EL SISTEMA
    // ===============================================================
    public void guardarTodo() {

        // Asegurar idsTiquetes en usuarios
    	for (Usuario u : usuarios) {
    	    if (u instanceof IDuenoTiquetes) {
    	        IDuenoTiquetes du = (IDuenoTiquetes) u;

    	        List<String> ids = new ArrayList<>();
    	        try {
    	            Collection<Tiquete> col = du.getTiquetes();
    	            
    	            if (col != null) {
    	                for (Tiquete t : col) {
    	                    ids.add(t.getId());
    	                   
    	                }
    	            }
    	
    	            
    	        } catch (Exception e) {
    	            System.out.println("ERROR: " + e.getMessage());
    	        }

    	        du.setIdsTiquetes(ids);
    	    }
    	}

        perUsuarios.guardar(generarJsonUsuarios());
        perEventos.guardar(generarJsonEventos());
        perTiquetes.guardar(generarJsonTiquetes());
        perTrans.guardar(generarJsonTransacciones());
        perMarket.guardar(marketplace);
        perVenues.guardar(generarJsonVenues());
    }
    
    private String generarJsonVenues() {

        StringBuilder sb = new StringBuilder();
        sb.append("{\n  \"venues\": [\n");

        for (int i = 0; i < venues.size(); i++) {
            Venue v = venues.get(i);

            sb.append("    {\n");
            sb.append("      \"ubicacion\": \"" + TextoUtils.escape(v.getUbicacion()) + "\",\n");
            sb.append("      \"capacidadMax\": " + v.getCapacidadMax() + ",\n");
            sb.append("      \"aprobado\": " + v.isAprobado() + "\n");
            sb.append("    }");
            if (i < venues.size() - 1) sb.append(",");
            sb.append("\n");
        }

        sb.append("  ]\n}");
        return sb.toString();
    }

    // ===============================================================
    //                     BUSQUEDAS RÁPIDAS
    // ===============================================================
    public Usuario buscarUsuario(String login) {
        if (login == null) return null;

        for (Usuario u : usuarios)
            if (login.equals(u.getLogin()))
                return u;

        return null;
    }

    public Evento buscarEventoPorNombre(String nombre) {
        if (nombre == null) return null;

        for (Evento e : eventos)
            if (nombre.equals(e.getNombre()))
                return e;

        return null;
    }

    public Tiquete buscarTiquetePorId(String id) {
        if (id == null) return null;

        for (Tiquete t : tiquetes)
            if (id.equals(t.getId()))
                return t;

        return null;
    }

    public Administrador getAdministrador() {
        for (Usuario u : usuarios)
            if (u instanceof Administrador)
                return (Administrador) u;

        return null;
    }

    // ===============================================================
    //       MÉTODOS PARA AGREGAR ELEMENTOS EN MEMORIA
    // ===============================================================
    public void agregarUsuario(Usuario u) { 
    	usuarios.add(u); 
    	}
    public void agregarEvento(Evento e) { 
    	eventos.add(e);
    	}
    public void agregarTiquete(Tiquete t) { 
    	tiquetes.add(t);
}
    public void agregarTransaccion(Transaccion t) { transacciones.add(t); 
    }
    public void agregarVenue(Venue v) {
    	venues.add(v);
    }

    // ===============================================================
    //        ACCESO AL MARKETPLACE CENTRALIZADO
    // ===============================================================
    public marketPlaceReventas getMarketplace() {
        return marketplace;
    }

    // ===============================================================
    //        GENERACIÓN DE JSON (USO INTERNO)
    // ===============================================================
    private String generarJsonUsuarios() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n  \"usuarios\": [\n");

        for (int i = 0; i < usuarios.size(); i++) {
            Usuario u = usuarios.get(i);

            sb.append("    {\n");
            sb.append("      \"login\": \"" + TextoUtils.escape(u.getLogin()) + "\",\n");
            sb.append("      \"password\": \"" + TextoUtils.escape(u.getPasswordInternal()) + "\",\n");
            sb.append("      \"tipo\": \"" + TextoUtils.escape(u.getTipoUsuario()) + "\"");

            // saldo para tipos que lo tengan
            if (u instanceof Cliente) sb.append(",\n      \"saldo\": " + ((Cliente) u).getSaldo());
            if (u instanceof Promotor) sb.append(",\n      \"saldo\": " + ((Promotor) u).getSaldo());
            if (u instanceof Organizador) sb.append(",\n      \"saldo\": " + ((Organizador) u).getSaldo());

            // idsTiquetes para usuarios que las tengan
            if (u instanceof IDuenoTiquetes) {
                List<String> ids = ((IDuenoTiquetes) u).getIdsTiquetes();
                sb.append(",\n      \"idsTiquetes\": [");
                if (ids != null && !ids.isEmpty()) {
                    for (int j = 0; j < ids.size(); j++) {
                        sb.append("\"").append(TextoUtils.escape(ids.get(j))).append("\"");
                        if (j < ids.size() - 1) sb.append(", ");
                    }
                }
                sb.append("]");
            }

            sb.append("\n    }");

            if (i < usuarios.size() - 1)
                sb.append(",");
            sb.append("\n");
        }

        sb.append("  ]\n}");
        return sb.toString();
    }



    private String generarJsonEventos() {

        StringBuilder sb = new StringBuilder();
        sb.append("{\n  \"eventos\": [\n");

        for (int i = 0; i < eventos.size(); i++) {

            Evento e = eventos.get(i);

            sb.append("    {\n");
            sb.append("      \"nombre\": \"" + TextoUtils.escape(e.getNombre()) + "\",\n");
            sb.append("      \"fecha\": \"" + TextoUtils.escape(e.getFecha()) + "\",\n");
            sb.append("      \"hora\": \"" + TextoUtils.escape(e.getHora()) + "\",\n");
            sb.append("      \"loginOrganizador\": \"" + TextoUtils.escape(e.getLoginOrganizador()) + "\",\n");
            sb.append("      \"cancelado\": " + e.getCancelado() + ",\n");

            Venue v = e.getVenueAsociado();
            if (v != null) {
                sb.append("      \"venueUbicacion\": \"" + TextoUtils.escape(v.getUbicacion()) + "\",\n");
                sb.append("      \"venueCapMax\": " + v.getCapacidadMax() + ",\n");
                sb.append("      \"venueAprobado\": " + v.isAprobado() + "\n");
            } else {
                sb.append("      \"venueUbicacion\": \"\",\n");
                sb.append("      \"venueCapMax\": 0,\n");
                sb.append("      \"venueAprobado\": false\n");
            }

            sb.append("    }");
            if (i < eventos.size() - 1) sb.append(",");
            sb.append("\n");
        }

        sb.append("  ]\n}");
        return sb.toString();
    }


    private String generarJsonTiquetes() {

        StringBuilder sb = new StringBuilder();
        sb.append("{\n  \"tiquetes\": [\n");

        for (int i = 0; i < tiquetes.size(); i++) {

            Tiquete t = tiquetes.get(i);

            sb.append("    {\n");
            sb.append("      \"tipoTiquete\": \"" + TextoUtils.escape(t.getTipoTiquete()) + "\",\n");
            sb.append("      \"id\": \"" + TextoUtils.escape(t.getId()) + "\",\n");
            sb.append("      \"precio\": " + t.getPrecioBaseSinCalcular() + ",\n");
            sb.append("      \"recargo\": " + t.getRecargo() + ",\n");
            sb.append("      \"fechaExpiracion\": \"" + TextoUtils.escape(t.getFechaExpiracion()) + "\",\n");
            sb.append("      \"nombre\": \"" + TextoUtils.escape(t.getNombre()) + "\",\n");
            sb.append("      \"transferido\": " + t.isTransferido() + ",\n");
            sb.append("      \"anulado\": " + t.isAnulado() + ",\n");

            Evento ev = t.getEvento();
            sb.append("      \"eventoAsociado\": \"" + (ev != null ? TextoUtils.escape(ev.getNombre()) : "") + "\",\n");

            Localidad loc = t.getLocalidadAsociada();
            if (loc != null) {
                sb.append("      \"localidadNombre\": \"" + TextoUtils.escape(loc.getNombre()) + "\",\n");
                sb.append("      \"localidadPrecio\": " + loc.calcularPrecio() + ",\n");
                sb.append("      \"localidadCapacidad\": " + loc.getCapacidad() + ",\n");
                sb.append("      \"localidadTipo\": " + loc.getTipo());
            } else {
                sb.append("      \"localidadNombre\": \"\",\n");
                sb.append("      \"localidadPrecio\": 0,\n");
                sb.append("      \"localidadCapacidad\": 0,\n");
                sb.append("      \"localidadTipo\": 0");
            }

            // ===== AGREGAR IDS DE TIQUETES INTERNOS SI ES MÚLTIPLE =====
            if (t instanceof TiqueteMultiple) {
                TiqueteMultiple tm = (TiqueteMultiple) t;
                sb.append(",\n      \"idsTiquetesInternos\": [");
                
                List<TiqueteSimple> internos = tm.getTiquetes();
                if (internos != null && !internos.isEmpty()) {
                    for (int j = 0; j < internos.size(); j++) {
                        sb.append("\"").append(TextoUtils.escape(internos.get(j).getId())).append("\"");
                        if (j < internos.size() - 1) sb.append(", ");
                    }
                }
                sb.append("]");
            }

            sb.append("\n    }");

            if (i < tiquetes.size() - 1)
                sb.append(",");

            sb.append("\n");
        }

        sb.append("  ]\n}");
        return sb.toString();
    }


    private String generarJsonTransacciones() {

        StringBuilder sb = new StringBuilder();
        sb.append("{\n  \"transacciones\": [\n");

        for (int i = 0; i < transacciones.size(); i++) {

            Transaccion t = transacciones.get(i);

            sb.append("    {\n");
            sb.append("      \"tipo\": \"" + TextoUtils.escape(t.getTipoTransaccion()) + "\",\n");

            sb.append("      \"fecha\": \"" + (t.getFecha() != null ? t.getFecha().toString() : "") + "\",\n");

            sb.append("      \"valor\": " + t.getValorTransaccion() + ",\n");

            String loginDueno = t.getDueno() != null ? t.getDueno().getLogin() : "";
            sb.append("      \"dueno\": \"" + TextoUtils.escape(loginDueno) + "\",\n");

            String idT = (t.getTiquete() != null ? t.getTiquete().getId() : "");
            sb.append("      \"idTiquete\": \"" + TextoUtils.escape(idT) + "\"\n");

            sb.append("    }");

            if (i < transacciones.size() - 1)
                sb.append(",");

            sb.append("\n");
        }

        sb.append("  ]\n}");
        return sb.toString();
    }
    
    
    
    // ===============================================================
    //             GETTERS PARA CONSOLA / LÓGICA
    // ===============================================================
    public List<Usuario> getUsuarios() { 
    	return usuarios; 
    	}
    public List<Evento> getEventos() {
    	return eventos; 
    	}
    public List<Tiquete> getTiquetes() { 
    	return tiquetes; 
    	}
    public List<Transaccion> getTransacciones() {
    	return transacciones; 
    	}
    
    
    

    public synchronized String generarIdTiquete() {
        contadorTiquetes++;
        return "TIQ-" + String.format("%08d", contadorTiquetes);
    }

    public int getContadorTiquetes() {
        return contadorTiquetes;
    }
}
