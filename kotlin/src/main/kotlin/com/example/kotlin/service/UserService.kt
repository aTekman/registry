package com.example.kotlin.service

import com.example.kotlin.model.User
import com.example.kotlin.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {
    private val passwordEncoder = BCryptPasswordEncoder()
    fun getAllUsers(): List<User> = userRepository.findAll()
    fun getUserById(id: Long):User = userRepository.findById(id).orElse(null)
    fun getUserByMedPolicy(medPolicy: String):User? = userRepository.findByMedPolicy(medPolicy)
    fun getUserByPassport(passport: String):User? = userRepository.findByPassport(passport)
    fun getUserBySnils(snils: String):User? = userRepository.findBySnils(snils)
    fun getUserByEmail(email: String):User? = userRepository.findByEmail(email)
    fun getUserByPhone(phone: String):User? = userRepository.findByPhone(phone)
    fun createUser(user: User): User{
        val hashedPassword = passwordEncoder.encode(user.password)
        val newUser = user.copy(password = hashedPassword)
        return userRepository.save(newUser)
    }
    fun validatePassword(user: User, password: String): Boolean {
            return if(passwordEncoder.matches(password, user.password)) true else false
    }

    fun updateUser(updUser: User, id: Long): User?{
        val existsUser: User = userRepository.findById(id).orElse(null)
        if (existsUser == null) {return null}

        else{
            val updatedUser = existsUser.copy(
                fullName = updUser.fullName,
                birthday = updUser.birthday,
                email = updUser.fullName,
                phone = updUser.phone,
                passport = updUser.passport,
                snils = updUser.snils,
                medPolicy = updUser.medPolicy,
                password = if(updUser.password.isNotBlank()) passwordEncoder.encode(updUser.password) else existsUser.password,
            )
            return userRepository.save(updUser)
        }
    }
    fun deleteUser(id: Long){
        val existsUser: User = userRepository.findById(id).orElse(null)
        if (existsUser == null) {return}

        else{
           return userRepository.delete(existsUser)
        }
    }
}