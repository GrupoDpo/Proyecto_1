
package interfaz;

import java.awt.*;
import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import Persistencia.SistemaPersistencia;
import tiquete.Tiquete;
import tiquete.TiqueteMultiple;
import usuario.IDuenoTiquetes;
import usuario.Usuario;
import usuario.Promotor;
import usuario.Organizador;
import usuario.Cliente;

public class ventanaCancelarOferta extends JFrame {
    private static final long serialVersionUID = 1L;

    // Componentes UI
    private JList<String> listaOfertas;
    private DefaultListModel<String> modeloLista;
    private JTextArea areaDetalle;
    private JButton btnCancelar;
    private JButton btnVolver;

    // Modelo
    private IDuenoTiquetes usuario;
    private SistemaPersistencia sistema;
    private HashMap<Integer, HashMap<Tiquete, String>> mapaOfertas;
    private HashMap<Tiquete, String> ofertaSeleccionada;

    public ventanaCancelarOferta(IDuenoTiquetes usuario, SistemaPersistencia sistema) {
        this.usuario = usuario;
        this.sistema = sistema;
        this.mapaOfertas = new HashMap<>();

        initComponents();
        cargarOfertas();
    }

    private void initComponents() {
        setTitle("BOLETAMASTER: Cancelar Oferta");
        setLayout(null);
        setSize(650, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Título
        JLabel lblTitulo = new JLabel("CANCELAR OFERTA DEL MARKETPLACE");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setBounds(20, 10, 400, 25);
        add(lblTitulo);

        // Instrucción
        JLabel lblInstruccion = new JLabel("Selecciona la oferta que deseas cancelar:");
        lblInstruccion.setBounds(20, 45, 400, 20);
        add(lblInstruccion);

        // Lista de ofertas
        modeloLista = new DefaultListModel<>();
        listaOfertas = new JList<>(modeloLista);
        listaOfertas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaOfertas.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarDetalleOferta();
            }
        });

        JScrollPane scrollOfertas = new JScrollPane(listaOfertas);
        scrollOfertas.setBounds(20, 70, 600, 150);
        add(scrollOfertas);

        // Área de detalle
        JLabel lblDetalle = new JLabel("Detalle de la oferta:");
        lblDetalle.setBounds(20, 230, 200, 20);
        add(lblDetalle);

        areaDetalle = new JTextArea();
        areaDetalle.setEditable(false);
        areaDetalle.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaDetalle.setBackground(new Color(240, 240, 240));
        
        JScrollPane scrollDetalle = new JScrollPane(areaDetalle);
        scrollDetalle.setBounds(20, 255, 600, 120);
        add(scrollDetalle);

        // Botones
        btnVolver = new JButton("Volver");
        btnVolver.setBounds(20, 400, 120, 35);
        btnVolver.addActionListener(e -> volverAlMenu());
        add(btnVolver);

        btnCancelar = new JButton("Cancelar Oferta");
        btnCancelar.setBounds(480, 400, 140, 35);
        btnCancelar.setEnabled(false);
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 12));
        btnCancelar.addActionListener(e -> cancelarOferta());
        add(btnCancelar);

        setVisible(true);
    }

    private void cargarOfertas() {
        modeloLista.clear();
        mapaOfertas.clear();

        Queue<HashMap<Tiquete, String>> todasLasOfertas = sistema.getMarketplace().getOfertas();

        if (todasLasOfertas == null || todasLasOfertas.isEmpty()) {
            modeloLista.addElement("No hay ofertas en el marketplace");
            areaDetalle.setText("El marketplace está vacío.");
            return;
        }

        // Filtrar solo las ofertas del usuario actual
        int num = 0;
        for (HashMap<Tiquete, String> mapa : todasLasOfertas) {
            for (Map.Entry<Tiquete, String> entry : mapa.entrySet()) {
                Tiquete tiquete = entry.getKey();
                String info = entry.getValue();

                // Verificar si el tiquete pertenece al usuario
                boolean esDelUsuario = false;
                for (Tiquete t : usuario.getTiquetes()) {
                    if (t.getId().equals(tiquete.getId())) {
                        esDelUsuario = true;
                        break;
                    }
                }

                if (esDelUsuario) {
                    // Extraer el precio de la info (formato: "precio;otrosDatos")
                    String precioStr = info.contains(";") ? info.split(";")[0] : info;
                    
                    String tipo = tiquete instanceof TiqueteMultiple ? "PAQUETE" : "SIMPLE";
                    String evento = tiquete.getEvento() != null ? 
                                    tiquete.getEvento().getNombre() : "Sin evento";
                    
                    String linea = String.format("%d. [%s] %s | ID: %s | Evento: %s | Precio: $%s", 
                        num + 1, tipo, tiquete.getNombre(), tiquete.getId(), evento, precioStr);

                    modeloLista.addElement(linea);
                    mapaOfertas.put(num, mapa);
                    num++;
                }
            }
        }

        if (modeloLista.isEmpty()) {
            modeloLista.addElement("No tienes ofertas activas en el marketplace");
            areaDetalle.setText("No tienes ofertas publicadas actualmente.");
        }
    }

    private void mostrarDetalleOferta() {
        int index = listaOfertas.getSelectedIndex();
        
        if (index < 0 || !mapaOfertas.containsKey(index)) {
            areaDetalle.setText("");
            btnCancelar.setEnabled(false);
            ofertaSeleccionada = null;
            return;
        }

        ofertaSeleccionada = mapaOfertas.get(index);
        
        // Obtener el tiquete y la info
        Map.Entry<Tiquete, String> entry = ofertaSeleccionada.entrySet().iterator().next();
        Tiquete tiquete = entry.getKey();
        String info = entry.getValue();
        
        String precioStr = info.contains(";") ? info.split(";")[0] : info;
        
        StringBuilder detalle = new StringBuilder();
        detalle.append("=== DETALLE DE LA OFERTA ===\n");
        detalle.append("Tipo: ").append(tiquete instanceof TiqueteMultiple ? 
                                        "Paquete Múltiple" : "Tiquete Simple").append("\n");
        detalle.append("ID: ").append(tiquete.getId()).append("\n");
        detalle.append("Nombre: ").append(tiquete.getNombre()).append("\n");

        if (tiquete.getEvento() != null) {
            detalle.append("Evento: ").append(tiquete.getEvento().getNombre()).append("\n");
            detalle.append("Fecha: ").append(tiquete.getEvento().getFecha()).append("\n");
        }

        detalle.append("Precio de reventa: $").append(precioStr).append("\n");
        detalle.append("Precio original: $").append(String.format("%.2f", 
            tiquete.getPrecioBaseSinCalcular())).append("\n");

        if (tiquete instanceof TiqueteMultiple) {
            TiqueteMultiple tm = (TiqueteMultiple) tiquete;
            detalle.append("Incluye ").append(tm.getTiquetes().size()).append(" tiquetes\n");
        }

        detalle.append("\n️  Al cancelar, la oferta se eliminará del marketplace");
        detalle.append("\n===========================");

        areaDetalle.setText(detalle.toString());
        btnCancelar.setEnabled(true);
    }

    private void cancelarOferta() {
        // Validar que hay una oferta seleccionada
        if (ofertaSeleccionada == null) {
            JOptionPane.showMessageDialog(this,
                    "Debes seleccionar una oferta.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener datos de la oferta
        Map.Entry<Tiquete, String> entry = ofertaSeleccionada.entrySet().iterator().next();
        Tiquete tiquete = entry.getKey();
        String info = entry.getValue();
        String precioStr = info.contains(";") ? info.split(";")[0] : info;

        // Confirmación
        String mensaje = String.format(
            "¿Confirmar cancelación de oferta?\n\n" +
            "Tiquete: %s\n" +
            "ID: %s\n" +
            "Precio de reventa: $%s\n\n" +
            "La oferta será eliminada del marketplace.\n" +
            "El tiquete permanecerá contigo.",
            tiquete.getNombre(),
            tiquete.getId(),
            precioStr
        );

        int confirmacion = JOptionPane.showConfirmDialog(this,
                mensaje,
                "Confirmar Cancelación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        // Cancelar oferta (eliminar del marketplace)
        try {
            Queue<HashMap<Tiquete, String>> ofertas = sistema.getMarketplace().getOfertas();
            ofertas.remove(ofertaSeleccionada);

            // Registrar cancelación en el log (para el administrador)
            String logMensaje = String.format(
                "[%s] Usuario '%s' CANCELÓ oferta | Tiquete: %s | ID: %s | Precio cancelado: $%s | Evento: %s",
                java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                ((Usuario) usuario).getLogin(),
                tiquete.getNombre(),
                tiquete.getId(),
                precioStr,
                tiquete.getEvento() != null ? tiquete.getEvento().getNombre() : "Sin evento"
            );
            sistema.getMarketplace().getLogEventos().add(logMensaje);

            // Guardar persistencia
            sistema.guardarTodo();

            JOptionPane.showMessageDialog(this,
                    "✓ OFERTA CANCELADA EXITOSAMENTE\n\n" +
                    "La oferta ha sido eliminada del marketplace.\n" +
                    "El tiquete permanece en tu poder.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

            // Recargar lista
            cargarOfertas();
            areaDetalle.setText("");
            ofertaSeleccionada = null;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cancelar la oferta:\n" + e.getMessage(),
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