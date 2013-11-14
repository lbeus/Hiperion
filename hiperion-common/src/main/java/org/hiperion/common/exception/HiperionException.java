package org.hiperion.common.exception;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 13.03.13.
 * Time: 18:03
 */
public class HiperionException extends Exception {

    public HiperionException() {
        super();
    }

    public HiperionException(String message) {
        super(message);
    }

    public HiperionException(Throwable throwable) {
        super(throwable);
    }

    public HiperionException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
