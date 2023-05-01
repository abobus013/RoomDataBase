package com.example.roomdatabase.domain

import com.example.roomdatabase.data.dao.NotesDao
import com.example.roomdatabase.data.model.MyNote

class MainRepository(val dao: NotesDao) {

    suspend fun getAllNotes() = dao.getAllNotes()

    suspend fun addNote(myNote: MyNote) = dao.addNote(myNote)

    suspend fun updateNotes(myNote: MyNote) = dao.updateNotes(myNote)

    suspend fun deleteNote(myNote: MyNote) = dao.deleteNote(myNote)

    suspend fun getNoteById(query: Int) = dao.getNoteById(query)

    suspend fun getNoteByIdName(query: String) = dao.searchNoteByName(query)

}