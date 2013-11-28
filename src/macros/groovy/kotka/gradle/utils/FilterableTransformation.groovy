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

package kotka.gradle.utils

import kotka.groovy.zweig.ZweigBuilder

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

import org.gradle.api.tasks.util.PatternSet
import org.gradle.api.specs.Spec

@GroovyASTTransformation(phase=CompilePhase.SEMANTIC_ANALYSIS)
class FilterableTransformation implements ASTTransformation {
    void visit(ASTNode[] nodes, SourceUnit sourceUnit) {
        def fieldName = nodes[0].members.fieldName.value
        def thisClass = nodes[1]

        def target = [variable: "this"]
        def field  = [field: fieldName, of: thisClass]

        thisClass.addField(ZweigBuilder.toNode([
            field:    fieldName,
            of:       thisClass,
            type:     PatternSet,
            init:     [construct: PatternSet]
        ]))

        [ "exclude", "include" ].each { op ->
            thisClass.addMethod(ZweigBuilder.toNode([
                    method:     op,
                    returnType: thisClass,
                    arguments:  [[patterns: "[Ljava.lang.String;"]],
                    body: [
                            [call: op,
                             on:   field,
                             with: [[variable: "patterns"]]],
                            target
                    ]
            ]))

            thisClass.addMethod(ZweigBuilder.toNode([
                    method:     op,
                    returnType: thisClass,
                    arguments:  [[patterns: Iterable]],
                    body: [
                            [call: op,
                             on:   field,
                             with: [[variable: "patterns"]]],
                            target
                    ]
            ]))

            thisClass.addMethod(ZweigBuilder.toNode([
                    method:     op,
                    returnType: thisClass,
                    arguments:  [[spec: Spec]],
                    body: [
                            [call: op,
                             on:   field,
                             with: [[variable: "spec"]]],
                            target
                    ]
            ]))

            thisClass.addMethod(ZweigBuilder.toNode([
                    method:     op,
                    returnType: thisClass,
                    arguments:  [[fn: Object]],
                    body: [
                            [call: op,
                             on:   field,
                             with: [[variable: "fn"]]],
                            target
                    ]
            ]))

            thisClass.addMethod(ZweigBuilder.toNode([
                    method:     "get${op.capitalize()}s",
                    returnType: Set,
                    body: [
                            [call: "get${op.capitalize()}s",
                             on:   field]
                    ]
            ]))

            thisClass.addMethod(ZweigBuilder.toNode([
                    method:     "set${op.capitalize()}s",
                    returnType: thisClass,
                    arguments:  [[patterns: Iterable]],
                    body: [
                            [call: "set${op.capitalize()}s",
                             on:   field,
                             with: [[variable: "patterns"]]],
                            target
                    ]
            ]))
        }
    }
}
