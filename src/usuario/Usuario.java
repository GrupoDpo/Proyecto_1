package usuario;

import tiquete.Tiquete;

public abstract class Usuario {
	
	private String login;
	private String password;
	private String tipoUsuario;
	
	public Usuario (String login, String password) {
		this.login = login;
		this.password = password;
	}
	
	public String getLogin() {
		return this.login;
	}
	
	public boolean IsPasswordTrue(String password_try) {
		if (password_try == this.password) {
			return true;
		}
		else {
			return false;
		}
		
	}
	
	public void solicitarRembolso(Tiquete tiquete) {
		
	}
	
	public abstract String getTipoUsuario();
	
	
	
	

}
