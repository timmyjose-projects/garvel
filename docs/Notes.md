## Tentative Kaapi interface

```
$ kaapi
A Java package manager inspired by Cargo, the Rust package manager.

Usage:
    kaapi <command> [<args>...]
    kaapi [options]

Options:
    -h, --help          Display this message
    -V, --version       Print version info and exit
    --list              List installed commands
    -v, --verbose ...   Use verbose output (-vv very verbose/build.rs output)
    -q, --quiet         No output printed to stdout
    --color WHEN        Coloring: auto, always, never [optional]
    --frozen            Require Kaapi.lock and cache are up to date
    --locked            Require Kaapi.lock is up to date

Some common kaapi commands are (see all commands with --list):
    build       Compile the current project
    clean       Remove the target directory
    doc         Build this project's and its dependencies' documentation
    new         Create a new kaapi project
    init        Create a new kaapi project in an existing directory
    run         Build and execute src/Main.java
    test        Run the tests
    bench       Run the benchmarks [optional]
    update      Update dependencies listed in Kaapi.lock
    search      Search registry for JARs/Modules
    publish     Package and upload this project to the registry (maybe have something like kaapi.io?)

See 'kaapi help <command>' for more information on a specific command.
```


## Project layout

```
$ kaapi new foo

foo
├── Kaapi.toml
└── src
    └── Lib.java

1 directory, 2 files
```

```
$ kaapi new --bin bar

bar
├── Kaapi.toml
└── src
    └── Main.java

1 directory, 2 files
```


```
$ cat Kaapi.toml
[package]
name = "bar"
version = "0.1.0" (use semver)
authors = ["tzj"]

[dependencies]
```


## Implementation Prerequisites

### Formats

 * TOML
 
### Techical Skills

 * Parsing getopt-style
 * TOML parsing
 * Networking
 * Diffing


## Design Notes

Expand this section into a separate document. Currently, this is used as as scratchpad.


## Running Log







