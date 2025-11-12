package Finanzas;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Map.Entry;

import excepciones.OfertaNoDIsponibleException;
import excepciones.TransferenciaNoPermitidaException;
import tiquete.Tiquete;
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
		        	compradorTiquete.eliminarOferta(tiqueteOferta);
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
	
	public void contraofertar() {
		
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
	
	
	
	
	

}
