package interfaz;

import java.awt.*;
import javax.swing.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Queue;

import Finanzas.Transaccion;
import Persistencia.SistemaPersistencia;
import tiquete.Tiquete;
import tiquete.TiqueteMultiple;
import usuario.IDuenoTiquetes;
import usuario.Usuario;
import usuario.Promotor;
import usuario.Organizador;
import usuario.Cliente;

public class ventanaCrearOferta extends JFrame {
    private static final long serialVersionUID = 1L;

    // Componentes UI
    private JList<String> listaTiquetes;
    private DefaultListModel<String> modeloLista;
    private JTextArea areaDetalle;
    private JTextField txtPrecio;
    private JButton btnPublicar;
    private JButton btnCancelar;

    // Modelo
    private IDuenoTiquetes usuario;
    private SistemaPersistencia sistema;
    private Transaccion trans;
    private HashMap<Integer, Tiquete> mapaTiquetes;
    private Tiquete tiqueteSeleccionado;

    public ventanaCrearOferta(IDuenoTiquetes usuario, SistemaPersistencia sistema, Transaccion trans) {
        this.usuario = usuario;
        this.sistema = sistema;
        this.trans = trans;
        this.mapaTiquetes = new HashMap<>();

        initComponents();
        cargarTiquetes();
    }

    private void initComponents() {
        setTitle("BOLETAMASTER: Crear Oferta en Marketplace");
        getContentPane().setLayout(null);
        setSize(650, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Título
        JLabel lblTitulo = new JLabel("CREAR OFERTA EN MARKETPLACE");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setBounds(20, 10, 400, 25);
        getContentPane().add(lblTitulo);

        // Instrucción
        JLabel lblInstruccion = new JLabel("Selecciona el tiquete que deseas revender:");
        lblInstruccion.setBounds(20, 45, 400, 20);
        getContentPane().add(lblInstruccion);

        // Lista de tiquetes
        modeloLista = new DefaultListModel<>();
        listaTiquetes = new JList<>(modeloLista);
        listaTiquetes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaTiquetes.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarDetalleTiquete();
            }
        });

        JScrollPane scrollTiquetes = new JScrollPane(listaTiquetes);
        scrollTiquetes.setBounds(20, 70, 600, 150);
        getContentPane().add(scrollTiquetes);

        // Área de detalle
        JLabel lblDetalle = new JLabel("Detalle del tiquete:");
        lblDetalle.setBounds(20, 230, 200, 20);
        getContentPane().add(lblDetalle);

        areaDetalle = new JTextArea();
        areaDetalle.setEditable(false);
        areaDetalle.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaDetalle.setBackground(new Color(240, 240, 240));
        
        JScrollPane scrollDetalle = new JScrollPane(areaDetalle);
        scrollDetalle.setBounds(20, 255, 600, 100);
        getContentPane().add(scrollDetalle);

        // Precio de oferta
        JLabel lblPrecio = new JLabel("Precio de reventa:");
        lblPrecio.setBounds(20, 370, 150, 25);
        getContentPane().add(lblPrecio);

        txtPrecio = new JTextField();
        txtPrecio.setBounds(150, 370, 120, 25);
        getContentPane().add(txtPrecio);

        JLabel lblDolar = new JLabel("$");
        lblDolar.setFont(new Font("Arial", Font.BOLD, 14));
        lblDolar.setBounds(135, 370, 20, 25);
        getContentPane().add(lblDolar);

        // Botones
        btnCancelar = new JButton("Salir");
        btnCancelar.setBounds(20, 420, 120, 35);
        btnCancelar.addActionListener(e -> volverAlMenu());
        getContentPane().add(btnCancelar);

        btnPublicar = new JButton("Publicar Oferta");
        btnPublicar.setBounds(480, 420, 140, 35);
        btnPublicar.setEnabled(false);
        btnPublicar.setBackground(new Color(46, 204, 113));
        btnPublicar.setForeground(Color.WHITE);
        btnPublicar.setFont(new Font("Arial", Font.BOLD, 12));
        btnPublicar.addActionListener(e -> publicarOferta());
        getContentPane().add(btnPublicar);

        setVisible(true);
    }

    private void cargarTiquetes() {
        modeloLista.clear();
        mapaTiquetes.clear();

        Collection<Tiquete> misTiquetes = usuario.getTiquetes();

        if (misTiquetes == null || misTiquetes.isEmpty()) {
            modeloLista.addElement("No tienes tiquetes para vender");
            areaDetalle.setText("No hay tiquetes disponibles para crear ofertas.");
            return;
        }

        // Obtener ofertas existentes para filtrar
        Queue<HashMap<Tiquete, String>> ofertasExistentes = sistema.getMarketplace().getOfertas();

        int num = 0;
        for (Tiquete t : misTiquetes) {
            // Verificar si ya está en oferta
            boolean yaEnOferta = false;
            for (HashMap<Tiquete, String> mapa : ofertasExistentes) {
                if (mapa.containsKey(t)) {
                    yaEnOferta = true;
                    break;
                }
            }

            // Solo mostrar tiquetes que NO estén en oferta
            if (!yaEnOferta) {
                String tipo = t instanceof TiqueteMultiple ? "PAQUETE" : "SIMPLE";
                String evento = t.getEvento() != null ? t.getEvento().getNombre() : "Sin evento";
                
                String linea = String.format("%d. [%s] %s | ID: %s | Evento: %s", 
                    num + 1, tipo, t.getNombre(), t.getId(), evento);

                modeloLista.addElement(linea);
                mapaTiquetes.put(num, t);
                num++;
            }
        }

        if (modeloLista.isEmpty()) {
            modeloLista.addElement("Todos tus tiquetes ya están en oferta");
            areaDetalle.setText("No tienes tiquetes disponibles para crear nuevas ofertas.");
        }
    }

    private void mostrarDetalleTiquete() {
        int index = listaTiquetes.getSelectedIndex();
        
        if (index < 0 || !mapaTiquetes.containsKey(index)) {
            areaDetalle.setText("");
            btnPublicar.setEnabled(false);
            tiqueteSeleccionado = null;
            return;
        }

        tiqueteSeleccionado = mapaTiquetes.get(index);
        
        StringBuilder detalle = new StringBuilder();
        detalle.append("=== DETALLE DEL TIQUETE ===\n");
        detalle.append("Tipo: ").append(tiqueteSeleccionado instanceof TiqueteMultiple ? 
                                        "Paquete Múltiple" : "Tiquete Simple").append("\n");
        detalle.append("ID: ").append(tiqueteSeleccionado.getId()).append("\n");
        detalle.append("Nombre: ").append(tiqueteSeleccionado.getNombre()).append("\n");

        if (tiqueteSeleccionado.getEvento() != null) {
            detalle.append("Evento: ").append(tiqueteSeleccionado.getEvento().getNombre()).append("\n");
            detalle.append("Fecha: ").append(tiqueteSeleccionado.getEvento().getFecha()).append("\n");
        }

        detalle.append("Precio original: $").append(String.format("%.2f", 
            tiqueteSeleccionado.getPrecioBaseSinCalcular())).append("\n");

        if (tiqueteSeleccionado instanceof TiqueteMultiple) {
            TiqueteMultiple tm = (TiqueteMultiple) tiqueteSeleccionado;
            detalle.append("Incluye ").append(tm.getTiquetes().size()).append(" tiquetes\n");
        }

        detalle.append("===========================");

        areaDetalle.setText(detalle.toString());
        btnPublicar.setEnabled(true);
        
        // Sugerir precio (precio original)
        txtPrecio.setText(String.format("%.2f", tiqueteSeleccionado.getPrecioBaseSinCalcular()));
    }

    private void publicarOferta() {
        // Validar que hay un tiquete seleccionado
        if (tiqueteSeleccionado == null) {
            JOptionPane.showMessageDialog(this,
                    "Debes seleccionar un tiquete.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar precio
        String precioStr = txtPrecio.getText().trim();
        if (precioStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debes ingresar un precio.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        double precio;
        try {
            precio = Double.parseDouble(precioStr);
            if (precio <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "El precio debe ser un número positivo válido.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verificar que el tiquete está en la lista del usuario
        Collection<Tiquete> listaTiquetes = usuario.getTiquetes();
        if (!listaTiquetes.contains(tiqueteSeleccionado)) {
            JOptionPane.showMessageDialog(this,
                    "Este tiquete no está en tu lista de tiquetes.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verificar que no esté ya en oferta (doble verificación)
        Queue<HashMap<Tiquete, String>> ofertas = sistema.getMarketplace().getOfertas();
        for (HashMap<Tiquete, String> mapa : ofertas) {
            if (mapa.containsKey(tiqueteSeleccionado)) {
                JOptionPane.showMessageDialog(this,
                        "Este tiquete ya fue agregado al marketplace.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Confirmación
        String mensaje = String.format(
            "¿Confirmar publicación de oferta?\n\n" +
            "Tiquete: %s\n" +
            "ID: %s\n" +
            "Precio de reventa: $%.2f\n\n" +
            "El tiquete quedará disponible en el marketplace.",
            tiqueteSeleccionado.getNombre(),
            tiqueteSeleccionado.getId(),
            precio
        );

        int confirmacion = JOptionPane.showConfirmDialog(this,
                mensaje,
                "Confirmar Oferta",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        // Publicar oferta
        try {
            trans.revenderTiquete(
                tiqueteSeleccionado,
                precio,
                (Usuario) usuario,
                sistema
            );

            // Registrar en el log del marketplace para el administrador
            String logMensaje = String.format(
                "[%s] Usuario '%s' publicó oferta | Tiquete: %s | ID: %s | Precio: $%.2f | Evento: %s",
                java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                ((Usuario) usuario).getLogin(),
                tiqueteSeleccionado.getNombre(),
                tiqueteSeleccionado.getId(),
                precio,
                tiqueteSeleccionado.getEvento() != null ? tiqueteSeleccionado.getEvento().getNombre() : "Sin evento"
            );
            sistema.getMarketplace().getLogEventos().add(logMensaje);

            // Guardar persistencia
            sistema.guardarTodo();

            JOptionPane.showMessageDialog(this,
                    "✓ OFERTA PUBLICADA EXITOSAMENTE\n\n" +
                    "Tu tiquete ahora está disponible en el marketplace.\n" +
                    "Precio de venta: $" + String.format("%.2f", precio),
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

            // Recargar lista
            cargarTiquetes();
            txtPrecio.setText("");
            areaDetalle.setText("");
            tiqueteSeleccionado = null;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al publicar la oferta:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Vuelve al menú principal según el tipo de usuario
     */
    private void volverAlMenu() {
        dispose();

        if (usuario instanceof Promotor) {
            new ventanaMenuPromotor((Promotor) usuario, sistema).setVisible(true);
        } else if (usuario instanceof Organizador) {
            new ventanaMenuOrganizador((Organizador) usuario, sistema).setVisible(true);
        } else if (usuario instanceof Cliente) {
            new ventanaMenuComprador((Cliente) usuario, sistema).setVisible(true);
        }
    }
}