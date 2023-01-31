package Display;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import Model.RawModel;

public class OBJloader
{
	public static RawModel loadObjModel(String fileName, Loader loader)
	{
		FileReader fr = null;
		try {
			fr = new FileReader(new File("res/obj/" + fileName + ".obj"));
		} catch (FileNotFoundException e) {
			System.err.println("Kan ikke finde obj file");
			e.printStackTrace();
		}
		
		BufferedReader reader = new BufferedReader(fr);
		String line;
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> textures = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();
		
		float[] verticesArray = null;
		float[] textureArray = null;
		float[] normalsArray = null;
		int[] indicesArray = null;
		
		try{
			while(true) {
				line = reader.readLine();
				String[] currentLine = line.split(" ");
				
				if(line.startsWith("v ")) {
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), 
							Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					vertices.add(vertex);
				}else if(line.startsWith("vt ")) {
					Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
					textures.add(texture);
				}else if(line.startsWith("vn ")) {
					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), 
							Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					normals.add(normal);
				}else if(line.startsWith("f ")) {
					textureArray = new float[vertices.size() * 2];
					normalsArray = new float[vertices.size() * 3];
					break;
				}
			}
			
			while(line != null)
			{
				if(!line.startsWith("f ")) 
				{
					line = reader.readLine();
					continue;
				}
				
				String[] currentLine = line.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");
				
				processVertex(vertex1, indices, textures, normals, textureArray, normalsArray);
				processVertex(vertex2, indices, textures, normals, textureArray, normalsArray);
				processVertex(vertex3, indices, textures, normals, textureArray, normalsArray);
				line = reader.readLine();
			}
			reader.close();
			
		}catch(Exception e){}
		
		verticesArray = new float[vertices.size() * 3];
		indicesArray = new int[indices.size()];
		
		int vertexPointer = 0;
		for(Vector3f vertex : vertices) {
			verticesArray[vertexPointer++] = vertex.x;
			verticesArray[vertexPointer++] = vertex.y;
			verticesArray[vertexPointer++] = vertex.z;
		}
		
		for(int i = 0; i < indices.size(); i++) {
			indicesArray[i] = indices.get(i);
		}
		
		return loader.loadToVAO(verticesArray, textureArray, normalsArray, indicesArray);
	}
	
	private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures, 
			List<Vector3f> normals, float[] textureArray, float[] normalArray) 
	{
		int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
		indices.add(currentVertexPointer);
		
		Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
		textureArray[currentVertexPointer * 2] = currentTex.x;
		textureArray[currentVertexPointer * 2 + 1] = 1 - currentTex.y;
		
		Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
		normalArray[currentVertexPointer * 3] = currentNorm.x;
		normalArray[currentVertexPointer * 3 + 1] = currentNorm.y;
		normalArray[currentVertexPointer * 3 + 2] = currentNorm.z;
	}
}
