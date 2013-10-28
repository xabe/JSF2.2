package com.prueba.util.exceptions;

public final class Exceptions {

    private Exceptions() {

    }

    public static <T extends Throwable> Throwable unwrap(Throwable exception, Class<T> type) {
            while (type.isInstance(exception) && exception.getCause() != null) {
                    exception = exception.getCause();
            }

            return exception;
    }

    public static <T extends Throwable> boolean is(Throwable exception, Class<T> type) {
        for (;exception != null; exception = exception.getCause()) {
            if (type.isInstance(exception)) {
                return true;
            }
        }

        return false;
    }

}

