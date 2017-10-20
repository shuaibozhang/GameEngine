package guis;

import org.lwjgl.util.vector.Vector2f;

/**
 * Created by zhangshuaibo on 2017/9/30.
 */
public class GuiTexture {
    private int texture;
    private Vector2f position;
    private Vector2f scale;
    private float rotate;

    public GuiTexture(int texture, Vector2f position, Vector2f scale, float rotate) {
        this.texture = texture;
        this.position = position;
        this.scale = scale;
        this.rotate = rotate;
    }

    public float getRotate() {
        return rotate;
    }

    public void setRotate(float rotate) {
        this.rotate = rotate;
    }

    public int getTexture() {
        return texture;
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getScale() {
        return scale;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public void setScale(Vector2f scale) {
        this.scale = scale;
    }
}
