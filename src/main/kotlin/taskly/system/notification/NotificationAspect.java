package taskly.system.notification;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import taskly.system.user.User;
import java.text.MessageFormat;

@Aspect
@Component
public class NotificationAspect {

    @Autowired
    private ExpoNotificationService notificationService;

    @AfterReturning(
            pointcut = "execution(public * *(..)) && @annotation(SendNotification)",
            returning = "result")
    public void sendNotification(JoinPoint joinPoint, Object result){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String object = new NotificationMessageFromObject(result).toString();
        String message = signature.getMethod().getAnnotation(SendNotification.class).value();
        String finalMessage = MessageFormat.format(message, object);
        notificationService.send(new Notification("Notification", finalMessage, user));
    }
}
