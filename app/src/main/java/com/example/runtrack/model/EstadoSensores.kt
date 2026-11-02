package com.utez.runtrack.model

data class EstadoSensores(
    val acelerometroDisponible: Boolean = false,
    val giroscopioDisponible: Boolean = false,
    val monitoreando: Boolean = false,
    val acelerometro: LecturaSensor = LecturaSensor(0f, 0f, 0f),
    val giroscopio: LecturaSensor = LecturaSensor(0f, 0f, 0f),
    val estadoMovimiento: String = "Sin datos",
    val cambioDireccion: String = "Sin datos",
    val lecturasRecibidas: Int = 0,
    val lecturasAceptadas: Int = 0
)
