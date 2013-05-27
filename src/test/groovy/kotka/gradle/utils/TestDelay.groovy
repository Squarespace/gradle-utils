package kotka.gradle.utils

import spock.lang.Specification

class TestDelay extends Specification {
    def "forcing a value is the value"() {
        expect:
        Delay.force(x) == x

        where:
        x = [ null, 1, "foo", "bar${1 + 1}", true, new Object() ]
    }

    def "forcing a delay gives the value"() {
        given:
        def x = 5
        def d = new Delay({ x })

        expect:
        Delay.force(d) == x
    }

    def "delaying a value works"() {
        given:
        def x = 5
        def d = new Delay({ x })

        when:
        x = 7

        then:
        Delay.force(d) == 7
    }

    def "exceptions are thrown and kept"() {
        given:
        def x = 0
        def d = new Delay({ x += 1; throw new Exception() })

        when:
        Delay.force(d)

        then:
        thrown(Exception)
        x == 1

        when:
        Delay.force(d)

        then:
        thrown(Exception)
        x == 1
    }
}
