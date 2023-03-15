package hnd.src.renderer;

import hnd.src.platform.windows.WindowsInput;
import org.joml.Math;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

/**
 * An orthographic camera controller that allows the user to move and rotate the camera in a 2D scene.
 */
public class OrthographicCameraController {

    /**
     * The current rotation of the camera in degrees, in the anti-clockwise direction.
     */
    private float cameraRotation = 0.0f;

    /**
     * The speed at which the camera is translated.
     */
    private float cameraTranslationSpeed = 5.0f;

    /**
     * The speed at which the camera is rotated.
     */
    private final float cameraRotationSpeed = 180.0f;

    /**
     * The aspect ratio of the camera.
     */
    private float aspectRatio;

    /**
     * The zoom level of the camera.
     */
    private final float zoomLevel = 1.0f;

    /**
     * The orthographic camera that this controller is controlling.
     */
    private final OrthographicCamera camera;

    /**
     * Whether or not the camera can be rotated.
     */
    private final boolean rotation;

    /**
     * The current position of the camera.
     */
    private final Vector3f cameraPosition = new Vector3f(0.0f, 0.0f, 0.0f);


    /**
     * Constructs an `OrthographicCameraController` with the given aspect ratio.
     *
     * @param aspectRatio the aspect ratio of the camera's viewport
     */

    public OrthographicCameraController(float aspectRatio) {
        this.aspectRatio = aspectRatio;
        this.camera = new OrthographicCamera(-aspectRatio * zoomLevel, aspectRatio * zoomLevel, -zoomLevel, zoomLevel);
        this.rotation = false;
    }

    /**
     * Called when the size of the camera's viewport changes. Updates the camera's projection matrix.
     *
     * @param width  the new width of the viewport
     * @param height the new height of the viewport
     */

    public void onResize(float width, float height) {
        aspectRatio = width / height;
        camera.setProjection(-aspectRatio * zoomLevel, aspectRatio * zoomLevel, -zoomLevel, zoomLevel);
    }

    /**
     * Called every frame to update the camera's position and rotation based on user input.
     *
     * @param ts the time elapsed since the last frame, in seconds
     */

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
            if (WindowsInput.isKeyPressed(GLFW.GLFW_KEY_E)) {
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

    /**
     * Returns the orthographic camera that this controller is controlling.
     *
     * @return the orthographic camera
     */

    public OrthographicCamera getCamera() {
        return camera;
    }
}
