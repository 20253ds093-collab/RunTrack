package com.example.runtrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.utez.runtrack.view.PantallaSensores
import com.utez.runtrack.viewmodel.SensoresViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: SensoresViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PantallaSensores(viewModel)
                }
            }
        }
    }
// se apaga el sensor al salir de la app
    override fun onPause() {
        super.onPause()
        viewModel.alPausar()
    }
// se activa al regresar
    override fun onResume() {
        super.onResume()
        viewModel.alReanudar()
    }
}
