package kotka.gradle.utils

import spock.lang.Specification

class TestDelayed extends Specification {
    class UnderTest {
        @Delayed def thing
    }

    def "getting a delayed property works for things"() {
        given:
        def o = new UnderTest()

        when:
        o.thing = x

        then:
        o.thing == x

        where:
        x = [ null, 1, "foo", "bar${1 + 1}", true, new Object(), { } ]
    }

    def "getting a delayed property actually forces the delay"() {
        given:
        def o = new UnderTest()
        def effect = 0
        o.thing = new Delay({ effect += 1 })

        expect:
        effect  == 0

        and:
        o.thing == 1

        and:
        effect  == 1
    }

    def "delayed setting a delayed property reset the property"() {
        given:
        def o = new UnderTest()
        def effect = 0
        o.delayedThing = { effect += 1 }

        expect:
        effect  == 0

        and:
        o.thing == 1

        and:
        effect  == 1
    }
}
