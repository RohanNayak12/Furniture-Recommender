package com.furniture.app.ui.theme.screens

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.furniture.app.database.HelperClass1
import com.furniture.app.database.Routes
import io.github.jan.supabase.SupabaseClient
import java.net.URLDecoder

@Composable
fun NavigationComposable(
    context: Context,
    navController: NavHostController,
    supabase: SupabaseClient
){
    val helperClass1= HelperClass1()
    val routes= Routes()
    val startDestination= routes.designAssistant

    var imgUrl: String =helperClass1.retrieveUrl()
    var json : String =helperClass1.retrieveJson()

    NavHost(navController=navController, startDestination = startDestination){
        composable(routes.designAssistant){
            DesignAssistantScreen2(supabase,navController,helperClass1)
        }
        composable(
            route = "${routes.furnitureRecommendation}/{Json}/{Url}",
            arguments = listOf(
                navArgument("Json") { type= NavType.StringType },
                navArgument("Url") { type= NavType.StringType }
            )
        ){  backStackEntry ->
            val jsonFromNav = URLDecoder.decode(
                backStackEntry.arguments?.getString("Json") ?: "",
                "UTF-8"
            )
            val urlFromNav = URLDecoder.decode(
                backStackEntry.arguments?.getString("Url") ?: "",
                "UTF-8"
            )

            DesignPreviewScreen(supabase=supabase, imgUrl = urlFromNav, json = jsonFromNav,navcontroller= navController,helperClass1=helperClass1)
        }
    }
}