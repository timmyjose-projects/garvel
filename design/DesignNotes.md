# Design Document

This document in intended to be a regularly updated document listing the process of development
of the `garvel` project.

First off, let us begin with a brief discussion of the goals of this project, and then start filling out the prerequisites
(as well as the required knowledge) to begin creating this project.



## Prolegomena

The intention of this project is to create a package cum build manager for Java in the same way
the [Cargo](https://github.com/rust-lang/cargo) tool is the *de facto* build and package manager for Rust.

[Maven](https://maven.apache.org/index.html), [Gradle](https://gradle.org/), and even [Ant](https://ant.apache.org/) are
extremely capable dependency and build managers, but they have their own problems - Maven is too complex, Gradle is just
all over the place, and Ant is simply outdated. The intention of this project is not to replace these tools (not by any
stretch of the imagination). It is simply a modest experiment in seeing if the approach taken by the `cargo` tool can be
achieved in Java.

The other goal of this project is to be as self-contained as possible. This implies that most (or more likely, all) of
the code will have no external dependencies aside from the obvious dependency on the JDK. This may not be the most
efficient or quickest way of getting this project going, but there are a couple of points in its favour:
  
  * Edification - the main aim of this project is to learn, explore, and implement.
  
  * Self-contained - no external dependencies implies a fully self-contained system.


Considering that the Maven repository is the biggest repository of Java artifacts in the world, my 
initial idea is to pull in dependencies from the Maven repository till such time as a repository can exist (if ever) for
 `garvel`, much like what `crates.io` does for Rust.


### Prerequisite Knowledge

Some subjects that will be required to implement this project are, off the top of my head, as follows. This list will
expand and grow (and the VCS history should reflect that more accurately) as my own understanding of the problem grows,
and the implementation proceeds. The main thing is to ensure that the design is robust enough to be able to handle
moderately flexible variations in approaches:

  * A thorough knowledge of Maven and Cargo internals is absolutely mandatory. 
    Maven for two reasons:
      * to pull in artifacts from the Maven repository, the conventions followed for artifact
        metadata will be needed, and

      * a deep understanding of the Maven lifecycle management as well as the conventions followed
        in terms of the project layout will be handy for a possible migration tool for existing
        Maven projects.
      
      * need to check licensing and legal angles to ensure that there is no violation. 
        
     For Cargo, a deep understanding of its architecture, design, and implementation will come in
     rather useful for implementing `garvel`. This does **not** entail re-writing the Cargo codebase
     in Java. It simply means potentially re-using some good designs from Cargo, at the very least.
     
  * I do not intend to use any build manager for this project. Rather, I will be writing custom
    build scripts for this project so that I can control the whole process. This entails:
        * deep knowledge of shell scripting (`sh` should be sufficient for this, not `bash`).
        * deep knowledge of Windows batch scripting.
        * multi-platform handling of environment variables, installation paths, and other 
          platform-specific details (at least for the three major platforms - Windows, Linux, 
          and macOS).
          
  * I plan to use a restricted subset of [json](https://www.json.org/ as the configuration language of choice. 
    the `init` and `new` commands will create the skeleton of the `Garvel.gl` file in the project root directory. 
    The template will be fixed to begin with, but may be changed for more options later on. Java properties files 
    were considered, but expressing dependencies in a nested manner would be cumbersome, if not downright unusable.    
  
  * I plan to use the json configuration to retrieve Maven repositories and store them in the local
  cache, and so that will require fast, efficient and robust networking and retrying capabilities. 
  Downloading artifacts in parallel would also be highly desirable, but complex enough that this
  needs more deliberation. 
  
  * Sufficient knowledge of user-level permissions on Windows, Linux, and macOS for creating 
  `garvel` specific configuration files, directories, as well as caches. 
  
  * In order to check if the project needs rebuilding, a very efficient diffing algorithm will come
    in handy. Preliminary tests suggest that a simple linear scan of the files (with a buffer size
    of around 8K) is fast, but when a project runs into tens of thousands of files and millions of
    LOC, then this approach will most likely fail miserably. I need to explore how `make` and other
    such tools detect changes instantly. Maybe this is also a good candidate for parallelism.
    
  * This is a minor point, but command-line options parsing needs to be robust and efficient. Implementing a custom parser
    for this seems to be in the offing.
    
  * A major requirement is to thoroughly understand the implication of using `JavaCompiler` to compile source code (of the
    projects that is, not the `garvel` project itself); A fallback mechanism/option to using the system `javac` explicitly
    would be desirable.   

  * Another semi-minor point is to ensure that semantic versioning is properly handled (level of support can be increased
    over releases, in terms of the number of usecases handled).
  
  * Integrity of the data is of paramount importance - the tool may fail, but under no circumstances should it ever corrupt
    or lose data. This needs to be explored in depth.
  
  * Good documentation is absolutely necessary. Javadoc may be the way to go here, but I am also
    not averse to adopting a `rustdoc` style documentation support. IMPORTANT: check with the Rust team
    if their OS licence allows for it.

  * Follow the SPI model to expose the core functionality to the CLI. This would also allow seamless integration of a GUI
    component later on, if needed. The same model can be followed internally in other modules - need to explore this
    further. @TODO


## Tentative garvel interface

```
$ garvel
A Java package manager and dependency manager

Usage:
    garvel [OPTIONS] [COMMAND]

Options:
    -v, --verbose       Use verbose output 
    -q, --quiet         No output printed to stdout (default)

Note that you can specify either `--verbose` or `--quiet` but not both. 

Some common garvel commands are (see all commands with --list):
    help        Display this help and exit
    version     Display version info and exit
    list        List all the available commands
    build       Compile the current project
    clean       Remove the target directory
    new         Create a new garvel project
    run         Build and execute src/Main.java
    test        Run the tests

See 'garvel help <command>' for more information on a specific command.
```


## Project layout

```
$ garvel new foo

foo
├── Garvel.gl
└── src
└── tests

2 directory, 2 files
```

```
$ garvel new --bin bar

bar
├── Garvel.gl
└── src
└── tests

1 directory, 2 files
```

```
$ cat Garvel.gl
# Configuration file for a runnable project
# The keys are fixed. Customise the values.
# Empty keys are not allowed.

# project metadata
[project]

name = "HelloWorld",
version = "0.1.0",
classpath = [ "./" ], # apart from the dependencies
authors = [ "me@me.com" ],
description = "A simple Hello, world project!",
homepage = "http://example.com/hello_world",
readme = "https://example.com/hello_world/readme.html",
keywords = [ "basic", "hello world"],
categories = [ "standalone", "jar" ],
licence = "MIT",
licence-file =  "custom-licence-path"

# the external dependencies for this project
# only exact version supported for now,
# but different options can be provdided later
[dependencies]

junit = "4.1.2",
log4j = "1.2.17", 


# targets for `garvel run --bin`
# optional section, but required for `garvel run`
# "target_name" : "path" format
# if given, at least one target must
# be specified
[bin]

"one" = "/bin/one/Main",
"two" = ""/bin/two/Main",
"three" = "/bin/three/Client"
```


### Progress 

My intention is to write the basic functionality from scratch, bootstrap the project, and then
use external dependencies. This will also entail massive refactorings, but the overall design should
be more or less minimally impacted. 

Right now, I am in the exploratory phase where I am trying to get a good understanding of the whole idea. 
Once a less-than-nebulous overall view is available, then a checklist will be created to properly monitor
progress. Considering that major refactorings may take place, I am not too keen at this juncture to proceed
in a rigid manner. As my understanding of the problem increases, the design will be tweaked accordingly.
To this end, however, it is imperative to have a basic solid understanding of the foundational ideas, and 
then real work can proceed. This should be explicitly marked with an official sign-off.

Regarding minimum JDK support, after much consideration, I have decided to set JDK 7 as the minimum supported JDK.
Even if some market analyses show that JDK 8 is the most commonly used JDK today, it would still make sense to write
code that will still work with JDK 7. So that means features like lambdas and stream are off limits for the `garvel`
source code itself. This should lead to minimal reductions in productivity in any case. If needed, the code can easily
be updated to make use of JDK 8+ features later on, but this is not very likely.





