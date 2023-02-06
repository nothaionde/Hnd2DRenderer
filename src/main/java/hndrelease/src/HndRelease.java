package hndrelease.src;

import hnd.src.core.Application;
import hnd.src.core.ApplicationSpecification;

public class HndRelease extends Application {

	public HndRelease(String[] args) {
		super(args);
		pushLayer(new ReleaseLayer());
	}

	public static Application createApplication(String[] args) {
		ApplicationSpecification spec = new ApplicationSpecification();
		spec.name = "Hnd Editor";
		spec.commandLineArgs = args;
		return new HndRelease(args);
	}
}
