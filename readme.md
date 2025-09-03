# KtBuilder

Simple Builder Code Generation for kotlin

Yes, we have named parameter in kotlin, but there are situations where we still need Builder.

1. Easier handling of optional parameters
   •	With constructors or factory methods, dealing with default/optional values can get messy.
   •	A Builder makes it easy to set only the required values while leaving the rest at their defaults.


2. Support for immutability
   •	Since you don’t use setters and finalize the object in a single build() call, it’s straightforward to create immutable objects that cannot be modified after construction.


3. Clean method chaining
   •	With a Builder, you can chain methods like builder.name().age().city()..., which makes the code neat and expressive.
   •	IDE auto-completion also helps reduce mistakes.

## User guide

To be documented

