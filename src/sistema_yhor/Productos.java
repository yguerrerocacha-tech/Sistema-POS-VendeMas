package sistema_yhor;

public class Productos {
    private int id;
    private int stock;
    private double precio;
    private String nombre;

    public Productos(int id, int stock, double precio, String nombre) {
        this.id = id;
        this.stock = stock;
        this.precio = precio;
        this.nombre = nombre;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}