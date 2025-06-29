package com.furniture.app.data

import android.util.Log
import com.furniture.app.database.ApiResponse
import com.furniture.app.database.ChatRequest
import com.furniture.app.database.Furniture
import com.furniture.app.database.Message
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit

class LlmActivity {
    val client= HttpClient(OkHttp){
        install(ContentNegotiation){
            json(Json{ignoreUnknownKeys=true;prettyPrint=true})
        }
        engine {
            config {
                connectTimeout(60, TimeUnit.SECONDS)
                readTimeout(60, TimeUnit.SECONDS)
                writeTimeout(60, TimeUnit.SECONDS)
            }
        }
    }

    suspend fun furnitureRecommendation(img_url: String,style: String){
        val requestBody= ChatRequest(
            model = "lgai/exaone-deep-32b",
            messages = listOf(
                Message(
                    role = "user",
                    content = """
                    Return ONLY raw JSON in the output. Do NOT include any thoughts, explanations, markdown (no ```), or comments. Just return valid JSON array.
                    image_url : $img_url
                    prompt: just simply suggest me top/best 4 furnitures details which will match in this room in $style style. Return output in Json Format.
                """.trimIndent()
                )
            ),
            stream = false
        )

        val response: HttpResponse=client.post("https://api.together.xyz/v1/chat/completions"){
            header("Authorization","Bearer 020b68e85f75861094fb604b410b4e41c48060a1be29c37bf8736bf0db019307")
            contentType(io.ktor.http.ContentType.Application.Json)
            setBody(requestBody)
        }
        var responseText = response.bodyAsText()
        var apiResponse= Json.decodeFromString<ApiResponse>(responseText)
        val finalApiResponse=apiResponse.choices.first().message.content
        val finalJsonOnly=finalApiResponse.substringAfter("</thought>").trim()
        val list: List<Furniture> = Json.decodeFromString<List<Furniture>>(finalJsonOnly)
        Log.d("Final Json",finalJsonOnly)
        list.forEach {
            Log.d(it.name,it.style)
        }
        //Log.d("final","$finalJsonOnly")
        Log.d("Status ssss",response.status.toString())

    }
}