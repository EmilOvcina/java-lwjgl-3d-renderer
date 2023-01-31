package Display;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager 
{
	private static final int WIDTH = 920, HEIGHT = 640;
	private static final String VERSION = "0.5";
	
	
	public static void createDisplay()
	{
		PixelFormat pformat = new PixelFormat();
		ContextAttribs cattribs = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setTitle("Version: " + VERSION + " - openGL: " + Sys.getVersion());
			Display.setResizable(true);
			Display.create(pformat, cattribs);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
	}
	
	public static void updateDisplay()
	{
		Display.sync(60);
		Display.update();
	}
	
	public static void cleanUp()
	{
		Display.destroy();
	}
}
