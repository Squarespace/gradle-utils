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
 * A <code>Delay</code> allows to delay a computation until a later point
 * in the future. It is a well known concept from more functional oriented
 * languages. For example in Scheme this construct is called a
 * <code>promise</code>. This implementation is closely inspired by Clojure's
 * <code>delay</code>.
 *
 * @author Meikel Brandmeyer
 */
final class Delay {
    def private Closure fn
    def private exc = null
    def private value = null

    /**
     * Constructs a new <code>Delay</code> which will produce the result
     * value of the provided <code>Closure</code> <code>f</code>.
     *
     * @param  f the closure which captures the computation
     * @return   a new <code>Delay</code>
     */
    Delay(Closure f) {
        fn = f
    }

    /**
     * Forces the computation to happen caching the result. In particular
     * any computation happens only once! In case an <code>Exception</code>
     * was thrown during the execution of the computation the exception is
     * rethrown on the current thread.
     *
     * This is a low-level function. You should use <code>Delay.force</code>
     * instead.
     *
     * @return    the result of the delayed computation.
     */
    def synchronized doForce() {
        if (fn != null) {
            try {
                value = fn()
            } catch (Exception e) {
                exc = e
            } finally {
                fn = null
            }
        }

        if (exc != null)
            throw exc

        value
    }

    /**
     * Forces the delayed compatution. Acts as the identity in case the * given
     * object is not a <code>Delay</code>. Should the produced value be another
     * <code>Delay</code> forcing is continued recursively. Should an exception
     * be thrown by the delayed computation it is rethrown to be handled by the
     * calling code.
     *
     * @param  o an arbitrary <code>Object</code>
     * @return   the delayed value in case <code>o</code> is a <code>Delay</code>, <code>o</code> itself otherwise
     */
    def static force(o) {
        while (o instanceof Delay) {
            o = o.doForce()
        }
        o
    }
}
