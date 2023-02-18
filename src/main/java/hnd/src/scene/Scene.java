package hnd.src.scene;

import hnd.src.renderer.EditorCamera;
import hnd.src.renderer.Renderer2D;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Scene {

    private final Vector3f transform = new Vector3f(1, 1, 0);
    private final Vector4f color = new Vector4f(0.8f, 0.8f, 0.8f, 1.0f);

    public void onUpdateEditor(EditorCamera camera) {
        renderScene(camera);
    }

    private void renderScene(EditorCamera camera) {
        Renderer2D.beginScene(camera);
        Renderer2D.drawQuad(transform, color);
        Renderer2D.endScene();
    }
}