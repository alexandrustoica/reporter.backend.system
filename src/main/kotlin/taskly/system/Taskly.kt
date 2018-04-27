package taskly.system

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableAutoConfiguration
class Taskly

fun main(args: Array<String>) {
    SpringApplication.run(Taskly::class.java, *args)
}
