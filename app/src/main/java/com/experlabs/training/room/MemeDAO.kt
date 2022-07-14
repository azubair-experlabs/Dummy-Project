package com.experlabs.training.room

import androidx.lifecycle.LiveData
import androidx.room.*

import com.experlabs.training.models.Meme

@Dao
interface MemeDAO {

    @Query("SELECT * FROM memes")
    suspend fun getAll(): List<Meme>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(memes: List<Meme>)

    @Delete
    suspend fun delete(meme: Meme)

    @Query("DELETE FROM memes")
    suspend fun deleteAll()
}