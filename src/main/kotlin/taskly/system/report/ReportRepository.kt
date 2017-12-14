package taskly.system.report

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import taskly.system.domain.User

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

@Repository
interface ReportRepository: PagingAndSortingRepository<Report, Int> {
    fun findReportById(id: Int): Report?
    fun findReportsByUser(user: User, page: Pageable): Page<Report>
}