package com.example.roomdatabase.data.dao

import androidx.room.*
import com.example.roomdatabase.data.model.MyNote

@Dao
interface NotesDao {

    @Query("SELECT * FROM table_note")
    suspend fun getAllNotes(): List<MyNote>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(myNote: MyNote)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNotes(myNote: MyNote)

    @Query("SELECT * FROM table_note WHERE id=:id")
    suspend fun getNoteById(id: Int): MyNote

    @Delete
    suspend fun deleteNote(myNote: MyNote)

    @Query("SELECT * FROM table_note WHERE name LIKE '%' || :searchText || '%' OR surname LIKE '%' || :searchText || '%'")
    suspend fun searchNoteByName(searchText: String): List<MyNote>

}