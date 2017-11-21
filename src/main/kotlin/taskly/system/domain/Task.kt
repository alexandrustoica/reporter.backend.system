package taskly.system.domain

import org.hibernate.annotations.CreationTimestamp
import java.io.Serializable
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
data class Task
constructor(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int,
        val text: String,
        val location: String,
        @Temporal(TemporalType.TIMESTAMP) @CreationTimestamp
        val date: Calendar,
        val timeEstimated: Time,
        val deadline: Calendar,
        val done: Boolean,
        val points: Int,

        @ManyToMany(cascade = arrayOf(CascadeType.ALL), targetEntity = User::class)
        @JoinTable(name = "Workflow", joinColumns = arrayOf(JoinColumn(name = "id_task", referencedColumnName = "id")),
                inverseJoinColumns = arrayOf(JoinColumn(name = "id_user", referencedColumnName = "id")))
        @NotNull val users: Set<User> = setOf()) : Serializable {

    constructor() : this(0, "", "", Calendar.getInstance(), Time(0), Calendar.getInstance(), false, 0)
    constructor(id: Int, text: String, location: String, done: Boolean) : this(id, text, location,
            Calendar.getInstance(), Time(0), Calendar.getInstance(), done, 0)
}
