package Consola;

import java.util.Scanner;

import usuario.Administrador;
import usuario.Cliente;
import usuario.Organizador;
import usuario.Promotor;
import usuario.Usuario;
import Persistencia.PersistenciaUsuariosJson;

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
                    
                    // POR AHORA ASUMIMOS QUE LA CONTRASENA ES CORRECTA
                    Usuario usuarioEnUso = // persistencia...
                    
                    menuParaUsuarios(usuarioEnUso);
                   
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
            			 Usuario newUser = new Administrador(nuevoLogin, nuevaPassword, tipoUsuario);
            			 
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
        
           
        
    }
    
    private void menuParaUsuarios(Usuario usuarioEnUso) {
        if (usuarioEnUso instanceof Cliente cliente) {
            menuCliente(cliente);  
        } 
        else if (usuarioEnUso instanceof Promotor promotor) {
            menuPromotor(promotor);
        } 
        else if (usuarioEnUso instanceof Organizador organizador) {
            menuOrganizador(organizador);
        } 
        else if (usuarioEnUso instanceof Administrador admin) {
            menuAdministrador(admin);
        } 
        else {
            System.out.println("Tipo de usuario no reconocido.");
        }
    }
    
    
    
    
    public void imprimirMenuCliente() {
    	System.out.println("\n========== MENÚ ==========");
        System.out.println("1. Comprar tiquetes");
        System.out.println("2. Transferir tiquetes");
        System.out.println("3. Solicitar reembolsos");
        System.out.println("----------------------------------");
        System.out.println("0. Salir");
        System.out.println("==================================");
    }
    
    public void menuCliente(Cliente cliente) {
    	int op;
        do {
            imprimirMenuCliente();
            System.out.print("Elige una opción: ");
            op = leerEntero(sc.nextLine());
            switch (op) {
                case 1:
                	// Despliegue de todos los eventos disponibles
                	cliente.comprarTiquete();
                case 2:
                	// Despliegue de los tiquetes del usuario en cuestion
                	cliente.transferirTiquete();
                case 3:
                	// Comunicarme con el administador para el rembolso 
                	cliente.solicitarReembolso();
                case 0:
                	System.out.println("Cerrando sesión ...");
                default: 
                	System.out.println("Opción no válida.");
            }
            if (op != 0) {
            	break;
            }
        } while (op != 0);
    }
    
    public void menuOrganizador(Organizador organizador) {
    	
        
        int op;
        do {
            imprimirMenuCliente();
            System.out.println("4. Crear evento");
        	System.out.println("----------------------------------");
            System.out.println("0. Salir");
            System.out.println("==================================");
            System.out.print("Elige una opción: ");
            op = leerEntero(sc.nextLine());
            switch (op) {
                case 1:
                	// Despliegue de todos los eventos disponibles
                	organizador.comprarTiquete();
                case 2:
                	// Despliegue de los tiquetes del usuario en cuestion
                	organizador.transferirTiquete();
                case 3:
                	// Comunicarme con el administador para el rembolso 
                	organizador.solicitarReembolso();
                
                case 4:
                	System.out.print("Ingrese el nombre o tipo de entrada del evento: ");
                    String entrada = sc.nextLine();

                    System.out.print("Ingrese la fecha del evento (YYYY-MM-DD): ");
                    String fecha = sc.nextLine();

                    System.out.print("Ingrese la hora del evento (HH:MM): ");
                    String hora = sc.nextLine();

                    System.out.println("\n--- DATOS DEL VENUE ASOCIADO ---");
                    System.out.print("Ingrese la ubicación del venue: ");
                    String ubicacion = sc.nextLine();

                    System.out.print("Ingrese la capacidad máxima del venue: ");
                    int capacidadMax = 0;
                    
                    
                    
                	organizador.crearEvento(entrada, fecha, hora);
                	
                	
                	
                case 0:
                	System.out.println("Cerrando sesión ...");
                default: 
                	System.out.println("Opción no válida.");
            }
            if (op != 0) {
            	break;
            }
        } while (op != 0);
    	
        
        int op;
        do {
            imprimirMenuCliente();
            System.out.print("Elige una opción: ");
            op = leerEntero(sc.nextLine());
            switch (op) {
                case 1:
                	// Despliegue de todos los eventos disponibles
                	cliente.comprarTiquete();
                case 2:
                	// Despliegue de los tiquetes del usuario en cuestion
                	cliente.transferirTiquete();
                case 3:
                	// Comunicarme con el administador para el rembolso 
                	cliente.solicitarReembolso();
                case 0:
                	System.out.println("Cerrando sesión ...");
                default: 
                	System.out.println("Opción no válida.");
            }
            if (op != 0) {
            	break;
            }
        } while (op != 0);
    	
    	
    }
    
    public void menuPromotor(Promotor promotor) {
    	imprimirMenuCliente();
    	System.out.println("4. Ver ganancias");
    	System.out.println("5. Ver porcentaje de ventas");
    	System.out.println("----------------------------------");
        System.out.println("0. Salir");
        System.out.println("==================================");
    }
    
    public void menuAdministrador(Administrador admin) {
    	System.out.println("\n========== MENÚ ADMINISTRADOR ==========");
        System.out.println("1. Crear Venue");
        System.out.println("2. Fijar cargos porcentuales de emision");
        System.out.println("3. Ver ganancias de tiqueteria");
        System.out.println("----------------------------------");
        System.out.println("0. Salir");
        System.out.println("==================================");
        
        Venue newVenue = new Venue(ubicacion, capacidadMax, aprobado)
    }
}

