package com.zhang;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import guis.GuiRender;
import guis.GuiTexture;
import models.TextureModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import renderEngine.*;
import models.RawModel;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

import java.io.File;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {
    public static long s_startTime = System.currentTimeMillis();
    public static void main(String[] args) {
        addLwjglNativesToJavaLibraryPathProperty();
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


        for(int i = 0; i < 100; i++){
            float x = random.nextFloat()*300 - 150;
            float z = random.nextFloat()*300 - 150;
            float y = terrain.getHeighOfTerrain(x, z);
            int ranidx = random.nextInt(4);
            TextureModel randomTextureModel = textureModelArray.get(ranidx);
            int scale = ranidx == 3 ? 15:1;
            Entity entity = new Entity(randomTextureModel, random.nextInt(4),new Vector3f(x, y, z), 0,
                    0,
                    0,scale);
            all.add(entity);
        }

        Player player = new Player(textureModel4,new Vector3f(0.f, 0.f, 0.f), 0,
                0,
                0,15);

        Camera camera = new Camera(player);
        camera.setPitch(60);

        List<Light> lights = new ArrayList<Light>();
        Light light = new Light(new Vector3f(0.0f,50.f,0), new Vector3f(1,1,1), new Vector3f(1,0.0f, 0.0f));
        Light light2 = new Light(new Vector3f(200.0f,500.f,0), new Vector3f(1,0,0));
        Light light3 = new Light(new Vector3f(500.0f,0.f,0), new Vector3f(0,0,0.3f));
        Light light4 = new Light(new Vector3f(0.0f,0.f,0), new Vector3f(0,0,0));
        lights.add(light);
        //lights.add(light2);
        //lights.add(light3);
        //lights.add(light4);

        List<GuiTexture> guis = new ArrayList<GuiTexture>();
        GuiRender guiRender = new GuiRender(loader);
        //GuiTexture guiTexture = new GuiTexture(loader.loadTexture("socuwan"), new Vector2f(0.0f, 0.0f), new Vector2f(0.3f,0.3f));
        //guis.add(guiTexture);

        MasterRender render = new MasterRender();

        TextMaster.init(loader);

        FontType font = new FontType(loader.loadTexture("dft"), new File("res/dft.fnt"));
        GUIText text = new GUIText("abcdefg hijklmn opq rst uvwxyz", 1.f, font, new Vector2f(0f, 0f), 1f, true);
        text.setColour(1, 0, 0);

        WaterFrameBuffers waterFrameBuffers = new WaterFrameBuffers();
        guis.add(new GuiTexture(waterFrameBuffers.getReflectionTexture(), new Vector2f(-0.5f, -0.5f), new Vector2f(0.2f,0.2f), 0));
        guis.add(new GuiTexture(waterFrameBuffers.getRefractionTexture(), new Vector2f(0.5f, -0.5f), new Vector2f(0.2f,0.2f), 0));

        WaterShader waterShader = new WaterShader();
        WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, render.getProjectionMatrix());
        List<WaterTile> waters = new ArrayList<WaterTile>();
        WaterTile waterTile = new WaterTile(waterFrameBuffers.getReflectionTexture(),75, -75, 0);
        waters.add(waterTile);

        while (!Display.isCloseRequested()){
            player.move(terrain);
            camera.move();

            GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

            Vector3f pos = camera.getPosition();
            float distance = 2 * (pos.y - waterTile.getHeight());
            camera.getPosition().y -= distance;
            camera.invertPitch();

            waterShader.start();
            waterShader.loadViewMatrixWater(camera);
            waterShader.stop();
            //Reflection
            for (Entity entity:all){
                render.processEntity(entity);
            }
            render.processTerrain(terrain);
            render.processEntity(player);

            waterFrameBuffers.bindReflectionFrameBuffer();
            render.render(lights, camera, new Vector4f(0, 1, 0, -waterTile.getHeight()));
            waterFrameBuffers.unbindCurrentFrameBuffer();

            //Refraction
            for (Entity entity:all){
                render.processEntity(entity);
            }
            render.processTerrain(terrain);
            render.processEntity(player);

            waterFrameBuffers.bindRefractionFrameBuffer();
            render.render(lights, camera, new Vector4f(0, -1, 0, -waterTile.getHeight()));
            waterFrameBuffers.unbindCurrentFrameBuffer();

            camera.getPosition().y += distance;
            camera.invertPitch();

            GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
            for (Entity entity:all){
                render.processEntity(entity);
            }
            render.processTerrain(terrain);
            render.processEntity(player);
            render.render(lights, camera, new Vector4f(0, -1, 0, 900000));
            waterRenderer.render(waters,camera);
            guiRender.render(guis);
            TextMaster.render();
            DisplayManager.updateDisplay();
        }
        waterFrameBuffers.cleanUp();
        guiRender.cleanUp();
        render.cleanUp();
        TextMaster.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }

    private static void addLwjglNativesToJavaLibraryPathProperty() {
        File JGLLib = new File("libs/native/macosx/");
        System.setProperty("org.lwjgl.librarypath", JGLLib.getAbsolutePath());
    }
}
