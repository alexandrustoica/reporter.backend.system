package taskly.system.specification

import org.springframework.data.jpa.domain.Specification
import taskly.system.report.Location
import java.lang.Math.max
import javax.persistence.criteria.*

open class AllInSectorOf<T>(
        private val criteria: SectorCriteria) :
        Specification<T> {

    override fun toPredicate(
            root: Root<T>,
            query: CriteriaQuery<*>,
            builder: CriteriaBuilder): Predicate {
        val limit = criteria.origin.destinationWith(criteria.radiusOfSector, 0.0)
        val delta = max(limit.latitude - criteria.origin.latitude,
                limit.longitude - criteria.origin.longitude)
        val latitudePredicate = builder.between(
                root.get<Location>(criteria.keyAttributeLocation)
                        .get<Double>("latitude"),
                criteria.origin.latitude - delta,
                criteria.origin.latitude + delta)
        val longitudePredicate = builder.between(
                root.get<Location>(criteria.keyAttributeLocation)
                        .get<Double>("longitude"),
                criteria.origin.longitude - delta,
                criteria.origin.longitude + delta)
        return builder.and(latitudePredicate, longitudePredicate)
    }
}
