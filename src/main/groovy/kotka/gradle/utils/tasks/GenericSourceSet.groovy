package kotka.gradle.utils.tasks

import org.codehaus.groovy.transform.GroovyASTTransformationClass

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@Retention(RetentionPolicy.SOURCE)
@Target([ElementType.TYPE])
@GroovyASTTransformationClass(["kotka.gradle.utils.tasks.GenericSourceSetConventionTransformation"])
@interface GenericSourceSet {
    String sourceName()
    String[] sourcePatterns() default []
}
