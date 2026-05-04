package sistema_yhor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Cursor;
import java.awt.SystemColor;

public class V1 extends JFrame {

    private JPanel contentPane;
    private JTextField txtId;
    private JTextField txtProd;
    private JTextField txtPrec;
    private JTextField txtStock;
    private JTextArea textArea;

    public V1() {
        setTitle("Módulo de Almacén - VendeMás");
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 1366, 768);
        
        contentPane = new JPanel();
        contentPane.setBackground(new Color(240, 242, 245)); 
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // --- BARRA SUPERIOR (HEADER) ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(52, 58, 64)); 
        headerPanel.setBounds(0, 0, 2000, 70);
        contentPane.add(headerPanel);
        headerPanel.setLayout(null);

        JLabel lblTitulo = new JLabel("GESTIÓN DE INVENTARIO Y ALMACÉN");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setBounds(40, 15, 500, 40);
        headerPanel.add(lblTitulo);

        // --- PANEL DE FORMULARIO ---
        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        formPanel.setBounds(50, 110, 400, 500);
        contentPane.add(formPanel);
        formPanel.setLayout(null);

        JLabel lblIngreso = new JLabel("Datos del Producto");
        lblIngreso.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblIngreso.setBounds(30, 20, 200, 30);
        formPanel.add(lblIngreso);

        JLabel lblId = new JLabel("ID:");
        lblId.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblId.setBounds(30, 80, 80, 25);
        formPanel.add(lblId);

        txtId = new JTextField();
        txtId.setBounds(130, 80, 220, 30);
        formPanel.add(txtId);

        JLabel lblProd = new JLabel("Producto:");
        lblProd.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblProd.setBounds(30, 130, 80, 25);
        formPanel.add(lblProd);

        txtProd = new JTextField();
        txtProd.setBounds(130, 130, 220, 30);
        formPanel.add(txtProd);

        JLabel lblPrec = new JLabel("Precio (S/):");
        lblPrec.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPrec.setBounds(30, 180, 80, 25);
        formPanel.add(lblPrec);

        txtPrec = new JTextField();
        txtPrec.setBounds(130, 180, 220, 30);
        formPanel.add(txtPrec);

        JLabel lblStock = new JLabel("Stock:");
        lblStock.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblStock.setBounds(30, 230, 80, 25);
        formPanel.add(lblStock);

        txtStock = new JTextField();
        txtStock.setBounds(130, 230, 220, 30);
        formPanel.add(txtStock);

        // --- BOTÓN INSERTAR ---
        JButton btnInsertar = new JButton("Insertar / Sumar Stock");
        btnInsertar.setBackground(new Color(40, 167, 69)); 
        btnInsertar.setForeground(SystemColor.activeCaptionText);
        btnInsertar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnInsertar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnInsertar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection cn = Conexion.conectar(); 
                    PreparedStatement pst = cn.prepareStatement(
                        "INSERT INTO productos (nombre_producto, precio, stock) VALUES(?,?,?)");
                    pst.setString(1, txtProd.getText().trim());
                    pst.setDouble(2, Double.parseDouble(txtPrec.getText().trim()));
                    pst.setInt(3, Integer.parseInt(txtStock.getText().trim()));
                    pst.executeUpdate();
                    
                    textArea.append("\n✅ PRODUCTO REGISTRADO: " + txtProd.getText());
                    actualizarLista(); 
                    limpiarCajas();
                } catch (Exception ex) { 
                    textArea.setText("❌ Error al insertar: " + ex.getMessage()); 
                }
            }
        });
        btnInsertar.setBounds(30, 300, 320, 40);
        formPanel.add(btnInsertar);

        // --- BOTÓN MODIFICAR ---
        JButton btnModificar = new JButton("Modificar Datos");
        btnModificar.setBackground(new Color(23, 162, 184)); 
        btnModificar.setForeground(SystemColor.activeCaptionText);
        btnModificar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnModificar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection cn = Conexion.conectar();
                    PreparedStatement pst = cn.prepareStatement(
                        "UPDATE productos SET nombre_producto=?, precio=?, stock=? WHERE id_producto=?");
                    pst.setString(1, txtProd.getText().trim());
                    pst.setDouble(2, Double.parseDouble(txtPrec.getText().trim()));
                    pst.setInt(3, Integer.parseInt(txtStock.getText().trim()));
                    pst.setInt(4, Integer.parseInt(txtId.getText().trim()));
                    pst.executeUpdate();
                    
                    textArea.append("\n✅ PRODUCTO ID " + txtId.getText() + " MODIFICADO");
                    actualizarLista();
                    limpiarCajas();
                } catch (Exception ex) { 
                    textArea.setText("❌ Error al modificar: " + ex.getMessage()); 
                }
            }
        });
        btnModificar.setBounds(30, 350, 150, 40);
        formPanel.add(btnModificar);

        // --- BOTÓN ELIMINAR ---
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(220, 53, 69)); 
        btnEliminar.setForeground(SystemColor.textInactiveText);
        btnEliminar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection cn = Conexion.conectar();
                    PreparedStatement pst = cn.prepareStatement("DELETE FROM productos WHERE id_producto = ?");
                    pst.setInt(1, Integer.parseInt(txtId.getText().trim()));
                    pst.executeUpdate();
                    
                    textArea.append("\n🗑️ PRODUCTO ELIMINADO");
                    actualizarLista();
                    limpiarCajas();
                } catch (Exception ex) { 
                    textArea.setText("❌ Error al eliminar: " + ex.getMessage()); 
                }
            }
        });
        btnEliminar.setBounds(200, 350, 150, 40);
        formPanel.add(btnEliminar);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 16));
        textArea.setBounds(500, 110, 750, 500);
        contentPane.add(textArea);
        
        JButton btnActualizar = new JButton("🔄 Refrescar Inventario");
        btnActualizar.setBackground(new Color(108, 117, 125)); 
        btnActualizar.setForeground(SystemColor.controlText);
        btnActualizar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnActualizar.setBounds(500, 630, 200, 40);
        btnActualizar.addActionListener(e -> actualizarLista());
        contentPane.add(btnActualizar);
        
       
        actualizarLista();
    }
    
    private void actualizarLista() {
        textArea.setText("--- INVENTARIO DESDE BASE DE DATOS ---\n");
        try {
            Connection cn = Conexion.conectar();
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM productos");
            while (rs.next()) {
                textArea.append("ID: " + rs.getInt("id_producto") + 
                                "\t| Prod: " + rs.getString("nombre_producto") + 
                                "\t| S/ " + rs.getDouble("precio") + 
                                "\t| Stock: " + rs.getInt("stock") + "\n");
            }
        } catch (Exception e) {
            textArea.append("\n❌ Error al cargar lista: " + e.getMessage());
        }
    }

    private void limpiarCajas() { 
        txtId.setText(""); txtProd.setText(""); txtPrec.setText(""); txtStock.setText(""); 
    }
}