package renderEngine;

import models.RawModel;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector2f;
import org.omg.PortableInterceptor.INACTIVE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangshuaibo on 2017/9/27.
 */
public class OBJLoader {
    public static RawModel loadObgModel(String filename, Loader loader){
        FileReader freader = null;

        try {
            freader = new FileReader(new File("res/" + filename + ".obj"));
        } catch (FileNotFoundException e) {
            System.err.print("can not find file:" + filename + "\n");
            e.printStackTrace();
        }

        BufferedReader bufferReader = new BufferedReader(freader);
        List<Vector3f> vertices = new ArrayList<Vector3f>();
        List<Vector2f> textures = new ArrayList<Vector2f>();
        List<Vector3f> normals = new ArrayList<Vector3f>();
        List<Integer> index = new ArrayList<Integer>();

        float[] verticesArray = null;
        float[] texturesArray = null;
        float[] normalsArray = null;
        int [] indexArray = null;
        String line = null;
        try{
            while (true){
                line = bufferReader.readLine();
                String [] currentLine = line.split(" ");

                if(line.startsWith("v ")){
                    Vector3f vector3f = new Vector3f(Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                    vertices.add(vector3f);
                }else if(line.startsWith("vt ")){
                    Vector2f vector2f = new Vector2f(Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]));
                    textures.add(vector2f);
                }else if(line.startsWith("vn ")){
                    Vector3f vector3f = new Vector3f(Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                    normals.add(vector3f);
                }else if(line.startsWith("f ")){
                    texturesArray = new float[vertices.size()*2];
                    normalsArray = new float[vertices.size()*3];
                    break;
                }
            }

            while (line != null){
                if(!line.startsWith("f ")){
                    line = bufferReader.readLine();
                    continue;
                }

                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");

                processVertex(vertex1, index, textures, normals, texturesArray, normalsArray);
                processVertex(vertex2, index, textures, normals, texturesArray, normalsArray);
                processVertex(vertex3, index, textures, normals, texturesArray, normalsArray);

                line = bufferReader.readLine();
            }

            bufferReader.close();

        }catch(Exception e){

        }

        verticesArray = new float[vertices.size()*3];
        indexArray = new int[index.size()*3];

        int idx = 0;
        for(Vector3f v:vertices){
            verticesArray[idx++] = v.x;
            verticesArray[idx++] = v.y;
            verticesArray[idx++] = v.z;
        }

        for(int i = 0; i < index.size(); i++){
            indexArray[i] = index.get(i);
        }

        return loader.loadToVAO(verticesArray, indexArray, normalsArray,texturesArray);

    }

    private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures, List<Vector3f> normals,
                                      float[] textureArray, float[] normalArray){
        int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
        indices.add(currentVertexPointer);

        Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
        textureArray[currentVertexPointer * 2] = currentTex.x;
        textureArray[currentVertexPointer * 2 + 1] = 1 - currentTex.y;

        Vector3f currentNormal = normals.get(Integer.parseInt(vertexData[2]) - 1);
        normalArray[currentVertexPointer * 3] = currentNormal.x;
        normalArray[currentVertexPointer * 3 + 1] = currentNormal.y;
        normalArray[currentVertexPointer * 3 + 2] = currentNormal.z;
    }
}
