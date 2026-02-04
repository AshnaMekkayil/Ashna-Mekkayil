package api

object Config {
    val baseUrl: String = System.getenv("BASE_URL") ?: "https://petstore.swagger.io/v2"
}
