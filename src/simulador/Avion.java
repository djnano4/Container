package simulador;

import java.nio.ByteBuffer;
import org.lwjgl.Sys;
import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;
import org.lwjgl.opengl.GLContext;
import static org.lwjgl.system.MemoryUtil.NULL;

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
        //Color de mi objeto.
        glColor3f(0.0f,1.0f,1.0f);
        /* Render triangle */
        //Para moverlo el triangulo hay q poner las coordenadas.
        glBegin(GL_TRIANGLES);
            glColor3f(0f, 1f, 0f);
            glVertex3f(get_x() -0.1f, get_y() -0.1f, 0f);
            glVertex3f(get_x() +0.1f, get_y() -0.1f, 0f);
            glVertex3f(get_x() +0f, get_y() +0.2f, 0f);
        glEnd();
    }
}