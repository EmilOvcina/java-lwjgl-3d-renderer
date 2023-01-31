package FontRender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL30;

import Display.Loader;
import Font.FontType;
import Font.GUIText;
import Font.TextMeshData;

public class TextMaster 
{
	private static Loader loader;
	private static Map<FontType, List<GUIText>> texts = new HashMap<FontType, List<GUIText>>();
	private static FontRenderer renderer; 
	
	private static int selectedIndex = 0;
	private static boolean pressed = false;
	
	public static void updateObjectsList(List<GUIText> list)
	{
		for(int i = 0; i < list.size(); i++)
		{
			list.get(i).setSelected(false);
		}
		list.get(selectedIndex).setSelected(true);
		
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN) && selectedIndex < list.toArray().length - 1 && !pressed) {
			selectedIndex++;
			pressed = true;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_UP) && selectedIndex > 0 && !pressed) {
			selectedIndex--;
			pressed = true;
		}
		
		if(!Keyboard.isKeyDown(Keyboard.KEY_UP) && !Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			pressed = false;
		}
		
		for(int i = 0; i < list.size(); i++)
		{
			if(list.get(i).isSelected()) {
				list.get(i).setColour(0.2f, 1, 0.2f);
			} else {
				list.get(i).setColour(1,1,1);
			}
		}
		
	}
	
	public static void init(Loader theLoader)
	{
		renderer = new FontRenderer();
		loader = theLoader; 
	}
	
	public static void render()
	{
		renderer.render(texts);
	}
	
	public static void loadText(GUIText text)
	{
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		int vao = loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
		text.setMeshInfo(vao, data.getVertexCount());
		List<GUIText> textBatch = texts.get(font);
		if(textBatch == null) {
			textBatch = new ArrayList<GUIText>();
			texts.put(font, textBatch);
		}
		
		textBatch.add(text);
	}
	
	public static void removeText(GUIText text)
	{
		List<GUIText> textBatch = texts.get(text.getFont());
		textBatch.remove(text);
		if(textBatch.isEmpty()) {
			texts.remove(text.getFont());
			GL30.glDeleteVertexArrays(text.getMesh());
		}
	}
	
	public static void cleanUp()
	{
		renderer.cleanUp();
	}
}
