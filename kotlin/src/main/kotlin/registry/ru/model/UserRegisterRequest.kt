package registry.ru.model

data class UserRegisterRequest(
    val birthday: String,
    val email: String,
    val fullName: String,
    val medPolicy:String,
    val passport: String,
    val phone: String,
    val snils: String,
    val password: String
)
