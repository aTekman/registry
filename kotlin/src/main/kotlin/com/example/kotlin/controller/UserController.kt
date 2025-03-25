package com.example.kotlin.controller

import com.example.kotlin.model.User
import com.example.kotlin.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @GetMapping
    fun getUsers(): List<User> = userService.getAllUsers()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): User? = userService.getUserById(id)

    @PostMapping
    fun createUser(@RequestBody user: User): User = userService.createUser(user)

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody updUser: User): User?{
        return userService.updateUser(updUser, id)
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long) = userService.deleteUser(id)

}