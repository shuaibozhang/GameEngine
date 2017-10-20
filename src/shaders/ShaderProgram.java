package shaders;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

/**
 * Created by zhangshuaibo on 2017/4/19.
 */
public abstract class ShaderProgram {
    private  int programID;
    private  int vertexShaderID;
    private  int fragmentShaderID;

    private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    public ShaderProgram(String vertexShaderFile, String fragmengShaderFile){
        vertexShaderID = loadShader(vertexShaderFile, GL20.GL_VERTEX_SHADER);
        fragmentShaderID = loadShader(fragmengShaderFile, GL20.GL_FRAGMENT_SHADER);
        programID = GL20.glCreateProgram();
        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);
        bindAttributes();
        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);
        getAllUniformLocations();
    }

    protected abstract void getAllUniformLocations();

    protected int getUniformLocation(String uniformName){
        return GL20.glGetUniformLocation(programID, uniformName);
    }

    public void start(){
        GL20.glUseProgram(programID);
    }

    public void stop(){
        GL20.glUseProgram(0);
    }

    public void cleanUp(){
        stop();
        GL20.glDetachShader(programID, vertexShaderID);
        GL20.glDetachShader(programID, fragmentShaderID);
        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);
        GL20.glDeleteProgram(programID);


    }

    protected void bindAttribute(int attribute, String variableName){
        GL20.glBindAttribLocation(programID, attribute, variableName);
    }

    protected void loadFloat(int location, float value){
        GL20.glUniform1f(location, value);
    }

    protected void loadInt(int location, int value){
        GL20.glUniform1i(location, value);
    }

    protected void loadVec3(int location, Vector3f value){
        GL20.glUniform3f(location, value.x, value.y, value.z);
    }

    protected void loadVec2(int location, Vector2f value){
        GL20.glUniform2f(location, value.x, value.y);
    }

    protected void loadBoolean(int location, boolean value){
        float toload = 0.f;
        if(value){
            toload = 1.f;
        }

        GL20.glUniform1f(location, toload);
    }

    protected  void loadMatrix4f(int location, Matrix4f matrix){
        matrix.store(matrixBuffer);
        matrixBuffer.flip();
        GL20.glUniformMatrix4(location, false, matrixBuffer);
    }

    protected abstract void bindAttributes();

    private static int loadShader(String fileName, int type){
        StringBuilder shaderSource = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null){
                shaderSource.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int shaderId = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderId, shaderSource);
        GL20.glCompileShader(shaderId);
        if(GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE){
            System.out.println(GL20.glGetShaderInfoLog(shaderId, 500));
            System.out.println("complieshader error");
            System.exit(-1);
        }

        return shaderId;
    }

    public void loadMatrix(int location_transformationMatrix, Matrix4f matrix) {
        loadMatrix4f(location_transformationMatrix, matrix);
    }

    protected void loadVec4(int location, Vector4f value){
        GL20.glUniform4f(location, value.x, value.y, value.z, value.w);
    }
}
