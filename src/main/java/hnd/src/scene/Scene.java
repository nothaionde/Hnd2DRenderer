package hnd.src.scene;

import hnd.src.core.UUID;
import hnd.src.renderer.EditorCamera;
import hnd.src.renderer.Renderer2D;
import hnd.src.renderer.Texture2D;
import hnd.src.scene.components.IDComponent;
import hnd.src.scene.components.SpriteRendererComponent;
import hnd.src.scene.components.TagComponent;
import hnd.src.scene.components.TransformComponent;
import org.joml.Vector4f;

import java.util.HashMap;
import java.util.Map;

public class Scene {
    public final Map<UUID, Entity> entityMap = new HashMap<>();

    public Scene() {
        Entity texture = createEntity("Texture");
        Entity color = createEntity("Color");
        SpriteRendererComponent colorComponent = new SpriteRendererComponent();
        SpriteRendererComponent textureComponent = new SpriteRendererComponent();
        color.addComponent(colorComponent);
        colorComponent.color = new Vector4f(0.5f, 0.5f, 0.2f, 1.0f);
        texture.addComponent(textureComponent);
        texture.getComponent(SpriteRendererComponent.class).texture = Texture2D.create("assets/textures/PixelBob.png");
        texture.getComponent(TransformComponent.class).translation.x = -0.5f;
        texture.getComponent(TransformComponent.class).translation.y = -0.5f;
    }

    public void onUpdateEditor(EditorCamera camera) {
        renderScene(camera);
    }

    private void renderScene(EditorCamera camera) {
        Renderer2D.beginScene(camera);
        // Draw Sprites
        entityMap.forEach((uuid, entity) -> {
            if (entity.hasComponent(TransformComponent.class) && entity.hasComponent(SpriteRendererComponent.class)) {
                Renderer2D.drawSprite(entity.getComponent(TransformComponent.class).getTransform(),
                        entity.getComponent(SpriteRendererComponent.class));
            }
        });
        Renderer2D.endScene();
    }

    public Entity createEntity(String name) {
        return createEntityWithUUID(new UUID(), name);
    }

    private Entity createEntityWithUUID(UUID uuid, String name) {
        Entity entity = new Entity();
        entity.addComponent(new IDComponent(uuid));
        entity.addComponent(new TransformComponent());
        TagComponent tag = new TagComponent();
        tag.tag = name.isEmpty() ? "New Entity" : name;
        entity.addComponent(tag);
        entityMap.put(uuid, entity);

        return entity;
    }

    public void destroyEntity(Entity entity) {
        entityMap.remove(entity.getUUID());
    }
}