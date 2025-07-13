package com.furniture.app.database

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys


@Serializable
@JsonIgnoreUnknownKeys
data class AliExpressSerialization(
    @SerialName("status")
    val status: AliExpressStatus,

    @SerialName("data")
    val data: AliExpresData
)

@Serializable
@JsonIgnoreUnknownKeys
data class AliExpressStatus (
    @SerialName("code")
    val code: Int = 0,

    @SerialName("message")
    val message: String = ""
)


@Serializable
@JsonIgnoreUnknownKeys
data class AliExpresData (
    @SerialName("pageIndex")
    val pageIndex: Int = 0,

    @SerialName("pageSize")
    val pageSize: String = "",

    @SerialName("totalCount")
    val totalCount: Int = 0,

    @SerialName("itemList")
    val itemList: List<Product> = emptyList()
)

@Serializable
@JsonIgnoreUnknownKeys
data class Product(
    @SerialName("title")
    val name: String = "",

    @SerialName("itemMainPic")
    val imgUrl: String = "",

    @SerialName("salePriceFormat")
    val price: String = "",

    @SerialName("salePrice")
    val salePrice: String = "",
)
