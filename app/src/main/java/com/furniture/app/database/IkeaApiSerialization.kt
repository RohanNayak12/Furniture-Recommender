package com.furniture.app.database

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys


@Serializable
@JsonIgnoreUnknownKeys
data class IkeaApiSerialization(
    val name: String,
    val price: Price,
    val image: String,
    val typeName: String
)

@Serializable
@JsonIgnoreUnknownKeys
data class Price(
    val currentPrice: Int
)
