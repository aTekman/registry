package registry.ru.controller

import registry.ru.model.Error
import registry.ru.model.User
import registry.ru.model.UserUpdRequest
import registry.ru.service.UserService
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity

@RestController
@RequestMapping("/api/v0.1/user")
class UserController(private val userService: UserService) {

    @GetMapping("/all")
    fun getUsers(): List<User> = userService.getAllUsers()

    @GetMapping("/id/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<Any> {
        val user = userService.getUserById(id)
        user.let {
            return ResponseEntity.ok().body(user)
        }
            return ResponseEntity.badRequest().body(Error("Пользователя с id $id не найдено"))
    }

    @GetMapping("/email/{email}")
    fun getByEmail(@PathVariable email: String): ResponseEntity<Any> {
        val user = userService.getUserByEmail(email)
        user.let {
            return ResponseEntity.ok().body(user)
        }
            return ResponseEntity.badRequest().body(Error("Пользователя с email $email не найдено"))
    }

    @PutMapping("/email/{email}")
    fun updateUser(@PathVariable email: String, @RequestBody updUser: UserUpdRequest): ResponseEntity<Any> = userService.updateUser(updUser, email)

    @DeleteMapping("/email/{email}")
    fun deleteUser(@PathVariable email: String): ResponseEntity<Any> = userService.deleteUser(email)
}