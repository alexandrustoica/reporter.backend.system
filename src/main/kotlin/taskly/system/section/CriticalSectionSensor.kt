package taskly.system.section

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import taskly.system.report.Location
import taskly.system.report.ReportService

@Component
class CriticalSectionSensor {

    @Autowired
    private lateinit var reportService: ReportService

    @Autowired
    private lateinit var criticalSectionService: CriticalSectionService

    fun criticalSectionFormedAt(
            location: Location, withRadius: Double, withSectionSize: Int): CriticalSection? {
        val criticalSections = criticalSectionService.findAllCriticalSectionsNear(
                location, byDistance = withRadius)
        val reports = reportService.findAllReportsNear(
                location, byDistance = withRadius)
        return if (criticalSections.isEmpty() && withSectionSize <= reports.size)
            criticalSectionService.save(CriticalSection(0, location, withRadius, reports))
        else null
    }

    fun deleteIfNeedCriticalSectionFrom(
            location: Location, withRadius: Double, withSectionSize: Int): CriticalSection? =
            criticalSectionService.findAt(location, withRadius)
                    ?.takeIf { it.reports.size <= withSectionSize }
                    ?.let { criticalSectionService.delete(it.id) }
}