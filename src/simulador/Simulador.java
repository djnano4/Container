package simulador;

import Utils.Drawable;
import Utils.FPCameraController;
import Utils.Matrix4f;
import Utils.OpenGLHelper;
import Utils.Shader;
import Utils.ShaderProgram;
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
import java.nio.ByteBuffer;
import org.lwjgl.Sys;
import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWvidmode;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import org.lwjgl.opengl.GLContext;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Simulador extends Dibujable {
    private final ArrayList<Dibujable> objDibujables = new ArrayList<>();
    private final OpenGLHelper openGLHelper = new OpenGLHelper("Aeropuerto");

    Aeropuerto aero = new Aeropuerto();//Para que el aeropuerto se pueda dibujar.

    public void creador_pista (float pos_x, float pos_y, float pos_z)
    {
        Pista pista = new Pista (pos_x,pos_y,pos_z,1, openGLHelper.getShaderProgram().get_id());
        System.out.println("Pista añadida"); 
        objDibujables.add(pista);
    }
     
    public void creador_avion(float pos_x, float pos_y, float pos_z, float ide_vuelo) {
        Avion avion = new Avion(pos_x,pos_y,pos_z,2,ide_vuelo, openGLHelper.getShaderProgram().get_id());
        System.out.println("Avión añadido");
        avion.prepareBuffers(openGLHelper);
        objDibujables.add(avion);
    }
    
    public void creador_torre(float pos_x, float pos_y, float pos_z){
        Torre torre = new Torre (pos_x,pos_y,pos_z,3);
        System.out.println("Torre añadida");
        objDibujables.add(torre);
    }
    
    public ArrayList<Dibujable> pull_type (int tipo_in) 
    {
        //Array para guardar los objetos dibujables que quieras sacar.
        final ArrayList<Dibujable> outDibujables;
        outDibujables = new ArrayList<>();
        //Buscamos en el array los objetos de ese tipo.
        //Recuerda pista1 avion2 torre3
        for (int i = 0; i < objDibujables.size(); i++)
        {
            //la forma de coger el objeto es con get.
            if (tipo_in == objDibujables.get(i).get_tipo()){
                outDibujables.add(objDibujables.get(i));
            } 
        }
        return outDibujables;    
    }
    
    @Override
    public void draw(){
    //Dibujamos cada uno de los objetos creados:
        aero.drawBackground();
        for (Dibujable objDibujable : objDibujables) {
            objDibujable.draw();
        }
    }
    
    public void come_alive() 
    {
        openGLHelper.initGL("VS_Texture.vs", "FS_Texture.fs");
        //Creamos los objetos y preparamos buffer en las posiciones deseadas:
        creador_pista(3,2,2);
        creador_avion(0.5f,0.5f,0.5f,0001);
        creador_avion(0.3f,0.3f,0.3f,0002);
        creador_torre(0.1f,0.1f,0.1f);
        //Llamamos a run-loop-draw
        openGLHelper.run((Drawable)objDibujables.get(2));
        
    }
}