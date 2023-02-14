package test;

import hnd.src.core.Application;
import hnd.src.core.ApplicationSpecification;

public class TestApp extends Application {
	public TestApp(String[] args) {
		super(args);
		pushLayer(new TestLayer());
	}

	public static Application createApplication(String[] args) {
		ApplicationSpecification spec = new ApplicationSpecification();
		spec.name = "Test app";
		spec.commandLineArgs = args;
		return new TestApp(args);
	}
}
