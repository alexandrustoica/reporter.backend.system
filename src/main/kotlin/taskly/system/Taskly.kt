package taskly.system

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

@SpringBootApplication
@EnableAutoConfiguration
class Taskly

fun main(args: Array<String>) {
    SpringApplication.run(Taskly::class.java, *args)
}