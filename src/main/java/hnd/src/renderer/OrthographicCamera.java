package hnd.src.renderer;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class OrthographicCamera {

	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;
	private Matrix4f viewProjectionMatrix;
	private Vector3f position = new Vector3f(0.0f, 0.0f, 0.0f);
	private float rotation = 0.0f;

	public OrthographicCamera(float left, float right, float bottom, float top) {
		viewMatrix = new Matrix4f();
		projectionMatrix = new Matrix4f().ortho(left, right, bottom, top, -1.0f, 1.0f);
		viewProjectionMatrix = projectionMatrix.mul(viewMatrix);
	}

	public void setProjection(float left, float right, float bottom, float top) {
		projectionMatrix = new Matrix4f().ortho(left, right, bottom, top, -1.0f, 1.0f);
		viewProjectionMatrix = projectionMatrix.mul(viewMatrix);
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
		recalculateViewMatrix();
	}

	public void setPosition(Vector3f position) {
		this.position = position;
		recalculateViewMatrix();
	}

	private void recalculateViewMatrix() {
		Matrix4f transform = new Matrix4f().translate(position).
				mul(new Matrix4f().rotate(Math.toRadians(rotation), new Vector3f(0, 0, 1)));
		viewMatrix = transform.invert();
		viewProjectionMatrix = projectionMatrix.mul(viewMatrix);
	}

	public Matrix4f getViewProjectionMatrix() {
		return viewProjectionMatrix;
	}
}
