package org.hibernate.utils;

import java.util.concurrent.Callable;

@FunctionalInterface
public interface VoidCallable extends Callable<Void> {

    void execute();

    @Override
    default Void call() {
        execute();
        return null;
    }
}
