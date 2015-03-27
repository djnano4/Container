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
        
        FloatBuffer vertices = BufferUtils.createFloatBuffer(3 * 3);
        vertices.put(-0.6f).put(-0.4f).put(0f);
        vertices.put(0.6f).put(-0.4f).put(0f);
        vertices.put(0f).put(0.6f).put(0f);
        vertices.flip();        
        
        FloatBuffer colors = BufferUtils.createFloatBuffer(3 * 3);
        colors.put(0f).put(1f).put(0f);
        colors.put(0f).put(1f).put(1f);
        colors.put(1f).put(0f).put(1f);
        colors.flip();
        
        int vbo_v = glGenBuffers();//hazme un sitio en vertex opengl.TUnel.
        set_vbo_v(vbo_v);
        glBindBuffer(GL_ARRAY_BUFFER, vbo_v);//Activa
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        
        int vbo_c = glGenBuffers();
        set_vbo_c(vbo_c);
        glBindBuffer(GL_ARRAY_BUFFER, vbo_c);
        glBufferData(GL_ARRAY_BUFFER, colors, GL_STATIC_DRAW);
        
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
    
     @Override 
    public void draw(){
        System.out.println("Dibujando avión... ");
        System.out.println("Posición avión " + get_x() + " " + get_y() + " " + get_z()); 
 
        glBindBuffer(GL_ARRAY_BUFFER, vbo_v);//activamelo
        glVertexAttribPointer(posAttrib, 3, GL_FLOAT, false, 0, 0);//conectalo !!!!!!!
  
        glBindBuffer(GL_ARRAY_BUFFER,vbo_c);
        glVertexAttribPointer(vertexColorAttribute, 3, GL_FLOAT, false, 0, 0);
        
        glDrawArrays(GL_TRIANGLES, 0, 3);
        
    }
}