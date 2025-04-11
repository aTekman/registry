package registry.ru.model

data class StaffRegisterRequest(
    val fullName: String,
    val phone: String,
    val email: String,
    val prof: String,
    val password: String,
    val role: String
)
