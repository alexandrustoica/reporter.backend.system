package taskly.system.security


class AccountCredentials(val username: String,
                         val password: String) {
    constructor(): this("", "")
}
