package com.example.roomdatabase.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.roomdatabase.dao.StudentsDao

@Database(entities = [Student::class], version = 1)
abstract class DataBase: RoomDatabase() {

    abstract fun getStudentsDao(): StudentsDao


    companion object {
        const val DATABASE_NAME = "db_name"

        fun getInstance(context: Context): DataBase {
            return Room.databaseBuilder(
                context.applicationContext,
                DataBase::class.java,
                DATABASE_NAME
            ).fallbackToDestructiveMigration().build()
        }
    }

}