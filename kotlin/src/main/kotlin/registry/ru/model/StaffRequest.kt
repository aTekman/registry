package registry.ru.model

data class StaffRequest(
    val fullName: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val prof: String? = null,
    val password: String? = null,
    val role: String? = null
)
