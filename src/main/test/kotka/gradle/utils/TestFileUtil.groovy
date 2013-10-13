package kotka.gradle.utils

import java.io.File

import spock.lang.Specification

class TestFileUtil extends Specification {
    def "FileUtil.file adds a child"() {
        given: "a parent file and a desired target"
        def parent = new File("a")
        def target = new File("a${File.separator"}b")

        expect:
        FileUtil.file(parent, "b").equals(target)
    }

    def "FileUtil.file concatenates multiple path elements"() {
        given: "a parent file and a desired target"
        def parent = new File("a")
        def target = new File("a${File.separator}b${File.separator}c${File.separator}d")

        expect:
        FileUtil.file(parent, "b", "c", "d").equals(target)
    }
}
