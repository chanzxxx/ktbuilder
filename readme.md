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

Setup gradle ksp plugin first
```kotlin
plugins {
    id("com.google.devtools.ksp") version "1.9.25-1.0.20" // use version compatible with your kotlin version
}
```

You can find compatible version from [here](https://github.com/google/ksp/releases).
The first three digits of ksp version should match your kotlin version.

Then add dependency
```kotlin
dependencies {
    ksp("com.chanzxxx.util:ktbuilder:0.1.1")
    implementation("com.chanzxxx.util:ktbuilder:0.1.1")
}
```

Now you can use `@KtBuilder` annotation to generate builder for your class.
Annotate your class with @KtBuilder
```kotlin
@KtBuilder
data class Person(val name: String, val age: Int)
```

Then you can use the generated builder like this 
```kotlin
val person = ktBuilderFor<Person>()
    .name("John")
    .age(30)
    .build()
```