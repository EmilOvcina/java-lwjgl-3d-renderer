package Guis;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import Display.Main;
import Display.OBJloader;
import Entities.Entity;
import Font.GUIText;
import FontRender.TextMaster;
import Model.TexturedModel;
import Textures.ModelTexture;

public class GuiAddObject extends GuiTexture
{	
	private TextField name;
	private TextField fileName;
	private TextField x;
	private TextField y;
	private TextField z;
	
	private List<TextField> fields = new ArrayList<TextField>();
	
	public GuiAddObject(int textureID, Vector2f position, Vector2f scale) {
		super(textureID, position, scale);
		name = new TextField(new GUIText("Name", 1f, Main.font, new Vector2f(.43f, .4f), 12, false));
		fileName = new TextField(new GUIText("fileName", 1f, Main.font, new Vector2f(.43f, .47f), 12, false));
		
		x = new TextField(new GUIText("0", 1f, Main.font, new Vector2f(.43f, .61f), 12, false));
		y = new TextField(new GUIText("0", 1f, Main.font, new Vector2f(.43f, .675f), 12, false));
		z = new TextField(new GUIText("0", 1f, Main.font, new Vector2f(.43f, .74f), 12, false));
		
		fields.add(name);
		fields.add(fileName);
		fields.add(x);
		fields.add(y);
		fields.add(z);
	}
	
	public void init()
	{
		for(TextField field : fields)
		{
			TextMaster.loadText(field.getText());
		}
	}
	
	public void render()
	{
		for(TextField field : fields)
		{
			if(field.isFocus())
			{
				typeInField(field);
			}
		}
	}
	
	public void typeInField(TextField field)
	{
		String text = field.getText().getText();
		while(Keyboard.next()) {
			if(!Keyboard.getEventKeyState() && Keyboard.getEventKey() != Keyboard.KEY_LSHIFT 
					&& Keyboard.getEventKey() != Keyboard.KEY_RSHIFT) { 
				//System.out.println(Keyboard.getEventCharacter());
				text += Keyboard.getEventCharacter();
				if(text.length() > 1 && Keyboard.getEventKey() == Keyboard.KEY_BACK) {
					text = text.substring(0, text.length() - 2);
				}
			}
		}
		field.setText(text);
		TextMaster.removeText(field.getText());
		TextMaster.loadText(field.getText());
	}
	
	public void addEntity()
	{
		setShouldRender(false);
		String navn = name.getText().getText();
		String file = fileName.getText().getText();
		float xPos = Float.parseFloat(x.getText().getText());
		float yPos = Float.parseFloat(y.getText().getText());
		float zPos = Float.parseFloat(z.getText().getText());
		
		Entity temp = new Entity(new TexturedModel(OBJloader.loadObjModel(file, Main.l), new ModelTexture(Main.l.loadtexture("obj/"+file))), 
				new Vector3f(xPos, yPos, zPos), 0, 0, 0, 1, navn);
		
		Main.entities.add(temp);
		Main.arrangeObjectList();
		
		for(TextField field : fields)
		{
			field.setFocus(false);
			field.setText("");
		}
		System.out.println(name.getText().getPosition());
	}
	
	public void cleanUp()
	{
		for(TextField field : fields)
		{
			TextMaster.removeText(field.getText());
		}
	}
	
	public void updateMouse()
	{
		//X-button
		if(Mouse.getX() >= 645 && Mouse.getX() < 689 && Mouse.getY() > 406 && Mouse.getY() <= 505)
			setShouldRender(false);
		
		//Name textfield
		if(Mouse.getX() >= 392 && Mouse.getX() < 627 && Mouse.getY() > 356 && Mouse.getY() <= 391) {
			for(TextField fi : fields) {
				fi.setFocus(false);
			}
			name.setFocus(true);
		}
		
		//fileName textfield
		if(Mouse.getX() >= 392 && Mouse.getX() < 627 && Mouse.getY() > 309 && Mouse.getY() <= 343) {
			for(TextField fi : fields) {
				fi.setFocus(false);
			}
			fileName.setFocus(true);
		}
		
		//x textfield
		if(Mouse.getX() >= 392 && Mouse.getX() < 627 && Mouse.getY() > 221 && Mouse.getY() <= 255) {
			for(TextField fi : fields) {
				fi.setFocus(false);
			}
			x.setFocus(true);
		}
		
		//y textfield
		if(Mouse.getX() >= 392 && Mouse.getX() < 627 && Mouse.getY() > 180 && Mouse.getY() <= 214) {
			for(TextField fi : fields) {
				fi.setFocus(false);
			}
			y.setFocus(true);
		}
		
		//z textfield
		if(Mouse.getX() >= 392 && Mouse.getX() < 627 && Mouse.getY() > 137 && Mouse.getY() <= 172) {
			for(TextField fi : fields) {
				fi.setFocus(false);
			}
			z.setFocus(true);
		}
		
		//tilføj textfield
		if(Mouse.getX() >= 254 && Mouse.getX() < 379 && Mouse.getY() > 437 && Mouse.getY() <= 467) {
			addEntity();
		}
	}
}
