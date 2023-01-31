package Display;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import Entities.Camera;
import Entities.Entity;
import Entities.Light;
import Font.GUIText;
import Model.TexturedModel;
import Shaders.StaticShader;
import Toolbox.MousePicker;

public class MasterRender 
{
	private static final float FOV = 70;
	private static final float NEAR_PLANE = -5;
	private static final float FAR_PLANE = 1000;
	private Matrix4f projectionMatrix;
	
	private StaticShader shader = new StaticShader();
	private Render render;
	
	private HashMap<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private float calScale = 1;
	private float translateX = 1;
	private float translateY = 1;
	private float translateZ = 1;
	private float rotX = 0;
	private float rotY = 0;
	private float rotZ = 0;
	private Vector3f calPos = new Vector3f(0,0,0);
	
	public MasterRender()
	{
		enableCulling();
		createProjectionMatrix();
		render = new Render(shader, projectionMatrix);
	}
	
	public void updateEntity(List<GUIText> text, List<Entity> entities, MousePicker mp, Camera cam)
	{
		if(Mouse.getX() > 184 && Mouse.getY() < 605 && !Main.addObject.isShouldRender()) {
			//Scale object
			if(Keyboard.isKeyDown(Keyboard.KEY_Z)) {
				for(int i = 0; i < text.size(); i++)
				{
					if(text.get(i).isSelected())
					{
						calScale += Mouse.getDY() * 0.03f;
						entities.get(i).setScale(calScale);
					}
				}
			}
			
			//Rotate x
			if(Keyboard.isKeyDown(Keyboard.KEY_A)) 
			{
				for(int i = 0; i < text.size(); i++)
				{
					if(text.get(i).isSelected())
					{
						if(cam.getAngleAroundCenter() > 0 && cam.getAngleAroundCenter() < 180)
							rotX += Mouse.getDX() * 0.2f;
						else 
							rotX -= Mouse.getDX() * 0.2f;
						entities.get(i).setRotX(rotX);
					}
				}
			}
			
			//Rotate Z
			if(Keyboard.isKeyDown(Keyboard.KEY_D)) 
			{
				for(int i = 0; i < text.size(); i++)
				{
					if(text.get(i).isSelected())
					{
						if(cam.getAngleAroundCenter() > -90 && cam.getAngleAroundCenter() < 90)
							rotZ += Mouse.getDX() * 0.2f;
						else 
							rotZ -= Mouse.getDX() * 0.2f;
						entities.get(i).setRotZ(rotZ);
					}
				}
			}
			
			//Rotate Y
			if(Keyboard.isKeyDown(Keyboard.KEY_S)) 
			{
				for(int i = 0; i < text.size(); i++)
				{
					if(text.get(i).isSelected())
					{
						if(cam.getAngleAroundCenter() > -90 && cam.getAngleAroundCenter() < 90)
							rotY += Mouse.getDX() * 0.2f;
						else 
							rotY -= Mouse.getDX() * 0.2f;
						rotY -= Mouse.getDX() * 0.2f;
						entities.get(i).setRotY(rotY);
					}
				}
			}
			
			//Translate on x-axis
			if(Keyboard.isKeyDown(Keyboard.KEY_Q)) {
				for(int i = 0; i < text.size(); i++)
				{
					if(text.get(i).isSelected())
					{
						if(cam.getAngleAroundCenter() > -90 && cam.getAngleAroundCenter() < 90)
							translateX -= Mouse.getDX() * 0.03f;
						else 
							translateX += Mouse.getDX() * 0.03f;
							
						entities.get(i).setPosition(new Vector3f(translateX,entities.get(i).getPosition().y, entities.get(i).getPosition().z));
					}
				}
			}
			//Translate on y-axis
			if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
				for(int i = 0; i < text.size(); i++)
				{
					if(text.get(i).isSelected())
					{
						translateY += Mouse.getDY() * 0.03f;
						entities.get(i).setPosition(new Vector3f(entities.get(i).getPosition().x,translateY, entities.get(i).getPosition().z));
					}
				}
			}
			//Translate on z-axis
			if(Keyboard.isKeyDown(Keyboard.KEY_E)) {
				for(int i = 0; i < text.size(); i++)
				{
					if(text.get(i).isSelected())
					{
						if(cam.getAngleAroundCenter() > 0 && cam.getAngleAroundCenter() < 180)
							translateZ += Mouse.getDX() * 0.03f;
						else
							translateZ -= Mouse.getDX() * 0.03f;
					
						entities.get(i).setPosition(new Vector3f(entities.get(i).getPosition().x, entities.get(i).getPosition().y, translateZ));
					}
				}
			}
				
			
			//Translate object
			if(Mouse.isButtonDown(0)) {
				if(!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && !Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
					for(int i = 0; i < text.size(); i++)
					{
						if(text.get(i).isSelected())
						{
							calPos.x = (mp.getCurrentRay().x * cam.getDistanceFromCenter()) + cam.getPosition().x;
							calPos.y = (mp.getCurrentRay().y * cam.getDistanceFromCenter()) + cam.getPosition().y;
							calPos.z = (mp.getCurrentRay().z * cam.getDistanceFromCenter()) + cam.getPosition().z;
							entities.get(i).setPosition(calPos);
						}
					}
				}
			}
		}
		calPos = new Vector3f(0,0,0);
	}
	
	public static void enableCulling()
	{
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void disableCulling()
	{
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	public void render(List<Light> lights, Camera camera)
	{
		prepare();
		shader.start();
		shader.loadLight(lights);
		shader.loadViewMatrix(camera);
		render.render(entities);
		shader.stop();
		entities.clear();
	}
	
	public void processEntity(Entity entity)
	{
		TexturedModel model = entity.getModel();
		List<Entity> batch = entities.get(model);
		if(batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(model, newBatch);
		}
	}
	
	public void cleanUp()
	{
		shader.cleanUp();
	}
	
	public void prepare()
	{
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0.7f, 0.7f, 0.7f, 1);
	}
	
	private void createProjectionMatrix()
	{
		float aspectRatio = (float)Display.getDisplayMode().getWidth() / (float) Display.getDisplayMode().getHeight();
		 float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		 float x_scale = y_scale / aspectRatio;
		 float frustum_length = FAR_PLANE - NEAR_PLANE;
		 
		 projectionMatrix = new Matrix4f();
		 projectionMatrix.m00 = x_scale;
		 projectionMatrix.m11 = y_scale;
		 projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		 projectionMatrix.m23 = -1;
		 projectionMatrix.m32 = -((2 * FAR_PLANE + NEAR_PLANE) / frustum_length);
		 projectionMatrix.m33 = 0;
	}
	
	public Matrix4f getProjectionMatrix()
	{
		return this.projectionMatrix;
	}
}
