package interfaz;

import java.awt.*;
import javax.swing.*;

import Persistencia.SistemaPersistencia;
import usuario.Administrador;
import usuario.Cliente;
import usuario.Organizador;
import usuario.Promotor;
import usuario.Usuario;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;




public class ventanaRegistro extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextField txtLogin;
    private JPasswordField passwordField;
    private JButton btnRegistrar;
    private JButton btnSalir;
    private JList<String> list;
    
    
    private SistemaPersistencia sistema;

    public ventanaRegistro(SistemaPersistencia sistema) {
        this.sistema = sistema;
        ArrayList<String> tiposUsuario = new ArrayList<String>();
    	
    	tiposUsuario.add("CLIENTE");
    	tiposUsuario.add("PROMOTOR");
    	tiposUsuario.add("ORGANIZADOR");
    	tiposUsuario.add("ADMINISTRADOR");
    	
    	getContentPane().setLayout(null);
        
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 400, 10);
        getContentPane().add(panel);
        
        txtLogin = new JTextField();
        txtLogin.setBounds(167, 55, 134, 26);
        getContentPane().add(txtLogin);
        txtLogin.setColumns(10);
        
        passwordField = new JPasswordField();
        passwordField.setBounds(167, 93, 134, 26);
        getContentPane().add(passwordField);
        
        
        
        JLabel lblLogin = new JLabel("Login");
        lblLogin.setBounds(97, 60, 61, 16);
        getContentPane().add(lblLogin);
        
        JLabel lblContrasea = new JLabel("Contraseña");
        lblContrasea.setBounds(79, 98, 79, 16);
        getContentPane().add(lblContrasea);

        list = new JList<>(tiposUsuario.toArray(new String[0]));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // SOLO UNA OPCIÓN
        list.setSelectedIndex(0); 
        list.setVisibleRowCount(3);

        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setBounds(142, 143, 108, 98);
        getContentPane().add(scrollPane);

        JLabel lblTipoUsuario = new JLabel("Tipo de Usuario");
        lblTipoUsuario.setBounds(30, 178, 100, 16);
        getContentPane().add(lblTipoUsuario);
        
        
        btnRegistrar = new JButton("Registrar Usuario");
        btnRegistrar.setBounds(252, 291, 148, 26);
        getContentPane().add(btnRegistrar);
        
        btnSalir = new JButton("Salir");
        btnSalir.setBounds(6, 291, 79, 26);
        getContentPane().add(btnSalir);
        
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarUsuario();
            }
        });

        // ⭐ FUNCIONALIDAD: Volver al Login
        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                volverAlLogin();
            }
        });
        setTitle("BOLETAMASTER: Registrar Usuario");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(399, 351);
        setVisible(true);
    }
        
        private void registrarUsuario() {
            String login = txtLogin.getText().trim();
            String password = new String(passwordField.getPassword());
            String tipoUsuario = list.getSelectedValue();

            // ===== VALIDACIONES =====
            if (login.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Por favor ingrese un login",
                    "Campo vacío",
                    JOptionPane.WARNING_MESSAGE);
                txtLogin.requestFocus();
                return;
            }


            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Por favor ingrese una contraseña",
                    "Campo vacío",
                    JOptionPane.WARNING_MESSAGE);
                passwordField.requestFocus();
                return;
            }

            if (tipoUsuario == null) {
                JOptionPane.showMessageDialog(this,
                    "Por favor seleccione un tipo de usuario",
                    "Selección requerida",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Verificar si el login ya existe
            if (sistema.buscarUsuario(login) != null) {
                JOptionPane.showMessageDialog(this,
                    "El login '" + login + "' ya está en uso",
                    "Login duplicado",
                    JOptionPane.ERROR_MESSAGE);
                txtLogin.selectAll();
                txtLogin.requestFocus();
                return;
            }

            // ===== CREAR USUARIO SEGÚN TIPO =====
            Usuario nuevoUsuario = null;

            try {
                switch (tipoUsuario) {
                    case "CLIENTE":
                        nuevoUsuario = new Cliente(login, password,0,tipoUsuario);
                        break;
                        
                    case "PROMOTOR":
                        nuevoUsuario = new Promotor(login, password,0,tipoUsuario);
                        break;
                        
                    case "ORGANIZADOR":
                        nuevoUsuario = new Organizador(login, password,0, tipoUsuario);
                        break;
                    case "ADMINISTRADOR":
                        nuevoUsuario = new Administrador(login, password,tipoUsuario);
                        break;
                        
                    default:
                        JOptionPane.showMessageDialog(this,
                            "Tipo de usuario no válido",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                }

                // Agregar usuario al sistema
                sistema.agregarUsuario(nuevoUsuario);
                sistema.guardarTodo();

                // Mensaje de éxito
                JOptionPane.showMessageDialog(this,
                    "Usuario '" + login + "' registrado exitosamente como " + tipoUsuario,
                    "Registro exitoso",
                    JOptionPane.INFORMATION_MESSAGE);

                // Limpiar campos
                limpiarCampos();

                // Preguntar si quiere iniciar sesión
                int opcion = JOptionPane.showConfirmDialog(this,
                    "¿Desea iniciar sesión ahora?",
                    "Iniciar sesión",
                    JOptionPane.YES_NO_OPTION);

                if (opcion == JOptionPane.YES_OPTION) {
                    volverAlLogin();
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Error al registrar usuario: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }

        // ⭐ MÉTODO: Limpiar campos
        private void limpiarCampos() {
            txtLogin.setText("");
            passwordField.setText("");
            list.setSelectedIndex(0);
            txtLogin.requestFocus();
        }
        
        private void volverAlLogin() {
            dispose();
            ventanaLogin login = new ventanaLogin(sistema);
            login.setVisible(true);
        }



        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                SistemaPersistencia sistema = new SistemaPersistencia();
                sistema.cargarTodo();
                
                ventanaRegistro registro = new ventanaRegistro(sistema);
                registro.setVisible(true);
            });
        }
}