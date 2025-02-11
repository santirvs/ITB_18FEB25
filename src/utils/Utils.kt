package utils

fun ajustarAncho(valor:String, ancho: Int, izquierda:Boolean = true, car:Char = ' ') : String {

    var result : String = ""

    if (izquierda) {
        result = car.toString().repeat(ancho) + valor
        result = result.substring(result.length- ancho)
    }
    else {
        result = valor + car.toString().repeat(ancho)
        result = result.substring(0, ancho)
    }
    return result
}