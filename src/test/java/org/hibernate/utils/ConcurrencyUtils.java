package org.hibernate.utils;

import lombok.SneakyThrows;

import java.util.concurrent.*;

public class ConcurrencyUtils {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r);
        t.setName("Test");
        return t;
    });

    @SneakyThrows
    @SuppressWarnings("UnusedReturnValue")
    public <T> T waitForFutureResult(Future<T> future, TimeUnit timeUnit, long amount) throws TimeoutException {
        return future.get(amount, timeUnit);
    }

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

    @SneakyThrows
    public void wait(TimeUnit unit, long amount) {
        unit.sleep(amount);
    }
}
