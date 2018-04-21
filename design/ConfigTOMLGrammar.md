### Grammar for the Garvel config (restricted TOML format)

```
Config ::= Project-Section  Dependencies-Section [ Bin-Section ]
```

```
Project-Section ::= '[' PROJECT ']' (NAME '=' Identifier) (VERSION = SemverString) 
            [ CLASSPATH = '[' ClassPathString (, ClassPathString)* ']') ]
            [ AUTHORS = '[' Identifier (, Identifier)* ']' ]
            [ DESCRIPTION = Identifier  EOL]
            [ HOMEPAGE = UrlString  EOL]
            [ README = UrlString EOL] 
            [ KEYWORDS = Identifier (, Identifier)* EOL]
            [ CATEGORIES = Identifier (, Identifier)* EOL]
            [ LICENCE = Identifier EOL]
            [ LICENCE-FILE = Identifier EOL]
            
```

```
Dependencies-Section ::= '[' DEPENDENCIES ']' Dependency-Pair (, Dependency-Pair)*
```

```
Dependency-Pair ::= Identifier '=' SemverString
```

```
SemverString ::= # refer to SemverGrammar.md
```

```
Bin-Section ::= '[' BIN ']' Bin-Pair (, Bin-Pair)*
```

```
Bin-Pair ::= Identifier  '=' ClassPathString 
```

```
ClassPathString ::= [a-zA-Z0-9_/:\;]
```

```
Identifier ::= Letter (Letter | Digit)*
```

```
Letter ::= [a-zA-Z_]
```

```
Digit ::= [0-9]
```