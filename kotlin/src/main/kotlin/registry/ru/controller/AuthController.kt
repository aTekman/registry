package registry.ru.controller

import registry.ru.component.JwtUtil
import registry.ru.service.UserService
import registry.ru.service.StaffService

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import registry.ru.model.*

@RestController
@RequestMapping("/api/v0.1/auth")
class AuthController(
    private val userService: UserService,
    private val staffService: StaffService,
    private val jwtUtil: JwtUtil
    ) {

    @PostMapping("/login")
    fun login(@RequestBody request: UserLogin): ResponseEntity<Any> {
        val user = userService.getUserByEmail(request.email)?: staffService.getStaffByEmail(request.email)?: return ResponseEntity.badRequest().body(Error("Пользователь не найден"))
        if (user is User) {
            if (!userService.validatePassword(user, request.password)) return ResponseEntity.badRequest().body(Error("Неверный пароль"))
            val token = jwtUtil.generateToken(user.id)
            return ResponseEntity.ok(AuthResponse(token, user))
        }
        else if (user is Staff){
            if (!staffService.validatePassword(user, request.password)) return ResponseEntity.badRequest().body(Error("Неверный пароль"))
            val token = jwtUtil.generateToken(user.id)
            return ResponseEntity.ok(AuthResponse(token, user))
        }
        return ResponseEntity.badRequest().body(Error("Не найден пользователь"))
    }

    @PostMapping("/register")
    fun register(@RequestBody request: UserRegisterRequest): ResponseEntity<Any> {
        return userService.createUser(request)
    }
    @PostMapping("/register/staff")
    fun register(@RequestBody request: StaffRegisterRequest): ResponseEntity<Any> {
        if (request.role == "USER") return ResponseEntity.badRequest().body(Error("Неправильно задана роль"))
        else return staffService.createStaff(request)
    }
}