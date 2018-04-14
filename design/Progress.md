### Progress Tracker

Implemented `JsonLexer` and `JsonScanner`. To implement `JsonParser`, must decide on
the AST for the parser. The AST, in whichever form, must also be cached so that 
every command does not need to parse the configuration file each time, unless the
file itself has changed.

For `JsonParser`, the AST will simply be a `Map` from pre-defined properties to their
values, following the template of the `Garvel.gl` file.

