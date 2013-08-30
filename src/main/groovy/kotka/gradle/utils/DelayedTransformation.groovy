package kotka.gradle.utils

import kotka.groovy.zweig.ZweigBuilder

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

@GroovyASTTransformation(phase=CompilePhase.SEMANTIC_ANALYSIS)
class DelayedTransformation implements ASTTransformation {
    void visit(ASTNode[] nodes, SourceUnit sourceUnit) {
        def fieldNode = nodes[1]

        def definingClass = fieldNode.owner

        definingClass.addMethod(ZweigBuilder.toNode([
                method:     "get${fieldNode.name.capitalize()}",
                returnType: fieldNode.type,
                body: [
                        [callStatic: "force",
                         on:         Delay,
                         with:       [fieldNode]]
                ]
        ]))

        definingClass.addMethod(ZweigBuilder.toNode([
                method:     "setDelayed${fieldNode.name.capitalize()}",
                arguments:  [[fn: Object]],
                returnType: ClassHelper.VOID_TYPE,
                body: [
                        [set: [variable: fieldNode],
                         to:  [construct: Delay,
                               with:      [[variable: "fn"]]]]
                ]
        ]))
    }
}
