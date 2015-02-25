package simulador;

public class Torre extends Dibujable{ 
    private float altura;
    
    public Torre (float pos_x, float pos_y, float pos_z, float tipo_in)
    {
        set_x(pos_x);
        set_y(pos_y);
        set_z(pos_z);
        set_tipo(tipo_in);
    }
    
@Override    
   public void draw() {
        System.out.println("Dibujando torre... ");
        System.out.println("Posición torre " + get_x() + " " + get_y() + " " + get_z());
    }   
   
}