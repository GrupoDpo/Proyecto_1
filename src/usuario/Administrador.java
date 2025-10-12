package usuario;

import java.util.ArrayList;

import Evento.Evento;
import tiquete.Tiquete;

public class Administrador extends Usuario {

	private double saldo;
	
	public Administrador(String login, String password, double saldo) {
		super(login, password);
		this.saldo = saldo;
	}
	
	public double getSaldo() {
		return this.saldo;
	}
	
	public void cobrarPorcentajeAdicional() {
		
	}
	
	public void fijarCobroEmisionImpresion(double cobroEmision) {
		
	}
	
	public void cancelarEvento(Evento evento) {
		
	}

	@Override
	public String getTipoUsuario() {
		return "ADMINISTRADOR";
	}
	
	

}
