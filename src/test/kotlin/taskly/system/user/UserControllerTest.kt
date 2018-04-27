package taskly.system.user

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test
import org.mockito.Matchers.*

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

    @Test
    fun whenRegisteringWithPoliceAccount_ValidPoliceMan_ExpectUserRegistered() {
        // given:
        val subject = User().copy(email = "test.test@test.com")
        // then:
        val result: User? = subject.copy(role = UserRole.POLICE)
        `when`(userService.save(anyObject())).thenReturn(result)
        // then:
        mockMvc.perform(post("/users/police")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(subject)))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(result)))

    }
}