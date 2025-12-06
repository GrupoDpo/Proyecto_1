package interfaz;

import java.awt.*;
import javax.swing.*;

import Finanzas.Transaccion;
import Persistencia.SistemaPersistencia;
import usuario.Administrador;
import usuario.Cliente;
import usuario.Organizador;
import usuario.Promotor;
import usuario.Usuario;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ventanaLogin extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField textField;
    private JPasswordField passwordField;
    private JButton btnNewButton_1;
    private JButton btnNewButton;
    
    private SistemaPersistencia sistema;

    public ventanaLogin(SistemaPersistencia sistema) {
    	
    	this.sistema = sistema;
        getContentPane().setLayout(null);
        
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 400, 10);
        getContentPane().add(panel);
        
        textField = new JTextField();
        textField.setBounds(138, 115, 134, 26);
        getContentPane().add(textField);
        textField.setColumns(10);
        
        passwordField = new JPasswordField();
        passwordField.setBounds(138, 153, 134, 26);
        getContentPane().add(passwordField);
        
        btnNewButton_1 = new JButton("Registrar");
        btnNewButton_1.setBounds(155, 218, 98, 26);
        getContentPane().add(btnNewButton_1);
        
        btnNewButton = new JButton("Iniciar Sesion");
        btnNewButton.setBounds(145, 191, 117, 26);
        getContentPane().add(btnNewButton);
        
        btnNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarSesion();
            }
        });

        // ⭐ FUNCIONALIDAD: Ir a Registro
        btnNewButton_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirVentanaRegistro();
            }
        });
        

       
        setTitle("BOLETAMASTER: Iniciar Sesion");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(399, 351);
        setVisible(true);
    }
        
        private void iniciarSesion() {
            String login = textField.getText().trim();
            String password = new String(passwordField.getPassword());

            // Validación de campos vacíos
            if (login.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Por favor ingrese su login",
                    "Campo vacío",
                    JOptionPane.WARNING_MESSAGE);
                textField.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Por favor ingrese su contraseña",
                    "Campo vacío",
                    JOptionPane.WARNING_MESSAGE);
                passwordField.requestFocus();
                return;
            }

            // Buscar usuario en el sistema
            Usuario usuario = sistema.buscarUsuario(login);

            if (usuario == null) {
                JOptionPane.showMessageDialog(this,
                    "Usuario no encontrado",
                    "Error de autenticación",
                    JOptionPane.ERROR_MESSAGE);
                textField.selectAll();
                textField.requestFocus();
                return;
            }

            // Verificar contraseña
            if (!usuario.getPasswordInternal().equals(password)) {
                JOptionPane.showMessageDialog(this,
                    "Contraseña incorrecta",
                    "Error de autenticación",
                    JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
                passwordField.requestFocus();
                return;
            }

            // ✅ Login exitoso
            JOptionPane.showMessageDialog(this,
                "¡Bienvenido, " + usuario.getLogin() + "!",
                "Inicio de sesión exitoso",
                JOptionPane.INFORMATION_MESSAGE);

            // Abrir ventana principal según el rol del usuario
            if (usuario instanceof Promotor) {
                new ventanaMenuPromotor((Promotor) usuario,sistema).setVisible(true);
            } else if (usuario instanceof Organizador) {
                new ventanaMenuOrganizador((Organizador) usuario,sistema).setVisible(true);
            } else if (usuario instanceof Administrador) {
                new ventanaMenuAdministrador((Administrador) usuario,sistema).setVisible(true);
            } else if (usuario instanceof Cliente) {
            	 new ventanaMenuComprador((Cliente) usuario,sistema).setVisible(true);
            }

            dispose();
        }
        
        private void abrirVentanaRegistro() {
            dispose();
            ventanaRegistro registro = new ventanaRegistro(sistema);
            registro.setVisible(true);
        }
    
    

  


    
    

	public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        // Cargar sistema de persistencia
        SistemaPersistencia sistema = new SistemaPersistencia();
        sistema.cargarTodo();
        
        ventanaLogin login = new ventanaLogin(sistema);
        login.setVisible(true);
    });
	
	
}
}