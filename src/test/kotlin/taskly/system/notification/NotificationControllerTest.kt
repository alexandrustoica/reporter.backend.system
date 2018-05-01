package taskly.system.notification

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import taskly.system.report.*
import taskly.system.section.CriticalSectionSensor
import taskly.system.user.User
import taskly.system.user.UserService
import java.io.File


@RunWith(SpringRunner::class)
@WebMvcTest(NotificationController::class)
class NotificationControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var notificationService: NotificationService

    @MockBean
    private lateinit var userService: UserService

    @Autowired
    private lateinit var mapper: ObjectMapper

    // This function is needed in order for Mockito.any to work in Kotlin
    @Suppress("UNCHECKED_CAST")
    private fun <T> any(): T = Mockito.any<T>().let { null as T }

    private fun loginWith(user: User) {
        SecurityContextHolder.getContext().authentication =
                UsernamePasswordAuthenticationToken(user, user.authorities)
    }

    @Test
    fun whenMarkingNotificationAsRead_WithValidNotification_ExpectNotificationMarked() {
        // given:
        val user = User().also { loginWith(it) }
        val notification = Notification("title", "message", user)
        val result = notification.copy(isRead = true)
        // when:
        `when`(notificationService.getNotificationById(notification.id)).thenReturn(notification)
        `when`(notificationService.markNotificationAsRead(notification.id)).thenReturn(result)
        // then:
        mockMvc.perform(put("/notifications/${notification.id}"))
                .andExpect(status().isAccepted)
                .andExpect(content().json(mapper.writeValueAsString(result)))
    }

    @Test
    fun whenMarkingNotificationAsRead_WithInvalidNotification_ExpectNotFound() {
        // given
        val user = User().also { loginWith(it) }
        val notification = Notification("title", "message", user)
        // when:
        `when`(notificationService.getNotificationById(notification.id)).thenReturn(notification)
        `when`(notificationService.markNotificationAsRead(notification.id)).thenReturn(null)
        // then:
        mockMvc.perform(put("/notifications/${notification.id}"))
                .andExpect(status().isNotFound)
    }

    @Test
    fun whenMarkingNotificationAsRead_WithInvalidUser_ExpectNotAcceptable() {
        // given
        val user = User().also { loginWith(it) }
        val notification = Notification("title", "message", user.copy(id = 1))
        // when:
        `when`(notificationService.getNotificationById(notification.id)).thenReturn(notification)
        // then:
        mockMvc.perform(put("/notifications/${notification.id}"))
                .andExpect(status().isNotAcceptable)
    }

    @Test
    fun whenGettingAllNotificationsFromCurrentUser_WithValidCurrentUser_ExpectNotifications() {
        // given
        val user = User().also { loginWith(it) }
        val subject = listOf(Notification("title", "message", user),
                Notification("title", "message", user))
        val result = PageImpl<Notification>(subject)
        // when:
        `when`(notificationService
                .findLatestNotificationsFor(user, PageRequest(0, 10)))
                .thenReturn(result)
        // then:
        mockMvc.perform(get("/notifications/${0}/${10}"))
                .andExpect(status().isOk)
                .andExpect(content().json(mapper.writeValueAsString(result)))
    }

    @Test
    fun whenSavingNotificationTokenForUser_WithValidUser_ExpectTokenSaved() {
        // given
        val user = User().also { loginWith(it) }
        val result = user.copy(expoNotificationToken = "token")
        // when:
        `when`(userService.getById(user.id)).thenReturn(user)
        `when`(userService.save(any())).thenReturn(result)
        // then:
        mockMvc.perform(put("/notifications/expo/${result.expoNotificationToken}"))
                .andExpect(status().isAccepted)
                .andExpect(content().json(mapper.writeValueAsString(result)))
    }
}