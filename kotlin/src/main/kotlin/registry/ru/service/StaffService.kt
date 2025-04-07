package registry.ru.service

import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import registry.ru.model.*
import registry.ru.repository.StaffRepository
import org.springframework.stereotype.Service
import registry.ru.component.JwtUtil
import java.util.UUID

@Service
class StaffService(private val staffRepository: StaffRepository, private val jwtUtil : JwtUtil) {
    private val passwordEncoder = BCryptPasswordEncoder()
    fun createStaff(staffRequest: StaffRegisterRequest): ResponseEntity<Any> {
        val fullName = staffRequest.fullName
        val phone = staffRequest.phone
        staffRepository.findByPhone(phone)?.let { return ResponseEntity.badRequest().body(Error("Номер телефона уже используется")) }
        val email = staffRequest.email
        staffRepository.findByEmail(email)?.let { return ResponseEntity.badRequest().body(Error("Email уже используется")) }
        val prof = staffRequest.prof
        val password = staffRequest.password
        val heashedPassword = passwordEncoder.encode(password)
        val role = try {
            Roles.valueOf(staffRequest.role).toString()
        } catch (e: Exception) { return ResponseEntity.badRequest().body(Error("Не задана роль"))}

        val newStaff = staffRepository.save(Staff(
            id = UUID.randomUUID().toString(),
            fullName = fullName,
            phone = phone,
            email = email,
            prof = prof,
            password = heashedPassword,
            role = role
        ))
        staffRepository.save(newStaff)
        val token = jwtUtil.generateToken(newStaff.email)
        return ResponseEntity.ok().body(AuthResponse(token, newStaff))
    }
    fun updateStaff(staffRequest: StaffRequest, id: String): ResponseEntity<Any> {
        val existStaff = staffRepository.findById(id).orElse(null)
        existStaff?.let {
            val password = try {
                passwordEncoder.encode(staffRequest.password)
            } catch (e: Exception) { existStaff.password }
            val role = try {
                Roles.valueOf(staffRequest.role?: existStaff.role).toString()
            } catch (e:Exception) { return ResponseEntity.badRequest().body(Error("Не задана роль"))}
            if (role == "USER") return ResponseEntity.badRequest().body(Error("Задана неправильная роль"))
            val updStaff = existStaff.copy(
                fullName = staffRequest.fullName?: existStaff.fullName,
                phone = staffRequest.phone?: existStaff.phone,
                email = staffRequest.email?: existStaff.email,
                prof = staffRequest.prof?: existStaff.prof,
                password = password,
                role = role
            )
            return ResponseEntity.ok().body(updStaff)
        }
        return ResponseEntity.badRequest().body(Error("Нет пользователя с id $id"))
    }
    fun validatePassword(user: Staff, password: String): Boolean {
        return if(passwordEncoder.matches(password, user.password)) true else false
    }
    fun getStaffByEmail(email: String): Staff? = staffRepository.findByEmail(email)
    fun getStaffByProf(prof: String): List<Staff> = staffRepository.findByProf(prof)
    fun getStaffById(id: String): Staff? = staffRepository.findById(id).orElse(null)
}