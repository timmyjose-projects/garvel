### Scratch Pad

This will serve as a scratchpad for items that do not fit clearly in any other document


#### Features for v1.0

  * Support for `help`, `list`, `version`, new`, `clean`, `build`, `run` for basic projects.
  * CLI support for the above features.
  * Implement a strict semantic versioning grammar.
  * Linux/macOS only support.
  
  
#### TODO

 * Update `categories` in `Garvel.gl` file to match against a fixed set of categories.
 * semver support for tilde and caret semantics.
 * extended semver support for wildcard semantics.
 * extend support for specifying local dependencies as well as git/github repos as dependencies.
 * ~~implement proper error handling in the CLI module~~.
 * ~~list command~~.
 * ~~help command~~.
 * ~~version command~~.
 * ~~new command~~.
 * ~~build command~~.
 * test command.
 * run commmand.
 * ~~clean command~~.
 * bench command.
 * Create and store SSL certificate for downloading external artifacts.
 * Update build scripts to handle proper targets - build, clean, test, deploy (generate JAR). 
 * Replace JSON parser with a strict TOML-like configuration file and parser (?)
 * For v1.0, simply use the `Garvel.gl` file as a marker for dependencies, and have a lean JAR for
   the project. In later versions, linking a fat JAR (static linking) should be possible using a
   custom classloader (or any other suitable mechanism). 
   
   
### Dependency management

 * `garvel setup` command - create directories, fetch and create registry.
 * `garvel update` command - fetch latest metadata from maven central and update registry.
 * `garvel dependency` command - display valid versions for given dependency and also its dependencies - useful 
    for writing the Garvel.gl file's dependencies section.
 * Maven POM parser.    
 * Migrate tool for Maven projects using the POM Parser to generate the corresponding Garvel.gl file.
 * Progress Monitor using total file size and currently downloaded bytes.
 * `Garvel.lock` file is now required to maintain the current state of the project's dependencies.
 
 * Logging becomes increasingly important. Configure the base Java logger. (lower priority).
 * ~~Improve error-handling by using custom methods to chain causes for proper display.~~
 
  
  Overall strategy:
  ****************
  
  Different types of repositories - Maven Central, Spring, Jboss, etc. Support Maven Central to begin with.
  
    * Download the entries from the home page (http://central.maven.org/maven2/).
    * Analyse the metadata file to get the list of versions and artifact names.
    * Walk the directories to retrieve the POM files. = > Need a good extensible and loosely-coupled
      system to handle dependency management.
    * For each POM file, retrieve the dependencies and add them to the list.
    * Do minimal analysis on the dependency graph.
    * Download the dependencies and cache them in the .garvel/registry directory.
    * This cache will be updated either when the `garvel update` command is invoked, 
      or a dependency in the current parsed state of the `Garvel.gl` file is not found 
      in the current cache => with the re-introduction of the `Garvel.lock` file, it's 
      as trivial as checking if the current version for a dependency differs from that
      in the lock file.
    * Handle the above tasks using parallelism for improved performance, especially downloading
      the dependencies as well as populating the registry.
    * Use MD5 and/or SHA1 checksum for integrity verification.
    * Cargo-style dependencies range semantics.
    * Have a new command, `purge` to delete the local repository cache without uninstalling Garvel.    
    
    
   #### Install layout (one-time operation)
    
    ```
    $HOME/.garvel
    $HOME/.garvel/registry
    $HOME/.garvel/cache
    ```
    
   #### New layout
    
    ```
    $PROJECT_ROOT/Garvel.gl
    $PROJECT_ROOT/src
    $PROJECT_ROOT/tests
    $PROJECT_ROOT/logs
    $PROJECT_ROOT/target (generated)
    $PROJECT_ROOT/target/build (generated)
    ```
    
   #### Dependency Manager design scratchings
   
   How does maven resolve dependencies? 
    
    * Use `Externalizable` to write dependency data in the project's folder, as well as store cache and registry data
    in $HOME/.garvel.
    
    * https://blog.packagecloud.io/eng/2017/03/09/how-does-a-maven-repository-work/ lists the way in which Maven
    constructs URLs to resolve dependencies for primary and secondary artifacts.
    
    *UPDATE* - download the entire Maven Central repository metadata takes too much time (far too many artifacts), and 
    will also take far too much space. Instead, here is the scope of the commands as of today:
    
    `Maven coordinates` refers to the (groupId, artifactId, version) tuple.
    
    * Install - Create the $HOME/.garvel and $HOME/.garvel/cache directories. The cache will contain the repositories
    that have been used by all Garvel projects. [DONE]
    
    * Uninstall - Delete $HOME/.garvel recursively. [DONE]
    
    * New - Create the project skeleton containg the following directories and files - Garvel.gl, src, tests. [DONE]
    
    * Build - 1). Create the `target` directory with `build` and `deps` directories. `build` will hold the build
              artifacts (class files basically), and the `deps` directory will hold the dependency graph of the current 
              project in binary form.The Dependency Graph will not only help generate the JAR file more quickly, but 
              also help in incremental builds where only the changed source files will be compiled (based on checking
              the timestamp of the class files against the source file).
              
              2). Parse the Garvel.gl file and populate the Core Cache. If there are any changes (new artifact, 
              changed version of existing artifact), then contact Maven Central and then update the Garvel cache at 
              $HOME/.garvel/cache, as well as update the Dependency Graph of the project itself. The downloaded artifacts
              will be checksummed using both MD5 and SHA1. For now, only HTTP support will be provided, which should be
              extended to HTTPS support in later versions. 
              
              3). Compile the project and create the deliverable JAR file, ${PROJECT_NAME}.jar in the `target` directory.
              If there are any errors, report them to the user and exit immediately.
              
              Note: Since the idea is to bootstrap Garvel to use Garvel itself, special handling must be done for the
              `Garvel` project name to generate the the wrapper scripts for `garvel`.jar as well. Whether to restrict
              new project names with the same name will depend on testing and verification.
                
    * Clean - Will simply delete the entire `target` folder. [DONE]
    
    * Run - Will invoke `build` to get the latest state of the project, and check for the target name in the Core Cache,
    reporting any errors if the target is not found. If the target name is not specified, and if the `Main-Class` attribute
    has been supplied in the `Lib` section of `Garvel.gl`, then an attempt will be made to run the JAR file itself.
    
    * Update - Deprecated. Since `install` does not download the registry information, there is no need for this 
    command. [DONE]
    
    * Dep - 1. Given the Maven Coordinates, display the available versions. To this end, the local cache will be queried
    first, and if not found, then Maven Central will be contacted for ths information. [DONE]
    
            2. If the `--show-dependencies` flag is supplied, then the transitive dependencies of the artifact will
            also be displayed in a suitably formatted manner. 
    
    * List - List all the installed commands. [DONE]
    
    * Version - List the current version of Garvel (core); [DONE]
    
    * Help - Given a subcommand, list the help pages for the command. [DONE}
    
    * Test - @TODO.
    
    
    *Format of the Dependency Graph*:
    
    Have a basic structure representing an artifact:
    
    Artifact {
      groupId,
      artifactId,
      version
    }
    
    An external integer, `id`, will be used as the id for Graph operations. The dependency structure will be created by the
    Graph logic, and there will be a mapping from the ids to the actual Artifact objects. Custom operations wil be
    provided via the callback mechanism.
    
    Start off with the specified artifact, and then transitively keep collecting the lists of dependencies for this
    artifact, creating a Dependency Graph (Directed Graph) along the way. Finally, perform a Topological Sort on the
    dependencies. If there is no cycle detected, then download the dependencies in parallel, and update the Garvel
    Cache, and store the dependency graph. The Topological Sort result will be used to populate the CLASSPATH value for the
    project.
    
    On the other hand, if there is a cycle, then report an error and exit immediately, suitably informing the user.
    
    The second issue that may arise is that X -> (depends on) Y, and Y -> Z1, but X -> Z2. So which version of Z to use?
    If we download both, there may be issues when loading the classes. How to resolve this? For now, simply error out 
    during compilation, if at all, by downloading the first version found in the dependency graph (Maven also follows 
    a similar strategy).  
    
    Use the Strategy pattern to allow adding more non-trivial algorithms in future versions (like say, allowing cyclic
    dependencies where applicable).
    
    Note: In this regards, Maven-style "Depdendency Scopes" (test, compiler, build etc.) will not be supported for 1.0.
    
    *Format of the Garvel Cache*:
    
     Artifacts stored in the following format:
     
     groupId /
       - artifactId /
          - version 1/
          - version 2/
          ...
          - version n/
     
     And a map containing mapping from groupId+artifactId+version to path (as String since `Path` is not serializable)
     in the cache. This is in binary form for performance. Whenever the cache is updated, this map will also be updated.
    
    Algorithm for Dependency Graph:
    
    1. The Dependency Graph will store the metadata of the dependencies
    of the project. The actual data for the artifacts themselves will be stored in the Garvel Cache (GC).
    
    Note: For the dependency graph, the only `id` used is a monotonously increasing integer value. There will be a mapping
    from the Maven coordinates to the integer value that is persisted so that steps 2 and 3 can be undertaken. 
    
    2. When a build is triggered, the Garvel.lock file will be queried for. If it does not exist, this is the first build
    for the project - parse the Garvel.gl file and get the list of all the current dependencies. Create a new Dependency Graph (DG) 
    and make these dependencies as the initial vertices. Then find all the transitive dependencies of all the dependencies, and populate the graph.
     
    Perform Topological Sort (TS) to find out the ordering of all the dependencies. Report any cyclic dependencies as errors (for 1.0).
    Then download all the dependencies in the Topological Order, and persist the Dependency Graph in the `target/deps` directory.
    Finally, clone the Garvel.gl file as the Garvel.lock file (removing the old one).
    
    3. i). Now, if a Garvel.lock file already existed, then parse both Garvel.lock and Garvel.gl, and compare the dependencies.
    If new dependencies were added, current dependencies removed, or current dependencies' versions changed, then delete the
    Garvel.lock file, update the DG, and perform TS, and continue as in 2. Finally, persist the DG, and clone the Garvel.gl
    file into the new Garvel.lock file. 
    
       ii). If no changes were found between the Garvel.gl and Garvel.lock file, then simply use the DG, if it exists,
       perform a TS and get the classpath string to build the project with. If the DG is not present, then create it, 
       perform TS for the build, and then persist the DG. Note that since the Garvel.lock file is present, this means
       that the artifacts are already in the GC. However, if `garvel uninstall` had been called previously, then this
       would fail. Therefore, even in the presence of the Garvel.lock file, we should query the GC to ensure that the
       artifacts are present (and if not, download them).
       

    How the Garvel Cache works?
    
    The Garvel Cache structure is shown above, but in order to handle queries about available artifacts, a Mapping file
    will also be persisted in the GC root directory. If this file is not present, then all the dependencies will have
    to be fetched again (for example, as a result of `garvel uninstall`).
    
    IMPORTANT: Mapping between semver and Maven naming schemes needs to be taken care of in future versions. Right now,
    the scheme is to use major + minor + [path] + [prerelease | build | prerelease + build] and then let any errors be
    handled when actually fetching the artifact from the repository. 
    
    
    How compilation works?
    
    Incremental compilation will not be suppported for 1.0 - it's basically all or nothing for now. To this end, a new
    abstraction layer will be introduced similar to the Dependency Manager framework - basic compilation strategy, which
    can be switch over to a more advanced compilation scheem in later versions.
    
    Simple Strategy:
    
    This is the strategy that will be followed for 1.0. In this case, the entire project source tree is is built together
    into the target/build directory, the project artifact(s) generated, and then the build directory is deleted. 
    
    Advanced Strategy:
    
    This can have many more varieties, but I am thinking, at the very least, some intelligent incremental compilation
    schemes to speed up compilation.
    
    Post 1.0, all issues and change requests will be logged on, and monitored through, Github.
    
    
    
   ### Future Feature List (post 1.0)
   
   Priority legend: L0 (must have), L1(have, if possible), L2(nice to have), L3(low priority).
   
   * Increase more fault-tolerant and robust POM file handling (such as resolving variables such as ${project.version}) -
   maybe develop a full module for this, would also help in creating a migration tool for Maven projects.
   
   * Https Support. (L0)
   
   * Support `javadoc` and Cargo-like documentation using a common interface. (L0)
   
   * Support System Comppiler. (L0)
   
   * Support Fat JAR creation. (L0)
   
   * Create a generic test interface to support common test platforms as well as Cargo-like test framework. (L0)
   
   * Full support for JDK modules (currently it works only on pre-Java 9 style Java projects - 
     basically projects without modules); (L1)
   
   * Support for proper incremental compilation. (L1)
   
   * Support for dependency graph update (instead of creating and replacing as is done for now); (L1)
   
   * Support `garvel run` with all possible combinations of classes with `main` methods (private, nested, static, etc.).
     Also, remove the explicit dependency on build. If there is a jar file in the `target` directory, that should be sufficient
     to run the target. (L0) [DONE]
   
   * Include another command, `garvel show <config-name>` to show the loaded values of configuration keys. This would be 
     useful for something like `garvel show targets` to list all targets for the project. (L2)  
   
   * Instead of copying the template file (Garvel.gl) into the new project root, customise the `name` field with the newly
     created project's name and create the configuration file. (L2)
     
   * Add checksum checking for downloaded artifacts. (L0)
   
   * Add query support for local dependency graph and local cache for `dep` command. (L1)
   
   
    