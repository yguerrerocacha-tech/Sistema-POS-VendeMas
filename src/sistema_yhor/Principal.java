package sistema_yhor;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import javax.swing.JOptionPane;
import java.awt.Cursor;
import javax.swing.ImageIcon;
import javax.swing.JTextField;

public class Principal extends JFrame {

    private JPanel contentPane;
    private JTextField txtUsuario;
    private JPasswordField txtContraseña;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Principal frame = new Principal();
                    
                    frame.setLocationRelativeTo(null); 
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Principal() {
        setResizable(false);
        setTitle("VendeMás - Pantalla de Inicio");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 450); 
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

       
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setBackground(new Color(230, 126, 34)); 
        panelIzquierdo.setBounds(0, 0, 300, 411);
        contentPane.add(panelIzquierdo);
        panelIzquierdo.setLayout(null);

        JLabel lblTitulo1 = new JLabel("SISTEMA");
        lblTitulo1.setForeground(new Color(255, 255, 255));
        lblTitulo1.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo1.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo1.setBounds(10, 10, 300, 30);
        panelIzquierdo.add(lblTitulo1);

        JLabel lblTitulo2 = new JLabel("VENDEMÁS");
        lblTitulo2.setForeground(new Color(255, 255, 255));
        lblTitulo2.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo2.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitulo2.setBounds(10, 44, 300, 50);
        panelIzquierdo.add(lblTitulo2);
        
        
        JLabel lblLogo = new JLabel("");
        lblLogo.setIcon(new ImageIcon(Principal.class.getResource("/imagen/Sin título.jpg")));
        lblLogo.setForeground(new Color(255, 255, 255));
        lblLogo.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        lblLogo.setBounds(10, 10, 280, 380);
        panelIzquierdo.add(lblLogo);

        
        JPanel panelDerecho = new JPanel();
        panelDerecho.setBackground(new Color(255, 255, 255));
        panelDerecho.setBounds(300, 0, 384, 411);
        contentPane.add(panelDerecho);
        panelDerecho.setLayout(null);

        JLabel lblBienvenido = new JLabel("INICIO DE SESIÓN");
        lblBienvenido.setBounds(0, 50, 384, 30);
        lblBienvenido.setForeground(new Color(51, 51, 51));
        lblBienvenido.setHorizontalAlignment(SwingConstants.CENTER);
        lblBienvenido.setFont(new Font("Segoe UI", Font.BOLD, 22));
        panelDerecho.add(lblBienvenido);

        JButton btnAlmacen = new JButton(" Gestión de Almacén");
        btnAlmacen.setBounds(50, 120, 280, 45);
        btnAlmacen.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); 
        btnAlmacen.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAlmacen.setBackground(Color.GRAY);  
        btnAlmacen.setForeground(Color.BLACK);
        btnAlmacen.setFocusPainted(false);
        btnAlmacen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                V1 ventanaAlmacen = new V1();
                ventanaAlmacen.setLocationRelativeTo(null);
                ventanaAlmacen.setVisible(true);
            }
        });
        panelDerecho.add(btnAlmacen);

        
        JButton btnVenta = new JButton(" Punto de Venta (Caja)");
        btnVenta.setBounds(50, 190, 280, 45);
        btnVenta.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVenta.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnVenta.setBackground(Color.GRAY); // 
        btnVenta.setForeground(Color.LIGHT_GRAY);
        btnVenta.setFocusPainted(false);
        btnVenta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                
                String usuario = txtUsuario.getText().trim();
                String password = new String(txtContraseña.getPassword());
                
                                if (usuario.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingresa tu usuario y contraseña.");
                    return;
                }
                
               
                try {
                    Conexion con = new Conexion();
                    Connection conexion = con.conectar();
                    
                    if (conexion != null) {
                        
                        String sql = "SELECT * FROM usuarios WHERE username = ? AND password_hash = ?";
                        PreparedStatement pst = conexion.prepareStatement(sql);
                        pst.setString(1, usuario);
                        pst.setString(2, password);
                        
                        ResultSet rs = pst.executeQuery();
                        
                        if (rs.next()) {
                            JOptionPane.showMessageDialog(null, "¡Bienvenido, " + usuario + "!");
                            
                         
                            Venta ventanaVentas = new Venta();
                            ventanaVentas.setLocationRelativeTo(null);
                            ventanaVentas.setVisible(true);
                            
                        } else {
                        
                            JOptionPane.showMessageDialog(null, "❌ Usuario o contraseña incorrecta.", "Error de Acceso", JOptionPane.ERROR_MESSAGE);
                        }
                        
                        conexion.close();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos: " + ex.getMessage());
                }
            }
        });
        panelDerecho.add(btnVenta);

        
        JButton btnSalir = new JButton(" Salir del Sistema");
        btnSalir.setBounds(50, 260, 280, 45);
        btnSalir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSalir.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSalir.setBackground(new Color(255, 0, 0)); 
        btnSalir.setForeground(Color.BLACK);
        btnSalir.setFocusPainted(false);
        btnSalir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });         panelDerecho.add(btnSalir);
        
        JLabel lblNewLabel = new JLabel("USUARIO :");
        lblNewLabel.setBounds(27, 333, 73, 30);
        panelDerecho.add(lblNewLabel);
        
        txtUsuario = new JTextField();
        txtUsuario.setText("");
        txtUsuario.setBounds(27, 357, 138, 24);
        panelDerecho.add(txtUsuario);
        txtUsuario.setColumns(10);
        
        JLabel lblNewLabel_1 = new JLabel("Contraseña");
        lblNewLabel_1.setBounds(175, 358, 68, 21);
        panelDerecho.add(lblNewLabel_1);
        
        txtContraseña = new JPasswordField();
        txtContraseña.setBounds(242, 359, 84, 18);
        panelDerecho.add(txtContraseña);
    }
}