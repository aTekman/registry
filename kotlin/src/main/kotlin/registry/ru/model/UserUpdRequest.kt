package registry.ru.model

data class UserUpdRequest(
    val birthday: String? = null,
    val email: String? = null,
    val fullName: String? = null,
    val medPolicy:String? = null,
    val passport: String? = null,
    val phone: String? = null,
    val snils: String? = null,
    val password: String? = null,
    val role: String? = null
)
