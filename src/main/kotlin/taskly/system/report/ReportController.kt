package taskly.system.report

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
import java.util.*
import java.util.Calendar.DAY_OF_YEAR


@RestController
@RequestMapping("/reports")
class ReportController {

    @Autowired
    private lateinit var reportService: ReportService

    @Autowired
    private lateinit var criticalSectionSensor: CriticalSectionSensor

    @Autowired
    private lateinit var userRepository: UserRepository

    private companion object {
        const val radiusOfCriticalSection: Double = 2000.0
        const val sizeOfCriticalSection: Int = 5
    }

    @ResponseBody
    @Secured("ROLE_USER")
    @PostMapping("")
    fun insert(@AuthenticationPrincipal @ApiIgnore user: User,
               @RequestBody report: Report): ResponseEntity<Report?> {
        val result = reportService.save(report.copy(user = getUserById(user.id)))
        criticalSectionSensor.criticalSectionFormedAt(report.location,
                radiusOfCriticalSection,
                sizeOfCriticalSection)
        return result?.let { ResponseEntity(it, HttpStatus.ACCEPTED) }
                ?: ResponseEntity(HttpStatus.UNAUTHORIZED)
    }

    @ResponseBody
    @Secured("ROLE_USER")
    @PutMapping("")
    fun update(@AuthenticationPrincipal @ApiIgnore user: User,
               @RequestBody report: Report): ResponseEntity<Report?> =
            insert(user, report)

    @ResponseBody
    @Secured("ROLE_USER")
    @DeleteMapping("/{id}")
    fun delete(@AuthenticationPrincipal @ApiIgnore user: User,
               @PathVariable id: Int): ResponseEntity<Report?> {
        val report = reportService.delete(id)
        report?.let {
            criticalSectionSensor.deleteIfNeedCriticalSectionFrom(it.location,
                    radiusOfCriticalSection, sizeOfCriticalSection)
        }
        return report?.let { ResponseEntity(it, HttpStatus.ACCEPTED) }
                ?: ResponseEntity(HttpStatus.UNAUTHORIZED)
    }

    @ResponseBody
    @Secured("ROLE_USER")
    @GetMapping("/{id}")
    fun getReportById(
            @AuthenticationPrincipal @ApiIgnore user: User,
            @PathVariable id: Int): Report =
            reportService.findById(id)

    @ResponseBody
    @Secured("ROLE_USER")
    @GetMapping("/{id}/photos")
    fun getPhotosFromReport(
            @AuthenticationPrincipal @ApiIgnore user: User,
            @PathVariable id: Int): List<Photo> =
            reportService.getPhotosFromPhoto(id)

    @ResponseBody
    @Secured("ROLE_USER")
    @GetMapping("")
    fun getAllReportsFromCurrentUser(
            @AuthenticationPrincipal @ApiIgnore user: User,
            @RequestParam page: Int,
            @RequestParam size: Int): Page<Report> =
            reportService.findByUser(getUserById(user.id), PageRequest(page, size))

    @ResponseBody
    @Secured("ROLE_USER")
    @GetMapping("/latest")
    fun getReportsFromLatestWeekFromCurrentUser(
            @AuthenticationPrincipal @ApiIgnore user: User): List<Report> {
        val dateFrom7DaysAgo = Calendar.getInstance()
                .also { it.add(DAY_OF_YEAR, -7) }
        return reportService.findByUserAndDateAfter(user,
                dateFrom7DaysAgo, PageRequest(0, 1000))
    }

    private fun getUserById(id: Int): User =
            userRepository.findUserById(id) ?: throw UserNotFound()
}