package hnd.src.scene.components;

import hnd.src.renderer.Texture2D;
import org.joml.Vector4f;

/**
 * Component that handles rendering of a sprite with color and texture.
 */
public class SpriteRendererComponent extends Component {
    /**
     * Color of the sprite.
     */
    public Vector4f color = new Vector4f(1.0f);

    /**
     * Texture of the sprite.
     */
    public Texture2D texture;

    /**
     * Tiling factor for the texture.
     */
    public float tilingFactor = 1.0f;
}
