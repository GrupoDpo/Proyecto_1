package Consola;

import java.util.Scanner;

import usuario.Administrador;
import usuario.Cliente;
import usuario.Organizador;
import usuario.Promotor;
import usuario.Usuario;

public class ConsolaAplicacion {
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion = 0;

        System.out.println("=====================================");
        System.out.println("        -- BOLETAMASTER --");
        System.out.println("=====================================");
        System.out.println();

        do {
            System.out.println("===== MENÚ PRINCIPAL =====");
            System.out.println("1. Iniciar sesión");
            System.out.println("2. Registrar nuevo usuario");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");
            
            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1:
                	System.out.println("\n--- INICIAR SESIÓN ---");
                    System.out.print("Ingrese su login: ");
                    String login = sc.nextLine();
                    System.out.print("Ingrese su contraseña: ");
                    String password = sc.nextLine();
                    System.out.println("Intentando iniciar sesión para el usuario '" + login + "'...");
                    System.out.println("(Por ahora solo muestra texto, no valida credenciales)");
                    System.out.println();
                    break;

                case 2:
                    
                	System.out.println("\n--- REGISTRO DE NUEVO USUARIO ---");
                    System.out.print("Ingrese nuevo login: ");
                    String nuevoLogin = sc.nextLine();
                    System.out.print("Ingrese nueva contraseña: ");
                    String nuevaPassword = sc.nextLine();

                    System.out.println("\nSeleccione el tipo de usuario:");
                    System.out.println("1. Cliente");
                    System.out.println("2. Promotor");
                    System.out.println("3. Organizador");
                    System.out.println("4. Administrador");
                    System.out.print("Tipo de usuario: ");
                    String tipo = sc.nextLine();
                    
                    String tipoUsuario = "";
                    
                    if (tipo.equals("1")) {
            			 tipoUsuario = "CLIENTE";
            			 Usuario newUser = new Cliente(nuevoLogin, nuevaPassword, 0, tipoUsuario);
            		} else if (tipo.equals("2")) {
            			 tipoUsuario =  "PROMOTOR";
            			 Usuario newUser = new Promotor(nuevoLogin, nuevaPassword, 0, tipoUsuario);
            			 
            		} else if (tipo.equals("3")) {
            			 tipoUsuario = "ORGANIZADOR";
            			 Usuario newUser = new Organizador(nuevoLogin, nuevaPassword, 0, tipoUsuario);
            			 
            		} else if (tipo.equals("4")) {
            			 tipoUsuario = "ADMINISTRADOR";
            			 Usuario newUser = new Administrador(nuevoLogin, nuevaPassword,tipoUsuario);
            			 
            		} else {
            			 tipoUsuario = "NA";
            		}
                    
                    if (tipoUsuario.equals("NA")) {
                    	System.out.println("ERROR: tipo no valido");
                    	break;
                    }
                    
                    
                    

                    System.out.println("\nRegistrando usuario...");
                    System.out.println("Login: " + nuevoLogin);
                    System.out.println("Tipo seleccionado: " + tipo);
                    System.out.println("(Por ahora solo imprime, no guarda el usuario)");
                    System.out.println();
                    break;

                case 3:
                    System.out.println("\nSaliendo del sistema...");
                    break;

                default:
                    System.out.println("\nOpción inválida. Intente nuevamente.\n");
                    break;
            }

        } while (opcion != 3);

        sc.close();
        System.out.println("Programa finalizado.");
    }
}

