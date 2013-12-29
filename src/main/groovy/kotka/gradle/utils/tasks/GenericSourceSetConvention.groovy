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

import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.internal.file.UnionFileTree
import org.gradle.api.internal.file.DefaultSourceDirectorySet
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.tasks.util.PatternFilterable
import org.gradle.api.tasks.util.PatternSet

/**
 * A base class to extend gradle's <code>SourceSet</code>. Not to be
 * used directly!
 *
 * <p>
 * This class contains all necessary logic. Unfortunately the gradle
 * extension mechanism is based in static class information. So you
 * can't simply derive the class and be happy.
 *
 * <p>
 * You should read the documentation on the <code>GenericSourceSet</code>
 * annotation.
 *
 * @author Meikel Brandmeyer
 */
class GenericSourceSetConvention {
    protected final SourceDirectorySet source
    protected final UnionFileTree      allSource
    protected final PatternFilterable  sourcePatterns = new PatternSet()

    GenericSourceSetConvention(String language, List<String> patterns,
            String displayName, FileResolver fileResolver) {
        def displayString = "${displayName} ${language} source"

        source = initSourceSet(displayString, fileResolver)
        patterns.each {
            source.filter.include  it
            sourcePatterns.include it
        }

        allSource = new UnionFileTree(displayString,
            source.matching(sourcePatterns))
    }

    def protected initSourceSet(displayString, fileResolver) {
        new DefaultSourceDirectorySet(displayString, fileResolver)
    }
}
