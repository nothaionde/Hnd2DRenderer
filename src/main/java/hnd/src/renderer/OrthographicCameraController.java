package hnd.src.renderer;

import hnd.src.platform.windows.WindowsInput;
import org.joml.Math;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class OrthographicCameraController {

	private float cameraRotation = 0.0f; //In degrees, in the anti-clockwise direction
	private float cameraTranslationSpeed = 5.0f;
	private float cameraRotationSpeed = 180.0f;
	private float aspectRatio;
	private float zoomLevel = 1.0f;
	private OrthographicCamera camera;
	private boolean rotation;
	private Vector3f cameraPosition = new Vector3f(0.0f, 0.0f, 0.0f);

	public OrthographicCameraController(float aspectRatio) {
		this.aspectRatio = aspectRatio;
		this.camera = new OrthographicCamera(-aspectRatio * zoomLevel, aspectRatio * zoomLevel, -zoomLevel, zoomLevel);
		this.rotation = false;
	}

	public void onResize(float width, float height) {
		aspectRatio = width / height;
		camera.setProjection(-aspectRatio * zoomLevel, aspectRatio * zoomLevel, -zoomLevel, zoomLevel);
	}

	public void onUpdate(float ts) {
		if (WindowsInput.isKeyPressed(GLFW.GLFW_KEY_A)) {
			cameraPosition.x -= Math.cos(Math.toRadians(cameraRotation)) * cameraTranslationSpeed * ts;
			cameraPosition.y -= Math.sin(Math.toRadians(cameraRotation)) * cameraTranslationSpeed * ts;
		} else if (WindowsInput.isKeyPressed(GLFW.GLFW_KEY_D)) {
			cameraPosition.x += Math.cos(Math.toRadians(cameraRotation)) * cameraTranslationSpeed * ts;
			cameraPosition.y += Math.sin(Math.toRadians(cameraRotation)) * cameraTranslationSpeed * ts;
		}
		if (WindowsInput.isKeyPressed(GLFW.GLFW_KEY_W)) {
			cameraPosition.x += -Math.sin(Math.toRadians(cameraRotation)) * cameraTranslationSpeed * ts;
			cameraPosition.y += Math.cos(Math.toRadians(cameraRotation)) * cameraTranslationSpeed * ts;
		} else if (WindowsInput.isKeyPressed(GLFW.GLFW_KEY_S)) {
			cameraPosition.x -= -Math.sin(Math.toRadians(cameraRotation)) * cameraTranslationSpeed * ts;
			cameraPosition.y -= Math.cos(Math.toRadians(cameraRotation)) * cameraTranslationSpeed * ts;
		}
		if (rotation) {
			if (WindowsInput.isKeyPressed(GLFW.GLFW_KEY_Q)) {
				cameraRotation += cameraRotationSpeed * ts;
			}
			if (WindowsInput.isKeyPressed(GLFW.GLFW_KEY_Q)) {
				cameraRotation -= cameraRotationSpeed * ts;
			}
			if (cameraRotation > 180.0f) {
				cameraRotation -= 360.0f;
			} else if (cameraRotation <= -180.0f) {
				cameraRotation += 360.0f;
			}
			camera.setRotation(cameraRotation);
		}
		camera.setPosition(cameraPosition);
		cameraTranslationSpeed = zoomLevel;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}
}
