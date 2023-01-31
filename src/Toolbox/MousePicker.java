package Toolbox;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import Display.Main;
import Entities.Camera;

public class MousePicker 
{
	private Vector3f currentRay;
	
	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;
	private Camera camera;
	
	public MousePicker(Camera camera, Matrix4f projectionMatrix)
	{
		this.camera = camera;
		this.projectionMatrix = projectionMatrix;
		this.viewMatrix = Maths.createViewMatrix(camera);
	}
	
	public void update()
	{
		viewMatrix = Maths.createViewMatrix(camera);
		currentRay = calculateMouseRay();
		
		if(Mouse.isButtonDown(0))
		{			
			buttonClick();
			
			if(Main.addObject.isShouldRender()) {
				Main.addObject.updateMouse();
			}
		}
	}
	
	public void buttonClick()
	{
		System.out.println(Mouse.getX() + " , " + Mouse.getY());
		if(Mouse.getX() > 208 && Mouse.getX() <= 252 && Mouse.getY() < 636 && Mouse.getY() >= 607)
		{
			Main.addObject.setShouldRender(true);
			Main.addObject.init();
		}
		
		if(Mouse.getX() > 253 && Mouse.getX() <= 298 && Mouse.getY() < 636 && Mouse.getY() >= 607)
		{
			//Fjern objekter
		}
	}
	
	private Vector3f calculateMouseRay()
	{
		float mouseX = Mouse.getX();
		float mouseY = Mouse.getY();
		Vector2f normalizedCoord = getNormalizedDeviceCoords(mouseX, mouseY);
		Vector4f clipCoords = new Vector4f(normalizedCoord.x, normalizedCoord.y, -1f, 1f);
		Vector4f eyeCoords = toEyeSpace(clipCoords);
		Vector3f worldCoords = toWorldCoord(eyeCoords);
		return worldCoords;
	}
	
	private Vector3f toWorldCoord(Vector4f eyeCoord)
	{
		Matrix4f invertedView = Matrix4f.invert(viewMatrix, null);
		Vector4f rayWorld = Matrix4f.transform(invertedView, eyeCoord, null);
		Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
		mouseRay.normalise();
		return mouseRay;
	}
	
	private Vector4f toEyeSpace(Vector4f clipCoords)
	{
		Matrix4f invertedProjection = Matrix4f.invert(projectionMatrix, null);
		Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipCoords, null);
		return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0);
	}
	
	private Vector2f getNormalizedDeviceCoords(float mouseX, float mouseY)
	{
		float x = (2f * mouseX) / Display.getWidth() - 1;
		float y = (2f * mouseY) / Display.getHeight() - 1f;
		return new Vector2f(x,y);
	}
	
	public Vector3f getCurrentRay()
	{
		return currentRay;
	}
}
