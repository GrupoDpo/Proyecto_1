package Finanzas;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import Persistencia.SistemaPersistencia;
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
        ofertas = new LinkedList<HashMap<Tiquete,String>>();
        logEventos = new ArrayList<String>();
    }

    public Queue<HashMap<Tiquete,String>> getOfertas() { return ofertas; }
    public List<String> getLogEventos() { return logEventos; }

    // ============================================================
    //  CREAR OFERTA
    // ============================================================
    public void crearOferta(Tiquete tiquete, double precio, Usuario vendedor, SistemaPersistencia sistema) {

        if (!(vendedor instanceof IDuenoTiquetes)) {
            System.out.println("El usuario no puede crear ofertas.");
            return;
        }

        HashMap<Tiquete, String> mapaOferta = new HashMap<Tiquete, String>();
        mapaOferta.put(tiquete, vendedor.getLogin() + " - Precio: $" + precio);

        ofertas.add(mapaOferta);

        String fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String registro = "[" + fechaHora + "] Oferta creada por " + vendedor.getLogin()
                          + " para tiquete " + tiquete.getId()
                          + " con precio $" + precio;

        logEventos.add(registro);

        System.out.println("Oferta creada exitosamente: " + registro);

        // Guardar todo el sistema
        sistema.guardarTodo();
    }


    // ============================================================
    //   ELIMINAR OFERTA
    // ============================================================
    public void eliminarOferta(Tiquete tiquete, Usuario vendedor, SistemaPersistencia sistema)
            throws OfertaNoDIsponibleException {

        HashMap<Tiquete, String> ofertaAEliminar = null;

        for (HashMap<Tiquete, String> mapa : ofertas) {
            if (mapa.containsKey(tiquete)) {
                ofertaAEliminar = mapa;
                break;
            }
        }

        if (ofertaAEliminar == null) {
            throw new OfertaNoDIsponibleException("No se encontró ninguna oferta para ese tiquete.");
        }

        ofertas.remove(ofertaAEliminar);

        String fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String registro = "[" + fechaHora + "] Oferta eliminada por "
                          + vendedor.getLogin() + " para tiquete " + tiquete.getId();

        logEventos.add(registro);
        System.out.println("Oferta eliminada: " + registro);

        sistema.guardarTodo();
    }



    // ============================================================
    //      REALIZAR CONTRAOFERTA
    // ============================================================
    public void contraofertar(Usuario comprador, SistemaPersistencia sistema)
            throws TransferenciaNoPermitidaException {

        if (ofertas.isEmpty()) {
            System.out.println("No hay ofertas disponibles.");
            return;
        }

        Scanner sc = new Scanner(System.in);

        System.out.println("=== OFERTAS DISPONIBLES ===");
        List<HashMap<Tiquete,String>> temp = new ArrayList<HashMap<Tiquete,String>>();

        for (HashMap<Tiquete,String> m : ofertas) temp.add(m);

        int index = 1;
        for (HashMap<Tiquete, String> mapa : temp) {
            for (Map.Entry<Tiquete, String> e : mapa.entrySet()) {
                System.out.println(index + ". Tiquete ID: " + e.getKey().getId() + " | " + e.getValue());
                index++;
            }
        }

        System.out.print("Seleccione oferta: ");
        int pos = sc.nextInt();
        sc.nextLine();

        if (pos < 1 || pos > temp.size()) {
            System.out.println("Selección inválida.");
            return;
        }

        HashMap<Tiquete,String> seleccion = temp.get(pos - 1);
        Map.Entry<Tiquete,String> entry = seleccion.entrySet().iterator().next();

        Tiquete tiquete = entry.getKey();
        String info = entry.getValue();

        String loginVendedor = info.split(" - ")[0];
        Usuario vendedor = sistema.buscarUsuario(loginVendedor);

        if (vendedor == null) {
            System.out.println("Vendedor no encontrado.");
            return;
        }

        if (comprador.getLogin().equals(loginVendedor)) {
            throw new TransferenciaNoPermitidaException("No puedes contraofertarte a ti mismo.");
        }

        System.out.print("Ingrese nuevo precio: ");
        double nuevoPrecio = sc.nextDouble();
        sc.nextLine();

        if (nuevoPrecio <= 0) {
            System.out.println("Precio inválido.");
            return;
        }

        // Guardar la contraoferta
        IDuenoTiquetes vendedorD = (IDuenoTiquetes) vendedor;
        HashMap<Tiquete,String> c = new HashMap<Tiquete,String>();
        c.put(tiquete, comprador.getLogin() + " - Contraoferta: $" + nuevoPrecio);
        vendedorD.getListaOfertas().add(c);

        String fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String registro = "[" + fechaHora + "] " + comprador.getLogin()
                          + " hizo una contraoferta a " + vendedor.getLogin()
                          + " por el tiquete " + tiquete.getId()
                          + " con precio $" + nuevoPrecio;

        logEventos.add(registro);

        System.out.println("Contraoferta enviada: " + registro);

        sistema.guardarTodo();
    }



    // ============================================================
    //  VER CONTRAOFERTAS (para el dueño del tiquete)
    // ============================================================
    public void Vercontraofertar(Usuario comprador, SistemaPersistencia sistema)
            throws TransferenciaNoPermitidaException {

        if (!(comprador instanceof IDuenoTiquetes)) {
            System.out.println("No puedes ver contraofertas.");
            return;
        }

        IDuenoTiquetes dueno = (IDuenoTiquetes) comprador;

        if (dueno.getListaOfertas().isEmpty()) {
            System.out.println("No tienes contraofertas pendientes.");
            return;
        }

        Scanner sc = new Scanner(System.in);

        List<HashMap<Tiquete,String>> procesar = new ArrayList<HashMap<Tiquete,String>>();

        System.out.println("=== CONTRAOFERTAS RECIBIDAS ===");

        for (HashMap<Tiquete,String> mapa : dueno.getListaOfertas()) {
            for (Map.Entry<Tiquete,String> e : mapa.entrySet()) {

                Tiquete tiq = e.getKey();
                String info = e.getValue();

                System.out.println("Tiquete: " + tiq.getId());
                System.out.println("Info: " + info);
                System.out.println("[1] Aceptar  [2] Rechazar");
                System.out.print("Opción: ");

                int opcion = sc.nextInt();
                sc.nextLine();

                if (opcion == 1) {
                    double precio = extraerPrecio(info);
                    dueno.actualizarSaldo(precio);
                    procesar.add(mapa);

                    System.out.println("Contraoferta aceptada. +" + precio);

                } else if (opcion == 2) {
                    System.out.println("Contraoferta rechazada.");
                    procesar.add(mapa);
                }
            }
        }

        dueno.getListaOfertas().removeAll(procesar);
        sistema.guardarTodo();
    }


    // ============================================================
    //  EXTRAER PRECIO
    // ============================================================
    public static double extraerPrecio(String etiqueta) throws TransferenciaNoPermitidaException {

        if (etiqueta == null) throw new TransferenciaNoPermitidaException("Etiqueta inválida.");

        String clave = "Precio:";
        int pos = etiqueta.indexOf(clave);

        if (pos < 0) throw new TransferenciaNoPermitidaException("No se encontró 'Precio:'.");

        String sub = etiqueta.substring(pos + clave.length()).trim();
        if (sub.startsWith("$")) sub = sub.substring(1);

        try {
            return Double.parseDouble(sub);
        } catch (Exception e) {
            throw new TransferenciaNoPermitidaException("Precio inválido.");
        }
    }


    // ============================================================
    //  LOG
    // ============================================================
    public void verLogEventos(Usuario u) {

        if (!(u instanceof Administrador)) {
            System.out.println("Solo admin puede ver el log.");
            return;
        }

        if (logEventos.isEmpty()) {
            System.out.println("No hay eventos registrados.");
            return;
        }

        System.out.println("===== LOG MARKETPLACE =====");
        int i = 1;
        for (String r : logEventos) {
            System.out.println(i + ". " + r);
            i++;
        }
    }
}
