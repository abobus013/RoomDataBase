package com.example.roomdatabase.repositories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.roomdatabase.data.DataBase
import com.example.roomdatabase.data.model.MyNote
import com.example.roomdatabase.domain.MainRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val repo = MainRepository(DataBase.getInstance(application).getNotesDao())

    private val _getAllNotesLifeData= MutableLiveData<List<MyNote>>()

    val getAllNotesLifeData: LiveData<List<MyNote>> get() = _getAllNotesLifeData

    suspend fun getAllNotes() {
        _getAllNotesLifeData.value = repo.getAllNotes()
    }

    suspend fun searchNoteByName(query: String) {
        _getAllNotesLifeData.value = repo.getNoteByIdName(query)
    }

    suspend fun searchNoteById(query: Int) {
        repo.getNoteById(query)
    }

    suspend fun addNote(note: MyNote) {
        repo.addNote(note)
    }

    suspend fun updateNotes(note: MyNote) {
        repo.updateNotes(note)
    }

    suspend fun deleteNote(note: MyNote) {
        repo.deleteNote(note)
    }

    init {
        viewModelScope.launch {
            getAllNotes()
        }
    }

}