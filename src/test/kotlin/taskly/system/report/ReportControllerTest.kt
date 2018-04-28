package taskly.system.report

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
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import taskly.system.section.CriticalSectionSensor
import taskly.system.user.User
import taskly.system.user.UserService


@RunWith(SpringRunner::class)
@WebMvcTest(ReportController::class)
class ReportControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var reportService: ReportService

    @MockBean
    private lateinit var criticalSectionSensor: CriticalSectionSensor

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
    fun whenSavingReport_WithValidReport_ExpectReportSaved() {
        // given:
        val user = User().also { loginWith(it) }
        val subject = Report()
        // when:
        val result = subject.copy(user = user, id = 1)
        `when`(reportService.save(any())).thenReturn(result)
        `when`(userService.getById(user.id)).thenReturn(user)
        // then:
        mockMvc.perform(post("/reports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(subject)))
                .andExpect(status().isAccepted)
                .andExpect(content().json(mapper.writeValueAsString(result)))
    }

    @Test
    fun whenSavingReport_WithUserNotLoggedIn_ExpectReportNotSaved() {
        // given:
        val user = User().also { loginWith(it) }
        val subject = Report()
        // when:
        `when`(reportService.save(any())).thenReturn(null)
        `when`(userService.getById(user.id)).thenReturn(null)
        // then:
        mockMvc.perform(post("/reports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(subject)))
                .andExpect(status().isNotFound)
    }

    @Test
    fun whenSavingReport_WithInvalidData_ExpectReportNotSaved() {
        // given:
        val user = User().also { loginWith(it) }
        val subject = Report()
        // when:
        `when`(reportService.save(any())).thenReturn(null)
        `when`(userService.getById(user.id)).thenReturn(user)
        // then:
        mockMvc.perform(post("/reports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(subject)))
                .andExpect(status().isNotAcceptable)
    }

    @Test
    fun whenSavingReport_WithValidData_ExpectCriticalSensorCalled() {
        // given:
        val user = User().also { loginWith(it) }
        val subject = Report()
        // when:
        val result = subject.copy(user = user, id = 1)
        `when`(reportService.save(any())).thenReturn(result)
        `when`(userService.getById(user.id)).thenReturn(user)
        mockMvc.perform(post("/reports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(subject)))
        // then:
        verify(criticalSectionSensor, times(1))
                .criticalSectionFormedAt(subject.location,
                        ReportController.SystemConstants.radiusOfCriticalSection,
                        ReportController.SystemConstants.sizeOfCriticalSection)
    }

    @Test
    fun whenUpdatingReport_WithValidReport_ExpectReportUpdated() {
        // given:
        val user = User().also { loginWith(it) }
        val subject = Report()
        // when:
        val result = subject.copy(user = user, id = 1, title = "updated")
        `when`(reportService.save(any())).thenReturn(result)
        `when`(userService.getById(user.id)).thenReturn(user)
        // then:
        mockMvc.perform(put("/reports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(result)))
                .andExpect(status().isAccepted)
                .andExpect(content().json(mapper.writeValueAsString(result)))
    }

    @Test
    fun whenUpdatingReport_WithReportNotFound_ExpectReportNotFound() {
        // given:
        val user = User().also { loginWith(it) }
        val subject = Report()
        // when:
        subject.copy(user = user, id = 1, title = "updated")
        `when`(reportService.save(any())).thenReturn(null)
        `when`(userService.getById(user.id)).thenReturn(user)
        // then:
        mockMvc.perform(put("/reports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(subject)))
                .andExpect(status().isNotAcceptable)
    }

    @Test
    fun whenDeletingReport_WithReportInDatabase_ExpectReportDeleted() {
        // given:
        val user = User().also { loginWith(it) }
        val subject = Report().copy(user = user, id = 1)
        // when:
        `when`(reportService.delete(subject)).thenReturn(subject)
        `when`(reportService.findById(subject.id)).thenReturn(subject)
        `when`(userService.getById(user.id)).thenReturn(user)
        // then:
        mockMvc.perform(delete("/reports/${subject.id}"))
                .andExpect(status().isAccepted)
                .andExpect(content().json(mapper.writeValueAsString(subject)))
    }

    @Test
    fun getReportById() {
        // given:
        // when:
        // then:
    }

    @Test
    fun getPhotosFromReport() {
        // given:
        // when:
        // then:
    }

    @Test
    fun getAllReportsFromCurrentUser() {
        // given:
        // when:
        // then:
    }

    @Test
    fun getReportsFromLatestWeekFromCurrentUser() {
        // given:
        // when:
        // then:
    }

    @Test
    fun markReportAsSolved() {
        // given:
        // when:
        // then:
    }

    @Test
    fun markReportAsSpam() {
        // given:
        // when:
        // then:
    }
}