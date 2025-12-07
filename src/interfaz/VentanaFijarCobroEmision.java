package interfaz;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.PlainDocument;

import Persistencia.SistemaPersistencia;
import usuario.Administrador;


public class VentanaFijarCobroEmision extends JFrame {
	private static final long serialVersionUID = 1L;
    private JButton btnFijarCobro;
    private JTextField textField;
    private SistemaPersistencia sistema;
    private Administrador admin;
    
    

    public VentanaFijarCobroEmision(SistemaPersistencia sistema, Administrador admin) {
        
    	this.admin = admin;
    	this.sistema = sistema;
    	
   
    	
    	getContentPane().setLayout(null);
        
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 400, 10);
        getContentPane().add(panel);
        
        btnFijarCobro = new JButton("Fijar Cobro");
        btnFijarCobro.setBounds(177, 129, 131, 26);
        getContentPane().add(btnFijarCobro);
        
        btnFijarCobro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FijarCobro();
            }
        });
        
       
        
        JLabel lblCobro = new JLabel("Cobro a fijar:");
        lblCobro.setBounds(40, 55, 137, 16);
        getContentPane().add(lblCobro);
        
        JButton btnSalir = new JButton("Salir");
        btnSalir.setBounds(6, 129, 79, 26);
        getContentPane().add(btnSalir);
       
        
        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                volverAlMenu();
            }
        });
        
        textField = new JTextField();
        textField.setBounds(177, 50, 106, 26);
        getContentPane().add(textField);
        textField.setColumns(10);
        
        PlainDocument doc = (PlainDocument) textField.getDocument();
        doc.setDocumentFilter(new FiltroSoloNumeros());

       
        setTitle("BOLETAMASTER: Fijar Cobro Emision");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(318, 206);
        setVisible(true);
    }
    
    private void FijarCobro() {
        String valorRecarga = textField.getText();

        if (valorRecarga.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa un valor numérico.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int recarga = Integer.parseInt(valorRecarga);

        if (recarga <= 0) {
            JOptionPane.showMessageDialog(this, "El valor debe ser mayor a 0.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        admin.fijarCobroEmisionImpresion(recarga);
        sistema.guardarTodo();

        JOptionPane.showMessageDialog(this, "Cobro fijado exitosamente");
    }
    
    private void volverAlMenu() {
    	dispose();
        new ventanaMenuAdministrador(admin, sistema).setVisible(true);;
             
    }
    


}
