package test;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import modelo.Credito;
import modelo.Cuenta;
import modelo.Debito;

public class TestVillafañe {
	private Date fechaCaducidad;
	private Credito credito;
	private Debito debito;
	private Cuenta cuenta;
	final double CREDITO_MAXIMO = 5000.0;
	
	@BeforeEach
	public void setUp() throws Exception {
		try {
			fechaCaducidad = new SimpleDateFormat("yyyy-MM-dd").parse("2022-09-30");
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		credito = new Credito("06-456213-33", "Alfredo Hernandez", fechaCaducidad, CREDITO_MAXIMO);
		debito = new Debito("06-456213-34", "Alfredo Hernandez", fechaCaducidad);
		cuenta = new Cuenta("06-456213-33", "Alfredo Hernandez");
		debito.setCuenta(cuenta);
		credito.setCuenta(cuenta);

	}
	
	@Test
	public void testIngresarConDebito(){
		try {
			debito.ingresar(4000);
			assertEquals(4000, debito.getSaldo(), "El saldo debería ser el ingresado (4000)");
		} catch (Exception e) {
			fail("El saldo debería ser el ingresado (4000): " + e.getMessage());
		}
	}
	
	@Test
	public void testRetirarConCredito() {
		try {
			credito.retirar(5000);
			assertTrue(credito.getCreditoDisponible() == 0, "Tras retirar el maximo del credito disponible debería quedar en 0");
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testComprasEnEstablecimientoConDebito() {
		try {
			cuenta.ingresar(10000);
			debito.pagoEnEstablecimiento("COTO", 4000);
			debito.pagoEnEstablecimiento("DÍA", 6000);
			assertEquals(0, cuenta.getSaldo(), "El saldo de la cuenta debería ser 0 tras gastarse en compras en establecimientos con debito");
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testComprasEnEstablecimientoConCredito() {
		try {
			credito.pagoEnEstablecimiento("COTO", 10000);
			credito.pagoEnEstablecimiento("DÍA", 10000);
			fail("No debería permitir comprar mas con credito del credito maximo disponible");
		} catch (Exception e) {
			assertTrue(e.getMessage().contains("insufieciente"), "Debería emitir una excepción por credito insuficiente");
			e.printStackTrace();
		}
	}
	
	@Test
	public void testLiquidarCredito() {
		try {
			cuenta.ingresar(10000);
			credito.pagoEnEstablecimiento("COTO", 2000);
			credito.pagoEnEstablecimiento("DIA", 3000);
			credito.liquidar(9, 22);
			assertEquals(5000, cuenta.getSaldo(), "Tras liquidarse los movimientos con credito debería impactar en el saldo de la cuenta");
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
