package sistema_yhor;
import java.util.ArrayList;

public class AlmacenService {
    public static ArrayList<Productos> lista = new ArrayList<>();

    public boolean insertar(int id, String desc, double precio, int stock) {
        if (buscarPorId(id) != null) return false;
        lista.add(new Productos(id, stock, precio, desc));
        return true;
    }

    // Sobrecarga 
    public boolean insertar(int id, String desc) {
        return insertar(id, desc, 0.0, 0); 
    }

    public static Productos buscarPorId(int id) {
        for (Productos p : lista) {
            if (p.getId() == id) return p;
        }
        return null;
    }
}