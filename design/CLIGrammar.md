### Grammar for parsing command-line options in garvel


The EBNF-like grammar is given as follows:

```
program ::= garvel [option] command 
```

```
option ::= -v | --verbose | -q | --quiet
```

```
command ::= helpCommand | versionCommand | listCommand 
           | newCommand | initCommand | updateCommand 
           | buildCommand | cleanCommand | runCommand 
           | testCommand | docCommand | benchCommand 
           | publishCommand             
```

```
helpCommand ::= subCommand 
```

```
versionCommand ::= versionOption (versionOption)*
```

```
listCommand ::= epsilon

```

```
newCommand ::= newOption (newOption)* path
```

```
newOption ::= --vcs Identifier* | --bin | --lib | --name Identifier* | - (h | -help)
```

```
initCommand ::= newOption (newOption)* path
```

```
updateCommand ::= updateOption (updateOption)*
```

```
buildCommand ::= buildOption (buildOption)*
```

```
cleanCommand ::= cleanOption (cleanOption)*
```

```
runCommand ::= runOption (runOption)* 
```

```
testCommand ::= TBD
```

```
benchCommand ::= TBD
```

```
publishCommand ::= publishOption (publishOption)*
```

```
build := build (buildOption)*
```

```
Identifier ::= Letter (Letter | Space | Digit | SpecialCharacters)*
```

```
Letter := [a-zA-Z]
```

```
Space ::= ' ' 
```

```
Digit := [0-9]
```

```
SpecialCharacters ::= [_-()]
```