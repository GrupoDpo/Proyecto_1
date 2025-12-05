package interfaz;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Evento.Evento;
import Finanzas.Transaccion;
import Persistencia.SistemaPersistencia;
import excepciones.SaldoInsuficienteExeption;
import excepciones.TiquetesNoDisponiblesException;
import tiquete.Tiquete;
import usuario.IDuenoTiquetes;
import usuario.Usuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ventanaComprarTiquete extends JFrame {
    private static final long serialVersionUID = 1L;

    private JList<String> listaEventos;
    private JList<String> listaTiquetes;
    private DefaultListModel<String> modeloEventos;
    private DefaultListModel<String> modeloTiquetes;
    
    private JLabel lblSaldoActual;
    private JSpinner spinnerCantidad;
    private JButton btnComprar;
    private JButton btnCancelar;

    // Modelo
    private SistemaPersistencia sistema;
    private Usuario comprador;
    private List<Evento> eventosDisponibles;
    private List<Tiquete> tiquetesDisponibles;
    private Map<String, Evento> mapaEventos;
    private Map<String, Tiquete> mapaTiquetes;

    public ventanaComprarTiquete(SistemaPersistencia sistema, Usuario comprador) {
    	
    	this.comprador = comprador;
        this.sistema = sistema;
        this.eventosDisponibles = new ArrayList<>();
        this.tiquetesDisponibles = new ArrayList<>();
        this.mapaEventos = new HashMap<>();
        this.mapaTiquetes = new HashMap<>();
        
        inicializarComponentes();
        cargarEventos();
        
    }
    private void inicializarComponentes() {
      
        
        
        setTitle("BOLETAMASTER: Comprar Tiquetes");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setLayout(null);

     // ========================================
        // PANEL SUPERIOR - Información del usuario
        // ========================================
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(52, 152, 219));
        panelSuperior.setBounds(0, 0, 600, 50);
        panelSuperior.setLayout(null);
        getContentPane().add(panelSuperior);

        JLabel lblBienvenida = new JLabel("Comprador: " + comprador.getLogin());
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 14));
        lblBienvenida.setForeground(Color.WHITE);
        lblBienvenida.setBounds(20, 15, 300, 20);
        panelSuperior.add(lblBienvenida);

        lblSaldoActual = new JLabel("Saldo: $0.00");
        lblSaldoActual.setFont(new Font("Arial", Font.BOLD, 14));
        lblSaldoActual.setForeground(Color.WHITE);
        lblSaldoActual.setBounds(400, 15, 180, 20);
        panelSuperior.add(lblSaldoActual);
        actualizarSaldo();

        // ========================================
        // EVENTOS
        // ========================================
        JLabel lblEventos = new JLabel("Escoja el evento de su interés:");
        lblEventos.setFont(new Font("Arial", Font.BOLD, 12));
        lblEventos.setBounds(50, 65, 250, 20);
        getContentPane().add(lblEventos);

        // Lista scrolleable de eventos
        modeloEventos = new DefaultListModel<>();
        listaEventos = new JList<>(modeloEventos);
        listaEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaEventos.setFont(new Font("Monospaced", Font.PLAIN, 11));

        JScrollPane scrollEventos = new JScrollPane(listaEventos);
        scrollEventos.setBounds(50, 90, 500, 100);
        getContentPane().add(scrollEventos);
        
     // ========================================
        // TIQUETES
        // ========================================
        JLabel lblTiquetes = new JLabel("Tiquetes asociados al evento:");
        lblTiquetes.setFont(new Font("Arial", Font.BOLD, 12));
        lblTiquetes.setBounds(50, 205, 250, 20);
        getContentPane().add(lblTiquetes);

        // Lista de tiquetes
        modeloTiquetes = new DefaultListModel<>();
        listaTiquetes = new JList<>(modeloTiquetes);
        listaTiquetes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaTiquetes.setFont(new Font("Monospaced", Font.PLAIN, 11));
        listaTiquetes.setEnabled(false);

        JScrollPane scrollTiquetes = new JScrollPane(listaTiquetes);
        scrollTiquetes.setBounds(50, 230, 500, 100);
        getContentPane().add(scrollTiquetes);

        // ========================================
        // CANTIDAD Y BOTONES
        // ========================================
        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setFont(new Font("Arial", Font.BOLD, 12));
        lblCantidad.setBounds(50, 350, 80, 25);
        getContentPane().add(lblCantidad);

        spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        spinnerCantidad.setFont(new Font("Arial", Font.PLAIN, 12));
        spinnerCantidad.setBounds(140, 350, 60, 25);
        ((JSpinner.DefaultEditor) spinnerCantidad.getEditor()).getTextField().setEditable(false);
        getContentPane().add(spinnerCantidad);

        btnComprar = new JButton("COMPRAR");
        btnComprar.setFont(new Font("Arial", Font.BOLD, 13));
        btnComprar.setBackground(new Color(46, 204, 113));
        btnComprar.setForeground(Color.WHITE);
        btnComprar.setFocusPainted(false);
        btnComprar.setBounds(250, 400, 140, 35);
        btnComprar.setEnabled(false);
        getContentPane().add(btnComprar);

        btnCancelar = new JButton("CANCELAR");
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 13));
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBounds(410, 400, 140, 35);
        getContentPane().add(btnCancelar);
        
        listaEventos.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    eventoSeleccionado();
                }
            }
        });

        // Listener: seleccionar tiquete -> habilitar compra
        listaTiquetes.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    tiqueteSeleccionado();
                }
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
                    evento.getVenueAsociado());
                
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
            btnComprar.setEnabled(false);
            return;
        }

        Evento evento = mapaEventos.get(seleccion);
        cargarTiquetesDeEvento(evento);
    }

    private void cargarTiquetesDeEvento(Evento evento) {
        modeloTiquetes.clear();
        mapaTiquetes.clear();
        tiquetesDisponibles.clear();

        if (evento.getTiquetes() == null || evento.getTiquetes().isEmpty()) {
            modeloTiquetes.addElement("No hay tiquetes disponibles");
            listaTiquetes.setEnabled(false);
            return;
        }

        // Mostrar CADA tiquete individualmente
        int contador = 1;
        for (Tiquete t : evento.getTiquetesDisponibles()) {
            if (!t.isTransferido() && !t.isAnulado()) {
                tiquetesDisponibles.add(t);
                
                // Formato: [TIPO] ID | Localidad | Precio
                String key = String.format("#%-3d [%s] %-12s | %-20s | $%.2f",
                    contador++,
                    t.getTipoTiquete(),
                    truncar(t.getId(), 12),
                    truncar(t.getLocalidadAsociada().getNombre(), 20),
                    t.getPrecioBaseSinCalcular());
                
                modeloTiquetes.addElement(key);
                mapaTiquetes.put(key, t);
            }
        }

        if (tiquetesDisponibles.isEmpty()) {
            modeloTiquetes.addElement("No hay tiquetes disponibles");
            listaTiquetes.setEnabled(false);
            return;
        }

        listaTiquetes.setEnabled(true);
    }

    /**
     * Maneja la selección de un tiquete
     */
    private void tiqueteSeleccionado() {
        String seleccion = listaTiquetes.getSelectedValue();
        
        if (seleccion == null || !mapaTiquetes.containsKey(seleccion)) {
            btnComprar.setEnabled(false);
            return;
        }

        // Habilitar compra de 1 tiquete
        btnComprar.setEnabled(true);
    }

    /**
     * Realiza la compra del tiquete seleccionado
     */
    private void realizarCompra() {
        // Validar selecciones
        String seleccionEvento = listaEventos.getSelectedValue();
        String seleccionTiquete = listaTiquetes.getSelectedValue();
        
        if (seleccionEvento == null || !mapaEventos.containsKey(seleccionEvento)) {
            JOptionPane.showMessageDialog(this,
                "Debe seleccionar un evento",
                "Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (seleccionTiquete == null || !mapaTiquetes.containsKey(seleccionTiquete)) {
            JOptionPane.showMessageDialog(this,
                "Debe seleccionar un tiquete",
                "Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Evento evento = mapaEventos.get(seleccionEvento);
        Tiquete tiquete = mapaTiquetes.get(seleccionTiquete);
        
        // Precio del tiquete
        double precio = tiquete.getPrecioBaseSinCalcular();
        
        // Verificar saldo
        if (comprador instanceof IDuenoTiquetes) {
            IDuenoTiquetes dueno = (IDuenoTiquetes) comprador;
            if (precio > dueno.getSaldo()) {
                JOptionPane.showMessageDialog(this,
                    String.format("Saldo insuficiente\n\nSaldo actual: $%.2f\nPrecio del tiquete: $%.2f",
                        dueno.getSaldo(), precio),
                    "Saldo Insuficiente",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        // Confirmar compra
        int confirmacion = JOptionPane.showConfirmDialog(this,
            String.format("¿Confirmar compra del tiquete?\n\n" +
                         "ID: %s\n" +
                         "Evento: %s\n" +
                         "Localidad: %s\n" +
                         "Tipo: %s\n" +
                         "Precio: $%.2f",
                         tiquete.getId(),
                         evento.getNombre(), 
                         tiquete.getLocalidadAsociada(),
                         tiquete.getTipoTiquete(),
                         precio),
            "Confirmar Compra",
            JOptionPane.YES_NO_OPTION);
        
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }
        int cantidad = (int) spinnerCantidad.getValue();

        // Realizar compra de 1 tiquete
        try {
        	Transaccion trans = new Transaccion("NA", null, null, null, null, 0);
            
            ArrayList<Tiquete> comprados = trans.comprarTiquete(tiquete,comprador,cantidad,
                evento,
                sistema.getAdministrador().getCobroEmision(),
                sistema
                
            );

            // Actualizar saldo
            actualizarSaldo();

            // Mostrar éxito con información del tiquete comprado
            Tiquete tiqueteComprado = comprados.get(0);
            
            JOptionPane.showMessageDialog(this,
                String.format("✓ Compra realizada exitosamente!\n\n" +
                             "ID del tiquete: %s\n" +
                             "Localidad: %s\n" +
                             "Tipo: %s\n" +
                             "Precio pagado: $%.2f\n" +
                             "Nuevo saldo: $%.2f",
                             tiqueteComprado.getId(),
                             tiqueteComprado.getLocalidadAsociada(),
                             tiqueteComprado.getTipoTiquete(),
                             precio,
                             ((IDuenoTiquetes)comprador).getSaldo()),
                "Compra Exitosa",
                JOptionPane.INFORMATION_MESSAGE);

            // Recargar eventos y limpiar selección
            cargarEventos();
            modeloTiquetes.clear();
            listaTiquetes.setEnabled(false);
            btnComprar.setEnabled(false);

        } catch (SaldoInsuficienteExeption e) {
            JOptionPane.showMessageDialog(this,
                "Saldo insuficiente\n\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (TiquetesNoDisponiblesException e) {
            JOptionPane.showMessageDialog(this,
                "Tiquete no disponible\n\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            // Recargar para actualizar disponibilidad
            cargarEventos();
            modeloTiquetes.clear();
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

    
}
