package com.example.kotlin.controller

import com.example.kotlin.model.User
import com.example.kotlin.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v0.1/user")
class UserController(private val userService: UserService) {

    @GetMapping("/all")
    fun getUsers(): List<User> = userService.getAllUsers()

    @GetMapping("/id/{id}")
    fun getById(@PathVariable id: Long): User? = userService.getUserById(id)

    @GetMapping("email/{email}")
    fun getByEmail(@PathVariable email: String): User? = userService.getUserByEmail(email)

    @PostMapping
    fun createUser(@RequestBody user: User): User = userService.createUser(user)

    @PutMapping("/email/{email}")
    fun updateUser(@PathVariable email: String, @RequestBody updUser: User): User? = userService.updateUser(updUser, email)

    @DeleteMapping("/email/{email}")
    fun deleteUser(@PathVariable email: String) = userService.deleteUser(email)
}