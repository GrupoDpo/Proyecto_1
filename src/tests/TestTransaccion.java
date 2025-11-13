package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Evento.Evento;
import Evento.Venue;
import Finanzas.Transaccion;
import Persistencia.PersistenciaUsuarios;
import excepciones.IDNoEncontrado;
import excepciones.SaldoInsuficienteExeption;
import excepciones.TiquetesNoDisponiblesException;
import excepciones.TiquetesVencidosTransferidos;
import excepciones.UsuarioPasswordIncorrecto;
import tiquete.PaqueteDeluxe;
import tiquete.Tiquete;
import tiquete.TiqueteSimple;
import usuario.Administrador;
import usuario.Cliente;

class TestTransaccion {

    private Cliente comprador;
    private Cliente vendedor;
    private Administrador admin;
    private Evento evento;
    private Tiquete tiquete;
    private PaqueteDeluxe paquete;
    private Transaccion transaccion;
    private PersistenciaUsuarios persistencia;

    @BeforeEach
    void setUp() {
        comprador = new Cliente("comprador123", "123", 1000.0, "CLIENTE");
        vendedor = new Cliente("vendedor123", "123", 500.0, "CLIENTE");
        admin = new Administrador("admin1", "admin123", "ADMINISTRADOR");

        persistencia = new PersistenciaUsuarios();
        persistencia.crearArchivo();
        persistencia.guardarTodos(List.of(admin));

        evento = new Evento(
                "Concierto Rock",
                "2025-12-10",
                "20:00",
                new HashMap<>(),
                new Venue("Coliseo Neiva", 5000, true),
                "organizador123",
                null,
                false
        );

        tiquete = new TiqueteSimple(
                "SIMPLE", 0.1, 5.0, "T001", "2025-12-20",
                200.0, "Concierto Rock", false, false, evento
        );

        evento.getMapaTiquetes().put("T001", tiquete);

        paquete = new PaqueteDeluxe("Merch oficial + bebidas", false);
        paquete.getTiquetes().add(tiquete);
        paquete.setPrecioPaquete(300.0);

        transaccion = new Transaccion("COMPRA", tiquete, comprador, LocalDateTime.now(), null, 200.0);
    }

    @AfterEach
    void tearDown() {
        comprador = null;
        vendedor = null;
        admin = null;
        evento = null;
        tiquete = null;
        paquete = null;
        transaccion = null;
        persistencia = null;
    }

    @Test
    void testComprarTiqueteExitoso() throws Exception {
        ArrayList<Tiquete> resultado = transaccion.comprarTiquete(tiquete, comprador, 1, evento);
        assertEquals(1, resultado.size());
        assertTrue(comprador.getTiquetes().contains(tiquete));
        assertTrue(tiquete.isTransferido());
        assertTrue(comprador.getSaldo() < 1000.0);
    }

    @Test
    void testComprarTiqueteSaldoInsuficiente() {
        comprador.actualizarSaldo(50.0);
        assertThrows(SaldoInsuficienteExeption.class, () -> {
            transaccion.comprarTiquete(tiquete, comprador, 1, evento);
        });
    }

    @Test
    void testComprarTiqueteSinDisponibles() {
        evento.getTiquetesDisponibles().clear();
        assertThrows(TiquetesNoDisponiblesException.class, () -> {
            transaccion.comprarTiquete(tiquete, comprador, 1, evento);
        });
    }

    @Test
    void testComprarPaqueteDeluxeExitoso() throws Exception {
        ArrayList<Tiquete> resultado = transaccion.comprarPaqueteDeluxe(paquete, comprador, 1, evento);
        assertFalse(resultado.isEmpty());
        assertTrue(comprador.getTiquetes().containsAll(resultado));
        assertTrue(resultado.get(0).isTransferido());
        assertTrue(comprador.getSaldo() < 1000.0);
    }

    @Test
    void testSolicitarReembolso() {
        // Se solicita el reembolso
        transaccion.solicitarReembolso(tiquete, "Evento cancelado");

        // Se recupera el administrador desde persistencia
        Administrador adminRecuperado = persistencia.recuperarAdministrador();

        assertNotNull(adminRecuperado, "El administrador recuperado no debe ser nulo.");
        assertFalse(adminRecuperado.getSolicitudes().isEmpty(), "Debe haber al menos una solicitud en la cola.");

        // Recuperar el primer elemento de la cola (peek)
        HashMap<Tiquete, String> solicitud = adminRecuperado.getSolicitudes().peek();
        assertNotNull(solicitud, "La solicitud de reembolso no debe ser nula.");
        assertTrue(solicitud.containsValue("Evento cancelado"), "Debe contener el motivo 'Evento cancelado'.");
    }

    @Test
    void testRevenderTiquete() {
        vendedor.agregarTiquete(tiquete);
        transaccion.revenderTiquete(tiquete, 250.0, vendedor);

        assertFalse(vendedor.getListaOfertas().isEmpty());
        assertTrue(vendedor.getListaOfertas().get(0).containsKey(tiquete));
    }

    @Test
    void testTransferirTiqueteExitoso() throws TiquetesVencidosTransferidos, IDNoEncontrado, UsuarioPasswordIncorrecto {
        vendedor.agregarTiquete(tiquete);

        String simulatedInput = "comprador123\n";
        InputStream originalIn = System.in;
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        String fechaActual = LocalDate.now().toString();
        transaccion.transferirTiquete(tiquete, vendedor, comprador, fechaActual);

        System.setIn(originalIn); 

        assertTrue(comprador.getTiquetes().contains(tiquete));
        assertFalse(vendedor.getTiquetes().contains(tiquete));
        assertTrue(tiquete.isTransferido());
    }
}
