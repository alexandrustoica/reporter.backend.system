package taskly.system.report

import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.function.Executable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import taskly.system.user.User
import taskly.system.user.UserRepository
import taskly.system.user.UserService

@DataJpaTest
@ExtendWith(SpringExtension::class)
@EnableJpaRepositories("taskly.system.section",
        "taskly.system.report", "taskly.system.user")
@ComponentScan("taskly.system.section", "taskly.system.report",
        "taskly.system.user", "taskly.system.security")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(locations = ["classpath:integrationtests.properties"])
public class UserServiceTest {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var service: UserService

    @Test
    fun whenSavingUser_WithValidUser_ExpectUser() {
        // given:
        val subject = User()
        // when:
        val result = service.save(subject)
        // then:
        assertThat(result, `is`(equalTo(userRepository.findAll().first())))
    }

    @Test
    fun whenDeletingUser_WithValidUser_ExpectUserDeleted() {
        // given:
        val subject = User().let { userRepository.save(it) }
        // when:
        val result = service.delete(subject)
        // then:
        assertAll(Executable { assertThat(userRepository.findAll().count(), `is`(0)) },
                Executable { assertThat(result, `is`(equalTo(subject.copy(id = 1)))) })
    }

    @Test
    fun whenDeletingUser_WithValidId_ExpectUserDeleted() {
        // given:
        val subject = User().let { userRepository.save(it) }
        // when:
        val result = service.delete(subject.id)
        // then:
        assertAll(Executable { assertThat(userRepository.findAll().count(), `is`(0)) },
                Executable { assertThat(result, `is`(equalTo(subject.copy(id = 1)))) })
    }

    @Test
    fun whenDeletingUser_WithInvalidId_ExpectNullValue() {
        // given:
        User().let { userRepository.save(it) }
        // when:
        val result = service.delete(100)
        // then:
        assertThat(result, `is`(nullValue()))
    }

    @Test
    fun whenUpdatingUser_WithValidUser_ExpectUserUpdated() {
        // given:
        val user = User().let { userRepository.save(it) }
        val subject = user.copy(name = "Update")
        // when:
        val result = service.update(subject)
        // then:
        assertThat(result?.name, `is`("Update"))
    }
}