# kotlin-abi-internal-lambda

This simple reproducer attempt to showcase a problem with determining the ABI fingerprint of a class like the following.

```kotlin
class InternalLambda {

    fun foo() {
        f()
    }

    private fun f() {
        println("foo")
    }

    private fun f(action: () -> Unit) {
        print("foo")
        action()
    }

    private fun b() {
        println("bar")
    }
}
```

More specifically: whichever of the private functions gets used in the body of the `foo()` function, the ABI fingerprint of the class should remain the same, but doesn't.

To demonstrate, run the reproducer: `./gradlew run --rerun-tasks`, you get:

```
ABI fingerprint of com/gradle/abi/InternalLambda.class is: 6935305231824228272
Calling the function yields: foo
```

Edit the body of the `foo()` function in `InternalLambda` (project `target`), replace `f()` with `b()`.

Run the reproducer: `./gradlew run --rerun-tasks`, you get:

```
ABI fingerprint of com/gradle/abi/InternalLambda.class is: 6935305231824228272
Calling the function yields: bar
```

Edit the body of the `foo()` function again, replace `b()` with the following:

```kotlin
    f {
        println(" + action")
    }
```

Run the reproducer: `./gradlew run --rerun-tasks`, you get:

```
ABI fingerprint of com/gradle/abi/InternalLambda$foo$1.class is: INACCESSIBLE
ABI fingerprint of com/gradle/abi/InternalLambda.class is: -8905778991490037264
Calling the function yields: foo + action
```
