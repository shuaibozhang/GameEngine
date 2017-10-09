package shaders;

import entities.Camera;
import entities.Light;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;

import java.util.List;

/**
 * Created by zhangshuaibo on 2017/9/28.
 */
public class TerrainShader extends ShaderProgram {
    private static final int MAX_LIGHTS = 4;
    private static final String VERTEX_FILE = "src/shaders/tarrainVertexShader";
    private static final String FRAGMENT_FILE = "src/shaders/tarrainFragmentShader";

    private int localtion_transformMat4;
    private int localtion_projectionMat4;
    private int localtion_viewMat4;

    private int localtion_lightPosition[];
    private int localtion_lightColour[];
    private int localtion_attenuation[];

    private int localtion_shineDamper;
    private int localtion_reflectivity;
    private int localtion_skyColour;

    private int localtion_backgroundTexture;
    private int localtion_rTexture;
    private int localtion_gTexture;
    private int localtion_bTexture;
    private int localtion_blendMap;

    public TerrainShader(){
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

        localtion_skyColour = super.getUniformLocation("skyColour");

        localtion_backgroundTexture = super.getUniformLocation("backgroundTexture");
        localtion_rTexture = super.getUniformLocation("rTexture");
        localtion_gTexture = super.getUniformLocation("gTexture");
        localtion_bTexture = super.getUniformLocation("bTexture");
        localtion_blendMap = super.getUniformLocation("blendMap");
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

    public void connectTextureUnit(){
        super.loadInt(localtion_backgroundTexture, 0);
        super.loadInt(localtion_rTexture, 1);
        super.loadInt(localtion_gTexture, 2);
        super.loadInt(localtion_bTexture, 3);
        super.loadInt(localtion_blendMap, 4);
    }

    public void loadSkyColour(Vector3f skyColour){
        super.loadVec3(localtion_skyColour, skyColour);
    }
}
