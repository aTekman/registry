package registry.ru.config

import registry.ru.security.JwtAuthFilter

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthFilter: JwtAuthFilter
){
    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager{
        return config.authenticationManager
    }

    @Bean
    fun seсurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf{ it.disable()}
            .sessionManagement{ it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)}
            .authorizeHttpRequests{
                it.requestMatchers("/api/v0.1/auth/**").permitAll()

                it.requestMatchers("/api/v0.1/admin/**").hasAuthority("ADMIN")
                it.requestMatchers("/api/v0.1/med/**").hasAnyAuthority("MEDIC","ADMIN")
                it.requestMatchers("/api/v0.1/user/**").hasAnyAuthority("USER","MEDIC","ADMIN")
                it.anyRequest().authenticated()
            }
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }
}