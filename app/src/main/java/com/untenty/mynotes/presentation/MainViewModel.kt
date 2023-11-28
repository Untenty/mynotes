package com.untenty.mynotes.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.untenty.mynotes.data.RepImpl
import com.untenty.mynotes.domain.NotesUseCase
import com.untenty.mynotes.domain.SettingsUseCase
import com.untenty.mynotes.domain.entities.Note

class MainViewModel : ViewModel() {

    private val notesUseCase = NotesUseCase(RepImpl)
    private val settingsUseCase = SettingsUseCase(RepImpl)

    private val _selectedNote = MutableLiveData<Note>()
    val selectedNote: LiveData<Note> = _selectedNote

    fun selectNote(id: Int) {
        _selectedNote.value = notesUseCase.getNote(id)
    }

    fun getNotesList(): List<Note> {
        return notesUseCase.getNotes()
    }

    fun changeNote(id: Int, text: String) {
        return notesUseCase.changeNote(id, text)
    }

    fun createNote(text: String): Int {
        return notesUseCase.createNote(text)
    }

    fun removeNote(id: Int) {
        notesUseCase.removeNote(id)
    }

    fun saveSettings(url: String) {
        settingsUseCase.saveAppSettings(url)
    }

    fun getUrl(): String {
        return RepImpl.settings.url
    }

}