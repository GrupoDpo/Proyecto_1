package Finanzas;

import usuario.Administrador;
import usuario.IDuenoTiquetes;
import usuario.Usuario;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import Evento.Evento;
import Persistencia.PersistenciaTransacciones;
import Persistencia.PersistenciaUsuarios;
import excepciones.IDNoEncontrado;
import excepciones.SaldoInsuficienteExeption;
import excepciones.TiquetesNoDisponiblesException;
import excepciones.TiquetesVencidosTransferidos;
import excepciones.TransferenciaNoPermitidaException;
import excepciones.UsuarioPasswordIncorrecto;
import tiquete.PaqueteDeluxe;
import tiquete.Tiquete;
import tiquete.TiqueteMultiple;
import tiquete.TiqueteSimple;

public class Transaccion implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Tiquete tiquete;
	private Usuario dueno;
	private LocalDateTime fecha;
	private static final double NUMERO_MAX_TRANSACCION = 10;
	private String tipoTransaccion;
	private double valorTransaccion;
	private Registro registro;
	
	
	
	public Transaccion(String tipoTransaccion, Tiquete tiquete, Usuario dueno, LocalDateTime localDateTime, Registro registro,
			double valorTransaccion) {
		this.tiquete = tiquete;
		this.dueno = dueno;
		this.setFecha(localDateTime);
		this.setRegistro(registro);
		this.setTipoTransaccion(tipoTransaccion);
		this.setValorTransaccion(valorTransaccion);
	}
	
	public Usuario getDueno() {
		return this.dueno;
	}
	
	public void transferirTiquete(Tiquete tiquete, Usuario dueno, Usuario usuarioDestino, String fechaActual) throws TiquetesVencidosTransferidos, 
	IDNoEncontrado, UsuarioPasswordIncorrecto {
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
				            
				            if (puedeTransferirse) {
				            	((IDuenoTiquetes) dueno).eliminarTiquete(tm);
				                ((IDuenoTiquetes) usuarioDestino).agregarTiquete(tm);
				                tm.setTransferido(true);
				                for (Tiquete t : tm.getTiquetes()) {
				                    t.setTransferido(true);
				                }
				                System.out.println("Paquete completo transferido con éxito.");
				            } else {
				                throw new TiquetesVencidosTransferidos("Tiquete(s) transferidos o vencidos");
				            }
				            
				        } else {
				        	System.out.print("Ingrese el ID del tiquete individual dentro del paquete: ");
				            String idTiquete = sc.nextLine();

				            boolean encontrado = false;
				            for (TiqueteSimple t : tm.getTiquetes()) {
				                if (t.getId().equals(idTiquete)) {
				                    encontrado = true;
				                    if (!t.esVigente(fechaActual) || t.isTransferido()) {
				                    	throw new TiquetesVencidosTransferidos("Tiquete(s) transferidos o vencidos");
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
				                throw new IDNoEncontrado("ID No encontrado");
				            }
				        }
					    
					    
					 System.out.print("Ingrese el id del tiquete a transferir: ");
				     String idTiquete = sc.nextLine();
				        for(Tiquete t: tiquetes) {
				        	if(idTiquete.equals(t.getId())) {
				        		((IDuenoTiquetes) dueno).eliminarTiquete(tiquete);
								((IDuenoTiquetes) usuarioDestino).agregarTiquete(tiquete);
								tiquete.setTransferido(true);
							} else {
								throw new IDNoEncontrado("ID No encontrado");
										
								}
									}
				            	
				           
							}
					    
						
			 } else {
				throw new UsuarioPasswordIncorrecto("Login o password incorrecto");
					}
				        }
				            	
				       }
		
		
		Registro registro = new Registro(dueno, tiquete, usuarioDestino);
	    Transaccion trans = new Transaccion("TRANSFERENCIA", tiquete, dueno, LocalDateTime.now(), registro, 0);

	    PersistenciaTransacciones persist = new PersistenciaTransacciones();
	    persist.agregar(trans);
		
		
					     
			}
	
	
	public ArrayList<Tiquete> comprarTiquete(Tiquete tiqueteComprar, Usuario comprador,int cantidad, Evento eventoAsociado) throws TiquetesNoDisponiblesException, TransferenciaNoPermitidaException, SaldoInsuficienteExeption {
		double precioTotal = 0;
		ArrayList<Tiquete> tiquetesComprados = new ArrayList<>();
		Collection<Tiquete> tiquetes = eventoAsociado.getTiquetesDisponibles();
		if (tiquetes.isEmpty()) {
	        throw new TiquetesNoDisponiblesException("No hay tiquetes disponibles.");
	        
	    }
		
			
		
		if (cantidad > NUMERO_MAX_TRANSACCION) {
			throw new TransferenciaNoPermitidaException("porque supera el número máximo de tiquetes por transacción.");
		    
		    }
		
		if (comprador instanceof IDuenoTiquetes) {
			double saldo = ((IDuenoTiquetes) comprador).getSaldo();
			

			if (tiqueteComprar instanceof TiqueteMultiple) {
			   precioTotal = tiqueteComprar.calcularPrecio();
			} else {
			    precioTotal = tiqueteComprar.calcularPrecio() * cantidad;
			    }
			
			if(precioTotal<= saldo ) {
				saldo -= precioTotal;
				((IDuenoTiquetes) comprador).actualizarSaldo(saldo);
			}else {
				throw new SaldoInsuficienteExeption("Saldo insuficiente");
				
				}
			
			if (tiqueteComprar instanceof TiqueteMultiple) {
				
				TiqueteMultiple tm = (TiqueteMultiple) tiqueteComprar;
				for (Tiquete t : tm.getTiquetes()) {
					((IDuenoTiquetes) comprador).agregarTiquete(t);
		            eventoAsociado.venderTiquete(t.getId());
		            tiquetesComprados.add(t);
		        }	
				} else {
					for (int i = 0; i < cantidad; i++) {
						((IDuenoTiquetes) comprador).agregarTiquete(tiqueteComprar);
			            eventoAsociado.venderTiquete(tiqueteComprar.getId());
			            tiquetesComprados.add(tiqueteComprar);
					}
			}
			
		}
		
	    Registro registro = new Registro(comprador, tiqueteComprar, null);
	    Transaccion trans = new Transaccion("COMPRA", tiquete, dueno, LocalDateTime.now(), registro, precioTotal);

	    PersistenciaTransacciones persist = new PersistenciaTransacciones();
	    persist.agregar(trans);
	
		return tiquetesComprados;
	}
	
	
	public ArrayList<Tiquete> comprarPaqueteDeluxe(PaqueteDeluxe paquete,
            Usuario comprador, int cantidad, Evento eventoAsociado ) throws TransferenciaNoPermitidaException, TiquetesNoDisponiblesException, SaldoInsuficienteExeption {
        ArrayList<Tiquete> tiquetesComprados = new ArrayList<>();

       
        if (paquete == null || comprador == null || eventoAsociado == null) {
        	throw new TransferenciaNoPermitidaException("porque los datos no son consistentes.");
        }
        if (cantidad <= 0) {
        	throw new TransferenciaNoPermitidaException("porque la cantidad a comprar debe ser mayor a 0.");
        }
        if (cantidad > NUMERO_MAX_TRANSACCION) {
        	throw new TransferenciaNoPermitidaException("porque supera el número máximo de tiquetes por transacción.");
            
        }

        if (cantidad != 1) {
        	throw new TransferenciaNoPermitidaException("porque Por ahora solo se soporta comprar 1 Paquete Deluxe por transacción con el paquete recibido.");
            
            
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
        	throw new TiquetesNoDisponiblesException("El paquete deluxe no tiene tiquetes.");
        }

        for (Tiquete t : tiqsPaquete) {
            Tiquete enEvento = eventoAsociado.getTiquetePorId(t.getId());
            if (enEvento == null) {
            	throw new TiquetesNoDisponiblesException("porque no hay tiquete disponibles en el evento: " + t.getId());
        
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
            	throw new SaldoInsuficienteExeption("Saldo insuficiente");
				
            }
            
            if (total > 0) {
                saldo -= total;
                dueno.actualizarSaldo(saldo);
            }
        } else {
        	throw new TransferenciaNoPermitidaException("ERROR: El comprador no puede poseer tiquetes.");
            
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

	
	public Usuario getReceptor(Usuario UsuarioReceptor) {
		return UsuarioReceptor;
	}

	
    
	

	public void solicitarReembolso(Tiquete tiqueteRembolso, String motivo) {
		PersistenciaUsuarios persistencia = new PersistenciaUsuarios();
		
		Administrador admin = persistencia.recuperarAdministrador();
		HashMap<Tiquete, String> solicitud = new HashMap<>();
		solicitud.put(tiqueteRembolso, motivo);
		
		admin.getSolicitudes().add(solicitud);
		
		
	}
	

	public void revenderTiquete(Tiquete tiqueteVenta, double precioOferta, Usuario vendedor) {
		marketPlaceReventas market = new marketPlaceReventas();
		
		market.crearOferta(tiqueteVenta, precioOferta , vendedor);
		
	}
	
	public ArrayList<Tiquete> comprarEnMarketplace(Tiquete tiquetePublicado, Usuario vendedor, Usuario comprador)
	        throws TiquetesNoDisponiblesException, TransferenciaNoPermitidaException, SaldoInsuficienteExeption {

	    if (tiquetePublicado == null) {
	        throw new TiquetesNoDisponiblesException("La oferta no está disponible.");
	    }
	    if (!(vendedor instanceof IDuenoTiquetes) || !(comprador instanceof IDuenoTiquetes)) {
	        throw new TransferenciaNoPermitidaException("Participantes inválidos para la transferencia.");
	    }
	    if (vendedor.getLogin() != null && comprador.getLogin() != null && vendedor.getLogin().equals(comprador.getLogin())) {
	        throw new TransferenciaNoPermitidaException("El comprador no puede ser el mismo que el vendedor.");
	    }

	    IDuenoTiquetes vend = (IDuenoTiquetes) vendedor;
	    IDuenoTiquetes comp = (IDuenoTiquetes) comprador;

	    List<HashMap<Tiquete, String>> ofertas = vend.getListaOfertas();
	    if (ofertas == null || ofertas.isEmpty()) {
	        throw new TiquetesNoDisponiblesException("El vendedor no tiene ofertas activas.");
	    }

	    HashMap<Tiquete, String> ofertaElegida = null;
	    String etiqueta = null;

	    for (HashMap<Tiquete, String> mapa : ofertas) {
	        for (var e : mapa.entrySet()) {
	            Tiquete t = e.getKey();
	            if (t != null && t.getId() != null && t.getId().equals(tiquetePublicado.getId())) {
	                ofertaElegida = mapa;
	                etiqueta = e.getValue();
	                break;
	            }
	        }
	        if (ofertaElegida != null) break;
	    }

	    if (ofertaElegida == null || etiqueta == null) {
	        throw new TiquetesNoDisponiblesException("No se encontró la oferta para el tiquete indicado.");
	    }

	    double precioOferta = marketPlaceReventas.extraerPrecio(etiqueta); // <<--- ahora parsea "login - Precio: $12345"
	    if (precioOferta <= 0) {
	        throw new TransferenciaNoPermitidaException("Precio de oferta inválido.");
	    }

	    double saldoComprador = comp.getSaldo();
	    if (precioOferta > saldoComprador) {
	        throw new SaldoInsuficienteExeption("Saldo insuficiente para comprar la oferta.");
	    }

	    comp.actualizarSaldo(saldoComprador - precioOferta);
	    vend.actualizarSaldo(vend.getSaldo() + precioOferta);

	    ArrayList<Tiquete> adquiridos = new ArrayList<>();
	    vend.eliminarTiquete(tiquetePublicado);
	    comp.agregarTiquete(tiquetePublicado);
	    if (!tiquetePublicado.isTransferido()) {
	        tiquetePublicado.setTransferido(true);
	    }
	    adquiridos.add(tiquetePublicado);

	    vend.eliminarOfertaListaPropia(tiquetePublicado);

	    Registro registro = new Registro(vendedor, tiquetePublicado, comprador);
	    Transaccion trans = new Transaccion(
	            "COMPRA_MARKETPLACE",
	            tiquetePublicado,
	            vendedor,
	            java.time.LocalDateTime.now(),
	            registro,
	            precioOferta
	    );

	    PersistenciaTransacciones pTrans = new PersistenciaTransacciones();
	    pTrans.agregar(trans);

	    return adquiridos;
	}
	
	
	

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public String getTipoTransaccion() {
		return tipoTransaccion;
	}

	public void setTipoTransaccion(String tipoTransaccion) {
		this.tipoTransaccion = tipoTransaccion;
	}

	public double getValorTransaccion() {
		return valorTransaccion;
	}

	public void setValorTransaccion(double valorTransaccion) {
		this.valorTransaccion = valorTransaccion;
	}

	public Registro getRegistro() {
		return registro;
	}

	public void setRegistro(Registro registro) {
		this.registro = registro;
	}
	
}

