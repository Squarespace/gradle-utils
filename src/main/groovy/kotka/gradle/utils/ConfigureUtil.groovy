package kotka.gradle.utils

class ConfigureUtil {
    static configure(Object target, Closure configureFn={}) {
        def fn = configureFn.clone()

        fn.with {
            resolveStrategy = Closure.DELEGATE_FIRST
            delegate        = target
        }

        if (fn.maximumNumberOfParameters == 0)
            fn.call()
        else
            fn.call(target)

        target
    }
}
