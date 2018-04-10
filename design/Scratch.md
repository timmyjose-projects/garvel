### Scratch Pad

This will serve as a scratchpad for items that do not fit clearly in any other document


#### Patterns

**Abstract Factory** - use this to expose the core services of garvel to external clients such as the CLI. Use this in conjunction
with the SPI model.


**Builder** - use this in the CLI to chain together options while constructing a `Command`.


**Command** - this pattern will come in useful when defining the various commands - build, new, init, clean, run, test, bench, etc.


**Chain of Responsibility** - this might come in handy for elegant proper handling.


**Observer** - this should come in handy in multiple modules to handle callbacks.


**State** - this may be useful when applying commands as a sequence of lifecycle steps.


**Strategy** - this could be used on conjunction with the state pattern for execution of commands by providing dynamic dispatch based on the current context.


**Template** - this could be used on top of the state and strategy patterns to provide an abstract lifecycle.


**Decorator** - may be applicable, not sure.


**Proxy** - might come in useful for implementing networking and vcs functionality.




