package Guis;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import Display.Loader;
import Model.RawModel;
import Toolbox.Maths;

public class GuiRender
{
	private final RawModel quad;
	private GuiShader shader;
	
	public GuiRender(Loader loader)
	{
		float[] positions = {-1, 1, -1,-1, 1,1, 1,-1};
		quad = loader.loadToVAO(positions);
		shader = new GuiShader();
	}
	
	public void render(List<GuiTexture> guis)
	{
		shader.start();
		GL30.glBindVertexArray(quad.getVaoId());
		GL20.glEnableVertexAttribArray(0);
		for(GuiTexture texture : guis)
		{
			if(texture.isShouldRender()){
				GL13.glActiveTexture(GL13.GL_TEXTURE0);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
				Matrix4f transformationMatrix = Maths.createTransformationMatrix(texture.getPosition(), texture.getScale());
				shader.loadTransformationMatrix(transformationMatrix);
				GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
				texture.render();
			} else {
				texture.cleanUp();
			}
		}
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	public void cleanUp()
	{
		shader.cleanUp();
	}
}
