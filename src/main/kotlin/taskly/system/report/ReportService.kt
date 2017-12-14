package taskly.system.report

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import taskly.system.user.User

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

@Service
class ReportService {

    @Autowired
    private lateinit var reportRepository: ReportRepository

    fun save(report: Report): Report? =
            reportRepository.save(report)

    fun delete(report: Report): Report? =
            reportRepository.delete(report).let{ report }

    fun delete(id: Int): Report? =
            findById(id).let { delete(it) }

    fun all(page: Pageable): Page<Report> =
            reportRepository.findAll(page)

    fun findById(id: Int): Report =
            reportRepository.findReportById(id) ?: throw ReportNotFound()

    fun findByUser(user: User, page: Pageable): Page<Report> =
            reportRepository.findReportsByUser(user, page)
}