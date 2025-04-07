package registry.ru.model

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class TiketRequest(
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    val date: LocalDateTime?,
    val description: String?,
    val results: String?,
    val doctor: String?,
    val status: String?,
    val user: String?
)

