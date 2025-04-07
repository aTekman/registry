package registry.ru.controller

import registry.ru.model.*
import registry.ru.service.UserService
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity
import registry.ru.service.StaffService
import registry.ru.service.TiketService
import java.util.UUID

@RestController
@RequestMapping("/api/v0.1/med")
class MedController(private val userService: UserService, private val tiketService: TiketService, private val staffService: StaffService) {
    @GetMapping("/tikets/all")
    fun getAllTikets(): List<Tiket> = tiketService.getAllTikets()

    @PostMapping("/tikets/new")
    fun createNewTiket(@RequestBody tiket: TiketCreateRequest): ResponseEntity<Any> {
        val user: User? = userService.getUserById(tiket.user)
        user?.let {
            val parsedStatus = TiketStatus.valueOf(tiket.status?: TiketStatus.подтверждается.toString()).toString()
            val newTiket: Tiket = Tiket(UUID.randomUUID().toString(),
            tiket.date,
            tiket.description,
            tiket.results,
            tiket.doctor,
            parsedStatus,
            user)
            tiketService.createNewTiket(newTiket)
        return ResponseEntity.ok().body(listOf("message: Запись успешно создана", newTiket))
        }
        return ResponseEntity.badRequest().body(Error("Пользователь с id ${tiket.user} не найден"))
    }

    @PutMapping("/tikets/update/")
    fun updateTiket(@RequestBody tiket: TiketCreateRequest): ResponseEntity<Any> {
        val user: User? = userService.getUserById(tiket.user)
        user?.let {
            tiket.status?: return ResponseEntity.badRequest().body(Error("Неверно задан статус"))
            val parsedStatus = try {
                TiketStatus.valueOf(tiket.status.lowercase()).toString()
            } catch (e: Exception) {
                return ResponseEntity.badRequest().body(Error("Неверно задан статус"))
            }
            val newTiket: Tiket = Tiket(UUID.randomUUID().toString(),
                tiket.date,
                tiket.description,
                tiket.results,
                tiket.doctor,
                parsedStatus,
                user)
            tiketService.createNewTiket(newTiket)
            return ResponseEntity.ok().body(listOf("message: Запись успешно создана", newTiket))
        }
        return ResponseEntity.badRequest().body(Error("Пользователь с id ${tiket.user} не найден"))
    }

    @GetMapping("tikets/id/{id}")
    fun getTiketById(@PathVariable id: String): ResponseEntity<Any> {
        val tiket: Tiket = tiketService.getTiketById(id)?: return ResponseEntity.badRequest().body(Error("Не найдено задачи с id $id"))
        return ResponseEntity.ok().body(tiket)
    }

    @PutMapping("/id/{id}")
    fun updateDoctor(@PathVariable id: String, @RequestBody doc: StaffRequest): ResponseEntity<Any> = staffService.updateStaff(doc, id)

    @GetMapping("email/{email}")
    fun getDoctorByEmail(@PathVariable email: String): ResponseEntity<Any> {
        val doctor = staffService.getStaffByEmail(email)?: return ResponseEntity.badRequest().body(Error("Не найдено пользователя с email $email"))
        return ResponseEntity.ok().body(doctor)
    }

    @GetMapping("id/{id}")
    fun getDoctorById(@PathVariable id: String): ResponseEntity<Any> {
        val doctor = staffService.getStaffById(id)?: return ResponseEntity.badRequest().body(Error("Не найдено пользователя с id $id"))
        return ResponseEntity.ok().body(doctor)
    }
}

