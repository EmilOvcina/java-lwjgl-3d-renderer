package Display;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import Entities.Camera;
import Entities.Entity;
import Entities.Light;
import Font.FontType;
import Font.GUIText;
import FontRender.TextMaster;
import Guis.GuiAddObject;
import Guis.GuiRender;
import Guis.GuiTexture;
import Model.RawModel;
import Model.TexturedModel;
import Textures.ModelTexture;
import Toolbox.MousePicker;

public class Main 
{
	public static GuiAddObject addObject;
	public static List<Entity> entities;
	public static List<GUIText> objects;
	
	public static FontType font;
	
	public static Loader l;
	
	public static void main(String[] args)
	{
		DisplayManager.createDisplay();
		
		l = new Loader();
		TextMaster.init(l);
		
		font = new FontType(l.loadFontTexture("tahoma"), new File("res/tahoma.fnt"));
		String text1 = "Objekter: ";
		GUIText text = new GUIText(text1, 1, font, new Vector2f(0,0.005f), 0.2f, true);
		text.setColour(1, 1, 1);
		
		GUIText textLight = new GUIText("Lys: ", 1, font, new Vector2f(0.5f,0.005f), 0.2f, true);
		textLight.setColour(1, 1, 1);
		
		//
		entities = new ArrayList<Entity>();
		RawModel quad = OBJloader.loadObjModel("stall", l);
		ModelTexture texture = new ModelTexture(l.loadtexture("obj/stallTexture"));
		texture.setReflectivity(1);
		texture.setShineDamper(10);
		TexturedModel texturedModel = new TexturedModel(quad, texture);
		Entity stall = new Entity(texturedModel, new Vector3f(5,0, -10), 0, 0, 0, 1, "Stall");
		
		Entity fern = new Entity(new TexturedModel(OBJloader.loadObjModel("fern", l), new ModelTexture(l.loadtexture("obj/fern"))), 
				new Vector3f(0, 0, 0), 0, 0, 0, 1, "Fern");
		fern.getModel().getTexture().setHasTransparency(true);
		fern.getModel().getTexture().setUseFakeLighting(true);
		
		entities.add(stall);
		entities.add(fern);
		
		Entity center = new Entity(new TexturedModel(OBJloader.loadObjModel("fern", l), new ModelTexture(l.loadtexture("black"))), 
				new Vector3f(0, 0, 0), 0, 0, 0, 1);
		fern.getModel().getTexture().setHasTransparency(true);
		fern.getModel().getTexture().setUseFakeLighting(true);
		Camera camera = new Camera(center);
		
		List<Light> lights = new ArrayList<Light>();
		Light sun = new Light(new Vector3f(0,10,10), new Vector3f(0.4f,0.4f,0.4f));
		Light sun1 = new Light(new Vector3f(0,20,0), new Vector3f(1,0,1), new Vector3f(1, 0.01f, 0.003f));
		lights.add(sun);
		lights.add(sun1);
		
		objects = new ArrayList<GUIText>();
		arrangeObjectList();
		
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiTexture gui = new GuiTexture(l.loadtexture("gui"), new Vector2f(-1, 1), new Vector2f(0.4f, 2f));
		GuiTexture toolbar = new GuiTexture(l.loadtexture("gui"), new Vector2f(-1, 1), new Vector2f(2f, 0.1f));
		
		//Knapper
		GuiTexture buttonAddObject = new GuiTexture(l.loadtexture("button"), new Vector2f(-0.5f, 0.948f), new Vector2f(0.05f,0.05f));
		GUIText textBAO = new GUIText("+", 1, font, new Vector2f(0.15f,0.007f), 0.2f, true);
		textBAO.setColour(1, 1, 1);
		
		GuiTexture buttonDelObject = new GuiTexture(l.loadtexture("button"), new Vector2f(-0.4f, 0.948f), new Vector2f(0.05f,0.05f));
		GUIText textBDO = new GUIText("-", 1, font, new Vector2f(0.2f,0.007f), 0.2f, true);
		textBDO.setColour(1, 1, 1);
	
		guis.add(buttonAddObject);
		guis.add(buttonDelObject);

		guis.add(toolbar);
		guis.add(gui);
		
		//Add object GUI
		addObject = new GuiAddObject(l.loadtexture("guiAddObject"), new Vector2f(0f,0f), new Vector2f(0.5f,0.6f));
		guis.add(addObject);
		addObject.setShouldRender(false);
		
		MasterRender render = new MasterRender();
		GuiRender guiRender = new GuiRender(l);
		
		MousePicker mp = new MousePicker(camera, render.getProjectionMatrix());
		
		while(!Display.isCloseRequested()) {
			//Update
			camera.move();
			mp.update();
			TextMaster.updateObjectsList(objects);
			render.updateEntity(objects, entities, mp, camera);
			camera.calculateCenterMovement();
			
			//Rendering
			for(Entity entity : entities)
			{
				render.processEntity(entity);
			}
			render.render(lights, camera);
			guiRender.render(guis);
			TextMaster.render();
			DisplayManager.updateDisplay();
		}
		TextMaster.cleanUp();
		guiRender.cleanUp();
		render.cleanUp();
		l.cleanUp();
		DisplayManager.cleanUp();
	}
	
	public static void arrangeObjectList()
	{ 
		objects.clear();
		
		for(int i = 0; i < entities.size(); i++)
		{
			objects.add(new GUIText(entities.get(i).getName(), 1, font, new Vector2f(0,0.3f), 0.2f, true));
		}
		
		for(int i = 0; i < objects.size(); i++)
		{
			float margin = (float) (i * 0.05);
			objects.get(i).setPosition(new Vector2f(0, 0.1f + margin));
			objects.get(i).setColour(1, 1, 1);
		}
		
		objects.get(0).setSelected(true);
	}
}