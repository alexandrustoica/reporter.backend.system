package taskly.system.user

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

enum class UserRole : Serializable {
    @JsonProperty("USER")
    USER,
    @JsonProperty("POLICE")
    POLICE;
}