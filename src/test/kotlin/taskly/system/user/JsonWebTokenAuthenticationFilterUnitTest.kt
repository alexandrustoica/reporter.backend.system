package taskly.system.user

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.context.WebApplicationContext
import java.io.Serializable


@RunWith(SpringRunner::class)
@WebMvcTest(UserController::class)
class JsonWebTokenAuthenticationFilterUnitTest {

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Autowired
    private lateinit var context: WebApplicationContext

    @MockBean
    private lateinit var userService: UserService

    @MockBean
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    data class UserData(val username: String, val password: String) : Serializable

    @Test
    fun whenAttemptAuthentication_WithValidUser_ExpectSuccess() {
        // given:
        val user = User().copy(username = "test", password = "test")
        `when`(userService.loadUserByUsername("test")).thenReturn(user)
        `when`(userRepository.findByUsername("test")).thenReturn(user)
        // when:
        val data = UserData("test", "test")
        // then:
        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", data.username)
                .param("password", data.password))
                .andDo(print())
                .andExpect(status().isOk)
    }
}