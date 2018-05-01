package taskly.system.report

import org.omg.CosNaming.NamingContextPackage.NotFound
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore
import taskly.system.section.CriticalSectionSensor
import taskly.system.user.User
import taskly.system.user.UserNotFound
import taskly.system.user.UserRepository
import taskly.system.user.UserService
import java.util.*
import java.util.Calendar.DAY_OF_YEAR
import javax.xml.ws.Response


@RestController
@RequestMapping("/reports")
class ReportController {

    @Autowired
    private lateinit var reportService: ReportService

    @Autowired
    private lateinit var criticalSectionSensor: CriticalSectionSensor

    @Autowired
    private lateinit var userService: UserService

    companion object SystemConstants {
        const val radiusOfCriticalSection: Double = 2000.0
        const val sizeOfCriticalSection: Int = 5
    }

    @ResponseBody
    @PostMapping("")
    @Secured(value = ["ROLE_USER", "ROLE_POLICE"])
    fun insert(@AuthenticationPrincipal @ApiIgnore user: User,
               @RequestBody report: Report): ResponseEntity<Report> {
        val result = reportService.save(report.copy(user = getUserById(user.id)))
        criticalSectionSensor.criticalSectionFormedAt(report.location,
                radiusOfCriticalSection,
                sizeOfCriticalSection)
        return result?.let { ResponseEntity(it, HttpStatus.ACCEPTED) }
                ?: ResponseEntity(HttpStatus.NOT_ACCEPTABLE)
    }

    @ResponseBody
    @PutMapping("")
    @Secured(value = ["ROLE_USER", "ROLE_POLICE"])
    fun update(@AuthenticationPrincipal @ApiIgnore user: User,
               @RequestBody report: Report): ResponseEntity<Report> =
            insert(user, report)

    @ResponseBody
    @DeleteMapping("/{id}")
    @Secured(value = ["ROLE_USER", "ROLE_POLICE"])
    fun delete(@AuthenticationPrincipal @ApiIgnore user: User,
               @PathVariable id: Int): ResponseEntity<Report?> {
        val reportFromDatabase = reportService.findById(id)
        val report = if (reportFromDatabase.user == getUserById(user.id))
            reportService.delete(reportFromDatabase) else null
        report?.let {
            criticalSectionSensor.deleteIfNeedCriticalSectionFrom(it.location,
                    radiusOfCriticalSection, sizeOfCriticalSection)
        }
        return report?.let { ResponseEntity(it, HttpStatus.ACCEPTED) }
                ?: ResponseEntity(HttpStatus.UNAUTHORIZED)
    }

    @ResponseBody
    @GetMapping("/{id}")
    @Secured(value = ["ROLE_USER", "ROLE_POLICE"])
    fun getReportById(
            @AuthenticationPrincipal @ApiIgnore user: User,
            @PathVariable id: Int): ResponseEntity<Report> =
            reportService.getById(id)
                    ?.let{ ResponseEntity(it, HttpStatus.ACCEPTED) }
                    ?: ResponseEntity(HttpStatus.NOT_FOUND)

    @ResponseBody
    @GetMapping("/{id}/photos")
    @Secured(value = ["ROLE_USER", "ROLE_POLICE"])
    fun getPhotosFromReport(
            @AuthenticationPrincipal @ApiIgnore user: User,
            @PathVariable id: Int): List<Photo> =
            reportService.getPhotosFromPhoto(id)

    @ResponseBody
    @GetMapping("/{page}/{size}")
    @Secured(value = ["ROLE_USER", "ROLE_POLICE"])
    fun getAllReportsFromCurrentUser(
            @AuthenticationPrincipal @ApiIgnore user: User,
            @PathVariable page: Int,
            @PathVariable size: Int): Page<Report> =
            reportService.findByUser(getUserById(user.id), PageRequest(page, size))

    @ResponseBody
    @GetMapping("/latest")
    @Secured(value = ["ROLE_USER", "ROLE_POLICE"])
    fun getReportsFromLatestWeekFromCurrentUser(
            @AuthenticationPrincipal @ApiIgnore user: User): List<Report> {
        val dateFrom7DaysAgo = Calendar.getInstance()
                .also { it.add(DAY_OF_YEAR, -7) }
        return reportService.findByUserAndDateAfter(user,
                dateFrom7DaysAgo, PageRequest(0, 1000))
    }

    @ResponseBody
    @PutMapping("/{id}/solved")
    @Secured(value = ["ROLE_POLICE"])
    fun markReportAsSolved(
            @PathVariable id: Int,
            @AuthenticationPrincipal @ApiIgnore user: User):
            ResponseEntity<Report> =
            reportService.markReportAsSolved(id)?.let {
                ResponseEntity(it, HttpStatus.ACCEPTED)
            } ?: ResponseEntity(HttpStatus.NOT_FOUND)

    @ResponseBody
    @PutMapping("/{id}/spam")
    @Secured(value = ["ROLE_POLICE"])
    fun markReportAsSpam(
            @PathVariable id: Int,
            @AuthenticationPrincipal @ApiIgnore user: User):
            ResponseEntity<Report> =
            reportService.markReportAsSpam(id)?.let {
                ResponseEntity(it, HttpStatus.ACCEPTED)
            } ?: ResponseEntity(HttpStatus.NOT_FOUND)

    private fun getUserById(id: Int): User =
            userService.getById(id) ?: throw UserNotFound()
}