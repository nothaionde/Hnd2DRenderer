package hnd.src.core;


import hnd.src.platform.windows.WindowsPlatformUtils;

/**
 * A class representing a timestep.
 */
public class Timestep {

    private static final Timestep instance = new Timestep();
    private float time;

    /**
     * Private constructor for Timestep class to prevent instantiation from outside.
     */
    private Timestep() {
    }

    /**
     * Returns the instance of Timestep class.
     *
     * @return the instance of Timestep class
     */
    public static Timestep getInstance() {
        return instance;
    }

    /**
     * Returns the current time.
     *
     * @return the current time
     */
    public static float getTime() {
        return WindowsPlatformUtils.getTime();
    }
}