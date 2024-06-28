package edu.backend.pawsitive

import com.google.gson.JsonParser
import io.github.cdimascio.dotenv.Dotenv
import io.jsonwebtoken.io.IOException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit


interface AIRequestStrategy {
    fun executeAIRequest(prompt: String): String
}

class OpenAIRequest : AIRequestStrategy {
    private val client: OkHttpClient = OkHttpClient.Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()

    override fun executeAIRequest(prompt: String): String {

        // get api key from .env
        val dotenv = Dotenv.load()
        val apiKey = dotenv["PAWSITIVE-API-KEY"]

        val mediaType = "application/json".toMediaType()
        val body = """
                {
                  "model": "gpt-4",
                  "messages": [
                    {"role": "system", "content": "You are a helpful assistant."},
                    {"role": "user", "content": "$prompt"}
                  ],
                  "temperature": 1,
                  "max_tokens": 256,
                  "top_p": 1,
                  "frequency_penalty": 0,
                  "presence_penalty": 0
                }
                """.trimIndent().toRequestBody(mediaType)
        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .post(body)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $apiKey")
            .build()

        try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val resBody = response.body?.string()
                response.close()
                // parse the response to json
                val jsonResponse = JsonParser.parseString(resBody).asJsonObject
                val keys = jsonResponse.getAsJsonArray("choices").get(0).asJsonObject.get("message").asJsonObject.get("content").asString
                return keys ?: "Error: Empty response"
            }
        } catch (e: Exception) {
            return "Error: ${e.message}"
        } finally {
            // Clear the connection pool
            client.connectionPool.evictAll()
        }


    return "Error: Unknown"
    }
}

@Configuration
class StrategyConfig {

    @Bean
    fun openAIRequest(): AIRequestStrategy {
        return OpenAIRequest()
    }

}
