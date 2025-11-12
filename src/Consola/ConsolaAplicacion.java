package Consola;

import java.util.Scanner;

import Finanzas.Transaccion;
import usuario.Administrador;
import usuario.Cliente;
import usuario.IDuenoTiquetes;
import usuario.Organizador;
import usuario.Promotor;
import usuario.Usuario;
import Persistencia.PersistenciaUsuarios;
import excepciones.UsuarioNoEncontrado;


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
                    
                    PersistenciaUsuarios persistenciaJson = new PersistenciaUsuarios();
                    boolean condicion = persistenciaJson.existeUsuario(login, password);
                    
        
                   
                    try {
                        if (!condicion) {
                            throw new UsuarioNoEncontrado("El usuario no existe en el sistema.");
                        }

                        System.out.println("Bienvenido!");

                    } catch (UsuarioNoEncontrado e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    
                    Usuario userActivo = persistenciaJson.buscarUsuario(login);
                    
                    menuParaUsuarios(userActivo);
                   
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
                    PersistenciaUsuarios persistenciaUsuariosJson = new PersistenciaUsuarios();
                    
                    
                    if (tipo.equals("1")) {
            			 tipoUsuario = "CLIENTE";
            			 Usuario newUser = new Cliente(nuevoLogin, nuevaPassword, 0, tipoUsuario);
						 persistenciaUsuariosJson.cargar(newUser);
						 persistenciaUsuariosJson.agregar(newUser);
						 

            			 
            		} else if (tipo.equals("2")) {
            			 tipoUsuario =  "PROMOTOR";
            			 Usuario newUser = new Promotor(nuevoLogin, nuevaPassword, 0, tipoUsuario);
            			 persistenciaUsuariosJson.cargar(newUser);
            			 persistenciaUsuariosJson.agregar(newUser);
            			 
            		} else if (tipo.equals("3")) {
            			 tipoUsuario = "ORGANIZADOR";
            			 Usuario newUser = new Organizador(nuevoLogin, nuevaPassword, 0, tipoUsuario);
            			 persistenciaUsuariosJson.cargar(newUser);
            			 persistenciaUsuariosJson.agregar(newUser);
            			 
            		} else if (tipo.equals("4")) {
            			 tipoUsuario = "ADMINISTRADOR";
            			 Usuario newUser = new Administrador(nuevoLogin, nuevaPassword, tipoUsuario);
            			 persistenciaUsuariosJson.cargar(newUser);
            			 persistenciaUsuariosJson.agregar(newUser);
            			 
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
    
    private static void menuParaUsuarios(Usuario usuarioEnUso) {
        if (usuarioEnUso instanceof Cliente cliente) {
            menuComprador(cliente, null);  
        } 
        else if (usuarioEnUso instanceof Promotor promotor) {
            menuPromotor(promotor, null);
        } 
        else if (usuarioEnUso instanceof Organizador organizador) {
            menuOrganizador(organizador, null);
        } 
        else if (usuarioEnUso instanceof Administrador admin) {
            menuAdministrador(admin);
        } 
        else {
            System.out.println("Tipo de usuario no reconocido.");
        }
    }
    
    
    public static void mostrarMenu(Usuario usuario) {
        System.out.println("========= MENÚ PRINCIPAL =========");
        System.out.println("Bienvenido, " + usuario.getLogin() + " (" + usuario.getTipoUsuario() + ")");
        System.out.println("----------------------------------");
        System.out.println("1. Comprar tiquetes");
        System.out.println("2. Comprar paquete Deluxe");
        System.out.println("3. Transferir tiquetes");
        System.out.println("4. Revender tiquetes");
        System.out.println("5. Recargar saldo");
        System.out.println("6. Solicitar reembolso");

        switch (usuario.getTipoUsuario().toUpperCase()) {
            case "PROMOTOR":
            	mostrarOpcionesPromotor();
            case "ORGANIZADOR":
            	mostrarOpcionesOrganizador();
        }

        System.out.println("0. Salir");
        System.out.println("==================================");
    }

    private static void mostrarOpcionesPromotor() {
        System.out.println("------ OPCIONES DE PROMOTOR ------");
        System.out.println("7. Sugerir un venue");
        System.out.println("8. Ver ganancias");
        System.out.println("9. Ver porcentaje de ventas por evento");
    }

    private static void mostrarOpcionesOrganizador() {
        System.out.println("------ OPCIONES DE ORGANIZADOR ------");
        System.out.println("7. Crear evento");
    }
    
    
    
    

    
    public static void menuComprador(Usuario comprador, Transaccion transaccion) {
    	Scanner sc = new Scanner(System.in);
    	int opcion;
    	
    	IDuenoTiquetes compradorDueno = (IDuenoTiquetes) comprador;
    
    
        do {
        	
            mostrarMenu(comprador);
            System.out.print("Elige una opción: ");
            while (!sc.hasNextInt()) {
                System.out.print("Por favor, ingresa un número válido: ");
                sc.next(); 
            }
            opcion = sc.nextInt();
            sc.nextLine();
            switch (opcion) {
                case 1:
                	System.out.println("Comprando tiquete...");
                	transaccion.comprarTiquete(null, compradorDueno, opcion, null);
                	break;
                case 2:
                	System.out.println("Comprando Paquete Deluxe...");
                	transaccion.comprarPaqueteDeluxe(null, compradorDueno, opcion, null);
                	break;
                case 3:
                	System.out.println("Transfiriendo tiquete...");
                	transaccion.transferirTiquete(null, compradorDueno, compradorDueno, null);
                	break;
                case 4:
                	System.out.println("Reembolsando tiquete...");
                	transaccion.solicitarReembolso();
                	break;
                	
                case 5:
                	System.out.println("revendiendo tiquete...");
                	transaccion.revenderTiquete();
                case 6:
                	System.out.println("Recargando el saldo...");
                	 System.out.print("Ingrese el valor a recargar: ");
                     double nuevoSaldo = Double.parseDouble(sc.nextLine());;
                	compradorDueno.actualizarSaldo(nuevoSaldo);
                	
                case 7:
                    if (comprador instanceof Promotor promotor) {
                        System.out.println("Sugerir un venue...");
                        promotor.sugerirVenue();
                    } else if (comprador instanceof Organizador organizador) {
                        System.out.println("Creando un evento...");
                        organizador.crearEvento(null, null, null, null, null, organizador.getLogin());
                    } else {
                        System.out.println("Opción no válida para este tipo de usuario.");
                    }
                    break;

                case 8:
                    if (comprador instanceof Promotor promotor) {
                        System.out.println("Viendo ganancias...");
                        promotor.getGanancias();
                    } else {
                        System.out.println("Opción no válida para este tipo de usuario.");
                    }
                    break;

                case 9:
                    if (comprador instanceof Promotor promotor) {
                        System.out.println("Viendo porcentaje de ventas por evento...");
                        promotor.getPorcentajeVenta();
                    } else {
                        System.out.println("Opción no válida para este tipo de usuario.");
                    }
                    break;
                	
                case 0:
                	System.out.println("Cerrando sesión ...");
                	break;
                default: 
                	System.out.println("Opción no válida.");
                	break;
                
                	
            }
            
            if (comprador instanceof Promotor) {
                mostrarOpcionesPromotor();
                
            } else if (comprador instanceof Organizador) {
                mostrarOpcionesOrganizador();
                
                
            } else if (comprador instanceof Cliente) {
                System.out.println("No hay opciones adicionales para clientes.");
            }
            
            
        } while (opcion != 0);

        sc.close();
     
        }
    
   
        
    
    
    
    public static void menuOrganizador(Organizador organizador, Transaccion transaccion) {
    	
        
    	Scanner sc = new Scanner(System.in);
    	int opcion;
        do {
        	mostrarMenu(organizador);
            System.out.println("7. Crear evento");
        	System.out.println("----------------------------------");
            System.out.println("0. Salir");
            System.out.println("==================================");
            System.out.print("Elige una opción: ");
            
            while (!sc.hasNextInt()) {
                System.out.print("Por favor, ingresa un número válido: ");
                sc.next(); 
            }
            opcion = sc.nextInt();
            sc.nextLine();
            
            switch (opcion) {
            case 1:
            	System.out.println("Comprando tiquete...");
            	transaccion.comprarTiquete(null, organizador, opcion, null);
            	break;
            case 2:
            	System.out.println("Comprando Paquete Deluxe...");
            	transaccion.comprarPaqueteDeluxe(null, organizador, opcion, null);
            	break;
            case 3:
            	System.out.println("Transfiriendo tiquete...");
            	transaccion.transferirTiquete(null, organizador, organizador, null);
            	break;
            case 4:
            	System.out.println("Reembolsando tiquete...");
            	transaccion.solicitarReembolso();
            	break;
            	
            case 5:
            	System.out.println("revendiendo tiquete...");
            	transaccion.revenderTiquete();
            case 6:
            	System.out.println("Recargando el saldo...");
            	 System.out.print("Ingrese el valor a recargar: ");
                 double nuevoSaldo =Double.parseDouble(sc.nextLine());;
            	 organizador.actualizarSaldo(nuevoSaldo);
            case 7:
            	System.out.print("Ingrese el nombre o tipo de entrada del evento: ");
                String entrada = sc.nextLine();

                System.out.print("Ingrese la fecha del evento (YYYY-MM-DD): ");
                String fecha = sc.nextLine();

                System.out.print("Ingrese la hora del evento (HH:MM): ");
                String hora = sc.nextLine();


                System.out.print("Ingrese la capacidad máxima del venue: ");
                int capacidadMax = 0;
            	organizador.crearEvento(entrada, fecha, hora, null, null); 
            case 0:
            	System.out.println("Cerrando sesión ...");
            	break;
            default: 
            	System.out.println("Opción no válida.");
            	break;
        }
    }while (opcion != 0);

    sc.close();
       
            
           
    	
    }
    
    public static void menuPromotor(Promotor promotor,Transaccion transaccion) {
    	Scanner sc = new Scanner(System.in);
    	int opcion;
    	do {
    		mostrarMenu(promotor);
        	System.out.println("6. Ver ganancias/porcentaje de ventas");
        	System.out.println("7. Sugerir Venue");
        	System.out.println("----------------------------------");
            System.out.println("0. Salir");
            System.out.println("==================================");
    		
            while (!sc.hasNextInt()) {
                System.out.print("Por favor, ingresa un número válido: ");
                sc.next(); 
            }
            opcion = sc.nextInt();
            sc.nextLine();
            
            switch (opcion) {
            case 1:
            	System.out.println("Comprando tiquete...");
            	transaccion.comprarTiquete(null, promotor, opcion, null);
            	break;
            case 2:
            	System.out.println("Comprando Paquete Deluxe...");
            	transaccion.comprarPaqueteDeluxe(null, promotor, opcion, null);
            	break;
            case 3:
            	System.out.println("Transfiriendo tiquete...");
            	transaccion.transferirTiquete(null, promotor, promotor, null);
            	break;
            case 4:
            	System.out.println("Reembolsando tiquete...");
            	transaccion.solicitarReembolso();
            	break;
            	
            case 5:
            	System.out.println("revendiendo tiquete...");
            	transaccion.revenderTiquete();
            	
            case 6:
            	promotor.getPorcentajeVenta();
            	promotor.getGanancias();
            	
    	}
    	}while (opcion != 0);

        sc.close();
    	
    	
    }
    
    public static void menuAdministrador(Administrador admin) {
    	System.out.println("\n========== MENÚ ADMINISTRADOR ==========");
        System.out.println("1. Crear Venue");
        System.out.println("2. Fijar cargos porcentuales de emision");
        System.out.println("3. Ver ganancias de tiqueteria");
        System.out.println("4. Aprobar/rechazar Venue");
        System.out.println("5. Ver log de reventas");
        System.out.println("6. Cancelar evento");
        System.out.println("7. Cancelar/aceptar cancelacion de evento");
        System.out.println("----------------------------------");
        System.out.println("0. Salir");
        System.out.println("==================================");
        
        

    }
    
    
    public static void ejecutarMenuAdministrador(Administrador admin) {
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            menuAdministrador(admin);

            System.out.print("Elige una opción: ");
            while (!sc.hasNextInt()) {
                System.out.print("Por favor, ingresa un número válido: ");
                sc.next();
            }
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1:
                    System.out.println("Creando Venue...");
                    admin.crearVenue("Ubicación temporal", 1000, false);
                    break;

                case 2:
                    System.out.println("Fijando cargos porcentuales de emisión...");
                    admin.fijarCobroEmisionImpresion(0.15);
                    break;

                case 3:
                    System.out.println("Viendo ganancias de tiquetería...");
                    admin.getGanancias();
                    break;

                case 4:
                    System.out.println("Aprobando/Rechazando Venue...");
                    admin.aprobarORechazarVenue(null, true);
                    break;

                case 5:
                    System.out.println("Viendo log de reventas...");
                    admin.verLogReventas();
                    break;

                case 6:
                    System.out.println("Cancelando evento...");
                    admin.cancelarEvento(null);
                    break;

                case 7:
                    System.out.println("Gestionando solicitud de cancelación de evento...");
                    admin.cancelarOAceptarCancelacion(null, true);
                    break;

                case 0:
                    System.out.println("Saliendo del menú de administrador...");
                    break;

                default:
                    System.out.println("Opción no válida.");
                    break;
            }

        } while (opcion != 0);

        sc.close();
    }
   
    
    
    
}

