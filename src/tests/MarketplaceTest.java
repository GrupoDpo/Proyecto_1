package tests;


import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import Finanzas.marketPlaceReventas;
import Finanzas.Transaccion;
import Persistencia.SistemaPersistencia;
import usuario.*;
import tiquete.*;
import Evento.*;
import excepciones.*;

import java.util.HashMap;
import java.util.Queue;

public class MarketplaceTest {
    
    private SistemaPersistencia sistema;
    private marketPlaceReventas marketplace;
    private Cliente vendedor;
    private Cliente comprador;
    private TiqueteSimple tiquete;
    private Evento evento;
    private Transaccion transaccion;
    
    @Before
    public void setUp() {
        sistema = TestHelper.crearSistemaLimpio();
        marketplace = sistema.getMarketplace();
        
        vendedor = TestHelper.crearCliente("vendedor", "pass1", 100000);
        comprador = TestHelper.crearCliente("comprador", "pass2", 500000);
        
        Venue venue = TestHelper.crearVenue("Arena", 1000);
        evento = TestHelper.crearEvento("Concierto", "2026-09-15", venue, "org");
        Localidad localidad = TestHelper.crearLocalidad("VIP", 100000, 50);
        
        tiquete = TestHelper.crearTiqueteSimple("TIQ-MKT-001", 100000, evento, localidad);
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
        marketplace = null;
        vendedor = null;
        comprador = null;
        tiquete = null;
        evento = null;
        transaccion = null;
        

        System.gc();
    }
    
    @Test
    public void testCrearOfertaExitosa() {
        int ofertasInicial = marketplace.getOfertas().size();
        int logInicial = marketplace.getLogEventos().size();
        
        marketplace.crearOferta(tiquete, 150000, vendedor, sistema);
        
        assertEquals("Debería agregar una oferta",
            ofertasInicial + 1, marketplace.getOfertas().size());
        
        assertEquals("Debería registrar en log",
            logInicial + 1, marketplace.getLogEventos().size());
        
        // Verificar que la oferta existe
        boolean encontrada = false;
        for (HashMap<Tiquete, String> oferta : marketplace.getOfertas()) {
            if (oferta.containsKey(tiquete)) {
                encontrada = true;
                break;
            }
        }
        
        assertTrue("La oferta debería estar en el marketplace", encontrada);
    }
    
    @Test
    public void testCrearMultiplesOfertas() {
        // Crear varios tiquetes y ofertas
        for (int i = 1; i <= 3; i++) {
            TiqueteSimple t = TestHelper.crearTiqueteSimple(
                "TIQ-MULTI-" + i, 80000, evento, 
                TestHelper.crearLocalidad("General", 80000, 100));
            t.setTransferido(true);
            vendedor.agregarTiquete(t);
            sistema.agregarTiquete(t);
            
            marketplace.crearOferta(t, 90000 + (i * 1000), vendedor, sistema);
        }
        
        assertTrue("Debería tener al menos 3 ofertas",
            marketplace.getOfertas().size() >= 3);
    }
    
    @Test
    public void testEliminarOferta() {
        marketplace.crearOferta(tiquete, 150000, vendedor, sistema);
        
        int ofertasAntes = marketplace.getOfertas().size();
        
        try {
            marketplace.eliminarOferta(tiquete, vendedor, sistema);
            
            assertEquals("Debería eliminar la oferta",
                ofertasAntes - 1, marketplace.getOfertas().size());
            
            // Verificar que ya no existe
            boolean encontrada = false;
            for (HashMap<Tiquete, String> oferta : marketplace.getOfertas()) {
                if (oferta.containsKey(tiquete)) {
                    encontrada = true;
                    break;
                }
            }
            
            assertFalse("La oferta no debería existir", encontrada);
            
        } catch (Exception e) {
            fail("No debería lanzar excepción: " + e.getMessage());
        }
    }
    
    @Test
    public void testEliminarOfertaNoExistente() {
        try {
            marketplace.eliminarOferta(tiquete, vendedor, sistema);
            fail("Debería lanzar OfertaNoDIsponibleException");
            
        } catch (OfertaNoDIsponibleException e) {
            assertTrue("Mensaje correcto",
                e.getMessage().contains("encontró"));
        }
    }
    
    @Test
    public void testComprarEnMarketplaceExitoso() {
        marketplace.crearOferta(tiquete, 150000, vendedor, sistema);
        
        double saldoVendedorInicial = vendedor.getSaldo();
        double saldoCompradorInicial = comprador.getSaldo();
        
        try {
            transaccion.comprarEnMarketplace(
                tiquete, vendedor, comprador, sistema);
            
            // Verificar transferencia de dinero
            assertEquals("Vendedor debería recibir dinero",
                saldoVendedorInicial + 150000, vendedor.getSaldo(), 0.01);
            
            assertEquals("Comprador debería pagar",
                saldoCompradorInicial - 150000, comprador.getSaldo(), 0.01);
            
            // Verificar transferencia de tiquete
            assertFalse("Vendedor no debería tener el tiquete",
                vendedor.getTiquetes().contains(tiquete));
            
            assertTrue("Comprador debería tener el tiquete",
                comprador.getTiquetes().contains(tiquete));
            
            // Verificar que la oferta se eliminó
            boolean encontrada = false;
            for (HashMap<Tiquete, String> oferta : marketplace.getOfertas()) {
                if (oferta.containsKey(tiquete)) {
                    encontrada = true;
                    break;
                }
            }
            
            assertFalse("La oferta debería eliminarse después de comprar",
                encontrada);
            
        } catch (Exception e) {
            fail("No debería lanzar excepción: " + e.getMessage());
        }
    }
    
    @Test
    public void testComprarSinSaldoSuficiente() {
        marketplace.crearOferta(tiquete, 600000, vendedor, sistema);
        
        try {
            transaccion.comprarEnMarketplace(
                tiquete, vendedor, comprador, sistema);
            fail("Debería lanzar SaldoInsuficienteExeption");
            
        } catch (SaldoInsuficienteExeption e) {
            assertTrue("Mensaje correcto",
                e.getMessage().toLowerCase().contains("insuficiente"));
        } catch (Exception e) {
            fail("Excepción incorrecta: " + e.getClass().getName());
        }
    }
    
    @Test
    public void testComprarPropiaTiquete() {
        marketplace.crearOferta(tiquete, 150000, vendedor, sistema);
        
        try {
            transaccion.comprarEnMarketplace(
                tiquete, vendedor, vendedor, sistema);
            fail("Debería lanzar TransferenciaNoPermitidaException");
            
        } catch (TransferenciaNoPermitidaException e) {
            assertTrue("Mensaje correcto",
                e.getMessage().toLowerCase().contains("ti mismo"));
        } catch (Exception e) {
            fail("Excepción incorrecta: " + e.getClass().getName());
        }
    }
    
    @Test
    public void testRegistrarContraoferta() {
        marketplace.crearOferta(tiquete, 150000, vendedor, sistema);
        
        int logInicial = marketplace.getLogEventos().size();
        
        try {
            marketplace.registrarContraoferta(
                tiquete, vendedor, comprador, 120000, sistema);
            
            // Verificar que se registró en log
            assertTrue("Log debería tener más eventos",
                marketplace.getLogEventos().size() > logInicial);
            
            // Verificar que el vendedor recibió la contraoferta
            assertNotNull("Vendedor debería tener lista de contraofertas",
                vendedor.getListaOfertas());
            
            assertFalse("Vendedor debería tener contraofertas",
                vendedor.getListaOfertas().isEmpty());
            
        } catch (Exception e) {
            fail("No debería lanzar excepción: " + e.getMessage());
        }
    }
    
    @Test
    public void testContraofertarATiMismo() {
        marketplace.crearOferta(tiquete, 150000, vendedor, sistema);
        
        try {
            marketplace.registrarContraoferta(
                tiquete, vendedor, vendedor, 120000, sistema);
            fail("Debería lanzar TransferenciaNoPermitidaException");
            
        } catch (TransferenciaNoPermitidaException e) {
            assertTrue("Mensaje correcto",
                e.getMessage().contains("ti mismo"));
        } catch (Exception e) {
            fail("Excepción incorrecta: " + e.getClass().getName());
        }
    }
    
    @Test
    public void testContraofertaPrecioInvalido() {
        marketplace.crearOferta(tiquete, 150000, vendedor, sistema);
        
        try {
            marketplace.registrarContraoferta(
                tiquete, vendedor, comprador, -5000, sistema);
            fail("Debería lanzar TransferenciaNoPermitidaException");
            
        } catch (TransferenciaNoPermitidaException e) {
            assertTrue("Mensaje correcto",
                e.getMessage().toLowerCase().contains("inválido"));
        } catch (Exception e) {
            fail("Excepción incorrecta: " + e.getClass().getName());
        }
    }
    
    @Test
    public void testProcesarContraofertaAceptada() {
        marketplace.crearOferta(tiquete, 150000, vendedor, sistema);
        
        // Crear contraoferta
        try {
            marketplace.registrarContraoferta(
                tiquete, vendedor, comprador, 130000, sistema);
        } catch (Exception e) {
            fail("Contraoferta falló: " + e.getMessage());
        }
        
        double saldoVendedorInicial = vendedor.getSaldo();
        double saldoCompradorInicial = comprador.getSaldo();
        
        try {
            marketplace.procesarContraoferta(
                tiquete, vendedor, comprador, true, 130000, sistema);
            
            // Verificar dinero
            assertEquals("Vendedor recibe dinero",
                saldoVendedorInicial + 130000, vendedor.getSaldo(), 0.01);
            
            assertEquals("Comprador paga",
                saldoCompradorInicial - 130000, comprador.getSaldo(), 0.01);
            
            // Verificar tiquete transferido
            assertFalse("Vendedor no tiene tiquete",
                vendedor.getTiquetes().contains(tiquete));
            
            assertTrue("Comprador tiene tiquete",
                comprador.getTiquetes().contains(tiquete));
            
        } catch (Exception e) {
            fail("No debería lanzar excepción: " + e.getMessage());
        }
    }
    
    @Test
    public void testProcesarContraofertaRechazada() {
        marketplace.crearOferta(tiquete, 150000, vendedor, sistema);
        
        try {
            marketplace.registrarContraoferta(
                tiquete, vendedor, comprador, 130000, sistema);
            
            double saldoVendedorInicial = vendedor.getSaldo();
            double saldoCompradorInicial = comprador.getSaldo();
            
            marketplace.procesarContraoferta(
                tiquete, vendedor, comprador, false, 130000, sistema);
            
            // Verificar que NO hubo transferencia
            assertEquals("Saldo vendedor no cambia",
                saldoVendedorInicial, vendedor.getSaldo(), 0.01);
            
            assertEquals("Saldo comprador no cambia",
                saldoCompradorInicial, comprador.getSaldo(), 0.01);
            
            assertTrue("Vendedor sigue con tiquete",
                vendedor.getTiquetes().contains(tiquete));
            
            assertFalse("Comprador no tiene tiquete",
                comprador.getTiquetes().contains(tiquete));
            
        } catch (Exception e) {
            fail("No debería lanzar excepción: " + e.getMessage());
        }
    }
    
    @Test
    public void testLogEventosSeRegistra() {
        int logInicial = marketplace.getLogEventos().size();
        
        // Crear oferta
        marketplace.crearOferta(tiquete, 150000, vendedor, sistema);
        assertEquals(logInicial + 1, marketplace.getLogEventos().size());
        
        // Eliminar oferta
        try {
            marketplace.eliminarOferta(tiquete, vendedor, sistema);
            assertEquals(logInicial + 2, marketplace.getLogEventos().size());
        } catch (Exception e) {
            fail("No debería fallar: " + e.getMessage());
        }
    }
    
    @Test
    public void testExtraerPrecioDeCadena() {
        try {
            double precio1 = marketPlaceReventas.extraerPrecio(
                "usuario1 - Precio: $150000");
            assertEquals(150000.0, precio1, 0.01);
            
            double precio2 = marketPlaceReventas.extraerPrecio(
                "usuario2 - Contraoferta: $120000.5");
            assertEquals(120000.5, precio2, 0.01);
            
        } catch (Exception e) {
            fail("No debería lanzar excepción: " + e.getMessage());
        }
    }
    
    @Test
    public void testExtraerPrecioInvalido() {
        try {
            marketPlaceReventas.extraerPrecio("sin precio aqui");
            fail("Debería lanzar excepción");
            
        } catch (TransferenciaNoPermitidaException e) {
            assertTrue(true);
        }
    }
}