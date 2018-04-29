### Grammar for parsing semantic versioning for garvel


```
Semver ::= Version ("-" PreRelease |  "+" Build | "-" PreRelease "+" Build)   
```

```
Version ::= major "." minor ["." patch]
```

```
Major ::= IntegerLiteral
```

```
Minor ::= IntegerLiteral
```

```
Patch ::= IntegerLiteral
```

```
PreRelease ::= Identifier ("." Identifier)*
```

```
Build ::= Identifier ("." Identifier)*
```

```
Identifier ::= Letter (Letter | Digit)*
```

```
IntegerLiteral ::= Digit (Digit)*
```


```
Digit ::= [0-9]
```

```
Letter ::= [a-zA-Z]
```           
