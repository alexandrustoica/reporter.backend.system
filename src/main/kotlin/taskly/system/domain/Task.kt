package taskly.system.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.hibernate.annotations.CreationTimestamp
import java.io.Serializable
import java.util.*
import javax.persistence.*

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

@Entity
@ApiModel
@Table(name = "Task")
data class Task constructor(

        @ApiModelProperty(hidden = true)
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int,

        val text: String,
        val location: String,
        val done: Boolean,
        val points: Int,

        @ApiModelProperty(hidden = true)
        @Temporal(TemporalType.TIMESTAMP) @CreationTimestamp
        val date: Calendar,

        @JsonIgnore
        @OrderBy(value="id")
        @ApiModelProperty(hidden = true)
        @ManyToMany(targetEntity = User::class)
        @JoinTable(name = "Workflow",
                joinColumns = arrayOf(JoinColumn(name = "id_task", referencedColumnName = "id")),
                inverseJoinColumns = arrayOf(JoinColumn(name = "id_user", referencedColumnName = "id")))
        val users: List<User> = listOf()) : Serializable {

    constructor() : this(0, "", "", false, 0, Calendar.getInstance())

    constructor(id: Int, text: String, location: String, done: Boolean) :
            this(id, text, location, done, 0, Calendar.getInstance())

    override fun toString(): String = text
    fun pushUser(user: User) = copy(users = users + listOf(user))
}
