package com.gradle.abi

class InternalLambda {

    fun foo() {
        f {
            println(" + action")
        }
    }

/*
    f()

    b()

    f {
        println(" + action")
    }
*/

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