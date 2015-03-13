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
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glVertex3f;
import org.lwjgl.opengl.GLContext;
import static org.lwjgl.system.MemoryUtil.NULL;
import java.util.ArrayList;

public class Simulador{
    private final ArrayList<Dibujable> objDibujables = new ArrayList<>();
    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback keyCallback;
    // The window handle
    private long window;
    Aeropuerto aero = new Aeropuerto();
    
    
    public void run() {
        try {
            come_alive();
            // Release window and window callbacks
            glfwDestroyWindow(window);
            keyCallback.release();
        } finally {
            // Terminate GLFW and release the GLFWerrorfun
            glfwTerminate();
            errorCallback.release();
        }
    }
    //while run, limpiar dibuja, makinaria init.
    public void initGL() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (glfwInit() != GL11.GL_TRUE) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure our window
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable

        int WIDTH = 600;
        int HEIGHT = 600;

        // Create the window
        window = glfwCreateWindow(WIDTH, HEIGHT, "Aeropuerto", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                    glfwSetWindowShouldClose(window, GL_TRUE); // We will detect this in our rendering loop
                }
            }
        });

        // Get the resolution of the primary monitor
        ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center our window
        glfwSetWindowPos(
                window,
                (GLFWvidmode.width(vidmode) - WIDTH) / 2,
                (GLFWvidmode.height(vidmode) - HEIGHT) / 2
        );

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);
    }

    public void creador_pista (float pos_x, float pos_y, float pos_z)
    {
        Pista pista = new Pista (pos_x,pos_y,pos_z,1);
        System.out.println("Pista añadida"); 
        objDibujables.add(pista);
    }
     
    public void creador_avion(float pos_x, float pos_y, float pos_z, float ide_vuelo) {
        Avion avion = new Avion(pos_x,pos_y,pos_z,2,ide_vuelo);
        System.out.println("Avión añadido");
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
    
    public void draw(){
    //Dibujamos cada uno de los objetos creados:
        aero.drawBackground();
        for (Dibujable objDibujable : objDibujables) {
            objDibujable.draw();
        }
    }
    
    private void come_alive() 
    {
        //Creamos los objetos en las posiciones deseadas:
        creador_pista(3,2,2);
        creador_avion(0.5f,0.5f,0.5f,0001);
        creador_avion(0.3f,0.3f,0.3f,0002);
        creador_torre(0.1f,0.1f,0.1f);
        
        while(true) {
            //mientras no cierres la ventana.
            while (glfwWindowShouldClose(window) == GL_FALSE) {
                GLContext.createFromCurrent();
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // borra el lienzo, clear the framebuffer.
                
                this.draw();
                //Imprimimos aviones y posiciones:
                System.out.println("En el Aeropuerto hay: " + pull_type(2).size());
                for (int i = 0; i < pull_type(2).size(); i++)
                {
                    System.out.println("Avion "+ i + " (" + 
                    pull_type(2).get(i).get_x() + "," +
                    pull_type(2).get(i).get_y() + "," +
                    pull_type(2).get(i).get_z() + ")");   
                }      
                // Poll for window events. The key callback above will only be
                // invoked during this call.
                glfwPollEvents();
                /* Swap buffers and poll Events */
                glfwSwapBuffers(window); // swap the color buffers
                
            }
        }
    }
}