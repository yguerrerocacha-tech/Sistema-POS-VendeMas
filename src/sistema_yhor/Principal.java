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
import javax.swing.JComboBox;

public class Principal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsuario;
	private JPasswordField txtContraseña;
	
	private int intentosFallidos = 0;
	private boolean sistemaBloqueado = false;
	private int segundosRestantes = 0;

	public static String usuarioActivo = "";
	public static String rolActivo = "";
	public static String turnoActivo = "";

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
		setBounds(100, 100, 750, 520); 
		
		contentPane = new JPanel();
		contentPane.setBackground(new Color(24, 28, 36));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panelContenedor = new JPanel();
		panelContenedor.setBackground(new Color(32, 38, 50));
		panelContenedor.setBounds(40, 25, 660, 425);
		panelContenedor.setBorder(new LineBorder(new Color(45, 55, 72), 1, true));
		contentPane.add(panelContenedor);
		panelContenedor.setLayout(null);

		JLabel lblLogo = new JLabel("");
		lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
		lblLogo.setIcon(new ImageIcon(Principal.class.getResource("/imagen/Sin título.jpg")));
		lblLogo.setBounds(40, 50, 240, 320);
		panelContenedor.add(lblLogo);

		JLabel lblBienvenido = new JLabel("INICIO DE SESIÓN");
		lblBienvenido.setForeground(new Color(243, 244, 246));
		lblBienvenido.setHorizontalAlignment(SwingConstants.CENTER);
		lblBienvenido.setFont(new Font("Segoe UI", Font.BOLD, 22));
		lblBienvenido.setBounds(320, 25, 300, 30);
		panelContenedor.add(lblBienvenido);

		JLabel lblUser = new JLabel("USUARIO");
		lblUser.setForeground(new Color(156, 163, 175));
		lblUser.setFont(new Font("Segoe UI", Font.BOLD, 11));
		lblUser.setBounds(330, 75, 200, 20);
		panelContenedor.add(lblUser);

		txtUsuario = new JTextField();
		txtUsuario.setCaretColor(Color.WHITE);
		txtUsuario.setForeground(Color.WHITE);
		txtUsuario.setBackground(new Color(21, 25, 32));
		txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtUsuario.setBorder(new LineBorder(new Color(55, 65, 81), 1, true));
		txtUsuario.setBounds(330, 95, 280, 35);
		panelContenedor.add(txtUsuario);
		txtUsuario.setColumns(10);

		JLabel lblPass = new JLabel("CONTRASEÑA");
		lblPass.setForeground(new Color(156, 163, 175));
		lblPass.setFont(new Font("Segoe UI", Font.BOLD, 11));
		lblPass.setBounds(330, 140, 200, 20);
		panelContenedor.add(lblPass);

		txtContraseña = new JPasswordField();
		txtContraseña.setCaretColor(Color.WHITE);
		txtContraseña.setForeground(Color.WHITE);
		txtContraseña.setBackground(new Color(21, 25, 32));
		txtContraseña.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtContraseña.setBorder(new LineBorder(new Color(55, 65, 81), 1, true));
		txtContraseña.setBounds(330, 160, 280, 35);
		panelContenedor.add(txtContraseña);

		// 1. BOTÓN PUNTO DE VENTA (CAJA) - EVALÚA ROLES Y TURNOS AUTOMÁTICAMENTE
		JButton btnVenta = new JButton("Punto de Venta (Caja)");
		btnVenta.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnVenta.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnVenta.setBackground(new Color(37, 99, 235)); 
		btnVenta.setForeground(Color.WHITE);
		btnVenta.setFocusPainted(false);
		btnVenta.setBorder(new LineBorder(new Color(37, 99, 235), 1, true));
		btnVenta.setBounds(330, 215, 280, 38);
		
		btnVenta.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) { btnVenta.setBackground(new Color(29, 78, 216)); }
			public void mouseExited(MouseEvent e) { btnVenta.setBackground(new Color(37, 99, 235)); }
		});
		
		btnVenta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (verificarBloqueo()) return;
				
				String usuario = txtUsuario.getText().trim();
				String password = new String(txtContraseña.getPassword());
				
				if (usuario.isEmpty() || password.isEmpty()) {
					JOptionPane.showMessageDialog(null, "⚠️ Por favor, ingresa tu usuario y contraseña.");
					return;
				}
				
				try {
					Connection conexion = Conexion.conectar();
					if (conexion != null) {
						String sql = "SELECT username, rol FROM usuarios WHERE username = ? AND password_hash = ?";
						PreparedStatement pst = conexion.prepareStatement(sql);
						pst.setString(1, usuario);
						pst.setString(2, password);
						
						ResultSet rs = pst.executeQuery();
						if (rs.next()) {
							usuarioActivo = rs.getString("username");
							rolActivo = rs.getString("rol");
							
							// MECÁNICA DE SELECCIÓN DE TURNO EN VIVO
							String[] turnos = {"MAÑANA", "TARDE"};
							String opcionTurno = (String) JOptionPane.showInputDialog(null, 
								"Seleccione su Turno de Trabajo para la apertura de caja:", 
								"Apertura de Turno - VendeMás", JOptionPane.QUESTION_MESSAGE, null, turnos, turnos[0]);
							
							if (opcionTurno == null) return; 
							turnoActivo = opcionTurno;

							JOptionPane.showMessageDialog(null, "¡Sesión iniciada con éxito!\nCajero: " + usuarioActivo + "\nTurno: " + turnoActivo);
							
							Conexion.usuarioLogueado = true;
							intentosFallidos = 0; 
							
							Venta ventanaVentas = new Venta();
							ventanaVentas.setLocationRelativeTo(null);
							
							ventanaVentas.addWindowListener(new WindowAdapter() {
								public void windowClosed(WindowEvent e) {
									Conexion.usuarioLogueado = false;
									txtUsuario.setText("");
									txtContraseña.setText("");
									Principal.this.setVisible(true);
								}
							});
							
							ventanaVentas.setVisible(true);
							Principal.this.setVisible(false); 
						} else {
							procesarIntentoFallido();
						}
						conexion.close();
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Error de conexión: " + ex.getMessage());
				}
			}
		});
		panelContenedor.add(btnVenta);

		JButton btnAlmacen = new JButton("Gestión de Almacén");
		btnAlmacen.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); 
		btnAlmacen.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnAlmacen.setBackground(new Color(75, 85, 99)); 
		btnAlmacen.setForeground(Color.WHITE);
		btnAlmacen.setFocusPainted(false);
		btnAlmacen.setBorder(new LineBorder(new Color(75, 85, 99), 1, true));
		btnAlmacen.setBounds(330, 265, 280, 38);
		
		btnAlmacen.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) { btnAlmacen.setBackground(new Color(55, 65, 81)); }
			public void mouseExited(MouseEvent e) { btnAlmacen.setBackground(new Color(75, 85, 99)); }
		});
		
		btnAlmacen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (verificarBloqueo()) return;

				String usuario = txtUsuario.getText().trim();
				String password = new String(txtContraseña.getPassword());
				
				if (usuario.isEmpty() || password.isEmpty()) {
					JOptionPane.showMessageDialog(null, "⚠ Ingrese usuario y contraseña para entrar al Almacén.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				try {
					Connection conexion = Conexion.conectar();
					if (conexion != null) {
						String sql = "SELECT username, rol FROM usuarios WHERE username = ? AND password_hash = ? AND rol = 'ADMINISTRADOR'";
						PreparedStatement pst = conexion.prepareStatement(sql);
						pst.setString(1, usuario);
						pst.setString(2, password);
						
						ResultSet rs = pst.executeQuery();
						if (rs.next()) {
							usuarioActivo = rs.getString("username");
							rolActivo = rs.getString("rol");
							turnoActivo = "ADMINISTRATIVO";

							JOptionPane.showMessageDialog(null, "Acceso concedido al Almacén, Administrador " + usuario);
							
							Conexion.usuarioLogueado = true;
							intentosFallidos = 0;
							
							V1 ventanaAlmacen = new V1();
							ventanaAlmacen.setLocationRelativeTo(null);
							
							ventanaAlmacen.addWindowListener(new WindowAdapter() {
								@Override
								public void windowClosed(WindowEvent e) {
									Conexion.usuarioLogueado = false;
									txtUsuario.setText("");
									txtContraseña.setText("");
									Principal.this.setVisible(true);
								}
							});
							
							ventanaAlmacen.setVisible(true);
							Principal.this.setVisible(false); 
						} else {
							procesarIntentoFallido();
						}
						conexion.close();
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Error de conexión: " + ex.getMessage());
				}
			}
		});
		panelContenedor.add(btnAlmacen);

		// 3. NUEVO PANEL / BOTÓN: SISTEMA DE REGISTRO EXCLUSIVO PARA ADMINISTRADORES
		JButton btnRegistrarUser = new JButton("🛠️ Registrar Personal (Solo Admin)");
		btnRegistrarUser.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnRegistrarUser.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnRegistrarUser.setBackground(new Color(15, 118, 110)); // Color verde pavo real ejecutivo
		btnRegistrarUser.setForeground(Color.WHITE);
		btnRegistrarUser.setFocusPainted(false);
		btnRegistrarUser.setBorderPainted(false);
		btnRegistrarUser.setBounds(330, 315, 280, 35);
		btnRegistrarUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String adminUser = JOptionPane.showInputDialog(null, "🔐 Ingrese Usuario Administrador de Autorización:", "Control de Seguridad", JOptionPane.WARNING_MESSAGE);
				String adminPass = JOptionPane.showInputDialog(null, "🔑 Ingrese Contraseña de Autorización:", "Control de Seguridad", JOptionPane.WARNING_MESSAGE);
				
				try {
					Connection cn = Conexion.conectar();
					PreparedStatement pst = cn.prepareStatement("SELECT * FROM usuarios WHERE username=? AND password_hash=? AND rol='ADMINISTRADOR'");
					pst.setString(1, adminUser);
					pst.setString(2, adminPass);
					ResultSet rs = pst.executeQuery();
					
					if (rs.next()) {
						JTextField fieldNewUser = new JTextField();
						JTextField fieldNewPass = new JTextField();
						String[] rolesDisponibles = {"ADMINISTRADOR", "CAJERO"};
						JComboBox<String> comboRoles = new JComboBox<>(rolesDisponibles);
						
						Object[] formularioRegistro = {
							"Nombre de Usuario del nuevo personal:", fieldNewUser,
							"Contraseña de Acceso:", fieldNewPass,
							"Rol Asignado en VendeMás:", comboRoles
						};
						
						int seleccion = JOptionPane.showConfirmDialog(null, formularioRegistro, "Alta de Nuevo Personal", JOptionPane.OK_CANCEL_OPTION);
						if (seleccion == JOptionPane.OK_OPTION) {
							if (fieldNewUser.getText().trim().isEmpty() || fieldNewPass.getText().trim().isEmpty()) {
								JOptionPane.showMessageDialog(null, "❌ Error: No se permiten campos vacíos.");
								return;
							}
							
							PreparedStatement pstInsert = cn.prepareStatement("INSERT INTO usuarios (username, password_hash, rol) VALUES (?,?,?)");
							pstInsert.setString(1, fieldNewUser.getText().trim());
							pstInsert.setString(2, fieldNewPass.getText().trim());
							pstInsert.setString(3, comboRoles.getSelectedItem().toString());
							pstInsert.executeUpdate();
							
							JOptionPane.showMessageDialog(null, "¡Personal registrado con éxito en el sistema!");
						}
					} else {
						JOptionPane.showMessageDialog(null, " AUTORIZACIÓN DENEGADA. Credenciales de administrador incorrectas.", "Seguridad VendeMás", JOptionPane.ERROR_MESSAGE);
					}
					cn.close();
				} catch(Exception ex) {
					JOptionPane.showMessageDialog(null, "Error al registrar usuario: " + ex.getMessage());
				}
			}
		});
		panelContenedor.add(btnRegistrarUser);

		// 4. BOTÓN SALIR
		JButton btnSalir = new JButton("Salir del Sistema");
		btnSalir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnSalir.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnSalir.setBackground(new Color(31, 41, 55)); 
		btnSalir.setForeground(new Color(239, 68, 68));
		btnSalir.setFocusPainted(false);
		btnSalir.setBorder(new LineBorder(new Color(55, 65, 81), 1, true));
		btnSalir.setBounds(330, 365, 280, 32);
		
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

	private boolean verificarBloqueo() {
		if (sistemaBloqueado) {
			JOptionPane.showMessageDialog(null, "⏳ ACCESO SUSPENDIDO. Espere a que termine la penalización por seguridad.", "Seguridad VendeMás", JOptionPane.ERROR_MESSAGE);
			return true;
		}
		if (intentosFallidos >= 3) {
			JOptionPane.showMessageDialog(null, "🔒 Demasiados intentos fallidos. Sistema bloqueado por seguridad.", "Seguridad VendeMás", JOptionPane.ERROR_MESSAGE);
			iniciarTemporizadorBloqueo();
			return true;
		}
		return false;
	}

	private void procesarIntentoFallido() {
		intentosFallidos++;
		int restantes = 3 - intentosFallidos;
		if (intentosFallidos >= 3) {
			JOptionPane.showMessageDialog(null, "🔒 Demasiados intentos fallidos. Sistema bloqueado por 15 segundos.", "Seguridad VendeMás", JOptionPane.ERROR_MESSAGE);
			iniciarTemporizadorBloqueo();
		} else {
			JOptionPane.showMessageDialog(null, "Credenciales incorrectas o Privilegios Insuficientes.\nIntentos restantes: " + restantes, "Error de Acceso", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void iniciarTemporizadorBloqueo() {
		sistemaBloqueado = true;
		segundosRestantes = 15; 
		
		txtUsuario.setEnabled(false);
		txtContraseña.setEnabled(false);

		javax.swing.Timer timer = new javax.swing.Timer(1000, null);
		timer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				segundosRestantes--;
				
				if (segundosRestantes > 0) {
					setTitle("VendeMás - BLOQUEADO Temporalmente (" + segundosRestantes + "s)");
				} else {
					timer.stop();
					sistemaBloqueado = false;
					intentosFallidos = 0;
					
					txtUsuario.setEnabled(true);
					txtContraseña.setEnabled(true);
					txtUsuario.setText("");
					txtContraseña.setText("");
					setTitle("VendeMás - Pantalla de Inicio");
					
					JOptionPane.showMessageDialog(null, "🔄 Sistema desbloqueado. Puede intentar iniciar sesión nuevamente.", "Seguridad VendeMás", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		timer.start();
	}
}