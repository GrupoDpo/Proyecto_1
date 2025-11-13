package Finanzas;



import tiquete.Tiquete;
import usuario.Usuario;

public class Registro  {
	
	private Usuario duenio;
    private Tiquete tiquete;
    private Usuario receptor;

    public Registro(Usuario duenio, Tiquete tiquete, Usuario receptor) {
        this.duenio = duenio;
        this.tiquete = tiquete;
        this.receptor = receptor;
    }

    

}
