package tests;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import Finanzas.Registro;
import Finanzas.Transaccion;
import tiquete.Tiquete;
import tiquete.TiqueteSimple;
import usuario.Usuario;

public class TestTransaccion {
	
	private Transaccion transaccion;
	private Tiquete tiqueteSimple;
	private Usuario dueno;
	private LocalDateTime localDateTime;
	private Registro registro;
	
	
	@BeforeEach
	public void setUp() {
		tiqueteSimple = new TiqueteSimple("SIMPLE",10.5, 5000.0,"TS-2025-001","2025-01-12",25000.0,"David", false, false);
		
		transaccion = new Transaccion("COMPRA",tiqueteSimple, dueno, localDateTime, registro, 0);
	}
	
	@Test
	public void testCompra() {
		
	}
	
	
	

	
	

}
