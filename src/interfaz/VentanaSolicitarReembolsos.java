package interfaz;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Evento.Evento;
import Finanzas.Transaccion;
import Persistencia.SistemaPersistencia;
import tiquete.Tiquete;
import usuario.Cliente;
import usuario.IDuenoTiquetes;
import usuario.Organizador;
import usuario.Promotor;
import usuario.Usuario;

public class VentanaSolicitarReembolsos extends JFrame {

	private static final long serialVersionUID = 1L;
	private JList<String> listaTiquetes;
    private DefaultListModel<String> modeloTiquetes;
    private JTextArea txtDetalleTiquete;
    private JButton btnSolicitarReembolso;
    private JButton btnSalir;
    private JTextArea txtMotivo;

    // Modelo
    private SistemaPersistencia sistema;
    private Usuario user;
    private Map<Tiquete, String> mapaSol;
    
    public VentanaSolicitarReembolsos(SistemaPersistencia sistema, Usuario user) {
    	this.sistema = sistema;
    	this.user = user;
    	this.mapaSol = new HashMap<>();
    	
    	inicializarComponentes();
    	cargarTiquetes();
    	
    }
    
    private void inicializarComponentes() {
    	setTitle("BOLETAMASTER: Solicitar reembolso");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);  // positions manuales
        
     // ============================
        // BARRA SUPERIOR
        // ============================
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(52, 152, 219));
        panelSuperior.setBounds(0, 0, 720, 50);
        panelSuperior.setLayout(null);
        add(panelSuperior);

        JLabel lblBienvenida = new JLabel(" HACER SOLICITUD");
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 16));
        lblBienvenida.setForeground(Color.WHITE);
        lblBienvenida.setBounds(20, 15, 300, 20);
        panelSuperior.add(lblBienvenida);
        
     // ============================
        // TIQUETES DISPONIBLES
        // ============================
        JLabel lblTiquetes = new JLabel("Tiquetes Disponibles para reembolsar:");
        lblTiquetes.setFont(new Font("Arial", Font.BOLD, 13));
        lblTiquetes.setBounds(30, 65, 350, 20);
        add(lblTiquetes);
        
     // Lista scrolleable de Tiquetes
        modeloTiquetes = new DefaultListModel<>();
        listaTiquetes = new JList<>(modeloTiquetes);
        listaTiquetes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaTiquetes.setFont(new Font("Monospaced", Font.PLAIN, 11));

        JScrollPane scrollTiquetes = new JScrollPane(listaTiquetes);
        scrollTiquetes.setBounds(30, 95, 660, 180);
        add(scrollTiquetes);
        
        JLabel lblDetalle = new JLabel("Detalle de la Oferta:");
        lblDetalle.setFont(new Font("Arial", Font.BOLD, 13));
        lblDetalle.setBounds(30, 290, 200, 20);
        add(lblDetalle);

        txtDetalleTiquete = new JTextArea();
        txtDetalleTiquete.setEditable(false);
        txtDetalleTiquete.setFont(new Font("Arial", Font.PLAIN, 12));
        txtDetalleTiquete.setLineWrap(true);
        txtDetalleTiquete.setWrapStyleWord(true);
        txtDetalleTiquete.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        txtDetalleTiquete.setBackground(new Color(245, 245, 245));

        JScrollPane scrollDetalle = new JScrollPane(txtDetalleTiquete);
        scrollDetalle.setBounds(30, 315, 660, 120);
        add(scrollDetalle);

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(null);
        panelBotones.setBounds(30, 555, 660, 60);
        add(panelBotones);
        
	     // ============================
	     // MOTIVO DEL REEMBOLSO
	     // ============================
	     JLabel lblMotivo = new JLabel("Motivo del reembolso:");
	     lblMotivo.setFont(new Font("Arial", Font.BOLD, 13));
	     lblMotivo.setBounds(30, 445, 250, 20);
	     add(lblMotivo);
	
	     txtMotivo = new JTextArea();
	     txtMotivo.setFont(new Font("Arial", Font.PLAIN, 12));
	     txtMotivo.setLineWrap(true);
	     txtMotivo.setWrapStyleWord(true);
	     txtMotivo.setEnabled(false);
	     txtMotivo.setBorder(BorderFactory.createLineBorder(Color.GRAY));
	
	     JScrollPane scrollMotivo = new JScrollPane(txtMotivo);
	     scrollMotivo.setBounds(30, 470, 660, 70);
	     add(scrollMotivo);

        // Botón Enviar Contraoferta (Centro-Izquierda)
        btnSolicitarReembolso = new JButton("ENVIAR SOLICITUD");
        btnSolicitarReembolso.setFont(new Font("Arial", Font.BOLD, 14));
        btnSolicitarReembolso.setBackground(new Color(46, 204, 113));
        btnSolicitarReembolso.setForeground(Color.WHITE);
        btnSolicitarReembolso.setFocusPainted(false);
        btnSolicitarReembolso.setBounds(150, 10, 220, 40);
        btnSolicitarReembolso.setEnabled(false);
        panelBotones.add(btnSolicitarReembolso);

        // Botón Cerrar (Centro-Derecha)
        btnSalir = new JButton("CERRAR");
        btnSalir.setFont(new Font("Arial", Font.BOLD, 14));
        btnSalir.setBackground(new Color(231, 76, 60));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFocusPainted(false);
        btnSalir.setBounds(390, 10, 120, 40);
        panelBotones.add(btnSalir);
        
     // ============================
        // LISTENERS
        // ============================
        listaTiquetes.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    tiqueteSeleccionado();
                }
            }
        });

        btnSolicitarReembolso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarSolicitud();
            }
        });

        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                volverAlMenu();
            }
        });
    
    }
    
    private void cargarTiquetes() {
    	modeloTiquetes.clear();
    	mapaSol.clear();
        txtDetalleTiquete.setText("");
        txtMotivo.setText("");
        txtMotivo.setEnabled(false);
        btnSolicitarReembolso.setEnabled(false);
        
        if (!(user instanceof IDuenoTiquetes)) {
            modeloTiquetes.addElement("Este usuario no puede poseer tiquetes");
            return;
        }
        IDuenoTiquetes dueno = (IDuenoTiquetes) user;
        
        ArrayList<Tiquete> tiquetes = (ArrayList<Tiquete>) dueno.getTiquetes();
        
        
        if (tiquetes == null || tiquetes.isEmpty()) {
            modeloTiquetes.addElement("No tienes tiquetes disponibles");
            return;
        }

        int contador = 1;
        for (Tiquete tiquete : tiquetes) {
            // Obtener información del evento asociado
            Evento evento = tiquete.getEventoAsociado();
            String nombreEvento = evento != null ? evento.getNombre() : "Sin evento";
            String fechaEvento = evento != null ? evento.getFecha() : "Sin fecha";
            
            // Formato: #001 | Evento | Fecha | Localidad | Tipo | Estado
            String key = String.format("#%-3d | %-25s | %-12s | %-15s | %-10s | %s",
                contador++,
                truncar(nombreEvento, 25),
                fechaEvento,
                truncar(tiquete.getLocalidadAsociada().getNombre(), 15),
                tiquete.getTipoTiquete(),
                obtenerEstadoTiquete(tiquete));
            
            modeloTiquetes.addElement(key);
            
            // Guardar el tiquete en el mapa para recuperarlo después
            mapaSol.put(tiquete, key);
        }

        if (modeloTiquetes.isEmpty()) {
            modeloTiquetes.addElement("No tienes tiquetes disponibles");
        }
    

    }
    private void tiqueteSeleccionado() {
        String seleccion = listaTiquetes.getSelectedValue();
        
        if (seleccion == null) {
            txtDetalleTiquete.setText("");
            btnSolicitarReembolso.setEnabled(false);
            return;
        }

        Tiquete tiquete = null;
        for (Map.Entry<Tiquete, String> entry : mapaSol.entrySet()) {
            if (entry.getValue().equals(seleccion)) {
                tiquete = entry.getKey();
                break;
            }
        }
        if (tiquete == null) {
            txtDetalleTiquete.setText("");
            btnSolicitarReembolso.setEnabled(false);
            return;
        }
        
        // Mostrar detalle completo del tiquete
        StringBuilder detalle = new StringBuilder();
        detalle.append("═══════════════════════════════════════════════\n");
        detalle.append("           DETALLE DEL TIQUETE\n");
        detalle.append("═══════════════════════════════════════════════\n\n");
        
        // Información del tiquete
        detalle.append(" INFORMACIÓN DEL TIQUETE\n");
        detalle.append("   ID: ").append(tiquete.getId()).append("\n");
        detalle.append("   Tipo: ").append(tiquete.getTipoTiquete()).append("\n");
        detalle.append("   Localidad: ").append(tiquete.getLocalidadAsociada().getNombre()).append("\n");
        detalle.append("   Precio: $").append(String.format("%.2f", tiquete.getPrecioBaseSinCalcular())).append("\n");
        detalle.append("   Fecha Expiración: ").append(tiquete.getFechaExpiracion()).append("\n");
        detalle.append("   Estado: ").append(obtenerEstadoTiquete(tiquete)).append("\n\n");
        
        // Información del evento
        Evento evento = tiquete.getEventoAsociado();
        if (evento != null) {
            detalle.append(" EVENTO ASOCIADO\n");
            detalle.append("   Nombre: ").append(evento.getNombre()).append("\n");
            detalle.append("   Fecha: ").append(evento.getFecha()).append("\n");
            detalle.append("   Lugar: ").append(evento.getVenueAsociado().getUbicacion()).append("\n");
            detalle.append("   Estado: ").append(evento.getCancelado() ? "❌ CANCELADO" : "✓ ACTIVO").append("\n\n");
        }
        
        detalle.append("═══════════════════════════════════════════════\n");
        
        // Verificar si se puede solicitar reembolso
        if (tiquete.isAnulado()) {
            detalle.append(" Este tiquete ya está anulado\n");
            btnSolicitarReembolso.setEnabled(false);
        } else if (!tiquete.isTransferido()) {
            detalle.append(" Este tiquete no está vendido\n");
            btnSolicitarReembolso.setEnabled(false);
        } else if (evento != null && evento.getCancelado()) {
            detalle.append(" Evento cancelado - Reembolso automático disponible\n");
            btnSolicitarReembolso.setEnabled(true);
        } else {
            detalle.append(" Puedes solicitar reembolso para este tiquete\n");
            btnSolicitarReembolso.setEnabled(true);
        }
        
        detalle.append("Escribe el motivo del reembolso.");
        txtMotivo.setText("");
        txtMotivo.setEnabled(true);
        txtMotivo.requestFocus();
        btnSolicitarReembolso.setEnabled(true);
        txtDetalleTiquete.setText(detalle.toString());
        txtDetalleTiquete.setCaretPosition(0); // Scroll al inicio
    }
    
    private void enviarSolicitud() {
        String seleccion = listaTiquetes.getSelectedValue();
        if (seleccion == null) return;

        Tiquete tiquete = buscarTiquetePorKey(seleccion);
        if (tiquete == null) return;

        String motivo = txtMotivo.getText().trim();

        if (motivo.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debes escribir un motivo para la solicitud.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Evento evento = tiquete.getEventoAsociado();

        // Confirmar acción
        int confirm = JOptionPane.showConfirmDialog(
                this,
                String.format(
                        "¿Enviar solicitud de reembolso?\n\n" +
                        "Tiquete: %s\n" +
                        "Evento: %s\n" +
                        "Usuario: %s\n\n" +
                        "Motivo:\n%s\n\n",
                        tiquete.getId(),
                        evento != null ? evento.getNombre() : "Sin evento",
                        user.getLogin(),
                        motivo
                ),
                "Confirmar Solicitud",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            // ✅ CORRECTO: Usar el método de Transaccion que ya hace todo
            Transaccion trans = new Transaccion("SOLICITUD_REEMBOLSO", tiquete, user, 
                                               java.time.LocalDateTime.now(), null, 0);
            trans.solicitarReembolso(tiquete, motivo, sistema);

            JOptionPane.showMessageDialog(this,
                    String.format("Solicitud de reembolso enviada exitosamente\n\n" +
                                  "Tiquete: %s\n" +
                                  "Evento: %s\n" +
                                  "Motivo entregado:\n%s\n\n" +
                                  "El administrador será notificado.",
                                  tiquete.getId(),
                                  tiquete.getEventoAsociado() != null ?
                                      tiquete.getEventoAsociado().getNombre() :
                                      "Sin evento",
                                  motivo),
                    "Solicitud enviada",
                    JOptionPane.INFORMATION_MESSAGE);

            // Limpiar interfaz
            modeloTiquetes.removeElement(seleccion);
            mapaSol.remove(tiquete);

            txtDetalleTiquete.setText("");
            txtMotivo.setText("");
            txtMotivo.setEnabled(false);
            btnSolicitarReembolso.setEnabled(false);

            if (modeloTiquetes.isEmpty()) {
                modeloTiquetes.addElement("No tienes tiquetes disponibles");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al enviar la solicitud:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    
    
    private String obtenerEstadoTiquete(Tiquete tiquete) {
        if (tiquete.isAnulado()) {
            return " ANULADO";
        } else if (tiquete.isTransferido()) {
            return " ACTIVO";
        } else {
            return " DISPONIBLE";
        }
    }
    
    private Tiquete buscarTiquetePorKey(String key) {
        for (Map.Entry<Tiquete, String> entry : mapaSol.entrySet()) {
            if (entry.getValue().equals(key)) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    
    
    
    
    
    private String truncar(String texto, int maxLength) {
        if (texto == null) return "";
        if (texto.length() <= maxLength) return texto;
        return texto.substring(0, maxLength - 3) + "...";
    }
    
    private void volverAlMenu() {
        dispose();
        
        if (user instanceof Promotor) {
            new ventanaMenuPromotor((Promotor) user, sistema).setVisible(true);
        } else if (user instanceof Organizador) {
            new ventanaMenuOrganizador((Organizador) user, sistema).setVisible(true);
        } else if (user instanceof Cliente) {
            new ventanaMenuComprador((Cliente) user, sistema).setVisible(true);
        }
    }
    
    
    
    

}
