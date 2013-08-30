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

import org.codehaus.groovy.transform.GroovyASTTransformationClass

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * Extends a given class to be gradle source set convention. It makes
 * <code>GenericSourceSetConvention</code> the super class of the annotated
 * class and adds the given patterns. Various getters are added to allow the
 * gradle extensions mechanism to work.
 *
 * <ul>
 *     <li><code>get&lt;source name&gt;</code></li>
 *     <li><code>getAll&lt;source name&gt;</code></li>
 *     <li><code>get&lt;source name&gt;Patterns</code></li>
 * </ul>
 *
 * @author Meikel Brandmeyer
 */
@Retention(RetentionPolicy.SOURCE)
@Target([ElementType.TYPE])
@GroovyASTTransformationClass(["kotka.gradle.utils.tasks.GenericSourceSetConventionTransformation"])
@interface GenericSourceSet {
    String sourceName()
    String[] sourcePatterns() default []
}
