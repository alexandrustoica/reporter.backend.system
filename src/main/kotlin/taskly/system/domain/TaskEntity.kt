package taskly.system.domain

import net.minidev.json.annotate.JsonIgnore
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
class TaskEntity(
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

        @ManyToMany(cascade = arrayOf(CascadeType.ALL), targetEntity = UserEntity::class)
        @JoinTable(name = "Workflow", joinColumns = arrayOf(JoinColumn(name = "id_task", referencedColumnName = "id")),
                inverseJoinColumns = arrayOf(JoinColumn(name = "id_user", referencedColumnName = "id")))
        @JsonIgnore @NotNull val users: Set<UserEntity> = setOf(),

        @ManyToMany(cascade = arrayOf(CascadeType.ALL), targetEntity = ProjectEntity::class)
        @JoinTable(name = "Collection", joinColumns = arrayOf(JoinColumn(name = "id_task", referencedColumnName = "id")),
                inverseJoinColumns = arrayOf(JoinColumn(name = "id_project", referencedColumnName = "id")))
        @JsonIgnore @NotNull val projects: Set<ProjectEntity> = setOf()) {

    constructor() : this(0, "", "", Calendar.getInstance(),
            Time(0), Time(0), Calendar.getInstance(), false, 0)
}