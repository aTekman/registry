package registry.ru.security

import registry.ru.component.JwtUtil
import registry.ru.model.User
import registry.ru.service.UserService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val jwtUtil: JwtUtil,
    private val userService: UserService
):OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val token = authHeader.replace("Bearer ","")
        try{
            val email = jwtUtil.getUsername(token)
            if(email != null && SecurityContextHolder.getContext().authentication == null) {
                val userDetails: User = userService.getUserByEmail(email)?: return filterChain.doFilter(request, response)

                if(jwtUtil.validateToken(token, userDetails.email)) {
                    val authorities = listOf(SimpleGrantedAuthority(userDetails.role))
                    val authToken = UsernamePasswordAuthenticationToken(
                        userDetails, null, authorities
                    )
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authToken
                }
            }
        }
        catch (e: Exception) {
            logger.warn("Ошибка аунтефикации: ${e.message}")
        }
        filterChain.doFilter(request,response)
    }
}