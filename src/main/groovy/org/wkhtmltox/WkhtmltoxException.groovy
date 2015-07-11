package org.wkhtmltox

/**
 * @author tobiasnendel
 */
class WkhtmltoxException extends RuntimeException {

    WkhtmltoxException(String s) {
        super(s)
    }

    WkhtmltoxException(Throwable throwable) {
        super(throwable)
    }
}
