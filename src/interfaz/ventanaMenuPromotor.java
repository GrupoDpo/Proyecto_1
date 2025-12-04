package interfaz;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;




public class ventanaMenuPromotor extends JFrame {
	private static final long serialVersionUID = 1L;
    private JButton btnNewButton_1;
    
    

    public ventanaMenuPromotor() {
        ArrayList<String> tiposUsuario = new ArrayList<String>();
    	
    	tiposUsuario.add("Cliente");
    	tiposUsuario.add("Promotor");
    	tiposUsuario.add("Organizador");
    	tiposUsuario.add("Administrador");
        getContentPane().setLayout(null);
        
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 400, 10);
        getContentPane().add(panel);
        
        btnNewButton_1 = new JButton("Comprar Tiquete");
        btnNewButton_1.setBounds(157, 55, 148, 26);
        getContentPane().add(btnNewButton_1);
        
        JLabel lblNewLabel = new JLabel("Bienvenido");
        lblNewLabel.setBounds(196, 11, 62, 33);
        getContentPane().add(lblNewLabel);
        
        JButton btnNewButton_1_1 = new JButton("Comprar Paquete Deluxe");
        btnNewButton_1_1.setBounds(134, 92, 187, 26);
        getContentPane().add(btnNewButton_1_1);
        
        JButton btnNewButton_1_2 = new JButton("Transferir Tiquete");
        btnNewButton_1_2.setBounds(157, 129, 148, 26);
        getContentPane().add(btnNewButton_1_2);
        
        JButton btnNewButton_1_2_1 = new JButton("Crear oferta reventa");
        btnNewButton_1_2_1.setBounds(157, 166, 148, 26);
        getContentPane().add(btnNewButton_1_2_1);
        
        JButton btnNewButton_1_2_2 = new JButton("Eliminar oferta reventa");
        btnNewButton_1_2_2.setBounds(157, 203, 148, 26);
        btnNewButton_1_2_2.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        getContentPane().add(btnNewButton_1_2_2);
        
        JButton btnNewButton_1_2_3 = new JButton("Comprar en Marketplace");
        btnNewButton_1_2_3.setBounds(145, 240, 176, 26);
        btnNewButton_1_2_3.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        getContentPane().add(btnNewButton_1_2_3);
        
        JButton btnNewButton_1_2_4 = new JButton("Contraofertar");
        btnNewButton_1_2_4.setBounds(157, 277, 148, 26);
        getContentPane().add(btnNewButton_1_2_4);
        
        JButton btnNewButton_1_2_4_1 = new JButton("Ver contraofertas ");
        btnNewButton_1_2_4_1.setBounds(157, 314, 148, 26);
        getContentPane().add(btnNewButton_1_2_4_1);
        
        JButton btnNewButton_1_2_4_1_1 = new JButton("Solicitar Reembolso");
        btnNewButton_1_2_4_1_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        btnNewButton_1_2_4_1_1.setBounds(157, 388, 148, 26);
        getContentPane().add(btnNewButton_1_2_4_1_1);
        
        JButton btnNewButton_1_2_4_1_1_1 = new JButton("Recargar Saldo");
        btnNewButton_1_2_4_1_1_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        btnNewButton_1_2_4_1_1_1.setBounds(157, 351, 148, 26);
        getContentPane().add(btnNewButton_1_2_4_1_1_1);
        
        JButton btnNewButton_1_2_4_1_1_2 = new JButton("Sugerir Venue");
        btnNewButton_1_2_4_1_1_2.setBounds(157, 425, 148, 26);
        getContentPane().add(btnNewButton_1_2_4_1_1_2);
        
        JButton btnNewButton_1_2_4_1_1_3 = new JButton("Ver ganancias");
        btnNewButton_1_2_4_1_1_3.setBounds(157, 470, 148, 26);
        getContentPane().add(btnNewButton_1_2_4_1_1_3);

       
        setTitle("BOLETAMASTER: Menu Promotor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(449, 571);
        setVisible(true);
    }

    public static void main(String[] args) {
        new ventanaMenuComprador();
    }
}


