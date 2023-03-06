package hnd.src.renderer;


import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class EditorCamera extends Camera {

    private float viewportWidth = 1280;
    private float viewportHeight = 720;
    private float FOV = 30.0f;
    private float aspectRatio = 1.778f;
    private float nearClip = 0.1f;
    private float farClip = 1000.0f;
    private Matrix4f viewMatrix = new Matrix4f();
    private Vector3f position = new Vector3f( 0.0f, 0.0f, 0.0f );
    private Vector3f focalPoint =  new Vector3f( 0.0f, 0.0f, 0.0f );
    private Vector2f initialMousePosition =  new Vector2f( 0.0f, 0.0f );
    private float distance = 10.0f;
    private float pitch = 0.0f;
    private float yaw = 0.0f;

    public EditorCamera(float fov, float aspectRatio, float nearClip, float farClip) {
        super(new Matrix4f().setPerspective((float) Math.toRadians(fov), aspectRatio, nearClip, farClip));
        this.FOV = fov;
        this.nearClip = nearClip;
        this.farClip = farClip;
        this.aspectRatio = aspectRatio;
        updateView();
    }
    public Matrix4f getViewProjection() {
        return viewMatrix;
    }

    public void setViewportSize(float width, float height) {
        this.viewportWidth = width;
        this.viewportHeight = height;
        updateProjectionMatrix();
    }

    private void updateProjectionMatrix() {

    }


    private void updateView() {

    }

    public void onUpdate(float ts) {

    }
}
