package com.furniture.app.database

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys


@Serializable
@JsonIgnoreUnknownKeys
data class ChatRequest(
    val model: String,
    val messages: List<Message>,
    val stream: Boolean=false
)
