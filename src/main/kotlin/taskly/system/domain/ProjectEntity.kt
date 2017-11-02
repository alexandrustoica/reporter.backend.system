package taskly.system.domain

import net.minidev.json.annotate.JsonIgnore
import org.jetbrains.annotations.NotNull
import java.io.Serializable
import javax.persistence.*

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

@Entity
@Table(name = "Project")
class ProjectEntity(@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
                    @NotNull val id: Int,
                    @NotNull val name: String,

                    @ManyToMany(mappedBy = "projects")
                    @JsonIgnore @NotNull val tasks: Set<TaskEntity> = setOf()) : Serializable {

    constructor() : this(0, "default")
}