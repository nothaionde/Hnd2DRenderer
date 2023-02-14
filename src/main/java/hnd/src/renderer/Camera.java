package hnd.src.renderer;

import org.joml.Matrix4f;

public class Camera {

	protected Matrix4f projection;

	public Camera(Matrix4f projection) {
		this.projection = projection;
	}
}
