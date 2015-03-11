package simulador;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3f;

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
        glColor3f(1.0f,0.0f,1.0f);
        glBegin(GL_QUADS);
            glColor3f(0f, 1f, 0f);
            glVertex3f(get_x() -0.1f, get_y() -0.1f, 0f);
            glVertex3f(get_x() +0.1f, get_y() -0.1f, 0f);
            glVertex3f(get_x() +0f, get_y() +0.2f, 0f);
        glEnd();
        
   } 
}
