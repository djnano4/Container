package simulador;
//declaro q quiero hacerlo pero no tengo la capacidad de hacerlo, ya mis hijos..
public abstract class Dibujable {   
    private float x,y,z;
    private float length;
    private float tipo;
    private float num_vuelo;
    public abstract void draw(); 
   //getters and setters    
   public float get_x()
    {
        return x;
    }
    public void set_x(float v)
    {
        this.x = v;
    }
    public float get_y()
    {
        return y;
    }
    public void set_y(float v)
    {
        this.y = v;
    }
    public float get_z()
    {
        return z;
    }
    public void set_z(float v)
    {
        this.z = v;
    }
    public float get_length()
    {
        return length;
    }
    public void set_length(float v)
    {
        this.length = v;
    }
    public float get_tipo()
    {
        return tipo;
    }
    public void set_tipo(float v)
    {
        this.tipo = v;
    }
    public float get_num_vuelo()
    {
        return num_vuelo;
    }
    public void set_num_vuelo(float v)
    {
        this.num_vuelo = v;
    }    
   /* public float get_vbo_v()
    {
        return vbo_v;
    }
    public void set_vbo_v(int v)
    {
        this.vbo_v = v;
    }
    public float get_vbo_c()
    {
        return vbo_c;
    }
    public void set_vbo_c(int v)
    {
        this.vbo_c = v;
    }*/
}
//variable global--> public static int esperanzavida;
//sin static tienes q crear el objeto calculadora q te suma, quien te suma no es la clase es le objeto,
//Calc calc = new Calc()
//calc.suma() Conveniente para guardar datos.
//si pones static puedes decirle a la clase q te sume:
//Calc.suma()NO DEPENDE DEL ESTADO.
//void no devuelvo nada.