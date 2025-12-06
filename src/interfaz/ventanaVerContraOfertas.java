package interfaz;

import java.awt.*;
import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.List;

import Finanzas.marketPlaceReventas;
import Persistencia.SistemaPersistencia;
import excepciones.TransferenciaNoPermitidaException;
import tiquete.Tiquete;
import tiquete.TiqueteMultiple;
import usuario.IDuenoTiquetes;
import usuario.Usuario;
import usuario.Promotor;
import usuario.Organizador;
import usuario.Cliente;

public class ventanaVerContraOfertas extends JFrame {
    private static final long serialVersionUID = 1L;

    // Componentes UI
    private JList<String> listaContraofertas;
    private DefaultListModel<String> modeloLista;
    private JTextArea areaDetalle;
    private JLabel lblPrecioOriginal;
    private JLabel lblPrecioContraoferta;
    private JButton btnAceptar;
    private JButton btnRechazar;
    private JButton btnVolver;

    // Modelo
    private IDuenoTiquetes vendedor;
    private SistemaPersistencia sistema;
    private HashMap<Integer, ContraofertaInfo> mapaContraofertas;
    private ContraofertaInfo contraofertaSeleccionada;

    public ventanaVerContraOfertas(IDuenoTiquetes vendedor, SistemaPersistencia sistema) {
        this.vendedor = vendedor;
        this.sistema = sistema;
        this.mapaContraofertas = new HashMap<>();

        initComponents();
        cargarContraofertas();
    }

    private void initComponents() {
        setTitle("BOLETAMASTER: Mis Contraofertas Recibidas");
        setLayout(null);
        setSize(750, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // ============================
        // BARRA SUPERIOR
        // ============================
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(52, 152, 219));
        panelSuperior.setBounds(0, 0, 750, 50);
        panelSuperior.setLayout(null);
        add(panelSuperior);

        JLabel lblTitulo = new JLabel("MIS CONTRAOFERTAS RECIBIDAS");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBounds(20, 15, 400, 20);
        panelSuperior.add(lblTitulo);

        JLabel lblUsuario = new JLabel("Vendedor: " + ((Usuario)vendedor).getLogin());
        lblUsuario.setFont(new Font("Arial", Font.BOLD, 13));
        lblUsuario.setForeground(Color.WHITE);
        lblUsuario.setBounds(550, 15, 180, 20);
        panelSuperior.add(lblUsuario);

        // ============================
        // INSTRUCCIÓN
        // ============================
        JLabel lblInstruccion = new JLabel("Selecciona una contraoferta para ver detalles:");
        lblInstruccion.setFont(new Font("Arial", Font.PLAIN, 12));
        lblInstruccion.setBounds(20, 60, 400, 20);
        add(lblInstruccion);

        // ============================
        // LISTA DE CONTRAOFERTAS
        // ============================
        modeloLista = new DefaultListModel<>();
        listaContraofertas = new JList<>(modeloLista);
        listaContraofertas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaContraofertas.setFont(new Font("Monospaced", Font.PLAIN, 11));
        listaContraofertas.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarDetalleContraoferta();
            }
        });

        JScrollPane scrollContraofertas = new JScrollPane(listaContraofertas);
        scrollContraofertas.setBounds(20, 85, 710, 150);
        add(scrollContraofertas);

        // ============================
        // DETALLE DE LA CONTRAOFERTA
        // ============================
        JLabel lblDetalle = new JLabel("Detalle de la contraoferta:");
        lblDetalle.setFont(new Font("Arial", Font.BOLD, 13));
        lblDetalle.setBounds(20, 245, 250, 20);
        add(lblDetalle);

        areaDetalle = new JTextArea();
        areaDetalle.setEditable(false);
        areaDetalle.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaDetalle.setBackground(new Color(245, 245, 245));
        areaDetalle.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        JScrollPane scrollDetalle = new JScrollPane(areaDetalle);
        scrollDetalle.setBounds(20, 270, 710, 180);
        add(scrollDetalle);

        // ============================
        // PANEL DE PRECIOS
        // ============================
        JPanel panelPrecios = new JPanel();
        panelPrecios.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
            "Comparación de Precios",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Arial", Font.BOLD, 12),
            new Color(52, 152, 219)
        ));
        panelPrecios.setBounds(20, 460, 710, 80);
        panelPrecios.setLayout(null);
        add(panelPrecios);

        // Tu precio original
        JLabel lblOriginalLabel = new JLabel("Tu Precio:");
        lblOriginalLabel.setFont(new Font("Arial", Font.BOLD, 12));
        lblOriginalLabel.setBounds(80, 30, 100, 25);
        panelPrecios.add(lblOriginalLabel);

        lblPrecioOriginal = new JLabel("$0.00");
        lblPrecioOriginal.setFont(new Font("Arial", Font.BOLD, 18));
        lblPrecioOriginal.setForeground(new Color(231, 76, 60));
        lblPrecioOriginal.setBounds(180, 30, 120, 25);
        panelPrecios.add(lblPrecioOriginal);

        // Flecha
        JLabel lblFlecha = new JLabel("→");
        lblFlecha.setFont(new Font("Arial", Font.BOLD, 24));
        lblFlecha.setBounds(320, 25, 40, 30);
        panelPrecios.add(lblFlecha);

        // Contraoferta
        JLabel lblContraofertaLabel = new JLabel("Contraoferta:");
        lblContraofertaLabel.setFont(new Font("Arial", Font.BOLD, 12));
        lblContraofertaLabel.setBounds(380, 30, 100, 25);
        panelPrecios.add(lblContraofertaLabel);

        lblPrecioContraoferta = new JLabel("$0.00");
        lblPrecioContraoferta.setFont(new Font("Arial", Font.BOLD, 18));
        lblPrecioContraoferta.setForeground(new Color(46, 204, 113));
        lblPrecioContraoferta.setBounds(500, 30, 120, 25);
        panelPrecios.add(lblPrecioContraoferta);

        // ============================
        // BOTONES
        // ============================
        btnVolver = new JButton("Volver");
        btnVolver.setFont(new Font("Arial", Font.BOLD, 13));
        btnVolver.setBounds(20, 560, 120, 40);
        btnVolver.addActionListener(e -> volverAlMenu());
        add(btnVolver);

        btnRechazar = new JButton("RECHAZAR");
        btnRechazar.setFont(new Font("Arial", Font.BOLD, 14));
        btnRechazar.setBackground(new Color(231, 76, 60));
        btnRechazar.setForeground(Color.WHITE);
        btnRechazar.setFocusPainted(false);
        btnRechazar.setBounds(390, 560, 160, 40);
        btnRechazar.setEnabled(false);
        btnRechazar.addActionListener(e -> procesarContraoferta(false));
        add(btnRechazar);

        btnAceptar = new JButton("ACEPTAR");
        btnAceptar.setFont(new Font("Arial", Font.BOLD, 14));
        btnAceptar.setBackground(new Color(46, 204, 113));
        btnAceptar.setForeground(Color.WHITE);
        btnAceptar.setFocusPainted(false);
        btnAceptar.setBounds(570, 560, 160, 40);
        btnAceptar.setEnabled(false);
        btnAceptar.addActionListener(e -> procesarContraoferta(true));
        add(btnAceptar);

        setVisible(true);
    }

    private void cargarContraofertas() {
        modeloLista.clear();
        mapaContraofertas.clear();

        List<HashMap<Tiquete, String>> listaOfertas = vendedor.getListaOfertas();

        if (listaOfertas == null || listaOfertas.isEmpty()) {
            modeloLista.addElement("No tienes contraofertas pendientes");
            areaDetalle.setText("No hay contraofertas para revisar en este momento.");
            return;
        }

        int num = 0;
        for (HashMap<Tiquete, String> mapa : listaOfertas) {
            for (Map.Entry<Tiquete, String> entry : mapa.entrySet()) {
                Tiquete tiquete = entry.getKey();
                String info = entry.getValue();

                // Extraer comprador y precio de la info
                // Formato: "comprador - Contraoferta: $precio"
                String comprador = "";
                double precio = 0.0;

                try {
                    String[] partes = info.split(" - Contraoferta: ");
                    if (partes.length == 2) {
                        comprador = partes[0];
                        String precioStr = partes[1].replace("$", "").trim();
                        precio = Double.parseDouble(precioStr);
                    }
                } catch (Exception e) {
                    continue; // Saltar si hay error en el formato
                }

                // Buscar precio original en el marketplace
                double precioOriginal = buscarPrecioOriginal(tiquete);

                String tipo = tiquete instanceof TiqueteMultiple ? "PAQUETE" : "SIMPLE";
                String evento = tiquete.getEvento() != null ? 
                                tiquete.getEvento().getNombre() : "Sin evento";

                String linea = String.format("#%-3d | [%s] %-12s | %-25s | De: %-15s | $%-8.2f",
                    num + 1,
                    tipo,
                    truncar(tiquete.getId(), 12),
                    truncar(evento, 25),
                    truncar(comprador, 15),
                    precio);

                modeloLista.addElement(linea);

                // Buscar el usuario comprador
                Usuario compradorUsuario = sistema.buscarUsuario(comprador);

                ContraofertaInfo infoCompleta = new ContraofertaInfo(
                    tiquete, 
                    compradorUsuario, 
                    precio, 
                    precioOriginal,
                    mapa
                );
                mapaContraofertas.put(num, infoCompleta);
                num++;
            }
        }

        if (modeloLista.isEmpty()) {
            modeloLista.addElement("No tienes contraofertas pendientes");
            areaDetalle.setText("No hay contraofertas para revisar en este momento.");
        }
    }

    private double buscarPrecioOriginal(Tiquete tiquete) {
        Queue<HashMap<Tiquete, String>> ofertas = sistema.getMarketplace().getOfertas();

        for (HashMap<Tiquete, String> mapa : ofertas) {
            for (Map.Entry<Tiquete, String> entry : mapa.entrySet()) {
                if (entry.getKey().getId().equals(tiquete.getId())) {
                    try {
                        return marketPlaceReventas.extraerPrecio(entry.getValue());
                    } catch (Exception e) {
                        return 0.0;
                    }
                }
            }
        }
        return 0.0;
    }

    private void mostrarDetalleContraoferta() {
        int index = listaContraofertas.getSelectedIndex();
        
        if (index < 0 || !mapaContraofertas.containsKey(index)) {
            areaDetalle.setText("");
            lblPrecioOriginal.setText("$0.00");
            lblPrecioContraoferta.setText("$0.00");
            btnAceptar.setEnabled(false);
            btnRechazar.setEnabled(false);
            contraofertaSeleccionada = null;
            return;
        }

        contraofertaSeleccionada = mapaContraofertas.get(index);
        
        Tiquete tiquete = contraofertaSeleccionada.tiquete;
        Usuario comprador = contraofertaSeleccionada.comprador;
        double precioContraoferta = contraofertaSeleccionada.precioContraoferta;
        double precioOriginal = contraofertaSeleccionada.precioOriginal;

        // Construir detalle
        StringBuilder detalle = new StringBuilder();
        detalle.append("=== DETALLE DE LA CONTRAOFERTA ===\n\n");
        
        detalle.append("TIQUETE\n");
        detalle.append("   ID: ").append(tiquete.getId()).append("\n");
        detalle.append("   Tipo: ").append(tiquete.getTipoTiquete()).append("\n");
        detalle.append("   Nombre: ").append(tiquete.getNombre()).append("\n");
        
        if (tiquete.getLocalidadAsociada() != null) {
            detalle.append("   Localidad: ").append(tiquete.getLocalidadAsociada().getNombre()).append("\n");
        }
        
        if (tiquete instanceof TiqueteMultiple) {
            TiqueteMultiple tm = (TiqueteMultiple) tiquete;
            detalle.append("   Incluye ").append(tm.getTiquetes().size()).append(" tiquetes\n");
        }
        detalle.append("\n");
        
        if (tiquete.getEvento() != null) {
            detalle.append("EVENTO\n");
            detalle.append("   Nombre: ").append(tiquete.getEvento().getNombre()).append("\n");
            detalle.append("   Fecha: ").append(tiquete.getEvento().getFecha()).append("\n");
            if (tiquete.getEvento().getVenueAsociado() != null) {
                detalle.append("   Lugar: ").append(tiquete.getEvento().getVenueAsociado().getUbicacion()).append("\n");
            }
            detalle.append("\n");
        }
        
        detalle.append("COMPRADOR INTERESADO\n");
        detalle.append("   Usuario: ").append(comprador != null ? comprador.getLogin() : "Desconocido").append("\n");
        if (comprador instanceof IDuenoTiquetes) {
            IDuenoTiquetes duenoComprador = (IDuenoTiquetes) comprador;
            detalle.append("   Saldo disponible: $").append(String.format("%.2f", duenoComprador.getSaldo())).append("\n");
        }
        detalle.append("\n");
        
        detalle.append("=== COMPARACION DE PRECIOS ===\n");
        detalle.append(String.format("   Tu precio original:  $%.2f\n", precioOriginal));
        detalle.append(String.format("   Contraoferta:        $%.2f\n", precioContraoferta));
        
        double diferencia = precioOriginal - precioContraoferta;
        if (diferencia > 0) {
            detalle.append(String.format("   Diferencia:          -$%.2f (%.1f%% menos)\n", 
                diferencia, (diferencia / precioOriginal) * 100));
        } else if (diferencia < 0) {
            detalle.append(String.format("   Diferencia:          +$%.2f (%.1f%% mas)\n", 
                Math.abs(diferencia), (Math.abs(diferencia) / precioOriginal) * 100));
        } else {
            detalle.append("   Diferencia:          Sin cambio\n");
        }

        areaDetalle.setText(detalle.toString());
        areaDetalle.setCaretPosition(0);

        // Actualizar labels de precio
        lblPrecioOriginal.setText(String.format("$%.2f", precioOriginal));
        lblPrecioContraoferta.setText(String.format("$%.2f", precioContraoferta));

        // Habilitar botones
        btnAceptar.setEnabled(true);
        btnRechazar.setEnabled(true);
    }

    private void procesarContraoferta(boolean aceptada) {
        if (contraofertaSeleccionada == null) {
            JOptionPane.showMessageDialog(this,
                    "Debes seleccionar una contraoferta.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Tiquete tiquete = contraofertaSeleccionada.tiquete;
        Usuario comprador = contraofertaSeleccionada.comprador;
        double precio = contraofertaSeleccionada.precioContraoferta;

        if (comprador == null) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo encontrar al comprador.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Confirmación
        String accion = aceptada ? "ACEPTAR" : "RECHAZAR";
        String mensaje = String.format(
            "¿Confirmar %s contraoferta?\n\n" +
            "Tiquete: %s\n" +
            "Comprador: %s\n" +
            "Contraoferta: $%.2f\n" +
            "Tu precio original: $%.2f\n\n" +
            (aceptada ? "Se realizará la venta y el tiquete será transferido." : 
                       "La contraoferta será rechazada."),
            accion,
            tiquete.getId(),
            comprador.getLogin(),
            precio,
            contraofertaSeleccionada.precioOriginal
        );

        int confirmacion = JOptionPane.showConfirmDialog(this,
                mensaje,
                "Confirmar " + accion,
                JOptionPane.YES_NO_OPTION,
                aceptada ? JOptionPane.QUESTION_MESSAGE : JOptionPane.WARNING_MESSAGE);

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        // Procesar contraoferta
        try {
            marketPlaceReventas market = sistema.getMarketplace();
            market.procesarContraoferta(
                tiquete,
                (Usuario) vendedor,
                comprador,
                aceptada,
                precio,
                sistema
            );

            // Eliminar la contraoferta de la lista del vendedor
            vendedor.getListaOfertas().remove(contraofertaSeleccionada.contraofertaOriginal);

            // Guardar persistencia
            sistema.guardarTodo();

            if (aceptada) {
                JOptionPane.showMessageDialog(this,
                        String.format("CONTRAOFERTA ACEPTADA\n\n" +
                                     "Venta realizada exitosamente!\n" +
                                     "Tiquete: %s\n" +
                                     "Comprador: %s\n" +
                                     "Precio de venta: $%.2f\n\n" +
                                     "El dinero ha sido transferido a tu saldo.",
                                     tiquete.getId(),
                                     comprador.getLogin(),
                                     precio),
                        "Venta Exitosa",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        String.format("CONTRAOFERTA RECHAZADA\n\n" +
                                     "La contraoferta de %s por el tiquete %s ha sido rechazada.\n" +
                                     "El tiquete permanece en tu poder.",
                                     comprador.getLogin(),
                                     tiquete.getId()),
                        "Contraoferta Rechazada",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            // Recargar lista
            cargarContraofertas();
            contraofertaSeleccionada = null;

        } catch (TransferenciaNoPermitidaException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al procesar contraoferta:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error inesperado:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private String truncar(String texto, int maxLength) {
        if (texto == null) return "";
        if (texto.length() <= maxLength) return texto;
        return texto.substring(0, maxLength - 3) + "...";
    }

    private void volverAlMenu() {
        dispose();

        if (vendedor instanceof Promotor) {
            new ventanaMenuPromotor((Promotor) vendedor, sistema).setVisible(true);
        } else if (vendedor instanceof Organizador) {
            new ventanaMenuOrganizador((Organizador) vendedor, sistema).setVisible(true);
        } else if (vendedor instanceof Cliente) {
            new ventanaMenuComprador((Cliente) vendedor, sistema).setVisible(true);
        }
    }

    // Clase interna para almacenar información de contraoferta
    private static class ContraofertaInfo {
        Tiquete tiquete;
        Usuario comprador;
        double precioContraoferta;
        double precioOriginal;
        HashMap<Tiquete, String> contraofertaOriginal;

        public ContraofertaInfo(Tiquete tiquete, Usuario comprador, 
                               double precioContraoferta, double precioOriginal,
                               HashMap<Tiquete, String> contraofertaOriginal) {
            this.tiquete = tiquete;
            this.comprador = comprador;
            this.precioContraoferta = precioContraoferta;
            this.precioOriginal = precioOriginal;
            this.contraofertaOriginal = contraofertaOriginal;
        }
    }
}