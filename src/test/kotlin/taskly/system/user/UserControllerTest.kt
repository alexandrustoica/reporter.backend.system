package taskly.system.user

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@RunWith(SpringRunner::class)
@WebMvcTest(UserController::class)
class UserControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var userService: UserService

    @Autowired
    private lateinit var mapper: ObjectMapper

    // This function is needed in order for Mockito.any to work in Kotlin
    private fun <T> any(): T = Mockito.any<T>().let { null as T }

    private fun loginWith(user: User) {
        SecurityContextHolder.getContext().authentication =
                UsernamePasswordAuthenticationToken(user, user.authorities)
    }

    @Test
    fun whenRegisteringWithPoliceAccount_ValidPoliceMan_ExpectUserRegistered() {
        // given:
        val subject = User()
        // then:
        val result: User? = subject.copy(role = UserRole.POLICE)
        `when`(userService.save(any())).thenReturn(result)
        // then:
        mockMvc.perform(post("/users/police")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(subject)))
                .andExpect(status().isAccepted)
                .andExpect(content().json(mapper.writeValueAsString(result)))
    }

    @Test
    fun whenRegisteringWithPoliceUser_WithInvalidData_ExpectPoliceNotRegistered() {
        // given:
        val subject = User()
        // then:
        `when`(userService.save(any())).thenReturn(null)
        // then:
        mockMvc.perform(post("/users/police")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(subject)))
                .andExpect(status().isNotAcceptable)
    }

    @Test
    fun whenRegisteringUser_WithInvalidData_ExpectUserNotRegistered() {
        // given:
        val subject = User()
        // then:
        `when`(userService.save(any())).thenReturn(null)
        // then:
        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(subject)))
                .andDo(print())
                .andExpect(status().isNotAcceptable)
    }

    @Test
    fun whenRegisteringAsNormalUser_WithValidData_ExpectUserRegistered() {
        // given:
        val subject = User()
        // when:
        val result = subject.copy(id = 1)
        `when`(userService.save(any())).thenReturn(result)
        // then:
        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(subject)))
                .andExpect(status().isAccepted)
                .andExpect(content().json(mapper.writeValueAsString(result)))
    }

    @Test
    fun whenDeletingAUser_WithSameUserLoggedIn_ExpectUserDeleted() {
        // given:
        val subject = User().also { loginWith(it) }
        // when:
        `when`(userService.delete(subject.id)).thenReturn(subject)
        // then:
        mockMvc.perform(delete("/users/"))
                .andExpect(status().isAccepted)
                .andExpect(content().json(mapper.writeValueAsString(subject)))
    }

    @Test
    fun whenDeletingAUserById_WithAnotherUserLoggedIn_ExpectUserNotDeleted() {
        // given:
        val subject = User().also { loginWith(it) }
        // when:
        `when`(userService.delete(subject.id)).thenReturn(null)
        // then:
        mockMvc.perform(delete("/users/"))
                .andExpect(status().isNotAcceptable)
    }

    @Test
    fun whenGettingTheCurrentlyLoggedUser_ExpectTheCurrentlyLoggedUser() {
        // given:
        val subject = User().also { loginWith(it) }
        // when:
        `when`(userService.getById(subject.id)).thenReturn(subject)
        // then:
        mockMvc.perform(get("/users/current"))
                .andExpect(status().isAccepted)
                .andExpect(content().json(mapper.writeValueAsString(subject)))
    }

    @Test
    fun whenGettingTheCurrentlyLoggedUser_UserDeleted_ExpectNotFoundResponse() {
        // given:
        val subject = User().also { loginWith(it) }
        // when:
        `when`(userService.getById(subject.id)).thenReturn(null)
        // then:
        mockMvc.perform(get("/users/current"))
                .andExpect(status().isNotFound)
    }

    @Test
    fun whenUpdatingUser_WithUserLoggedIn_ExpectUserUpdated() {
        // given:
        val subject = User().also { loginWith(it) }
        // when:
        val result = subject.copy(username = "updated")
        `when`(userService.update(any())).thenReturn(result)
        // then:
        mockMvc.perform(put("/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(result)))
                .andExpect(status().isAccepted)
                .andExpect(content().json(mapper.writeValueAsString(result)))
    }

    @Test
    fun whenUpdatingUser_WithUserNotCurrent_ExpectUserNotUpdated() {
        // given:
        val subject = User().also { loginWith(it) }
        // when:
        val result = subject.copy(id = 1, username = "updated")
        // then:
        mockMvc.perform(put("/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(result)))
                .andExpect(status().isNotAcceptable)
    }

    @Test
    fun whenUpdatingUser_WithInvalidData_ExpectUserNotUpdated() {
        // given:
        val subject = User().also { loginWith(it) }
        // when:
        val result = subject.copy(username = "updated")
        `when`(userService.update(any())).thenReturn(null)
        // then:
        mockMvc.perform(put("/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(result)))
                .andExpect(status().isNotFound)
    }

}
