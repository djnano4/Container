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
    
    public Pista (float pos_x, float pos_y, float pos_z, float tipo_in, int shader)
    {
        set_x(pos_x);
        set_y(pos_y);
        set_z(pos_z);
        set_tipo(tipo_in);
        
        FloatBuffer vertices = BufferUtils.createFloatBuffer(4 * 4);
        vertices.put(-0.8f).put(-0.2f).put(0f);
        vertices.put(0.8f).put(-0.2f).put(0f);
        vertices.put(0.8f).put(0.2f).put(0f);
        vertices.put(-0.8f).put(0.2f).put(0f);
        vertices.flip();        
        
        FloatBuffer colors = BufferUtils.createFloatBuffer(4 * 4);
        colors.put(0f).put(1f).put(0f);
        colors.put(0f).put(1f).put(1f);
        colors.put(1f).put(0f).put(1f);
        colors.put(1f).put(0f).put(1f);
        colors.flip();
        //El shader q le metemos al avion es el q creamos en initgl del simulador. Aqui sabemos el shader xq se lo hemos pasado como parametro.
        int posAttrib = glGetAttribLocation(shader, "aVertexPosition");//localiza la puerta entre shader y buffer.
        glEnableVertexAttribArray(posAttrib);//Abre la puerta.
        set_posAttrib(posAttrib);
        
        int vertexColorAttribute = glGetAttribLocation(shader, "aVertexColor");//localiza
        glEnableVertexAttribArray(vertexColorAttribute);
        set_vertex(vertexColorAttribute);

        int uniformPosX = glGetUniformLocation(shader, "posX");//localiza
        set_uniform(uniformPosX);
        
        int vbo_v = glGenBuffers();//hazme un sitio en vertex opengl.TUnel.
        set_vbo_v(vbo_v);
        glBindBuffer(GL_ARRAY_BUFFER, vbo_v);//Activa
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);//Meto datos.
        
        int vbo_c = glGenBuffers();//Creas.
        set_vbo_c(vbo_c);
        glBindBuffer(GL_ARRAY_BUFFER, vbo_c);//Activo.
        glBufferData(GL_ARRAY_BUFFER, colors, GL_STATIC_DRAW);//Meto datos.
    }
   
@Override  
   public void draw() 
   {
        System.out.println("Dibujando pista " + get_length());
        System.out.println("Posición pista " + get_x() + " " + get_y() + " " + get_z());
        
        glBindBuffer(GL_ARRAY_BUFFER, (int)get_vbo_v());//activamelo 
        glVertexAttribPointer((int)get_posAttrib(), 3, GL_FLOAT, false, 0, 0);//conectalo con el buffer activo!!!!!!!
        
        glBindBuffer(GL_ARRAY_BUFFER,(int)get_vbo_c());
        glVertexAttribPointer((int)get_vertex(), 3, GL_FLOAT, false, 0, 0);

        glUniform1f(get_uniform(), 0);
        glDrawArrays(GL_QUADS, 0, 4);//de 4 vertices empezando desde el 0.
   } 
}
