package taskly.system.event;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import taskly.system.event.BroadcastEvent;
import taskly.system.event.BroadcastedEvent;
import taskly.system.event.EventType;
import taskly.system.event.WebSocketHandler;

@Aspect
@Component
public class EventAspect {

    private final WebSocketHandler handler;

    @Autowired
    public EventAspect(final WebSocketHandler handler) {
        this.handler = handler;
    }

    @Autowired
    private TaskExecutor taskExecutor;

    @AfterReturning(
            pointcut = "execution(* *(..)) && @annotation(taskly.system.event.BroadcastEvent)",
            returning = "result")
    public void broadcastEvent(
            final JoinPoint joinPoint, final Object result) {
        taskExecutor.execute(() -> {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            EventType type = signature.getMethod().getAnnotation(BroadcastEvent.class).type();
            handler.send(new BroadcastedEvent<>(type, result));
        });
    }
}
