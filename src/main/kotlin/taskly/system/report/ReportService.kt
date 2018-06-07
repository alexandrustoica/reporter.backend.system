package taskly.system.report

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import taskly.system.event.BroadcastEvent
import taskly.system.event.EventType
import taskly.system.notification.SendNotification
import taskly.system.specification.AllReportsInSectorOf
import taskly.system.specification.SectorCriteria
import taskly.system.user.User
import java.util.*


@Service
class ReportService {

    @Autowired
    private lateinit var reportRepository: ReportRepository

    @BroadcastEvent(type = EventType.REPORT_CREATED)
    fun save(report: Report): Report? =
            reportRepository.save(report)

    @BroadcastEvent(type = EventType.REPORT_DELETED)
    fun delete(report: Report): Report? =
            reportRepository.delete(report).let { report }

    fun delete(id: Int): Report? =
            findById(id).let { delete(it) }

    fun all(page: Pageable): Page<Report> =
            reportRepository.findAll(page)

    fun findById(id: Int): Report =
            reportRepository.findReportById(id) ?: throw ReportNotFound()

    fun getById(id: Int): Report? =
            reportRepository.findReportById(id)

    fun findByUser(user: User, page: Pageable): Page<Report> =
            reportRepository.findReportsByUserOrderByDateDesc(user, page)

    fun getPhotosFromPhoto(id: Int): List<Photo> =
            findById(id).photos

    fun findAllReportsNear(origin: Location, byDistance: Double): List<Report> =
            reportRepository.findAll(AllReportsInSectorOf(
                    SectorCriteria(origin, radiusOfSector = byDistance)))
                    .filter { it.location.distanceFrom(origin) <= byDistance }

    fun findByUserAndDateAfter(
            user: User, date: Calendar, page: Pageable): List<Report> =
            reportRepository.findReportsByUserAndDateAfter(user, date, page)

    @BroadcastEvent(type = EventType.REPORT_SOLVED)
    @SendNotification("Your report {0} was solved by the police!")
    fun markReportAsSolved(id: Int): Report? =
            reportRepository.findReportById(id)?.copy(isSolved = true)
                    ?.let { reportRepository.save(it) }

    @BroadcastEvent(type = EventType.REPORT_MARKED_AS_SPAM)
    @SendNotification("Your report {0} was marked as spam by our system!")
    fun markReportAsSpam(id: Int): Report? =
            reportRepository.findReportById(id)?.copy(isSpam = true)
                    ?.let { reportRepository.save(it) }

}