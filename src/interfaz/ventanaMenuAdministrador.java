package interfaz;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;




public class ventanaMenuAdministrador extends JFrame {
	private static final long serialVersionUID = 1L;
    private JButton btnNewButton_1;
    
    

    public ventanaMenuAdministrador() {
        ArrayList<String> tiposUsuario = new ArrayList<String>();
    	
    	tiposUsuario.add("Cliente");
    	tiposUsuario.add("Promotor");
    	tiposUsuario.add("Organizador");
    	tiposUsuario.add("Administrador");
        getContentPane().setLayout(null);
        
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 400, 10);
        getContentPane().add(panel);
        
        btnNewButton_1 = new JButton("Crear Venue");
        btnNewButton_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        btnNewButton_1.setBounds(157, 55, 148, 26);
        getContentPane().add(btnNewButton_1);
        
        JLabel lblNewLabel = new JLabel("Bienvenido");
        lblNewLabel.setBounds(196, 11, 62, 33);
        getContentPane().add(lblNewLabel);
        
        JButton btnNewButton_1_1 = new JButton("Revisar Solicitudes de Venue");
        btnNewButton_1_1.setBounds(134, 92, 187, 26);
        getContentPane().add(btnNewButton_1_1);
        
        JButton btnNewButton_1_2 = new JButton("Ver rembolsos");
        btnNewButton_1_2.setBounds(157, 129, 148, 26);
        getContentPane().add(btnNewButton_1_2);
        
        JButton btnNewButton_1_2_1 = new JButton("Ver reventas");
        btnNewButton_1_2_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        btnNewButton_1_2_1.setBounds(157, 166, 148, 26);
        getContentPane().add(btnNewButton_1_2_1);
        
        JButton btnNewButton_1_2_2 = new JButton("Cancelar evento");
        btnNewButton_1_2_2.setBounds(157, 203, 148, 26);
        btnNewButton_1_2_2.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        getContentPane().add(btnNewButton_1_2_2);
        
        JButton btnNewButton_1_2_3 = new JButton("Gestionar Solicitudes de Cancelar evento");
        btnNewButton_1_2_3.setBounds(145, 240, 176, 26);
        btnNewButton_1_2_3.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        getContentPane().add(btnNewButton_1_2_3);
        
        JButton btnNewButton_1_2_4 = new JButton("Fijar cobro de emision");
        btnNewButton_1_2_4.setBounds(157, 277, 148, 26);
        getContentPane().add(btnNewButton_1_2_4);
        
        JButton btnNewButton_1_2_4_1 = new JButton("Fijar recargo");
        btnNewButton_1_2_4_1.setBounds(157, 314, 148, 26);
        getContentPane().add(btnNewButton_1_2_4_1);

       
        setTitle("BOLETAMASTER: Menu Promotor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(449, 400);
        setVisible(true);
    }

    public static void main(String[] args) {
        new ventanaMenuComprador();
    }
}


