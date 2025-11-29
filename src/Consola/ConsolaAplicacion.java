package Consola;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Scanner;

import Evento.Evento;
import Evento.Localidad;
import Evento.Venue;
import Finanzas.Transaccion;
import Finanzas.marketPlaceReventas;
import Persistencia.SistemaPersistencia;
import excepciones.IDNoEncontrado;
import excepciones.OfertaNoDIsponibleException;
import excepciones.SaldoInsuficienteExeption;
import excepciones.TiquetesNoDisponiblesException;
import excepciones.TiquetesVencidosTransferidos;
import excepciones.TransferenciaNoPermitidaException;
import tiquete.PaqueteDeluxe;
import tiquete.Tiquete;
import tiquete.TiqueteMultiple;
import tiquete.TiqueteSimple;
import usuario.*;

public class ConsolaAplicacion {
	private static Scanner sc = new Scanner(System.in); 
    public static void main(String[] args) throws TransferenciaNoPermitidaException {
    	
        SistemaPersistencia sistema = new SistemaPersistencia();

        sistema.cargarTodo();
 
      

        int opcion = 0;

        System.out.println("=====================================");
        System.out.println("        -- BOLETAMASTER --");
        System.out.println("=====================================");
        System.out.println();

        do {
            System.out.println("===== MENÚ PRINCIPAL =====");
            System.out.println("1. Iniciar sesión");
            System.out.println("2. Registrar usuario");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                opcion = -1;
            }

            switch (opcion) {

                case 1: {
                    System.out.print("Login: ");
                    String login = sc.nextLine();
                    System.out.print("Password: ");
                    String pass = sc.nextLine();

                    Usuario usuario = sistema.buscarUsuario(login);
                   
                  

                    if (usuario == null || !usuario.IsPasswordTrue(pass)) {
                        System.out.println("Credenciales incorrectas.\n");
                        break;
                    }

                    System.out.println("Bienvenido, " + usuario.getLogin());
                    menuUsuario(usuario, sistema);
                    break;
                }

                case 2:
                    registrarUsuario(sistema);
                    break;

                case 3:
                    System.out.println("Saliendo...");
                    break;

                default:
                    System.out.println("Opción inválida.");
            }

        } while (opcion != 3);

        sistema.guardarTodo();
        sc.close();
    }

    // ----------------------------------------------------------
    // REGISTRO
    // ----------------------------------------------------------
    private static void registrarUsuario(SistemaPersistencia sistema) {
        

        System.out.print("Nuevo login: ");
        String login = sc.nextLine();

        if (sistema.buscarUsuario(login) != null) {
            System.out.println("Ese login ya existe.");
            return;
        }

        System.out.print("Contraseña: ");
        String pass = sc.nextLine();

        System.out.println("Tipo:");
        System.out.println("1. Cliente");
        System.out.println("2. Promotor");
        System.out.println("3. Organizador");
        System.out.println("4. Administrador");
        System.out.print("Seleccione: ");

        String op = sc.nextLine();
        Usuario nuevo = null;

        switch (op) {
            case "1": nuevo = new Cliente(login, pass, 0, "CLIENTE"); break;
            case "2": nuevo = new Promotor(login, pass, 0, "PROMOTOR"); break;
            case "3": nuevo = new Organizador(login, pass, 0, "ORGANIZADOR"); break;
            case "4": nuevo = new Administrador(login, pass, "ADMINISTRADOR"); break;
            default:
                System.out.println("Opción inválida.");
                return;
        }
       
    

        sistema.agregarUsuario(nuevo);
        sistema.guardarTodo();

        // Verificar que se guardó
        System.out.println("Total usuarios en memoria: " + sistema.getUsuarios().size());
        System.out.println("Usuario registrado.");
    }

    // ----------------------------------------------------------
    // MENÚ GENERAL
    // ----------------------------------------------------------
    private static void menuUsuario(Usuario u, SistemaPersistencia sistema) throws TransferenciaNoPermitidaException {

        if (u instanceof Administrador admin)
            menuAdministrador(admin, sistema);

        else if (u instanceof IDuenoTiquetes duen)
            menuComprador(duen, sistema);

        else
            System.out.println("Tipo de usuario no soportado.");
    }
    
 

    // ----------------------------------------------------------
    // MENÚ COMPRADOR
    // ----------------------------------------------------------
    private static void menuComprador(IDuenoTiquetes usuario, SistemaPersistencia sistema) throws TransferenciaNoPermitidaException {

        int op = -1;

        Transaccion trans = new Transaccion("NA", null, null, null, null, 0);
        marketPlaceReventas market = sistema.getMarketplace();

        do {
            System.out.println("\n====== MENÚ USUARIO ======");
            System.out.println("Saldo: $" + String.format("%.2f", usuario.getSaldo()));
            System.out.println("1. Comprar tiquete");
            System.out.println("2. Comprar Paquete Deluxe");
            System.out.println("3. Transferir tiquete");
            System.out.println("4. Crear oferta de reventa");
            System.out.println("5. Cancelar oferta de reventa");
            System.out.println("6. Comprar en marketplace");
            System.out.println("7. Contraofertar");
            System.out.println("8. Ver contraofertas recibidas");
            System.out.println("9. Recargar saldo");
            System.out.println("10. Solicitar reembolso");

            // Opciones específicas por tipo de usuario
            if (usuario instanceof Organizador) {
                System.out.println("11. Crear evento");
                System.out.println("12. Solicitar cancelación de evento");
                System.out.println("13. Agregar tiquetes al evento");
            }

            if (usuario instanceof Promotor) {
                System.out.println("11. Sugerir venue");
                System.out.println("12. Ver ganancias");
            }

            System.out.println("0. Salir");
            System.out.print("Seleccione: ");

            try { op = Integer.parseInt(sc.nextLine()); }
            catch (Exception e) { op = -1; }

            switch (op) {

                case 1:
                    comprarTiquete(usuario, sistema, trans);
                    break;

                case 2:
                    comprarPaqueteDeluxe(usuario, sistema, trans);
                    break;

                case 3:
                    transferirTiquete(usuario, sistema, trans);
                    break;

                case 4:
                    crearOferta(usuario, sistema, trans);
                    break;

                case 5:
                    cancelarOferta(usuario, sistema, market);
                    break;

                case 6:
                    comprarMarketplace(usuario, sistema, trans);
                    break;

                case 7:
                    contraofertar(usuario, sistema, market);
                    break;
                    
                    
                case 8:
                	verContraofertas(usuario, sistema,market);
                    break;

                case 9:
                    recargarSaldo(usuario);
                    break;

                case 10:
                    solicitarReembolso(usuario, sistema, trans);
                    break;

                case 11:
                    if (usuario instanceof Organizador) {
                        crearEvento((Organizador) usuario, sistema);
                    } else if (usuario instanceof Promotor) {
                        sugerirVenue((Promotor) usuario, sistema);
                    } else {
                        System.out.println("Opción no válida para este tipo de usuario.");
                    }
                    break;

                case 12:
                    if (usuario instanceof Organizador) {
                        solicitarCancelacionEvento((Organizador) usuario, sistema);
                    } else if (usuario instanceof Promotor) {
                        verGanancias((Promotor) usuario);
                    } else {
                        System.out.println("Opción no válida para este tipo de usuario.");
                    }
                    break;

                case 13:
                    if (usuario instanceof Organizador) {
                        agregarTiquetesEvento((Organizador) usuario, sistema);
                    } else {
                        System.out.println("Opción no válida para este tipo de usuario.");
                    }
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Opción inválida.");
            }

            sistema.guardarTodo();

        } while (op != 0);
    }

    // ----------------------------------------------------------
    // OPERACIONES DEL MENÚ
    // ----------------------------------------------------------

    private static void comprarTiquete(IDuenoTiquetes usuario, SistemaPersistencia sistema, Transaccion trans) {

        System.out.println("\n=== COMPRAR TIQUETE ===");
        System.out.println("Eventos disponibles:");
        for (Evento e : sistema.getEventos()) {
            if (!e.getCancelado()) {
                System.out.println("- " + e.getNombre() + " (" + e.getFecha() + ")");
            }
        }

        System.out.print("\nNombre del evento: ");
        String nombre = sc.nextLine();

        Evento ev = sistema.buscarEventoPorNombre(nombre);
        if (ev == null) {
            System.out.println("✗ Evento no encontrado.");
            return;
        }

        if (ev.getCancelado()) {
            System.out.println("✗ Este evento está cancelado.");
            return;
        }

        HashMap<String, Tiquete> disponibles = ev.getTiquetes();
        
        if (disponibles.isEmpty()) {
            System.out.println("✗ No hay tiquetes disponibles.");
            return;
        }

        // ===== ORGANIZAR TIQUETES POR TIPO =====
        System.out.println("\n===== TIQUETES DISPONIBLES =====");
        
        int opcion = 1;
        HashMap<Integer, TiqueteOpcion> menuOpciones = new HashMap<>();
        
        // 1. Agrupar tiquetes SIMPLES por localidad
        HashMap<String, ArrayList<TiqueteSimple>> simplesPorLocalidad = new HashMap<>();
        
        for (Tiquete t : disponibles.values()) {
            if (t instanceof TiqueteSimple) {
                TiqueteSimple ts = (TiqueteSimple) t;
                String loc = ts.getLocalidadAsociada() != null 
                    ? ts.getLocalidadAsociada().getNombre() 
                    : "Sin localidad";
                
                simplesPorLocalidad.putIfAbsent(loc, new ArrayList<>());
                simplesPorLocalidad.get(loc).add(ts);
            }
        }
        
        // Mostrar tiquetes SIMPLES
        if (!simplesPorLocalidad.isEmpty()) {
            System.out.println("\n🎫 TIQUETES SIMPLES:");
            
            
            
            for (String localidad : simplesPorLocalidad.keySet()) {
                ArrayList<TiqueteSimple> tiquetes = simplesPorLocalidad.get(localidad);
                
                
                TiqueteSimple ejemplo = tiquetes.get(0);
                
                System.out.println("  " + opcion + ". " + localidad + " (SIMPLE)");
                System.out.println("     Precio base: $" + String.format("%.2f", ejemplo.getPrecioBaseSinCalcular()));
                System.out.println("     Recargo: " + ejemplo.getRecargo() + "%");
                System.out.println("     Disponibles: " + (tiquetes.size()));
                
                menuOpciones.put(opcion, new TiqueteOpcion("SIMPLE", localidad, ejemplo, (tiquetes.size())));
                opcion++;
            }
        }
        
        // 2. Mostrar tiquetes MÚLTIPLES
        ArrayList<TiqueteMultiple> tiquetesMultiples = new ArrayList<>();
        
        for (Tiquete t : disponibles.values()) {
            if (t instanceof TiqueteMultiple) {
                tiquetesMultiples.add((TiqueteMultiple) t);
            }
        }
        
        if (!tiquetesMultiples.isEmpty()) {
            System.out.println("\n📦 PAQUETES/TIQUETES MÚLTIPLES:");
            
            for (TiqueteMultiple tm : tiquetesMultiples) {
                System.out.println("  " + opcion + ". " + tm.getNombre() + " (MÚLTIPLE)");
                System.out.println("     Incluye: " + tm.getTiquetes().size() + " tiquetes");
                System.out.println("     Precio base: $" + String.format("%.2f", tm.getPrecioBaseSinCalcular()));
                System.out.println("     Recargo: " + tm.getRecargo() + "%");
                
                menuOpciones.put(opcion, new TiqueteOpcion("MULTIPLE", tm.getNombre(), tm, 1));
                opcion++;
            }
        }

        System.out.println("\n================================");
        System.out.print("Seleccione el tiquete que desea comprar (número): ");
        
        int seleccion = 0;
        try {
            seleccion = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("✗ Opción inválida.");
            return;
        }

        if (!menuOpciones.containsKey(seleccion)) {
            System.out.println("✗ Opción no válida.");
            return;
        }

        TiqueteOpcion opcionSeleccionada = menuOpciones.get(seleccion);
        Tiquete tiqueteSeleccionado = opcionSeleccionada.tiquete;
        
        // ===== PEDIR CANTIDAD (SOLO SI ES SIMPLE) =====
        int cantidad = 1;
        
        if (opcionSeleccionada.tipo.equals("SIMPLE")) {
            System.out.print("\n¿Cuántos tiquetes deseas? (máx: " + opcionSeleccionada.disponibles + "): ");
            try {
                cantidad = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("✗ Cantidad inválida.");
                return;
            }
            
            if (cantidad <= 0 || cantidad > opcionSeleccionada.disponibles) {
                System.out.println("✗ Cantidad no válida.");
                return;
            }
        } else {
            System.out.println("\n(Los paquetes múltiples se compran completos, cantidad = 1)");
        }

        // ===== CALCULAR PRECIO =====
        double cobroEmision = sistema.getAdministrador().getCobroEmision();
        double precioUnitario = tiqueteSeleccionado.calcularPrecio(cobroEmision);
        double precioTotal = (opcionSeleccionada.tipo.equals("MULTIPLE")) 
            ? precioUnitario 
            : precioUnitario * cantidad;

        // ===== MOSTRAR RESUMEN =====
        System.out.println("\n===== RESUMEN DE COMPRA =====");
        System.out.println("Tipo: " + opcionSeleccionada.tipo);
        System.out.println("Evento: " + ev.getNombre());
        
        if (opcionSeleccionada.tipo.equals("SIMPLE")) {
            System.out.println("Localidad: " + opcionSeleccionada.nombre);
            System.out.println("Cantidad: " + cantidad);
        } else {
            System.out.println("Paquete: " + opcionSeleccionada.nombre);
            TiqueteMultiple tm = (TiqueteMultiple) tiqueteSeleccionado;
            System.out.println("Incluye " + tm.getTiquetes().size() + " tiquetes:");
            for (Tiquete t : tm.getTiquetes()) {
                String loc = t.getLocalidadAsociada() != null 
                    ? t.getLocalidadAsociada().getNombre() 
                    : "N/A";
                System.out.println("  - " + loc);
            }
        }
        
        System.out.println("----------------------------");
        System.out.println("Precio base: $" + String.format("%.2f", tiqueteSeleccionado.getPrecioBaseSinCalcular()));
        System.out.println("Recargo: " + tiqueteSeleccionado.getRecargo() + "%");
        System.out.println("Cobro emisión: " + cobroEmision + "%");
        System.out.println("----------------------------");
        
        if (opcionSeleccionada.tipo.equals("SIMPLE")) {
            System.out.println("Precio unitario: $" + String.format("%.2f", precioUnitario));
        }
        
        System.out.println("PRECIO TOTAL: $" + String.format("%.2f", precioTotal));
        System.out.println("----------------------------");
        System.out.println("Tu saldo actual: $" + String.format("%.2f", usuario.getSaldo()));
        System.out.println("Saldo después: $" + String.format("%.2f", usuario.getSaldo() - precioTotal));
        System.out.println("============================\n");

        if (precioTotal > usuario.getSaldo()) {
            System.out.println("✗ Saldo insuficiente. Te faltan: $" + 
                String.format("%.2f", precioTotal - usuario.getSaldo()));
            return;
        }

        System.out.print("¿Confirmar compra? (s/n): ");
        String confirmar = sc.nextLine();

        if (!confirmar.equalsIgnoreCase("s")) {
            System.out.println("Compra cancelada.");
            return;
        }

        // ===== PROCEDER CON LA COMPRA =====
        try {
            trans.comprarTiquete(tiqueteSeleccionado, (Usuario) usuario, cantidad, ev, cobroEmision, sistema);
            
            
            System.out.println("\n✓ ¡Compra realizada exitosamente!");
            
        
            
            
            
            
            System.out.println("  Tipo: " + opcionSeleccionada.tipo);
            
            if (opcionSeleccionada.tipo.equals("SIMPLE")) {
                System.out.println("  Localidad: " + opcionSeleccionada.nombre);
                System.out.println("  Cantidad: " + cantidad + " tiquetes");
            } else {
                System.out.println("  Paquete: " + opcionSeleccionada.nombre);
            }
            
            System.out.println("  Total pagado: $" + String.format("%.2f", precioTotal));
            System.out.println("  Nuevo saldo: $" + String.format("%.2f", usuario.getSaldo()));
            
        } catch (TiquetesNoDisponiblesException e) {
            System.out.println("✗ " + e.getMessage());
        } catch (TransferenciaNoPermitidaException e) {
            System.out.println("✗ " + e.getMessage());
        } catch (SaldoInsuficienteExeption e) {
            System.out.println("✗ " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Error inesperado: " + e.getMessage());
      
        }
    }

    // ===== CLASE AUXILIAR =====
    private static class TiqueteOpcion {
        String tipo;          
        String nombre;        
        Tiquete tiquete;      
        int disponibles;      
        
        TiqueteOpcion(String tipo, String nombre, Tiquete tiquete, int disponibles) {
            this.tipo = tipo;
            this.nombre = nombre;
            this.tiquete = tiquete;
            this.disponibles = disponibles;
        }
    }
    
    private static void comprarPaqueteDeluxe(IDuenoTiquetes usuario, SistemaPersistencia sistema, Transaccion trans) {
        try {
            System.out.println("\n=== COMPRA DE PAQUETE DELUXE ===");
            
            System.out.print("Ingrese el ID del usuario comprador: ");
            String idComprador = sc.nextLine();
            Usuario comprador = sistema.buscarUsuario(idComprador);
            
            if (comprador == null) {
                System.out.println("Error: Usuario no encontrado.");
                return;
            }
            
            // Verificar que el comprador puede poseer tiquetes
            if (!(comprador instanceof IDuenoTiquetes)) {
                System.out.println("Error: Este usuario no puede comprar tiquetes.");
                return;
            }
            
            // Mostrar saldo actual
            IDuenoTiquetes dueno = (IDuenoTiquetes) comprador;
            System.out.println("Saldo actual: $" + dueno.getSaldo());
            
            // 2. Solicitar ID del evento
            System.out.print("Ingrese el ID del evento: ");
            String idEvento = sc.nextLine();
            Evento evento = sistema.buscarEventoPorNombre(idEvento);
            
            if (evento == null) {
                System.out.println("Error: Evento no encontrado.");
                return;
            }
            
            // 3. Mostrar paquetes deluxe disponibles del evento
            ArrayList<PaqueteDeluxe> paquetesDisponibles =
                    new ArrayList<>(sistema.getPaquetesDeluxePorEvento(evento));
            
            if (paquetesDisponibles == null || paquetesDisponibles.isEmpty()) {
                System.out.println("Error: No hay paquetes deluxe disponibles para este evento.");
                return;
            }
            
            System.out.println("\nPaquetes Deluxe disponibles:");
            for (int i = 0; i < paquetesDisponibles.size(); i++) {
                PaqueteDeluxe paq = paquetesDisponibles.get(i);
                int totalTiquetes = paq.getTiquetes().size() + paq.getTiquetesAdicionales().size();
                
                System.out.println("\n" + (i + 1) + ". PAQUETE DELUXE");
                System.out.println("   Precio: $" + paq.getPrecioPaquete());
                System.out.println("   Mercancía y Beneficios: " + paq.getMercanciaYBeneficios());
                System.out.println("   Total de tiquetes: " + totalTiquetes);
                System.out.println("   - Tiquetes base: " + paq.getTiquetes().size());
                System.out.println("   - Tiquetes adicionales: " + paq.getTiquetesAdicionales().size());
                System.out.println("   Estado: " + (paq.isAnulado() ? "ANULADO" : "DISPONIBLE"));
            }
            
            // 4. Seleccionar paquete
            System.out.print("\nSeleccione el número del paquete a comprar: ");
            int opcionPaquete = Integer.parseInt(sc.nextLine());
            
            if (opcionPaquete < 1 || opcionPaquete > paquetesDisponibles.size()) {
                System.out.println("Error: Opción inválida.");
                return;
            }
            
            PaqueteDeluxe paqueteSeleccionado = paquetesDisponibles.get(opcionPaquete - 1);
            
            // Verificar que no esté anulado
            if (paqueteSeleccionado.isAnulado()) {
                System.out.println("Error: Este paquete está anulado y no se puede comprar.");
                return;
            }
            
            // 5. Confirmar cantidad (debe ser 1 según el método)
            System.out.print("Cantidad a comprar (debe ser 1): ");
            int cantidad = Integer.parseInt(sc.nextLine());
            
            if (cantidad != 1) {
                System.out.println("Error: Solo se permite comprar 1 paquete deluxe por transacción.");
                return;
            }
            
            // 6. Mostrar resumen de la compra
            System.out.println("\n--- RESUMEN DE COMPRA ---");
            System.out.println("Mercancía y Beneficios: " + paqueteSeleccionado.getMercanciaYBeneficios());
            System.out.println("Tiquetes incluidos: " + 
                (paqueteSeleccionado.getTiquetes().size() + paqueteSeleccionado.getTiquetesAdicionales().size()));
            
            if ("ORGANIZADOR".equalsIgnoreCase(comprador.getTipoUsuario())) {
                System.out.println("Precio original: $" + paqueteSeleccionado.getPrecioPaquete());
                System.out.println("*** CORTESÍA ORGANIZADOR - Total a pagar: $0.00 ***");
            } else {
                System.out.println("Total a pagar: $" + paqueteSeleccionado.getPrecioPaquete());
            }
            
            // 7. Confirmar compra
            System.out.print("\n¿Confirmar compra? (S/N): ");
            String confirmacion = sc.nextLine();
            
            if (!confirmacion.equalsIgnoreCase("S")) {
                System.out.println("Compra cancelada.");
                return;
            }
            
            // 8. Realizar la compra
            ArrayList<Tiquete> tiquetesComprados = trans.comprarPaqueteDeluxe(paqueteSeleccionado, comprador, cantidad, evento, sistema);
            // 9. Mostrar resultado exitoso
            System.out.println("\n¡COMPRA REALIZADA EXITOSAMENTE!");
            System.out.println("═══════════════════════════════════");
            System.out.println("Paquete Deluxe adquirido");
            System.out.println("Beneficios: " + paqueteSeleccionado.getMercanciaYBeneficios());
            System.out.println("Total de tiquetes: " + tiquetesComprados.size());
            
            System.out.println("\nDetalle de tiquetes:");
            for (Tiquete t : tiquetesComprados) {
                System.out.println("  • ID: " + t.getId() + 
                                 " | Localidad: " + t.getLocalidadAsociada() +
                                 " | Precio base: $" + t.getPrecioBaseSinCalcular());
            }
            
            System.out.println("\nNuevo saldo: $" + dueno.getSaldo());
            System.out.println("═══════════════════════════════════");
            
        } catch (TransferenciaNoPermitidaException e) {
            System.out.println("Error en la transacción: " + e.getMessage());
        } catch (TiquetesNoDisponiblesException e) {
            System.out.println("Error de disponibilidad: " + e.getMessage());
        } catch (SaldoInsuficienteExeption e) {
            System.out.println("Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error: Debe ingresar un número válido.");
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

   

    private static void transferirTiquete(IDuenoTiquetes usuario, SistemaPersistencia sistema, Transaccion trans) {
        System.out.println("\n=== TRANSFERIR TIQUETE ===");
        
        // Mostrar tiquetes del usuario
        System.out.println("Tus tiquetes:");
        Collection<Tiquete> misTiquetes = usuario.getTiquetes();
        
        if (misTiquetes == null || misTiquetes.isEmpty()) {
            System.out.println("No tienes tiquetes para transferir.");
            return;
        }
        
        int num = 1;
        HashMap<Integer, Tiquete> menuTiquetes = new HashMap<>();
        
        for (Tiquete t : misTiquetes) {
            String tipo = t instanceof TiqueteMultiple ? "PAQUETE" : "SIMPLE";
            String evento = t.getEvento() != null ? t.getEvento().getNombre() : "Sin evento";
            
            System.out.println(num + ". [" + tipo + "] " + t.getNombre() + 
                " | ID: " + t.getId() + " | Evento: " + evento);
            
            menuTiquetes.put(num, t);
            num++;
        }
        
        System.out.print("\nSelecciona el tiquete a transferir (número): ");
        int seleccion = 0;
        try {
            seleccion = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("✗ Opción inválida.");
            return;
        }
        
        if (!menuTiquetes.containsKey(seleccion)) {
            System.out.println("✗ Opción no válida.");
            return;
        }
        
        Tiquete tiqueteSeleccionado = menuTiquetes.get(seleccion);
        
        // Mostrar detalle del tiquete
        System.out.println("\n=== DETALLE DEL TIQUETE ===");
        System.out.println("Tipo: " + (tiqueteSeleccionado instanceof TiqueteMultiple ? "Paquete Múltiple" : "Tiquete Simple"));
        System.out.println("ID: " + tiqueteSeleccionado.getId());
        System.out.println("Nombre: " + tiqueteSeleccionado.getNombre());
        
        if (tiqueteSeleccionado.getEvento() != null) {
            System.out.println("Evento: " + tiqueteSeleccionado.getEvento().getNombre());
            System.out.println("Fecha: " + tiqueteSeleccionado.getEvento().getFecha());
        }
        
        if (tiqueteSeleccionado instanceof TiqueteMultiple) {
            TiqueteMultiple tm = (TiqueteMultiple) tiqueteSeleccionado;
            System.out.println("Incluye " + tm.getTiquetes().size() + " tiquetes");
        }
        
        System.out.println("===========================");

        System.out.print("\nLogin del usuario destino: ");
        String login = sc.nextLine();

        Usuario destino = sistema.buscarUsuario(login);
        if (destino == null) {
            System.out.println("✗ Usuario no encontrado.");
            return;
        }

        if (!(destino instanceof IDuenoTiquetes)) {
            System.out.println("✗ El usuario destino no puede recibir tiquetes.");
            return;
        }
        
        if (destino.getLogin().equals(((Usuario)usuario).getLogin())) {
            System.out.println("✗ No puedes transferir a ti mismo.");
            return;
        }

        // Confirmar transferencia
        System.out.println("\n=== CONFIRMAR TRANSFERENCIA ===");
        System.out.println("Tiquete: " + tiqueteSeleccionado.getNombre() + " (" + tiqueteSeleccionado.getId() + ")");
        System.out.println("De: " + ((Usuario)usuario).getLogin());
        System.out.println("A: " + destino.getLogin());
        
        if (tiqueteSeleccionado instanceof TiqueteMultiple) {
            System.out.println("NOTA: Se transferirá el paquete completo.");
        }
        
        System.out.println("===============================");
        System.out.print("¿Confirmar transferencia? (s/n): ");

        if (!sc.nextLine().equalsIgnoreCase("s")) {
            System.out.println("Transferencia cancelada.");
            return;
        }

        // Pedir password para confirmar
        System.out.print("Ingresa tu password para confirmar: ");
        String password = sc.nextLine();

        if (!((Usuario)usuario).IsPasswordTrue(password)) {
            System.out.println("✗ Password incorrecta.");
            return;
        }

        try {
            // Fecha actual en formato YYYY-MM-DD
            String fechaActual = java.time.LocalDate.now().toString();
            
            trans.transferirTiquete(
                tiqueteSeleccionado, 
                (Usuario) usuario, 
                destino, 
                fechaActual, 
                sistema
            );
            
            System.out.println("\n================================");
            System.out.println("✓ TRANSFERENCIA EXITOSA");
            System.out.println("================================");
            
        } catch (TiquetesVencidosTransferidos e) {
            System.out.println("✗ " + e.getMessage());
        } catch (IDNoEncontrado e) {
            System.out.println("✗ " + e.getMessage());
        } catch (TransferenciaNoPermitidaException e) {
            System.out.println("✗ " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Error inesperado: " + e.getMessage());
        }
    }
    
    
    

    private static void crearOferta(IDuenoTiquetes usuario, SistemaPersistencia sistema, Transaccion trans) {
     

        System.out.print("ID del tiquete para oferta: ");
        String id = sc.nextLine();

        Tiquete t = sistema.buscarTiquetePorId(id);
        if (t == null) {
            System.out.println("Tiquete no encontrado.");
            return;
        }
        
        Collection<Tiquete> listaTiquetes =usuario.getTiquetes();
        if(!listaTiquetes.contains(t)) {
        	System.out.println("Este tiquete no está en su lista de tiquetes.");
            return;
        }
        
        Queue<HashMap<Tiquete, String>> ofertas = sistema.getMarketplace().getOfertas();
        for (HashMap<Tiquete, String> mapa : ofertas) {
            if (mapa.containsKey(t) && t.getId().equals(id)) {
            	System.out.println("Este tiquete ya fue agrega al sistema.");
                return;
            }	
        }
        
        
        		

        System.out.print("Precio: ");
        double precio = Double.parseDouble(sc.nextLine());

        trans.revenderTiquete(t, precio, (Usuario) usuario, sistema);
        System.out.println("Oferta publicada.");
    }
    
    
    
    private static void cancelarOferta(IDuenoTiquetes usuario, SistemaPersistencia sistema, marketPlaceReventas market) {
        System.out.println("\n=== CANCELAR OFERTA DE REVENTA ===");
        
        Queue<HashMap<Tiquete,String>> todasOfertas = market.getOfertas();
        
        if (todasOfertas == null || todasOfertas.isEmpty()) {
            System.out.println("No hay ofertas en el marketplace.");
            return;
        }
        
        // Buscar ofertas del usuario
        List<OfertaInfo> misOfertas = new ArrayList<>();
        
        for (HashMap<Tiquete,String> oferta : todasOfertas) {
            for (Map.Entry<Tiquete,String> e : oferta.entrySet()) {
                Tiquete t = e.getKey();
                String info = e.getValue();
                
                // Extraer login del vendedor de la info
                String loginVendedor = info.split(" - ")[0];
                
                if (loginVendedor.equals(((Usuario)usuario).getLogin())) {
                    double precio = 0;
                    try {
                        precio = marketPlaceReventas.extraerPrecio(info);
                    } catch (Exception ex) {
                        precio = 0;
                    }
                    misOfertas.add(new OfertaInfo(t, (Usuario)usuario, precio));
                }
            }
        }
        
        if (misOfertas.isEmpty()) {
            System.out.println("No tienes ofertas publicadas.");
            return;
        }
        
        // Mostrar ofertas del usuario
        System.out.println("\nTus ofertas:");
        for (int i = 0; i < misOfertas.size(); i++) {
            OfertaInfo oferta = misOfertas.get(i);
            String eventoNombre = oferta.tiquete.getEvento() != null 
                ? oferta.tiquete.getEvento().getNombre() 
                : "Sin evento";
            
            System.out.println((i + 1) + ". " + oferta.tiquete.getNombre() + " - " + eventoNombre);
            System.out.println("   ID: " + oferta.tiquete.getId());
            System.out.println("   Precio: $" + String.format("%.2f", oferta.precio));
        }
        
        System.out.print("\nSelecciona la oferta a cancelar (número): ");
        int seleccion = 0;
        try {
            seleccion = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("Opción inválida.");
            return;
        }
        
        if (seleccion < 1 || seleccion > misOfertas.size()) {
            System.out.println("Opción no válida.");
            return;
        }
        
        OfertaInfo ofertaSeleccionada = misOfertas.get(seleccion - 1);
        
        // Confirmar cancelación
        System.out.println("\n=== CONFIRMAR CANCELACIÓN ===");
        System.out.println("Tiquete: " + ofertaSeleccionada.tiquete.getNombre());
        System.out.println("ID: " + ofertaSeleccionada.tiquete.getId());
        System.out.println("Precio publicado: $" + String.format("%.2f", ofertaSeleccionada.precio));
        System.out.println("============================");
        System.out.print("¿Confirmar cancelación? (s/n): ");
        
        if (!sc.nextLine().equalsIgnoreCase("s")) {
            System.out.println("Cancelación abortada.");
            return;
        }
        
        try {
            market.eliminarOferta(ofertaSeleccionada.tiquete, (Usuario)usuario, sistema);
            System.out.println("\n✓ Oferta cancelada exitosamente.");
            
        } catch (OfertaNoDIsponibleException e) {
            System.out.println("✗ " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }
    
    private static void contraofertar(IDuenoTiquetes usuario, SistemaPersistencia sistema, marketPlaceReventas market) {
        System.out.println("\n=== HACER CONTRAOFERTA ===");
        
        Queue<HashMap<Tiquete,String>> todasOfertas = market.getOfertas();
        
        if (todasOfertas == null || todasOfertas.isEmpty()) {
            System.out.println("No hay ofertas disponibles.");
            return;
        }
        
        // Filtrar ofertas (excluir las propias)
        System.out.println("\n===== OFERTAS DISPONIBLES =====");
        int num = 1;
        HashMap<Integer, OfertaInfo> menuOfertas = new HashMap<>();
        
        for (HashMap<Tiquete,String> oferta : todasOfertas) {
            for (Map.Entry<Tiquete,String> e : oferta.entrySet()) {
                Tiquete t = e.getKey();
                String info = e.getValue();
                
                // Extraer login del vendedor
                String loginVendedor = info.split(" - ")[0];
                
                // No mostrar ofertas propias
                if (loginVendedor.equals(((Usuario)usuario).getLogin())) {
                    continue;
                }
                
                Usuario vendedor = sistema.buscarUsuario(loginVendedor);
                
                if (vendedor != null) {
                    double precioActual = 0;
                    try {
                        precioActual = marketPlaceReventas.extraerPrecio(info);
                    } catch (Exception ex) {
                        precioActual = 0;
                    }
                    
                    String eventoNombre = t.getEvento() != null 
                        ? t.getEvento().getNombre() 
                        : "Sin evento";
                    
                    System.out.println(num + ". " + t.getNombre() + " - " + eventoNombre);
                    System.out.println("   ID: " + t.getId());
                    System.out.println("   Precio actual: $" + String.format("%.2f", precioActual));
                    System.out.println("   Vendedor: " + loginVendedor);
                    System.out.println();
                    
                    menuOfertas.put(num, new OfertaInfo(t, vendedor, precioActual));
                    num++;
                }
            }
        }
        
        if (menuOfertas.isEmpty()) {
            System.out.println("No hay ofertas disponibles (o todas son tuyas).");
            return;
        }
        
        System.out.println("================================");
        System.out.print("Selecciona la oferta para contraofertar (número): ");
        
        int seleccion = 0;
        try {
            seleccion = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("✗ Opción inválida.");
            return;
        }
        
        if (!menuOfertas.containsKey(seleccion)) {
            System.out.println("✗ Opción no válida.");
            return;
        }
        
        OfertaInfo ofertaSeleccionada = menuOfertas.get(seleccion);
        
        // Mostrar detalle
        System.out.println("\n=== DETALLE DE LA OFERTA ===");
        System.out.println("Tiquete: " + ofertaSeleccionada.tiquete.getNombre());
        System.out.println("ID: " + ofertaSeleccionada.tiquete.getId());
        System.out.println("Precio actual: $" + String.format("%.2f", ofertaSeleccionada.precio));
        System.out.println("Vendedor: " + ofertaSeleccionada.vendedor.getLogin());
        System.out.println("============================");
        
        System.out.print("\nIngresa tu contraoferta (precio): $");
        double nuevoPrecio = 0;
        try {
            nuevoPrecio = Double.parseDouble(sc.nextLine());
        } catch (Exception e) {
            System.out.println("✗ Precio inválido.");
            return;
        }
        
        if (nuevoPrecio <= 0) {
            System.out.println("✗ El precio debe ser mayor a 0.");
            return;
        }
        
        // Confirmar contraoferta
        System.out.println("\n=== CONFIRMAR CONTRAOFERTA ===");
        System.out.println("Tiquete: " + ofertaSeleccionada.tiquete.getNombre());
        System.out.println("Precio actual: $" + String.format("%.2f", ofertaSeleccionada.precio));
        System.out.println("Tu contraoferta: $" + String.format("%.2f", nuevoPrecio));
        
        if (nuevoPrecio >= ofertaSeleccionada.precio) {
            System.out.println("⚠ Tu contraoferta es igual o mayor al precio actual.");
            System.out.println("   ¿Prefieres comprar directamente?");
        }
        
        System.out.println("==============================");
        System.out.print("¿Enviar contraoferta? (s/n): ");
        
        if (!sc.nextLine().equalsIgnoreCase("s")) {
            System.out.println("Contraoferta cancelada.");
            return;
        }
        
        try {
            market.registrarContraoferta(
                ofertaSeleccionada.tiquete,
                ofertaSeleccionada.vendedor,
                (Usuario) usuario,
                nuevoPrecio,
                sistema
            );
            
            System.out.println("\n✓ Contraoferta enviada al vendedor.");
            System.out.println("  El vendedor puede aceptar o rechazar tu oferta.");
            
        } catch (TransferenciaNoPermitidaException e) {
            System.out.println("✗ " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }
    
    private static void verContraofertas(IDuenoTiquetes usuario, SistemaPersistencia sistema, marketPlaceReventas market) {
        System.out.println("\n=== CONTRAOFERTAS RECIBIDAS ===");
        
        List<HashMap<Tiquete,String>> contraofertas = usuario.getListaOfertas();
        
        if (contraofertas == null || contraofertas.isEmpty()) {
            System.out.println("No tienes contraofertas pendientes.");
            return;
        }
        
        List<HashMap<Tiquete,String>> procesadas = new ArrayList<>();
        int num = 1;
        
        for (HashMap<Tiquete,String> contraoferta : contraofertas) {
            for (Map.Entry<Tiquete,String> e : contraoferta.entrySet()) {
                Tiquete tiq = e.getKey();
                String info = e.getValue();
                
                // Extraer comprador y precio
                String[] partes = info.split(" - ");
                if (partes.length < 2) continue;
                
                String loginComprador = partes[0];
                
                double precio = 0;
                try {
                    precio = marketPlaceReventas.extraerPrecio(info);
                } catch (Exception ex) {
                    System.out.println("⚠ Error extrayendo precio de: " + info);
                    continue;
                }
                
                // Buscar comprador
                Usuario comprador = sistema.buscarUsuario(loginComprador);
                
                if (comprador == null) {
                    System.out.println("⚠ Comprador no encontrado: " + loginComprador);
                    continue;
                }
                
                // Buscar tiquete real
                Tiquete tiqueteReal = sistema.buscarTiquetePorId(tiq.getId());
                
                if (tiqueteReal == null) {
                    System.out.println("⚠ Tiquete no encontrado: " + tiq.getId());
                    procesadas.add(contraoferta);
                    continue;
                }
                
                // Mostrar contraoferta
                System.out.println("\n--- Contraoferta #" + num + " ---");
                System.out.println("Tiquete: " + tiqueteReal.getNombre());
                System.out.println("ID: " + tiqueteReal.getId());
                
                if (tiqueteReal.getEvento() != null) {
                    System.out.println("Evento: " + tiqueteReal.getEvento().getNombre());
                }
                
                System.out.println("De: " + loginComprador);
                System.out.println("Precio ofrecido: $" + String.format("%.2f", precio));
                
                // Verificar saldo del comprador
                if (comprador instanceof IDuenoTiquetes) {
                    double saldoComprador = ((IDuenoTiquetes) comprador).getSaldo();
                    System.out.println("Saldo del comprador: $" + String.format("%.2f", saldoComprador));
                    
                    if (saldoComprador < precio) {
                        System.out.println("El comprador NO tiene saldo suficiente.");
                    }
                }
                
                System.out.println("----------------------");
                System.out.println("[1] Aceptar");
                System.out.println("[2] Rechazar");
                System.out.print("Opción: ");
                
                int opcion = 0;
                try {
                    opcion = Integer.parseInt(sc.nextLine());
                } catch (Exception ex) {
                    opcion = 2;
                }
                
                try {
                    if (opcion == 1) {
                        // Aceptar contraoferta
                        market.procesarContraoferta(
                            tiqueteReal,
                            (Usuario) usuario,
                            comprador,
                            true,
                            precio,
                            sistema
                        );
                        
                        System.out.println("\n✓ Contraoferta aceptada exitosamente.");
                        System.out.println("  Recibiste: $" + String.format("%.2f", precio));
                        System.out.println("  Nuevo saldo: $" + String.format("%.2f", usuario.getSaldo()));
                        
                    } else {
                        market.procesarContraoferta(
                            tiqueteReal,
                            (Usuario) usuario,
                            comprador,
                            false,
                            precio,
                            sistema
                        );
                    }
                    
                    procesadas.add(contraoferta);
                    
                } catch (TransferenciaNoPermitidaException ex) {
                    System.out.println("✗ Error: " + ex.getMessage());
                    procesadas.add(contraoferta);
                }
                
                num++;
            }
        }
        
        contraofertas.removeAll(procesadas);
        sistema.guardarTodo();
        
        System.out.println("\n✓ Todas las contraofertas fueron procesadas.");
    }

    private static void comprarMarketplace(IDuenoTiquetes usuario, SistemaPersistencia sistema, Transaccion trans) throws TransferenciaNoPermitidaException {
        System.out.println("\n=== COMPRAR EN MARKETPLACE ===");
        
        marketPlaceReventas marketplace = sistema.getMarketplace();
        Queue<HashMap<Tiquete,String>> ofertas = marketplace.getOfertas();
        
        if (ofertas == null || ofertas.isEmpty()) {
            System.out.println("No hay ofertas disponibles en el marketplace.");
            return;
        }
        
        // Mostrar todas las ofertas
        System.out.println("\n===== OFERTAS DISPONIBLES =====");
        int num = 1;
        HashMap<Integer, OfertaInfo> menuOfertas = new HashMap<>();
        
        for (HashMap<Tiquete,String> oferta : ofertas) {
            for (java.util.Map.Entry<Tiquete,String> e : oferta.entrySet()) {
                Tiquete t = e.getKey();
                String etiqueta = e.getValue();
                double precio = marketPlaceReventas.extraerPrecio(etiqueta);
                
                // Buscar el vendedor
                Usuario vendedor = null;
                for (Usuario u : sistema.getUsuarios()) {
                    if (u instanceof IDuenoTiquetes) {
                        IDuenoTiquetes dueno = (IDuenoTiquetes) u;
                        
                        for (Tiquete tiq : dueno.getTiquetes()) {
                            if (tiq.getId().equals(t.getId())) {
                                vendedor = u;
                                break;
                            }
                        }
                    }
                    if (vendedor != null) break;
                }
                
                if (vendedor != null && !vendedor.getLogin().equals(((Usuario)usuario).getLogin())) {
                    String eventoNombre = t.getEvento() != null ? t.getEvento().getNombre() : "Sin evento";
                    
                    System.out.println(num + ". " + t.getNombre() + " - " + eventoNombre);
                    System.out.println("   ID: " + t.getId());
                    System.out.println("   Precio: $" + String.format("%.2f", precio));
                    System.out.println("   Vendedor: " + vendedor.getLogin());
                    System.out.println();
                    
                    menuOfertas.put(num, new OfertaInfo(t, vendedor, precio));
                    num++;
                }
            }
        }
        
        if (menuOfertas.isEmpty()) {
            System.out.println("No hay ofertas disponibles (o todas son tuyas).");
            return;
        }
        
        System.out.println("================================");
        System.out.print("Selecciona la oferta a comprar (número): ");
        
        int seleccion = 0;
        try {
            seleccion = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("✗ Opción inválida.");
            return;
        }
        
        if (!menuOfertas.containsKey(seleccion)) {
            System.out.println("✗ Opción no válida.");
            return;
        }
        
        OfertaInfo ofertaSeleccionada = menuOfertas.get(seleccion);
        
        // Mostrar resumen
        System.out.println("\n===== RESUMEN DE COMPRA =====");
        System.out.println("Tiquete: " + ofertaSeleccionada.tiquete.getNombre());
        System.out.println("ID: " + ofertaSeleccionada.tiquete.getId());
        System.out.println("Precio: $" + String.format("%.2f", ofertaSeleccionada.precio));
        System.out.println("Vendedor: " + ofertaSeleccionada.vendedor.getLogin());
        System.out.println("----------------------------");
        System.out.println("Tu saldo: $" + String.format("%.2f", usuario.getSaldo()));
        System.out.println("Saldo después: $" + String.format("%.2f", usuario.getSaldo() - ofertaSeleccionada.precio));
        System.out.println("============================");
        
        if (ofertaSeleccionada.precio > usuario.getSaldo()) {
            System.out.println("✗ Saldo insuficiente.");
            return;
        }
        
        System.out.print("\n¿Confirmar compra? (s/n): ");
        if (!sc.nextLine().equalsIgnoreCase("s")) {
            System.out.println("Compra cancelada.");
            return;
        }
        
        try {
            trans.comprarEnMarketplace(
                ofertaSeleccionada.tiquete,
                ofertaSeleccionada.vendedor,
                (Usuario) usuario,
                sistema
            );
            
            System.out.println("\n✓ ¡Compra en marketplace exitosa!");
            System.out.println("  Tiquete: " + ofertaSeleccionada.tiquete.getId());
            System.out.println("  Pagado: $" + String.format("%.2f", ofertaSeleccionada.precio));
            System.out.println("  Nuevo saldo: $" + String.format("%.2f", usuario.getSaldo()));
            
        } catch (TiquetesNoDisponiblesException e) {
            System.out.println("✗ " + e.getMessage());
        } catch (TransferenciaNoPermitidaException e) {
            System.out.println("✗ " + e.getMessage());
        } catch (SaldoInsuficienteExeption e) {
            System.out.println("✗ " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    // Clase auxiliar
    private static class OfertaInfo {
        Tiquete tiquete;
        Usuario vendedor;
        double precio;
        
        OfertaInfo(Tiquete tiquete, Usuario vendedor, double precio) {
            this.tiquete = tiquete;
            this.vendedor = vendedor;
            this.precio = precio;
        }
    }

    private static void solicitarReembolso(IDuenoTiquetes usuario, SistemaPersistencia sistema, Transaccion trans) {
       

        System.out.print("ID del tiquete para reembolso: ");
        String id = sc.nextLine();

        Tiquete t = sistema.buscarTiquetePorId(id);
        if (t == null) {
            System.out.println("Tiquete no encontrado.");
            return;
        }

        System.out.print("Motivo: ");
        String motivo = sc.nextLine();

        trans.solicitarReembolso(t, motivo, sistema);
        System.out.println("Solicitud enviada.");
    }

    private static void recargarSaldo(IDuenoTiquetes usuario) {
   
        System.out.print("Cantidad a recargar: ");
        double valor = Double.parseDouble(sc.nextLine());
        usuario.actualizarSaldo(usuario.getSaldo() + valor);
        System.out.println("Saldo actualizado.");
    }
    


    private static void crearEvento(Organizador organizador, SistemaPersistencia sistema) {
        System.out.print("Nombre del evento: ");
        String nombre = sc.nextLine();
        
        System.out.print("Fecha (YYYY-MM-DD): ");
        String fecha = sc.nextLine();
        
        System.out.print("Hora (HH:MM): ");
        String hora = sc.nextLine();
        
        // Mostrar venues disponibles (si tienes una lista en el sistema)
        System.out.println("\nVenues disponibles:");
        // TODO: Si tienes una lista de venues en SistemaPersistencia, mostrarlos aquí
        // Por ahora, pedimos la ubicación manualmente
        
        System.out.print("Ubicación del venue: ");
        String ubicacion = sc.nextLine();
        
        System.out.print("Capacidad del venue: ");
        int capacidad = 0;
        try {
            capacidad = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("Capacidad inválida.");
            return;
        }
        
        // Crear venue temporal (o buscarlo si ya existe en tu sistema)
        Venue venue = new Venue(ubicacion, capacidad, true); // Asumimos aprobado
        
        try {
            // Crear evento con tiquetes vacíos inicialmente
            HashMap<String, Tiquete> tiquetesDisponibles = new HashMap<>();
            
            Evento nuevoEvento = organizador.crearEvento(
                nombre, 
                fecha, 
                hora, 
                tiquetesDisponibles, 
                venue, 
                organizador.getLogin(),
                sistema
            );
            
            System.out.println("✓ Evento '" + nombre + "' creado exitosamente.");
            
        } catch (Exception e) {
            System.out.println("✗ Error al crear evento: " + e.getMessage());
        }
    }
    
    private static void agregarTiquetesEvento(Organizador organizador, SistemaPersistencia sistema) {
        System.out.println("\n=== AGREGAR TIQUETES A EVENTO ===");
        
        // Mostrar eventos del organizador
        System.out.println("Tus eventos:");
        List<Evento> misEventos = new ArrayList<>();
        for (Evento e : sistema.getEventos()) {
            if (e.getLoginOrganizador().equals(organizador.getLogin())) {
                misEventos.add(e);
                if(!e.getCancelado()) {
                System.out.println("- " + e.getNombre());
                }
                
            }
        }
        
        if (misEventos.isEmpty()) {
            System.out.println("No tienes eventos creados.");
            return;
        }
        
        System.out.print("\nNombre del evento: ");
        String nombreEvento = sc.nextLine();
        
        Evento evento = sistema.buscarEventoPorNombre(nombreEvento);
        if (evento == null || !evento.getLoginOrganizador().equals(organizador.getLogin())) {
            System.out.println("✗ Evento no encontrado o no te pertenece.");
            return;
        }
        
        // ===== ELEGIR TIPO DE TIQUETE =====
        System.out.println("\n¿Qué tipo de tiquete deseas crear?");
        System.out.println("1. Tiquetes simples");
        System.out.println("2. Paquete/Tiquete múltiple");
        System.out.print("Seleccione: ");
        
        String opcion = sc.nextLine();
        
        switch (opcion) {
            case "1":
                crearTiquetesSimples(evento, sistema);
                break;
            case "2":
                crearTiqueteMultiple(evento, sistema);
                break;
            default:
                System.out.println("✗ Opción inválida.");
        }
    }

    // ===== CREAR TIQUETES SIMPLES =====
    private static void crearTiquetesSimples(Evento evento, SistemaPersistencia sistema) {
        System.out.println("\n=== CREAR TIQUETES SIMPLES ===");
        
        System.out.print("Nombre de la localidad (ej: VIP, Platea, General): ");
        String nombreLocalidad = sc.nextLine();
        
        System.out.print("Precio base: ");
        double precio = 0;
        try {
            precio = Double.parseDouble(sc.nextLine());
        } catch (Exception e) {
            System.out.println("✗ Precio inválido.");
            return;
        }
        
        System.out.print("Capacidad de la localidad: ");
        int capacidad = 0;
        try {
            capacidad = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("✗ Capacidad inválida.");
            return;
        }
        
        System.out.print("Tipo (0=NUMERADA, 1=SIN_NUMERAR): ");
        int tipo = 0;
        try {
            tipo = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("✗ Tipo inválido.");
            return;
        }
        
        System.out.print("Recargo (% ej: 10 para 10%): ");
        double recargo = 0;
        try {
            recargo = Double.parseDouble(sc.nextLine());
        } catch (Exception e) {
            recargo = 5.0; // Por defecto 5%
        }
        
        // Crear localidad
        Localidad localidad = new Localidad(nombreLocalidad, precio, capacidad, tipo);
        
        System.out.print("¿Cuántos tiquetes generar? (máx: " + capacidad + "): ");
        int cantidadTiquetes = 0;
        try {
            cantidadTiquetes = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("✗ Cantidad inválida.");
            return;
        }
        
        if (cantidadTiquetes > capacidad || cantidadTiquetes <= 0) {
            System.out.println("✗ Cantidad no válida.");
            return;
        }
        
        // Generar tiquetes simples
        int generados = 0;
        for (int i = 0; i < cantidadTiquetes; i++) {
            String idTiquete = sistema.generarIdTiquete();
            
            TiqueteSimple tiquete = new TiqueteSimple(
            	    "SIMPLE",              // tipoTiquete
            	    idTiquete,             // identificador
            	    evento.getFecha(),     // fechaExpiracion ✅
            	    precio,                // precio ✅
            	    nombreLocalidad,       // nombre
            	    false,                 // transferido
            	    false,                 // anulado
            	    evento,                // eventoAsociado
            	    recargo,               // recargo ✅
            	    localidad              // localidadAsociada
            	);
            
            try {
                evento.agregarTiquete(tiquete);
                sistema.agregarTiquete(tiquete);
                generados++;
            } catch (Exception e) {
                System.out.println("✗ Error al generar tiquete: " + e.getMessage());
            }
        }
        
        sistema.guardarTodo();
        System.out.println("✓ Se generaron " + generados + " tiquetes SIMPLES para '" + nombreLocalidad + "'");
    }

    // ===== CREAR TIQUETE MÚLTIPLE =====
    private static void crearTiqueteMultiple(Evento evento, SistemaPersistencia sistema) {
        System.out.println("\n=== CREAR PAQUETE/TIQUETE MÚLTIPLE ===");
        
        System.out.print("Nombre del paquete (ej: Paquete VIP Premium, Combo Familiar): ");
        String nombrePaquete = sc.nextLine();
        
        System.out.print("Precio base del paquete: ");
        double precioPaquete = 0;
        try {
            precioPaquete = Double.parseDouble(sc.nextLine());
        } catch (Exception e) {
            System.out.println("✗ Precio inválido.");
            return;
        }
        
        System.out.print("Recargo del paquete (% ej: 10 para 10%): ");
        double recargoPaquete = 0;
        try {
            recargoPaquete = Double.parseDouble(sc.nextLine());
        } catch (Exception e) {
            recargoPaquete = 5.0;
        }
        
        System.out.print("¿Cuántos tiquetes incluye el paquete?: ");
        int cantidadTiquetes = 0;
        try {
            cantidadTiquetes = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("✗ Cantidad inválida.");
            return;
        }
        
        if (cantidadTiquetes <= 0) {
            System.out.println("✗ Debe incluir al menos 1 tiquete.");
            return;
        }
        
        // ===== PREGUNTAR UNA SOLA VEZ LA INFORMACIÓN DE LA LOCALIDAD =====
        System.out.println("\n--- Información de los tiquetes (todos serán iguales) ---");
        
        System.out.print("Nombre de la localidad: ");
        String nombreLocalidad = sc.nextLine();
        
        System.out.print("Precio base individual: ");
        double precioIndividual = 0;
        try {
            precioIndividual = Double.parseDouble(sc.nextLine());
        } catch (Exception e) {
            System.out.println("✗ Precio inválido, usando 0.");
            precioIndividual = 0;
        }
        
        System.out.print("Capacidad: ");
        int capacidad = 0;
        try {
            capacidad = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            capacidad = 100; // Default
        }
        
        System.out.print("Tipo (0=NUMERADA, 1=SIN_NUMERAR): ");
        int tipoLocalidad = 0;
        try {
            tipoLocalidad = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            tipoLocalidad = 0;
        }
        
        // Crear UNA localidad que usarán todos
        Localidad localidad = new Localidad(nombreLocalidad, precioIndividual, capacidad, tipoLocalidad);
        
        // ===== GENERAR LOS TIQUETES (TODOS IGUALES) =====
        ArrayList<TiqueteSimple> tiquetesDelPaquete = new ArrayList<>();
        
        System.out.println("\nGenerando " + cantidadTiquetes + " tiquetes de '" + nombreLocalidad + "'...");
        
        for (int i = 1; i <= cantidadTiquetes; i++) {
            String idTiquete = sistema.generarIdTiquete();
            
            TiqueteSimple tiqueteInterno = new TiqueteSimple(
                "SIMPLE",              // tipoTiquete
                idTiquete,             // identificador
                evento.getFecha(),     // fechaExpiracion
                precioIndividual,      // precio
                nombreLocalidad,       // nombre
                false,                 // transferido
                false,                 // anulado
                evento,                // eventoAsociado
                0,                     // recargo (los tiquetes internos no tienen recargo propio)
                localidad              // localidadAsociada
            );
            
            tiquetesDelPaquete.add(tiqueteInterno);
            sistema.agregarTiquete(tiqueteInterno);
            
            System.out.println("  ✓ Generado: " + idTiquete);
        }
        
        // ===== CREAR EL TIQUETE MÚLTIPLE =====
        String idPaquete = sistema.generarIdTiquete();
        
        TiqueteMultiple paquete = new TiqueteMultiple(
            "MULTIPLE",
            idPaquete,
            evento.getFecha(),
            precioPaquete,
            nombrePaquete,
            false,
            false,
            evento,
            recargoPaquete,
            null, // o localidad si quieres asociarla al paquete
            tiquetesDelPaquete
        );
        
        try {
            evento.agregarTiquete(paquete);
            sistema.agregarTiquete(paquete);
            sistema.guardarTodo();
            
            System.out.println("\n✓ Paquete '" + nombrePaquete + "' creado exitosamente!");
            System.out.println("  ID: " + idPaquete);
            System.out.println("  Incluye " + cantidadTiquetes + " tiquetes de '" + nombreLocalidad + "'");
            System.out.println("  Precio del paquete: $" + String.format("%.2f", precioPaquete));
            System.out.println("  Precio individual por tiquete: $" + String.format("%.2f", precioIndividual));
            
        } catch (Exception e) {
            System.out.println("✗ Error al crear paquete: " + e.getMessage());
        }
    }
  
    private static void solicitarCancelacionEvento(Organizador organizador, SistemaPersistencia sistema) {
        System.out.println("\n=== TUS EVENTOS ACTIVOS ===");
        
        List<Evento> misEventos = new ArrayList<>();
        for (Evento e : sistema.getEventos()) {
            if (e.getLoginOrganizador().equals(organizador.getLogin())) {
                misEventos.add(e);
                if(!e.getCancelado()) {
                	System.out.println("- " + e.getNombre() + " (" + e.getFecha() + ")");
                }
                
            }
        }
        
        if (misEventos.isEmpty()) {
            System.out.println("No tienes eventos creados.");
            return;
        }
        
        System.out.print("\nNombre del evento a cancelar: ");
        String nombre = sc.nextLine();
        
        Evento evento = sistema.buscarEventoPorNombre(nombre);
        
        if (evento == null) {
            System.out.println("✗ Evento no encontrado.");
            return;
        }
        
        if (!evento.getLoginOrganizador().equals(organizador.getLogin())) {
            System.out.println("✗ No puedes cancelar un evento que no es tuyo.");
            return;
        }
        
        System.out.print("Motivo de cancelación: ");
        String motivo = sc.nextLine();
        
        try {
            organizador.solicitarCancelacioDeEvento(evento, motivo, sistema);
            System.out.println("✓ Solicitud de cancelación enviada al administrador.");
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private static void sugerirVenue(Promotor promotor, SistemaPersistencia sistema) {
        System.out.println("\n=== SUGERIR NUEVO VENUE ===");
        
        System.out.print("Ubicación del venue: ");
        String ubicacion = sc.nextLine();
        
        System.out.print("Capacidad máxima: ");
        int capacidad = 0;
        try {
            capacidad = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("✗ Capacidad inválida.");
            return;
        }
        
        System.out.print("Mensaje/descripción para el administrador: ");
        String mensaje = sc.nextLine();
        
        // Crear venue (no aprobado, requiere aprobación del admin)
        Venue venue = new Venue(ubicacion, capacidad, false);
        
        try {
            promotor.sugerirVenue(venue, mensaje, sistema);
            System.out.println("✓ Venue sugerido exitosamente. El administrador lo revisará.");
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private static void verGanancias(Promotor promotor) {
        System.out.println("\n=== CALCULANDO GANANCIAS ===");
        
        System.out.print("Ingrese el cobro de emisión a aplicar: ");
        double cobroEmision = 0.0;
        try {
            cobroEmision = Double.parseDouble(sc.nextLine());
        } catch (Exception e) {
            System.out.println("Usando cobro de emisión por defecto: 0.0");
            cobroEmision = 0.0;
        }
        
        promotor.verGanancias(cobroEmision);
        
        System.out.println("\nPresiona Enter para continuar...");
        sc.nextLine();
    }





    // ----------------------------------------------------------
    // MENÚ ADMINISTRADOR
    // ----------------------------------------------------------
    private static void menuAdministrador(Administrador admin, SistemaPersistencia sistema) {

  
        int op = -1;

        do {
            System.out.println("\n====== MENÚ ADMINISTRADOR ======");
            System.out.println("1. Crear Venue");
            System.out.println("2. Revisar solicitudes Venue");
            System.out.println("3. Ver reembolsos");
            System.out.println("4. Ver reventas (log marketplace)");
            System.out.println("5. Cancelar evento");
            System.out.println("6. Gestionar solicitudes de cancelación de evento");
            System.out.println("7. fijar cobro emision");
            System.out.println("8. fijar recargo");
            System.out.println("0. Salir");
            System.out.print("Seleccione: ");

            try { op = Integer.parseInt(sc.nextLine()); }
            catch (Exception e) { op = -1; }

            switch (op) {
                case 1: {
                    System.out.print("Ubicación del Venue: ");
                    String ubic = sc.nextLine();
                    System.out.print("Capacidad máxima: ");
                    int cap = 0;
                    try { cap = Integer.parseInt(sc.nextLine()); } catch (Exception ignored) {}
                    System.out.print("¿Aprobado por defecto? (s/n): ");
                    String apr = sc.nextLine();
                    boolean aprobado = "s".equalsIgnoreCase(apr);
                    Venue v = admin.crearVenue(ubic, cap, aprobado);
                    System.out.println("Venue creado: " + v.getUbicacion() + " (aprobado=" + aprobado + ")");
                    // Si quieres guardar venues en algún repositorio, hazlo desde SistemaPersistencia.
                    sistema.guardarTodo();
                    break;
                }

                case 2:
                    // Procesa y persiste cambios en las solicitudes de venues
                	verSolicitudVenue(admin, sistema);
                    break;

                case 3:
                	verSolicitudesReembolso(admin, sistema); 
           
                    break;

                case 4:
                    // Mostrar log del marketplace (requiere que SistemaPersistencia exponga el marketplace)
                    if (sistema.getMarketplace() != null) {
                        sistema.getMarketplace().verLogEventos(admin);
                    } else {
                        System.out.println("Marketplace no disponible en el sistema.");
                    }
                    break;

                case 5: {
                    // Cancelar evento: pedir nombre y cancelar
                    System.out.println("Eventos actuales:");
                    for (Evento e : sistema.getEventos()) {
                    	if(!e.getCancelado()) {
                    		System.out.println("- " + e.getNombre());
                    	}
                        
                    }
                    System.out.print("Nombre del evento a cancelar: ");
                    String nombreEv = sc.nextLine();
                    Evento ev = sistema.buscarEventoPorNombre(nombreEv);
                    if (ev == null) {
                        System.out.println("Evento no encontrado.");
                    } else {
                        admin.cancelarEvento(ev);
                        // actualizar evento en lista del sistema y persistir
                        List<Evento> lista = sistema.getEventos();
                        for (int i = 0; i < lista.size(); i++) {
                            if (lista.get(i).getNombre().equals(ev.getNombre())) {
                                lista.set(i, ev);
                                break;
                            }
                        }
                        sistema.guardarTodo();
                        System.out.println("Evento cancelado y persistido.");
                    }
                    break;
                }

                case 6:
                    // Usar la implementación del admin que gestiona solicitudes de cancelación y que necesita SistemaPersistencia
                    admin.verSolicitudCancelacionEvento(sistema);
                    // el método internamente persiste cuando corresponde, pero forzamos guardar por seguridad
       
                    break;
                    
                case 7:
                	System.out.print("Ingrese el cobro de emision: ");
                    double cobro = 	Double.parseDouble(sc.nextLine());
                    admin.fijarCobroEmisionImpresion(cobro);
                    sistema.guardarTodo();
                    break;
                    
                    
                case 8:
                	System.out.print("Ingrese el recargo: ");
                    double recargo = Double.parseDouble(sc.nextLine());
                    admin.cobrarPorcentajeAdicional(recargo);
                    sistema.guardarTodo();
                    break;

                case 0:
                    System.out.println("Saliendo del menú administrador...");
                    break;

                default:
                    System.out.println("Opción inválida.");
                    break;
            }

        } while (op != 0);
    }
    
    private static void verSolicitudVenue(Administrador admin, SistemaPersistencia sistema) {
    	Queue<HashMap<Venue,String>> pendientes = admin.getSolicitudesVenue();

    	for (HashMap<Venue,String> s : pendientes) {
    	    for (Entry<Venue,String> entry : s.entrySet()) {
    	        Venue v = entry.getKey();
    	        String msg = entry.getValue();

    	        System.out.println("Solicitud de Venue:");
    	        System.out.println("Ubicación: " + v.getUbicacion());
    	        System.out.println("Capacidad: " + v.getCapacidadMax());
    	        System.out.println("Mensaje: " + msg);

    	        System.out.println("[1] Aceptar");
    	        System.out.println("[2] Rechazar");
    	        int opcion = sc.nextInt(); sc.nextLine();

    	        boolean aceptada = (opcion == 1);

    	        admin.verSolicitudVenue(v, msg, sistema, aceptada);
    	    }
    	}
    }
    
    
    private static void verSolicitudesReembolso(Administrador admin, SistemaPersistencia sistema) {
        System.out.println("\n=== SOLICITUDES DE REEMBOLSO ===");
        
    
        Queue<HashMap<Tiquete,String>> solicitudes = admin.getSolicitudes();
        
        if (solicitudes == null || solicitudes.isEmpty()) {
            System.out.println("No hay solicitudes de reembolso pendientes.");
            return;
        }
        
        List<HashMap<Tiquete,String>> procesadas = new ArrayList<>();
        int num = 1;
        
        for (HashMap<Tiquete,String> solicitud : solicitudes) {
            for (Map.Entry<Tiquete,String> entry : solicitud.entrySet()) {
                Tiquete tiq = entry.getKey();
                String motivo = entry.getValue();
                
                // Buscar tiquete real
                Tiquete tiqueteReal = sistema.buscarTiquetePorId(tiq.getId());
                
                if (tiqueteReal == null) {
                    System.out.println(" Tiquete no encontrado: " + tiq.getId());
                    procesadas.add(solicitud);
                    continue;
                }
                
                // Buscar el dueño del tiquete
                Usuario dueno = null;
                for (Usuario u : sistema.getUsuarios()) {
                    if (u instanceof IDuenoTiquetes) {
                        IDuenoTiquetes duenoT = (IDuenoTiquetes) u;
                        
                        for (Tiquete t : duenoT.getTiquetes()) {
                            if (t.getId().equals(tiqueteReal.getId())) {
                                dueno = u;
                                break;
                            }
                        }
                    }
                    if (dueno != null) break;
                }
                
                if (dueno == null) {
                    System.out.println(" No se encontró dueño para tiquete: " + tiq.getId());
                    procesadas.add(solicitud);
                    continue;
                }
                
                // Mostrar solicitud
                System.out.println("\n--- Solicitud #" + num + " ---");
                System.out.println("Usuario: " + dueno.getLogin());
                System.out.println("Tiquete: " + tiqueteReal.getNombre());
                System.out.println("ID: " + tiqueteReal.getId());
                
                if (tiqueteReal.getEvento() != null) {
                    System.out.println("Evento: " + tiqueteReal.getEvento().getNombre());
                    System.out.println("Fecha evento: " + tiqueteReal.getEvento().getFecha());
                }
                
                double precioBase = tiqueteReal.getPrecioBaseSinCalcular();
                
                System.out.println("Precio base: $" + String.format("%.2f", precioBase));
                System.out.println("Motivo: " + motivo);
                System.out.println("----------------------");
                System.out.println("[1] Aceptar reembolso");
                System.out.println("[2] Rechazar");
                System.out.print("Opción: ");
                
                int opcion = 0;
                try {
                    opcion = Integer.parseInt(sc.nextLine());
                } catch (Exception e) {
                    opcion = 2;
                }
                
                try {
                    if (opcion == 1) {
                        admin.verSolicitudReembolso(dueno, tiqueteReal, sistema, true);
                    } else {
                        admin.verSolicitudReembolso(dueno, tiqueteReal, sistema, false);
                    }
                    
                    procesadas.add(solicitud);
                    
                } catch (TransferenciaNoPermitidaException e) {
                    System.out.println(" Error: " + e.getMessage());
                    procesadas.add(solicitud);
                }
                
                num++;
            }
        }
        
        System.out.println("\n Todas las solicitudes fueron procesadas.");
    }
    
}