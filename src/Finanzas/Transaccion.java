package Finanzas;

import usuario.Cliente;
import usuario.Organizador;
import usuario.Promotor;
import usuario.Usuario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import tiquete.Tiquete;

public class Transaccion {
	
	Tiquete tiquete;
	int numeroMaxTransaccion;
	String metodoPago;
	
	
	public Transaccion(Tiquete tiquete, int numeroMaxTransaccion, String meotodPago) {
		this.tiquete = tiquete;
		this.numeroMaxTransaccion = numeroMaxTransaccion;
		this.metodoPago = metodoPago;
	}
	
	public void transferirTiquete(Tiquete tiquete, Usuario dueno, Usuario recipiente) {
		if (dueno.getTipoUsuario() != "ADMINISTRADOR") {
			Scanner sc = new Scanner(System.in);
			System.out.print("Ingresa tu login: ");
			String login = sc.nextLine();
			System.out.print("Ingresa tu password: ");
			String password = sc.nextLine();
			
			if (dueno.getLogin() == login && dueno.IsPasswordTrue(password)) {
				System.out.print("Cuantos tiquetes va a transferir: ");
				String numeroTiquetes = sc.nextLine();
				int numTiquetes = Integer.parseInt(numeroTiquetes);
				if (dueno instanceof Cliente) {
				    Collection<Tiquete> tiquetes = ((Cliente) dueno).getTiquetes();
				} else if (dueno instanceof Organizador) {
					Collection<Tiquete> tiquetes = ((Organizador) dueno).getTiquetes();
				} else if (dueno instanceof Promotor) {
					Collection<Tiquete> tiquetes = ((Promotor) dueno).getTiquetes();
				}
				
			for (int i  = 1; i <= numTiquetes; i ++) {
				
			}
				
			}

			} else {
				System.out.print("ERROR: login o password incorrecto");
			}
					
		}
			
		}

	
	
	


	public Tiquete getTiquete() {
		return this.tiquete;
		}

	public int getNumeroMaxTransaccion() {
			return this.numeroMaxTransaccion;
	}

	public String getMetodoPago() {
			return this.metodoPago;
	}

	public void setNumeroMaxTransaccion(int numeroMaxTransaccion) {
			this.numeroMaxTransaccion = numeroMaxTransaccion;
		
		
	
	

}
