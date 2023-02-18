package hnd.src.renderer.vertex;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.nio.FloatBuffer;

public class Vertex {
	public Vector3f position = new Vector3f();
	public Vector4f color = new Vector4f();
	public Vector2f texCoords = new Vector2f();
	public float texID;

	// Size of Vertex buffer: position = 3 + color = 4 + texCoords = 2 + texID = 1;
	private final int size = 10 * Float.BYTES;

	public int size() {
		return size;
	}

	public int getPositionOffset() {
		return 0;
	}

	public int getColorOffset() {
		return 12;
	}

	public int getTexCoordsOffset() {
		return 28;
	}

	public int getTexIDOffset() {
		return 36;
	}

	public float[] asFloatArray() {
		return new float[]{position.x, position.y, position.z, color.x, color.y, color.z, color.w, texCoords.x, texCoords.y, texID};
	}
}
