package hnd.src.renderer;


import org.joml.Matrix4f;

/**
 * The Camera class represents a camera in 3D space, which can be used to render a scene from a certain viewpoint.
 */
public class Camera {

    /**
     * The projection matrix used by this camera to convert 3D coordinates to 2D screen coordinates.
     */
    protected Matrix4f projection;

    /**
     * Creates a new Camera with the specified projection matrix.
     *
     * @param projection the projection matrix to use for this camera
     */
    public Camera(Matrix4f projection) {
        this.projection = projection;
    }
}
