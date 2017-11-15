package taskly.system.domain

import com.fasterxml.jackson.annotation.JsonInclude
import org.jetbrains.annotations.NotNull
import java.io.Serializable
import javax.persistence.*

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

@Entity
@Table(name = "Project")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class Project(@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
                   @NotNull val id: Int,
                   @NotNull var name: String,
                   @ManyToMany(mappedBy = "projects")
                   @NotNull val tasks: Set<Task> = setOf()) : Serializable {

    constructor() : this(0, "default")
    constructor(name: String) : this(0, name)
}