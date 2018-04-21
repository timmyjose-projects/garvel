### Grammar for the Garvel config (restricted TOML format)

```
Config ::= Project-Section  Dependencies-Section [ Bin-Section ]
```

```
Project-Section ::= '[' PROJECT ']' (NAME '=' Identifier) (VERSION = Identifier) 
            [ CLASSPATH = '[' Identifier (, Identifier)* ']') ]
            [ AUTHORS = '[' Identifier (, Identifier)* ']' ]
            [ DESCRIPTION = Identifier ]
            [ HOMEPAGE = Identifier ]
            [ README = Identifier ] 
            [ KEYWORDS = '[' Identifier (, Identifier)* ']' ]
            [ CATEGORIES = '[' Identifier (, Identifier)* ']' ]
            [ LICENCE = Identifier ]
            [ LICENCE-FILE = Identifier ]
            
```

```
Dependencies-Section ::= '[' DEPENDENCIES ']' Dependency-Pair (, Dependency-Pair)*
```

```
Dependency-Pair ::= Identifier '=' SemverString
```

```
Bin-Section ::= '[' BIN ']' Bin-Pair (, Bin-Pair)*
```

```
Bin-Pair ::= Identifier  '=' ClassPathString 
```

```
Identifier ::= '"' Letter (Letter | Digit)* '"'
```

```
Letter ::= [a-zA-Z_+-:/;]
```

```
Digit ::= [0-9]
```