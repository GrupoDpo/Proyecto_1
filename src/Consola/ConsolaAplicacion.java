package Consola;

import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import Evento.Evento;
import Evento.Venue;
import Evento.RegistroEventos;
import Finanzas.Transaccion;
import Finanzas.marketPlaceReventas;
import usuario.Administrador;
import usuario.Cliente;
import usuario.IDuenoTiquetes;
import usuario.Organizador;
import usuario.Promotor;
import usuario.Usuario;
import Persistencia.PersistenciaEventos;
import Persistencia.PersistenciaUsuarios;
import Persistencia.marketplacePersistencia;
import excepciones.UsuarioNoEncontrado;
import tiquete.PaqueteDeluxe;
import tiquete.Tiquete;


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
       
       if (usuarioEnUso instanceof Administrador admin) {
            menuAdministrador(admin);
        } 
        else if (usuarioEnUso instanceof IDuenoTiquetes) {
           menuComprador(usuarioEnUso, null, null);
        }
    }
    
    
    public static void mostrarMenu(Usuario usuario) {
        System.out.println("========= MENÚ PRINCIPAL =========");
        System.out.println("Bienvenido, " + usuario.getLogin() + " (" + usuario.getTipoUsuario() + ")");
        System.out.println("----------------------------------");
        System.out.println("1. Comprar tiquetes");
        System.out.println("2. Comprar paquete Deluxe");
        System.out.println("3. Transferir tiquetes");
        System.out.println("4. Crear oferta de reeventa de tiquetes");
        System.out.println("5. Comprar tiquete Revendido");
        System.out.println("6. cancelar oferta Tiquete");
        System.out.println("7. Contraofertar");
        System.out.println("8. Recargar saldo");
        System.out.println("9. Solicitar reembolso");
        System.out.println("10. Ver contraofertas");

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
        System.out.println("11. Sugerir un venue");
        System.out.println("12. Ver ganancias");
    }

    private static void mostrarOpcionesOrganizador() {
        System.out.println("------ OPCIONES DE ORGANIZADOR ------");
        System.out.println("11. Crear evento");
    }
    
    public static void imprimirEventos(PersistenciaEventos pers) {
    	List<Evento> eventos = pers.cargarTodos();
    	
    	for (Evento e : eventos) {
    	    System.out.println("=== EVENTO ===");
    	    System.out.println("Nombre: " + e.getNombre());
    	    System.out.println("Fecha: " + e.getFecha());
    	    System.out.println("Hora: " + e.getHora());
    	    System.out.println("Organizador: " + e.getLoginOrganizador());
    	    System.out.println("Venue asociado: " + e.getVenueAsociado());
    	    System.out.println("Cancelado: " + e.getCancelado());
    	    System.out.println("---------------------------");
    	}
    }
    
    public static void imprimirMarketplace(marketplacePersistencia pers) {
        List<marketPlaceReventas> reventas = pers.cargarTodos();

        System.out.println("=== LISTADO DE OFERTAS EN EL MARKETPLACE ===");

        if (reventas.isEmpty()) {
            System.out.println("No hay marketplace guardado.");
            return;
        }

        marketPlaceReventas mp = reventas.get(0);

        Queue<HashMap<Tiquete, String>> ofertas = mp.getOfertas();

        if (ofertas.isEmpty()) {
            System.out.println("No hay ofertas publicadas actualmente.");
            return;
        }

        int contador = 1;
        for (HashMap<Tiquete, String> mapa : ofertas) {
            for (Tiquete tiq : mapa.keySet()) {
                System.out.println("=== OFERTA #" + contador + " ===");
                System.out.println("Tiquete ID: " + tiq.getId());
                System.out.println("Nombre del tiquete: " + tiq.getNombre());
                System.out.println("Evento asociado: " + tiq.getEventoAsociado().getNombre());
                System.out.println("Precio / Vendedor: " + mapa.get(tiq));
                System.out.println("Transferido: " + tiq.isTransferido());
                System.out.println("Anulado: " + tiq.isAnulado());
                System.out.println("---------------------------");
                contador++;
            }
        }
    }
    
    

    
    public static void menuComprador(Usuario usuario, Transaccion transaccion, marketPlaceReventas market) {
    	Scanner sc = new Scanner(System.in);
    	int opcion;
    	
    	IDuenoTiquetes compradorDueno = (IDuenoTiquetes) usuario;
    	PersistenciaEventos EventPers = new PersistenciaEventos();
    	marketplacePersistencia marketPers = new marketplacePersistencia();
    
    
        do {
        	
        	
        	
            mostrarMenu(usuario);
            imprimirEventos(EventPers);
            imprimirMarketplace(marketPers);
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
                	transaccion.comprarTiquete(tiqueteComprar,  comprador, cantidad,eventoAsociado);
                	break;
                case 2:
                	System.out.println("Comprando Paquete Deluxe...");
                	transaccion.comprarPaqueteDeluxe( paquete,
                             comprador,  cantidad,  eventoAsociado);
                	break;
                case 3:
                	System.out.println("Transfiriendo tiquete...");
                	transaccion.transferirTiquete( tiquete,  dueno,  usuarioDestino,  fechaActual);
                	break;
                	
                case 4:
                	System.out.println("creando oferta de reventa de tiquete...");
                	transaccion.revenderTiquete( tiqueteVenta,  precioOferta,  vendedor);
                case 5:
                	System.out.println("creando oferta de reventa de tiquete...");
                	transaccion.comprarEnMarketplace(null, usuario, usuario);
                case 6:
                	System.out.println("eliminando oferta de reventa de tiquete...");
                	market.eliminarOferta(null, usuario);
                case 7:
                	System.out.println("eliminando oferta de reventa de tiquete...");
                	market.contraofertar(usuario);
                case 8:
                	System.out.println("Recargando el saldo...");
                	 System.out.print("Ingrese el valor a recargar: ");
                     double nuevoSaldo = Double.parseDouble(sc.nextLine());;
                	compradorDueno.actualizarSaldo(nuevoSaldo);
                	
                case 9:
                	System.out.println("Reembolsando tiquete...");
                	transaccion.solicitarReembolso( tiqueteRembolso,  motivo);
                	break;
                	
                case 10:
                	System.out.println("Reembolsando tiquete...");
                	market.Vercontraofertar(usuario);
                	break;
                	
                case 11:
                    if (compradorDueno instanceof Promotor promotor) {
                        System.out.println("Sugerir un venue...");
                        promotor.sugerirVenue(venue,  mensaje);
                    } else if (compradorDueno instanceof Organizador organizador) {
                        System.out.println("Creando un evento...");
                        organizador.crearEvento(null, null, null, null, null, organizador.getLogin());
                    } else {
                        System.out.println("Opción no válida para este tipo de usuario.");
                    }
                    break;

                case 12:
                    if (compradorDueno instanceof Promotor promotor) {
                        System.out.println("Viendo ganancias...");
                        promotor.verGanancias();
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
            
            if (compradorDueno instanceof Promotor) {
                mostrarOpcionesPromotor();
                
            } else if (compradorDueno instanceof Organizador) {
                mostrarOpcionesOrganizador();
                
                
            } 
            
            
        } while (opcion != 0);

        sc.close();
     
        }
    
   
        
    
    
    
    
    
    public static void menuAdministrador(Administrador admin) {
    	System.out.println("\n========== MENÚ ADMINISTRADOR ==========");
        System.out.println("1. Crear Venue");
        System.out.println("2. Aprobar/rechazar Venue");
        System.out.println("3. Fijar cargos porcentuales de emision");
        System.out.println("4.ver solicitudes de reembolso");
        System.out.println("5. Ver ganancias de la tiquetera");
        System.out.println("6. Ver log de reventas");
        System.out.println("7. Cancelar evento");
        System.out.println("8. Cancelar/aceptar cancelacion de evento");
        System.out.println("----------------------------------");
        System.out.println("0. Salir");
        System.out.println("==================================");
        
        

    }
    
    
    public static void ejecutarMenuAdministrador(Administrador admin,marketPlaceReventas market) {
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
                    System.out.println("Verificando solicitudes de venue...");
                    admin.verSolicitudVenue();
                    break;

                case 3:
                    System.out.println("Fijando cargos porcentuales de emisión...");
                    admin.fijarCobroEmisionImpresion(0.15);
                    break;
                    
                case 4:
                    System.out.println("Verificando solicitudes de reembolsos...");
                    admin.verSolicitud(admin);
                    break;

                case 5:
                    System.out.println("Viendo ganancias de tiquetería...");
                    admin.verGananciasAdministrador(null);
                    break;

                

                case 6:
                    System.out.println("Viendo log de reventas...");
                    market.verLogEventos(admin);
                    break;

                case 7:
                    System.out.println("Cancelando evento...");
                    admin.cancelarEvento(null);
                    break;

                case 8:
                    System.out.println("Gestionando solicitud de cancelación de evento...");
                    admin.verSolicitudCancelacionEvento();
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

