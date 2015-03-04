package simulador;

public class Avion extends Dibujable{
    private float size;
    private float speed;
    private float inclinacion_x, inclinacion_y, inclinacion_z;    
    
    public Avion (float pos_x, float pos_y, float pos_z, float tipo_in, float ide_vuelo)
    {
        set_x(pos_x);
        set_y(pos_y);
        set_z(pos_z);
        set_tipo(tipo_in);
        set_num_vuelo(ide_vuelo);
    }
    @Override 
    public void draw(){
        System.out.println("Dibujando avión... ");
        System.out.println("Posición avión " + get_x() + " " + get_y() + " " + get_z());
    }
    public void volar()
    {
        float x_new = get_x();
        x_new++;
        set_x(x_new);
        
        float y_new = get_y();
        y_new++;
        set_y(y_new);
        
        float z_new = get_z();
        z_new++;
        set_z(z_new);
        
        System.out.println("Aumentando posición en 1..." + get_x() + " " + get_y() + " " + get_z());
    }
}
