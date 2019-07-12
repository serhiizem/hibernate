package org.hibernate.utils;

import lombok.SneakyThrows;
import org.junit.jupiter.api.function.Executable;

@FunctionalInterface
public interface CauseRethrowingExecutable extends Executable {

    void action();

    @Override
    @SneakyThrows
    default void execute() {
        try {
            action();
        } catch (Exception e) {
            throw e.getCause();
        }
    }
}
