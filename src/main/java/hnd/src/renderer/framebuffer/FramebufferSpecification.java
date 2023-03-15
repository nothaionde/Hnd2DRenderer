package hnd.src.renderer.framebuffer;

/**
 * A class that represents the specifications for a framebuffer.
 */
public class FramebufferSpecification {

    /**
     * The width of the framebuffer.
     */
    public int width;

    /**
     * The height of the framebuffer.
     */
    public int height;

    /**
     * The number of samples to use for the framebuffer.
     */
    public int samples;

    /**
     * Indicates whether the framebuffer is intended to be used as a swapchain target.
     */
    public boolean swapChainTarget = false;
}