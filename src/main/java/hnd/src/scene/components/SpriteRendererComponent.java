package hnd.src.scene.components;

import hnd.src.renderer.Texture2D;
import org.joml.Vector4d;
import org.joml.Vector4f;

public class SpriteRendererComponent extends Component {
    public Vector4f color = new Vector4f(1.0f);
    public Texture2D texture;
    public float tilingFactor = 1.0f;
}
