package com.furniture.app.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys


@Serializable
@JsonIgnoreUnknownKeys
data class FurnitureItem(
    val name: String?,
    val description: String?,
    val type: String?
)
