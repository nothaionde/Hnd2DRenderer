package hnd.src.renderer;


import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;


/**
 * A camera that can be used in an editor context. Allows the user to control the camera's position and orientation.
 */
public class EditorCamera extends Camera {

    private float viewportWidth = 1280;
    private float viewportHeight = 720;
    private float FOV = 30.0f;
    private float aspectRatio = 1.778f;
    private float nearClip = 0.1f;
    private float farClip = 1000.0f;
    private Matrix4f viewMatrix = new Matrix4f();
    private Vector3f position = new Vector3f(0.0f, 0.0f, 0.0f);
    private Vector3f focalPoint = new Vector3f(0.0f, 0.0f, 0.0f);
    private Vector2f initialMousePosition = new Vector2f(0.0f, 0.0f);
    private float distance = 0.0f;
    private float pitch = 0.0f;
    private float yaw = 0.0f;

    /**
     * Constructs a new EditorCamera with the given parameters.
     *
     * @param fov         the field of view in degrees
     * @param aspectRatio the aspect ratio of the viewport (width/height)
     * @param nearClip    the near clipping plane distance
     * @param farClip     the far clipping plane distance
     */
    public EditorCamera(float fov, float aspectRatio, float nearClip, float farClip) {
        super(new Matrix4f().perspective((float) Math.toRadians(fov), aspectRatio, nearClip, farClip));
        this.FOV = fov;
        this.nearClip = nearClip;
        this.farClip = farClip;
        this.aspectRatio = aspectRatio;
        updateView();
    }

    /**
     * Returns the view-projection matrix for this camera.
     *
     * @return the view-projection matrix
     */
    public Matrix4f getViewProjection() {
        return viewMatrix;
    }

    /**
     * Sets the viewport size for this camera.
     *
     * @param width  the width of the viewport
     * @param height the height of the viewport
     */
    public void setViewportSize(float width, float height) {
        this.viewportWidth = width;
        this.viewportHeight = height;
        updateProjection();
    }

    /**
     * Updates the projection matrix based on the current viewport width, viewport height,
     * field of view, near clip distance, and far clip distance.
     */
    private void updateProjection() {
        aspectRatio = viewportWidth / viewportHeight;
        projection = new Matrix4f().perspective((float) Math.toRadians(FOV), aspectRatio, nearClip, farClip);
    }

    /**
     * Updates the view matrix based on the current position, orientation, and focal point.
     */
    private void updateView() {
        position = calculatePosition();

        Quaternionf orientation = getOrientation();
        viewMatrix = new Matrix4f().translate(position).mul(new Matrix4f().rotate(orientation));
        viewMatrix.invert();
    }

    /**
     * Calculates the orientation quaternion based on the current pitch and yaw angles.
     *
     * @return the orientation quaternion
     */
    private Quaternionf getOrientation() {
        return new Quaternionf().rotateXYZ(-pitch, -yaw, 0.0f);
    }

    /**
     * Calculates the camera position based on the current focal point, distance, and forward direction.
     *
     * @return the camera position
     */
    private Vector3f calculatePosition() {
        Vector3f forwardDirection = getForwardDirection();
        return focalPoint.sub(forwardDirection.mul(distance));
    }

    /**
     * Calculates the forward direction vector based on the current orientation.
     *
     * @return the forward direction vector
     */
    private Vector3f getForwardDirection() {
        Quaternionf orientation = getOrientation();
        return orientation.transform(new Vector3f(0.0f, 0.0f, -1.0f));
    }

    /**
     * Updates the camera state based on the elapsed time since the last update.
     *
     * @param ts the elapsed time since the last update, in seconds
     */
    public void onUpdate(float ts) {
        updateView();
    }
}
