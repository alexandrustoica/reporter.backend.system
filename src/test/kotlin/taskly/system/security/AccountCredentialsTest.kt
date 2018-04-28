package taskly.system.security

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.both
import org.junit.Assert.*
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.function.Executable

class AccountCredentialsTest {

    @Test
    fun whenCreatingAccountCredentials_WithValidData_ExpectValidCredentials() {
        // given & when:
        val subject = AccountCredentials(
                username = "test",
                password = "password")
        // then:
        assertAll(Executable { assertThat(subject.username, `is`("test")) },
                Executable { assertThat(subject.password, `is`("password")) })
    }

    @Test
    fun whenCreatingAccountCredentials_WithEmptyData_ExpectEmptyData() {
        // given & when:
        val result = AccountCredentials()
        // then:
        assertThat("", both(`is`(result.username)).and(`is`(result.password)))
    }
}