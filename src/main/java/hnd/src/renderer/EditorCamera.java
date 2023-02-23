package hnd.src.renderer;


import hnd.src.platform.windows.WindowsInput;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class EditorCamera extends Camera {

    private final float FOV;
    private float aspectRatio;
    private final float nearClip;
    private final float farClip;
    private final Matrix4f viewMatrix = new Matrix4f();
    private Vector3f position = new Vector3f(0.0f, 0.0f, 0.0f);
    private final Vector3f focalPoint = new Vector3f(0.0f, 0.0f, 0.0f);
    private Vector2f initialMousePosition = new Vector2f(0.0f, 0.0f);
    private float distance = 10.0f;
    private final float pitch = 0.0f;
    private final float yaw = 0.0f;
    private float viewportWidth = 1280;
    private float viewportHeight = 720;

    public EditorCamera(float fov, float aspectRatio, float nearClip, float farClip) {
        super(new Matrix4f().perspective(Math.toRadians(fov), aspectRatio, nearClip, farClip));
        FOV = fov;
        this.aspectRatio = aspectRatio;
        this.nearClip = nearClip;
        this.farClip = farClip;
        updateView();
    }

    private void updateView() {
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
        if (WindowsInput.isKeyPressed(GLFW.GLFW_KEY_LEFT_ALT)) {
            Vector2f mouse = new Vector2f(WindowsInput.getMouseX(), WindowsInput.getMouseY());
            Vector2f delta = new Vector2f((mouse.x - initialMousePosition.x) * 0.003f, (mouse.y - initialMousePosition.y) * 0.003f);
            initialMousePosition = mouse;

            if (WindowsInput.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_MIDDLE)) {
                mousePan(delta);
            } else if (WindowsInput.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
                mouseRotate(delta);
            } else if (WindowsInput.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
                mouseZoom(delta.y);
            }
        }
        updateView();
    }

    private void mousePan(Vector2f delta) {
    }

    private void mouseRotate(Vector2f delta) {

    }

    private void mouseZoom(float delta) {
        distance -= delta * zoomSpeed();
        if (distance < 1.0f) {
//            focalPoint += getForwardDirection();
            distance = 1.0f;
        }
    }

    private float zoomSpeed() {
        float distance = this.distance * 0.2f;
        distance = java.lang.Math.max(distance, 0.2f);
        float speed = distance * distance;
        speed = java.lang.Math.min(speed, 100.0f); // 100 is max speed
        return speed;
    }

    public Matrix4f getViewProjection() {
//        return projection.mul(viewMatrix);
        return viewMatrix;
    }
}
