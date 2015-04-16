package simulador;

import Utils.Drawable;
import Utils.*;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.BufferUtils;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.GL_TEXTURE2;
import static org.lwjgl.opengl.GL13.GL_TEXTURE3;
import static org.lwjgl.opengl.GL13.GL_TEXTURE4;
import static org.lwjgl.opengl.GL13.GL_TEXTURE5;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;


public class Avion extends Dibujable{
    private float size;
    private float speed;
    private float inclinacion_x, inclinacion_y, inclinacion_z;   
    private float count;
    private float angle;
    private static final float angularVelocity = 30.f;
    private ShaderProgram shaderProgram;
    private int uniModel;
    private int uNMatrixAttribute;
    private OpenGLHelper openGLHelper;
    private Texture texture0, texture1, texture2;
    private int cubeVao, axiesVao, sphereVao, planeVao, roadVao;
    private int uKdAttribute;
    private int uKaAttribute;
    private int useTextures;
    private int uniTex1, uniTex2, uniTex3;
    
    private final SphereModel sphereModel = new SphereModel();
    private final CubeModel cubeModel = new CubeModel();
    private final PlaneModel planeModel = new PlaneModel(5, 5);
    private final PlaneModel roadModel = new PlaneModel(5, 1);
    
    private Texture fieldstoneTexture, stoneTexture, nicegrasstexture, roadTexture;
    
    float[] vertices = new float[]{
         1.0f,  1.0f, 1.0f,  // 0 - Top Right Of The Quad (Front)
        -1.0f,  1.0f, 1.0f,  // 1 - Top Left Of The Quad (Front)
        -1.0f, -1.0f, 1.0f,  // 2 - Bottom Left Of The Quad (Front)
         1.0f, -1.0f, 1.0f,  // 3 - Bottom Right Of The Quad (Front)

         1.0f,  1.0f, -1.0f, // 4 - Top Right Of The Quad (Top)
        -1.0f,  1.0f, -1.0f, // 5 - Top Left Of The Quad (Top)
        -1.0f,  1.0f,  1.0f, // 6 - Bottom Left Of The Quad (Top)
         1.0f,  1.0f,  1.0f, // 7 - Bottom Right Of The Quad (Top)

         -1.0f, 1.0f,  1.0f, // 8 - Top Right Of The Quad (Left)
        -1.0f,  1.0f, -1.0f, // 9 - Top Left Of The Quad (Left)
        -1.0f, -1.0f, -1.0f, // 10 - Bottom Left Of The Quad (Left)
        -1.0f, -1.0f,  1.0f, // 11 - Bottom Right Of The Quad (Left)
        
         1.0f, -1.0f, -1.0f, // 12 - Bottom Left Of The Quad (Back)
        -1.0f, -1.0f, -1.0f, // 13 - Bottom Right Of The Quad (Back)
        -1.0f,  1.0f, -1.0f, // 14 - Top Right Of The Quad (Back)
         1.0f,  1.0f, -1.0f, // 15 - Top Left Of The Quad (Back)
        
         1.0f, -1.0f,  1.0f, // 16 - Top Right Of The Quad (Bottom)
        -1.0f, -1.0f,  1.0f, // 17 - Top Left Of The Quad (Bottom)
        -1.0f, -1.0f, -1.0f, // 18 - Bottom Left Of The Quad (Bottom)
         1.0f, -1.0f, -1.0f, // 19 - Bottom Right Of The Quad (Bottom)

         1.0f,  1.0f, -1.0f, // 20 - Top Right Of The Quad (Right)
         1.0f,  1.0f,  1.0f, // 21 - Top Left Of The Quad (Right)
         1.0f, -1.0f,  1.0f, // 22 - Bottom Left Of The Quad (Right)
         1.0f, -1.0f, -1.0f  // 23 - Bottom Right Of The Quad (Right)
    };

    float[] colors = new float[]{
        0.9f, 0.9f, 0.9f, // Top Right Of The Quad (Front)
        0.9f, 0.9f, 0.9f, // Top Left Of The Quad (Front)
        0.9f, 0.9f, 0.9f, // Bottom Left Of The Quad (Front)
        0.9f, 0.9f, 0.9f, // Bottom Right Of The Quad (Front)

        1.0f, 1.0f, 1.0f, // Top Right Of The Quad (Top)
        1.0f, 1.0f, 1.0f, // Top Left Of The Quad (Top)
        1.0f, 1.0f, 1.0f, // Bottom Left Of The Quad (Top)
        1.0f, 1.0f, 1.0f, // Bottom Right Of The Quad (Top)

        0.7f, 0.7f, 0.7f, // Top Right Of The Quad (Left)
        0.7f, 0.7f, 0.7f, // Top Left Of The Quad (Left)
        0.7f, 0.7f, 0.7f, // Bottom Left Of The Quad (Left)
        0.7f, 0.7f, 0.7f, // Bottom Right Of The Quad (Left)

        0.7f, 0.7f, 0.7f, // Bottom Left Of The Quad (Back)
        0.7f, 0.7f, 0.7f, // Bottom Right Of The Quad (Back)
        0.7f, 0.7f, 0.7f, // Top Right Of The Quad (Back)
        0.7f, 0.7f, 0.7f, // Top Left Of The Quad (Back)

        0.5f, 0.5f, 0.5f, // Top Right Of The Quad (Bottom)
        0.5f, 0.5f, 0.5f, // Top Left Of The Quad (Bottom)
        0.5f, 0.5f, 0.5f, // Bottom Left Of The Quad (Bottom)
        0.5f, 0.5f, 0.5f, // Bottom Right Of The Quad (Bottom)

        0.9f, 0.9f, 0.9f, // Top Right Of The Quad (Right)
        0.9f, 0.9f, 0.9f, // Top Left Of The Quad (Right)
        0.9f, 0.9f, 0.9f, // Bottom Left Of The Quad (Right)
        0.9f, 0.9f, 0.9f  // Bottom Right Of The Quad (Right)
    };

    float[] textCoords = new float[]{
        1.0f, 1.0f, // Top Right Of The Quad (Front)
        0.0f, 1.0f, // Top Left Of The Quad (Front)
        0.0f, 0.0f, // Bottom Left Of The Quad (Front)
        1.0f, 0.0f, // Bottom Right Of The Quad (Front)

        1.0f, 1.0f, // Top Right Of The Quad (Top)
        0.0f, 1.0f, // Top Left Of The Quad (Top)
        0.0f, 0.0f, // Bottom Left Of The Quad (Top)
        1.0f, 0.0f, // Bottom Right Of The Quad (Top)

        1.0f, 1.0f, // Top Right Of The Quad (Left)
        0.0f, 1.0f, // Top Left Of The Quad (Left)
        0.0f, 0.0f, // Bottom Left Of The Quad (Left)
        1.0f, 0.0f, // Bottom Right Of The Quad (Left)

        1.0f, 1.0f, // Bottom Left Of The Quad (Back)
        0.0f, 1.0f, // Bottom Right Of The Quad (Back)
        0.0f, 0.0f, // Top Right Of The Quad (Back)
        1.0f, 0.0f, // Top Left Of The Quad (Back)

        1.0f, 1.0f, // Top Right Of The Quad (Bottom)
        0.0f, 1.0f, // Top Left Of The Quad (Bottom)
        0.0f, 0.0f, // Bottom Left Of The Quad (Bottom)
        1.0f, 0.0f, // Bottom Right Of The Quad (Bottom)

        1.0f, 1.0f, // Top Right Of The Quad (Right)
        0.0f, 1.0f, // Top Left Of The Quad (Right)
        0.0f, 0.0f, // Bottom Left Of The Quad (Right)
        1.0f, 0.0f, // Bottom Right Of The Quad (Right)
    };
    
    int[] elements  = new int[]{
        0, 1, 2, // Top Right, Top Left, Bottom Left Of The Quad (Front)
        0, 2, 3, // Top Right, Bottom Left, Bottom Right Of The Quad (Front)
        
        4, 5, 6, // Top Right, Top Left, Bottom Left Of The Quad (Top)
        4, 6, 7, // Top Right, Bottom Left, Bottom Right Of The Quad(Top)

        8,  9, 10, // Top Right, Top Left, Bottom Left Of The Quad (Left)
        8, 10, 11, // Top Right, Bottom Left, Bottom Right Of The Quad (Left)

        12, 13, 14, // Top Right, Top Left, Bottom Left Of The Quad (Back)
        12, 14, 15, // Top Right, Bottom Left, Bottom Right Of The Quad (Back)
       
        16, 17, 18, // Top Right, Top Left, Bottom Left Of The Quad (Bottom)
        16, 18, 19, // Top Right, Bottom Left, Bottom Right Of The Quad (Bottom)

        20, 21, 22, // Top Right, Top Left, Bottom Left Of The Quad (Right)
        20, 22, 23, // Top Right, Bottom Left, Bottom Right Of The Quad (Right)
};

    public Avion (float pos_x, float pos_y, float pos_z, float tipo_in, float ide_vuelo, int shader)
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
        
        System.out.println("AumentprepareBuffersando posición en 1..." + get_x() + " " + get_y() + " " + get_z());
    }
    
    public void prepareBuffers(OpenGLHelper openGLHelper_param) {
        
        // --------------------- AXIES MODEL  -------------------------------//
        openGLHelper = openGLHelper_param;
        shaderProgram = openGLHelper.getShaderProgram();
        int posAttrib = shaderProgram.getAttributeLocation("aVertexPosition");
        int vertexNormalAttribute = shaderProgram.getAttributeLocation("aVertexNormal");
        int texCoordsAttribute = shaderProgram.getAttributeLocation("aVertexTexCoord");

        axiesVao = glGenVertexArrays();
        glBindVertexArray(axiesVao);

        int vbo_v = cubeModel.createVerticesBuffer();
        glBindBuffer(GL_ARRAY_BUFFER, vbo_v);
        glEnableVertexAttribArray(posAttrib);
        glVertexAttribPointer(posAttrib, 3, GL_FLOAT, false, 0, 0);

        int vbo_n = cubeModel.createNormalsBuffer();
        glBindBuffer(GL_ARRAY_BUFFER, vbo_n);
        glEnableVertexAttribArray(vertexNormalAttribute);
        glVertexAttribPointer(vertexNormalAttribute, 3, GL_FLOAT, false, 0, 0);

        int ebo = cubeModel.createIndicesBuffer();
        glBindVertexArray(0);

        uniModel = shaderProgram.getUniformLocation("model");
        uNMatrixAttribute = shaderProgram.getUniformLocation("uNMatrix");
        useTextures = shaderProgram.getUniformLocation("useTextures");

        // ----------------------- CUBE MODEL -----------------------------//
        cubeVao = glGenVertexArrays();
        glBindVertexArray(cubeVao);

        glBindBuffer(GL_ARRAY_BUFFER, vbo_v);
        glEnableVertexAttribArray(posAttrib);
        glVertexAttribPointer(posAttrib, 3, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, vbo_n);
        glEnableVertexAttribArray(vertexNormalAttribute);
        glVertexAttribPointer(vertexNormalAttribute, 3, GL_FLOAT, false, 0, 0);

        int vbo_t = cubeModel.createTextCoordsBuffer();
        glBindBuffer(GL_ARRAY_BUFFER, vbo_t);
        glEnableVertexAttribArray(texCoordsAttribute);
        glVertexAttribPointer(texCoordsAttribute, 2, GL_FLOAT, false, 0, 0);

        ebo = cubeModel.createIndicesBuffer();

        glActiveTexture(GL_TEXTURE0);
        nicegrasstexture = Texture.loadTexture("nicegrass.jpg");
        uniTex1 = shaderProgram.getUniformLocation("Texture1");
        shaderProgram.setUniform(uniTex1, 0);

        glActiveTexture(GL_TEXTURE1);
        Texture texture = Texture.loadTexture("baserock.jpg");
        uniTex2 = shaderProgram.getUniformLocation("Texture2");
        shaderProgram.setUniform(uniTex2, 1);

        glActiveTexture(GL_TEXTURE2);
        texture = Texture.loadTexture("darkrockalpha.png");
        uniTex3 = shaderProgram.getUniformLocation("Texture3");
        shaderProgram.setUniform(uniTex3, 2);

        glBindVertexArray(0);
        
        glActiveTexture(GL_TEXTURE3);
        fieldstoneTexture = Texture.loadTexture("fieldstone.jpg");
        
        glActiveTexture(GL_TEXTURE4);
        stoneTexture = Texture.loadTexture("stone-128px.jpg");
        
        glActiveTexture(GL_TEXTURE5);
        roadTexture = Texture.loadTexture("stone-256px.jpg");
        
        // ----------------------- SPHERE MODEL -----------------------------//
        sphereVao = glGenVertexArrays();
        glBindVertexArray(sphereVao);

        vbo_v = sphereModel.createVerticesBuffer();
        glBindBuffer(GL_ARRAY_BUFFER, vbo_v);
        glEnableVertexAttribArray(posAttrib);
        glVertexAttribPointer(posAttrib, 3, GL_FLOAT, false, 0, 0);

        vbo_n = sphereModel.createNormalsBuffer();
        glBindBuffer(GL_ARRAY_BUFFER, vbo_n);
        glEnableVertexAttribArray(vertexNormalAttribute);
        glVertexAttribPointer(vertexNormalAttribute, 3, GL_FLOAT, false, 0, 0);

        ebo = sphereModel.createIndicesBuffer();
        glBindVertexArray(0);
        
        // ----------------------- PLANE MODEL -----------------------------//
        planeVao = glGenVertexArrays();
        glBindVertexArray(planeVao);

        vbo_v = planeModel.createVerticesBuffer();
        glBindBuffer(GL_ARRAY_BUFFER, vbo_v);
        glEnableVertexAttribArray(posAttrib);
        glVertexAttribPointer(posAttrib, 3, GL_FLOAT, false, 0, 0);

        vbo_n = planeModel.createNormalsBuffer();
        glBindBuffer(GL_ARRAY_BUFFER, vbo_n);
        glEnableVertexAttribArray(vertexNormalAttribute);
        glVertexAttribPointer(vertexNormalAttribute, 3, GL_FLOAT, false, 0, 0);

        vbo_t = planeModel.createTextCoordsBuffer();
        glBindBuffer(GL_ARRAY_BUFFER, vbo_t);
        glEnableVertexAttribArray(texCoordsAttribute);
        glVertexAttribPointer(texCoordsAttribute, 2, GL_FLOAT, false, 0, 0);

        ebo = planeModel.createIndicesBuffer();
        glBindVertexArray(0);
        
        // ----------------------- ROAD MODEL -----------------------------//
        roadVao = glGenVertexArrays();
        glBindVertexArray(roadVao);

        vbo_v = roadModel.createVerticesBuffer();
        glBindBuffer(GL_ARRAY_BUFFER, vbo_v);
        glEnableVertexAttribArray(posAttrib);
        glVertexAttribPointer(posAttrib, 3, GL_FLOAT, false, 0, 0);

        vbo_n = roadModel.createNormalsBuffer();
        glBindBuffer(GL_ARRAY_BUFFER, vbo_n);
        glEnableVertexAttribArray(vertexNormalAttribute);
        glVertexAttribPointer(vertexNormalAttribute, 3, GL_FLOAT, false, 0, 0);

        vbo_t = roadModel.createTextCoordsBuffer();
        glBindBuffer(GL_ARRAY_BUFFER, vbo_t);
        glEnableVertexAttribArray(texCoordsAttribute);
        glVertexAttribPointer(texCoordsAttribute, 2, GL_FLOAT, false, 0, 0);

        ebo = roadModel.createIndicesBuffer();
        glBindVertexArray(0);
        
    }
    
      public void initLights()
    {
        int uLightPositionAttribute = shaderProgram.getUniformLocation("LightPosition");
        int uLightIntensityAttribute = shaderProgram.getUniformLocation("LightIntensity");

        uKaAttribute = shaderProgram.getUniformLocation("Ka");
        uKdAttribute = shaderProgram.getUniformLocation("Kd");
        int uKsAttribute = shaderProgram.getUniformLocation("Ks");

        int uShininessAttribute = shaderProgram.getUniformLocation("Shininess");

        glUniform4f(uLightPositionAttribute, 2.0f, 4.0f, 2.0f, 1.0f);
        glUniform3f(uLightIntensityAttribute, 0.7f, 0.7f, 0.7f);

        glUniform3f(uKaAttribute, 1.0f, 1.0f, 1.0f);
        glUniform3f(uKdAttribute, 0.8f, 0.8f, 0.8f);
        glUniform3f(uKsAttribute, 0.2f, 0.2f, 0.2f);

        glUniform1f(uShininessAttribute, 18.0f);
    }
     
    //no le podemos pasar nada xq es de tipo abstracto. por eso se declara al principio.
@Override

    public void draw(){
        System.out.println("Dibujando avión... ");
        System.out.println("Posición avión " + get_x() + " " + get_y() + " " + get_z()); 
        
    /*
        Matrix4f model = Matrix4f.rotate(angle*1.5f, 1.0f, 0.7f, 0); 
        model = Matrix4f.scale(0.9f, 0.9f, 0.9f).multiply(model);
        model = Matrix4f.translate(4, 2, 0).multiply(model);
        glUniformMatrix4(uniModel, false, model.getBuffer());

        texture1.bind();

        glDrawElements(GL_TRIANGLES, 2*3*6, GL_UNSIGNED_INT, 0);  */
        angle += angularVelocity * openGLHelper.getDeltaTime();

        glBindVertexArray(sphereVao);
        glUniform3f(uKaAttribute, 0.8f, 0.2f, 0.5f);
        glUniform3f(uKdAttribute, 0.8f, 0.2f, 0.5f);
        glUniform1i(useTextures, 0);

        Matrix4f model = Matrix4f.scale(6.0f, 1.0f, 1.0f);
        model = Matrix4f.translate(5.0f*(1.2f + (float)Math.cos(angle/10)), 4, 3).multiply(model);
        glUniformMatrix4(uniModel, false, model.getBuffer());

        Matrix3f normalMatrix = model.multiply(openGLHelper.getViewMatrix()).toMatrix3f().invert();
        normalMatrix.transpose();
        glUniformMatrix3(uNMatrixAttribute, false, normalMatrix.getBuffer());

        glDrawElements(GL_TRIANGLES, sphereModel.getIndicesLength(), GL_UNSIGNED_INT, 0);
    }
}