# Design Document


This document in intended to be a regularly updated document listing the process of development
of the `kaapi` project.

First off, let us begin with a brief discussion of the goals of this project, and then start filling out the prerequisites (as well as the required knowledge) to begin creating this project.



## Prolegomena


The intention of this project is to create a package cum build manager for Java in the same way
the [Cargo](https://github.com/rust-lang/cargo) tool is the *de facto* build and package manager for Rust.

[Maven](https://maven.apache.org/index.html), [Gradle](https://gradle.org/), and even [Ant](https://ant.apache.org/) are extremely capable dependency and build managers, but they have their own problems - Maven is too complex, Gradle is just all over the place, and Ant is simply outdate. The intention of this project is not to replace these tools (not by any stretch of the imagination). It is simply a modest experiment in seeing if the approach taken by the `cargo` tool can be 
achieved in Java.

The other goal of this project is to be as self-contained as possible. This implies that most (or more likely, all) of the code will have no external dependencies aside from the obvious dependency on the JDK. This may not be the most efficient or quickest way of getting this project going, but there are a couple of points in its favour:
  
  * Edification - the main aim of this project is to learn, explore, and implement.
  
  * Self-contained - no external dependencies implies a fully self-contained system.


Considering that the Maven repository is the biggest repository of Java artifacts in the world, my 
initial idea is to pull in dependencies from the Maven repository till such time as a repository can exist (if ever) for `kaapi`, much like what `crates.io` does for Rust.


### Prerequisite Knowledge

Some subjects that will be required to implement this project are, off the top of my head, as follows. This list will expand and grow (and the VCS history should reflect that more accurately) as my own understanding of the problem grows, and the implementation proceeds. The main thing is to ensure that the design is robust enough to be able to handle moderately flexible variations in approaches:

  * A thorough knowledge of Maven and Cargo internals is absolutely mandatory. 
    Maven for two reasons:
      * to pull in artifacts from the Maven repository, the conventions followed for artifact
        metadata will be needed, and

      * a deep understanding of the Maven lifecycle management as well as the conventions followed
        in terms of the project layout will be handy for a possible migration tool for existing
        Maven projects.
      
      * need to check licensing and legal angles to ensure that there is no violation. 
        
     For Cargo, a deep understanding of its architecture, design, and implementation will come in
     rather useful for implementing `kaapi`. This does **not** entail re-writing the Cargo codebase
     in Java. It simply means potentially re-using some good designs from Cargo, at the very least.
     
  * I do not intend to use any build manager for this project. Rather, I will be writing custom
    build scripts for this project so that I can control the whole process. This entails:
        * deep knowledge of shell scripting (`sh` should be sufficient for this, not `bash`).
        * deep knowledge of Windows batch scripting.
        * multi-platform handling of environment variables, installation paths, and other 
          platform-specific details (at least for the three major platforms - Windows, Linux, 
          and macOS).
          
  * In the vein of Cargo, I plan to use [TOML](https://github.com/toml-lang/toml) as the configuration language of choice. I need to understand this format so that I can implement a parser for at least a *strict subset* of TOML.
  
  * I plan to use the TOML configuration to retrieve Maven repositories and store them in the local
  cache, and so that will require fast, efficient and robust networking and retrying capabilities. 
  Downloading arttifacts in parallel would also be highly desirable, but complex enough that this
  needs more deliberation. 
  
  * Sufficient knowledge of user-level permissions on Windows, Linux, and macOS for creating 
  `kaapi` specific configuration files, directories, as well as caches. 
  
  * In order to check if the project needs rebuilding, a very efficient diffing algorithm will come
    in handy. Preliminary tests suggest that a simple linear scan of the files (with a buffer size
    of around 8K) is fast, but when a project runs into tens of thousands of files and millions of
    LOC, then this approach will most likely fail miserably. I need to explore how `make` and other
    such tools detect changes instantly. Maybe this is also a good candidate for parallelism.
    
  * This is a minor point, but command-line options parsing needs to be robust and efficient. Implementing a custom parser for this seems to be in the offing.
  
  * Integrity of the data is of paramout importance - the tool may fail, but under no circumstances should it ever corrupt or lose data. This needs to be explored in depth.
  
  * Good documentation is absolutely necessary. Javadoc may be the way to go here, but I am also
  not averse to adopting a `rustdoc` style documentation support. IMPORTANT: check with the Rust team if their OS licence allows for it.





