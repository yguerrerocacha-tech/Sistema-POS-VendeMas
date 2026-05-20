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
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable; 
import javax.swing.table.DefaultTableModel; 
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ComponentAdapter; 
import java.awt.event.ComponentEvent;   
import java.awt.Cursor;
import java.awt.SystemColor;
import java.awt.Toolkit;
import javax.swing.JScrollPane;

public class V1 extends JFrame {

    private JPanel contentPane;
    private JTextField txtId;
    private JTextField txtProd;
    private JTextField txtPrec;
    private JTextField txtStock;
    
    private JTable tablaProductos; 
    private JScrollPane scrollPane; 
    private JButton btnActualizar;  

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

        // --- BARRA SUPERIOR ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(52, 58, 64)); 
        headerPanel.setBounds(0, 0, 2500, 70); 
        contentPane.add(headerPanel);
        headerPanel.setLayout(null);

        JLabel lblTitulo = new JLabel("GESTIÓN DE INVENTARIO Y ALMACÉN");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setBounds(40, 15, 500, 40);
        headerPanel.add(lblTitulo);

        // --- BOTÓN PARA REGRESAR AL PUNTO DE VENTA (CAJA) ---
        JButton btnVolverVenta = new JButton("⬅️ VOLVER A VENTAS (CAJA)");
        btnVolverVenta.addActionListener(e -> {
            Venta ventanaVenta = new Venta(); 
            ventanaVenta.setVisible(true); 
            dispose(); 
        });
        btnVolverVenta.setBounds(10, 73, 240, 36); 
        btnVolverVenta.setBackground(new Color(108, 117, 125)); 
        btnVolverVenta.setForeground(Color.WHITE);
        btnVolverVenta.setFont(new Font("Segoe UI", Font.BOLD, 14));
        contentPane.add(btnVolverVenta);

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
        configurarSoloNumeros(txtId);
        
        // --- LA MAGIA DE LA OPCIÓN B: BUSCAR AL PRESIONAR ENTER ---
        txtId.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Si presiona Enter
                    String idStr = txtId.getText().trim();
                    if (idStr.isEmpty()) return;
                    
                    try {
                        Connection cn = Conexion.conectar();
                        PreparedStatement pst = cn.prepareStatement("SELECT * FROM productos WHERE id_producto = ?");
                        pst.setInt(1, Integer.parseInt(idStr));
                        ResultSet rs = pst.executeQuery();
                        
                        if (rs.next()) {
                            // Autocompletamos las cajas con los datos de MySQL
                            txtProd.setText(rs.getString("nombre_producto"));
                            txtPrec.setText(String.format("%.2f", rs.getDouble("precio")));
                            
                            // Avisamos al usuario el stock actual y dejamos listo el foco
                            int stockActual = rs.getInt("stock");
                            Toolkit.getDefaultToolkit().beep();
                            txtStock.setText(""); // Limpiamos para que ponga la cantidad del repartidor
                            txtStock.requestFocus(); // Mueve el cursor automáticamente a la caja de Stock
                            
                            JOptionPane.showMessageDialog(V1.this, 
                                "📦 Producto: " + rs.getString("nombre_producto") + 
                                "\n📊 Stock Actual: " + stockActual + " unidades.\n\n👉 Escribe en 'Stock' la cantidad que llegó para sumarla.", 
                                "Producto Encontrado", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(V1.this, "⚠️ El ID no existe. Registre como producto nuevo si lo desea.");
                            txtProd.setText("");
                            txtPrec.setText("");
                            txtStock.setText("");
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(V1.this, "❌ Error al buscar producto: " + ex.getMessage());
                    }
                }
            }
        });

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
        configurarSoloNumeros(txtStock);

        // --- BOTÓN INSERTAR ---
        JButton btnInsertar = new JButton("Registrar Nuevo Producto");
        btnInsertar.setBackground(new Color(40, 167, 69)); 
        btnInsertar.setForeground(SystemColor.activeCaptionText);
        btnInsertar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnInsertar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnInsertar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(txtId.getText().trim());
                    String nombre = txtProd.getText().trim();
                    double precio = Double.parseDouble(txtPrec.getText().trim());
                    int stock = Integer.parseInt(txtStock.getText().trim());

                    if (precio <= 0 || stock <= 0) {
                        JOptionPane.showMessageDialog(V1.this, "⚠️ ERROR: Precio y Stock deben ser mayores a 0.");
                        return; 
                    }

                    Connection cn = Conexion.conectar(); 
                    PreparedStatement pst = cn.prepareStatement(
                        "INSERT INTO productos (id_producto, nombre_producto, precio, stock) VALUES(?,?,?,?)");

                    pst.setInt(1, id);
                    pst.setString(2, nombre);
                    pst.setDouble(3, precio);
                    pst.setInt(4, stock);

                    pst.executeUpdate();
                    
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(V1.this, "¡Producto registrado con éxito!");
                    
                    actualizarLista(); 
                    limpiarCajas();
                } catch (Exception ex) { 
                    JOptionPane.showMessageDialog(V1.this, "❌ Error: " + ex.getMessage());
                }
            }
        });
        btnInsertar.setBounds(30, 300, 320, 40);
        formPanel.add(btnInsertar);

        // --- BOTÓN MODIFICAR ---
        JButton btnModificar = new JButton("Modificar / Sumar Stock");
        btnModificar.setBackground(new Color(23, 162, 184)); 
        btnModificar.setForeground(SystemColor.activeCaptionText);
        btnModificar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnModificar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(txtId.getText().trim());
                    String nombre = txtProd.getText().trim();
                    double precio = Double.parseDouble(txtPrec.getText().trim());
                    int stockSumar = Integer.parseInt(txtStock.getText().trim());

                    Connection cn = Conexion.conectar();
                    PreparedStatement pst = cn.prepareStatement(
                        "UPDATE productos SET nombre_producto=?, precio=?, stock = stock + ? WHERE id_producto=?");
                    pst.setString(1, nombre);
                    pst.setDouble(2, precio);
                    pst.setInt(3, stockSumar);
                    pst.setInt(4, id);
                    
                    if (pst.executeUpdate() > 0) {
                        Toolkit.getDefaultToolkit().beep();
                        JOptionPane.showMessageDialog(V1.this, "¡Inventario actualizado con la carga del repartidor!");
                        actualizarLista();
                        limpiarCajas();
                    }
                } catch (Exception ex) { 
                    JOptionPane.showMessageDialog(V1.this, "❌ Error al modificar: " + ex.getMessage());
                }
            }
        });
        btnModificar.setBounds(30, 350, 180, 40);
        formPanel.add(btnModificar);

        // --- BOTÓN ELIMINAR ---
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(220, 53, 69)); 
        btnEliminar.setForeground(SystemColor.activeCaptionText);
        btnEliminar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection cn = Conexion.conectar();
                    int idAEliminar = Integer.parseInt(txtId.getText().trim());
                    PreparedStatement pst = cn.prepareStatement("DELETE FROM productos WHERE id_producto = ?");
                    pst.setInt(1, idAEliminar);
                    if (pst.executeUpdate() > 0) {
                        Toolkit.getDefaultToolkit().beep();
                        JOptionPane.showMessageDialog(V1.this, "¡Producto removido!");
                        actualizarLista();
                        limpiarCajas();
                    }
                } catch (Exception ex) { 
                    JOptionPane.showMessageDialog(V1.this, "❌ Error técnico: " + ex.getMessage());
                }
            }
        });
        btnEliminar.setBounds(220, 350, 130, 40);
        formPanel.add(btnEliminar);

        // --- ÁREA DE INVENTARIO CON SCROLL ADAPTABLE ---
        scrollPane = new JScrollPane();
        scrollPane.setBounds(480, 110, 830, 500); 
        contentPane.add(scrollPane);
        
        tablaProductos = new JTable();
        tablaProductos.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablaProductos.setRowHeight(22); 
        scrollPane.setViewportView(tablaProductos);
        
        btnActualizar = new JButton("🔄 Refrescar Inventario");
        btnActualizar.setBackground(new Color(108, 117, 125)); 
        btnActualizar.setForeground(SystemColor.controlText);
        btnActualizar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnActualizar.setBounds(480, 630, 220, 40);
        btnActualizar.addActionListener(e -> actualizarLista());
        contentPane.add(btnActualizar);
        
        // --- LISTENER RESPONSIVO ---
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int anchoVentana = contentPane.getWidth();
                int altoVentana = contentPane.getHeight();
                
                int nuevoAnchoScroll = anchoVentana - 530; 
                int nuevoAltoScroll = altoVentana - 240;   
                
                if (nuevoAnchoScroll > 200 && nuevoAltoScroll > 100) {
                    scrollPane.setBounds(480, 110, nuevoAnchoScroll, nuevoAltoScroll);
                    btnActualizar.setBounds(480, 110 + nuevoAltoScroll + 15, 220, 40);
                }
            }
        });
        
        actualizarLista();
    }
    
    private void actualizarLista() {
        String[] columnas = {"ID", "Producto", "Precio (S/.)", "Stock"};
        DefaultTableModel modelo = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        try {
            Connection cn = Conexion.conectar();
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery("SELECT id_producto, nombre_producto, precio, stock FROM productos");
            
            String[] fila = new String[4];
            while (rs.next()) {
                fila[0] = rs.getString("id_producto");
                fila[1] = rs.getString("nombre_producto");
                fila[2] = "S/. " + String.format("%.2f", rs.getDouble("precio"));
                fila[3] = rs.getString("stock");
                modelo.addRow(fila);
            }
            
            tablaProductos.setModel(modelo);
            tablaProductos.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error al cargar lista: " + e.getMessage());
        }
    }

    private void limpiarCajas() { 
        txtId.setText(""); txtProd.setText(""); txtPrec.setText(""); txtStock.setText(""); 
    }

    private void configurarSoloNumeros(JTextField campo) {
        campo.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) e.consume();
            }
        });
    }
}