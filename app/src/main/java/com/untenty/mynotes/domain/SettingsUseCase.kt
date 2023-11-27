package com.untenty.mynotes.domain

import com.untenty.mynotes.domain.entities.AppSettings

class SettingsUseCase(private val rep: Rep) {

    fun saveAppSettings(ip: String) {
        rep.saveAppSettings(ip)
    }

    fun readAppSettings(): AppSettings {
        return rep.readAppSettings()
    }

    fun getAppSettings(): AppSettings {
        return rep.getAppSettings()
    }

}