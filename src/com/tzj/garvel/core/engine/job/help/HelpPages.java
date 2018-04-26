package com.tzj.garvel.core.engine.job.help;

public class HelpPages {
    public static final String helpCommand = "garvel-help\n" +
            "Prints detailed description of the given subcommand\n" +
            "\n" +
            "USAGE:\n" +
            "    garvel help subcommand\n" +
            "\n" +
            "ARGS:\n" +
            "    <subcommand>...   The subcommand whose help message to display\n\n";

    public static final String versionComand = "garvel-version\n" +
            "Shows the current version of Garvel\n" +
            "\n" +
            "USAGE:\n" +
            "    garvel version\n" +
            "\n" +
            "ARGS:\n" +
            "    None\n\n";

    public static final String listCommand = "garvel-list\n" +
            "Lists all the available commands\n" +
            "\n" +
            "USAGE:\n" +
            "    garvel list\n" +
            "\n" +
            "ARGS:\n" +
            "    None\n\n";

    public static final String installCommand = "garvel-install\n" +
            "Installs the Garvel manager for this system. It creates the .garvel metadata directory inside $HOME or %%USERPROFILE%%.\n" +
            "\n" +
            "USAGE:\n" +
            "    garvel install\n" +
            "\n" +
            "ARGS:\n" +
            "    None\n\n";

    public static final String uninstallCommand = "garvel-uninstall\n" +
            "Uninstalls the Garvel manager for this system. It removes the .garvel metadata directory inside $HOME or %%USERPROFILE%%.\n" +
            "\n" +
            "USAGE:\n" +
            "    garvel uninstall\n" +
            "\n" +
            "ARGS:\n" +
            "    None\n\n";


    public static final String newCommand = "garvel-new\n" +
            "Creates a new project in the current directory. If the `garvel install` command was not executed prior this this command,\n" +
            "it also invokes the `garvel install` command to perform the initial Garvel setup before creating the project.\n" +
            "\n" +
            "USAGE:\n" +
            "    garvel new [--vcs <vcs-name>] PATH\n" +
            "\n" +
            "ARGS:\n" +
            "    [--vcs <vcs-name> ] initialises this project with the given VCS. Valid values for `vcs-name` are:\n" +
            "                        git, fossil, mercurial, svn, cvs, and none (default).\n" +
            "    PATH                the name of the new project. Valid identifiers for the platform will be accepted.\n\n";

    public static final String buildCommand = "garvel-build\n" +
            "Builds the current project. The project artifact will be deployed in the `target` directory under project root, either as a \n" +
            "single JAR file (default) containing the project's contents, or if the `--fat-jar` key has been specified in the `Garvel.gl` file, as a\n" +
            "single fat JAR file with all the dependencies bundled in.\n\n" +
            "In addition, if the `lib` section is present, then the mandatory `main-class` key will ensure that the built JAR file is a runnable JAR file\n" +
            " with the `Main-Class` JAR attribute set to the supplied value for `main-class`\n" +
            "\n" +
            "USAGE:\n" +
            "    garvel build\n" +
            "\n" +
            "ARGS:\n" +
            "    None\n\n";

    public static final String cleanCommand = "garvel-clean\n" +
            "Cleans the current project. Specifically, it deletes the `target` directory.\n" +
            "\n" +
            "USAGE:\n" +
            "    garvel clean\n" +
            "\n" +
            "ARGS:\n" +
            "    None\n\n";

    public static final String runCommand = "garvel-run\n" +
            "Run the specified target (as specified in the `bin` section of the `Garvel.gl` file).\n" +
            "If no target-name is specified, and if the project is runnable i.e., the `main-class` key has been\n" +
            "specified in the `lib` section of the `Garvel.gl` file, then the project itself will be run, otherwise\n" +
            "an error message will be issued.\n" +
            "\n" +
            "USAGE:\n" +
            "    garvel run [target-name]\n" +
            "\n" +
            "ARGS:\n" +
            "    target-name the target name (as specified in the `bin` section of the `Garvel.gl` file). Optional.\n\n";

    public static final String depCommand = "garvel-dep\n" +
            "For the given ARTIFACT, lists all the currently available versions, and if the `--show-dependencies` option is\n" +
            "supplied, then lists all the artifacts this artifact depends upon.\n" +
            "\n" +
            "USAGE:\n" +
            "    garvel dep [-s | --show-dependencies ] ARTIFACT\n" +
            "\n" +
            "ARGS:\n" +
            "    [-s | --show-dependencies] display the dependencies for this artifact.\n" +
            "    ARTIFACT                   the  artifact (project dependency) whose metadata we are interested in.\n\n";
}
