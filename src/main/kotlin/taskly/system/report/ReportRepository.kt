package taskly.system.report

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

@Repository
interface ReportRepository: PagingAndSortingRepository<Report, Int> {
    fun findReportById(id: Int): Report?
}