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

public class TransferenciaTest {
    
    private SistemaPersistencia sistema;
    private Cliente vendedor;
    private Cliente comprador;
    private TiqueteSimple tiquete;
    private Transaccion transaccion;
    private Evento evento;
    private Localidad localidad;
    
    @Before
    public void setUp() {
        sistema = TestHelper.crearSistemaLimpio();
        vendedor = TestHelper.crearCliente("vendedor", "pass1", 100000);
        comprador = TestHelper.crearCliente("comprador", "pass2", 200000);
        
        Venue venue = TestHelper.crearVenue("Arena", 500);
        evento = TestHelper.crearEvento("Show", "2026-09-10", venue, "org");
        localidad = TestHelper.crearLocalidad("Platea", 60000, 100);
        
        tiquete = TestHelper.crearTiqueteSimple("TIQ-T01", 60000, evento, localidad);
        tiquete.setTransferido(true); // Ya fue comprado
        
        vendedor.agregarTiquete(tiquete);
        
        sistema.agregarUsuario(vendedor);
        sistema.agregarUsuario(comprador);
        sistema.agregarEvento(evento);
        sistema.agregarTiquete(tiquete);
        
        transaccion = new Transaccion("NA", null, null, null, null, 0);
    }
    
    @After
    public void tearDown() {
        sistema = null;
        vendedor = null;
        comprador = null;
        tiquete = null;
        transaccion = null;
        evento = null;
        localidad = null;
        
  
        System.gc();
    }
    
    @Test
    public void testTransferenciaExitosa() {
        int tiquetesVendedorInicial = vendedor.getTiquetes().size();
        int tiquetesCompradorInicial = comprador.getTiquetes().size();
        
        try {
            transaccion.transferirTiquete(
                tiquete, vendedor, comprador, "2025-11-24", sistema);
            
            assertFalse("Vendedor no debería tener el tiquete",
                vendedor.getTiquetes().contains(tiquete));
            
            assertTrue("Comprador debería tener el tiquete",
                comprador.getTiquetes().contains(tiquete));
            
            assertEquals("Vendedor tiene un tiquete menos",
                tiquetesVendedorInicial - 1, vendedor.getTiquetes().size());
            
            assertEquals("Comprador tiene un tiquete más",
                tiquetesCompradorInicial + 1, comprador.getTiquetes().size());
            
        } catch (Exception e) {
            fail("No debería lanzar excepción: " + e.getMessage());
        }
    }
    
    @Test
    public void testTransferirTiqueteNoComprado() {
        TiqueteSimple tiqueteNoComprado = TestHelper.crearTiqueteSimple(
            "TIQ-T02", 50000, evento, localidad);
        tiqueteNoComprado.setTransferido(false); // No comprado
        
        vendedor.agregarTiquete(tiqueteNoComprado);
        sistema.agregarTiquete(tiqueteNoComprado);
        
        try {
            transaccion.transferirTiquete(
                tiqueteNoComprado, vendedor, comprador, "2025-11-24", sistema);
            fail("Debería lanzar TransferenciaNoPermitidaException");
            
        } catch (TransferenciaNoPermitidaException e) {
            assertTrue("Mensaje correcto",
                e.getMessage().contains("no ha sido comprado"));
        } catch (Exception e) {
            fail("Excepción incorrecta: " + e.getClass().getName());
        }
    }
    
    @Test
    public void testTransferirTiqueteNoPropio() {
        Cliente otroUsuario = TestHelper.crearCliente("otro", "pass", 50000);
        sistema.agregarUsuario(otroUsuario);
        
        // El tiquete pertenece a vendedor, pero otroUsuario intenta transferir
        try {
            transaccion.transferirTiquete(
                tiquete, otroUsuario, comprador, "2025-11-24", sistema);
            fail("Debería lanzar IDNoEncontrado");
            
        } catch (IDNoEncontrado e) {
            assertTrue("Mensaje correcto",
                e.getMessage().toLowerCase().contains("no posees"));
        } catch (Exception e) {
            fail("Excepción incorrecta: " + e.getClass().getName());
        }
    }
    
    @Test
    public void testTransferirTiqueteVencido() {
        TiqueteSimple tiqueteVencido = new TiqueteSimple(
            "SIMPLE", "TIQ-VENCIDO", "2020-01-01", 50000, "Vencido",
            true, false, evento, 0, localidad
        );
        
        vendedor.agregarTiquete(tiqueteVencido);
        sistema.agregarTiquete(tiqueteVencido);
        
        try {
            transaccion.transferirTiquete(
                tiqueteVencido, vendedor, comprador, "2025-11-24", sistema);
            fail("Debería lanzar TiquetesVencidosTransferidos");
            
        } catch (TiquetesVencidosTransferidos e) {
            assertTrue("Mensaje correcto",
                e.getMessage().toLowerCase().contains("vencido"));
        } catch (Exception e) {
            fail("Excepción incorrecta: " + e.getClass().getName());
        }
    }
}