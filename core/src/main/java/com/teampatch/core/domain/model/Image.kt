package com.teampatch.core.domain.model

sealed interface Image {

    data class Uri(val uri: String) : Image

    data class Url(val url: String) : Image
}