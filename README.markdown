# Gradle utilities

## Description

[Gradle](http://www.gradle.org) is a beautiful and powerful build system.
However extending it with plugins sometimes bears some repetition, which
is hard to get rid of due to the way gradle works under the hood. At
other times gradle provides some nice feature, but doesn't expose it
(yet) as public API.

*gradle-utils* provide the missing link. With helpers which don't
explode underneath your hands. And “macros” (aka. AST transforms) to
reduce boilerplate so you don't have to care how the latter work.

## Example

The `@Delayed` annotation may be used to delay the evaluation of a
field's value.

    class SomeTask extends DefaultTask {
        @Delayed
        def projectVersion
    }
    
    /* in SomePlugin */
    project.task("someTask", type: SomeTask) {
        delayedProjectVersion = { project.version }
    }
    
    /* in build.gradle */
    apply plugin: "some"
    
    version = "diff.erent.version"
    
    assert someTask.projectVersion == version

## Usage

The *gradle-utils* are available from Clojars as maven dependency.

    repositories {
        maven { url "http://clojars.org/repo" }
    }
    
    dependencies {
        compile "de.kotka.gradle:gradle-utils:&lt;version&gt;"
    }

## Contribution

Any form of contribution is highly appreciated!

However please send pull requests only in case you know how they work.
If in doubt please open a ticket in the issue tracker with an attached
patch. This helps you and me to get things sorted out more quickly than
with pull requests containing tons of unrelated changes.

## Contact

 * the [author](mailto:mb@kotka.de)
 * the [current version](https://clojars.org/de.kotka.gradle/gradle-utils) on Clojars
 * the [source repository](https://bitbucket.org/kotkade/gradle-utils) on Bitbucket
 * the [bugtracker](https://bitbucket.org/kotkade/gradle-utils/issues) on Bitbucket
 * the [dark side](https://github.com/kotkade/gradle-utils) in case your SCM does
   not support mercurial repositories (which says something about your SCM, btw).
   However the bitbucket one is authoritative.

