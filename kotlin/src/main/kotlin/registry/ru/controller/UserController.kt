package registry.ru.controller

import registry.ru.service.UserService
import registry.ru.service.TiketService
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity
import registry.ru.model.*
import registry.ru.service.StaffService
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*


@RestController
@RequestMapping("/api/v0.1/user")
class UserController(private val userService: UserService, private val tiketService: TiketService, private val staffService: StaffService) {

    @GetMapping("/tikets/{id}")
    fun getTiketsById(@PathVariable id: String): ResponseEntity<Any> {
        val user = userService.getUserById(id)
        user?.let {
            return tiketService.getTiketsByUserId(user.id)
        }
        return ResponseEntity.badRequest().body(Error("Не найдено пользователя с id $id"))
    }

    @PostMapping("/tikets/new")
    fun createNewTiket(@RequestBody tiket: TiketCreateRequest): ResponseEntity<Any> {
        val user: User? = userService.getUserById(tiket.user)
        user?.let {
            val parsedStatus = try {
                TiketStatus.подтверждается.toString()
            } catch (e: Exception) {
                return ResponseEntity.badRequest().body(Error("Неверно задан статус"))
            }
            if (tiket.date < LocalDate.now().plusDays(1).atStartOfDay()) return ResponseEntity.badRequest().body(Error("Неверно выбрано время"))
            val newTiket: Tiket = Tiket(
                UUID.randomUUID().toString(),
                tiket.date,
                tiket.description,
                tiket.results,
                tiket.doctor,
                parsedStatus,
                user)
            tiketService.createNewTiket(newTiket)
            return ResponseEntity.ok().body(Response("Запись успешно отправлена на подтвержнение", newTiket))
        }
        return ResponseEntity.badRequest().body(Error("Пользователь с id ${tiket.user} не найден"))
    }

    @PutMapping("/tikets/cancel/{id}")
    fun cancelTiket(@PathVariable id: String): ResponseEntity<Any> = tiketService.cancelTiket(id)

    @GetMapping("/id/{id}")
    fun getUserById(@PathVariable id: String): ResponseEntity<Any> {
        val user = userService.getUserById(id)
        user.let {
            return ResponseEntity.ok().body(user)
        }
            return ResponseEntity.badRequest().body(Error("Пользователя с id $id не найдено"))
    }

    @GetMapping("/email/{email}")
    fun getUserByEmail(@PathVariable email: String): ResponseEntity<Any> {
        val user = userService.getUserByEmail(email)
        user.let {
            return ResponseEntity.ok().body(user)
        }
            return ResponseEntity.badRequest().body(Error("Пользователя с email $email не найдено"))
    }

    @GetMapping("/med/{prof}")
    fun getDoctorsByProf(@PathVariable prof: String): ResponseEntity<Any> {
        val doctors = staffService.getStaffByProf(prof)
        if (doctors.isEmpty()) return ResponseEntity.badRequest().body(Error("Нет докторов данного направления"))
        else return ResponseEntity.ok().body(doctors)
    }

    @PutMapping("/id/{id}")
    fun updateUser(@PathVariable id: String, @RequestBody updUser: UserUpdRequest): ResponseEntity<Any> = userService.updateUser(updUser, id)

    @DeleteMapping("/id/{id}")
    fun deleteUser(@PathVariable id: String): ResponseEntity<Any> = userService.deleteUser(id)
}