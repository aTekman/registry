package registry.ru.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0,

    @Column(name = "fullName", nullable = false, columnDefinition = "TEXT")
    val fullName: String,

    @Column(name = "birthday", nullable = false, columnDefinition = "TEXT")
    val birthday: String,

    @Column(name = "phone",unique = true, nullable = false, columnDefinition = "TEXT")
    val phone: String,

    @Column(name = "email",unique = true, nullable = false, columnDefinition = "TEXT")
    val email: String,

    @Column(name = "passport",unique = true, nullable = false, length = 10)
    val passport: String,

    @Column(name = "snils",unique = true, nullable = false, length = 11)
    val snils: String,

    @Column(name = "medPolicy",unique = true, nullable = false, length = 16)
    val medPolicy: String,

    @JsonIgnore
    @Column(name = "password", nullable = false, columnDefinition = "TEXT")
    val password: String,

    @Column(name = "role", nullable = false, columnDefinition = "TEXT")
    val role: String
)
