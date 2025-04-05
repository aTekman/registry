package registry.ru.controller

import registry.ru.component.JwtUtil
import registry.ru.model.Error
import registry.ru.service.UserService
import registry.ru.model.User
import registry.ru.model.AuthResponse
import registry.ru.model.UserLogin
import registry.ru.model.UserRegisterRequest

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
        val user = userService.getUserByEmail(request.email)?: return ResponseEntity.badRequest().body(Error("Пользователь не найден"))

        if (!userService.validatePassword(user, request.password)) {
            return ResponseEntity.badRequest().body(Error("Неверный пароль"))
        }

        val token = jwtUtil.generateToken(user.email)
        return ResponseEntity.ok(AuthResponse(token, user))
    }

    @PostMapping("/register")
    fun register(@RequestBody request: registry.ru.model.UserRegisterRequest): ResponseEntity<Any> {
        val userEmail = userService.getUserByEmail(request.email)
        val userPhone = userService.getUserByPhone(request.phone)
        val userPassport = userService.getUserByPassport(request.passport)
        val userMedPolicy = userService.getUserByMedPolicy(request.medPolicy)
        val userSnils = userService.getUserBySnils(request.snils)

        userEmail?.let { return ResponseEntity.badRequest().body(Error("Пользователь с таким email уже существует")) }
        userPhone?.let { return ResponseEntity.badRequest().body(Error( "Пользователь с таким номером телефона уже существует")) }
        userPassport?.let { return ResponseEntity.badRequest().body(Error("Пользователь с таким паспортом уже существует")) }
        userMedPolicy?.let { return ResponseEntity.badRequest().body(Error( "Пользователь с таким мед. полисом уже существует")) }
        userSnils?.let { return ResponseEntity.badRequest().body(Error( "Пользователь с таким снилс уже существует")) }

        val user = userService.createUser(User(0,request.fullName,request.birthday,request.phone, request.email,request.passport,request.snils,request.medPolicy,request.password,request.role?:"USER"))
        val token = jwtUtil.generateToken(request.email)

        return ResponseEntity.ok(AuthResponse(token, user))
    }
}