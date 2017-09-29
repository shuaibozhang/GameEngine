package renderEngine;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TextureModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import shaders.StaticShader;
import shaders.TerrainShader;
import terrains.Terrain;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhangshuaibo on 2017/9/28.
 */
public class MasterRender {
    private StaticShader shader = new StaticShader();
    private EntityRender entityRender;
    private TerrainsRender terrainsRender;
    private TerrainShader terrainShader = new TerrainShader();

    private Matrix4f projectionMatrix;

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;

    private Vector3f skyColour = new Vector3f(0.8f,0.8f,0.8f);

    private Map<TextureModel, List<Entity>> entities = new HashMap<TextureModel, List<Entity>>();
    private List<Terrain> terrains = new ArrayList<Terrain>();

    public void cleanUp(){
        terrainShader.cleanUp();
        shader.cleanUp();
    }

    public MasterRender() {

        createProjectionMatrix();
        entityRender = new EntityRender(shader, projectionMatrix);
        terrainsRender = new TerrainsRender(terrainShader, projectionMatrix);
    }

    public void render(Light light, Camera camera){
        prepare();

        shader.start();
        shader.loadLight(light);
        shader.loadViewMatrix(camera);
        shader.loadSkyColour(skyColour);
        entityRender.render(entities);
        shader.stop();

        terrainShader.start();
        terrainShader.loadLight(light);
        terrainShader.loadViewMatrix(camera);
        terrainShader.loadSkyColour(skyColour);
        terrainsRender.render(terrains);
        terrainShader.stop();

        entities.clear();
        terrains.clear();
    }

    public static void enableCulling(){
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);

    }

    public static void disableCulling(){
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    public void processEntity(Entity entity){
        TextureModel model = entity.getModel();
        List<Entity> batch = entities.get(model);
        if(batch != null){
            batch.add(entity);
        }else{
            List<Entity> newbatch = new ArrayList<Entity>();
            newbatch.add(entity);
            entities.put(model, newbatch);
        }
    }

    public void processTerrain(Terrain entity){
        terrains.add(entity);
    }

    private void createProjectionMatrix(){
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) (1.f/Math.tan(Math.toRadians(FOV/2.f)) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -(FAR_PLANE + NEAR_PLANE)/frustum_length;
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * FAR_PLANE * NEAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
    }

    public void prepare(){
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(skyColour.x,skyColour.y,skyColour.z,1);
    }

}
