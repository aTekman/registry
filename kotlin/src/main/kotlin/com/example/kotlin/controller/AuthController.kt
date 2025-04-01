package com.example.kotlin.controller

import com.example.kotlin.component.JwtUtil
import com.example.kotlin.service.UserService
import com.example.kotlin.model.User

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v0.1/auth")
class AuthController(
    private val userService: UserService,
    private val jwtUtil: JwtUtil
    ) {

    @PostMapping("/login")
    fun login(@RequestBody request: UserLogin): ResponseEntity<Any> {
        val user = userService.getUserByEmail(request.email)?: return ResponseEntity.badRequest().body(mapOf("error" to "Пользователь не найден"))

        if (!userService.validatePassword(user, request.password)) {
            return ResponseEntity.badRequest().body(mapOf("error" to "Неверный пароль"))
        }

        val token = jwtUtil.generateToken(user.email)
        return ResponseEntity.ok(AuthResponse(token, user))
    }

    @PostMapping("/register")
    fun register(@RequestBody request: User): ResponseEntity<Any> {
        val userEmail = userService.getUserByEmail(request.email)
        val userPhone = userService.getUserByPhone(request.phone)
        val userPassport = userService.getUserByPassport(request.passport)
        val userMedPolicy = userService.getUserByMedPolicy(request.medPolicy)
        val userSnils = userService.getUserBySnils(request.snils)

        if (userEmail != null) return ResponseEntity.badRequest().body(mapOf("error" to "Пользователь с таким email уже существует"))
        if (userPhone != null) return ResponseEntity.badRequest().body(mapOf("error" to "Пользователь с таким номером телефона уже существует"))
        if (userPassport != null) return ResponseEntity.badRequest().body(mapOf("error" to "Пользователь с таким паспортом уже существует"))
        if (userMedPolicy != null) return ResponseEntity.badRequest().body(mapOf("error" to "Пользователь с таким мед. полисом уже существует"))
        if (userSnils != null) return ResponseEntity.badRequest().body(mapOf("error" to "Пользователь с таким снилс уже существует"))
        if (request.role == null) return ResponseEntity.badRequest().body(mapOf("error" to "Не указана роль"))

        val user = userService.createUser(request)
        val token = jwtUtil.generateToken(request.email)



        return ResponseEntity.ok(AuthResponse(token, user))
    }
}

data class UserLogin(
    val email: String,
    val password: String
)

data class AuthResponse(
    val token: String,
    val user: User
)