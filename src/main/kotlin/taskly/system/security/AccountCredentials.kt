package taskly.system.security

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

class AccountCredentials(val username: String,
                         val password: String) {
    constructor(): this("", "")
}
