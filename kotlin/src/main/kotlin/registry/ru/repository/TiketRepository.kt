package registry.ru.repository

import registry.ru.model.Tiket
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface TiketRepository: JpaRepository<Tiket, String> {
    fun findByDateGreaterThanEqual(date: LocalDateTime): List<Tiket>
    fun findByDateBeforeAndStatus(date: LocalDateTime, status: String): List<Tiket>
    fun findByUserId(userId: String): List<Tiket>
    fun findByStatus(status: String): List<Tiket>
    fun findByDoctor(doctor: String): List<Tiket>

}