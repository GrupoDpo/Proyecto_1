package Finanzas;

import usuario.Administrador;
import usuario.IDuenoTiquetes;
import usuario.Usuario;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

import Evento.Evento;
import Persistencia.SistemaPersistencia;
import excepciones.IDNoEncontrado;
import excepciones.SaldoInsuficienteExeption;
import excepciones.TiquetesNoDisponiblesException;
import excepciones.TiquetesVencidosTransferidos;
import excepciones.TransferenciaNoPermitidaException;
import tiquete.PaqueteDeluxe;
import tiquete.Tiquete;
import tiquete.TiqueteMultiple;


public class Transaccion {

    private Tiquete tiquete;
    private Usuario dueno;
    private LocalDateTime fecha;
    private static final double NUMERO_MAX_TRANSACCION = 10;
    private String tipoTransaccion;
    private double valorTransaccion;
    private Registro registro;

    public Transaccion(String tipoTransaccion, Tiquete tiquete, Usuario dueno,
                       LocalDateTime localDateTime, Registro registro, double valorTransaccion) {

        this.tiquete = tiquete;
        this.dueno = dueno;
        this.fecha = localDateTime;
        this.registro = registro;
        this.tipoTransaccion = tipoTransaccion;
        this.valorTransaccion = valorTransaccion;
    }

    public Usuario getDueno() { return dueno; }
    public Tiquete getTiquete() { return tiquete; }
    public LocalDateTime getFecha() { return fecha; }
    public String getTipoTransaccion() { return tipoTransaccion; }
    public double getValorTransaccion() { return valorTransaccion; }
    public Registro getRegistro() { return registro; }

    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public void setTipoTransaccion(String tipoTransaccion) { this.tipoTransaccion = tipoTransaccion; }
    public void setValorTransaccion(double valorTransaccion) { this.valorTransaccion = valorTransaccion; }
    public void setRegistro(Registro registro) { this.registro = registro; }


    public void transferirTiquete(
            Tiquete tiquete,
            Usuario dueno,
            Usuario usuarioDestino,
            String fechaActual,
            SistemaPersistencia sistema)
            throws TiquetesVencidosTransferidos, IDNoEncontrado, TransferenciaNoPermitidaException {

        // Validar que ambos usuarios puedan tener tiquetes
        if (!(dueno instanceof IDuenoTiquetes)) {
            throw new TransferenciaNoPermitidaException("El remitente no puede poseer tiquetes");
        }

        if (!(usuarioDestino instanceof IDuenoTiquetes)) {
            throw new TransferenciaNoPermitidaException("El destinatario no puede poseer tiquetes");
        }

        IDuenoTiquetes duenoTiquetes = (IDuenoTiquetes) dueno;
        IDuenoTiquetes destinoTiquetes = (IDuenoTiquetes) usuarioDestino;

        // Validar que el dueño tenga el tiquete
        boolean tieneTiquete = false;
        for (Tiquete t : duenoTiquetes.getTiquetes()) {
            if (t.getId().equals(tiquete.getId())) {
                tieneTiquete = true;
                break;
            }
        }

        if (!tieneTiquete) {
            throw new IDNoEncontrado("No posees este tiquete");
        }

        // Validar que el tiquete esté comprado (transferido = true significa que fue vendido)
        if (!tiquete.isTransferido()) {
            throw new TransferenciaNoPermitidaException(
                "Este tiquete aún no ha sido comprado, no se puede transferir");
        }

        // ===== TRANSFERIR SEGÚN TIPO =====
        if (tiquete instanceof TiqueteMultiple) {
            
            TiqueteMultiple tm = (TiqueteMultiple) tiquete;

            // Validar que todos los tiquetes del paquete estén vigentes
            for (Tiquete t : tm.getTiquetes()) {
                if (!t.esVigente(fechaActual)) {
                    throw new TiquetesVencidosTransferidos(
                        "El paquete contiene tiquetes vencidos");
                }
            }

            // Transferir el paquete completo
            // NOTA: NO cambiamos el flag transferido, solo movemos entre usuarios
            duenoTiquetes.eliminarTiquete(tm);
            destinoTiquetes.agregarTiquete(tm);

            System.out.println("✓ Paquete transferido exitosamente.");

        } else {
            
            // TIQUETE SIMPLE
            if (!tiquete.esVigente(fechaActual)) {
                throw new TiquetesVencidosTransferidos("Tiquete vencido");
            }

            // Transferir
            // NOTA: NO cambiamos el flag transferido, solo movemos entre usuarios
            duenoTiquetes.eliminarTiquete(tiquete);
            destinoTiquetes.agregarTiquete(tiquete);

            System.out.println("✓ Tiquete transferido exitosamente.");
        }
        
        
        

        // ===== REGISTRAR TRANSACCIÓN =====
        Registro registro = new Registro(dueno, tiquete, usuarioDestino);
        Transaccion trans = new Transaccion(
            "TRANSFERENCIA", 
            tiquete, 
            dueno, 
            LocalDateTime.now(), 
            registro, 
            0
        );

        sistema.agregarTransaccion(trans);
        sistema.guardarTodo();

        System.out.println("  De: " + dueno.getLogin());
        System.out.println("  A: " + usuarioDestino.getLogin());
        System.out.println("  Tiquete: " + tiquete.getId());
    }


    // ===============================================================
    //  COMPRAR TIQUETE
    // ===============================================================
    public ArrayList<Tiquete> comprarTiquete(
            Tiquete tiqueteComprar,
            Usuario comprador,
            int cantidad,
            Evento eventoAsociado,
            double cobroEmision,
            SistemaPersistencia sistema)
            throws TiquetesNoDisponiblesException, TransferenciaNoPermitidaException, SaldoInsuficienteExeption {
    	
    	

        ArrayList<Tiquete> comprados = new ArrayList<Tiquete>();

        if (eventoAsociado.getTiquetesDisponibles().isEmpty())
            throw new TiquetesNoDisponiblesException("No hay tiquetes disponibles.");

        if (cantidad > NUMERO_MAX_TRANSACCION)
            throw new TransferenciaNoPermitidaException("Supera máximo permitido.");

        if (!(comprador instanceof IDuenoTiquetes))
            throw new TransferenciaNoPermitidaException("Usuario no puede comprar.");

        IDuenoTiquetes dueno = (IDuenoTiquetes) comprador;

        double total = (tiqueteComprar instanceof TiqueteMultiple)
                ? tiqueteComprar.calcularPrecio(cobroEmision)
                : tiqueteComprar.calcularPrecio(cobroEmision) * cantidad;

        if (total > dueno.getSaldo()) 
            throw new SaldoInsuficienteExeption("Saldo insuficiente.");

        dueno.actualizarSaldo(dueno.getSaldo() - total);

        if (tiqueteComprar instanceof TiqueteMultiple) {

            TiqueteMultiple tm = (TiqueteMultiple) tiqueteComprar;

            for (Tiquete t : tm.getTiquetes()) {
                dueno.agregarTiquete(t);
                eventoAsociado.venderTiquete(t.getId());
                comprados.add(t);
            }

        } else {
            
            String localidadBuscada = tiqueteComprar.getLocalidadAsociada() != null 
                ? tiqueteComprar.getLocalidadAsociada().getNombre() 
                : null;
            
            int compradosCount = 0;
            
            HashMap<String, Tiquete> disponibles = eventoAsociado.getTiquetes();
            
           
            ArrayList<Tiquete> listaDisponibles = new ArrayList<>(disponibles.values());
            
            for (Tiquete t : listaDisponibles) {  // ← Iterar sobre la copia
                if (compradosCount >= cantidad) break;
                
                String localidadActual = t.getLocalidadAsociada() != null 
                    ? t.getLocalidadAsociada().getNombre() 
                    : null;
                
                boolean mismaLocalidad = (localidadBuscada == null && localidadActual == null) ||
                                        (localidadBuscada != null && localidadBuscada.equals(localidadActual));
                
                if (mismaLocalidad && !t.isTransferido() && !t.isAnulado()) {
                    dueno.agregarTiquete(t);
                    
                    t.setTransferido(true);
                    eventoAsociado.venderTiquete(t.getId()); 
                    comprados.add(t);
                    compradosCount++;
                }
            }
            
            if (compradosCount < cantidad) {
                throw new TiquetesNoDisponiblesException(
                    "Solo hay " + compradosCount + " tiquetes disponibles de esta localidad.");
            }
        }
        
        double precioBaseTotal;
        double precioFinalTotal;

        if (tiqueteComprar instanceof TiqueteMultiple) {
            // Asumimos que la cantidad de paquetes es 1 en este flujo
            double precioBaseUnitario = tiqueteComprar.getPrecioBaseSinCalcular();
            double precioFinalUnitario = tiqueteComprar.calcularPrecio(cobroEmision);

            precioBaseTotal = precioBaseUnitario;
            precioFinalTotal = precioFinalUnitario;
        } else {
            double precioBaseUnitario = tiqueteComprar.getPrecioBaseSinCalcular();
            double precioFinalUnitario = tiqueteComprar.calcularPrecio(cobroEmision);

            precioBaseTotal = precioBaseUnitario * cantidad;
            precioFinalTotal = precioFinalUnitario * cantidad;
        }

        // Lo que gana la tiquetera: SOLO sobrecargos fijados por el admin
        double ingresoAdminTotal = precioFinalTotal - precioBaseTotal;

        // Llamamos al helper que actualiza el EstadosFinancieros del evento y persiste
        actualizarEstadosFinancierosEvento(eventoAsociado, precioBaseTotal, ingresoAdminTotal);

        Registro registro = new Registro(comprador, tiqueteComprar, null);

        Transaccion trans = new Transaccion(
                "COMPRA",
                tiqueteComprar,
                comprador,
                LocalDateTime.now(),
                registro,
                total);

        sistema.agregarTransaccion(trans);
        sistema.guardarTodo();

        return comprados;
    }
    
    public ArrayList<Tiquete> comprarPaqueteDeluxe(
            PaqueteDeluxe paquete,
            Usuario comprador,
            int cantidad,
            Evento eventoAsociado,
            SistemaPersistencia sistema)
            throws TransferenciaNoPermitidaException, TiquetesNoDisponiblesException, SaldoInsuficienteExeption {

        ArrayList<Tiquete> tiquetesComprados = new ArrayList<>();

        // --- Validaciones básicas ---
        if (paquete == null || comprador == null || eventoAsociado == null) {
            throw new TransferenciaNoPermitidaException("Datos inconsistentes para la compra.");
        }
        if (cantidad <= 0) {
            throw new TransferenciaNoPermitidaException("La cantidad debe ser mayor que 0.");
        }
        if (cantidad > NUMERO_MAX_TRANSACCION) {
            throw new TransferenciaNoPermitidaException("Supera el número máximo de tiquetes por transacción.");
        }
        // Por enunciado y por simplicidad: 1 paquete por transacción
        if (cantidad != 1) {
            throw new TransferenciaNoPermitidaException("Solo se soporta comprar 1 Paquete Deluxe por transacción.");
        }

        // --- Reunir todos los tiquetes que trae el paquete ---
        ArrayList<Tiquete> tiqsPaquete = new ArrayList<>();
        if (paquete.getTiquetes() != null) {
            for (Tiquete t : paquete.getTiquetes()) {
                if (t != null) {
                	tiqsPaquete.add(t);
                }
            }
        }
        if (paquete.getTiquetesAdicionales() != null) {
            for (Tiquete t : paquete.getTiquetesAdicionales()) {
                if (t != null) {
                	tiqsPaquete.add(t);
                }
            }
        }

        if (tiqsPaquete.isEmpty()) {
            throw new TiquetesNoDisponiblesException("El paquete deluxe no contiene tiquetes.");
        }

        // --- Verificar disponibilidad en el evento ---
        for (Tiquete t : tiqsPaquete) {
            Tiquete enEvento = eventoAsociado.getTiquetePorId(t.getId());
            if (enEvento == null) {
                throw new TiquetesNoDisponiblesException(
                    "No hay disponible el tiquete en el evento: " + t.getId());
            }
        }

        // --- Calcular total a pagar ---
        double total = paquete.getPrecioPaquete();

        // Si el comprador es ORGANIZADOR, es cortesía → total = 0
        if ("ORGANIZADOR".equalsIgnoreCase(comprador.getTipoUsuario())) {
            total = 0.0;
        }

        // --- Verificar saldo y actualizar ---
        if (!(comprador instanceof IDuenoTiquetes)) {
            throw new TransferenciaNoPermitidaException("El comprador no puede poseer tiquetes.");
        }

        IDuenoTiquetes dueno = (IDuenoTiquetes) comprador;
        double saldo = dueno.getSaldo();

        if (total > saldo) {
            throw new SaldoInsuficienteExeption("Saldo insuficiente");
        }

        if (total > 0) {
            dueno.actualizarSaldo(saldo - total);
        }

        // --- Marcar tiquetes como transferidos y asignarlos al comprador ---
        for (Tiquete t : tiqsPaquete) {
            // Por enunciado: tiquetes de Paquete Deluxe NO se pueden transferir luego.
            // Aquí podrías marcar un flag adicional en Tiquete si quieres reforzar esa regla.
            t.setTransferido(true);
        }

        for (Tiquete t : tiqsPaquete) {
            dueno.agregarTiquete(t);
            eventoAsociado.venderTiquete(t.getId());
            tiquetesComprados.add(t);
        }

        // --- Actualizar estados financieros del evento ---
        double precioBaseTotal = 0.0;
        for (Tiquete t : tiqsPaquete) {
            precioBaseTotal += t.getPrecioBaseSinCalcular();
        }

        double precioFinalTotal = paquete.getPrecioPaquete();
        double ingresoAdminTotal = Math.max(0.0, precioFinalTotal - precioBaseTotal);

        actualizarEstadosFinancierosEvento(eventoAsociado, precioBaseTotal, ingresoAdminTotal);

        // --- Registrar transacción en el sistema ---
        Registro registro = new Registro(comprador, null, null); 
        Transaccion trans = new Transaccion(
                "COMPRA_PAQUETE_DELUXE",
                null,
                comprador,
                LocalDateTime.now(),
                registro,
                total);

        sistema.agregarTransaccion(trans);
        sistema.guardarTodo();

        return tiquetesComprados;
    }



    // ===============================================================
    //  SOLICITAR REEMBOLSO
    // ===============================================================
    public void solicitarReembolso(
            Tiquete tiqueteRembolso,
            String motivo,
            SistemaPersistencia sistema) {

        Usuario admin = sistema.getAdministrador();

        if (!(admin instanceof Administrador)) {
            System.out.println("No existe administrador.");
            return;
        }

        Administrador a = (Administrador) admin;

        HashMap<Tiquete, String> solicitud = new HashMap<Tiquete, String>();
        solicitud.put(tiqueteRembolso, motivo);
        a.getSolicitudes().add(solicitud);

        sistema.guardarTodo();
    }


    // ===============================================================
    //  REVENTA → MARKETPLACE
    // ===============================================================
    public void revenderTiquete(Tiquete tiqueteVenta, double precio, Usuario vendedor, SistemaPersistencia sistema) {

        sistema.getMarketplace().crearOferta(tiqueteVenta, precio, vendedor, sistema);

        Registro registro = new Registro(vendedor, tiqueteVenta, null);
        Transaccion trans = new Transaccion("PUBLICAR_VENTA", tiqueteVenta, vendedor,
                LocalDateTime.now(), registro, 0);

        sistema.agregarTransaccion(trans);
        sistema.guardarTodo();
    }


    // ===============================================================
    //  COMPRAR TIQUETE EN MARKETPLACE
    // ===============================================================
    public ArrayList<Tiquete> comprarEnMarketplace(
            Tiquete tiquetePublicado,
            Usuario vendedor,
            Usuario comprador,
            SistemaPersistencia sistema)
            throws TiquetesNoDisponiblesException, TransferenciaNoPermitidaException, SaldoInsuficienteExeption {

        if (!(vendedor instanceof IDuenoTiquetes) || !(comprador instanceof IDuenoTiquetes))
            throw new TransferenciaNoPermitidaException("Usuarios inválidos.");

        if (vendedor.getLogin().equals(comprador.getLogin()))
            throw new TransferenciaNoPermitidaException("No puedes comprarte a ti mismo.");

        IDuenoTiquetes vend = (IDuenoTiquetes) vendedor;
        IDuenoTiquetes comp = (IDuenoTiquetes) comprador;

        // BUSCAR EN EL MARKETPLACE GLOBAL
        marketPlaceReventas marketplace = sistema.getMarketplace();
        Queue<HashMap<Tiquete,String>> todasOfertas = marketplace.getOfertas();

        if (todasOfertas == null || todasOfertas.isEmpty())
            throw new TiquetesNoDisponiblesException("No hay ofertas en el marketplace.");

        HashMap<Tiquete,String> ofertaEncontrada = null;
        String etiqueta = null;
        Usuario vendedorReal = null;

        // Buscar la oferta del tiquete
        for (HashMap<Tiquete,String> oferta : todasOfertas) {
            for (java.util.Map.Entry<Tiquete,String> e : oferta.entrySet()) {
                Tiquete t = e.getKey();
                
                if (t.getId().equals(tiquetePublicado.getId())) {
                    
                    // Verificar que el vendedor pasado sea el dueño actual
                    // Buscar quién tiene este tiquete
                    for (Usuario u : sistema.getUsuarios()) {
                        if (u instanceof IDuenoTiquetes) {
                            IDuenoTiquetes dueno = (IDuenoTiquetes) u;
                            
                            for (Tiquete tiq : dueno.getTiquetes()) {
                                if (tiq.getId().equals(t.getId())) {
                                    vendedorReal = u;
                                    break;
                                }
                            }
                        }
                        if (vendedorReal != null) break;
                    }
                    
                    // Verificar que coincida con el vendedor pasado
                    if (vendedorReal != null && vendedorReal.getLogin().equals(vendedor.getLogin())) {
                        ofertaEncontrada = oferta;
                        etiqueta = e.getValue();
                        break;
                    }
                }
            }
            if (ofertaEncontrada != null) break;
        }

        if (ofertaEncontrada == null || etiqueta == null)
            throw new TiquetesNoDisponiblesException("No se encontró la oferta o no pertenece al vendedor indicado.");

        // Extraer precio de la etiqueta
        double precio = marketPlaceReventas.extraerPrecio(etiqueta);

        if (precio > comp.getSaldo())
            throw new SaldoInsuficienteExeption("Saldo insuficiente.");

        // ===== REALIZAR TRANSACCIÓN =====
        
        // Actualizar saldos
        comp.actualizarSaldo(comp.getSaldo() - precio);
        vend.actualizarSaldo(vend.getSaldo() + precio);

        // Transferir tiquete
        vend.eliminarTiquete(tiquetePublicado);
        comp.agregarTiquete(tiquetePublicado);
        
        // NOTA: NO cambiar transferido porque ya está en true (fue comprado antes)

        // Remover oferta del marketplace
        marketplace.getOfertas().remove(ofertaEncontrada);

        // Registrar transacción
        Registro registro = new Registro(vendedor, tiquetePublicado, comprador);

        Transaccion trans = new Transaccion(
            "COMPRA_MARKETPLACE",
            tiquetePublicado,
            vendedor,
            LocalDateTime.now(),
            registro,
            precio
        );

        sistema.agregarTransaccion(trans);
        sistema.guardarTodo();

        ArrayList<Tiquete> r = new ArrayList<>();
        r.add(tiquetePublicado);
        return r;
    }
    
 // ---- Helper para actualizar estados financieros y persistir ----
    private void actualizarEstadosFinancierosEvento(Evento evento,
                                                    double precioBaseTotal,
                                                    double ingresoAdminTotal) {
        if (evento == null) return;

        // Actualizar objeto en memoria
        evento.registrarVenta(precioBaseTotal, ingresoAdminTotal);

        // Persistir lista de eventos
        SistemaPersistencia pers = new SistemaPersistencia();
        List<Evento> eventos = pers.getEventos();

        for (int i = 0; i < eventos.size(); i++) {
            Evento ev = eventos.get(i);
            if (ev.getNombre().equals(evento.getNombre()) &&
                ev.getFecha().equals(evento.getFecha())) {
                eventos.set(i, evento);
                break;
            }
        }

        pers.guardarTodo();
    }

    
    
    
}
