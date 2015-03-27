package simulador;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;
import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import org.lwjgl.opengl.GLContext;
import static org.lwjgl.system.MemoryUtil.NULL;
import java.util.ArrayList;

public class Pista extends Dibujable{
    private float orientacion;

    public Pista (float pos_x, float pos_y, float pos_z, float tipo_in)
    {
        set_x(pos_x);
        set_y(pos_y);
        set_z(pos_z);
        set_tipo(tipo_in);
    }
   
@Override  
   public void draw() 
   {
        System.out.println("Dibujando pista " + get_length());
        System.out.println("Posición pista " + get_x() + " " + get_y() + " " + get_z() );   
   } 
}
