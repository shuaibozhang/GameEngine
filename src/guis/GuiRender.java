package guis;

import models.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;
import toolbox.Maths;

import java.util.List;

/**
 * Created by zhangshuaibo on 2017/9/30.
 */
public class GuiRender {
    private RawModel quad;
    private GuiShader shader;
    public GuiRender(Loader loader) {
        float[] positions = {-1,1, -1,-1, 1,1, 1,-1};
        quad = loader.loadToVAO(positions);
        shader = new GuiShader();
    }

    public void render(List<GuiTexture> guis){
        shader.start();
        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glDisable(GL11.GL_DEPTH_TEST);
        //render
        for (GuiTexture guiTexture : guis){
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, guiTexture.getTexture());
            Matrix4f mat = Maths.createTransformationMatrix(new Vector3f(guiTexture.getPosition().x, guiTexture.getPosition().y, 0.0f),
                    0.0f,0.0f,0.0f,
                    new Vector3f(guiTexture.getScale().getX(), guiTexture.getScale().getY(),0.0f));
            shader.loadTransformation(mat);
            GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
        }

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }

    public void cleanUp(){
        shader.cleanUp();
    }
}
