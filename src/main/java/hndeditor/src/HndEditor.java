package hndeditor.src;


import hnd.src.core.Application;
import hnd.src.core.ApplicationSpecification;

public class HndEditor extends Application {


	public HndEditor(String[] args) {
		super(args);
		pushLayer(new EditorLayer());
	}

	public static Application createApplication(String[] args) {
		ApplicationSpecification spec = new ApplicationSpecification();
		spec.name = "Hnd Editor";
		spec.commandLineArgs = args;
		return new HndEditor(args);
	}
}