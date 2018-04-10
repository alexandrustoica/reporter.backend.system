package taskly.system.report

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import taskly.system.user.User
import java.util.*


@Repository
interface ReportRepository : PagingAndSortingRepository<Report, Int> {
    fun findReportById(id: Int): Report?
    fun findReportsByUserOrderByDateDesc(
            user: User, page: Pageable): Page<Report>
    fun findReportsByUserAndDateAfter(
            user: User, date: Calendar, page: Pageable): List<Report>
}