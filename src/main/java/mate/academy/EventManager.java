package mate.academy;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private final List<EventListener> listeners = new CopyOnWriteArrayList<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public void registerListener(EventListener listener) {
        if (Objects.nonNull(listener) && !executorService.isShutdown()) {
            listeners.add(listener);
        }
    }

    public void deregisterListener(EventListener listener) {
        if (Objects.nonNull(listener) && !executorService.isShutdown()) {
            listeners.remove(listener);
        }
    }

    public void notifyEvent(Event event) {
        if (Objects.nonNull(event) && !executorService.isShutdown()) {
            for (EventListener listener : listeners) {
                executorService.submit(() -> {
                    try {
                        listener.onEvent(event);
                    } catch (Exception e) {
                        System.out.println("Error occurred in "
                                + Thread.currentThread().getName()
                                + ": "
                                + e.getMessage());
                    }
                });
            }
        }
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
