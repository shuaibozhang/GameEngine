package renderEngine;

import models.RawModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.opengl.*;
/**
 * Created by zhangshuaibo on 2017/4/18.
 */
public class Loader {
    private List<Integer> vaos = new ArrayList<Integer>();
    private List<Integer> vbos = new ArrayList<Integer>();
    private List<Integer> textures = new ArrayList<Integer>();

    public RawModel loadToVAO(float[] postions, int[] indices, float[] normals,float[] uvs){
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0,postions, 3);
        storeDataInAttributeList(1,uvs,2);
        storeDataInAttributeList(2,normals,3);
        unbindVAO();
        return new RawModel(vaoID, indices.length);
    }

    public int loadToVAO(float[] postions, float[] uvs){
        int vaoID = createVAO();
        storeDataInAttributeList(0,postions, 2);
        storeDataInAttributeList(1,uvs,2);
        unbindVAO();
        return vaoID;
    }

    public RawModel loadToVAO(float[] postions, int off){
        int vaoID = createVAO();
        storeDataInAttributeList(0,postions, off);
        unbindVAO();
        return new RawModel(vaoID, postions.length/off);
    }

    public RawModel loadToVAO(float[] postions){
        int vaoID = createVAO();
        storeDataInAttributeList(0,postions, 2);
        unbindVAO();
        return new RawModel(vaoID, postions.length/2);
    }

    public int loadTexture(String filename){
        Texture texture = null;

        try {
            texture = TextureLoader.getTexture("png", new FileInputStream("res/" + filename + ".png"));

            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -2.f);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int textureID = texture.getTextureID();
        textures.add(textureID);
        return  textureID;
    }

    public void cleanUp(){
        for (int vao : vaos){
            GL30.glDeleteVertexArrays(vao);
        }

        for (int vbo : vbos){
            GL15.glDeleteBuffers(vbo);
        }

        for (int textureID : textures){
            GL11.glDeleteTextures(textureID);
        }
    }

    private int createVAO(){
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);

        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    private void storeDataInAttributeList(int attributeNumber, float[] data, int size){
        int vboID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        vbos.add(vboID);

        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, size, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void bindIndicesBuffer(int[] indices){
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);

        IntBuffer buffer = storeDataInInterBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private IntBuffer storeDataInInterBuffer(int[] data){
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private void unbindVAO(){
        GL30.glBindVertexArray(0);
    }
}
