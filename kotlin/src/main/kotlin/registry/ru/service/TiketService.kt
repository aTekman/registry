package registry.ru.service

import registry.ru.repository.TiketRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import registry.ru.model.*
import java.time.LocalDateTime

@Service
class TiketService(private val tiketRepository: TiketRepository) {
    fun getAllTikets(): List<Tiket> = tiketRepository.findAll()
    fun getTiketsByDate(date: LocalDateTime): ResponseEntity<Any> {
        try {
            val tikets: List<Tiket> = tiketRepository.findByDateGreaterThanEqual(date)
            if (tikets.isNotEmpty()) return ResponseEntity.ok().body(tikets)
            else return ResponseEntity.badRequest().body(Error("Нет задач на текущую дату"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(Error(e))
        }
    }
    fun getTiketsByUserId(id: String): ResponseEntity<Any> {
        try {
            val tikets: List<Tiket> = tiketRepository.findByUserId(id)
            if (tikets.isNotEmpty()) return ResponseEntity.ok().body(tikets)
            else return ResponseEntity.badRequest().body(Error("Приемов нет"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(Error(e))
        }
    }
    fun getTiketsByStatus(status: String): ResponseEntity<Any> {
        val parsedStatus = try {
            TiketStatus.valueOf(status.lowercase()).toString()
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body("Неверно передан статус")
        }
        val tikets =tiketRepository.findByStatus(parsedStatus)
        if (tikets.isNotEmpty()) return ResponseEntity.ok().body(tikets)
        else return ResponseEntity.badRequest().body(Error("Нет осмотров со статусом $parsedStatus"))
    }
    fun createNewTiket(tiket: Tiket): Tiket = tiketRepository.save(tiket)
    fun getTiketById(id: String): Tiket? = tiketRepository.findById(id).orElse(null)
}