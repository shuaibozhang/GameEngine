package shaders;

import entities.Camera;
import entities.Light;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;
import toolbox.Maths;

import java.util.List;

/**
 * Created by zhangshuaibo on 2017/4/19.
 */
public class StaticShader extends ShaderProgram {
    private static final int MAX_LIGHTS = 4;
    private static final String VERTEX_FILE = "src/shaders/vertexShader";
    private static final String FRAGMENT_FILE = "src/shaders/fragmentShader";

    private int localtion_transformMat4;
    private int localtion_projectionMat4;
    private int localtion_viewMat4;

    private int localtion_lightPosition[];
    private int localtion_lightColour[];
    private int localtion_attenuation[];

    private int localtion_shineDamper;
    private int localtion_reflectivity;

    private int localtion_useFakeLighting;

    private int localtion_skyColour;

    private int localtion_numOfRows;
    private int localtion_offset;

    private int localtion_plane;

    public StaticShader(){
        super(VERTEX_FILE, FRAGMENT_FILE);

    }

    @Override
    protected void getAllUniformLocations() {
        localtion_transformMat4 = super.getUniformLocation("transformMat");
        localtion_projectionMat4 = super.getUniformLocation("projectionMat");
        localtion_viewMat4 = super.getUniformLocation("viewMat");

        localtion_lightPosition = new int[MAX_LIGHTS];
        localtion_lightColour = new int[MAX_LIGHTS];
        localtion_attenuation = new int[MAX_LIGHTS];
        for(int i = 0; i < MAX_LIGHTS; i++) {
            localtion_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
            localtion_lightColour[i] = super.getUniformLocation("lightColour[" + i + "]");
            localtion_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
        }

        localtion_shineDamper = super.getUniformLocation("shineDamper");
        localtion_reflectivity = super.getUniformLocation("reflectivity");

        localtion_useFakeLighting = super.getUniformLocation("useFakeLighting");

        localtion_skyColour = super.getUniformLocation("skyColour");

        localtion_numOfRows = super.getUniformLocation("numOfRows");
        localtion_offset = super.getUniformLocation("offset");

        localtion_plane = super.getUniformLocation("plane");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1,"textureCoords");
        super.bindAttribute(2,"normals");
    }

    public void loadTransformMatrix(Matrix4f mat4){
        super.loadMatrix4f(localtion_transformMat4, mat4);
    }

    public void loadProjectionMatrix(Matrix4f mat4){
        super.loadMatrix4f(localtion_projectionMat4, mat4);
    }

    public void loadViewMatrix(Camera camera){
        Matrix4f mat4 = Maths.createViewMatrix(camera);
        super.loadMatrix4f(localtion_viewMat4, mat4);
    }

    public void loadLight(List<Light> light){
        for (int i = 0; i < MAX_LIGHTS; i++){
            if(i < light.size()){
                Light curLight = light.get(i);
                super.loadVec3(localtion_lightPosition[i], curLight.getPosition());
                super.loadVec3(localtion_lightColour[i], curLight.getColour());
                super.loadVec3(localtion_attenuation[i], curLight.getAttenuation());
            }
            else{
                super.loadVec3(localtion_lightPosition[i],new Vector3f(0,0,0));
                super.loadVec3(localtion_lightColour[i],new Vector3f(0,0,0));
                super.loadVec3(localtion_attenuation[i], new Vector3f(1,0,0));
            }
        }
    }

    public void loadShineVariables(float damper, float reflctivity){
        super.loadFloat(localtion_shineDamper, damper);
        super.loadFloat(localtion_reflectivity, reflctivity);
    }

    public void loadUseFakeLight(boolean useFakeLight){
        super.loadBoolean(localtion_useFakeLighting, useFakeLight);
    }

    public void loadSkyColour(Vector3f skyColour){
        super.loadVec3(localtion_skyColour, skyColour);
    }

    public void loadNumOfRows(int rows){
        super.loadFloat(localtion_numOfRows, rows);
    }

    public void loadOffSet(float offx, float offy){
        super.loadVec2(localtion_offset, new Vector2f(offx, offy));
    }

    public void loadPlane(Vector4f plane){
        super.loadVec4(localtion_plane ,plane);
    }
}
