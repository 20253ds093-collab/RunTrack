package com.utez.runtrack.model

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager


class SensorRepositorio(context: Context) {

    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
   // regresa null si el dispositivo no tiene ese sensor
    private val acelerometro: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val giroscopio: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

    private var listener: SensorEventListener? = null

    fun acelerometroDisponible(): Boolean = acelerometro != null

    fun giroscopioDisponible(): Boolean = giroscopio != null

    fun iniciar(alRecibir: (Int, LecturaSensor) -> Unit) {
        if (listener != null) return                      // ya estaba activo

        val escucha = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val lectura = LecturaSensor(event.values[0], event.values[1], event.values[2])
                alRecibir(event.sensor.type, lectura)
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            }
        }
        // para actualizar la pantalla
        acelerometro?.let { sensorManager.registerListener(escucha, it, SensorManager.SENSOR_DELAY_UI) }
        giroscopio?.let { sensorManager.registerListener(escucha, it, SensorManager.SENSOR_DELAY_UI) }
        listener = escucha
    }
//apaga
    fun detener() {
        listener?.let { sensorManager.unregisterListener(it) }
        listener = null
    }
}
