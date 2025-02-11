package utils

/**
 * Ajusta el ancho de una cadena de texto
 * @param valor: String - Cadena de texto a ajustar
 * @param ancho: Int - Ancho deseado
 * @param izquierda: Boolean - Si se rellena por la izquierda (true) o por la derecha (false) (por defecto es true)
 * @param car: Char  - Carácter de relleno (por defecto es un espacio)
 */
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