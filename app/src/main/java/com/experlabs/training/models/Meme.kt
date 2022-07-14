package com.experlabs.training.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memes")
data class Meme(

    @PrimaryKey var id: String = "",
    var name: String = "",
    var url: String = "",
    var width: String = "",
    var height: String = "",
    var box_count: String = "",
)
