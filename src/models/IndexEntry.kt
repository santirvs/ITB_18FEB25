package models

import interfaces.IGuardable
import utils.ajustarAncho
import java.io.*
import java.util.*

class IndexEntry  {
    public var codi: Int = 0
    public var posicio : Long = 0L


    constructor(codi:Int, posicio: Long){
        this.codi = codi
        this.posicio = posicio
    }

}
