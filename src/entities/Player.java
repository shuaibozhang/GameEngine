package entities;

import models.TextureModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;

/**
 * Created by zhangshuaibo on 2017/9/29.
 */
public class Player extends Entity {

    private static final float RUN_SPEED = 20;
    private static final float TURN_SPEED = 160;
    private static final float GRAVITY = -50;
    private static final float JUMP_POWER = 30;

    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;

    private float upwardsSpeed = 0;

    private float terrainHeight = 0;

    private boolean isInAir = false;

    public Player(TextureModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
    }

    public void move(){
        checkInput();
        this.increaseRotation(0,currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
        float delta = DisplayManager.getFrameTimeSeconds();
        float distance = currentSpeed * delta;
        float dx = (float)(distance * Math.sin(Math.toRadians(this.getRotY())));
        float dz = (float)(distance * Math.cos(Math.toRadians(this.getRotY())));
        super.increasePosition(dx,0,dz);

        upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
        super.increasePosition(0,upwardsSpeed * DisplayManager.getFrameTimeSeconds(),0);

        if(super.getPosition().y < terrainHeight){
            upwardsSpeed = 0.f;
            super.getPosition().y = terrainHeight;
            isInAir = false;
        }
    }

    public  void jump(){
        if(!isInAir){
            upwardsSpeed = JUMP_POWER;
            isInAir = true;
        }
    }

    private void checkInput(){
        if(Keyboard.isKeyDown(Keyboard.KEY_W)){
            currentSpeed = RUN_SPEED;
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_S)){
            currentSpeed = -RUN_SPEED;
        }else{
            currentSpeed = 0;
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_A)){
            currentTurnSpeed = TURN_SPEED;
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_D)){
            currentTurnSpeed = -TURN_SPEED;
        }else{
            currentTurnSpeed = 0;
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
            jump();
        }

    }
}
