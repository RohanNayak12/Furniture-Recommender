package com.furniture.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.furniture.app.data.IkeaAPI
import com.furniture.app.data.LlmActivity
import com.furniture.app.ui.theme.FurnitureTheme
import com.furniture.app.ui.theme.screens.CameraScreen
import com.furniture.app.ui.theme.screens.DesignAssistantScreen
import com.furniture.app.ui.theme.screens.DesignAssistantScreen2
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val supabase = createSupabaseClient(
            supabaseUrl = "https://iuxaxmrgdlcafhowpmod.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Iml1eGF4bXJnZGxjYWZob3dwbW9kIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTAxNDUxNzYsImV4cCI6MjA2NTcyMTE3Nn0.POxAKz8GJiONqsPrME2PilrQUtEzagpJNZEcE3Lep4w"
        ) {
            install(Postgrest)
            install(Storage)
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FurnitureTheme {
                /*
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                 */
                //HomeScreen()
                //CameraScreen(supabase)
                //Greeting("John")
                DesignAssistantScreen2(supabase)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val ob= IkeaAPI()
    Row(
        Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                ob.apiCall("blue sofa")
            }
        }
        ) { Text("Button")}
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FurnitureTheme {
        Greeting("Android")
    }
}