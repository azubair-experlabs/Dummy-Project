package com.experlabs.training.models

import com.google.gson.annotations.SerializedName

data class Memelist (
    @SerializedName("memes")
    var memelist : List<Meme>? = null
)