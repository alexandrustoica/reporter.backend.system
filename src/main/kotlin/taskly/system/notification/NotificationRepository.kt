package taskly.system.notification

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import taskly.system.user.User

@Repository
interface NotificationRepository: PagingAndSortingRepository<Notification, Int> {
    fun findNotificationsByUser(user: User): List<Notification>
}