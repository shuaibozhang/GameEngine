package guis;

import models.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import renderEngine.Loader;

import java.util.List;

/**
 * Created by zhangshuaibo on 2017/9/30.
 */
public class GuiRender {
    private RawModel quad;
    private GuiShader shader;
    public GuiRender(Loader loader) {
        float[] positions = {-1,-1, -1,1, 1,1, 1,-1};
        quad = loader.loadToVAO(positions);
        shader = new GuiShader();
    }

    public void render(List<GuiTexture> guis){
        shader.start();
        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);

        //render
        for (GuiTexture guiTexture : guis){
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, guiTexture.getTexture());
            GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
        }

        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }

    public void cleanUp(){
        shader.cleanUp();
    }
}
