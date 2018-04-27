package taskly.system.notification;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TestAspect {


    @Before("execution(* *..findByUser*(*, *))")
    public void test() {
        System.out.println("MUIE C# FROM ASPECT");
    }
}
