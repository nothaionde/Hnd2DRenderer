package hnd.src.scene.components;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Represents the transformation component of a game object.
 */
public class TransformComponent extends Component {

    /**
     * The translation vector of the game object.
     */
    public Vector3f translation = new Vector3f(0.0f, 0.0f, 0.0f);

    /**
     * The rotation vector of the game object.
     */
    public Vector3f rotation = new Vector3f(0.0f, 0.0f, 0.0f);

    /**
     * The scale vector of the game object.
     */
    public Vector3f scale = new Vector3f(1.0f, 1.0f, 1.0f);

    /**
     * Returns the transformation matrix of the game object.
     *
     * @return The transformation matrix of the game object.
     */
    public Matrix4f getTransform() {
        return new Matrix4f().translate(translation).scale(scale);
    }

}