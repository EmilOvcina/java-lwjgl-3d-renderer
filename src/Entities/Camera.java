package Entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera 
{
	private float distanceFromCenter = 30;
	private float angleAroundCenter = 0;
	
	private Vector3f position = new Vector3f(5,10,30);
	private float pitch;
	private float yaw;
	private float roll;
	
	private float calX = 0;
	private float calY = 0;
	private float calZ = 0;
	
	private float ms = 0.04f;
	
	private Entity center;
	
	//TODO selectable entities
	public Camera(Entity center)
	{
		this.center = center; 
	}
	
	public void move()
	{		
		calculateZoom();
		calculatePitch();
		calculateAngleAroundCenter();
		
		float horDistance = calculateHorizontal();
		float verDistance = calculateVertical();
		calculateCameraPosition(horDistance, verDistance);
		this.yaw = 180 - (angleAroundCenter);
	}
	
	public void calculateCenterMovement()
	{
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			if(Mouse.isButtonDown(0)) {
				//-90 -- 90 
				if(angleAroundCenter >= -90 && angleAroundCenter <= 90) {
					calX += Mouse.getDX() * ms;
					calZ -= Mouse.getDX() * ms;
				}
				
				//90 -- 180 
				if(angleAroundCenter > 90 && angleAroundCenter <= 180) {
					calX -= Mouse.getDX() * ms;
					calZ += Mouse.getDX() * ms;
				}
				
				//-90 -- -180 
				if(angleAroundCenter < -90 && angleAroundCenter >= -180) {
					calX -= Mouse.getDX() * ms;
					calZ += Mouse.getDX() * ms;
				}
				
				//Y aksis
				calY -= Mouse.getDY() * ms;
				center.setPosition(new Vector3f(calX, calY, calZ));
			}
		}
	}
	
	private void calculateCameraPosition(float hor, float ver) 
	{
		float theta = angleAroundCenter + center.getRotY();
		float xOffset = (float) (hor * Math.sin(Math.toRadians(theta)));
		float zOffset = (float) (hor * Math.cos(Math.toRadians(theta)));
		
		position.x = center.getPosition().x - xOffset;
		position.z = center.getPosition().z - zOffset;
		position.y = center.getPosition().y + ver;
		
		if(angleAroundCenter > 180.1)
			angleAroundCenter = -180;
		if(angleAroundCenter < -180.1)
			angleAroundCenter = 180;
			
	}
	
	private float calculateHorizontal() {
		return (float) (distanceFromCenter * Math.cos(Math.toRadians(pitch)));
	}
	
	private float calculateVertical() {
		return (float) (distanceFromCenter * Math.sin(Math.toRadians(pitch)));
	}
	
	private void calculateZoom()
	{
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromCenter -= zoomLevel;
	}
	
	private void calculatePitch()
	{
		if(Mouse.isButtonDown(1)) {
			float deltaPitch = Mouse.getDY() * 0.1f;
			pitch -= deltaPitch;
		}
	}
	
	private void calculateAngleAroundCenter()
	{
		if(Mouse.isButtonDown(1)) {
			float deltaAngle = Mouse.getDX() * 0.1f;
			angleAroundCenter -= deltaAngle;
		}
	}
	
	public float getDistanceFromCenter()
	{
		return this.distanceFromCenter;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	public float getPitch() {
		return pitch;
	}
	public float getYaw() {
		return yaw;
	}
	public float getRoll() {
		return roll;
	}
	
	public float getAngleAroundCenter() {
		return angleAroundCenter;
	}
}
