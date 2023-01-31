package Shaders;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import Entities.Camera;
import Entities.Light;
import Toolbox.Maths;

public class StaticShader extends ShaderProgram
{
	private static final int MAX_LIGHTS = 16;
	
	private static String VERTEX_FILE = "/Shaders/vertexShader.txt";
	private static String FRAGMENT_FILE = "/Shaders/fragmentShader.txt";
	
	private int location_transformationmatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition[];
	private int location_lightColour[];
	private int location_attenuation[];
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_useFakeLighting;
	
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	protected void getAllUniformLocations() {
		location_transformationmatrix = super.getUniformLocation("transformationmatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_useFakeLighting = super.getUniformLocation("useFakeLighting");
		
		location_lightColour = new int[MAX_LIGHTS];
		location_lightPosition = new int[MAX_LIGHTS];
		location_attenuation = new int[MAX_LIGHTS];
		
		for(int i = 0; i < MAX_LIGHTS; i++)
		{
			location_lightColour[i] = super.getUniformLocation("lightColour[" + i + "]");
			location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
			location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
		}
	}

	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}
	
	public void loadFakeLightingVariable(boolean useFake)
	{
		super.loadBoolean(location_useFakeLighting, useFake);
	}
	
	public void loadShineVariables(float damper, float reflectivity)
	{
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationmatrix, matrix);
	}
	
	public void loadLight(List<Light> lights)
	{
		for(int i = 0; i < MAX_LIGHTS; i++)
		{
			if(i < lights.size()) {
				super.loadVector(location_lightPosition[i], lights.get(i).getPosition());
				super.loadVector(location_lightColour[i], lights.get(i).getColour());
				super.loadVector(location_attenuation[i], lights.get(i).getAttenuation());
			} else {
				super.loadVector(location_lightPosition[i], new Vector3f(0,0,0));
				super.loadVector(location_lightColour[i], new Vector3f(0,0,0));
				super.loadVector(location_attenuation[i], new Vector3f(1,0,0));
			}
		}
	}
	
	public void loadProjectionMatrix(Matrix4f matrix)
	{
		super.loadMatrix(location_projectionMatrix, matrix);
	}
	
	public void loadViewMatrix(Camera camera)
	{
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
}
