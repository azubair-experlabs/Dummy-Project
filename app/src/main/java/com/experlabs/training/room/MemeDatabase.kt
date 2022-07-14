package com.experlabs.training.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.experlabs.training.models.Meme


@Database(entities = [Meme::class], version = 1)
abstract class MemeDatabase : RoomDatabase() {

    abstract fun dao(): MemeDAO?

    companion object {
        private var instance: MemeDatabase? = null

        fun getInstance(context: Context): MemeDatabase? {
            if (instance == null) {

                instance = Room.databaseBuilder(
                    context.applicationContext,
                    MemeDatabase::class.java,
                    "memes_database"
                )
                    .fallbackToDestructiveMigration().addCallback(roomCallback)
                    .build()
            }
            return instance
        }

        // below line is to create a callback for our room database.
        private val roomCallback: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
            }
        }
    }


}