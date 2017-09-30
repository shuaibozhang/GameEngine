package textures;

/**
 * Created by zhangshuaibo on 2017/4/26.
 */
public class ModelTexture {
    private int textureID;
    private float shineDamper = 1;
    private float reflectivity = 0;
    private boolean hasTransparency = false;
    private boolean usingFakeLighting = false;

    private int numOfRows = 1;

    public int getNumOfRows() {
        return numOfRows;
    }

    public void setNumOfRows(int numOfRows) {
        this.numOfRows = numOfRows;
    }

    public ModelTexture(int id){
        this.textureID = id;
    }

    public int getTextureID(){
        return this.textureID;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

    public boolean isHasTransparency() {
        return hasTransparency;
    }

    public void setHasTransparency(boolean hasTransparency) {
        this.hasTransparency = hasTransparency;
    }

    public boolean isUsingFakeLighting() {
        return usingFakeLighting;
    }

    public void setUsingFakeLighting(boolean usingFakeLighting) {
        this.usingFakeLighting = usingFakeLighting;
    }
}
