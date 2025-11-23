package tests;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import tiquete.*;
import Evento.*;
import java.util.ArrayList;

public class TiqueteTest {
    
    private Evento evento;
    private Localidad localidad;
    private TiqueteSimple tiqueteSimple;
    private Venue venue;
    
    @Before
    public void setUp() {
        venue = TestHelper.crearVenue("Estadio Nacional", 5000);
        evento = TestHelper.crearEvento("Rock Concert", "2026-08-20", venue, "organizador1");
        localidad = TestHelper.crearLocalidad("VIP", 100000, 50);
        tiqueteSimple = TestHelper.crearTiqueteSimple("TIQ-001", 100000, evento, localidad);
    }
    
    @After
    public void tearDown() {
        evento = null;
        localidad = null;
        tiqueteSimple = null;
        venue = null;
        
        System.gc();
    }
    
    @Test
    public void testCrearTiqueteSimple() {
        assertNotNull("Tiquete no debería ser null", tiqueteSimple);
        assertEquals("TIQ-001", tiqueteSimple.getId());
        assertEquals(100000.0, tiqueteSimple.getPrecioBaseSinCalcular(), 0.01);
        assertEquals("SIMPLE", tiqueteSimple.getTipoTiquete());
        assertFalse("Tiquete no debería estar transferido", 
            tiqueteSimple.isTransferido());
        assertFalse("Tiquete no debería estar anulado",
            tiqueteSimple.isAnulado());
    }
    
    @Test
    public void testCalcularPrecioSinRecargo() {
        double cobroEmision = 10.0;
        double precioCalculado = tiqueteSimple.calcularPrecio(cobroEmision);
        assertEquals("Precio debería incluir cobro de emisión", 
            110000.0, precioCalculado, 0.01);
    }
    
    @Test
    public void testCalcularPrecioConRecargo() {
        TiqueteSimple tiqueteConRecargo = new TiqueteSimple(
            "SIMPLE", "TIQ-002", "2026-12-31", 100000, "Test",
            false, false, evento, 15.0, localidad
        );
        
        double cobroEmision = 10.0;
        double precioCalculado = tiqueteConRecargo.calcularPrecio(cobroEmision);
        assertEquals("Precio debería incluir recargo y emisión", 
            125000.0, precioCalculado, 0.01);
    }
    
    @Test
    public void testCalcularPrecioSinCobroEmision() {
        double precioCalculado = tiqueteSimple.calcularPrecio(0);
        assertEquals("Sin cobro de emisión debería ser precio base", 
            100000.0, precioCalculado, 0.01);
    }
    
    @Test
    public void testMarcarComoTransferido() {
        assertFalse("Inicialmente no transferido", tiqueteSimple.isTransferido());
        
        tiqueteSimple.setTransferido(true);
        
        assertTrue("Tiquete debería estar transferido", 
            tiqueteSimple.isTransferido());
    }
    
    @Test
    public void testMarcarComoAnulado() {
        assertFalse("Inicialmente no anulado", tiqueteSimple.isAnulado());
        
        tiqueteSimple.setAnulado(true);
        
        assertTrue("Tiquete debería estar anulado",
            tiqueteSimple.isAnulado());
    }
    
    @Test
    public void testTiqueteVigenteFechaFutura() {
        TiqueteSimple tiqueteFuturo = new TiqueteSimple(
            "SIMPLE", "TIQ-003", "2030-12-31", 50000, "Futuro",
            false, false, evento, 0, localidad
        );
        
        assertTrue("Tiquete con fecha futura debería ser vigente",
            tiqueteFuturo.esVigente("2025-01-01"));
    }
    
    @Test
    public void testTiqueteNoVigenteFechaPasada() {
        TiqueteSimple tiquetePasado = new TiqueteSimple(
            "SIMPLE", "TIQ-004", "2020-01-01", 50000, "Pasado",
            false, false, evento, 0, localidad
        );
        
        assertFalse("Tiquete con fecha pasada no debería ser vigente",
            tiquetePasado.esVigente("2025-11-24"));
    }
    
    @Test
    public void testCrearTiqueteMultiple() {
        ArrayList<TiqueteSimple> tiquetesInternos = new ArrayList<>();
        
        for (int i = 1; i <= 3; i++) {
            TiqueteSimple t = TestHelper.crearTiqueteSimple(
                "TIQ-INTERNO-" + i, 50000, evento, localidad);
            tiquetesInternos.add(t);
        }
        
        TiqueteMultiple paquete = new TiqueteMultiple(
            "MULTIPLE", "PKG-001", "2026-12-31", 120000, "Paquete Familiar",
            false, false, evento, 10.0, localidad, tiquetesInternos
        );
        
        assertNotNull("Paquete no debería ser null", paquete);
        assertEquals("MULTIPLE", paquete.getTipoTiquete());
        assertEquals(3, paquete.getTiquetes().size());
        assertEquals(120000.0, paquete.getPrecioBaseSinCalcular(), 0.01);
    }
}