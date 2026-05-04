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
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;

public class Venta extends JFrame {

    private JPanel contentPane;
    private JTextField txtIdVenta;
    private JTextField txtNombreVenta;
    private JTextField txtPrecioVenta;
    private JTextField txtCantidadVenta;
    private JTextField txtDescuento;
    private JTextArea textAreaVenta;
    private JTextField txtDniVenta;
    private JTextField txtClienteNombre;
    private JButton btnBuscarCliente;
    
    private String detallesCarrito = ""; 
    private double subtotalAcumulado = 0.0;

    public Venta() {
        setTitle("Punto de Venta - VendeMás");
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 1366, 768);
        
        contentPane = new JPanel();
        contentPane.setBackground(new Color(240, 242, 245)); 
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(52, 58, 64)); 
        headerPanel.setBounds(0, 0, 2000, 70);
        contentPane.add(headerPanel);
        headerPanel.setLayout(null);

        JLabel lblTitulo = new JLabel("PUNTO DE VENTA (CAJA)");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setBounds(40, 15, 400, 40);
        headerPanel.add(lblTitulo);

        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        formPanel.setBounds(10, 110, 450, 500);
        contentPane.add(formPanel);
        formPanel.setLayout(null);

        JLabel lblBuscar = new JLabel("Escanear / Buscar Producto");
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblBuscar.setBounds(30, 23, 300, 30);
        formPanel.add(lblBuscar);
  
        JLabel lblDni = new JLabel("DNI Cliente:");
        lblDni.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDni.setBounds(293, 239, 100, 25);
        formPanel.add(lblDni);

        txtDniVenta = new JTextField();
        txtDniVenta.setBounds(293, 274, 120, 30);
        formPanel.add(txtDniVenta);

        btnBuscarCliente = new JButton("🔍");
        btnBuscarCliente.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String dni = txtDniVenta.getText().trim();
                String nombreEscrito = txtClienteNombre.getText().trim();

                try {
                    Connection cn = Conexion.conectar();
                 
                    PreparedStatement pst = cn.prepareStatement("SELECT nombre_completo FROM clientes WHERE dni = ?");
                    pst.setString(1, dni);
                    ResultSet rs = pst.executeQuery();

                    if (rs.next()) {
                      
                        txtClienteNombre.setText(rs.getString("nombre_completo"));
                        textAreaVenta.setText("✅ Cliente frecuente: " + rs.getString("nombre_completo"));
                    } else if (!nombreEscrito.isEmpty()) {
                       
                        PreparedStatement pstIns = cn.prepareStatement("INSERT INTO clientes (dni, nombre_completo) VALUES (?, ?)");
                        pstIns.setString(1, dni);
                        pstIns.setString(2, nombreEscrito);
                        pstIns.executeUpdate();
                        textAreaVenta.setText("✨ Cliente registrado con éxito.");
                    } else {
                       
                        textAreaVenta.setText(" Cliente nuevo. Escribe su nombre y presiona la lupa para guardarlo.");
                        txtClienteNombre.requestFocus();
                    }
                } catch (Exception ex) {
                    textAreaVenta.setText(" Error en MySQL: " + ex.getMessage());
                }
            }
        });
        btnBuscarCliente.setBounds(395, 446, 50, 30);
        formPanel.add(btnBuscarCliente);

        JLabel lblNomCli = new JLabel("Cliente:");
        lblNomCli.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblNomCli.setBounds(40, 447, 100, 25);
        formPanel.add(lblNomCli);

        JLabel lblId = new JLabel("ID Producto:");
        lblId.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblId.setBounds(30, 80, 100, 25);
        formPanel.add(lblId);

        txtIdVenta = new JTextField();
        txtIdVenta.setBounds(130, 80, 150, 30);
        formPanel.add(txtIdVenta);
        
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(new Color(108, 117, 125)); 
        btnBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection cn = Conexion.conectar();
                    PreparedStatement pst = cn.prepareStatement("SELECT * FROM productos WHERE id_producto = ?");
                    pst.setInt(1, Integer.parseInt(txtIdVenta.getText()));
                    ResultSet rs = pst.executeQuery();
                    if (rs.next()) {
                        txtNombreVenta.setText(rs.getString("nombre_producto")); 
                        txtPrecioVenta.setText(String.valueOf(rs.getDouble("precio")));
                        textAreaVenta.setText(" Producto listo. Stock disponible: " + rs.getInt("stock"));
                    } else {
                        textAreaVenta.setText(" Producto no encontrado en MySQL.");
                        txtNombreVenta.setText(""); 
                        txtPrecioVenta.setText("");
                    }
                } catch (Exception ex) { 
                    textAreaVenta.setText("❌ Error: ID inválido o conexión fallida."); 
                }
            }
        });
        btnBuscar.setBounds(293, 79, 120, 30);
        formPanel.add(btnBuscar);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblNombre.setBounds(30, 130, 100, 25);
        formPanel.add(lblNombre);

        txtNombreVenta = new JTextField();
        txtNombreVenta.setEditable(false);
        txtNombreVenta.setBounds(130, 130, 280, 30);
        formPanel.add(txtNombreVenta);

        JLabel lblPrecio = new JLabel("Precio (S/):");
        lblPrecio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPrecio.setBounds(30, 180, 100, 25);
        formPanel.add(lblPrecio);

        txtPrecioVenta = new JTextField();
        txtPrecioVenta.setEditable(false);
        txtPrecioVenta.setBounds(130, 180, 150, 30);
        formPanel.add(txtPrecioVenta);

        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblCantidad.setBounds(30, 250, 100, 25);
        formPanel.add(lblCantidad);

        txtCantidadVenta = new JTextField();
        txtCantidadVenta.setBounds(130, 250, 150, 30);
        formPanel.add(txtCantidadVenta);

        JLabel lblDesc = new JLabel("Desc (%):");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDesc.setBounds(30, 300, 100, 25);
        formPanel.add(lblDesc);

        txtDescuento = new JTextField();
        txtDescuento.setForeground(SystemColor.inactiveCaptionText);
        txtDescuento.setBounds(130, 300, 150, 30);
        formPanel.add(txtDescuento);

        JButton btnCalcular = new JButton(" AGREGAR AL CARRITO");
        btnCalcular.setBackground(new Color(0, 123, 255)); 
        btnCalcular.setForeground(SystemColor.desktop);
        btnCalcular.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnCalcular.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection cn = Conexion.conectar();
                    int idProd = Integer.parseInt(txtIdVenta.getText());
                    int cant = Integer.parseInt(txtCantidadVenta.getText());

                    PreparedStatement pst = cn.prepareStatement("SELECT * FROM productos WHERE id_producto = ?");
                    pst.setInt(1, idProd);
                    ResultSet rs = pst.executeQuery();

                    if (rs.next()) {
                        int stockActual = rs.getInt("stock");
                        if (stockActual >= cant) {
                            double precio = rs.getDouble("precio");
                            String nombre = rs.getString("nombre_producto");
                            double subtotalItem = precio * cant;

                            PreparedStatement pstStock = cn.prepareStatement("UPDATE productos SET stock = stock - ? WHERE id_producto = ?");
                            pstStock.setInt(1, cant);
                            pstStock.setInt(2, idProd);
                            pstStock.executeUpdate();

                            PreparedStatement pstCli = cn.prepareStatement("INSERT IGNORE INTO clientes (dni, nombre_completo) VALUES (?, ?)");
                            pstCli.setString(1, txtDniVenta.getText().trim());
                            pstCli.setString(2, txtClienteNombre.getText().trim());
                            pstCli.executeUpdate();

                            subtotalAcumulado += subtotalItem;
                            detallesCarrito += String.format("%-20s %d x S/%.2f = S/%.2f\n", nombre, cant, precio, subtotalItem);

                            textAreaVenta.setText("🛒 CARRITO ACTUAL:\n" +
                                                "Cliente: " + txtClienteNombre.getText() + "\n" +
                                                "--------------------------------------------------\n" +
                                                detallesCarrito +
                                                "--------------------------------------------------\n" +
                                                "SUBTOTAL PARCIAL: S/ " + String.format("%.2f", subtotalAcumulado));

                            txtIdVenta.setText(""); txtNombreVenta.setText(""); txtPrecioVenta.setText(""); txtCantidadVenta.setText("");
                        } else {
                            textAreaVenta.setText("❌ Stock insuficiente. Solo quedan " + stockActual);
                        }
                    }
                } catch (Exception ex) {
                    textAreaVenta.setText("❌ Error: Verifique los datos o la conexión.");
                }
            }
        });
        btnCalcular.setBounds(30, 380, 380, 50);
        formPanel.add(btnCalcular);
        
                txtClienteNombre = new JTextField();
                txtClienteNombre.setBounds(90, 447, 280, 30);
                formPanel.add(txtClienteNombre);
                txtClienteNombre.setEditable(true);

        textAreaVenta = new JTextArea();
        textAreaVenta.setEditable(false);
        textAreaVenta.setFont(new Font("Consolas", Font.BOLD, 16));
        textAreaVenta.setBounds(524, 110, 500, 500);
        contentPane.add(textAreaVenta);
        
        JButton btnFinalizar = new JButton("FINALIZAR Y EMITIR BOLETA");
        btnFinalizar.setBounds(347, 671, 380, 50);
        btnFinalizar.setBackground(new Color(40, 167, 69)); 
        btnFinalizar.setForeground(Color.RED);
        btnFinalizar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        contentPane.add(btnFinalizar);
        
           JButton btnIrAlmacen = new JButton(" IR A GESTIÓN ALMACÉN");
           btnIrAlmacen.addActionListener(new ActionListener() {
        	    public void actionPerformed(ActionEvent e) {
        	        
        	        V1 ventanaAlmacen = new V1(); 
        	        ventanaAlmacen.setVisible(true);
        	        dispose(); 
        	    }
        	});
           btnIrAlmacen.setBounds(10, 73, 229, 36);
           contentPane.add(btnIrAlmacen);
           btnIrAlmacen.setBackground(new Color(108, 117, 125)); 
           btnIrAlmacen.setForeground(Color.DARK_GRAY);
           btnIrAlmacen.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        btnFinalizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (subtotalAcumulado == 0) {
                    textAreaVenta.setText(" El carrito está vacío. Agregue productos primero.");
                    return;
                }

               
                double descuento = 0.0;
                if (!txtDescuento.getText().isEmpty()) {
                    try {
                        double porc = Double.parseDouble(txtDescuento.getText());
                        descuento = subtotalAcumulado * (porc / 100);
                    } catch (Exception ex) {}
                }
                double opGravada = subtotalAcumulado - descuento;
                double igv = opGravada * 0.18;
                double totalPagar = opGravada + igv;

                // 2. LUEGO ESCRIBIMOS LA BOLETA (Antes de borrar nada)
                textAreaVenta.setText(
                    "==================================================\n" +
                    "            CAFETERÍA VENDE MÁS - BOLETA          \n" +
                    "==================================================\n" +
                    " CLIENTE: " + txtClienteNombre.getText() + "\n" + 
                    " DNI:     " + txtDniVenta.getText() + "\n" +
                    "--------------------------------------------------\n" +
                    detallesCarrito + 
                    "--------------------------------------------------\n" +
                    " SUBTOTAL:       S/ " + String.format("%.2f", subtotalAcumulado) + "\n" +
                    " DESCUENTO:     -S/ " + String.format("%.2f", descuento) + "\n" +
                    " IGV (18%):     +S/ " + String.format("%.2f", igv) + "\n" +
                    "==================================================\n" +
                    " TOTAL A PAGAR:  S/ " + String.format("%.2f", totalPagar) + "\n" +
                    "==================================================\n" +
                    "        ¡Gracias por su preferencia!            "
                );

                
                detallesCarrito = "";
                subtotalAcumulado = 0.0;
                
                
                txtDniVenta.setText("");
                txtClienteNombre.setText("");
                txtDescuento.setText("");
            }
        });    } 
} 