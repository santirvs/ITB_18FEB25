package interfaces

import models.Fichero

interface IGuardable {
    fun guardar(f: Fichero)
    fun leer(f: Fichero)
}