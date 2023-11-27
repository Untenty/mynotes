package com.untenty.mynotes.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class Note(var id: Int, var text: String, var preview: String)