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
		 
		 if (vendedor instanceof IDuenoTiquetes) {
             IDuenoTiquetes duenoTiquete = (IDuenoTiquetes) vendedor;
             mapaOferta.put(tiquete, duenoTiquete + " - Precio: $" + precio);
             ofertas.add(mapaOferta);
             
             
		 }
		 String fechaHora = LocalDateTime.now()
	                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	        String registro = "[" + fechaHora + "] Oferta creada por " + vendedor 
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
	
	public void Vercontraofertar(Usuario comprador) {
		try (Scanner sc = new Scanner(System.in)) {
		List<HashMap<Tiquete, String>> procesados = new ArrayList<>();
		if (comprador instanceof IDuenoTiquetes) {
			
            IDuenoTiquetes compradorTiquete = (IDuenoTiquetes) comprador;
            if (compradorTiquete.getOfertas().isEmpty()) {
                System.out.println("No hay contraofertas pendientes.");
            
		for (HashMap<Tiquete, String> mapa : compradorTiquete.getOfertas() ) {
		    for (Entry<Tiquete, String> entry : mapa.entrySet()) {
		        Tiquete tiqueteOferta = entry.getKey();
		        String info = entry.getValue();

		        System.out.println("--- contra oferta ---");
		        System.out.println("Tiquete: " + tiqueteOferta.getId());
		        System.out.println("Info oferta: " + info);
		        
		        double precioPropuesto = 0;

		        if (info.contains("Precio: $")) {
		            String[] partes = info.split("Precio: \\$");
		            if (partes.length > 1) {
		                precioPropuesto = Double.parseDouble(partes[1].trim());
		            }
		        }
		        System.out.println("[1] Aceptar");
		        System.out.println("[2] Rechazar");
		        System.out.print("Seleccione una opción: ");

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

		
            compradorTiquete.getOfertas().removeAll(procesados);
		}
}

		
		
		
	}
	
	
	
	
	
	

}
