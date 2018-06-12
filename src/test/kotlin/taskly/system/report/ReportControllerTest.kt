package taskly.system.report

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockito_kotlin.verify
import org.apache.coyote.http11.Constants.a
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Matchers.anyObject
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
import taskly.system.section.CriticalSectionSensor
import taskly.system.user.User
import taskly.system.user.UserService
import java.io.File
import java.util.*


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
    fun whenGettingReportById_WithValidId_ExpectReport() {
        // given:
        val user = User().also { loginWith(it) }
        val subject = Report().copy(user = user, id = 1)
        // when:
        `when`(reportService.getById(subject.id)).thenReturn(subject)
        `when`(userService.getById(user.id)).thenReturn(user)
        // then:
        mockMvc.perform(get("/reports/${subject.id}"))
                .andExpect(status().isAccepted)
                .andExpect(content().json(mapper.writeValueAsString(subject)))
    }

    @Test
    fun whenGettingReportById_WithInvalidId_ExpectReportNotFound() {
        // given:
        val user = User().also { loginWith(it) }
        val subject = Report().copy(user = user, id = 1)
        // when:
        `when`(reportService.getById(subject.id)).thenReturn(null)
        `when`(userService.getById(user.id)).thenReturn(user)
        // then:
        mockMvc.perform(get("/reports/${subject.id}"))
                .andExpect(status().isNotFound)
    }

    @Test
    fun whenGettingPhotosFromReport_WithValidPhotos_ExpectPhotos() {
        // given:
        val photos = listOf(Photo(PhotoAsBytes(File("images/invalid/example_2.png"), "png").value()))
        val user = User().also { loginWith(it) }
        val subject = Report().copy(user = user, id = 1, photos = photos)
        // when:
        `when`(reportService.getPhotosFromPhoto(subject.id)).thenReturn(photos)
        `when`(userService.getById(user.id)).thenReturn(user)
        // then:
        mockMvc.perform(get("/reports/${subject.id}/photos"))
                .andExpect(status().isOk)
                .andExpect(content().json(mapper.writeValueAsString(photos)))
    }

    @Test
    fun whenGettingAllReportsFromCurrentUser_WithValidCurrentUser_ExpectReports() {
        // given:
        val user = User().also { loginWith(it) }
        val subject = listOf(Report().copy(user = user, id = 1),
                Report().copy(user = user, id = 2),
                Report().copy(user = user, id = 3))
        // when:
        val result = PageImpl<Report>(subject)
        `when`(reportService.findByUser(user, PageRequest(0, 3))).thenReturn(result)
        `when`(userService.getById(user.id)).thenReturn(user)
        // then:
        mockMvc.perform(get("/reports/${0}/${3}"))
                .andExpect(status().isOk)
                .andExpect(content().json(mapper.writeValueAsString(result)))
    }

    @Test
    fun whenGettingReportsFromLastWeekFromUser_WithValidUser_ExpectReports() {
        // given:
        val user = User().also { loginWith(it) }
        val subject = listOf(Report().copy(user = user, id = 1),
                Report().copy(user = user, id = 2),
                Report().copy(user = user, id = 3))
        // when:
        `when`(reportService.findByUserAndDateAfter(any(), any(), any()))
                .thenReturn(subject)
        // then:
        mockMvc.perform(get("/reports/latest"))
                .andExpect(status().isOk)
                .andExpect(content().json(mapper.writeValueAsString(subject)))
    }

    @Test
    fun whenMarkingReportAsSolved_WithValidReport_ExpectReportMarked() {
        // given:
        val user = User().also { loginWith(it) }
        val subject = Report().copy(user = user, id = 1)
        val result = subject.copy(isSolved = true)
        // when:
        `when`(reportService.markReportAsSolved(subject.id)).thenReturn(result)
        // then:
        mockMvc.perform(put("/reports/${subject.id}/solved"))
                .andExpect(status().isAccepted)
                .andExpect(content().json(mapper.writeValueAsString(result)))
    }

    @Test
    fun whenMarkingReportAsSpam_WithValidReport_ExpectReportMarked() {
        // given:
        val user = User().also { loginWith(it) }
        val subject = Report().copy(user = user, id = 1)
        val result = subject.copy(isSpam = true)
        // when:
        `when`(reportService.markReportAsSpam(subject.id)).thenReturn(result)
        // then:
        mockMvc.perform(put("/reports/${subject.id}/spam"))
                .andExpect(status().isAccepted)
                .andExpect(content().json(mapper.writeValueAsString(result)))
    }

    @Test
    fun whenMarkingReportAsSpam_WithInvalidReport_ExpectNotFound() {
        // given:
        val user = User().also { loginWith(it) }
        val subject = Report().copy(user = user, id = 1)
        // when:
        `when`(reportService.markReportAsSpam(subject.id)).thenReturn(null)
        // then:
        mockMvc.perform(put("/reports/${subject.id}/spam"))
                .andExpect(status().isNotFound)
    }

    @Test
    fun whenMarkingReportAsSolved_WithInvalidReport_ExpectNotFound() {
        // given:
        val user = User().also { loginWith(it) }
        val subject = Report().copy(user = user, id = 1)
        // when:
        `when`(reportService.markReportAsSolved(subject.id)).thenReturn(null)
        // then:
        mockMvc.perform(put("/reports/${subject.id}/solved"))
                .andExpect(status().isNotFound)
    }
}