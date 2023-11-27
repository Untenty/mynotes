package com.untenty.mynotes.navigation

sealed class Screen(val route: String) {
    object NotesList : Screen(ROUTE_NOTES_LIST)
    object NoteCard : Screen(ROUTE_NOTE)
    object Settings : Screen(ROUTE_SETTINGS)

    private companion object {
        const val ROUTE_NOTES_LIST = "NotesList"
        const val ROUTE_NOTE = "NoteCard"
        const val ROUTE_SETTINGS = "Settings"
    }

}