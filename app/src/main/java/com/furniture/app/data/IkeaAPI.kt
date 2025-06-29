package com.furniture.app.data

import android.util.Log
import com.furniture.app.database.IkeaApiSerialization
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.Request
import java.util.concurrent.TimeUnit

class IkeaAPI {
    val client = HttpClient(OkHttp){
        install(ContentNegotiation){
            json(Json { ignoreUnknownKeys=true;prettyPrint=true })
        }
        engine {
            config {
                connectTimeout(60, TimeUnit.SECONDS)
                readTimeout(60, TimeUnit.SECONDS)
                writeTimeout(60, TimeUnit.SECONDS)
            }
        }
    }

    suspend fun apiCall(keyword: String){
        var keyString=keyword.replace(" ","%20")
        val response: HttpResponse =client
            .get("https://ikea-api.p.rapidapi.com/keywordSearch"){
                headers {
                    append("x-rapidapi-host", "ikea-api.p.rapidapi.com")
                    append("x-rapidapi-key", "f417cd8958mshb0eaf99c377893dp1bfcb2jsn96a512ef0987")
                }
                parameter("keyword",keyString)
                parameter("countryCode","in")
                parameter("languageCode", "en")
            }

        val jsonString=response.bodyAsText()
        Log.d("ikea",jsonString)

        var list= Json.decodeFromString<List<IkeaApiSerialization>>(jsonString)
        list=list.take(4)
        list.forEach {
            Log.d(it.name,"${it.price.currentPrice} and ${it.typeName}")
        }
    }
}