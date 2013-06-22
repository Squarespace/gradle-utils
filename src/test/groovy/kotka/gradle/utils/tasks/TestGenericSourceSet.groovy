package kotka.gradle.utils.tasks

import spock.lang.Specification

class TestGenericSourceSet extends Specification {
    def "transformation adds relevant methods"() {
        given:
        def set    = new SomeTestSourceSetConvention("main", null)
        def called = false

        expect:
        set.some != null
        set.some { x -> assert x == set.some; called = true }
        set.allSome != null
        set.someSourcePatterns != null

        and:
        called == true
    }
}
