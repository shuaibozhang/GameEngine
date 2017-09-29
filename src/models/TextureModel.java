package models;

import textures.ModelTexture;

/**
 * Created by zhangshuaibo on 2017/4/26.
 */
public class TextureModel {
    private RawModel rawModel;
    private ModelTexture texture;

    public TextureModel(RawModel model, ModelTexture texture){
        this.rawModel = model;
        this.texture = texture;
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public ModelTexture getTexture() {
        return texture;
    }

    public void setRawModel(RawModel rawModel) {
        this.rawModel = rawModel;
    }

    public void setTexture(ModelTexture texture) {
        this.texture = texture;
    }
}
