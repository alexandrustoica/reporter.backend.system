package taskly.system.user

import org.junit.runner.RunWith
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Test

import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@RunWith(SpringRunner::class)
@WebMvcTest(UserController::class)
class UserControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var userService: UserService

    @MockBean
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var mapper: ObjectMapper

    // This function is needed in order for Mockito.any to work in Kotlin
    private fun <T> any(): T = Mockito.any<T>().let { null as T }

    @Test
    fun whenRegisteringWithPoliceAccount_ValidPoliceMan_ExpectUserRegistered() {
        // given:
        val subject = User().copy(email = "test.test@test.com")
        // then:
        val result: User? = subject.copy(role = UserRole.POLICE)
        `when`(userService.save(any())).thenReturn(result)
        // then:
        mockMvc.perform(post("/users/police")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(subject)))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(content().json(mapper.writeValueAsString(result)))

    }
}