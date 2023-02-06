package hnd.src.core;


import hnd.src.platform.windows.WindowsPlatformUtils;

/**
 * Class that holds global timer for the application logic
 */
public class Timestep {

	private static final Timestep instance = new Timestep();
	private float time;

	private Timestep() {

	}

	public static Timestep getInstance() {
		return instance;
	}


	public static float getTime() {
		return WindowsPlatformUtils.getTime();
	}
}
