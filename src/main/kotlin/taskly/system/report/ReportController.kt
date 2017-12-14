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
import taskly.system.domain.User
import taskly.system.exception.UserNotFound
import taskly.system.repository.UserRepository

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
            @RequestParam size: Int): Page<Report> =
            reportService.findByUser(getUserById(user.id), PageRequest(page, size))


    private fun getUserById(id: Int): User =
            userRepository.findUserById(id) ?: throw UserNotFound()
}