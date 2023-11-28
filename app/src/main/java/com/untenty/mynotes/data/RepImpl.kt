package com.untenty.mynotes.data

import android.content.SharedPreferences
import android.util.Base64
import android.util.Log
import com.untenty.mynotes.domain.Rep
import com.untenty.mynotes.domain.entities.AppSettings
import com.untenty.mynotes.domain.entities.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.DataOutputStream
import java.io.OutputStream
import java.net.URL


object RepImpl : Rep {

    private lateinit var prefs: SharedPreferences

    private var listNotes: MutableList<Note> = mutableListOf()
    var settings: AppSettings = AppSettings("")

    fun init(sharedPreferences: SharedPreferences) {
        prefs = sharedPreferences
        listNotes = readNotes()
        settings = readAppSettings()
    }

    override fun getNote(id: Int): Note {
        return listNotes.find { note -> note.id == id }!!
    }

    override fun getNotes(): List<Note> {
        return listNotes
    }

    override fun createNote(text: String): Int {
        val id = (listNotes.lastOrNull()?.id ?: -1) + 1
        listNotes.add(Note(id, text, text))
        saveNotes()
        return id
    }

    override fun removeNote(id: Int) {
        listNotes.remove(listNotes.find { note -> note.id == id }!!)
        saveNotes()
    }

    override fun changeNote(id: Int, text: String) {
        val note = listNotes.find { note -> note.id == id }!!
        note.text = text
        note.preview = text
        saveNotes()
    }

    private fun readNotes(): MutableList<Note> {
        val notesStr = prefs.getString("notes", "") ?: ""
        return if (notesStr != "") {
            Json.decodeFromString(notesStr)
        } else
            mutableListOf()
    }

    private fun saveNotes() {
        CoroutineScope(Dispatchers.IO).launch {
            val editor = prefs.edit()
            editor.putString("notes", Json.encodeToString(listNotes)).apply()
            sendNotes()
        }
    }

    private suspend fun sendNotes() {
        withContext(Dispatchers.IO) {
            val jsonString = Json.encodeToString(getNotes())
            val flags = Base64.URL_SAFE or Base64.NO_WRAP
            val encodedString: String = Base64.encodeToString(jsonString.toByteArray(), flags)
            val url = URL(settings.url)

            val postData = "notes=$encodedString"
            val conn = url.openConnection()

            conn.doOutput = true
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            conn.setRequestProperty("Content-Length", postData.length.toString())

            try {
                val out: OutputStream = conn.getOutputStream()
                val dos = DataOutputStream(out)
                dos.writeBytes(postData)
                //dos.flush()
                conn.getInputStream() //why???
                dos.close()
                out.close()
            } catch (e: Exception) {
                Log.e("notes", e.toString())
            } finally {

            }
        }
    }

    override fun saveAppSettings(ip: String) {
        settings.url = ip
        val editor = prefs.edit()
        editor.putString("settings", Json.encodeToString(settings)).apply()
    }

    override fun readAppSettings(): AppSettings {
        val settingsStr = prefs.getString("settings", "") ?: ""
        return if (settingsStr != "") {
            Json.decodeFromString(settingsStr)
        } else AppSettings("")
    }

    override fun getAppSettings(): AppSettings {
        return settings
    }

}