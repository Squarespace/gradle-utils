/*-
 * Copyright 2013 © Meikel Brandmeyer.
 * All rights reserved.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package kotka.gradle.utils.tasks

import kotka.gradle.utils.ConfigureUtil

import kotka.groovy.zweig.ZweigBuilder
import kotka.groovy.zweig.ZweigUtil

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

import org.gradle.api.file.FileTree
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.tasks.util.PatternFilterable

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
                returnType: ZweigUtil.nonGeneric(target),
                arguments:  [[fn: Closure]],
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
