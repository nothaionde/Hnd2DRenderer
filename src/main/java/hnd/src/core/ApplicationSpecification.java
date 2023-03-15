package hnd.src.core;

/**
 * This class represents an application specification, which contains information about an application
 * such as its name, working directory, and command line arguments.
 */
public class ApplicationSpecification {
    /**
     * The name of the application. Default value is "Hnd Application".
     */
    public String name = "Hnd Application";
    /**
     * The working directory of the application.
     */
    public String workingDirectory;
    /**
     * The command line arguments to be passed to the application.
     */
    public String[] commandLineArgs;
}
