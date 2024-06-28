package edu.backend.pawsitive

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
class AITests(
    @Autowired
    val aiRequestStrategy: AIRequestStrategy,
) {
    @Test
    fun testAIRequestStrategy() {
        val prompt = """
                For this pet

                    "pet": {
                        "id": 1,
                        "name": "Buddy",
                        "address": "123 Main St",
                        "about": "Friendly dog",
                        "age": 3,
                        "animalType": {
                            "id": 1,
                            "name": "Dog",
                            "breed": {
                                "id": 1,
                                "name": "Golden Retriever"
                            }
                        }
                    }

                Based on the animalType and age, return a json with the keys "costs", "cares" and "details" with specific information that i will need if I want to adopt this pet.

                For the "costs" value, return a string with all the information needed, do the same for "cares" and "details". In details, add some extra info that I may need.

                RETURN ONLY THE JSON PLEASE, NO MORE TEXT IS NEEDED

                AVOID THE MARKDOWN
                """.trimIndent().replace("\n","").replace("\"","\\\"")
        //remove \n from prompt
        //handle the " in the prompt

        val response = aiRequestStrategy.executeAIRequest(prompt)
        println(response)

    }
}