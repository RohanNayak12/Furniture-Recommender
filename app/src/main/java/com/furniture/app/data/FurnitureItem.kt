package com.furniture.app.data

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys


@Serializable
@JsonIgnoreUnknownKeys
data class FurnitureItemFromLLM(
    @Required
    val name: String?,
    val description: String?,
    val type: String?="",
    val color: String?=""
)
