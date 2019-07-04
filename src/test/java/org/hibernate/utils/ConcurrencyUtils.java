package org.hibernate.utils;

import java.util.concurrent.*;

public class ConcurrencyUtils {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r);
        t.setName("Test");
        return t;
    });

    public void executeSync(VoidCallable callable) {
        try {
            Future<Void> future = executorService.submit(callable);
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T executeSync(Callable<T> callable) {
        try {
            return executorService.submit(callable).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
