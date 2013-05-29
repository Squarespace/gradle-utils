package kotka.gradle.utils

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.ast.expr.BinaryExpression
import org.codehaus.groovy.ast.expr.ConstructorCallExpression
import org.codehaus.groovy.ast.expr.FieldExpression
import org.codehaus.groovy.ast.expr.StaticMethodCallExpression
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codehaus.groovy.ast.stmt.ExpressionStatement
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.syntax.Token
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

import java.lang.reflect.Modifier

@GroovyASTTransformation(phase=CompilePhase.SEMANTIC_ANALYSIS)
class DelayedTransformation implements ASTTransformation {
    void visit(ASTNode[] nodes, SourceUnit sourceUnit) {
        def delayNode = ClassHelper.make(Delay)
        def fieldNode = nodes[1]

        def getterNode = new MethodNode(
                "get${fieldNode.name.capitalize()}",
                Modifier.PUBLIC,
                fieldNode.type,
                [] as Parameter[],
                [] as ClassNode[],
                new ExpressionStatement(
                        new StaticMethodCallExpression(
                                delayNode,
                                "force",
                                new FieldExpression(fieldNode)
                        )
                )
        )

        def delayedSetterNode = new MethodNode(
                "setDelayed${fieldNode.name.capitalize()}",
                Modifier.PUBLIC,
                ClassHelper.VOID_TYPE,
                [new Parameter(ClassHelper.OBJECT_TYPE, "fn")] as Parameter[],
                [] as ClassNode[],
                new ExpressionStatement(
                        new BinaryExpression(
                                new VariableExpression(fieldNode),
                                Token.newSymbol("=", -1, -1),
                                new ConstructorCallExpression(
                                        delayNode,
                                        new VariableExpression("fn")
                                )
                        )
                )
        )

        fieldNode.owner.addMethod getterNode
        fieldNode.owner.addMethod delayedSetterNode
    }
}
