package com.furniture.app.database

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys


@Serializable
@JsonIgnoreUnknownKeys
data class Furniture(
    val name: String="",
    val type: String="",
    val style: String="",
    val color: String=""
)
