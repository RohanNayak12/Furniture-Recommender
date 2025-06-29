package com.furniture.app.database

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys


@Serializable
@JsonIgnoreUnknownKeys
data class ApiResponse(
    val id: String,
    val `object`: String,
    val choices: List<Choice>
)

@Serializable
@JsonIgnoreUnknownKeys
data class Choice (
    val message: Message
)
