package com.untenty.mynotes.domain

import com.untenty.mynotes.domain.entities.Note

class NotesUseCase(private val rep: Rep) {

    fun getNote(id: Int): Note {
        return rep.getNote(id)
    }

    fun getNotes(): List<Note> {
        return rep.getNotes()
    }

    fun createNote(text: String): Int {
        return rep.createNote(text)
    }

    fun removeNote(id: Int) {
        rep.removeNote(id)
    }

    fun changeNote(id: Int, text: String) {
        rep.changeNote(id, text)
    }

}