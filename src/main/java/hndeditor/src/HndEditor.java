package hndeditor.src;


import hnd.src.core.Application;
import hnd.src.core.ApplicationSpecification;

/**
 * HndEditor class represents an application for editing Hnd files.
 * This class extends the Application class and adds a new EditorLayer as a layer.
 */
public class HndEditor extends Application {

    /**
     * Creates a new HndEditor instance.
     *
     * @param args Command line arguments for the application.
     */
    public HndEditor(String[] args) {
        super(args);
        pushLayer(new EditorLayer());
    }

    /**
     * Creates a new Application instance with the provided arguments.
     *
     * @param args Command line arguments for the application.
     * @return A new HndEditor instance.
     */
    public static Application createApplication(String[] args) {
        ApplicationSpecification spec = new ApplicationSpecification();
        spec.name = "Hnd Editor";
        spec.commandLineArgs = args;
        return new HndEditor(args);
    }
}