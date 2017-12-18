package taskly.system.configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import taskly.system.security.JsonWebTokenAuthenticationFilter
import taskly.system.security.JsonWebTokenAuthorizationFilter
import taskly.system.security.SecurityConstants
import taskly.system.user.UserRepository
import taskly.system.user.UserService


/**
 * @author Alexandru Stoica
 * @version 1.0
 */


@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = arrayOf("taskly.system.security"))
class SecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var constants: SecurityConstants

    @Autowired
    private lateinit var userRepository: UserRepository

    override fun userDetailsService(): UserDetailsService = userService

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userService).passwordEncoder(BCryptPasswordEncoder())
    }

    override fun configure(http: HttpSecurity) {
        http.cors().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .csrf().disable().exceptionHandling().accessDeniedPage("/access-denied").and()
                .authorizeRequests()
                .antMatchers("/api/login/*").permitAll()
                .antMatchers("/login/*").permitAll()
                .antMatchers("/users/register").permitAll()
                .anyRequest().authenticated().and()
                .addFilter(JsonWebTokenAuthorizationFilter(authenticationManager(), constants, userRepository))
                .addFilter(JsonWebTokenAuthenticationFilter(authenticationManager(), constants, userService))
                .csrf().disable().exceptionHandling().accessDeniedPage("/access-denied")
    }
}
