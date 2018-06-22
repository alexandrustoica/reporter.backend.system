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
    private ExpoNotificationService expoNotificationService;

    @Autowired
    private NotificationService notificationService;

    @AfterReturning(
            pointcut = "execution(* *(..)) && @annotation(SendNotification)",
            returning = "result")
    public ExpoNotificationService.DataExpoNotification sendNotification (
            final JoinPoint joinPoint, final Object result) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String message = signature.getMethod().getAnnotation(SendNotification.class).value();
        return (result != null) ? sendNotificationToUser(message,
                new NotificationMessageFromObject(result).toString()) : null;
    }

    private ExpoNotificationService.DataExpoNotification sendNotificationToUser(
            final String messageTemplate, final String result) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String finalMessage = MessageFormat.format(messageTemplate, result);
        Notification notification = new Notification("Notification", finalMessage, user);
        notificationService.save(notification);
        return !user.getExpoNotificationToken().equals("") ?
                expoNotificationService.send(notification) : null;
    }

}
