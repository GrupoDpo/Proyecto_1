package interfaz;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Evento.Evento;
import Finanzas.Transaccion;
import Persistencia.SistemaPersistencia;
import excepciones.SaldoInsuficienteExeption;
import excepciones.TiquetesNoDisponiblesException;
import tiquete.Tiquete;
import usuario.Cliente;
import usuario.IDuenoTiquetes;
import usuario.Organizador;
import usuario.Promotor;
import usuario.Usuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import interfaz.GeneradorQR;

public class ventanaComprarTiquete extends JFrame {
    private static final long serialVersionUID = 1L;

    private JList<String> listaEventos;
    private JList<String> listaTiquetes;
    private DefaultListModel<String> modeloEventos;
    private DefaultListModel<String> modeloTiquetes;
    
    private JLabel lblSaldoActual;
    private JLabel lblDisponibles;
    private JLabel lblTotalPagar;
    private JSpinner spinnerCantidad;
    private JButton btnComprar;
    private JButton btnCancelar;

    // Modelo
    private SistemaPersistencia sistema;
    private Usuario comprador;
    private List<Evento> eventosDisponibles;
    private Map<String, Evento> mapaEventos;
    private Map<String, List<Tiquete>> mapaTiquetesPorTipo; // Lista de tiquetes del mismo tipo
    private String tipoTiqueteSeleccionado;
    private Evento eventoActual;

    public ventanaComprarTiquete(SistemaPersistencia sistema, Usuario comprador) {
    	
    	this.comprador = comprador;
        this.sistema = sistema;
        this.eventosDisponibles = new ArrayList<>();
        this.mapaEventos = new HashMap<>();
        this.mapaTiquetesPorTipo = new HashMap<>();
        
        inicializarComponentes();
        cargarEventos();
        
    }
    private void inicializarComponentes() {
      
        
        
    	setTitle("Comprar Tiquetes");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);  // positions manuales


     // ============================
        // BARRA SUPERIOR
        // ============================
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(70, 130, 180)); // Azul más suave
        panelSuperior.setBounds(0, 0, 900, 50);
        panelSuperior.setLayout(null);
        add(panelSuperior);

        JLabel lblBienvenida = new JLabel("Comprador: " + comprador.getLogin());
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 13));
        lblBienvenida.setForeground(Color.WHITE);
        lblBienvenida.setBounds(20, 15, 250, 20);
        panelSuperior.add(lblBienvenida);

        lblSaldoActual = new JLabel("Saldo: $0.00");
        lblSaldoActual.setFont(new Font("Arial", Font.BOLD, 13));
        lblSaldoActual.setForeground(Color.WHITE);
        lblSaldoActual.setBounds(700, 15, 180, 20);
        panelSuperior.add(lblSaldoActual);
        actualizarSaldo();


        // ============================
        // EVENTOS
        // ============================
        JLabel lblEventos = new JLabel("Seleccione un evento:");
        lblEventos.setFont(new Font("Arial", Font.PLAIN, 13));
        lblEventos.setBounds(50, 70, 250, 20);
        add(lblEventos);

        modeloEventos = new DefaultListModel<>();
        listaEventos = new JList<>(modeloEventos);
        listaEventos.setFont(new Font("Arial", Font.PLAIN, 12));

        JScrollPane scrollEventos = new JScrollPane(listaEventos);
        scrollEventos.setBounds(50, 95, 600, 110);
        add(scrollEventos);


        // ============================
        // TIQUETES
        // ============================
        JLabel lblTiquetes = new JLabel("Tipos de tiquetes disponibles:");
        lblTiquetes.setFont(new Font("Arial", Font.PLAIN, 13));
        lblTiquetes.setBounds(50, 215, 450, 20);
        add(lblTiquetes);

        modeloTiquetes = new DefaultListModel<>();
        listaTiquetes = new JList<>(modeloTiquetes);
        listaTiquetes.setFont(new Font("Arial", Font.PLAIN, 12));
        listaTiquetes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollTiquetes = new JScrollPane(listaTiquetes);
        scrollTiquetes.setBounds(50, 240, 600, 110);
        add(scrollTiquetes);


        // ============================
        // CANTIDAD
        // ============================
        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setFont(new Font("Arial", Font.PLAIN, 13));
        lblCantidad.setBounds(50, 365, 100, 25);
        add(lblCantidad);

        spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 1, 1));
        spinnerCantidad.setFont(new Font("Arial", Font.PLAIN, 12));
        spinnerCantidad.setBounds(130, 365, 60, 25);
        ((JSpinner.DefaultEditor) spinnerCantidad.getEditor()).getTextField().setEditable(false);
        spinnerCantidad.setEnabled(false);
        add(spinnerCantidad);

        lblDisponibles = new JLabel("");
        lblDisponibles.setFont(new Font("Arial", Font.ITALIC, 11));
        lblDisponibles.setForeground(new Color(100, 100, 100));
        lblDisponibles.setBounds(200, 365, 200, 25);
        add(lblDisponibles);

        // ============================
        // INFORMACIÓN DE COMPRA
        // ============================
        JLabel lblInfoCompra = new JLabel("Resumen de compra:");
        lblInfoCompra.setFont(new Font("Arial", Font.BOLD, 13));
        lblInfoCompra.setBounds(50, 400, 200, 25);
        add(lblInfoCompra);

        lblTotalPagar = new JLabel("Total a pagar: $0.00");
        lblTotalPagar.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotalPagar.setForeground(new Color(46, 125, 50));
        lblTotalPagar.setBounds(50, 425, 250, 25);
        add(lblTotalPagar);


        // ============================
        // BOTONES
        // ============================
        btnComprar = new JButton("Comprar Tiquetes");
        btnComprar.setFont(new Font("Arial", Font.BOLD, 13));
        btnComprar.setBackground(new Color(46, 204, 113));
        btnComprar.setForeground(Color.WHITE);
        btnComprar.setBounds(50, 470, 180, 40);
        btnComprar.setEnabled(false);
        add(btnComprar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 13));
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setBounds(250, 470, 150, 40);
        add(btnCancelar);

        // ----------------------------
        // BOTÓN SALIR ABAJO IZQUIERDA
        // ----------------------------
        JButton btnSalir = new JButton("Salir");
        btnSalir.setFont(new Font("Arial", Font.BOLD, 12));
        btnSalir.setBounds(50, 560, 120, 35);
        add(btnSalir);

        btnSalir.addActionListener(e -> volverAlMenu());
        
        listaEventos.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    eventoSeleccionado();
                }
            }
        });

        // Listener: seleccionar tiquete -> habilitar spinner
        listaTiquetes.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    tiqueteSeleccionado();
                }
            }
        });

        // Listener: cambiar cantidad en spinner
        spinnerCantidad.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                actualizarTotal();
            }
        });

        // Listener: botón comprar
        btnComprar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarCompra();
            }
        });

        // Listener: botón cancelar
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    private void cargarEventos() {
        modeloEventos.clear();
        mapaEventos.clear();
        eventosDisponibles.clear();

        if (sistema == null || sistema.getEventos() == null) {
            modeloEventos.addElement("No hay eventos disponibles");
            return;
        }

        for (Evento evento : sistema.getEventos()) {
            // Solo mostrar eventos no cancelados con tiquetes disponibles
            if (!evento.getCancelado() && tieneTiquetesDisponibles(evento)) {
                eventosDisponibles.add(evento);
                
                String key = String.format("%-30s | %s | %s", 
                    truncar(evento.getNombre(), 30),
                    evento.getFecha(),
                    evento.getVenueAsociado().getUbicacion());
                
                modeloEventos.addElement(key);
                mapaEventos.put(key, evento);
            }
        }

        if (modeloEventos.isEmpty()) {
            modeloEventos.addElement("No hay eventos con tiquetes disponibles");
        }
    }

    /**
     * Verifica si un evento tiene tiquetes disponibles
     */
    private boolean tieneTiquetesDisponibles(Evento evento) {
        if (evento.getTiquetes() == null) return false;
        
        for (Tiquete t : evento.getTiquetesDisponibles()) {
            if (!t.isTransferido() && !t.isAnulado()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Maneja la selección de un evento
     */
    private void eventoSeleccionado() {
        String seleccion = listaEventos.getSelectedValue();
        
        if (seleccion == null || !mapaEventos.containsKey(seleccion)) {
            listaTiquetes.setEnabled(false);
            modeloTiquetes.clear();
            spinnerCantidad.setEnabled(false);
            btnComprar.setEnabled(false);
            lblDisponibles.setText("");
            lblTotalPagar.setText("Total a pagar: $0.00");
            return;
        }

        eventoActual = mapaEventos.get(seleccion);
        cargarTiquetesDeEvento(eventoActual);
    }

    private void cargarTiquetesDeEvento(Evento evento) {
        modeloTiquetes.clear();
        mapaTiquetesPorTipo.clear();

        if (evento.getTiquetes() == null || evento.getTiquetes().isEmpty()) {
            modeloTiquetes.addElement("No hay tiquetes disponibles");
            listaTiquetes.setEnabled(false);
            spinnerCantidad.setEnabled(false);
            lblDisponibles.setText("");
            return;
        }

        // Agrupar tiquetes por tipo/localidad/precio
        Map<String, List<Tiquete>> grupos = new HashMap<>();
        
        for (Tiquete t : evento.getTiquetesDisponibles()) {
            if (!t.isTransferido() && !t.isAnulado()) {
                // Validar que el tiquete tenga localidad
                if (t.getLocalidadAsociada() == null) {
                    System.err.println("Advertencia: Tiquete " + t.getId() + " sin localidad asociada");
                    continue;
                }
                
                // Clave única: Tipo + Localidad + Precio
                String clave = t.getTipoTiquete() + "|" + 
                              t.getLocalidadAsociada().getNombre() + "|" + 
                              t.getPrecioBaseSinCalcular();
                
                grupos.computeIfAbsent(clave, k -> new ArrayList<>()).add(t);
            }
        }

        if (grupos.isEmpty()) {
            modeloTiquetes.addElement("No hay tiquetes disponibles");
            listaTiquetes.setEnabled(false);
            spinnerCantidad.setEnabled(false);
            lblDisponibles.setText("");
            return;
        }

        // Mostrar grupos de tiquetes
        for (Map.Entry<String, List<Tiquete>> entry : grupos.entrySet()) {
            List<Tiquete> tiquetes = entry.getValue();
            Tiquete primero = tiquetes.get(0);
            
            // Validación adicional por seguridad
            if (primero.getLocalidadAsociada() == null) {
                continue;
            }
            
            String key = String.format("[%s] %-20s | $%.2f | Disponibles: %d",
                primero.getTipoTiquete(),
                truncar(primero.getLocalidadAsociada().getNombre(), 20),
                primero.getPrecioBaseSinCalcular(),
                tiquetes.size());
            
            modeloTiquetes.addElement(key);
            mapaTiquetesPorTipo.put(key, tiquetes);
        }

        listaTiquetes.setEnabled(true);
    }

    /**
     * Maneja la selección de un tipo de tiquete
     */
    private void tiqueteSeleccionado() {
        String seleccion = listaTiquetes.getSelectedValue();
        
        if (seleccion == null || !mapaTiquetesPorTipo.containsKey(seleccion)) {
            spinnerCantidad.setEnabled(false);
            spinnerCantidad.setValue(1);
            btnComprar.setEnabled(false);
            lblDisponibles.setText("");
            lblTotalPagar.setText("Total a pagar: $0.00");
            return;
        }

        tipoTiqueteSeleccionado = seleccion;
        List<Tiquete> disponibles = mapaTiquetesPorTipo.get(seleccion);
        int maxDisponibles = disponibles.size();

        // Configurar spinner con el máximo disponible
        spinnerCantidad.setModel(new SpinnerNumberModel(1, 1, maxDisponibles, 1));
        spinnerCantidad.setEnabled(true);
        spinnerCantidad.setValue(1);
        
        lblDisponibles.setText("(" + maxDisponibles + " disponibles)");
        
        actualizarTotal();
        btnComprar.setEnabled(true);
    }

    /**
     * Actualiza el total a pagar según la cantidad seleccionada
     */
    private void actualizarTotal() {
        if (tipoTiqueteSeleccionado == null || !mapaTiquetesPorTipo.containsKey(tipoTiqueteSeleccionado)) {
            lblTotalPagar.setText("Total a pagar: $0.00");
            return;
        }

        List<Tiquete> tiquetes = mapaTiquetesPorTipo.get(tipoTiqueteSeleccionado);
        if (tiquetes.isEmpty()) {
            lblTotalPagar.setText("Total a pagar: $0.00");
            return;
        }

        int cantidad = (int) spinnerCantidad.getValue();
        double precioUnitario = tiquetes.get(0).getPrecioBaseSinCalcular();
        double total = precioUnitario * cantidad;

        lblTotalPagar.setText(String.format("Total a pagar: $%.2f (%d x $%.2f)", 
            total, cantidad, precioUnitario));
    }

    /**
     * Realiza la compra de los tiquetes seleccionados
     */
    private void realizarCompra() {
        // Validar selección de evento
        String seleccionEvento = listaEventos.getSelectedValue();
        
        if (seleccionEvento == null || !mapaEventos.containsKey(seleccionEvento)) {
            JOptionPane.showMessageDialog(this,
                "Debe seleccionar un evento",
                "Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Validar selección de tiquete
        if (tipoTiqueteSeleccionado == null || !mapaTiquetesPorTipo.containsKey(tipoTiqueteSeleccionado)) {
            JOptionPane.showMessageDialog(this,
                "Debe seleccionar un tipo de tiquete",
                "Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Tiquete> tiquetesDisponibles = mapaTiquetesPorTipo.get(tipoTiqueteSeleccionado);
        int cantidad = (int) spinnerCantidad.getValue();
        
        if (cantidad > tiquetesDisponibles.size()) {
            JOptionPane.showMessageDialog(this,
                "No hay suficientes tiquetes disponibles",
                "Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Tiquete primerTiquete = tiquetesDisponibles.get(0);
        double precioUnitario = primerTiquete.getPrecioBaseSinCalcular();
        double precioTotal = precioUnitario * cantidad;
        
        // Validar que tenga localidad
        if (primerTiquete.getLocalidadAsociada() == null) {
            JOptionPane.showMessageDialog(this,
                "Error: Los tiquetes seleccionados no tienen localidad asignada",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Verificar saldo
        if (comprador instanceof IDuenoTiquetes) {
            IDuenoTiquetes dueno = (IDuenoTiquetes) comprador;
            if (precioTotal > dueno.getSaldo()) {
                JOptionPane.showMessageDialog(this,
                    String.format("Saldo insuficiente\n\n" +
                                 "Saldo actual: $%.2f\n" +
                                 "Total a pagar: $%.2f\n" +
                                 "Faltan: $%.2f",
                        dueno.getSaldo(), 
                        precioTotal,
                        precioTotal - dueno.getSaldo()),
                    "Saldo Insuficiente",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        // Confirmar compra
        int confirmacion = JOptionPane.showConfirmDialog(this,
            String.format("¿Confirmar compra?\n\n" +
                         "Evento: %s\n" +
                         "Tipo: %s\n" +
                         "Localidad: %s\n" +
                         "Cantidad: %d tiquetes\n" +
                         "Precio unitario: $%.2f\n" +
                         "TOTAL: $%.2f",
                         eventoActual.getNombre(),
                         primerTiquete.getTipoTiquete(),
                         primerTiquete.getLocalidadAsociada().getNombre(),
                         cantidad,
                         precioUnitario,
                         precioTotal),
            "Confirmar Compra",
            JOptionPane.YES_NO_OPTION);
        
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        // Realizar compra
        ArrayList<Tiquete> tiquetesComprados = new ArrayList<>();
        int compradosExitosos = 0;
        
        try {
            Transaccion trans = new Transaccion("NA", null, null, null, null, 0);
            
            // Comprar los primeros N tiquetes disponibles
            for (int i = 0; i < cantidad && i < tiquetesDisponibles.size(); i++) {
                try {
                    Tiquete tiquete = tiquetesDisponibles.get(i);
                    
                    // Validar antes de comprar
                    if (tiquete.getLocalidadAsociada() == null) {
                        System.err.println("Saltando tiquete sin localidad: " + tiquete.getId());
                        continue;
                    }
                    
                    ArrayList<Tiquete> comprados = trans.comprarTiquete(
                        tiquete,
                        comprador,
                        1,
                        eventoActual,
                        sistema.getAdministrador().getCobroEmision(),
                        sistema
                    );
                    tiquetesComprados.addAll(comprados);
                    compradosExitosos++;
                } catch (Exception e) {
                    System.err.println("Error comprando tiquete " + (i+1) + ": " + e.getMessage());
                }
            }

            // Actualizar saldo
            actualizarSaldo();

            // Mostrar resultado
            if (compradosExitosos > 0) {
                StringBuilder mensaje = new StringBuilder();
                mensaje.append("✓ Compra realizada exitosamente!\n\n");
                mensaje.append("Tiquetes comprados: ").append(compradosExitosos).append("\n");
                mensaje.append("Total pagado: $").append(String.format("%.2f", precioUnitario * compradosExitosos)).append("\n");
                
                if (comprador instanceof IDuenoTiquetes) {
                    mensaje.append("Nuevo saldo: $").append(
                        String.format("%.2f", ((IDuenoTiquetes)comprador).getSaldo())
                    ).append("\n\n");
                }
                
                mensaje.append("IDs de tiquetes:\n");
                for (Tiquete t : tiquetesComprados) {
                    mensaje.append("• ").append(t.getId()).append("\n");
                }
                
                JOptionPane.showMessageDialog(this,
                    mensaje.toString(),
                    "Compra Exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Mostrar QR del primer tiquete
                if (!tiquetesComprados.isEmpty()) {
                    Tiquete primerComprado = tiquetesComprados.get(0);
                    BufferedImage qrImagen = GeneradorQR.generarQR(primerComprado, 250);
                    if (qrImagen != null) {
                        JLabel labelQR = new JLabel(new ImageIcon(qrImagen));
                        JOptionPane.showMessageDialog(this, 
                            labelQR, 
                            "QR del primer tiquete - " + primerComprado.getId(), 
                            JOptionPane.PLAIN_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this,
                    "No se pudo completar la compra",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }

            // Recargar
            cargarEventos();
            modeloTiquetes.clear();
            listaTiquetes.setEnabled(false);
            spinnerCantidad.setEnabled(false);
            spinnerCantidad.setValue(1);
            btnComprar.setEnabled(false);
            lblDisponibles.setText("");
            lblTotalPagar.setText("Total a pagar: $0.00");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al realizar la compra:\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Actualiza el label del saldo
     */
    private void actualizarSaldo() {
        if (comprador instanceof IDuenoTiquetes) {
            IDuenoTiquetes dueno = (IDuenoTiquetes) comprador;
            lblSaldoActual.setText(String.format("Saldo: $%.2f", dueno.getSaldo()));
        }
    }

    /**
     * Trunca un texto a una longitud máxima
     */
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
        	 new ventanaMenuComprador((Cliente) comprador ,sistema).setVisible(true);
        }
             
    }

    
}