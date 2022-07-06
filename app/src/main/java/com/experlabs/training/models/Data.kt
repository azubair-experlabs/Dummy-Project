package com.experlabs.training.models

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("data")
    var memes : Memelist? = null
)