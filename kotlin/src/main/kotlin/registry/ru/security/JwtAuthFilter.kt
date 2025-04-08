package registry.ru.security

import registry.ru.component.JwtUtil
import registry.ru.model.User
import registry.ru.model.Staff
import registry.ru.service.UserService
import registry.ru.service.StaffService
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
    private val userService: UserService,
    private val staffService: StaffService
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
            val id = jwtUtil.getUsername(token)
            if(id != null && SecurityContextHolder.getContext().authentication == null) {
                val userDetails = userService.getUserById(id)
                userDetails?.let {
                    if (jwtUtil.validateToken(token, userDetails.id)) {
                        val authorities = listOf(SimpleGrantedAuthority(userDetails.role))
                        val authToken = UsernamePasswordAuthenticationToken(
                            userDetails, null, authorities
                        )
                        authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                        SecurityContextHolder.getContext().authentication = authToken
                    }
                }
                val staffDetails = staffService.getStaffById(id)?: return filterChain.doFilter(request, response)
                if (jwtUtil.validateToken(token, staffDetails.id)) {
                    val authorities = listOf(SimpleGrantedAuthority(staffDetails.role))
                    val authToken = UsernamePasswordAuthenticationToken(
                        staffDetails, null, authorities
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