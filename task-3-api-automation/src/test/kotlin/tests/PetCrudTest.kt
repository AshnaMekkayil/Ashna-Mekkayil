package tests

import api.Pet
import api.PetApi
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Instant

class PetCrudTest {

    private fun uniqueId(): Long = Instant.now().toEpochMilli() % 2_000_000_000

    @Test
    fun `PET CRUD happy path`() {
        val id = uniqueId()
        val pet = Pet(id = id, name = "qa-home-challenge-pet")

        // CREATE
        val createRes = PetApi.createPet(pet)
        assertEquals(200, createRes.statusCode, "Create failed: ${createRes.asString()}")

        // READ
        val getRes = PetApi.getPet(id)
        assertEquals(200, getRes.statusCode, "Get failed: ${getRes.asString()}")
        assertEquals(id, getRes.jsonPath().getLong("id"))

        // UPDATE
        val updated = pet.copy(name = "qa-pet-updated", status = "sold")
        val updateRes = PetApi.updatePet(updated)
        assertEquals(200, updateRes.statusCode, "Update failed: ${updateRes.asString()}")

        // DELETE
        val deleteRes = PetApi.deletePet(id)
        if (deleteRes.statusCode != 200 && deleteRes.statusCode != 204) {
            throw AssertionError("Delete failed: ${deleteRes.statusCode} ${deleteRes.asString()}")
        }

        // GET after delete (Petstore can return 404 or 400)
        val getAfterDelete = PetApi.getPet(id)
        if (getAfterDelete.statusCode != 404 && getAfterDelete.statusCode != 400) {
            throw AssertionError("Expected not found after delete, got: ${getAfterDelete.statusCode}")
        }
    }
}
