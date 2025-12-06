package interfaz;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

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
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Evento.Evento;
import Finanzas.marketPlaceReventas;
import Persistencia.SistemaPersistencia;
import excepciones.TransferenciaNoPermitidaException;
import tiquete.Tiquete;
import usuario.Cliente;
import usuario.IDuenoTiquetes;
import usuario.Organizador;
import usuario.Promotor;
import usuario.Usuario;

public class VentanaContraofertar extends JFrame {
    private static final long serialVersionUID = 1L;

    private JList<String> listaOfertas;
    private DefaultListModel<String> modeloOfertas;
    
    private JLabel lblSaldoActual;
    private JButton btnEnviarContraoferta;
    private JButton btnActualizar;
    private JButton btnCerrar;
    private JTextArea txtDetalleOferta;
    private JLabel lblPrecioOriginal;
    private JTextField txtPrecioContraoferta;

    // Modelo
    private SistemaPersistencia sistema;
    private Usuario comprador;
    private Map<String, OfertaInfo> mapaOfertas;
    
    public VentanaContraofertar(SistemaPersistencia sistema, Usuario comprador) throws TransferenciaNoPermitidaException {
        this.comprador = comprador;
        this.sistema = sistema;
        this.mapaOfertas = new HashMap<>();

        inicializarComponentes();
        cargarOfertas();
    }
    
    private void inicializarComponentes() {
        setTitle("BOLETAMASTER: Hacer Contraoferta");
        setSize(720, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        
        // ============================
        // BARRA SUPERIOR
        // ============================
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(52, 152, 219));
        panelSuperior.setBounds(0, 0, 720, 50);
        panelSuperior.setLayout(null);
        add(panelSuperior);

        JLabel lblBienvenida = new JLabel(" HACER CONTRAOFERTA");
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 16));
        lblBienvenida.setForeground(Color.WHITE);
        lblBienvenida.setBounds(20, 15, 300, 20);
        panelSuperior.add(lblBienvenida);

        lblSaldoActual = new JLabel("Saldo: $0.00");
        lblSaldoActual.setFont(new Font("Arial", Font.BOLD, 14));
        lblSaldoActual.setForeground(Color.WHITE);
        lblSaldoActual.setBounds(520, 15, 180, 20);
        panelSuperior.add(lblSaldoActual);
        actualizarSaldo();
        
        // ============================
        // OFERTAS DISPONIBLES
        // ============================
        JLabel lblOfertas = new JLabel("Ofertas Disponibles para Contraofertar:");
        lblOfertas.setFont(new Font("Arial", Font.BOLD, 13));
        lblOfertas.setBounds(30, 65, 350, 20);
        add(lblOfertas);
        
 
        btnActualizar = new JButton(" Actualizar");
        btnActualizar.setFont(new Font("Arial", Font.PLAIN, 11));
        btnActualizar.setBounds(580, 60, 110, 30);
        btnActualizar.setFocusPainted(false);
        btnActualizar.addActionListener(e -> {
            try {
                cargarOfertas();
            } catch (TransferenciaNoPermitidaException ex) {
                JOptionPane.showMessageDialog(this,
                    "Error al cargar ofertas: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        add(btnActualizar);
        
        // Lista scrolleable de ofertas
        modeloOfertas = new DefaultListModel<>();
        listaOfertas = new JList<>(modeloOfertas);
        listaOfertas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaOfertas.setFont(new Font("Monospaced", Font.PLAIN, 11));

        JScrollPane scrollOfertas = new JScrollPane(listaOfertas);
        scrollOfertas.setBounds(30, 95, 660, 180);
        add(scrollOfertas);

        // ============================
        // DETALLE DE LA OFERTA
        // ============================
        JLabel lblDetalle = new JLabel("Detalle de la Oferta:");
        lblDetalle.setFont(new Font("Arial", Font.BOLD, 13));
        lblDetalle.setBounds(30, 290, 200, 20);
        add(lblDetalle);

        txtDetalleOferta = new JTextArea();
        txtDetalleOferta.setEditable(false);
        txtDetalleOferta.setFont(new Font("Arial", Font.PLAIN, 12));
        txtDetalleOferta.setLineWrap(true);
        txtDetalleOferta.setWrapStyleWord(true);
        txtDetalleOferta.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        txtDetalleOferta.setBackground(new Color(245, 245, 245));

        JScrollPane scrollDetalle = new JScrollPane(txtDetalleOferta);
        scrollDetalle.setBounds(30, 315, 660, 120);
        add(scrollDetalle);
        
        // ============================
        // SECCIÓN DE CONTRAOFERTA
        // ============================
        JPanel panelContraoferta = new JPanel();
        panelContraoferta.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
            "Tu Contraoferta",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Arial", Font.BOLD, 12),
            new Color(52, 152, 219)
        ));
        panelContraoferta.setBounds(30, 450, 660, 90);
        panelContraoferta.setLayout(null);
        add(panelContraoferta);

        // Precio original
        JLabel lblPrecioOriginalLabel = new JLabel("Precio Original:");
        lblPrecioOriginalLabel.setFont(new Font("Arial", Font.BOLD, 12));
        lblPrecioOriginalLabel.setBounds(40, 30, 120, 25);
        panelContraoferta.add(lblPrecioOriginalLabel);

        lblPrecioOriginal = new JLabel("$0.00");
        lblPrecioOriginal.setFont(new Font("Arial", Font.BOLD, 16));
        lblPrecioOriginal.setForeground(new Color(231, 76, 60));
        lblPrecioOriginal.setBounds(170, 30, 100, 25);
        panelContraoferta.add(lblPrecioOriginal);

        // Tu oferta
        JLabel lblTuOferta = new JLabel("Tu Oferta:");
        lblTuOferta.setFont(new Font("Arial", Font.BOLD, 12));
        lblTuOferta.setBounds(320, 30, 80, 25);
        panelContraoferta.add(lblTuOferta);

        JLabel lblDolar = new JLabel("$");
        lblDolar.setFont(new Font("Arial", Font.BOLD, 16));
        lblDolar.setBounds(410, 30, 15, 25);
        panelContraoferta.add(lblDolar);

        txtPrecioContraoferta = new JTextField();
        txtPrecioContraoferta.setFont(new Font("Arial", Font.PLAIN, 14));
        txtPrecioContraoferta.setBounds(425, 30, 120, 30);
        txtPrecioContraoferta.setEnabled(false);
        panelContraoferta.add(txtPrecioContraoferta);

        JLabel lblAyuda = new JLabel("(Ingrese su precio propuesto)");
        lblAyuda.setFont(new Font("Arial", Font.ITALIC, 10));
        lblAyuda.setForeground(Color.GRAY);
        lblAyuda.setBounds(425, 60, 200, 15);
        panelContraoferta.add(lblAyuda);


        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(null);
        panelBotones.setBounds(30, 555, 660, 60);
        add(panelBotones);

        // Botón Enviar Contraoferta (Centro-Izquierda)
        btnEnviarContraoferta = new JButton("ENVIAR CONTRAOFERTA");
        btnEnviarContraoferta.setFont(new Font("Arial", Font.BOLD, 14));
        btnEnviarContraoferta.setBackground(new Color(46, 204, 113));
        btnEnviarContraoferta.setForeground(Color.WHITE);
        btnEnviarContraoferta.setFocusPainted(false);
        btnEnviarContraoferta.setBounds(150, 10, 220, 40);
        btnEnviarContraoferta.setEnabled(false);
        panelBotones.add(btnEnviarContraoferta);

        // Botón Cerrar (Centro-Derecha)
        btnCerrar = new JButton("CERRAR");
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCerrar.setBackground(new Color(231, 76, 60));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setBounds(390, 10, 120, 40);
        panelBotones.add(btnCerrar);
        
        // ============================
        // LISTENERS
        // ============================
        listaOfertas.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    ofertaSeleccionada();
                }
            }
        });

        btnEnviarContraoferta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarContraoferta();
            }
        });

        btnCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                volverAlMenu();
            }
        });
    }
    
    private void cargarOfertas() throws TransferenciaNoPermitidaException {
        modeloOfertas.clear();
        mapaOfertas.clear();
        txtDetalleOferta.setText("");
        lblPrecioOriginal.setText("$0.00");
        txtPrecioContraoferta.setText("");
        txtPrecioContraoferta.setEnabled(false);
        btnEnviarContraoferta.setEnabled(false);

        marketPlaceReventas marketplace = sistema.getMarketplace();
        
        if (marketplace == null) {
            modeloOfertas.addElement("El marketplace no está disponible");
            return;
        }

        Queue<HashMap<Tiquete, String>> todasOfertas = marketplace.getOfertas();

        if (todasOfertas == null || todasOfertas.isEmpty()) {
            modeloOfertas.addElement("No hay ofertas disponibles en el marketplace");
            return;
        }

        int contador = 1;
        
        // Recorrer todas las ofertas
        for (HashMap<Tiquete, String> oferta : todasOfertas) {
            for (Map.Entry<Tiquete, String> entry : oferta.entrySet()) {
                Tiquete tiquete = entry.getKey();
                String etiqueta = entry.getValue();
                
                // Buscar el vendedor (dueño actual del tiquete)
                Usuario vendedor = buscarVendedor(tiquete);
                
                if (vendedor == null) continue; // Oferta inválida
                
                // No mostrar mis propias ofertas
                if (vendedor.getLogin().equals(comprador.getLogin())) {
                    continue;
                }
                
                // Extraer precio de la etiqueta
                double precio = marketPlaceReventas.extraerPrecio(etiqueta);
                
                // Obtener evento del tiquete
                Evento evento = tiquete.getEventoAsociado();
                String nombreEvento = evento != null ? evento.getNombre() : "Sin evento";
                
                
                String key = String.format("#%-3d | %-25s | %-15s | $%-8.2f | %s",
                    contador++,
                    truncar(nombreEvento, 25),
                    truncar(tiquete.getLocalidadAsociada().getNombre(), 15),
                    precio,
                    truncar(vendedor.getLogin(), 20));
                
                modeloOfertas.addElement(key);
                
                // Guardar información completa de la oferta
                OfertaInfo info = new OfertaInfo(tiquete, vendedor, precio, etiqueta, oferta);
                mapaOfertas.put(key, info);
            }
        }

        if (modeloOfertas.isEmpty()) {
            modeloOfertas.addElement("No hay ofertas de otros usuarios disponibles");
        }
    }
    
    private Usuario buscarVendedor(Tiquete tiquete) {
        for (Usuario u : sistema.getUsuarios()) {
            if (u instanceof IDuenoTiquetes) {
                IDuenoTiquetes dueno = (IDuenoTiquetes) u;
                
                for (Tiquete t : dueno.getTiquetes()) {
                    if (t.getId().equals(tiquete.getId())) {
                        return u;
                    }
                }
            }
        }
        return null;
    }
    
    private void ofertaSeleccionada() {
        String seleccion = listaOfertas.getSelectedValue();
        
        if (seleccion == null || !mapaOfertas.containsKey(seleccion)) {
            txtDetalleOferta.setText("");
            lblPrecioOriginal.setText("$0.00");
            txtPrecioContraoferta.setText("");
            txtPrecioContraoferta.setEnabled(false);
            btnEnviarContraoferta.setEnabled(false);
            return;
        }

        OfertaInfo info = mapaOfertas.get(seleccion);
        Tiquete tiquete = info.tiquete;
        Usuario vendedor = info.vendedor;
        double precio = info.precio;
        
        // Mostrar detalle completo de la oferta
        StringBuilder detalle = new StringBuilder();
        detalle.append("═══════════════════════════════════════════════\n");
        detalle.append("           DETALLE DE LA OFERTA\n");
        detalle.append("═══════════════════════════════════════════════\n\n");
        
        // Información del tiquete
        detalle.append("🎫 TIQUETE\n");
        detalle.append("   ID: ").append(tiquete.getId()).append("\n");
        detalle.append("   Tipo: ").append(tiquete.getTipoTiquete()).append("\n");
        detalle.append("   Localidad: ").append(tiquete.getLocalidadAsociada().getNombre()).append("\n");
        detalle.append("   Fecha Expiración: ").append(tiquete.getFechaExpiracion()).append("\n\n");
        
        // Información del evento
        Evento evento = tiquete.getEventoAsociado();
        if (evento != null) {
            detalle.append(" EVENTO\n");
            detalle.append("   Nombre: ").append(evento.getNombre()).append("\n");
            detalle.append("   Fecha: ").append(evento.getFecha()).append("\n");
            detalle.append("   Lugar: ").append(evento.getVenueAsociado().getUbicacion()).append("\n\n");
        }
        
        // Información del vendedor
        detalle.append("👤 VENDEDOR\n");
        detalle.append("   Usuario: ").append(vendedor.getLogin()).append("\n\n");
        
        // Precio
        detalle.append("═══════════════════════════════════════════════\n");
        detalle.append(" PRECIO PEDIDO: $").append(String.format("%.2f", precio)).append("\n");
        detalle.append("═══════════════════════════════════════════════\n\n");
        
        detalle.append(" Propón tu precio y el vendedor podrá aceptar o rechazar tu contraoferta.");
        
        txtDetalleOferta.setText(detalle.toString());
        txtDetalleOferta.setCaretPosition(0);
        
        // Mostrar precio original y habilitar campo
        lblPrecioOriginal.setText(String.format("$%.2f", precio));
        txtPrecioContraoferta.setText("");
        txtPrecioContraoferta.setEnabled(true);
        txtPrecioContraoferta.requestFocus();
        btnEnviarContraoferta.setEnabled(true);
    }
    
    private void enviarContraoferta() {
        String seleccion = listaOfertas.getSelectedValue();
        
        if (seleccion == null || !mapaOfertas.containsKey(seleccion)) {
            JOptionPane.showMessageDialog(this,
                "Debe seleccionar una oferta",
                "Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        OfertaInfo info = mapaOfertas.get(seleccion);
        Tiquete tiquete = info.tiquete;
        Usuario vendedor = info.vendedor;
        double precioOriginal = info.precio;

        // Validar el precio ingresado
        String precioTexto = txtPrecioContraoferta.getText().trim();
        
        if (precioTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Debe ingresar un precio para su contraoferta",
                "Campo Requerido",
                JOptionPane.WARNING_MESSAGE);
            txtPrecioContraoferta.requestFocus();
            return;
        }

        double precioContraoferta;
        try {
            precioContraoferta = Double.parseDouble(precioTexto);
            
            if (precioContraoferta <= 0) {
                JOptionPane.showMessageDialog(this,
                    "El precio debe ser mayor a 0",
                    "Precio Inválido",
                    JOptionPane.ERROR_MESSAGE);
                txtPrecioContraoferta.selectAll();
                txtPrecioContraoferta.requestFocus();
                return;
            }
            
            if (precioContraoferta >= precioOriginal) {
                int opcion = JOptionPane.showConfirmDialog(this,
                    String.format("Tu contraoferta ($%.2f) es igual o mayor al precio pedido ($%.2f).\n\n" +
                                 "¿Prefieres comprar directamente al precio del vendedor?",
                                 precioContraoferta, precioOriginal),
                    "Precio Alto",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
                
                if (opcion == JOptionPane.YES_OPTION) {
                    return;
                }
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Precio inválido. Ingrese un número válido\nEjemplo: 50.00",
                "Error de Formato",
                JOptionPane.ERROR_MESSAGE);
            txtPrecioContraoferta.selectAll();
            txtPrecioContraoferta.requestFocus();
            return;
        }

        // Verificar saldo
        if (comprador instanceof IDuenoTiquetes) {
            IDuenoTiquetes dueno = (IDuenoTiquetes) comprador;
            if (precioContraoferta > dueno.getSaldo()) {
                JOptionPane.showMessageDialog(this,
                    String.format("Tu saldo ($%.2f) es insuficiente para tu contraoferta ($%.2f)",
                        dueno.getSaldo(), precioContraoferta),
                    "Saldo Insuficiente",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
        }


        int confirmacion = JOptionPane.showConfirmDialog(this,
            String.format("¿Enviar contraoferta?\n\n" +
                         "Tiquete: %s\n" +
                         "Evento: %s\n" +
                         "Vendedor: %s\n\n" +
                         "Precio Original: $%.2f\n" +
                         "Tu Contraoferta: $%.2f\n\n" +
                         "El vendedor podrá aceptar o rechazar tu oferta.",
                         tiquete.getId(),
                         tiquete.getEventoAsociado() != null ? 
                             tiquete.getEventoAsociado().getNombre() : "Sin evento",
                         vendedor.getLogin(),
                         precioOriginal,
                         precioContraoferta),
            "Confirmar Contraoferta",
            JOptionPane.YES_NO_OPTION);
        
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

      
        try {
            marketPlaceReventas market = sistema.getMarketplace();
            market.registrarContraoferta(tiquete, vendedor, comprador, precioContraoferta, sistema);

            // Mostrar éxito
            JOptionPane.showMessageDialog(this,
                String.format("✓ Contraoferta enviada exitosamente!\n\n" +
                             "Tiquete: %s\n" +
                             "Tu oferta: $%.2f\n" +
                             "Vendedor: %s\n\n" +
                             "El vendedor será notificado de tu contraoferta.\n",
                             tiquete.getId(),
                             precioContraoferta,
                             vendedor.getLogin()),
                "Contraoferta Enviada",
                JOptionPane.INFORMATION_MESSAGE);

           
            modeloOfertas.removeElement(seleccion);
            mapaOfertas.remove(seleccion);

            // Limpiar formulario
            txtPrecioContraoferta.setText("");
            txtPrecioContraoferta.setEnabled(false);
            btnEnviarContraoferta.setEnabled(false);
            txtDetalleOferta.setText("");
            lblPrecioOriginal.setText("$0.00");
            
            // Si no quedan ofertas, mostrar mensaje
            if (modeloOfertas.isEmpty()) {
                modeloOfertas.addElement("No hay más ofertas disponibles");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al enviar contraoferta:\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void actualizarSaldo() {
        if (comprador instanceof IDuenoTiquetes) {
            IDuenoTiquetes dueno = (IDuenoTiquetes) comprador;
            lblSaldoActual.setText(String.format("Saldo: $%.2f", dueno.getSaldo()));
        }
    }

    private String truncar(String texto, int maxLength) {
        if (texto == null) return "";
        if (texto.length() <= maxLength) return texto;
        return texto.substring(0, maxLength - 3) + "...";
    }
    
    private void volverAlMenu() {
        dispose();
        
        if (comprador instanceof Promotor) {
            new ventanaMenuPromotor((Promotor) comprador, sistema).setVisible(true);
        } else if (comprador instanceof Organizador) {
            new ventanaMenuOrganizador((Organizador) comprador, sistema).setVisible(true);
        } else if (comprador instanceof Cliente) {
            new ventanaMenuComprador((Cliente) comprador, sistema).setVisible(true);
        }
    }
    
    private static class OfertaInfo {
        Tiquete tiquete;
        Usuario vendedor;
        double precio;
        String etiqueta;
        HashMap<Tiquete, String> ofertaOriginal;

        public OfertaInfo(Tiquete tiquete, Usuario vendedor, double precio, 
                         String etiqueta, HashMap<Tiquete, String> ofertaOriginal) {
            this.tiquete = tiquete;
            this.vendedor = vendedor;
            this.precio = precio;
            this.etiqueta = etiqueta;
            this.ofertaOriginal = ofertaOriginal;
        }
    }
}