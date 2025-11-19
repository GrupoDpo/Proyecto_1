package Finanzas;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import Persistencia.SistemaPersistencia;
import excepciones.OfertaNoDIsponibleException;
import excepciones.TransferenciaNoPermitidaException;
import tiquete.Tiquete;
import usuario.Administrador;
import usuario.IDuenoTiquetes;
import usuario.Usuario;

public class marketPlaceReventas {

    private Queue<HashMap<Tiquete, String>> ofertas;
    private List<String> logEventos;

    public marketPlaceReventas() {
        ofertas = new LinkedList<HashMap<Tiquete,String>>();
        logEventos = new ArrayList<String>();
    }

    public Queue<HashMap<Tiquete,String>> getOfertas() { return ofertas; }
    public List<String> getLogEventos() { return logEventos; }

    // ============================================================
    //  CREAR OFERTA
    // ============================================================
    public void crearOferta(Tiquete tiquete, double precio, Usuario vendedor, SistemaPersistencia sistema) {

        if (!(vendedor instanceof IDuenoTiquetes)) {
            System.out.println("El usuario no puede crear ofertas.");
            return;
        }

        HashMap<Tiquete, String> mapaOferta = new HashMap<Tiquete, String>();
        mapaOferta.put(tiquete, vendedor.getLogin() + " - Precio: $" + precio);

        ofertas.add(mapaOferta);

        String fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String registro = "[" + fechaHora + "] Oferta creada por " + vendedor.getLogin()
                          + " para tiquete " + tiquete.getId()
                          + " con precio $" + precio;

        logEventos.add(registro);

        System.out.println("Oferta creada exitosamente: " + registro);

        // Guardar todo el sistema
        sistema.guardarTodo();
    }


    // ============================================================
    //   ELIMINAR OFERTA
    // ============================================================
    public void eliminarOferta(Tiquete tiquete, Usuario vendedor, SistemaPersistencia sistema)
            throws OfertaNoDIsponibleException {

        HashMap<Tiquete, String> ofertaAEliminar = null;

        for (HashMap<Tiquete, String> mapa : ofertas) {
            if (mapa.containsKey(tiquete)) {
                ofertaAEliminar = mapa;
                break;
            }
        }

        if (ofertaAEliminar == null) {
            throw new OfertaNoDIsponibleException("No se encontró ninguna oferta para ese tiquete.");
        }

        ofertas.remove(ofertaAEliminar);

        String fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String registro = "[" + fechaHora + "] Oferta eliminada por "
                          + vendedor.getLogin() + " para tiquete " + tiquete.getId();

        logEventos.add(registro);
        System.out.println("Oferta eliminada: " + registro);

        sistema.guardarTodo();
    }



    // ============================================================
    //      REALIZAR CONTRAOFERTA
    // ============================================================
    public void registrarContraoferta(
            Tiquete tiquete,
            Usuario vendedor,
            Usuario comprador,
            double nuevoPrecio,
            SistemaPersistencia sistema) 
            throws TransferenciaNoPermitidaException {
        
        if (comprador.getLogin().equals(vendedor.getLogin())) {
            throw new TransferenciaNoPermitidaException("No puedes contraofertarte a ti mismo.");
        }
        
        if (nuevoPrecio <= 0) {
            throw new TransferenciaNoPermitidaException("Precio inválido.");
        }
        
        // Buscar el tiquete real en el sistema
        Tiquete tiqueteReal = sistema.buscarTiquetePorId(tiquete.getId());
        
        if (tiqueteReal == null) {
            throw new TransferenciaNoPermitidaException("Tiquete no encontrado.");
        }
        
        // Guardar la contraoferta en la lista del vendedor
        IDuenoTiquetes vendedorD = (IDuenoTiquetes) vendedor;
        HashMap<Tiquete,String> contraoferta = new HashMap<>();
        contraoferta.put(tiqueteReal, comprador.getLogin() + " - Contraoferta: $" + nuevoPrecio);
        vendedorD.getListaOfertas().add(contraoferta);
        
        // Se registra en el log
        String fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String registro = "[" + fechaHora + "] " + comprador.getLogin()
                          + " hizo una contraoferta a " + vendedor.getLogin()
                          + " por el tiquete " + tiqueteReal.getId()
                          + " con precio $" + nuevoPrecio;
        
        logEventos.add(registro);
        
        System.out.println("Contraoferta enviada exitosamente.");
        
        sistema.guardarTodo();
    }

    // ============================================================
    //  VER CONTRAOFERTAS (para el dueño del tiquete)
    // ============================================================
    public void procesarContraoferta(
            Tiquete tiquete,
            Usuario vendedor,
            Usuario comprador,
            boolean aceptada,
            double precio,
            SistemaPersistencia sistema) 
            throws TransferenciaNoPermitidaException {
        
        if (!(vendedor instanceof IDuenoTiquetes) || !(comprador instanceof IDuenoTiquetes)) {
            throw new TransferenciaNoPermitidaException("Usuarios inválidos.");
        }
        
        IDuenoTiquetes vendedorD = (IDuenoTiquetes) vendedor;
        IDuenoTiquetes compradorD = (IDuenoTiquetes) comprador;
        
        // Buscar tiquete real
        Tiquete tiqueteReal = sistema.buscarTiquetePorId(tiquete.getId());
        
        if (tiqueteReal == null) {
            throw new TransferenciaNoPermitidaException("Tiquete no encontrado.");
        }
        
        String fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        if (aceptada) {
            if (compradorD.getSaldo() < precio) {
                throw new TransferenciaNoPermitidaException("El comprador no tiene saldo suficiente.");
            }
            
            // Realizar la transferencia
            compradorD.actualizarSaldo(compradorD.getSaldo() - precio);
            vendedorD.actualizarSaldo(vendedorD.getSaldo() + precio);
            
            // Transferir el tiquete
            vendedorD.eliminarTiquete(tiqueteReal);
            compradorD.agregarTiquete(tiqueteReal);
            
            // Eliminar la oferta del marketplace
            HashMap<Tiquete,String> ofertaAEliminar = null;
            for (HashMap<Tiquete,String> oferta : ofertas) {
                if (oferta.containsKey(tiqueteReal)) {
                    ofertaAEliminar = oferta;
                    break;
                }
            }
            
            if (ofertaAEliminar != null) {
                ofertas.remove(ofertaAEliminar);
            }
            
            // se registra en el log
            String registro = "[" + fechaHora + "] Contraoferta ACEPTADA: "
                              + vendedor.getLogin() + " vendió tiquete " + tiqueteReal.getId()
                              + " a " + comprador.getLogin() + " por $" + precio;
            
            logEventos.add(registro);
            
            System.out.println("Contraoferta aceptada y tiquete transferido.");
            
        } else {
            // Rechazada
            String registro = "[" + fechaHora + "] Contraoferta RECHAZADA: "
                              + vendedor.getLogin() + " rechazó oferta de " + comprador.getLogin()
                              + " para tiquete " + tiqueteReal.getId();
            
            logEventos.add(registro);
            
            System.out.println(" Contraoferta rechazada.");
        }
        
        sistema.guardarTodo();
    }

    // ============================================================
    //  EXTRAER PRECIO
    // ============================================================
    public static double extraerPrecio(String etiqueta) throws TransferenciaNoPermitidaException {

        if (etiqueta == null) {
            throw new TransferenciaNoPermitidaException("Etiqueta inválida.");
        }

        String clave = null;
        int pos = -1;
        
        if (etiqueta.contains("Precio:")) {
            clave = "Precio:";
            pos = etiqueta.indexOf(clave);
        } else if (etiqueta.contains("Contraoferta:")) {
            clave = "Contraoferta:";
            pos = etiqueta.indexOf(clave);
        }

        if (pos < 0) {
            throw new TransferenciaNoPermitidaException("No se encontró 'Precio:' ni 'Contraoferta:'.");
        }

        String sub = etiqueta.substring(pos + clave.length()).trim();
        
        // Quitar el símbolo $
        if (sub.startsWith("$")) {
            sub = sub.substring(1);
        }

        try {
            return Double.parseDouble(sub);
        } catch (Exception e) {
            throw new TransferenciaNoPermitidaException("Precio inválido: " + sub);
        }
    }


    // ============================================================
    //  LOG
    // ============================================================
    public void verLogEventos(Usuario u) {

        if (!(u instanceof Administrador)) {
            System.out.println("Solo admin puede ver el log.");
            return;
        }

        if (logEventos.isEmpty()) {
            System.out.println("No hay eventos registrados.");
            return;
        }

        System.out.println("===== LOG MARKETPLACE =====");
        int i = 1;
        for (String r : logEventos) {
            System.out.println(i + ". " + r);
            i++;
        }
    }
}
