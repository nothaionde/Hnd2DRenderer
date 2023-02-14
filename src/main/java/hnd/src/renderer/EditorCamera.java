package hnd.src.renderer;


import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class EditorCamera extends Camera {

	private float FOV = 45.0f;
	private float aspectRatio = 1.778f;
	private float nearClip = 0.1f;
	private float farClip = 1000.0f;
	private Matrix4f viewMatrix;
	private Vector3f position = new Vector3f(0.0f, 0.0f, 0.0f);
	private Vector3f focalPoint = new Vector3f(0.0f, 0.0f, 0.0f);
	private Vector2f initialMousePosition = new Vector2f(0.0f, 0.0f);
	private float distance = 10.0f;
	private float pitch = 0.0f;
	private float yaw = 0.0f;
	private float viewportWidth = 1280;
	private float viewportHeight = 720;

	public EditorCamera(float fov, float aspectRatio, float nearClip, float farClip) {
		super(new Matrix4f().perspective(Math.toRadians(fov), aspectRatio, nearClip, farClip));
		FOV = fov;
		this.aspectRatio = aspectRatio;
		this.nearClip = nearClip;
		this.farClip = farClip;
	}

	public void setViewportSize(float width, float height) {
		viewportWidth = width;
		viewportHeight = height;
		updateProjection();
	}

	private void updateProjection() {
		aspectRatio = viewportWidth / viewportHeight;
		projection = new Matrix4f().perspective(Math.toRadians(FOV), aspectRatio, nearClip, farClip);
	}

	public void onUpdate(float ts) {
		// TODO editor camera update view
	}
}
