package com.ipn.jump

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ipn.jump.session.SessionActivity
import com.ipn.jump.ui.theme.JumpTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JumpTheme {
                App()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun App() {
    val context = LocalContext.current
    val viewModel: VM = viewModel()
    val navController = rememberNavController()
    var currentDestination by remember{ mutableStateOf("Hogar")}
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentDestination) },
                navigationIcon = {
                    val mainDestinations = listOf("Hogar", "Explorar", "Perfil")
                    if (mainDestinations.contains(currentDestination)){
                        // TODO
                        /* IconButton(onClick = {
                            /*TODO: Add the dynamic changes*/
                            navController.navigate("Explorar")
                        }) {
                            Icon(Icons.Outlined.Menu, null)
                        }*/
                    }else {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.Outlined.ArrowBack, null)
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("Buscador") }) {
                        Icon(Icons.Outlined.Search, "Buscar cartas")
                    }
                }
            )
        },
        content = { padding -> Nav(navController, padding, viewModel, context) },
        //bottomBar = { MainNavigationBar(navController) }
    )
    navController.addOnDestinationChangedListener{_, destination, _ ->
        currentDestination = destination.route.toString()
    }
}

@Composable
private fun Nav(
    navController: NavHostController,
    padding: PaddingValues,
    viewModel: VM,
    context: Context
){
    val navigateToSession = {
        val intent = Intent(context, SessionActivity::class.java)
        intent.putExtra("path", viewModel.path)
        context.startActivity(intent)
    }

    NavHost(navController = navController, startDestination = "Hogar", Modifier.padding(padding)){
        composable("Hogar"){
            HomeScreen(onNavigateToMateria = { materiaPath ->
                viewModel.updatePath(materiaPath)
                viewModel.getMateriaFromPath()
                viewModel.retrieveUnidades()
                navController.navigate("Materia")
            },
                viewModel = viewModel
            )
        }
        composable("Materia") {
            MateriaScreen(
                onStudyClicked = { path ->
                    viewModel.updatePath(path)
                    navigateToSession()
                },
                onParcialClicked = { parcialPath ->
                    viewModel.updatePath(parcialPath)
                    viewModel.getUnidadFromPath()
                    viewModel.retrieveTemas()
                    navController.navigate("Parcial")
                },
                viewModel = viewModel
            )
        }
        composable("Parcial") {
            ParcialScreen(
                onClick = { path ->
                    viewModel.updatePath(path)
                    navigateToSession()
                },
                onLongClick = { temaId ->
                    viewModel.updatePath(temaId)
                    val intent = Intent(context, DeveloperActivity::class.java)
                    intent.putExtra("path", viewModel.path)
                    context.startActivity(intent)
                },
                viewModel = viewModel
            )
        }

        composable("Buscador"){
            viewModel.retrieveCardsPreview()
            BrowserScreen(viewModel, onClick = {
                val intent = Intent(context, DeveloperActivity::class.java)
                intent.putExtra("id", it)
                context.startActivity(intent)
            })
        }
        composable("Nueva Sesión"){
            /*TODO: navigate to the actual screen*/
        }
        composable("Explorar"){
            ExploreScreen()
        }
        composable("Perfil"){
            ProfileScreen()
        }
    }
}

@Composable
private fun MainNavigationBar(navController: NavHostController) {
    var selectedItem by remember { mutableStateOf("Hogar") }
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Home, contentDescription = null) },
            label = { Text(stringResource(R.string.hogar)) },
            selected = selectedItem == "Hogar",
            onClick = {
                selectedItem = "Hogar"
                navController.navigate("Hogar")
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Search, contentDescription = null) },
            label = { Text(stringResource(R.string.explorar)) },
            selected = selectedItem == "Explorar",
            onClick = {
                selectedItem = "Explorar"
                navController.navigate("Explorar")
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.AccountCircle, contentDescription = null) },
            label = { Text(stringResource(R.string.perfil)) },
            selected = selectedItem == "Perfil",
            onClick = {
                selectedItem = "Perfil"
                navController.navigate("Perfil")
            }
        )
    }
}