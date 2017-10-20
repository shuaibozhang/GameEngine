package water;

public class WaterTile {
	
	public static final float TILE_SIZE = 600;

	private float height;
	private float x,z;
	private int textureId;
	public WaterTile(int textureId, float centerX, float centerZ, float height){
		this.x = centerX;
		this.z = centerZ;
		this.height = height;
		this.textureId = textureId;
	}

	public int getTextureId() {
		return textureId;
	}

	public void setTextureId(int textureId) {
		this.textureId = textureId;
	}

	public float getHeight() {
		return height;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}



}
