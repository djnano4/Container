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

public class Simulador extends Dibujable {
    private final ArrayList<Dibujable> objDibujables = new ArrayList<>();
    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback keyCallback;
    // The window handle
    private long window;
    Aeropuerto aero = new Aeropuerto();//Para que el aeropuerto se pueda dibujar.
    private int shaderProgram;
    public void run() {
        try {
            come_alive();//crear y dibujar.
            glfwDestroyWindow(window);
            keyCallback.release();
        } finally {
            glfwTerminate();
            errorCallback.release();
        }
    }

    public void initGL() {
        glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));
        
        if (glfwInit() != GL_TRUE) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

        int WIDTH = 700;
        int HEIGHT = 600;

        window = glfwCreateWindow(WIDTH, HEIGHT, "Aeropuerto", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                    glfwSetWindowShouldClose(window, GL_TRUE); // We will detect this in our rendering loop
                }
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
        
        //Limpiamos.
        GLContext.createFromCurrent();
        glClearColor(0.0f, 1.0f, 1.0f, 0.0f);
        
        //SHADERSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, VertexShaderSrc);
        glCompileShader(vertexShader);
        int status = glGetShaderi(vertexShader, GL_COMPILE_STATUS);
        if (status != GL_TRUE) {
            throw new RuntimeException(glGetShaderInfoLog(vertexShader));
        }
        //SHADERSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, FragmentShaderSrc);
        glCompileShader(fragmentShader);
        status = glGetShaderi(fragmentShader, GL_COMPILE_STATUS);
        if (status != GL_TRUE) {
            throw new RuntimeException(glGetShaderInfoLog(vertexShader));
        }
        //LOS JUNTAMOS:
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glBindFragDataLocation(shaderProgram, 0, "fragColor");
        glLinkProgram(shaderProgram);
        glUseProgram(shaderProgram);     
    } 
    
   static final String VertexShaderSrc
            = "        attribute vec3 aVertexPosition;\n"
            + "        attribute vec3 aVertexColor;\n"
            + "        varying vec4 vColor;\n"
            + "\n"  
            + "        uniform float posX;\n"
            + "\n"
            + "        void main(void) {\n"
            + "            gl_Position = vec4(aVertexPosition.x + posX, aVertexPosition.yz, 1.0);\n"
            + "            vColor = vec4(aVertexColor, 1.0);\n"
            + "        }";

    static final String FragmentShaderSrc
            = "        varying vec4 vColor;\n"
            + "        void main(void) {\n"
            + "            gl_FragColor = vColor;\n"
            + "        }";
     
    public void creador_pista (float pos_x, float pos_y, float pos_z)
    {
        Pista pista = new Pista (pos_x,pos_y,pos_z,1, shaderProgram);
        System.out.println("Pista añadida"); 
        objDibujables.add(pista);
    }
     
    public void creador_avion(float pos_x, float pos_y, float pos_z, float ide_vuelo) {
        Avion avion = new Avion(pos_x,pos_y,pos_z,2,ide_vuelo, shaderProgram);
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
    
    @Override
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
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // borra el lienzo, clear the framebuffer.      
                //dibujamos todo:
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
                glfwSwapBuffers(window);
                
                glfwPollEvents();               
            }
        }
    }
}