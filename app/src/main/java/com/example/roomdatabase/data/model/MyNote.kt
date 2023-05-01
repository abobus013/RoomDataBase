package com.example.roomdatabase.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_note")
data class MyNote(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val surname: String,
   //@DrawableRes val image: Int
)
