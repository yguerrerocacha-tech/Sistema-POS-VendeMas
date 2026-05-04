package sistema_yhor;

public class VentaService {
    public static double calcularTotal(double precio, int cant) {
        return precio * cant;
    }

    public static double aplicarDescuento(double subtotal, double pct) {
        return subtotal * (1 - (pct / 100));
    }

    
    public static double aplicarDescuento(double subtotal) {
        return aplicarDescuento(subtotal, 10.0);
    }
}