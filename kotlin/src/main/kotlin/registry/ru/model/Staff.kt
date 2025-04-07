package registry.ru.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "staff")
data class Staff(
    @Id
    @Column(name = "id", nullable = false, unique = true)
    val id: String,

    @Column(name = "fullName", nullable = false, columnDefinition = "TEXT")
    val fullName: String,

    @Column(name = "phone",unique = true, nullable = false, columnDefinition = "TEXT")
    val phone: String,

    @Column(name = "email",unique = true, nullable = false, columnDefinition = "TEXT")
    val email: String,

    @Column(name = "prof", nullable = false, columnDefinition = "TEXT")
    val prof: String,

    @JsonIgnore
    @Column(name = "password", nullable = false, columnDefinition = "TEXT")
    val password: String,

    @Column(name = "role", nullable = false, columnDefinition = "TEXT")
    val role: String
)