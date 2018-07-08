/*
 * Copyright 2018 SerVB.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.servb.pph.util.staticfunction

/**
 * Tracer of possible errors.
 * TODO: Write a normal trace!
 *
 * @author SerVB
 */
object Tracer {

    /**
     * Checks if it's an error. Traces if there is an error.
     *
     * @param noError True if no error, false otherwise.
     */
    fun check(noError: Boolean) {
        if (noError == false) {
            doThrow("An error detected!!!")
        }
    }

    /**
     * Checks if it's an error. Traces if xx == 0.
     *
     * @param xx 0 if there is an error.
     */
    fun check(xx: Int) {
        check(xx != 0)
    }

    /**
     * Traces anyway.
     *
     * @param msg Error message.
     */
    fun check(msg: String) {
        doThrow(msg)
    }

    /**
     * Throws exception anyway.
     *
     * @param msg Error message.
     */
    private fun doThrow(msg: String) {
        throw IllegalStateException("fatal > tracer: $msg")
    }
}