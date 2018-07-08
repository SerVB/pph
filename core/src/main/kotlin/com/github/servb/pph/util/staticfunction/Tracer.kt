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
