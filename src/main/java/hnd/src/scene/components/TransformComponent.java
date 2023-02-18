package hnd.src.scene.components;

import org.joml.*;

public class TransformComponent {
    public Vector3f translation = new Vector3f(0.0f, 0.0f, 0.0f);
    public Vector3f rotation = new Vector3f(0.0f, 0.0f, 0.0f);
    public Vector3f scale = new Vector3f(1.0f, 1.0f, 0.0f);

    public Matrix4f getTransform() {
        rotation = new Quaternionf().getEulerAnglesXYZ(this.rotation);
        return new Matrix4f().translate(translation)
                .rotationXYZ(rotation.x, rotation.y, rotation.z)
                .mul(new Matrix4f().scale(scale));
    }
}
