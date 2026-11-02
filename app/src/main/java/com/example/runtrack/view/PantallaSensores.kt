package com.utez.runtrack.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.utez.runtrack.model.LecturaSensor
import com.utez.runtrack.viewmodel.SensoresViewModel

@Composable
fun PantallaSensores(viewModel: SensoresViewModel) {
    val estado = viewModel.estado

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("RunTrack", style = MaterialTheme.typography.headlineMedium)
        Text("Monitor de movimiento para corredores")
        Spacer(Modifier.height(12.dp))

        Text("Acelerómetro: " + textoDisponible(estado.acelerometroDisponible))
        Text("Giroscopio: " + textoDisponible(estado.giroscopioDisponible))
        Spacer(Modifier.height(12.dp))

        Row {
            Button(onClick = { viewModel.iniciar() }, enabled = !estado.monitoreando) {
                Text("Iniciar")
            }
            Spacer(Modifier.width(12.dp))
            Button(onClick = { viewModel.detener() }, enabled = estado.monitoreando) {
                Text("Detener")
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(if (estado.monitoreando) "Estado: monitoreando" else "Estado: detenido")
        Spacer(Modifier.height(12.dp))

        TarjetaSensor("Acelerómetro (m/s²)", estado.acelerometro, estado.estadoMovimiento)
        Spacer(Modifier.height(8.dp))
        TarjetaSensor("Giroscopio (rad/s)", estado.giroscopio, estado.cambioDireccion)
        Spacer(Modifier.height(8.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Filtrado de lecturas", style = MaterialTheme.typography.titleMedium)
                Text("Recibidas: ${estado.lecturasRecibidas}")
                Text("Mostradas: ${estado.lecturasAceptadas}")
                Text("Descartadas: ${estado.lecturasRecibidas - estado.lecturasAceptadas}")
            }
        }
    }
}

@Composable
fun TarjetaSensor(titulo: String, lectura: LecturaSensor, resultado: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleMedium)
            Text("X: %.2f".format(lectura.x))
            Text("Y: %.2f".format(lectura.y))
            Text("Z: %.2f".format(lectura.z))
            Text(resultado)
        }
    }
}

private fun textoDisponible(disponible: Boolean): String =
    if (disponible) "disponible" else "no disponible"
