package taskly.system.domain

import org.hibernate.annotations.CreationTimestamp
import java.sql.Time
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

@Entity
@Table(name = "Task")
class Task(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        @NotNull val id: Int,
        @NotNull val text: String,
        @NotNull val location: String,
        @Temporal(TemporalType.TIMESTAMP) @CreationTimestamp
        @NotNull val date: Calendar,
        @NotNull val timeLeft: Time,
        @NotNull val timeEstimated: Time,
        @NotNull val deadline: Calendar,
        @NotNull val done: Boolean,
        @NotNull val points: Int,

        @ManyToMany(cascade = arrayOf(CascadeType.ALL), targetEntity = User::class)
        @JoinTable(name = "Workflow", joinColumns = arrayOf(JoinColumn(name = "id_task", referencedColumnName = "id")),
                inverseJoinColumns = arrayOf(JoinColumn(name = "id_user", referencedColumnName = "id")))
        @NotNull val users: Set<User> = setOf(),

        @ManyToMany(cascade = arrayOf(CascadeType.ALL), targetEntity = Project::class)
        @JoinTable(name = "Collection", joinColumns = arrayOf(JoinColumn(name = "id_task", referencedColumnName = "id")),
                inverseJoinColumns = arrayOf(JoinColumn(name = "id_project", referencedColumnName = "id")))
        @NotNull val projects: Set<Project> = setOf()) {

    constructor() : this(0, "", "", Calendar.getInstance(),
            Time(0), Time(0), Calendar.getInstance(), false, 0)
}