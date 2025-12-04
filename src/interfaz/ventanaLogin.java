package interfaz;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ventanaLogin extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField textField;
    private JPasswordField passwordField;
    private JButton btnNewButton_1;
    private JButton btnNewButton;

    public ventanaLogin() {
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
        

       
        setTitle("BOLETAMASTER: Iniciar Sesion");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(399, 351);
        setVisible(true);
    }

    public static void main(String[] args) {
        new ventanaLogin();
    }
}