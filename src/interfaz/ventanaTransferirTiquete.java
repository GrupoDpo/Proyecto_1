package interfaz;

import java.awt.*;
import javax.swing.*;
import java.util.Collection;
import java.util.HashMap;

import Persistencia.SistemaPersistencia;
import Finanzas.Transaccion;
import usuario.Cliente;
import usuario.IDuenoTiquetes;
import usuario.Organizador;
import usuario.Promotor;
import usuario.Usuario;
import tiquete.Tiquete;
import tiquete.TiqueteMultiple;
import excepciones.TiquetesVencidosTransferidos;
import excepciones.IDNoEncontrado;
import excepciones.TransferenciaNoPermitidaException;

public class ventanaTransferirTiquete extends JFrame {
    private static final long serialVersionUID = 1L;

    // Componentes UI
    private JList<String> listaTiquetes;
    private DefaultListModel<String> modeloLista;
    private JTextArea areaDetalle;
    private JTextField txtLoginDestino;
    private JPasswordField txtPassword;
    private JButton btnTransferir;
    private JButton btnCancelar;

    // Modelo
    private IDuenoTiquetes usuario;
    private SistemaPersistencia sistema;
    private Transaccion trans;
    private HashMap<Integer, Tiquete> mapaTiquetes;
    private Tiquete tiqueteSeleccionado;

    public ventanaTransferirTiquete(IDuenoTiquetes usuario, SistemaPersistencia sistema, Transaccion trans) {
        this.usuario = usuario;
        this.sistema = sistema;
        this.trans = trans;
        this.mapaTiquetes = new HashMap<>();

        initComponents();
        cargarTiquetes();
    }

    private void initComponents() {
        setTitle("BOLETAMASTER: Transferir Tiquete");
        getContentPane().setLayout(null);
        setSize(600, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Título
        JLabel lblTitulo = new JLabel("TRANSFERIR TIQUETE");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setBounds(20, 10, 300, 25);
        getContentPane().add(lblTitulo);

        // Panel de tiquetes
        JLabel lblTiquetes = new JLabel("Selecciona el tiquete a transferir:");
        lblTiquetes.setBounds(20, 45, 300, 20);
        getContentPane().add(lblTiquetes);

        modeloLista = new DefaultListModel<>();
        listaTiquetes = new JList<>(modeloLista);
        listaTiquetes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaTiquetes.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarDetalleTiquete();
            }
        });

        JScrollPane scrollTiquetes = new JScrollPane(listaTiquetes);
        scrollTiquetes.setBounds(20, 70, 550, 120);
        getContentPane().add(scrollTiquetes);

        // Área de detalle
        JLabel lblDetalle = new JLabel("Detalle del tiquete:");
        lblDetalle.setBounds(20, 200, 200, 20);
        getContentPane().add(lblDetalle);

        areaDetalle = new JTextArea();
        areaDetalle.setEditable(false);
        areaDetalle.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaDetalle.setBackground(new Color(240, 240, 240));
        
        JScrollPane scrollDetalle = new JScrollPane(areaDetalle);
        scrollDetalle.setBounds(20, 225, 550, 120);
        getContentPane().add(scrollDetalle);

        // Login destino
        JLabel lblDestino = new JLabel("Login del usuario destino:");
        lblDestino.setBounds(20, 360, 200, 20);
        getContentPane().add(lblDestino);

        txtLoginDestino = new JTextField();
        txtLoginDestino.setBounds(220, 360, 200, 25);
        getContentPane().add(txtLoginDestino);

        // Password
        JLabel lblPassword = new JLabel("Tu password (confirmar):");
        lblPassword.setBounds(20, 395, 200, 20);
        getContentPane().add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(220, 395, 200, 25);
        getContentPane().add(txtPassword);

        // Botones
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(20, 450, 120, 30);
        btnCancelar.addActionListener(e -> volverAlMenu());
        getContentPane().add(btnCancelar);

        btnTransferir = new JButton("Transferir");
        btnTransferir.setBounds(458, 450, 120, 30);
        btnTransferir.setEnabled(false);
        btnTransferir.addActionListener(e -> realizarTransferencia());
        getContentPane().add(btnTransferir);

        setVisible(true);
    }

    private void cargarTiquetes() {
        modeloLista.clear();
        mapaTiquetes.clear();

        Collection<Tiquete> misTiquetes = usuario.getTiquetes();

        if (misTiquetes == null || misTiquetes.isEmpty()) {
            modeloLista.addElement("No tienes tiquetes para transferir");
            areaDetalle.setText("No hay tiquetes disponibles.");
            return;
        }

        int num = 1;
        for (Tiquete t : misTiquetes) {
            String tipo = t instanceof TiqueteMultiple ? "PAQUETE" : "SIMPLE";
            String evento = t.getEvento() != null ? t.getEvento().getNombre() : "Sin evento";

            String linea = num + ". [" + tipo + "] " + t.getNombre() + 
                          " | ID: " + t.getId() + " | Evento: " + evento;

            modeloLista.addElement(linea);
            mapaTiquetes.put(num - 1, t); // índice de lista
            num++;
        }
    }

    private void mostrarDetalleTiquete() {
        int index = listaTiquetes.getSelectedIndex();
        
        if (index < 0 || !mapaTiquetes.containsKey(index)) {
            areaDetalle.setText("");
            btnTransferir.setEnabled(false);
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

        if (tiqueteSeleccionado instanceof TiqueteMultiple) {
            TiqueteMultiple tm = (TiqueteMultiple) tiqueteSeleccionado;
            detalle.append("Incluye ").append(tm.getTiquetes().size()).append(" tiquetes\n");
            detalle.append("\nNOTA: Se transferirá el paquete completo.\n");
        }

        detalle.append("===========================");

        areaDetalle.setText(detalle.toString());
        btnTransferir.setEnabled(true);
    }

    private void realizarTransferencia() {
        // Validar que hay un tiquete seleccionado
        if (tiqueteSeleccionado == null) {
            JOptionPane.showMessageDialog(this,
                    "Debes seleccionar un tiquete.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar login destino
        String loginDestino = txtLoginDestino.getText().trim();
        if (loginDestino.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debes ingresar el login del usuario destino.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Buscar usuario destino
        Usuario destino = sistema.buscarUsuario(loginDestino);
        if (destino == null) {
            JOptionPane.showMessageDialog(this,
                    "Usuario no encontrado.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!(destino instanceof IDuenoTiquetes)) {
            JOptionPane.showMessageDialog(this,
                    "El usuario destino no puede recibir tiquetes.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (destino.getLogin().equals(((Usuario)usuario).getLogin())) {
            JOptionPane.showMessageDialog(this,
                    "No puedes transferir a ti mismo.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar password
        String password = new String(txtPassword.getPassword());
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debes ingresar tu password para confirmar.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!((Usuario)usuario).IsPasswordTrue(password)) {
            JOptionPane.showMessageDialog(this,
                    "Password incorrecta.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Confirmación final
        String mensaje = "=== CONFIRMAR TRANSFERENCIA ===\n" +
                        "Tiquete: " + tiqueteSeleccionado.getNombre() + " (" + tiqueteSeleccionado.getId() + ")\n" +
                        "De: " + ((Usuario)usuario).getLogin() + "\n" +
                        "A: " + destino.getLogin() + "\n";

        if (tiqueteSeleccionado instanceof TiqueteMultiple) {
            mensaje += "\nNOTA: Se transferirá el paquete completo.\n";
        }

        mensaje += "\n¿Confirmar transferencia?";

        int confirmacion = JOptionPane.showConfirmDialog(this,
                mensaje,
                "Confirmar Transferencia",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirmacion != JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this,
                    "Transferencia cancelada.",
                    "Cancelado",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Realizar transferencia
        try {
            String fechaActual = java.time.LocalDate.now().toString();

            trans.transferirTiquete(
                tiqueteSeleccionado,
                (Usuario) usuario,
                destino,
                fechaActual,
                sistema
            );

            // ✅ IMPORTANTE: Guardar toda la persistencia después de la transferencia
            sistema.guardarTodo();

            JOptionPane.showMessageDialog(this,
                    "================================\n" +
                    "✓ TRANSFERENCIA EXITOSA\n" +
                    "================================\n" +
                    "Tiquete transferido correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

            // Recargar lista de tiquetes
            cargarTiquetes();
            txtLoginDestino.setText("");
            txtPassword.setText("");
            areaDetalle.setText("");
            tiqueteSeleccionado = null;

        } catch (TiquetesVencidosTransferidos e) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage(),
                    "Tiquete Vencido",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IDNoEncontrado e) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage(),
                    "ID No Encontrado",
                    JOptionPane.ERROR_MESSAGE);
        } catch (TransferenciaNoPermitidaException e) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage(),
                    "Transferencia No Permitida",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error inesperado: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    	private void volverAlMenu() {
        
    	
    	dispose();
    	
        if (usuario instanceof Promotor) {
            new ventanaMenuPromotor((Promotor) usuario, sistema).setVisible(true);
        } else if (usuario instanceof Organizador) {
            new ventanaMenuOrganizador((Organizador) usuario, sistema).setVisible(true);
        } else if (usuario instanceof Cliente) {
        	 new ventanaMenuComprador((Cliente) usuario ,sistema).setVisible(true);
        }
             
    }
}