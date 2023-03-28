package com.example.roomdatabase.data

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class Student(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val surname: String,
   //@DrawableRes val image: Int
)
