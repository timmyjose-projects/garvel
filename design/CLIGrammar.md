### Grammar for parsing command-line options in garvel


The EBNF-like grammar is given as follows:

```
Program ::= [(-v | --verbose) | (-q | --quiet)] Command 
```

```
Command ::= help Command-Name
           | list 
           | version
           | install 
           | uninstall
           | init [--vcs Identifier] PATH
           | new [--vcs Identifier] PATH
           | build
           | clean
           | run [TargetName] [TargetArgs]
           | test
           | dep [(-s | --show-dependencies) VERSION] Dependency-Name
```

```
CommandName ::= CommandIdentifier
```

```
CommandIdentifier ::= "help" | "list" | "version" | "init" | new" | "build" | "clean" | "run" | "test"
```

```
TargetName ::= Identifier 
```

```
Target Args ::= Identifier (Identifier)*
```

```
Dependency-Name ::= Identifier
```

```
PATH ::= Identifier
```

``
VERSION ::= Identifier
``

```
Identifier ::= Letter (Letter | Digit)*
```

```
Letter := [a-zA-Z_-/]
```

```
Digit := [0-9]
```


