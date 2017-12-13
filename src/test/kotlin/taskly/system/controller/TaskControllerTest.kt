package taskly.system.controller

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.test.context.junit.jupiter.SpringExtension
import taskly.system.Taskly
import taskly.system.repository.TaskRepository


/**
 * @author Alexandru Stoica
 * @version 1.0
 */

@DataJpaTest()
@ExtendWith(SpringExtension::class)
@DisplayName("Test")
@AutoConfigureTestEntityManager
@AutoConfigureDataJpa
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
@SpringBootTest(classes = arrayOf(Taskly::class))
@EnableJpaRepositories("task.system.repository")
@ComponentScan("task.system")
class TaskControllerTest {

    @Autowired
    private lateinit var taskRepository: TaskRepository

    @Test
    internal fun start() {
        println(taskRepository)
        assertThat(taskRepository, IsNull.notNullValue())
    }
}