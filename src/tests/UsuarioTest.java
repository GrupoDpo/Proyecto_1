package tests;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import usuario.*;
import tiquete.*;
import Evento.*;

public class UsuarioTest {
    
    private Cliente cliente;
    private Organizador organizador;
    private Promotor promotor;
    private Evento evento;
    private Localidad localidad;
    
    @Before
    public void setUp() {
        cliente = TestHelper.crearCliente("testUser", "password123", 100000);
        organizador = TestHelper.crearOrganizador("testOrg", "pass456", 50000);
        promotor = TestHelper.crearPromotor("testPromo", "pass789", 75000);
        
        Venue venue = TestHelper.crearVenue("Estadio Test", 1000);
        evento = TestHelper.crearEvento("Test Concert", "2026-06-15", venue, "testOrg");
        localidad = TestHelper.crearLocalidad("VIP", 50000, 100);
    }
    
    @After
    public void tearDown() {
        cliente = null;
        organizador = null;
        promotor = null;
        evento = null;
        localidad = null;
        
        System.gc();
    }
    
    @Test
    public void testCrearCliente() {
        assertNotNull("El cliente no debería ser null", cliente);
        assertEquals("testUser", cliente.getLogin());
        assertEquals("CLIENTE", cliente.getTipoUsuario());
        assertEquals(100000.0, cliente.getSaldo(), 0.01);
    }
    
    @Test
    public void testCrearOrganizador() {
        assertNotNull("El organizador no debería ser null", organizador);
        assertEquals("testOrg", organizador.getLogin());
        assertEquals("ORGANIZADOR", organizador.getTipoUsuario());
        assertEquals(50000.0, organizador.getSaldo(), 0.01);
    }
    
    @Test
    public void testCrearPromotor() {
        assertNotNull("El promotor no debería ser null", promotor);
        assertEquals("testPromo", promotor.getLogin());
        assertEquals("PROMOTOR", promotor.getTipoUsuario());
        assertEquals(75000.0, promotor.getSaldo(), 0.01);
    }
    
    @Test
    public void testValidarPasswordCorrecto() {
        assertTrue("Password correcto debería validar", 
            cliente.IsPasswordTrue("password123"));
    }
    
    @Test
    public void testValidarPasswordIncorrecto() {
        assertFalse("Password incorrecto no debería validar", 
            cliente.IsPasswordTrue("wrongpass"));
    }
    
    @Test
    public void testActualizarSaldo() {
        double saldoInicial = cliente.getSaldo();
        cliente.actualizarSaldo(150000);
        
        assertEquals("Saldo debería actualizarse", 
            150000.0, cliente.getSaldo(), 0.01);
        assertNotEquals("Saldo debería cambiar", 
            saldoInicial, cliente.getSaldo(), 0.01);
    }
    
    @Test
    public void testActualizarSaldoNegativo() {
        cliente.actualizarSaldo(-50000);
        assertEquals("Saldo negativo debería aceptarse", 
            -50000.0, cliente.getSaldo(), 0.01);
    }
    
    @Test
    public void testAgregarTiquete() {
        TiqueteSimple tiquete = TestHelper.crearTiqueteSimple(
            "TIQ-001", 50000, evento, localidad);
        
        cliente.agregarTiquete(tiquete);
        
        assertNotNull("Tiquetes no debería ser null", cliente.getTiquetes());
        assertTrue("Cliente debería tener el tiquete", 
            cliente.getTiquetes().contains(tiquete));
        assertEquals("Debería tener 1 tiquete", 
            1, cliente.getTiquetes().size());
    }
    
    @Test
    public void testAgregarMultiplesTiquetes() {
        for (int i = 1; i <= 5; i++) {
            TiqueteSimple tiquete = TestHelper.crearTiqueteSimple(
                "TIQ-00" + i, 50000, evento, localidad);
            cliente.agregarTiquete(tiquete);
        }
        
        assertEquals("Debería tener 5 tiquetes", 
            5, cliente.getTiquetes().size());
    }
    
    @Test
    public void testEliminarTiquete() {
        TiqueteSimple tiquete = TestHelper.crearTiqueteSimple(
            "TIQ-001", 50000, evento, localidad);
        
        cliente.agregarTiquete(tiquete);
        assertEquals(1, cliente.getTiquetes().size());
        
        cliente.eliminarTiquete(tiquete);
        assertEquals("Debería tener 0 tiquetes", 
            0, cliente.getTiquetes().size());
        assertFalse("No debería contener el tiquete",
            cliente.getTiquetes().contains(tiquete));
    }
    
    @Test
    public void testEliminarTiqueteNoExistente() {
        TiqueteSimple tiquete1 = TestHelper.crearTiqueteSimple(
            "TIQ-001", 50000, evento, localidad);
        TiqueteSimple tiquete2 = TestHelper.crearTiqueteSimple(
            "TIQ-002", 50000, evento, localidad);
        
        cliente.agregarTiquete(tiquete1);
        cliente.eliminarTiquete(tiquete2);
        
        assertEquals("Debería seguir teniendo 1 tiquete", 
            1, cliente.getTiquetes().size());
    }
}