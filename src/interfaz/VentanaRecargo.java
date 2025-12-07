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

public class VentanaRecargo extends JFrame {
	private static final long serialVersionUID = 1L;
    private JButton btnFijarCobro;
    private JTextField textField;
    private SistemaPersistencia sistema;
    private Administrador admin;
    
    

    public VentanaRecargo(SistemaPersistencia sistema, Administrador admin) {
        
    	this.admin = admin;
    	this.sistema = sistema;
    	
   
    	
    	getContentPane().setLayout(null);
        
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 400, 10);
        getContentPane().add(panel);
        
        btnFijarCobro = new JButton("Fijar recargo");
        btnFijarCobro.setBounds(177, 129, 131, 26);
        getContentPane().add(btnFijarCobro);
        
        btnFijarCobro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	FijarRecargo();
            }
        });
        
       
        
        JLabel lblCobro = new JLabel("Porcentaje (1-100):");
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

       
        setTitle("BOLETAMASTER: Fijar Recargo");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(318, 206);
        setVisible(true);
    }
    
    private void FijarRecargo() {
        String valorRecarga = textField.getText();

        if (valorRecarga.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa un porcentaje.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int porcentaje = Integer.parseInt(valorRecarga);

        if (porcentaje <= 0 || porcentaje > 100) {
            JOptionPane.showMessageDialog(this,
                    "El porcentaje debe estar entre 1 y 100.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        admin.cobrarPorcentajeAdicional(porcentaje);

       

        sistema.guardarTodo();

        JOptionPane.showMessageDialog(this, "Recargo fijado exitosamente");
    }
    
    private void volverAlMenu() {
    	dispose();
        new ventanaMenuAdministrador(admin, sistema).setVisible(true);;
             
    }
    


}
