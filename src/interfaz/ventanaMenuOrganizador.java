package interfaz;

import java.awt.*;
import javax.swing.*;

import Finanzas.Transaccion;
import Persistencia.SistemaPersistencia;
import usuario.IDuenoTiquetes;
import usuario.Organizador;


public class ventanaMenuOrganizador extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private Organizador organizador;
    private SistemaPersistencia sistema;

    public ventanaMenuOrganizador(Organizador organizador, SistemaPersistencia sistema) {
        this.organizador = organizador;
        this.sistema = sistema;

        
        getContentPane().setLayout(null);

        // Bienvenida
        JLabel lblBienvenida = new JLabel("Bienvenido Organizador, " + organizador.getLogin());
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 14));
        lblBienvenida.setBounds(100, 20, 300, 25);
        getContentPane().add(lblBienvenida);

        JLabel lblSaldo = new JLabel("Saldo: $" + organizador.getSaldo());
        lblSaldo.setFont(new Font("Arial", Font.PLAIN, 12));
        lblSaldo.setForeground(new Color(0, 128, 0));
        lblSaldo.setBounds(200, 45, 150, 20);
        getContentPane().add(lblSaldo);

        int y = 75;

        // ===== OPCIONES ESPECÍFICAS DE ORGANIZADOR =====
        
        JButton btnCrearEvento = new JButton("Crear Evento");
        btnCrearEvento.setBounds(50, y, 180, 30);
        btnCrearEvento.setBackground(new Color(52, 152, 219));
        btnCrearEvento.setForeground(Color.WHITE);
        btnCrearEvento.addActionListener(e -> abrirVentanaCrearEvento());
        getContentPane().add(btnCrearEvento);

        JButton btnVerMisEventos = new JButton("Solicitar cancelacion evento");
        btnVerMisEventos.setBounds(250, y, 180, 30);
        btnVerMisEventos.setBackground(new Color(52, 152, 219));
        btnVerMisEventos.setForeground(Color.WHITE);
        btnVerMisEventos.addActionListener(e -> abrirVentanaSolicitarCancelacionEvento());
        getContentPane().add(btnVerMisEventos);
        y += 40;

        JButton btnAgregarTiquetes = new JButton("Agregar Tiquetes a Evento");
        btnAgregarTiquetes.setBounds(50, y, 180, 30);
        btnAgregarTiquetes.setBackground(new Color(52, 152, 219));
        btnAgregarTiquetes.setForeground(Color.WHITE);
        btnAgregarTiquetes.addActionListener(e -> abrirVentanaAgregarTiquetes());
        getContentPane().add(btnAgregarTiquetes);


        // Separador
        JSeparator separator = new JSeparator();
        separator.setBounds(50, y, 380, 2);
        getContentPane().add(separator);
        y += 30;

        JLabel lblOpcionesCompra = new JLabel("OPCIONES DE COMPRA");
        lblOpcionesCompra.setFont(new Font("Arial", Font.BOLD, 12));
        lblOpcionesCompra.setBounds(165, y, 180, 20);
        getContentPane().add(lblOpcionesCompra);
        y += 30;

        // ===== OPCIONES COMUNES DE COMPRADOR =====
        
        JButton btnComprarTiquete = new JButton("Comprar Tiquete");
        btnComprarTiquete.setBounds(150, y, 180, 30);
        btnComprarTiquete.addActionListener(e -> abrirVentanaComprarTiquete());
        getContentPane().add(btnComprarTiquete);
        y += 40;

        JButton btnComprarPaqueteDeluxe = new JButton("Comprar Paquete Deluxe");
        btnComprarPaqueteDeluxe.setBounds(150, y, 180, 30);
        btnComprarPaqueteDeluxe.addActionListener(e -> abrirVentanaComprarPaqueteDeluxe());
        getContentPane().add(btnComprarPaqueteDeluxe);
        y += 40;


        JButton btnTransferirTiquete = new JButton("Transferir Tiquete");
        btnTransferirTiquete.setBounds(150, y, 180, 30);
        btnTransferirTiquete.addActionListener(e -> abrirVentanaTransferirTiquete());
        getContentPane().add(btnTransferirTiquete);
        y += 40;

        JButton btnCrearOferta = new JButton("Crear Oferta Reventa");
        btnCrearOferta.setBounds(150, y, 180, 30);
        btnCrearOferta.addActionListener(e -> abrirVentanaCrearOferta());
        getContentPane().add(btnCrearOferta);
        y += 40;

        JButton btnEliminarOferta = new JButton("Eliminar Oferta Reventa");
        btnEliminarOferta.setBounds(150, y, 180, 30);
        btnEliminarOferta.addActionListener(e -> abrirVentanaEliminarOferta());
        getContentPane().add(btnEliminarOferta);
        y += 40;

        JButton btnComprarMarketplace = new JButton("Comprar en Marketplace");
        btnComprarMarketplace.setBounds(150, y, 180, 30);
        btnComprarMarketplace.addActionListener(e -> abrirVentanaMarketplace());
        getContentPane().add(btnComprarMarketplace);
        y += 40;

        JButton btnContraofertar = new JButton("Contraofertar");
        btnContraofertar.setBounds(150, y, 180, 30);
        btnContraofertar.addActionListener(e -> abrirVentanaContraofertar());
        getContentPane().add(btnContraofertar);
        y += 40;

        JButton btnVerContraofertas = new JButton("Ver Contraofertas");
        btnVerContraofertas.setBounds(150, y, 180, 30);
        btnVerContraofertas.addActionListener(e -> abrirVentanaVerContraofertas());
        getContentPane().add(btnVerContraofertas);
        y += 40;

        JButton btnRecargarSaldo = new JButton("Recargar Saldo");
        btnRecargarSaldo.setBounds(150, y, 180, 30);
        btnRecargarSaldo.addActionListener(e -> abrirVentanaRecargarSaldo());
        getContentPane().add(btnRecargarSaldo);
        y += 40;

        JButton btnSolicitarReembolso = new JButton("Solicitar Reembolso");
        btnSolicitarReembolso.setBounds(150, y, 180, 30);
        btnSolicitarReembolso.addActionListener(e -> abrirVentanaSolicitarReembolso());
        getContentPane().add(btnSolicitarReembolso);
        y += 40;
        
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setBounds(125, y, 180, 30);
        btnCerrarSesion.setBackground(new Color(231, 76, 60));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
        getContentPane().add(btnCerrarSesion);

        setTitle("BOLETAMASTER: Menu Organizador");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(480, 377);
        setLocationRelativeTo(null);
    }

    // Métodos específicos de Organizador
    private void abrirVentanaCrearEvento() {
    	dispose();
    	new ventanaCrearEvento(sistema, organizador).setVisible(true);
    }

    private void abrirVentanaSolicitarCancelacionEvento() {
        JOptionPane.showMessageDialog(this, "Ver Mis Eventos - En desarrollo");
    }

    private void abrirVentanaAgregarTiquetes() {
        dispose();
        new ventanaCrearTiqueteSimple(sistema, organizador);
    }


    private void abrirVentanaComprarTiquete() {
        // TODO: Implementar ventana de compra de tiquetes
    	dispose();
    	ventanaComprarTiquete ventana = new ventanaComprarTiquete(sistema, organizador);
        ventana.setVisible(true);
    }

    private void abrirVentanaComprarPaqueteDeluxe() {
        // TODO: Implementar ventana de compra de paquete deluxe
        JOptionPane.showMessageDialog(this, "Ventana Comprar Paquete Deluxe - En desarrollo");
    }

    private void abrirVentanaTransferirTiquete() {
        // TODO: Implementar ventana de transferir tiquete
    	dispose();
    	Transaccion trans = new Transaccion("NA", null, null, null, null, 0);
        new ventanaTransferirTiquete(organizador,sistema,trans).setVisible(true);
    }
    private void abrirVentanaCrearOferta() {
        // TODO: Implementar ventana crear oferta
        dispose();
        Transaccion trans = new Transaccion("NA", null, null, null, null, 0);
        new ventanaCrearOferta(organizador, sistema, trans).setVisible(true);
    }

    private void abrirVentanaEliminarOferta() {
        // TODO: Implementar ventana eliminar oferta
    	dispose();
        new ventanaCancelarOferta(organizador, sistema).setVisible(true);
        
    }


    private void abrirVentanaMarketplace() {
        // TODO: Implementar ventana marketplace
        JOptionPane.showMessageDialog(this, "Ventana Marketplace - En desarrollo");
    }

    private void abrirVentanaContraofertar() {
        // TODO: Implementar ventana contraofertar
        JOptionPane.showMessageDialog(this, "Ventana Contraofertar - En desarrollo");
    }

    private void abrirVentanaVerContraofertas() {
        // TODO: Implementar ventana ver contraofertas
        JOptionPane.showMessageDialog(this, "Ventana Ver Contraofertas - En desarrollo");
    }

    private void abrirVentanaRecargarSaldo() {
        // TODO: Implementar ventana recargar saldo
        JOptionPane.showMessageDialog(this, "Ventana Recargar Saldo - En desarrollo");
    }

    private void abrirVentanaSolicitarReembolso() {
        // TODO: Implementar ventana solicitar reembolso
        JOptionPane.showMessageDialog(this, "Ventana Solicitar Reembolso - En desarrollo");
    }

    private void cerrarSesion() {
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea cerrar sesión?",
            "Cerrar Sesión",
            JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            dispose();
            ventanaLogin login = new ventanaLogin(sistema);
            login.setVisible(true);
        }
    }
}