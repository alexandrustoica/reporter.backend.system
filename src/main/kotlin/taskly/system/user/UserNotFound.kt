package taskly.system.user

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

@ResponseStatus(
        value = HttpStatus.NOT_FOUND,
        reason="404 The user was not found by the system! " +
                "Please check your username or password!")
class UserNotFound: RuntimeException("404 User Not Found")
