# Garvel - a Java Build and Dependency Manager

A dependency and build manager for Java, loosely inspired by the Cargo tool for Rust, that aims to provide a simplified and powerful way to manage complex projects.

This started out as an edificational project that would provide a more modern (and XML-less) way of managing a broad-range of Java projects - from simple projets to 
extremely complex projects. Garvel does not impose any hard restricstions on the layout of the project apart from the basic initial layout.

Post version 1.0, the idea is to improve the project not only in terms of performance, but also in terms of scope and robustness. Contributors are, of course,
very much welcome to help take this from an educational project into a robust system that may be used for real-world production systems.


## Getting Started

`garvel` is extremely easy to build and install. In fact, `garvel` can be built and deployed using an existing `garvel.jar` file. To generate the initial version
purely from code, though, a custom build script is provided. This build script will generate the `garvel.jar` file as well as a `garvel.sh` wrapper to run the
JAR file. 

Once the initial JAR file has been generated, there is no more actual dependence on the build script. As of version 1.0 though, the best way of delivering the
project artifacts has not been finalised.


### Prerequisites

You will need a working Java compiler (JDK version 7 or above). This project has no external dependencies (one of the side-effects of being an edificational
undertaking).

Also note that the available build script (as of version 1.0) works only on UNIX-like systems. A stub file is available for Windows, but that has to be filled
in. If anybody with experience writign Windows batch scripts wishes to help with that, that would be very welcome! 

However, as noted in the previous section, once the initial JAR (created by the build script) is available, then it should be possible to bootstrap the project
on Windows as well, and there will be no more dependency on the build script thereonin.

To actually work with the project code, any IDE of your choosing will suffice. There is no dependency on any specific VCS or IDE.


### Building and Installing

Building the project can be done in the following two ways. Note that the first step is currently **required** for the first generation of `garvel.jar`.

Using the `scripts/build.sh` script:

```
Macushla:garvel z0ltan$ ./scripts/build.sh
/Users/z0ltan/Code/Projects/garvel

[ Creating build directory ]

[ Created build directory ]

[ Compiling source files

.//com/tzj/garvel/core/net/connectors/HttpConnector.java
.//com/tzj/garvel/core/net/connectors/HttpsConnector.java

<files elided>

.//com/tzj/garvel/common/spi/core/command/CommandParams.java
.//com/tzj/garvel/common/spi/core/command/CommandType.java
.//com/tzj/garvel/common/spi/core/command/CommandResult.java
.//com/tzj/garvel/common/spi/core/CoreService.java
.//com/tzj/garvel/common/spi/core/CoreServiceLoader.java
.//com/tzj/garvel/common/spi/util/UtilService.java
.//com/tzj/garvel/common/spi/exception/GarvelCheckedException.java
.//com/tzj/garvel/common/spi/exception/GarvelUncheckedException.java

to /Users/z0ltan/Code/Projects/garvel/build ]

warning: [options] bootstrap class path not set in conjunction with -source 7
1 warning
[ Finished compiling source files ]

[ Creating project JAR garvel.jar ]

[ Created project JAR garvel.jar in /Users/z0ltan/Code/Projects/garvel/target ]

[ Creating wrapper script for garvel ]

[ Finished creating wrapper script  for garvel in /Users/z0ltan/Code/Projects/garvel/target ]

Add the following lines to your .bashrc or .bash_profile file:
    export PATH="/Users/z0ltan/Code/Projects/garvel/target/garvel.sh":$PATH
    alias garvel="/Users/z0ltan/Code/Projects/garvel/target/garvel.sh"

[ Deleting build directory ]

[ Finished deleting build directory. Build was successful. ]
```

As the script output mentions, a wrapper script for the project JAR file (`garvel.jar`) is automatically created in the `scripts` directory. Adding the following lines to your `~/.bashrc` or
`~/.bash_profile` configuration file is recommended for an easier experience, and especially if you plan to build the project using the build script:

```
    export PATH="<Garvel project path>/garvel/target/garvel.sh":$PATH
    alias garvel="<Garvel project path>/garvel/target/garvel.sh"
```

The second way is to use the garvel code itself to generate the JAR file for the project. Note that this approach requires an already existing `garvel.jar` file, and as such has a dependency on
the first approach. Assuming that the environment variables have been set up as shown above, run the following command from a terminal, inside the `garvel` project root:

```
Macushla:garvel z0ltan$ garvel build
Creating target directory hierarchy.... DONE
Populating Core Caches...DONE
Analysing Dependencies...DONE
Detected JDK version 11
Starting compilation of source files...
	Compiling project sources...DONE
	Generating project JAR...DONE
Building project artifacts.... DONE

Project built successfully

Macushla:garvel z0ltan$ tree target/
target/
├── deps
│   └── dependency.graph
├── garvel-1.0.0.jar
├── garvel.jar
└── garvel.sh

1 directory, 4 files
```

This uses the `Garvel.gl` configuration file of the `garvel` project itself to build itself, generating the `garvel-0.1.0.jar` file in the `target` directory. The project uses semantic versioning,
and `1.0.0` is the version mentioned in the `Garvel.gl` configuration file.

Note: if you have set up the environment variables as recommended, then with every build you will have to manually rename the JAR file from `garvel-1.0.0.jar` to `garvel.jar`. 
Alternatively, you can simply modify the wrapper script, `garvel.sh` as per your needs, or simply use a different method of invoking the `garvel` JAR file.


### Sample Run Demo

Assuming that the environment variables have been correctly set up (previous section), here are some demos showcasing 
the current state of `garvel`.

To see usage information on `garvel`, simply run `garvel`:

```
Macushla:testbed z0ltan$ garvel
A Java package manager and dependency manager

Usage:
	garvel [OPTIONS] [COMMAND]

Options:
	-v, --verbose      Use verbose output (default)
	-q, --quiet        No output printed to stdout

Note that you can specify either `--verbose` or `--quiet` but not both.

Some common garvel commands are (see all commands with --list):
	help        Display help for a specific command
	version     Display version information
	list        List all the available commands
	install     Setup Garvel
	uninstall   Remove Garvel setup
	new         Create a new Garvel project
	build       Compile the current project and generate artifacts
	clean       Remove the target directory
	run         Build and execute the specified target
	dep         Display the available versions and dependencies of the specified jar

See 'garvel help <command>' for more information on a specific command.


```

To see help for a specific command, you can run `garvel help <command-name>`. For example,

```
Macushla:testbed z0ltan$ garvel help dep
garvel-dep

For the given jar, lists all the currently available versions, and if the `--show-dependencies` option is
supplied, then lists all the artifacts the specific version of the jar depends upon.

USAGE:
    garvel dep [(-s | --show-dependencies) <version> ] ARTIFACT

ARGS:
    [-s | --show-dependencies <version> ] display the dependencies for this jar version.
    ARTIFACT                             the  jar (in `groupId/artifactId` format) whose metadata we are interested in.
```


#### Installing garvel

The term "install" is a bit misleading here. The main purpose of installation vis-a-vis `garvel` is to create the
metadata for `garvel` itself, so that it can have a cache of downloaded dependencies for faster builds. In short,
this step will create the `garvel` project metadata structure under `$HOME` or `%USERPROFILE%`.

Let's go ahead and install `garvel` using the `garvel install` command:

```
Macushla:testbed z0ltan$ tree ~/.garvel
/Users/z0ltan/.garvel [error opening dir]

0 directories, 0 files

Macushla:testbed z0ltan$ garvel install
Installed Garvel successfully.

Macushla:testbed z0ltan$ tree ~/.garvel
/Users/z0ltan/.garvel
└── cache

1 directory, 0 files
```

Note: even if you forget to invoke this command, all other command which require use of this system-level cache will 
      actually invoke this command before executing themselves. Also, if you want to uninstall this cache for some
      reason, the use the `garvel uninstall` command.
      
    

#### Creating a new project

To create a new project skeleton, simply invoke `garvel new <project-name>`. For example,

```
Macushla:testbed z0ltan$ garvel new hello-world
Project "hello-world" created successfuily

Macushla:testbed z0ltan$ cd hello-world/
Macushla:hello-world z0ltan$ tree
.
├── Garvel.gl
├── src
└── tests

2 directories, 1 file
```

That's basically it - `garvel` simply expects the project to have the structure shown above. Any other layout within this framework 
should work without any problems. Note, however, that all resources that will be part of your project's generated artifact (JAR file)
should be inside the `src` folder.

The `Garvel.gl` file is the configuration file for thie new project. Let's have a look at its contents:

```
Macushla:hello-world z0ltan$ cat Garvel.gl
[project]

name = "<Your project name>"
version = "0.1.0"
classpath = [ "." ] 
authors = [ "you@your-email.com" ]
description = "<project description>"
homepage = "<your homepage, or perhaps a github url>"
readme = "<link to your project's readme file>"
keywords = [ "enter", "some", "keywords", "to", "describe", "your", "project"]
categories = [ "what", "kind of project", "this is"] 
licence = "<standard licence type>"
licence-file = "<link to a custom licence file, if applicable>"


[dependencies]

junit/junit = "4.12"
log4j/log4j = "1.2.17"


[lib]

main-class = "com.foo.bar.Main"
fat-jar = "false"


[bin]

target1 = "com.foo.bar.Main" # normal class
target2 = "com.foo.bar.Main$Baz" # nested class
```

The `Garvel.gl` file is assumed to have the following four sections:

  * `project` - this describes the project metadata. This section is **mandatory**.
                Note that the keys provided in the template `Garvel.gl` file are fixed.
                Amongst these keys, `name`, and `version` are mandatory whereas the rest
                are optional. 
                Custom keys cannot be specified as of v1.0.
  
  * `dependencies` - this section defines the external dependencies of your project. 
                     This section is optional.
  
  * `lib` - this section has two possibe attributes:
      * `main-class` - this defines the entry-point for your project JAR file. This is **mandatory** if this section
                       is present. The section itself is optional.
      * `fat-jar`    - this attribute indicates that `garvel` must build the project along with all its dependencies into
                       a single "fat" jar. This attribute is optional, and default to `false`. *This feature is not 
                       implemented as of v1.0*.
      
  * `bin` - this section defines targets for the `garvel run` command. This section is also optional.


Note: The relative ordering of the sections is fixed. That is to say, `project` must always be the first section, followed by `dependencies`
      (if present), and then `lib` (if present), and finally `bin` (if present). To make it clearer still, the following are all valid
      configurations:

  * `project`
  * `project`, `dependencies`
  * `project`, `lib`
  * `project`, `bin`
  * `project`, `depedencies`, `lib`
  * `project`, `dependencies`, `bin`
  * `project`, `dependencies`, `lib`, `bin`
         
  whereas the following configurations would be invalid:
      
  * `dependencies` (missing `project` section)
  * `project`, `lib`, `dependencies` (`lib` cannot precede `dependencies`)
  * `project`, `dependencies`, `bin`, `lib` (`bin` cannot precede `lib`)


Next, let's trim down our `Garvel.gl` file down to the bare essentials:

```
Macushla:hello-world z0ltan$ cat Garvel.gl
[project]

name = "hello-world"
version = "1.2.3"
```


#### Building the project

Now, let's try to run this skeleton project:

```
Macushla:hello-world z0ltan$ garvel run
Creating target directory hierarchy.... DONE
Populating Core Caches...DONE
Analysing Dependencies...DONE
Cleaning up after failed build...DONE
Unable to run project. Reason = Prerequisite (build) for run command failed, Project Build failed: failed: no source files present in the project
```

We need to have some source files to actually build the project! Also, you will observe that a new file was generated in the project root: `Garvel.lock`.
This file, at the moment, contains the exact same contents as the `Garvel.gl` file. However, when the project's configuration file changes (especially
when new dependencies are added/removed/modified), then the `Garvel.lock` file plays a crucial role in deciding which dependencies to resolve and 
download. 

As such, this file it **not** to be modified by hand. Also, this file should be excluded from version control as it's likely to be in a constant state of
flux.

Okay, let's fill in some code now:

```
Macushla:hello-world z0ltan$ mkdir -p src/com/foo/

Macushla:hello-world z0ltan$ mkdir -p src/com/bar

Macushla:hello-world z0ltan$ cat src/com/foo/Add.java
package com.foo;

public class Add {
    public sum(final int x, final int y) {
        return x + y;
    }
}

Macushla:hello-world z0ltan$ cat src/com/
bar/ foo/
Macushla:hello-world z0ltan$ cat src/com/bar/Adder.java
package com.bar;

import com.foo.Add;

import java.util.Scanner;

public class Adder {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        final int a = in.nextInt();
        final int b = in.nextInt();

        Add add = new Add();
        System.out.printf("Sum of %d and %d = %d\n", a, b, add.sum(a, b));
    }
}

```

Let's try to build it!

```
Macushla:hello-world z0ltan$ garvel build
Creating target directory hierarchy.... DONE
Populating Core Caches...DONE
Analysing Dependencies...DONE
Detected JDK version 11
Starting compilation of source files...
Build failed due to compilation errors
/Users/z0ltan/Code/Projects/Playground/testbed/hello-world/src/com/foo/Add.java:4: invalid method declaration; return type required

Cleaning up after failed build...DONE
Build failed: Project Build failed: Build failed due to compilation errors

```

Ah, okay. So we forgot to include the return type for our `sum` method in `com/foo/Add.java`. Let's fix that and try again:

```
Macushla:hello-world z0ltan$ garvel build
Creating target directory hierarchy.... DONE
Populating Core Caches...DONE
Analysing Dependencies...DONE
Detected JDK version 11
Starting compilation of source files...
	Compiling project sources...DONE
	Generating project JAR...DONE
Building project artifacts.... DONE

Project built successfully

Macushla:hello-world z0ltan$ tree
.
├── Garvel.gl
├── Garvel.lock
├── src
│   └── com
│       ├── bar
│       │   └── Adder.java
│       └── foo
│           └── Add.java
├── target
│   ├── deps
│   │   └── dependency.graph
│   └── hello-world-1.2.3.jar
└── tests

7 directories, 6 files
```


Excellent! So we now have a few new artifacts that look interesting:

  * `target` - this folder is created by the build process and houses the project artifact, `hello-world-1.2.3.jar`
               whose name is created from the project `name` and `version` fields - this is the reason why these
               fields are mandatory in the `Garvel.gl` file.
               
  * `deps`   - contains the Dependency Graph for the project, `dependency.graph`. 
               This Dependency Graph becomes useful when we actually add project dependencies.

Also, let's take a look at what happened to our good old `garvel` cache:

```
Macushla:hello-world z0ltan$ tree ~/.garvel
/Users/z0ltan/.garvel
└── cache
    └── cache.mapping

1 directory, 1 file
```

Aha! So we have a new file, `cache.mapping`. Right now, the cache itself doesn't contain any actual artifacts, but for more complicate projects, this
file will allow `garvel` to check for dependencies in the system-level cache before trying to contact the Maven repos themselves.


#### Running the project

Now that we have the project successfully built, let's try to run it!

```
Macushla:hello-world z0ltan$ garvel run
Unable to run project. Reason = Please specify the `main-class` attribute if you don't specify a bin target
```

That's right, our configuration doesn't have either the `lib` section or the `bin` section!

First off, let's try providing the `main-class` attribute, since we do have a runnable class in our project.
Our `Garvel.gl` file now looks like so:

```
Macushla:hello-world z0ltan$ garvel run
1
2
Sum of 1 and 2 = 3
Target "none" was run successfully

```

Nice! Okay, let's try and run it using the `garvel run` command as well. So we add the `bin` section to our `Garvel.gl` file:

```
Macushla:hello-world z0ltan$ cat Garvel.gl
[project]

name = "hello-world"
version = "1.2.3"

[lib]

main-class = "com.bar.Adder"


[bin]

add = "com.bar.Adder"

```

```
Macushla:hello-world z0ltan$ garvel run add
23
19
Sum of 23 and 19 = 42
Target "add" was run successfully

```


Excellent! 

Just for completion, let's add some a dependency on `Junit` to assert that `1+1 = 2`! Which version of `Junit` do we use though?
Let's try to see the versions using the `garvel dep` command:

```
Macushla:hello-world z0ltan$ garvel dep junit/junit
Latest version: 4.12
Release version: 4.12
Available versions:
	3.7
	3.8
	3.8.1
	3.8.2
	4.0
	4.1
	4.2
	4.3
	4.3.1
	4.4
	4.5
	4.6
	4.7
	4.8
	4.8.1
	4.8.2
	4.9
	4.10
	4.11-beta-1
	4.11
	4.12-beta-1
	4.12-beta-2
	4.12-beta-3
	4.12

```

As we can see, the artifact is actually a Maven artifact (since `garvel` uses the Maven repos to retrieve artifacts for the project). The 
artifact must be specified in the `groupId/artifactId` format (using Maven terminology).

To see the dependencies of `JUnit` version `4.12-beta-3` (say), we can invoke the `garvel dep` command with the `--show-dependencies` flag,
and passing in the specific version:


```
Macushla:hello-world z0ltan$ garvel dep --show-dependencies 4.12-beta-3 junit/junit
Latest version: 4.12
Release version: 4.12
Available versions:
	3.7
	3.8
	3.8.1
	3.8.2
	4.0
	4.1
	4.2
	4.3
	4.3.1
	4.4
	4.5
	4.6
	4.7
	4.8
	4.8.1
	4.8.2
	4.9
	4.10
	4.11-beta-1
	4.11
	4.12-beta-1
	4.12-beta-2
	4.12-beta-3
	4.12


The dependency tree for version 4.12-beta-3 is:
+ junit/junit:4.12-beta-3
| + org.hamcrest/hamcrest-core:1.3

```

Okay, so `Junit` has only one dependency, `hamcrest` version `1.3`.

Let's add the contrived assertion check to the `Adder` class:

```
Macushla:hello-world z0ltan$ cat src/com/bar/Adder.java
package com.bar;

import com.foo.Add;

import java.util.Scanner;
import static org.junit.assertEquals;

public class Adder {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        final int a = in.nextInt();
        final int b = in.nextInt();

        Add add = new Add();
        System.out.printf("Sum of %d and %d = %d\n", a, b, add.sum(a, b));

        assertEquals(1+1, 2);
    }
}
```

Now, we add the dependency on `Junit` to the `Garvel.gl` file:

```
Macushla:hello-world z0ltan$ cat Garvel.gl
[project]

name = "hello-world"
version = "1.2.3"

[dependencies]

junit/junit = "4.12-beta-3"

[lib]

main-class = "com.bar.Adder"


[bin]

add = "com.bar.Adder"

```

The dependencies must follow the format: `groupId/artifactId = "<version>"`. Let's trigger a build:

```
Macushla:hello-world z0ltan$ garvel build
Creating target directory hierarchy.... DONE
Populating Core Caches...DONE
Finished downloading dependency junit/junit:4.12-beta-3
Finished downloading dependency org.hamcrest/hamcrest-core:1.3
Analysing Dependencies...DONE
Detected JDK version 11
Starting compilation of source files...
	Compiling project sources...DONE
	Generating project JAR...DONE
Building project artifacts.... DONE

Project built successfully
```

Running it:

```
Macushla:hello-world z0ltan$ garvel run
2
5
Sum of 2 and 5 = 7
Project was run successfully

```

Nice! Note that the first time downloading fresh dependencies will take some time, but once downloaded into the local cache, subsequent builds
will be considerably faster. Also note that if the 

Let's see what the system-level `garvel` cache now looks like:

```
Macushla:hello-world z0ltan$ tree ~/.garvel
/Users/z0ltan/.garvel
└── cache
    ├── cache.mapping
    ├── junit
    │   └── junit
    │       └── 4.12-beta-3
    │           └── junit-4.12-beta-3.jar
    └── org
        └── hamcrest
            └── hamcrest-core
                └── 1.3
                    └── hamcrest-core-1.3.jar

```

So the artifacts have been loaded into a nicely ordered tree - very nice! And finally, just to make sure that the JAR file was actually used, let's check the generated JAR file's classpath:


```
Macushla:hello-world z0ltan$ unzip target/hello-world-1.2.3.jar | cat META-INF/MANIFEST.MF
Manifest-Version: 1.2.3
Main-Class: com.bar.Adder
Class-Path: /Users/z0ltan/.garvel/cache/junit/junit/4.12-beta-3/junit-4.
 12-beta-3.jar /Users/z0ltan/.garvel/cache/org/hamcrest/hamcrest-core/1.
 3/hamcrest-core-1.3.jar

```

And that covers v1.0 of `garvel`.


## Versioning

Garvel uses [SemVer](http://semver.org/) for versioning. 


## Additional Documents

The project has a `design` directory which has the following files:

  * CLIGrammar.md - this file describes the grammar for the Command-Line Interface (CLI).
  * ConfigTOMLGrammar.md - this file describes the grammar for the configuration format used by `garvel` (a subset of `TOML`).
  * SemverGrammar.md - this file describes the grammar for the parsing of semantic versioning used for project dependencies.
  * DesignNotes.md - the intial ntes of the project. This is hopelessly outdated, and will be updated soon.
  * Scratch.md - random notes, scribblings, TODOs et al. This file will be removed shortly.


Now that version 1.0 has been achieved, my plan is to track all new features, changes, and issues through Github itself. 
In addition, more documents will be created and added showing the actual design and layout of the `garvel` source code
along with rationale for those design decisions.



## Authors

* **Timmy Jose [zoltan.jose@gmail.com]** - *Initial work* - [timmyjose](https://github.com/timmyjose)



## License

This project is licensed under the MIT License - see the [LICENSE.md](https://github.com/timmyjose/garvel/blob/master/LICENSE) 

