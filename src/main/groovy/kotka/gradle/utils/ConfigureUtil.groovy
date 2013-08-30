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

/**
 * A utility class to help configuring objects.
 *
 * @author Meikel Brandmeyer
 */
class ConfigureUtil {
    /**
     * Configures the <code>target</code> object. It sets it as the delegate
     * of the <code>configureFn</code> closure and calls the latter. Passes
     * the target object also as first argument to the closure should it
     * support it.
     *
     * @param  target      the object to configure
     * @param  configureFn a closure to be executed with <code>target</code> as delegate
     * @return             <code>target</code>
     */
    static configure(Object target, Closure configureFn={}) {
        def fn = configureFn.clone()

        fn.with {
            resolveStrategy = Closure.DELEGATE_FIRST
            delegate        = target
        }

        if (fn.maximumNumberOfParameters == 0)
            fn.call()
        else
            fn.call(target)

        target
    }
}
