package sistema_yhor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    
    private static final String URL = "jdbc:mysql://localhost:3306/db_goodmarket";
    private static final String USER = "root";
    private static final String PASSWORD = "Guerrero240320";
    
    public static Connection conectar() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("¡ÉXITO! Conexión establecida con GoodMarket.");
            
        } catch (ClassNotFoundException e) {
            System.out.println("Error: No se encontró el conector .jar  " + e.getMessage());
            
        } catch (SQLException e) {
            System.out.println("Error de Base de Datos (Revisa tu clave o si MySQL está abierto): " + e.getMessage());
        }
        
        return con;
    }
    
    public static void main(String[] args) {
        Conexion prueba = new Conexion();
        prueba.conectar();
    }
}