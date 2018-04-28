package taskly.system.notification;

import org.jooq.lambda.Unchecked;

import java.util.stream.Collectors;
import java.util.stream.Stream;


public final class NotificationMessageFromObject {

    private final Object object;

    public NotificationMessageFromObject(final Object object) {
        this.object = object;
    }

    @Override
    public String toString() {
        String[] fieldNames = object.getClass().getAnnotation(AsNotification.class).of();
        return String.join(" ", Stream.of(fieldNames)
                .map(it -> Unchecked.supplier(() -> object.getClass().getDeclaredField(it)).get())
                .peek(it -> it.setAccessible(true))
                .map(it -> Unchecked.supplier(() -> it.get(object)).get().toString())
                .collect(Collectors.toList()));
    }
}
