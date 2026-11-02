package com.utez.runtrack.model

import kotlin.math.abs
import kotlin.math.max


class FiltroLecturas(private val umbral: Float, private val intervaloMs: Long) {

    private var ultima: LecturaSensor? = null
    private var ultimoTiempo = 0L

    fun aceptar(nueva: LecturaSensor, ahoraMs: Long): Boolean {
        val anterior = ultima
        if (anterior != null) {
            if (ahoraMs - ultimoTiempo < intervaloMs) {
                return false
            }
            val cambio = max(
                max(abs(nueva.x - anterior.x), abs(nueva.y - anterior.y)),
                abs(nueva.z - anterior.z)
            )
            if (cambio < umbral) {
                return false
            }
        }
        ultima = nueva
        ultimoTiempo = ahoraMs
        return true
    }

    fun reiniciar() {
        ultima = null
        ultimoTiempo = 0L
    }
}
