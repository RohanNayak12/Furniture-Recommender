package com.furniture.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.furniture.app.data.IkeaAPI
import com.furniture.app.ui.theme.FurnitureTheme
import com.furniture.app.ui.theme.screens.NavigationComposable
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

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
                val navController= rememberNavController()
                NavigationComposable(LocalContext.current,navController,supabase)
                //DesignAssistantScreen2(supabase)
                /*
                val item= IkeaApiSerialization(
                    image = "https://www.ikea.com/in/en/images/products/herrakra-armchair-diseroed-dark-yellow__1213667_pe911203_s5.jpg",
                    name = "test",
                    url = "https://www.pexels.com/photo/turned-on-black-torchiere-lamp-112811/",
                    typeName = "type test",
                    price = Price(
                        currentPrice = 33
                    )
                )
                FurnitureItemRow(item=item,{})

                 */
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val ob= IkeaAPI()
    /*
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
                //ob.apiCall("blue sofa")
            }
        }
        ) { Text("Button")}
    }

     */
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FurnitureTheme {
        Greeting("Android")
    }
}