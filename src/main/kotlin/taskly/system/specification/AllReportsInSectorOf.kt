package taskly.system.specification

import taskly.system.specification.AllInSectorOf
import taskly.system.report.Report

class AllReportsInSectorOf(criteria: SectorCriteria) :
        AllInSectorOf<Report>(criteria)