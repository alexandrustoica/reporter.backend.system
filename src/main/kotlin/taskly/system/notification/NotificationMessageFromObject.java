package taskly.system.notification;

import org.jooq.lambda.Unchecked;

import java.util.stream.Stream;


public final class NotificationMessageFromObject {

    private final Object object;

    public NotificationMessageFromObject(final Object object) {
        this.object = object;
    }

    @Override
    public String toString() {
        String[] fieldNames = object.getClass().getAnnotation(AsNotification.class).of();
        return Stream.of(fieldNames)
                .map(it -> Unchecked.supplier(() -> object.getClass().getDeclaredField(it)).get())
                .peek(it -> it.setAccessible(true))
                .map(it -> (String) Unchecked.supplier(() -> it.get(object)).get())
                .reduce("", (acc, it) -> it + " " + acc);
    }
}
