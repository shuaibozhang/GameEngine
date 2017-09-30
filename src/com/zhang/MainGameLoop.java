package com.zhang;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import guis.GuiRender;
import guis.GuiTexture;
import models.TextureModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;
import models.RawModel;
import shaders.StaticShader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.Maths;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {
    public static long s_startTime = System.currentTimeMillis();
    public static void main(String[] args) {
	// write your code here
        DisplayManager.createDisplay();
        Random random = new Random();
        Loader loader = new Loader();

        List<Entity> all = new ArrayList<Entity>();
        List<TextureModel>  textureModelArray = new ArrayList<TextureModel>();

        //grass
        RawModel rawModel = OBJLoader.loadObgModel("grassModel", loader);
        ModelTexture texture = new ModelTexture(loader.loadTexture("grassTexture"));
        TextureModel textureModel = new TextureModel(rawModel, texture);
        textureModel.getTexture().setShineDamper(20);
        textureModel.getTexture().setReflectivity(0);
        textureModel.getTexture().setHasTransparency(true);
        textureModel.getTexture().setUsingFakeLighting(true);
        textureModelArray.add(textureModel);

        //grass
        RawModel rawModel2 = OBJLoader.loadObgModel("fern", loader);
        ModelTexture texture2 = new ModelTexture(loader.loadTexture("fern"));
        texture2.setNumOfRows(2);
        TextureModel textureModel2 = new TextureModel(rawModel2, texture2);
        textureModel2.getTexture().setShineDamper(20);
        textureModel2.getTexture().setReflectivity(0);
        textureModel2.getTexture().setHasTransparency(true);
        textureModel2.getTexture().setUsingFakeLighting(true);
        textureModelArray.add(textureModel2);

        //grass
        RawModel rawModel3 = OBJLoader.loadObgModel("grassModel", loader);
        ModelTexture texture3 = new ModelTexture(loader.loadTexture("flower"));
        TextureModel textureModel3 = new TextureModel(rawModel3, texture3);
        textureModel3.getTexture().setShineDamper(20);
        textureModel3.getTexture().setReflectivity(0);
        textureModel3.getTexture().setHasTransparency(true);
        textureModel3.getTexture().setUsingFakeLighting(true);
        textureModelArray.add(textureModel3);

        RawModel rawModel4 = OBJLoader.loadObgModel("tree", loader);
        ModelTexture texture4 = new ModelTexture(loader.loadTexture("tree"));
        TextureModel textureModel4 = new TextureModel(rawModel4, texture4);
        textureModel4.getTexture().setShineDamper(20);
        textureModel4.getTexture().setReflectivity(0);
        textureModelArray.add(textureModel4);

        TerrainTexture blendTexture = new TerrainTexture(loader.loadTexture("blendMap"));

        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));

        Terrain terrain = new Terrain(-400,-400,loader,
                new TerrainTexturePack(backgroundTexture,rTexture,gTexture,bTexture), blendTexture, "heightmap");


        for(int i = 0; i < 300; i++){
            float x = random.nextFloat()*300 - 150;
            float z = random.nextFloat()*300 - 150;
            float y = terrain.getHeighOfTerrain(x, z);
            int ranidx = random.nextInt(4);
            TextureModel randomTextureModel = textureModelArray.get(ranidx);
            int scale = ranidx == 3 ? 5:1;
            Entity entity = new Entity(randomTextureModel, random.nextInt(4),new Vector3f(x, y, z), 0,
                    0,
                    0,scale);
            all.add(entity);
        }

        Player player = new Player(textureModel4,new Vector3f(0.f, 0.f, 0.f), 0,
                0,
                0,1);

        Camera camera = new Camera(player);
        Light light = new Light(new Vector3f(0.0f,100.f,0), new Vector3f(1,1,1));

        List<GuiTexture> guis = new ArrayList<GuiTexture>();
        GuiRender guiRender = new GuiRender(loader);
        GuiTexture guiTexture = new GuiTexture(loader.loadTexture("grassy"), new Vector2f(0.5f, 0.5f), new Vector2f(1,1));
        guis.add(guiTexture);

        MasterRender render = new MasterRender();

        while (!Display.isCloseRequested()){
            camera.move();
            player.move(terrain);

            for (Entity entity:all){
                render.processEntity(entity);
            }
            render.processTerrain(terrain);
            render.processEntity(player);
            render.render(light, camera);

            guiRender.render(guis);

            DisplayManager.updateDisplay();
        }
        guiRender.cleanUp();
        render.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
