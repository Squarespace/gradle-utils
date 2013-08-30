package kotka.gradle.utils.tasks

import kotka.gradle.utils.ConfigureUtil
import kotka.groovy.zweig.ZweigBuilder
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.builder.AstBuilder
import org.codehaus.groovy.ast.expr.ArgumentListExpression
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.ConstructorCallExpression
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codehaus.groovy.ast.stmt.ExpressionStatement
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

import org.gradle.api.file.FileTree
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.tasks.util.PatternFilterable

import org.objectweb.asm.Opcodes

@GroovyASTTransformation(phase=CompilePhase.SEMANTIC_ANALYSIS)
class GenericSourceSetConventionTransformation implements ASTTransformation {
    void visit(ASTNode[] nodes, SourceUnit sourceUnit) {
        def sourceName  = nodes[0].members.sourceName.value
        def sourceNameC = sourceName.capitalize()

        def patterns = nodes[0].members.sourcePatterns

        def target = nodes[1]
        target.setSuperClass ClassHelper.make(GenericSourceSetConvention)

        target.addConstructor(ZweigBuilder.toNode([
                constructor: [[displayName: String],
                              [fileResolver: FileResolver]],
                body: [
                        [construct: ClassNode.SUPER,
                         with:      [sourceName, patterns,
                                     [variable: "displayName"],
                                     [variable: "fileResolver"]]]
                ]
        ]))

        target.addMethod(ZweigBuilder.toNode([
                method:     "get${sourceNameC}",
                returnType: SourceDirectorySet,
                body: [
                        [field: "source", of: target]
                ]
        ]))

        target.addMethod(ZweigBuilder.toNode([
                method:     sourceName,
                // FIXME: Object here is wrong, but I don't know
                //        how to use target here.
                //        mb - 12.06.2013
                returnType: Object,
                // FIXME: Object here is wrong, but I don't know
                //        how to use Closure here.
                //        mb - 12.06.2013
                arguments:  [[fn: Object]],
                body: [
                        [callStatic: "configure",
                         on:         ConfigureUtil,
                         with:       [[field: "source", of: target],
                                      [variable: "fn"]]],
                        [variable: "this"]
                ]
        ]))

        target.addMethod(ZweigBuilder.toNode([
                method:     "getAll${sourceNameC}",
                returnType: FileTree,
                body: [
                        [field: "allSource", of: target]
                ]
        ]))

        target.addMethod(ZweigBuilder.toNode([
                method:     "get${sourceNameC}SourcePatterns",
                returnType: PatternFilterable,
                body: [
                        [field: "sourcePatterns", of: target]
                ]
        ]))
    }
}
