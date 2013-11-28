package kotka.gradle.utils

import spock.lang.Specification

class TestFilterable extends Specification {
    @Filterable(fieldName="filter")
    class UnderTest {
    }

    def "including things works"() {
        given:
        def f = new UnderTest()

        when:
        f.include "**/bar.*"

        then:
        f.includes == ([ "**/bar.*" ] as Set)
    }
}
