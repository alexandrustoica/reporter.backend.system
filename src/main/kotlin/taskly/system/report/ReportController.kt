package taskly.system.report

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore
import taskly.system.user.User
import taskly.system.user.UserNotFound
import taskly.system.user.UserRepository
import java.util.*
import java.util.Calendar.DAY_OF_YEAR

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

@RestController
@RequestMapping("/reports")
class ReportController {

    @Autowired
    private lateinit var reportService: ReportService

    @Autowired
    private lateinit var userRepository: UserRepository

    @ResponseBody
    @Secured("ROLE_USER")
    @PostMapping("")
    fun insert(@AuthenticationPrincipal @ApiIgnore user: User,
               @RequestBody report: Report): ResponseEntity<Report?> =
            reportService.save(report.copy(user = getUserById(user.id)))?.
                    let { ResponseEntity(it, HttpStatus.ACCEPTED) }
                    ?: ResponseEntity(HttpStatus.UNAUTHORIZED)

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
               @PathVariable id: Int): ResponseEntity<Report?> =
            reportService.delete(id)?.
                    let { ResponseEntity(it, HttpStatus.ACCEPTED) }
                    ?: ResponseEntity(HttpStatus.UNAUTHORIZED)

    @ResponseBody
    @Secured("ROLE_USER")
    @GetMapping("/{id}")
    fun getReportById(@AuthenticationPrincipal @ApiIgnore user: User,
                      @PathVariable id: Int): Report =
            reportService.findById(id)

    @ResponseBody
    @Secured("ROLE_USER")
    @GetMapping("")
    fun getAllReportsFromCurrentUser(
            @AuthenticationPrincipal @ApiIgnore user: User,
            @RequestParam page: Int,
            @RequestParam size: Int): List<Report> =
            reportService.findByUser(getUserById(user.id), PageRequest(page, size)).content

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