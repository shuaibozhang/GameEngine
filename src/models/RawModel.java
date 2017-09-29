package models;

/**
 * Created by zhangshuaibo on 2017/4/18.
 */
public class RawModel {
    private int vaoID;
    private int vertexCount;

    public RawModel(int vaoID, int vertexCount){
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }

    public int getVaoID(){
        return vaoID;
    }

    public int getVertexCount(){
        return vertexCount;
    }
}
