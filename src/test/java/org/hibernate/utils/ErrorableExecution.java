package org.hibernate.utils;

import javax.persistence.EntityManager;
import java.util.function.Consumer;

@FunctionalInterface
public interface ErrorableExecution<T> extends Consumer<EntityManager> {
}
