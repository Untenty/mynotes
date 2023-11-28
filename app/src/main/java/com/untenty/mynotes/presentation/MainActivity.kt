package com.untenty.mynotes.presentation

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.untenty.mynotes.ui.theme.MynotesTheme
import com.untenty.mynotes.navigation.AppNavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.untenty.mynotes.data.RepImpl
import com.untenty.mynotes.domain.entities.Note
import com.untenty.mynotes.navigation.Screen
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RepImpl.init(getSharedPreferences("settings", Context.MODE_PRIVATE))
        val viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setContent {
            MynotesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val navHostController = rememberNavController()
    AppNavGraph(navHostController,
        notesListScreenContent = { NotesList(viewModel, navHostController) },
        noteCardScreenContent = { NoteCard(viewModel, navHostController) },
        settingsScreenContent = { Settings(viewModel) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesList(viewModel: MainViewModel, navHostController: NavHostController) {

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .width(200.dp)
                    .fillMaxHeight()
            ) {
                Text("Menu", modifier = Modifier.padding(16.dp))
                Divider()
                NavigationDrawerItem(
                    label = { Text(text = "Settings") },
                    icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
                    selected = false,
                    onClick = { navHostController.navigate(Screen.Settings.route); scope.launch { drawerState.close() } }
                )
            }
        },
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text("Notes")
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = { scope.launch { drawerState.open() } }
                            ) { Icon(Icons.Filled.Menu, contentDescription = null) }
                        },
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        shape = CircleShape,
                        onClick = {
                            viewModel.selectNote(viewModel.createNote(""))
                            navHostController.navigate(Screen.NoteCard.route)
                        },
                    ) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "icon")
                    }
                },
                floatingActionButtonPosition = FabPosition.End

            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = it
                ) {
                    items(viewModel.getNotesList()) { note ->
                        NoteItem(viewModel, note, navHostController)
                    }
                }
            }
        }
    )

}

@Composable
fun NoteItem(viewModel: MainViewModel, note: Note, navHostController: NavHostController) {
    OutlinedButton(
        border = BorderStroke(1.dp, Color.Blue), shape = RoundedCornerShape(20),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Blue),
        onClick = { viewModel.selectNote(note.id); navHostController.navigate(Screen.NoteCard.route) },
        modifier = Modifier
            .clip(RectangleShape)
            .padding(10.dp)
    ) {
        Box {
            Text(note.preview, color = Color.Red)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteCard(viewModel: MainViewModel, navHostController: NavHostController) {
    val note = viewModel.selectedNote.value!!
    val text = remember { mutableStateOf(note.text) }
    Column {
        TextField(
            value = text.value,
            onValueChange = { newText -> text.value = newText }
        )
        Button(onClick = {
            viewModel.changeNote(note.id, text.value)
            navHostController.navigate(Screen.NotesList.route)
        }) {
            Text("Ok")
        }
        Button(onClick = {
            viewModel.removeNote(note.id)
            navHostController.navigate(Screen.NotesList.route)
        }) {
            Text("Del")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(viewModel: MainViewModel) {
    val text = remember { mutableStateOf(viewModel.getUrl()) }
    Column {
        TextField(
            value = text.value,
            onValueChange = { newText -> text.value = newText }
        )
        Button(onClick = {
            viewModel.saveSettings(text.value)
        }) {
            Text("Save")
        }
    }
}