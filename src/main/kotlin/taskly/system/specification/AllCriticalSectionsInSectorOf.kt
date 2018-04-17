package taskly.system.specification

import taskly.system.specification.AllInSectorOf
import taskly.system.section.CriticalSection

class AllCriticalSectionsInSectorOf(criteria: SectorCriteria) :
        AllInSectorOf<CriticalSection>(criteria)