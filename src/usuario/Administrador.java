package usuario;

import java.util.ArrayList;

import Evento.Evento;
import tiquete.Tiquete;

public class Administrador extends Usuario {

	
	
	public Administrador(String login, String password, String tipoUsuario) {
		super(login, password,tipoUsuario);
		
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
