package Finanzas;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import Persistencia.PersistenciaUsuarios;

import java.util.Map.Entry;

import excepciones.OfertaNoDIsponibleException;
import excepciones.TransferenciaNoPermitidaException;
import tiquete.Tiquete;
import usuario.Administrador;
import usuario.IDuenoTiquetes;
import usuario.Usuario;

public class marketPlaceReventas {
	private Queue<HashMap<Tiquete, String>> ofertas;
	private List<String> logEventos;
	public marketPlaceReventas() {
		 ofertas = new LinkedList<>();
		 logEventos = new ArrayList<>();
		
	}
	
	public void crearOferta(Tiquete tiquete,double precio, Usuario vendedor) {
		
		 HashMap<Tiquete, String> mapaOferta = new HashMap<>();
		 
		 String loginDueno = vendedor.getLogin();
		 
		 if (vendedor instanceof IDuenoTiquetes) {
             mapaOferta.put(tiquete, loginDueno + " - Precio: $" + precio);
             ofertas.add(mapaOferta);
             
             
		 }
		 
		 String fechaHora = LocalDateTime.now()
	                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	        String registro = "[" + fechaHora + "] Oferta creada por " + loginDueno 
	                        + " para tiquete " + tiquete.getId() 
	                        + " con precio $" + precio;
	        logEventos.add(registro);

	        System.out.println("Oferta creada exitosamente: " + registro);

	}
	
	public void eliminarOferta(Tiquete tiquete, Usuario vendedor) throws OfertaNoDIsponibleException {
		HashMap<Tiquete, String> ofertaAEliminar = null;

	    for (HashMap<Tiquete, String> mapa : ofertas) {
	        if (mapa.containsKey(tiquete)) {
	            ofertaAEliminar = mapa;
	            break;
	        }
	    }

	    if (ofertaAEliminar != null) {
	        ofertas.remove(ofertaAEliminar);

	        String fechaHora = LocalDateTime.now()
	                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	        String registro = "[" + fechaHora + "] Oferta eliminada por " + vendedor 
	                        + " para tiquete " + tiquete.getId();
	        logEventos.add(registro);

	        System.out.println("Oferta eliminada exitosamente.");
	        
	    } else {
	        throw new OfertaNoDIsponibleException("No se encontró ninguna oferta para ese tiquete.");
	        
	    }
		
		
	}
	
	public void Vercontraofertar(Usuario comprador) throws TransferenciaNoPermitidaException {
		try (Scanner sc = new Scanner(System.in)) {
		List<HashMap<Tiquete, String>> procesados = new ArrayList<>();
		if (comprador instanceof IDuenoTiquetes) {
			
            IDuenoTiquetes compradorTiquete = (IDuenoTiquetes) comprador;
            if (compradorTiquete.getListaOfertas().isEmpty()) {
                System.out.println("No hay contraofertas pendientes.");
            
		for (HashMap<Tiquete, String> mapa : compradorTiquete.getListaOfertas() ) {
		    for (Entry<Tiquete, String> entry : mapa.entrySet()) {
		        Tiquete tiqueteOferta = entry.getKey();
		        String info = entry.getValue();

		        System.out.println("--- contra oferta ---");
		        System.out.println("Tiquete: " + tiqueteOferta.getId());
		        System.out.println("Info oferta: " + info);
		        
		        double precioPropuesto = extraerPrecio(info);

		        

		        int opcion = sc.nextInt(); 
		        sc.nextLine(); 

		        if (opcion == 1) {

		        	compradorTiquete.actualizarSaldo(precioPropuesto);
		        	compradorTiquete.eliminarOfertaListaPropia(tiqueteOferta);
		                System.out.println("contraOferta aceptada. precioAcordado: $" + precioPropuesto);
		            
		        } else if (opcion == 2) {
		            System.out.println("contraoferta rechazada.");
		        }

		        procesados.add(mapa);
		    }
		
		}
		}

		
            compradorTiquete.getListaOfertas().removeAll(procesados);
		}
}

		
		
		
	}
	
	public void contraofertar(Usuario comprador) throws TransferenciaNoPermitidaException {
	    PersistenciaUsuarios persistencia = new PersistenciaUsuarios();
	    try (Scanner sc = new Scanner(System.in)) {
			if (ofertas.isEmpty()) {
			    System.out.println("No hay ofertas disponibles actualmente.");
			    return;
			}

			System.out.println("=== OFERTAS DISPONIBLES ===");
			int index = 1;
			List<HashMap<Tiquete, String>> listaOfertasTemp = new ArrayList<>(ofertas);

			for (HashMap<Tiquete, String> mapa : listaOfertasTemp) {
			    for (Entry<Tiquete, String> entry : mapa.entrySet()) {
			        Tiquete t = entry.getKey();
			        String info = entry.getValue();
			        System.out.println(index + ". Tiquete ID: " + t.getId() + " | " + info);
			        index++;
			    }
			}

			System.out.print("Ingrese el número de la oferta a contraofertar: ");
			int seleccion = sc.nextInt();
			sc.nextLine();

			if (seleccion < 1 || seleccion > listaOfertasTemp.size()) {
			    System.out.println("Selección inválida.");
			    return;
			}

			HashMap<Tiquete, String> ofertaSeleccionada = listaOfertasTemp.get(seleccion - 1);
			Entry<Tiquete, String> entrySeleccionada = ofertaSeleccionada.entrySet().iterator().next();
			Tiquete tiquete = entrySeleccionada.getKey();
			String info = entrySeleccionada.getValue();

			String loginVendedor = info.split(" - ")[0];
			Usuario vendedor = persistencia.buscarUsuario(loginVendedor);

			if (vendedor == null) {
			    System.out.println("No se encontró el vendedor en el sistema.");
			    return;
			}

			if (comprador.getLogin().equals(loginVendedor)) {
			    throw new TransferenciaNoPermitidaException("No puedes contraofertar tu propia oferta.");
			}

			System.out.print("Ingrese su precio propuesto: ");
			double nuevoPrecio = sc.nextDouble();
			sc.nextLine();

			if (nuevoPrecio <= 0) {
			    System.out.println("El precio debe ser mayor que 0.");
			    return;
			}

			if (!(vendedor instanceof IDuenoTiquetes)) {
			    throw new TransferenciaNoPermitidaException("El vendedor no es un dueño de tiquetes válido.");
			}

			IDuenoTiquetes vendedorTiquete = (IDuenoTiquetes) vendedor;

			HashMap<Tiquete, String> contraoferta = new HashMap<>();
			contraoferta.put(tiquete, comprador.getLogin() + " - Contraoferta: $" + nuevoPrecio);
			vendedorTiquete.getListaOfertas().add(contraoferta);

			
			persistencia.agregar((Usuario) vendedorTiquete);

			
			String fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			String registro = "[" + fechaHora + "] " + comprador.getLogin() +
			                  " hizo una contraoferta a " + loginVendedor +
			                  " por el tiquete " + tiquete.getId() +
			                  " con precio $" + nuevoPrecio;
			logEventos.add(registro);

			System.out.println(" Contraoferta enviada exitosamente: " + registro);
		}
	}

	
	
	
	
	
	
	
	public static double extraerPrecio(String etiqueta) throws TransferenciaNoPermitidaException {
	    if (etiqueta == null) throw new TransferenciaNoPermitidaException("Etiqueta de oferta inválida.");

	    String clave = "Precio:";
	    int i = etiqueta.indexOf(clave);
	    if (i < 0) throw new TransferenciaNoPermitidaException("No se encontró 'Precio:' en la oferta.");

	    String tail = etiqueta.substring(i + clave.length()).trim(); 
	    if (tail.startsWith("$")) tail = tail.substring(1).trim();

	    tail = tail.replace(',', '.');   
	    
	    StringBuilder sb = new StringBuilder();
	    for (int k = 0; k < tail.length(); k++) {
	        char c = tail.charAt(k);
	        if ((c >= '0' && c <= '9') || c == '.') sb.append(c);
	        else if (sb.length() > 0) break;  
	    }
	    String num = sb.toString();
	    if (num.isEmpty()) throw new TransferenciaNoPermitidaException("Precio no presente en la oferta.");

	    int lastDot = num.lastIndexOf('.');
	    if (lastDot > 0) {
	        String enteros = num.substring(0, lastDot).replace(".", "");
	        String dec = num.substring(lastDot + 1);
	        num = enteros + (dec.isEmpty() ? "" : "." + dec);
	    }

	    try {
	        return Double.parseDouble(num);
	    } catch (NumberFormatException e) {
	        throw new TransferenciaNoPermitidaException("Formato de precio de oferta inválido.");
	    }
	}
	
	
	
	public void verLogEventos(Usuario usuario) {
	    if (!(usuario instanceof Administrador)) {
	        System.out.println(" Acceso denegado: solo el administrador puede consultar el registro de eventos.");
	        return;
	    }

	    System.out.println("===== LOG DE EVENTOS DEL MARKETPLACE =====");

	    if (logEventos.isEmpty()) {
	        System.out.println("No hay eventos registrados en el sistema.");
	        return;
	    }

	    int contador = 1;
	    for (String registro : logEventos) {
	        System.out.println(contador + ". " + registro);
	        contador++;
	    }

	}
	
	
	

}
