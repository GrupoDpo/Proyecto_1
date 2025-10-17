package Finanzas;

import usuario.IDuenoTiquetes;
import usuario.Usuario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import Evento.Evento;
import Persistencia.IFormateo;
import tiquete.PaqueteDeluxe;
import tiquete.Tiquete;
import tiquete.TiqueteMultiple;
import tiquete.TiqueteSimple;

public class Transaccion implements IFormateo {
	
	Tiquete tiquete;
	int numeroMaxTransaccion;
	String  metodoPago;
	
	
	
	public Transaccion(Tiquete tiquete, int numeroMaxTransaccion, String metodoPago) {
		this.tiquete = tiquete;
		this.numeroMaxTransaccion = numeroMaxTransaccion;
		this.metodoPago = metodoPago;
	}
	
	public void transferirTiquete(Tiquete tiquete, Usuario dueno, Usuario usuarioDestino, String fechaActual) {
		if (dueno.getTipoUsuario().equalsIgnoreCase("ADMINISTRADOR")) {
			try (Scanner sc = new Scanner(System.in)) {
				System.out.print("Ingresa el login del destinatario: ");
				String login = sc.nextLine();
				System.out.print("Ingresa tu password: ");
				String password = sc.nextLine();
				
				
				if (usuarioDestino.getLogin() == login && dueno.IsPasswordTrue(password)) {
					
					if (dueno instanceof IDuenoTiquetes && usuarioDestino instanceof IDuenoTiquetes) {
					    Collection<Tiquete> tiquetes = ((IDuenoTiquetes) dueno).getTiquetes();
					    TiqueteMultiple tm = (TiqueteMultiple) tiquete;
					    System.out.print("¿Desea transferir todo el paquete? (s/n): ");
					    String opcion = sc.nextLine();
					    if (opcion.equalsIgnoreCase("s")) {
					    	boolean puedeTransferirse = true;
				            for (TiqueteSimple t : tm.getTiquetes()) {
				                if (!t.esVigente(fechaActual) || t.isTransferido()) {
				                    puedeTransferirse = false;
				                    break;
				                }
				            }
				            
				            if(puedeTransferirse) {
				            	((IDuenoTiquetes) dueno).eliminarTiquete(tm);
				                ((IDuenoTiquetes) usuarioDestino).agregarTiquete(tm);
				                tm.setTransferido(true);
				                for (Tiquete t : tm.getTiquetes()) {
				                    t.setTransferido(true);
				                }
				                System.out.println("Paquete completo transferido con éxito.");
				            } else {
				                System.out.println("No se puede transferir el paquete: contiene tiquetes vencidos o ya transferidos.");
				            }
				            
				        }else {
				        	System.out.print("Ingrese el ID del tiquete individual dentro del paquete: ");
				            String idTiquete = sc.nextLine();

				            boolean encontrado = false;
				            for (TiqueteSimple t : tm.getTiquetes()) {
				                if (t.getId().equals(idTiquete)) {
				                    encontrado = true;
				                    if (!t.esVigente(fechaActual)) {
				                        System.out.println("ERROR: el tiquete está vencido.");
				                    } else if (t.isTransferido()) {
				                        System.out.println("ERROR: el tiquete ya fue transferido anteriormente.");
				                    } else {
				                        t.setTransferido(true);
				                        ((IDuenoTiquetes) usuarioDestino).agregarTiquete(t);
				                        ((IDuenoTiquetes) dueno).eliminarTiquete(t);
				                        System.out.println("Tiquete individual transferido con éxito.");
				                    }
				                    break;
				                }
				            }

				            if (!encontrado) {
				                System.out.println("ERROR: el ID no corresponde a ningún tiquete dentro del paquete.");
				            }
				        }
					    
					    
					 System.out.print("Ingrese el id del tiquete a transferir: ");
				     String idTiquete = sc.nextLine();
				        for(Tiquete t: tiquetes) {
				        	if(idTiquete.equals(t.getId())) {
				        		((IDuenoTiquetes) dueno).eliminarTiquete(tiquete);
								((IDuenoTiquetes) usuarioDestino).agregarTiquete(tiquete);
								tiquete.setTransferido(true);
							}else {
								System.out.print("ERROR: el id escrito no es válido");
										
								}
									}
				            	
				           
							}
					    
						
			 }else {
				System.out.print("ERROR: login o password incorrecto");
					}
				        }
				            	
				       }
					     
			}
	
	
	public ArrayList<Tiquete> comprarTiquete(Tiquete tiqueteComprar, Usuario comprador,int cantidad, Evento eventoAsociado) {
		ArrayList<Tiquete> tiquetesComprados = new ArrayList<>();
		Collection<Tiquete> tiquetes = eventoAsociado.getTiquetesDisponibles();
		if (tiquetes.isEmpty()) {
	        System.out.println("ERROR: No hay tiquetes disponibles.");
	        return tiquetesComprados;
	    }
		
			
		
		if (cantidad > numeroMaxTransaccion) {
		   System.out.println("ERROR: Supera el número máximo de tiquetes por transacción.");
		    return tiquetesComprados;
		    }
		
		if(comprador instanceof IDuenoTiquetes) {
			double saldo =((IDuenoTiquetes) comprador).getSaldo();
			double precioTotal = 0;
			
			

			if (tiqueteComprar instanceof TiqueteMultiple) {
			   precioTotal = tiqueteComprar.calcularPrecio();
			} else {
			    precioTotal = tiqueteComprar.calcularPrecio() * cantidad;
			    }
			
			if(precioTotal<= saldo ) {
				saldo -= precioTotal;
				((IDuenoTiquetes) comprador).actualizarSaldo(saldo);
			}else {
				System.out.print("ERROR: Saldo insuficiente");
				return tiquetesComprados;
				}
			
			if (tiqueteComprar instanceof TiqueteMultiple) {
				
				TiqueteMultiple tm = (TiqueteMultiple) tiqueteComprar;
				for (Tiquete t : tm.getTiquetes()) {
					((IDuenoTiquetes) comprador).agregarTiquete(t);
		            eventoAsociado.venderTiquete(t.getId());
		            tiquetesComprados.add(t);
		        }	
				}else {
					for (int i = 0; i < cantidad; i++) {
						((IDuenoTiquetes) comprador).agregarTiquete(tiqueteComprar);
			            eventoAsociado.venderTiquete(tiqueteComprar.getId());
			            tiquetesComprados.add(tiqueteComprar);
					}
			}
			
		}
	
		return tiquetesComprados;
	}
	
	
	public ArrayList<Tiquete> comprarPaqueteDeluxe(PaqueteDeluxe paquete,
            Usuario comprador, int cantidad, Evento eventoAsociado ) {
        ArrayList<Tiquete> tiquetesComprados = new ArrayList<>();

       
        if (paquete == null || comprador == null || eventoAsociado == null) {
            System.out.println("ERROR: Datos incompletos para comprar paquete Deluxe.");
            return tiquetesComprados;
        }
        if (cantidad <= 0) {
            System.out.println("ERROR: La cantidad debe ser mayor que cero.");
            return tiquetesComprados;
        }
        if (cantidad > numeroMaxTransaccion) {
            System.out.println("ERROR: Supera el número máximo de paquetes por transacción.");
            return tiquetesComprados;
        }

        if (cantidad != 1) {
            System.out.println("ERROR: Por ahora solo se soporta comprar 1 Paquete Deluxe por transacción con el paquete recibido.");
            return tiquetesComprados;
        }
        
        ArrayList<Tiquete> tiqsPaquete = new ArrayList<>();
        if (paquete.getTiquetes() != null) {
            for (Tiquete t : paquete.getTiquetes()) {
                if (t != null) {
                	tiqsPaquete.add(t);
                }
                	
            }
        }
        if (paquete.getTiquetesAdicionales() != null) {
            for (Tiquete t : paquete.getTiquetesAdicionales()) {
                if (t != null) {
                	tiqsPaquete.add(t);
            }
        }
        }

        if (tiqsPaquete.isEmpty()) {
            System.out.println("ERROR: El paquete Deluxe no contiene tiquetes.");
            return tiquetesComprados;
        }

        for (Tiquete t : tiqsPaquete) {
            Tiquete enEvento = eventoAsociado.getTiquetePorId(t.getId());
            if (enEvento == null) {
                System.out.println("ERROR: Tiquete no disponible en el evento: " + t.getId());
                return tiquetesComprados;
            }
        }

        double total = paquete.getPrecioPaquete();

    
        boolean esCortesiaOrganizador = "ORGANIZADOR".equalsIgnoreCase(comprador.getTipoUsuario());
        if (esCortesiaOrganizador) {
            total = 0.0;
        }

      
        if (comprador instanceof IDuenoTiquetes) {
            IDuenoTiquetes dueno = (IDuenoTiquetes) comprador;
            double saldo = dueno.getSaldo();

            if (total > saldo) {
                System.out.println("ERROR: Saldo insuficiente para comprar el paquete Deluxe.");
                return tiquetesComprados;
            }
            
            if (total > 0) {
                saldo -= total;
                dueno.actualizarSaldo(saldo);
            }
        } else {
            System.out.println("ERROR: El comprador no puede poseer tiquetes (no implementa IDuenoTiquetes).");
            return tiquetesComprados;
        }

        
        for (Tiquete t : tiqsPaquete) {
            
            t.setTransferido(true); 
        }

  
        IDuenoTiquetes dueno = (IDuenoTiquetes) comprador;
        for (Tiquete t : tiqsPaquete) {
            dueno.agregarTiquete(t);
            eventoAsociado.venderTiquete(t.getId());
            tiquetesComprados.add(t);
        }

        return tiquetesComprados;
	}
	
	
	public Tiquete getTiquete() {
		return this.tiquete;
		}

	public int getNumeroMaxTransaccion() {
			return this.numeroMaxTransaccion;
	}

	

	public void setNumeroMaxTransaccion(int numeroMaxTransaccion) {
			this.numeroMaxTransaccion = numeroMaxTransaccion;
		
		
	
	
	}

	@Override
	public String formatear() {
		// TODO Auto-generated method stub
		return null;
	}
	
}

