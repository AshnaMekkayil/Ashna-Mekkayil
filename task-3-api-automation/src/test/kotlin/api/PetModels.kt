package api

data class Pet(
    val id: Long,
    val name: String,
    val photoUrls: List<String> = listOf("https://example.invalid/pet.png"),
    val status: String? = "available"
)
