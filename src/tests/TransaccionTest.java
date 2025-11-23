package tests;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import Finanzas.Transaccion;
import Persistencia.SistemaPersistencia;
import usuario.*;
import tiquete.*;
import Evento.*;
import excepciones.*;

import java.util.ArrayList;

public class TransaccionTest {
    
    private SistemaPersistencia sistema;
    private Cliente cliente;
    private Evento evento;
    private Localidad localidad;
    private Transaccion transaccion;
    private Venue venue;
    
    @Before
    public void setUp() {
        sistema = TestHelper.crearSistemaLimpio();
        cliente = TestHelper.crearCliente("comprador", "pass123", 500000);
        
        venue = TestHelper.crearVenue("Coliseo", 1000);
        evento = TestHelper.crearEvento("Concierto Test", "2026-07-15", 
            venue, "organizador");
        
        localidad = TestHelper.crearLocalidad("General", 80000, 200);
        
        transaccion = new Transaccion("NA", null, null, null, null, 0);
        
        sistema.agregarUsuario(cliente);
        sistema.agregarEvento(evento);
    }
    
    @After
    public void tearDown() {
        // Limpiar referencias
        sistema = null;
        cliente = null;
        evento = null;
        localidad = null;
        transaccion = null;
        venue = null;
        
        // Limpiar archivos

        
        System.gc();
    }
    
    @Test
    public void testComprarUnTiqueteExitoso() {
        TiqueteSimple tiquete = TestHelper.crearTiqueteSimple(
            "TIQ-001", 80000, evento, localidad);
        evento.agregarTiquete(tiquete);
        sistema.agregarTiquete(tiquete);
        
        double saldoInicial = cliente.getSaldo();
        
        try {
            ArrayList<Tiquete> comprados = transaccion.comprarTiquete(
                tiquete, cliente, 1, evento, 10.0, sistema);
            
            assertNotNull("Debería devolver lista", comprados);
            assertEquals("Debería comprar 1 tiquete", 1, comprados.size());
            assertTrue("Saldo debería disminuir", 
                cliente.getSaldo() < saldoInicial);
            
            // Precio: 80000 + 10% = 88000
            assertEquals("Saldo correcto después de compra",
                412000.0, cliente.getSaldo(), 0.01);
            
        } catch (Exception e) {
            fail("No debería lanzar excepción: " + e.getMessage());
        }
    }
    
    @Test
    public void testComprarMultiplesTiquetesExitoso() {
        // Crear 5 tiquetes
        for (int i = 1; i <= 5; i++) {
            TiqueteSimple t = TestHelper.crearTiqueteSimple(
                "TIQ-00" + i, 80000, evento, localidad);
            evento.agregarTiquete(t);
            sistema.agregarTiquete(t);
        }
        
        TiqueteSimple tiqueteComprar = (TiqueteSimple) evento.getTiquetePorId("TIQ-001");
        
        try {
            ArrayList<Tiquete> comprados = transaccion.comprarTiquete(
                tiqueteComprar, cliente, 3, evento, 10.0, sistema);
            
            assertEquals("Debería comprar 3 tiquetes", 3, comprados.size());
            assertEquals("Cliente debería tener 3 tiquetes", 
                3, cliente.getTiquetes().size());
            
            // Verificar que los IDs son diferentes
            String id1 = comprados.get(0).getId();
            String id2 = comprados.get(1).getId();
            String id3 = comprados.get(2).getId();
            
            assertNotEquals("IDs deberían ser diferentes", id1, id2);
            assertNotEquals("IDs deberían ser diferentes", id2, id3);
            assertNotEquals("IDs deberían ser diferentes", id1, id3);
            
        } catch (Exception e) {
            fail("No debería lanzar excepción: " + e.getMessage());
        }
    }
    
    @Test
    public void testComprarSinSaldoSuficiente() {
        Cliente clientePobre = TestHelper.crearCliente("pobre", "pass", 1000);
        sistema.agregarUsuario(clientePobre);
        
        TiqueteSimple tiquete = TestHelper.crearTiqueteSimple(
            "TIQ-001", 100000, evento, localidad);
        evento.agregarTiquete(tiquete);
        sistema.agregarTiquete(tiquete);
        
        try {
            transaccion.comprarTiquete(
                tiquete, clientePobre, 1, evento, 10.0, sistema);
            fail("Debería lanzar SaldoInsuficienteExeption");
            
        } catch (SaldoInsuficienteExeption e) {
            assertTrue("Mensaje correcto", 
                e.getMessage().toLowerCase().contains("insuficiente"));
            assertEquals("Saldo no debería cambiar", 
                1000.0, clientePobre.getSaldo(), 0.01);
        } catch (Exception e) {
            fail("Excepción incorrecta: " + e.getClass().getName());
        }
    }
    
    @Test
    public void testComprarSinTiquetesDisponibles() {
        // Crear evento sin tiquetes
        Evento eventoVacio = TestHelper.crearEvento(
            "Evento Vacío", "2026-08-01", venue, "org");
        sistema.agregarEvento(eventoVacio);
        
        TiqueteSimple tiquete = TestHelper.crearTiqueteSimple(
            "TIQ-001", 80000, eventoVacio, localidad);
        
        try {
            transaccion.comprarTiquete(
                tiquete, cliente, 1, eventoVacio, 10.0, sistema);
            fail("Debería lanzar TiquetesNoDisponiblesException");
            
        } catch (TiquetesNoDisponiblesException e) {
            assertTrue("Mensaje correcto",
                e.getMessage().toLowerCase().contains("disponibles"));
        } catch (Exception e) {
            fail("Excepción incorrecta: " + e.getClass().getName());
        }
    }
    
    @Test
    public void testComprarCantidadExcedeLimite() {
        for (int i = 1; i <= 5; i++) {
            TiqueteSimple t = TestHelper.crearTiqueteSimple(
                "TIQ-00" + i, 80000, evento, localidad);
            evento.agregarTiquete(t);
            sistema.agregarTiquete(t);
        }
        
        TiqueteSimple tiquete = (TiqueteSimple) evento.getTiquetePorId("TIQ-001");
        
        try {
            // Intentar comprar 20 (asumiendo que el límite es menor)
            transaccion.comprarTiquete(
                tiquete, cliente, 20, evento, 10.0, sistema);
            fail("Debería lanzar TransferenciaNoPermitidaException");
            
        } catch (TransferenciaNoPermitidaException e) {
            assertTrue("Mensaje correcto",
                e.getMessage().toLowerCase().contains("máximo"));
        } catch (TiquetesNoDisponiblesException e) {
            // También válido si no hay suficientes tiquetes
            assertTrue(true);
        } catch (Exception e) {
            fail("Excepción incorrecta: " + e.getClass().getName());
        }
    }
}