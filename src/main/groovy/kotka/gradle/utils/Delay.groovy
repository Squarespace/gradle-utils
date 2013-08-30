package kotka.gradle.utils

final class Delay {
    def private Closure fn
    def private exc = null
    def private value = null

    Delay(Closure f) {
        fn = f
    }

    def synchronized doForce() {
        if (fn != null) {
            try {
                value = fn()
            } catch (Exception e) {
                exc = e
            } finally {
                fn = null
            }
        }

        if (exc != null)
            throw exc

        value
    }

    def static force(o) {
        while (o instanceof Delay) {
            o = o.doForce()
        }
        o
    }
}
