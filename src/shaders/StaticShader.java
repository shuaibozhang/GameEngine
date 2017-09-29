package shaders;

import entities.Camera;
import entities.Light;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;

/**
 * Created by zhangshuaibo on 2017/4/19.
 */
public class StaticShader extends ShaderProgram {
    private static final String VERTEX_FILE = "src/shaders/vertexShader";
    private static final String FRAGMENT_FILE = "src/shaders/fragmentShader";

    private int localtion_transformMat4;
    private int localtion_projectionMat4;
    private int localtion_viewMat4;

    private int localtion_lightPosition;
    private int localtion_lightColour;

    private int localtion_shineDamper;
    private int localtion_reflectivity;

    private int localtion_useFakeLighting;

    private int localtion_skyColour;

    public StaticShader(){
        super(VERTEX_FILE, FRAGMENT_FILE);

    }

    @Override
    protected void getAllUniformLocations() {
        localtion_transformMat4 = super.getUniformLocation("transformMat");
        localtion_projectionMat4 = super.getUniformLocation("projectionMat");
        localtion_viewMat4 = super.getUniformLocation("viewMat");

        localtion_lightPosition = super.getUniformLocation("lightPosition");
        localtion_lightColour = super.getUniformLocation("lightColour");

        localtion_shineDamper = super.getUniformLocation("shineDamper");
        localtion_reflectivity = super.getUniformLocation("reflectivity");

        localtion_useFakeLighting = super.getUniformLocation("useFakeLighting");

        localtion_skyColour = super.getUniformLocation("skyColour");
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

    public void loadLight(Light light){
        super.loadVec3(localtion_lightPosition, light.getPosition());
        super.loadVec3(localtion_lightColour, light.getColour());
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

}
