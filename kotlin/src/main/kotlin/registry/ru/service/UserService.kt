package registry.ru.service

import registry.ru.model.Error
import registry.ru.model.Response
import registry.ru.model.User
import registry.ru.model.UserUpdRequest
import registry.ru.repository.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {
    private val passwordEncoder = BCryptPasswordEncoder()
    fun getAllUsers(): List<User> = userRepository.findAll()
    fun getUserById(id: Long):User = userRepository.findById(id).orElse(null)

    fun getUserByMedPolicy(medPolicy: String):User? = userRepository.findByMedPolicy(medPolicy)
    fun getUserByPassport(passport: String):User? = userRepository.findByPassport(passport)
    fun getUserBySnils(snils: String):User? = userRepository.findBySnils(snils)
    fun getUserByEmail(email: String):User? = userRepository.findByEmail(email)
    fun getUserByPhone(phone: String):User? = userRepository.findByPhone(phone)

    fun createUser(user: User): User{
        val hashedPassword = passwordEncoder.encode(user.password)
        val newUser = user.copy(password = hashedPassword)
        return userRepository.save(newUser)
    }
    fun validatePassword(user: User, password: String): Boolean {
            return if(passwordEncoder.matches(password, user.password)) true else false
    }

    fun updateUser(updUser: UserUpdRequest, email: String): ResponseEntity<Any>{
        val existsUser: User? = userRepository.findByEmail(email)
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
        return ResponseEntity.badRequest().body(Error("Не найдено пользователя с email $email"))
    }
    fun deleteUser(email: String): ResponseEntity<Any> {
        val existsUser: User = userRepository.findByEmail(email)?: return ResponseEntity.badRequest().body(Error("Не найдено пользователя с email $email"))
        userRepository.delete(existsUser)
        return ResponseEntity.ok().body(Response("Пользователь успешно удален"))

    }
}