package sistema_yhor;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JPasswordField;
import java.awt.Cursor;
import javax.swing.ImageIcon;
import javax.swing.JTextField;

public class Principal extends JFrame {

    private static final long serialVersionUID = 1L;
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
        setBounds(100, 100, 750, 500); 
        
        contentPane = new JPanel();
        contentPane.setBackground(new Color(24, 28, 36));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panelContenedor = new JPanel();
        panelContenedor.setBackground(new Color(32, 38, 50));
        panelContenedor.setBounds(40, 30, 660, 400);
        panelContenedor.setBorder(new LineBorder(new Color(45, 55, 72), 1, true));
        contentPane.add(panelContenedor);
        panelContenedor.setLayout(null);

        JLabel lblLogo = new JLabel("");
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        lblLogo.setIcon(new ImageIcon(Principal.class.getResource("/imagen/Sin título.jpg")));
        lblLogo.setBounds(40, 40, 240, 320);
        panelContenedor.add(lblLogo);

        JLabel lblBienvenido = new JLabel("INICIO DE SESIÓN");
        lblBienvenido.setForeground(new Color(243, 244, 246));
        lblBienvenido.setHorizontalAlignment(SwingConstants.CENTER);
        lblBienvenido.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblBienvenido.setBounds(320, 35, 300, 30);
        panelContenedor.add(lblBienvenido);

        JLabel lblUser = new JLabel("USUARIO");
        lblUser.setForeground(new Color(156, 163, 175));
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblUser.setBounds(330, 85, 200, 20);
        panelContenedor.add(lblUser);

        txtUsuario = new JTextField();
        txtUsuario.setCaretColor(Color.WHITE);
        txtUsuario.setForeground(Color.WHITE);
        txtUsuario.setBackground(new Color(21, 25, 32));
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsuario.setBorder(new LineBorder(new Color(55, 65, 81), 1, true));
        txtUsuario.setBounds(330, 110, 280, 35);
        panelContenedor.add(txtUsuario);
        txtUsuario.setColumns(10);

        JLabel lblPass = new JLabel("CONTRASEÑA");
        lblPass.setForeground(new Color(156, 163, 175));
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblPass.setBounds(330, 155, 200, 20);
        panelContenedor.add(lblPass);

        txtContraseña = new JPasswordField();
        txtContraseña.setCaretColor(Color.WHITE);
        txtContraseña.setForeground(Color.WHITE);
        txtContraseña.setBackground(new Color(21, 25, 32));
        txtContraseña.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtContraseña.setBorder(new LineBorder(new Color(55, 65, 81), 1, true));
        txtContraseña.setBounds(330, 180, 280, 35);
        panelContenedor.add(txtContraseña);

        JButton btnVenta = new JButton("Punto de Venta (Caja)");
        btnVenta.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVenta.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnVenta.setBackground(new Color(37, 99, 235)); 
        btnVenta.setForeground(Color.BLACK);
        btnVenta.setFocusPainted(false);
        btnVenta.setBorder(new LineBorder(new Color(37, 99, 235), 1, true));
        btnVenta.setBounds(330, 240, 280, 38);
        
        btnVenta.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnVenta.setBackground(new Color(29, 78, 216)); }
            public void mouseExited(MouseEvent e) { btnVenta.setBackground(new Color(37, 99, 235)); }
        });
        
        btnVenta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String usuario = txtUsuario.getText().trim();
                String password = new String(txtContraseña.getPassword());
                
                if (usuario.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingresa tu usuario y contraseña.");
                    return;
                }
                
                try {
                    Connection conexion = Conexion.conectar();
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
                            
                            // Si el usuario cierra la caja, el login vuelve a ser visible
                            ventanaVentas.addWindowListener(new WindowAdapter() {
                                @Override
                                public void windowClosed(WindowEvent e) {
                                    Principal.this.setVisible(true);
                                }
                            });
                            
                            ventanaVentas.setVisible(true);
                            Principal.this.setVisible(false); // Oculta la pantalla principal sin destruirla
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
        panelContenedor.add(btnVenta);

        JButton btnAlmacen = new JButton("Gestión de Almacén");
        btnAlmacen.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); 
        btnAlmacen.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAlmacen.setBackground(new Color(75, 85, 99));  
        btnAlmacen.setForeground(Color.BLACK);
        btnAlmacen.setFocusPainted(false);
        btnAlmacen.setBorder(new LineBorder(new Color(75, 85, 99), 1, true));
        btnAlmacen.setBounds(330, 290, 280, 38);
        
        btnAlmacen.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnAlmacen.setBackground(new Color(55, 65, 81)); }
            public void mouseExited(MouseEvent e) { btnAlmacen.setBackground(new Color(75, 85, 99)); }
        });
        
        btnAlmacen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                V1 ventanaAlmacen = new V1();
                ventanaAlmacen.setLocationRelativeTo(null);
                
                // Si el usuario cierra el almacén, el login vuelve a ser visible
                ventanaAlmacen.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        Principal.this.setVisible(true);
                    }
                });
                
                ventanaAlmacen.setVisible(true);
                Principal.this.setVisible(false); // Oculta la pantalla principal sin destruirla
            }
        });
        panelContenedor.add(btnAlmacen);

        JButton btnSalir = new JButton("Salir del Sistema");
        btnSalir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSalir.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSalir.setBackground(new Color(31, 41, 55)); 
        btnSalir.setForeground(new Color(239, 68, 68));
        btnSalir.setFocusPainted(false);
        btnSalir.setBorder(new LineBorder(new Color(55, 65, 81), 1, true));
        btnSalir.setBounds(330, 340, 280, 32);
        
        btnSalir.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnSalir.setBackground(new Color(17, 24, 39)); }
            public void mouseExited(MouseEvent e) { btnSalir.setBackground(new Color(31, 41, 55)); }
        });
        
        btnSalir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        panelContenedor.add(btnSalir);
    }
}