package taskly.system.notification

import com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component

@Aspect
@Component
open class NotificationAspect {

    @Pointcut("within(@org.springframework.stereotype.Repository *)")
    fun repositoryClassMethods() {}

    @Before("repositoryClassMethods()")
    fun beforeSampleCreation() {
        println("aspect code")
    }
}