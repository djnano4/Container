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

    public Avion (float pos_x, float pos_y, float pos_z, float tipo_in, float ide_vuelo, int shader)
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
        //El shader q le metemos al avion es el q creamos en initgl del simulador. Aqui sabemos el shader xq se lo hemos pasado como parametro.
        int posAttrib = glGetAttribLocation(shader, "aVertexPosition");//localiza la puerta entre shader y buffer.
        glEnableVertexAttribArray(posAttrib);//Abre la puerta.
        set_posAttrib(posAttrib);
        
        int vertexColorAttribute = glGetAttribLocation(shader, "aVertexColor");//localiza
        glEnableVertexAttribArray(vertexColorAttribute);
        set_vertex(vertexColorAttribute);
        
        int vbo_v = glGenBuffers();//hazme un sitio en vertex opengl.TUnel.
        set_vbo_v(vbo_v);
        glBindBuffer(GL_ARRAY_BUFFER, vbo_v);//Activa
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);//Meto datos.
        
        int vbo_c = glGenBuffers();//Creas.
        set_vbo_c(vbo_c);
        glBindBuffer(GL_ARRAY_BUFFER, vbo_c);//Activo.
        glBufferData(GL_ARRAY_BUFFER, colors, GL_STATIC_DRAW);//Meto datos.

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
 
        glBindBuffer(GL_ARRAY_BUFFER, (int)get_vbo_v());//activamelo 
        glVertexAttribPointer((int)get_posAttrib(), 3, GL_FLOAT, false, 0, 0);//conectalo con el buffer activo!!!!!!!
        
        glBindBuffer(GL_ARRAY_BUFFER,(int)get_vbo_c());
        glVertexAttribPointer((int)get_vertex(), 3, GL_FLOAT, false, 0, 0);
        
        glDrawArrays(GL_TRIANGLES, 0, 3);//de 3 vertices empezando desde el 0.
    }
}