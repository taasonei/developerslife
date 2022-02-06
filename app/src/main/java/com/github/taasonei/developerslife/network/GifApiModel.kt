package com.github.taasonei.developerslife.network

import com.squareup.moshi.Json

data class GifApiModel(
    @field:Json(name = "id") val id: Long?,
    @field:Json(name = "description") val description: String?,
    @field:Json(name = "gifURL") val gifURL: String?
)
