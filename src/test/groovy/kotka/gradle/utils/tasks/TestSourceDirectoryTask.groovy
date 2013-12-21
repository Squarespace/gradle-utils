package kotka.gradle.utils.tasks

import org.gradle.testfixtures.ProjectBuilder

import spock.lang.Shared
import spock.lang.Specification

import com.github.goldin.spock.extensions.tempdir.TempDir

import java.io.File

class TestSourceDirectoryTask extends Specification {
    @Shared
    @TempDir
    def File baseDir

    def static aSrc = [ "a/foo.txt", "a/bar.txt" ]
    def static xSrc = [ "x/baz.txt" ]

    def setupFiles(baseDir, files) {
        files.each {
            def f = new File(baseDir, it)
            f.parentFile.mkdirs()
            f.text = "Dummy"
        }
    }

    def "we get directories as well as the source"() {
        given: "a project"
        def p = ProjectBuilder.builder().build()

        and:   "a file hierarchy"
        def a = new File(baseDir, "srca")
        def x = new File(baseDir, "srcx")
        setupFiles(a, aSrc)
        setupFiles(x, xSrc)

        when:
        def t = p.task("t", type: SourceDirectoryTask)
        t.srcDirs a

        then:
        t.srcDirs as Set == [ a ] as Set
        t.source as Set  == aSrc.collect { new File(a, it) } as Set
    }

    def "multiple source directories are possible"() {
        given: "a project"
        def p = ProjectBuilder.builder().build()

        and:   "a file hierarchy"
        def a = new File(baseDir, "srca")
        def x = new File(baseDir, "srcx")
        setupFiles(a, aSrc)
        setupFiles(x, xSrc)

        when:
        def t = p.task("t", type: SourceDirectoryTask)
        t.srcDirs a, x

        then:
        t.srcDirs as Set == [ a, x ] as Set
        t.source as Set  == (aSrc.collect { new File(a, it) } +
            xSrc.collect { new File(x, it) }) as Set
    }

    def "overriding source directories is possible"() {
        given: "a project"
        def p = ProjectBuilder.builder().build()

        and:   "a file hierarchy"
        def a = new File(baseDir, "srca")
        def x = new File(baseDir, "srcx")
        setupFiles(a, aSrc)
        setupFiles(x, xSrc)

        when:
        def t = p.task("t", type: SourceDirectoryTask)
        t.srcDir a
        t.srcDirs = [ x ]

        then:
        t.srcDirs as Set == [ x ] as Set
        t.source as Set  == xSrc.collect { new File(x, it) } as Set
    }

    def "filtering source files is possible"() {
        given: "a project"
        def p = ProjectBuilder.builder().build()

        and:   "a file hierarchy"
        def a = new File(baseDir, "srca")
        def x = new File(baseDir, "srcx")
        setupFiles(a, aSrc)
        setupFiles(x, xSrc)

        when:
        def t = p.task("t", type: SourceDirectoryTask)
        t.srcDirs a, x
        t.exclude "x/*"

        then:
        t.srcDirs as Set == [ a, x ] as Set
        t.source as Set  == aSrc.collect { new File(a, it) } as Set
    }
}
