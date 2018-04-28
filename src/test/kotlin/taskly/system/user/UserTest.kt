package taskly.system.user

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Test

class UserTest {

    @Test
    fun whenUserCreated_WithValidUser_ExpectUserEnabled() {
        // given & when & then:
        assertThat(User().isEnabled, `is`(true))
    }

    @Test
    fun whenUserCreated_WithValidUser_ExpectUserAccountNotExpired() {
        // given & when & then:
        assertThat(User().isAccountNonExpired, `is`(true))
    }

    @Test
    fun whenUserCreated_WithValidUser_ExpectUserAccountNotLocked() {
        // given & when & then:
        assertThat(User().isAccountNonLocked, `is`(true))
    }

    @Test
    fun whenUserCreated_WithValidUser_ExpectCredentialsNotExpired() {
        // given & when & then:
        assertThat(User().isCredentialsNonExpired, `is`(true))
    }

    @Test
    fun whenUserConvertedToString_ValidUser_ExpectUsername() {
        // given & when & then:
        assertThat(User().copy(username = "test").toString(), `is`("test"))
    }
}