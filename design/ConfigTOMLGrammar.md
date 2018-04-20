### Grammar for the Garvel config (restricted TOML)

```
Config ::= Project-Section  Dependencies-Section Lib-Section [ Bin-Section ]
```

```
Project-Section ::= '[' PROJECT ']' (NAME = Identifier) (VERSION = SemverString) (CLASSPATH = '[' ClassPathString (, ClassPathString)* ']')
            (AUTHORS = '[' Identifier (, Identifier)* ']')
            [ DESCRIPTION = Identifier  EOL]
            [ HOMEPAGE = UrlString  EOL]
            [ README = UrlString EOL] 
            [ KEYWORDS = Identifier (, Identifier)* EOL]
            [ CATEGORIES = Identifier (, Identifier)* EOL]
            [ LICENCE = Identifier EOL]
            [ LICENCE-FILE = Identifier EOL]
            
```

```
Dependencies-Section ::= Dependency-Pair (EOL Dependency-Pair)* EOL
```

```
Dependency-Pair ::= Identifier '=' SemverString EOL
```

```
SemverString ::= # refer to SemverGrammar.md
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