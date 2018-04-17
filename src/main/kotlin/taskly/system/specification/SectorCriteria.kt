package taskly.system.specification

import taskly.system.report.Location

data class SectorCriteria(
        val origin: Location,
        val radiusOfSector: Double,
        val keyAttributeLocation: String = "location")