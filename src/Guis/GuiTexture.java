package Guis;

import org.lwjgl.util.vector.Vector2f;

public class GuiTexture 
{
	private int textureID;
	private Vector2f position;
	private Vector2f scale;
	private boolean shouldRender = true;
	
	public GuiTexture(int textureID, Vector2f position, Vector2f scale) {
		this.textureID = textureID;
		this.position = position;
		this.scale = scale;
	}
	
	public void render(){}
	public void cleanUp(){}
	
	public int getTextureID() {
		return textureID;
	}
	public Vector2f getPosition() {
		return position;
	}
	public Vector2f getScale() {
		return scale;
	}
	public boolean isShouldRender() {
		return shouldRender;
	}
	public void setShouldRender(boolean should) {
		this.shouldRender = should;
	}
}