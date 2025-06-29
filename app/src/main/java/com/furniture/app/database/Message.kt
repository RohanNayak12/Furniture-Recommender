package com.furniture.app.database

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys


@Serializable
@JsonIgnoreUnknownKeys
data class Message(
    val role: String,
    val content: String
)
