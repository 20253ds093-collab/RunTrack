package com.utez.runtrack.viewmodel

import android.app.Application
import android.hardware.Sensor
import android.os.SystemClock
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.utez.runtrack.model.EstadoSensores
import com.utez.runtrack.model.FiltroLecturas
import com.utez.runtrack.model.LecturaSensor
import com.utez.runtrack.model.SensorRepositorio
import kotlin.math.abs
import kotlin.math.sqrt

class SensoresViewModel(app: Application) : AndroidViewModel(app) {

    private val repositorio = SensorRepositorio(app)

    private val filtroAcelerometro = FiltroLecturas(umbral = 0.2f, intervaloMs = 100)
    private val filtroGiroscopio = FiltroLecturas(umbral = 0.1f, intervaloMs = 100)

    private var activoAntesDePausa = false

    var estado by mutableStateOf(
        EstadoSensores(
            acelerometroDisponible = repositorio.acelerometroDisponible(),
            giroscopioDisponible = repositorio.giroscopioDisponible()
        )
    )
        private set

    fun iniciar() {
        if (!estado.acelerometroDisponible && !estado.giroscopioDisponible) return
        filtroAcelerometro.reiniciar()
        filtroGiroscopio.reiniciar()
        repositorio.iniciar { tipo, lectura -> procesar(tipo, lectura) }
        estado = estado.copy(monitoreando = true)
    }

    fun detener() {
        repositorio.detener()
        estado = estado.copy(monitoreando = false)
    }

    fun alPausar() {
        activoAntesDePausa = estado.monitoreando
        detener()
    }

    fun alReanudar() {
        if (activoAntesDePausa) {
            activoAntesDePausa = false
            iniciar()
        }
    }

    override fun onCleared() {
        repositorio.detener()
    }

    private fun procesar(tipo: Int, lectura: LecturaSensor) {
        val esAcelerometro = tipo == Sensor.TYPE_ACCELEROMETER
        val filtro = if (esAcelerometro) filtroAcelerometro else filtroGiroscopio
        val recibidas = estado.lecturasRecibidas + 1

        if (!filtro.aceptar(lectura, SystemClock.elapsedRealtime())) {
            estado = estado.copy(lecturasRecibidas = recibidas)   // se cuenta, pero no se muestra
            return
        }
        val aceptadas = estado.lecturasAceptadas + 1

        estado = if (esAcelerometro) {
            val magnitud = sqrt(lectura.x * lectura.x + lectura.y * lectura.y + lectura.z * lectura.z)
            val movimiento = if (abs(magnitud - 9.81f) > 1.5f) "En movimiento" else "En reposo"
            estado.copy(
                acelerometro = lectura, estadoMovimiento = movimiento,
                lecturasRecibidas = recibidas, lecturasAceptadas = aceptadas
            )
        } else {
            val direccion = if (abs(lectura.z) > 1.0f) "Cambio de dirección" else "Dirección estable"
            estado.copy(
                giroscopio = lectura, cambioDireccion = direccion,
                lecturasRecibidas = recibidas, lecturasAceptadas = aceptadas
            )
        }
    }
}
