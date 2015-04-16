package simulador;

import Utils.CubeModel;
import Utils.Drawable;
import Utils.Matrix3f;
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
import Utils.Matrix4f;
import Utils.OpenGLHelper;
import Utils.PlaneModel;
import Utils.ShaderProgram;
import Utils.SphereModel;
import Utils.Texture;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.GL_TEXTURE2;
import static org.lwjgl.opengl.GL13.GL_TEXTURE3;
import static org.lwjgl.opengl.GL13.GL_TEXTURE4;
import static org.lwjgl.opengl.GL13.GL_TEXTURE5;
import static org.lwjgl.opengl.GL13.glActiveTexture;


public class Pista extends Dibujable{
    private float orientacion;
    private float angle;
    private OpenGLHelper openGLHelper;
    private ShaderProgram shaderProgram;
    private int uniModel;
    private Texture texture0, texture1, texture2;
    private static final float angularVelocity = 30.f;
    
    private int uNMatrixAttribute;
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
            
    public Pista (float pos_x, float pos_y, float pos_z, float tipo_in, int shader)
    {
        set_x(pos_x);
        set_y(pos_y);
        set_z(pos_z);
        set_tipo(tipo_in);
    } 

    public void prepareBuffers(OpenGLHelper openGLHelper_param) {
        openGLHelper = openGLHelper_param;
        shaderProgram = openGLHelper.getShaderProgram();

        // --------------------- AXIES MODEL  -------------------------------//
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
     
   @Override
   public void draw() 
   {
        System.out.println("Dibujando pista " + get_length());
        System.out.println("Posición pista " + get_x() + " " + get_y() + " " + get_z());
        
        /*Matrix4f model = Matrix4f.rotate(angle*1.5f, 1.0f, 1.7f, 0); 
        model = Matrix4f.scale(1.9f, 1.9f, 1.9f).multiply(model);
        model = Matrix4f.translate(8, 5, 0).multiply(model);
        glUniformMatrix4(uniModel, false, model.getBuffer());

        texture2.bind();
       
        glDrawElements(GL_TRIANGLES, 2*3*6, GL_UNSIGNED_INT, 0); */

        shaderProgram.setUniform(uniTex1, nicegrasstexture.getId() -1);
        glBindVertexArray(roadVao);
        glUniform3f(uKaAttribute, 1.0f, 1.0f, 1.0f);
        glUniform3f(uKdAttribute, 1.0f, 1.0f, 1.0f);
        glUniform1i(useTextures, 1);

        Matrix4f model = Matrix4f.scale(10.0f, 1.0f, 2.0f);
        model = Matrix4f.translate(10, 0, 0).multiply(model);
        glUniformMatrix4(uniModel, false, model.getBuffer());

        Matrix3f normalMatrix = model.multiply(openGLHelper.getViewMatrix()).toMatrix3f().invert();
        normalMatrix.transpose();
        glUniformMatrix3(uNMatrixAttribute, false, normalMatrix.getBuffer());
        // Enable polygon offset to resolve depth-fighting isuses
        glEnable(GL_POLYGON_OFFSET_FILL);
        glPolygonOffset(-2.0f, 4.0f);
        glDrawElements(GL_TRIANGLES, roadModel.getIndicesLength(), GL_UNSIGNED_INT, 0);
        glDisable(GL_POLYGON_OFFSET_FILL);

   } 
}
