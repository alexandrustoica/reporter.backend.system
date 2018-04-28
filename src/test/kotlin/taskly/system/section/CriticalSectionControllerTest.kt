package taskly.system.section

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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import taskly.system.report.Location
import taskly.system.user.User
import taskly.system.user.UserRepository

@RunWith(SpringRunner::class)
@WebMvcTest(CriticalSectionController::class)
class CriticalSectionControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var criticalService: CriticalSectionService

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
    fun whenGettingAllCriticalLocations_ValidArea_ExpectCriticalLocations() {
        // given:
        User().also { loginWith(it) }
        val result = listOf(CriticalSection(Location(0.0, 0.0)),
                CriticalSection(Location(0.01, 0.01)),
                CriticalSection(Location(0.02, 0.02)))
        val subject = Area(Location(0.0, 0.0), radius = 2000.0)
        // when:
        `when`(criticalService.findAllCriticalSectionsNear(
                subject.origin, subject.radius)).thenReturn(result)
        // then:
        mockMvc.perform(post("/sections/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(subject)))
                .andExpect(status().isOk)
                .andExpect(content().json(mapper.writeValueAsString(result)))
    }
}