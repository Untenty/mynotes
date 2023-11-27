package com.untenty.mynotes.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    notesListScreenContent: @Composable () -> Unit,
    noteCardScreenContent: @Composable () -> Unit,
    settingsScreenContent: @Composable () -> Unit,
) {
    NavHost(
        navController = navHostController, startDestination = Screen.NotesList.route
    ) {
        composable(Screen.NotesList.route) { notesListScreenContent() }
        composable(Screen.NoteCard.route) { noteCardScreenContent() }
        composable(Screen.Settings.route) { settingsScreenContent() }
    }
}