### Grammar for parsing command-line options in garvel


The EBNF-like grammar is given as follows:

```
Program ::= [(-v | --verbose) | (-q | --quiet)] Command 
```

```
Command ::= help CommandName
           | list 
           | version
           | new [--vcs Identifier | --vcs Identifier --bin | --vcs Identifier --lib | --bin | --lib] PATH
           | build
           | clean
           | run
           | test
```

```
CommandName ::= CommandIdentifier
```

```
CommandIdentifier ::= "help" | "list" | "version" | "new" | "build" | "clean" | "run" | "test"
```

```
PATH ::= Identifier
```

```
Identifier ::= Letter (Letter | Digit)*
```

```
Letter := [a-zA-Z_-/]
```

```
Digit := [0-9]
```


