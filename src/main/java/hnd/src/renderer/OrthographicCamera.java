package hnd.src.renderer;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * A camera that projects a 3D scene onto a 2D plane using an orthographic projection.
 */
public class OrthographicCamera {

    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
    private Matrix4f viewProjectionMatrix;
    private Vector3f position;
    private float rotation;

    /**
     * Constructs a new orthographic camera with the specified projection parameters.
     *
     * @param left   the left edge of the view frustum
     * @param right  the right edge of the view frustum
     * @param bottom the bottom edge of the view frustum
     * @param top    the top edge of the view frustum
     */
    public OrthographicCamera(float left, float right, float bottom, float top) {
        position = new Vector3f(0.0f, 0.0f, 0.0f);
        rotation = 0.0f;
        projectionMatrix = new Matrix4f().ortho(left, right, bottom, top, -1.0f, 1.0f);
        viewMatrix = new Matrix4f();
        viewProjectionMatrix = new Matrix4f().mul(projectionMatrix).mul(viewMatrix);
    }

    /**
     * Sets the projection parameters of this camera to the specified values.
     *
     * @param left   the left edge of the view frustum
     * @param right  the right edge of the view frustum
     * @param bottom the bottom edge of the view frustum
     * @param top    the top edge of the view frustum
     */
    public void setProjection(float left, float right, float bottom, float top) {
        projectionMatrix = new Matrix4f().ortho(left, right, bottom, top, -1.0f, 1.0f);
        viewProjectionMatrix = new Matrix4f().mul(projectionMatrix).mul(viewMatrix);
    }

    /**
     * Sets the rotation of this camera to the specified value.
     *
     * @param rotation the new rotation value, in degrees
     */
    public void setRotation(float rotation) {
        this.rotation = rotation;
        recalculateViewMatrix();
    }

    /**
     * Sets the position of this camera to the specified value.
     *
     * @param position the new position value
     */
    public void setPosition(Vector3f position) {
        this.position = position;
        recalculateViewMatrix();
    }

    /**
     * Recalculates the view matrix for this camera based on its position and rotation.
     */
    private void recalculateViewMatrix() {
        Matrix4f transform = new Matrix4f().translate(position).rotate((float) Math.toRadians(rotation), 0.0f, 0.0f, 1.0f);

        viewMatrix = transform.invert();
        viewProjectionMatrix = new Matrix4f().mul(projectionMatrix).mul(viewMatrix);
    }

    /**
     * Returns the view projection matrix for this camera.
     *
     * @return the view projection matrix
     */
    public Matrix4f getViewProjectionMatrix() {
        return viewProjectionMatrix;
    }
}
