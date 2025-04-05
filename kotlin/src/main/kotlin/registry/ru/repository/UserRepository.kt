package registry.ru.repository

import registry.ru.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Long>{
    fun findByMedPolicy(medPolicy: String): User?
    fun findByPassport(passport: String): User?
    fun findBySnils(snils: String): User?
    fun findByEmail(email: String): User?
    fun findByPhone(phone: String): User?
}