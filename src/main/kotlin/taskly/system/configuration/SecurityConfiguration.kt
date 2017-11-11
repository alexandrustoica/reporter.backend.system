package taskly.system.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

@Configuration
@EnableWebSecurity
class SecurityConfiguration: WebSecurityConfigurerAdapter() {

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password("password")
                .roles("USER")
    }

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests().anyRequest().authenticated().and()
                .csrf().disable().exceptionHandling().accessDeniedPage("/access-denied").and()
                .formLogin().and()
                .httpBasic()
    }
}