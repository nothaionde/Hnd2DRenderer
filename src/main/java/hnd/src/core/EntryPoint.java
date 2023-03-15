package hnd.src.core;

/**
 * The entry point of the application.
 */
public class EntryPoint {

    /**
     * The main method that is called when the application is launched.
     *
     * @param args the command line arguments that were passed to the application
     */
    public static void main(String[] args) {
        Application application = Application.createApplication(args);
        application.run();
    }
}
