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

/**
 * A Scene object represents a collection of entities in a scene.
 * The scene contains an entity map that stores all entities in the scene.
 * It also contains methods to create, destroy, and render entities.
 */
public class Scene {
    /**
     * A map that stores all entities in the scene with a UUID key and an Entity value.
     */
    public final Map<UUID, Entity> entityMap = new HashMap<>();

    /**
     * Constructor for a Scene object.
     * Creates two entities with SpriteRendererComponent objects, one with a texture and one with a color.
     * The texture entity is named "Texture" and the color entity is named "Color".
     */
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

    /**
     * Method to update the scene for the editor camera.
     * Renders the scene.
     *
     * @param camera The editor camera object used to render the scene.
     */
    public void onUpdateEditor(EditorCamera camera) {
        renderScene(camera);
    }

    /**
     * Method to render the scene.
     * Begins the scene using the Renderer2D class and draws all entities in the entity map with a
     * TransformComponent and a SpriteRendererComponent.
     * Ends the scene using the Renderer2D class.
     *
     * @param camera The camera object used to render the scene.
     */
    private void renderScene(EditorCamera camera) {
        Renderer2D.beginScene(camera);
        // Draw Sprites
        entityMap.forEach((uuid, entity) -> {
            if (entity.hasComponent(TransformComponent.class) && entity.hasComponent(SpriteRendererComponent.class)) {
                Renderer2D.drawSprite(entity.getComponent(TransformComponent.class).getTransform(), entity.getComponent(SpriteRendererComponent.class));
            }
        });
        Renderer2D.endScene();
    }

    /**
     * Method to create a new entity with a given name.
     * Creates a new entity with a UUID key, a TransformComponent, and a TagComponent with the given name.
     * Adds the entity to the entity map and returns it.
     *
     * @param name The name of the entity.
     * @return The newly created entity.
     */
    public Entity createEntity(String name) {
        return createEntityWithUUID(new UUID(), name);
    }

    /**
     * Creates a new entity with the specified UUID and name.
     *
     * @param uuid The UUID to assign to the new entity.
     * @param name The name to give the new entity.
     * @return The newly created entity.
     */
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

    /**
     * Removes an entity from the scene.
     *
     * @param entity The entity to remove from the scene.
     */
    public void destroyEntity(Entity entity) {
        entityMap.remove(entity.getUUID());
    }
}