package com.untenty.mynotes.domain

import com.untenty.mynotes.domain.entities.Note
import com.untenty.mynotes.domain.entities.AppSettings

interface Rep {
    fun getNotes(): List<Note>
    fun getNote(id: Int): Note
    fun createNote(text: String): Int
    fun removeNote(id: Int)
    fun changeNote(id: Int, text: String)
    fun saveAppSettings(ip: String)
    fun readAppSettings(): AppSettings
    fun getAppSettings(): AppSettings
}