package simulador;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3f;

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

        glColor3f(1.0f,1.0f,1.0f);
        glBegin(GL_QUADS);
            glVertex3f(get_x() -0.2f,get_y() + 0.2f, 0f);                
            glVertex3f(get_x() + 0.2f,get_y() + 0.2f, 0f);
            glVertex3f(get_x() + 0.2f,get_y() -0.2f, 0f);
            glVertex3f(get_x() -0.2f,get_y() -0.2f, 0f);
        glEnd();
    }   
   
}