package registry.ru.service

import registry.ru.repository.UserRepository
import registry.ru.component.JwtUtil
import registry.ru.model.*
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

import java.util.*

@Service
class UserService(private val userRepository: UserRepository, private val jwtUtil : JwtUtil) {
    private val passwordEncoder = BCryptPasswordEncoder()
    fun getAllUsers(): List<User> = userRepository.findAll()
    fun getUserById(id: String):User? = userRepository.findById(id).orElse(null)
    fun getUserByEmail(email: String):User? = userRepository.findByEmail(email)

    fun createUser(user: UserRegisterRequest): ResponseEntity<Any> {
        val userEmail = userRepository.findByEmail(user.email)
        val userPhone = userRepository.findByPhone(user.phone)
        val userPassport = userRepository.findByPassport(user.passport)
        val userMedPolicy = userRepository.findByMedPolicy(user.medPolicy)
        val userSnils = userRepository.findBySnils(user.snils)

        userEmail?.let { return ResponseEntity.badRequest().body(Error("Пользователь с таким email уже существует")) }
        userPhone?.let { return ResponseEntity.badRequest().body(Error( "Пользователь с таким номером телефона уже существует")) }
        userPassport?.let { return ResponseEntity.badRequest().body(Error("Пользователь с таким паспортом уже существует")) }
        userMedPolicy?.let { return ResponseEntity.badRequest().body(Error( "Пользователь с таким мед. полисом уже существует")) }
        userSnils?.let { return ResponseEntity.badRequest().body(Error( "Пользователь с таким снилс уже существует")) }

        val hashedPassword = passwordEncoder.encode(user.password)
        val role = Roles.USER.toString()
        val newUser = User(UUID.randomUUID().toString(),
            user.fullName,
            user.birthday,
            user.phone,
            user.email,
            user.passport,
            user.snils,
            user.medPolicy,
            hashedPassword,
            role)
        userRepository.save(newUser)
        val token = jwtUtil.generateToken(newUser.id)
        return ResponseEntity.ok().body(AuthResponse(token, newUser))
    }
    fun validatePassword(user: User, password: String): Boolean {
            return if(passwordEncoder.matches(password, user.password)) true else false
    }
    fun updateUser(updUser: UserUpdRequest, id: String): ResponseEntity<Any>{
        val existsUser: User? = userRepository.findById(id).orElse(null)
        existsUser?.let{
            val updatedUser = existsUser.copy(
                fullName = updUser.fullName ?: existsUser.fullName,
                birthday = updUser.birthday ?: existsUser.birthday,
                email = updUser.email ?: existsUser.email,
                phone = updUser.phone ?: existsUser.phone,
                passport = updUser.passport ?: existsUser.passport,
                snils = updUser.snils ?: existsUser.snils,
                medPolicy = updUser.medPolicy ?: existsUser.medPolicy,
                password = if(updUser.password != null) passwordEncoder.encode(updUser.password) else existsUser.password
            )
            return ResponseEntity.ok().body(userRepository.save(updatedUser))
        }
        return ResponseEntity.badRequest().body(Error("Не найдено пользователя с id $id"))
    }
    fun deleteUser(id: String): ResponseEntity<Any> {
        val existsUser: User = userRepository.findById(id).orElse(null)?: return ResponseEntity.badRequest().body(Error("Не найдено пользователя с id $id"))
        userRepository.delete(existsUser)
        return ResponseEntity.ok().body(Response("Пользователь успешно удален"))

    }
}