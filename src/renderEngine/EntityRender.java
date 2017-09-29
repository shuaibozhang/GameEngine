package renderEngine;

import entities.Entity;
import models.RawModel;
import models.TextureModel;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.Maths;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangshuaibo on 2017/4/18.
 */
public class EntityRender {

    private StaticShader shader = null;

    public EntityRender(StaticShader shader, Matrix4f projectionMatrix){
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(Map<TextureModel, List<Entity>> entities){
        for (TextureModel model:entities.keySet()){
            prepareTexureModel(model);
            List<Entity> batch = entities.get(model);
            for (Entity entity:batch){
                prepareInstance(entity);

                RawModel rawModel = model.getRawModel();
                GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }

            unbindTextureModel();
        }
    }

    private void prepareTexureModel(TextureModel model){
        RawModel rawModel = model.getRawModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        if(model.getTexture().isHasTransparency()){
            MasterRender.disableCulling();
        }

        this.shader.loadUseFakeLight(model.getTexture().isUsingFakeLighting());

        this.shader.loadShineVariables(model.getTexture().getShineDamper(), model.getTexture().getReflectivity());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getTextureID());
    }

    private void unbindTextureModel(){

        MasterRender.enableCulling();

        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    private void prepareInstance(Entity entity){
        Matrix4f transform = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(),entity.getRotY(),entity.getRotZ(),entity.getScale());
        shader.loadTransformMatrix(transform);
    }
}
