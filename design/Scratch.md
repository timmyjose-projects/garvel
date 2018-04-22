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
  
  Overall strategy:
  ****************
  
  Different types of repositories - Maven Central, Spring, Jboss, etc. Support Maven Central to begin with.
  
    * Download the entries from the home page (http://central.maven.org/maven2/).
    * Analyse the metadata file to get the list of versions and artifact names.
    * Walk the directories to retrieve the POM files. = > Need a good extensible and loosely-coupled
      system to handle dependency management.
    * For each POM file, retrieve the dependencies and add them to the list.
    * Do minimal analysis on the dependency graph.
    * Downloiad the dependencies and cache them in the .garvel/registry directory.
    * This cache will be updated either when the `garvel update` command is invoked, 
      or a dependency in the current parsed state of the `Garvel.gl` file is not found 
      in the current cache => with the re-introduction of the `Garvel.lock` file, it's 
      as trivial as checking if the current version for a dependency differs from that
      in the lock file.
    