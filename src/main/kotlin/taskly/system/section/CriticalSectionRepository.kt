package taskly.system.section

import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository


@Repository
interface CriticalSectionRepository:
        PagingAndSortingRepository<CriticalSection, Int>,
        JpaSpecificationExecutor<CriticalSection> {
    fun findById(id: Int): CriticalSection?
}