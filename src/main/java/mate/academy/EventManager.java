package mate.academy;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {
    private final Set<EventListener> listeners = new CopyOnWriteArraySet<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public void registerListener(EventListener listener) {
        if (Objects.isNull(listener)) {
            throw new IllegalArgumentException("Listener can't be null");
        }
        if (executorService.isShutdown()) {
            throw new IllegalStateException("Event manager is shutdown");
        }
        listeners.add(listener);
    }

    public void deregisterListener(EventListener listener) {
        if (Objects.isNull(listener)) {
            throw new IllegalArgumentException("Listener can't be null");
        }
        if (executorService.isShutdown()) {
            throw new IllegalStateException("Event manager is shutdown");
        }
        listeners.remove(listener);
    }

    public void notifyEvent(Event event) {
        if (Objects.isNull(event)) {
            throw new IllegalArgumentException("Event can't be null");
        }
        if (executorService.isShutdown()) {
            throw new IllegalStateException("Event manager is shutdown");
        }
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

    public void shutdown() {
        executorService.shutdown();
    }
}
