### Grammar for the Garvel config (restricted TOML format)

```
Config ::= Project-Section  Dependencies-Section [Lib-Section] [ Bin-Section ]
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
Dependency-Pair ::= Identifier '=' Semver
```

```
Semver ::= // refer to SemverGrammar.md
```

```
Lib-Section ::=  '[' LIB ']' Main-Class [Fat-Jar]
```

```
Main-Class ::= MAIN_CLASS '=' Identifier
```

```
Fat-Jar ::= "true" | "false"
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