package registry.ru.controller

import org.apache.catalina.Executor
import registry.ru.model.*
import registry.ru.service.UserService
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity
import registry.ru.service.StaffService
import registry.ru.service.TiketService
import java.time.LocalDate
import java.util.UUID

@RestController
@RequestMapping("/api/v0.1/med")
class MedController(private val userService: UserService, private val tiketService: TiketService, private val staffService: StaffService) {
    @GetMapping("/tikets/all")
    fun getAllTikets(): List<Tiket> = tiketService.getAllTikets()

    @GetMapping("/tikets/status/{status}")
    fun getTiketsByStatus(@PathVariable status: String): ResponseEntity<Any> = tiketService.getTiketsByStatus(status)

    @PostMapping("/tikets/new")
    fun createNewTiket(@RequestBody tiket: TiketCreateRequest): ResponseEntity<Any> {
        val user: User? = userService.getUserById(tiket.user)
        user?.let {
            val parsedStatus = try{
                TiketStatus.valueOf((tiket.status?: TiketStatus.подтверждается.toString()).lowercase()).toString()
            } catch (e: Exception) {return ResponseEntity.badRequest().body(Error("Неверно передан статус"))}
            if (tiket.date < LocalDate.now().plusDays(1).atStartOfDay()) return ResponseEntity.badRequest().body(Error("Неверно выбрано время"))
            val newTiket: Tiket = Tiket(UUID.randomUUID().toString(),
            tiket.date,
            tiket.description,
            tiket.results,
            tiket.doctor,
            parsedStatus,
            user)
            tiketService.createNewTiket(newTiket)
        return ResponseEntity.ok().body(Response("Запись успешно создана", newTiket))
        }
        return ResponseEntity.badRequest().body(Error("Пользователь с id ${tiket.user} не найден"))
    }

    @PutMapping("/tikets/update/{id}")
    fun updateTiket(@PathVariable id: String, @RequestBody tiket: TiketRequest, ): ResponseEntity<Any> {
        val existTiket: Tiket? = tiketService.getTiketById(id)
        existTiket?.let {
            tiket.user?.let {
                val user = userService.getUserById(tiket.user) ?: return ResponseEntity.badRequest()
                    .body(Error("Неверно указан пользователь"))
                return tiketService.updateTiket(id, tiket, user)
            }
            return tiketService.updateTiket(id, tiket, existTiket.user)
        }
        return ResponseEntity.badRequest().body(Error("Не найдено записи с id $id"))
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

    @DeleteMapping("id/{id}")
    fun deleteDoctor(@PathVariable id: String): ResponseEntity<Any> = staffService.deleteStaff(id)
}

