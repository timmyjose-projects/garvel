### Grammar for parsing command-line options in kaapi

The EBNF-like grammar is given as follows:

```
program ::= kaapi [option] command 
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
helpCommand ::= help helpCommand 
```

```
versionCommand ::= Identifier 
```

```
listCommand ::= Identifier (Identifier)*

```

```
newCommand ::= newOption (newOption)* path
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