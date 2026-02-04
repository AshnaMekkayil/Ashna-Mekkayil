package api

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.response.Response

object PetApi {

    fun createPet(pet: Pet): Response =
        given()
            .baseUri(Config.baseUrl)
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(pet)
        .`when`()
            .post("/pet")

    fun getPet(id: Long): Response =
        given()
            .baseUri(Config.baseUrl)
            .accept(ContentType.JSON)
        .`when`()
            .get("/pet/$id")

    fun updatePet(pet: Pet): Response =
        given()
            .baseUri(Config.baseUrl)
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(pet)
        .`when`()
            .put("/pet")

    fun deletePet(id: Long): Response =
        given()
            .baseUri(Config.baseUrl)
            .accept(ContentType.JSON)
        .`when`()
            .delete("/pet/$id")
}
