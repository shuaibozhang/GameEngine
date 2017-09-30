package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by zhangshuaibo on 2017/9/27.
 */
public class Camera {
    private Vector3f position = new Vector3f(0,0,0);
    private float pitch;
    private float yaw;
    private float roll;

    private Player player;

    private float disanceFromPlayer = 150.f;
    private float angleAroundPlayer = 0.f;

    public Camera(Player player) {
        this.player = player;
    }

    public void move(){
        calculateZoom();
        calculatePitch();
        calculateAngleAoundPlayer();
        float horizontalDistance = calculateHorizontalDistance();
        float vertialDistance = calculateVertialDistance();
        calculateCameraPosition(horizontalDistance, vertialDistance);
        this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
    }

    private void calculateCameraPosition(float horizontalDistance, float vertialDistance){
        position.y = player.getPosition().y + vertialDistance;

        float theangle = player.getRotY() + angleAroundPlayer;
        float offx = (float)( horizontalDistance * Math.sin(Math.toRadians(theangle)));
        float offz = (float)( horizontalDistance * Math.cos(Math.toRadians(theangle)));
        position.x = player.getPosition().x - offx;
        position.z = player.getPosition().z - offz;
    }

    private float calculateHorizontalDistance(){
        return disanceFromPlayer * (float) Math.cos(Math.toRadians(pitch));
    }

    private float calculateVertialDistance(){
        return disanceFromPlayer * (float) Math.sin(Math.toRadians(pitch));
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getRoll() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    private void calculateZoom(){
        float zoom = Mouse.getDWheel() * 0.1f;
        disanceFromPlayer -= zoom;
    }

    private void calculatePitch(){
        if(Mouse.isButtonDown(1)){
            float pitchChange = Mouse.getDY() * 0.1f;
            pitch -= pitchChange;
        }
    }

    private void calculateAngleAoundPlayer(){
        if(Mouse.isButtonDown(1)){
            float angleChange = Mouse.getDX() * 0.3f;
            angleAroundPlayer -= angleChange;
        }
    }
}
