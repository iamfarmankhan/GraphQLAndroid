package net.farman.graphqldemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import net.farman.graphqldemo.movie.CountryListScreen
import net.farman.graphqldemo.ui.theme.GraphQLDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GraphQLDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CountryListScreen(modifier = Modifier.padding(innerPadding)) { id ->

                    }
                }
            }
        }
    }
}



