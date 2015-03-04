package simulador;

import java.util.ArrayList;
import org.lwjgl.Sys;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
 
import java.nio.ByteBuffer;
 
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Aeropuerto extends Dibujable{
    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback   keyCallback;
    private long window;
    
    private final ArrayList<Dibujable> objDibujables = new ArrayList<>();
    
    //si ponemos final en una clase quiere decir q no puede haber clases q la hereden.
    public Aeropuerto()
    {
    //constructor, de momento vacío.
    }
    
    public void run(){
        try {
            init();
            loop();
            glfwDestroyWindow(window);
            keyCallback.release();
        } finally {
            glfwTerminate();
            errorCallback.release();
        }
    }
    private void init(){
        glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));
        if ( glfwInit() != GL11.GL_TRUE )
            throw new IllegalStateException("Unable to initialize GLFW");
        glfwDefaultWindowHints(); 
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
 
        int WIDTH = 300;
        int HEIGHT = 300;
        
        window = glfwCreateWindow(WIDTH, HEIGHT, "Aeropuerto", NULL, NULL);
        
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");
        glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                    glfwSetWindowShouldClose(window, GL_TRUE); 
            }
        });
        ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(
            window,
            (GLFWvidmode.width(vidmode) - WIDTH) / 2,
            (GLFWvidmode.height(vidmode) - HEIGHT) / 2
        );
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
    }
    private void loop(){
        GLContext.createFromCurrent();//Asociado a un hilo. EL unico q manejo.
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);//Fijamos el color.
        while ( glfwWindowShouldClose(window) == GL_FALSE ) { //MIentras no cierres la pantalla.Limpias continuamente.
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); 
            glfwSwapBuffers(window);
            glfwPollEvents();//Recoge si has exo algo en el raton o teclado.
        }
    }
    
    @Override//Indicamos que vamos a redifinir en esta clase hija un metodo que
    //tiene la clase padre. Cuando se llame a estos metodos, las clases hijas
    //ejecutaran el metodo redefinido en estas y en las que no se hayan redefinido
    //se ejecutara el de la clase padre.
    public void draw() 
    {
       System.out.println("Dibujando Aeropuerto... "); 
       run();
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

    public void come_alive()
    {
        //Creamos los objetos en las posiciones deseadas:
        creador_pista(3,2,2);
        creador_avion(1,1,1,0001);
        creador_avion(2,1,1,0002);
        creador_torre(2,2,2);
        
        while(true) {
        //Dibujamos cada uno de los objetos creados:
        draw();
        for (int i = 0; i < objDibujables.size(); i++)
        {
            objDibujables.get(i).draw();
            
        }
        //Imprimimos aviones y posiciones:
        System.out.println("En el Aeropuerto hay: " + pull_type(2).size());
        for (int i = 0; i < pull_type(2).size(); i++)
        {
            System.out.println("Avion "+ i + " (" + 
                    pull_type(2).get(i).get_x() + "," +
                    pull_type(2).get(i).get_y() + "," +
                    pull_type(2).get(i).get_z() + ")");   
        }
        }
    }
    
}
