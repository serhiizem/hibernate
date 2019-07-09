package org.hibernate.utils;

import java.util.function.Supplier;

@FunctionalInterface
public interface VoidSupplier extends Supplier<Void> {

    void operation();

    @Override
    default Void get() {
        operation();
        return null;
    }
}
