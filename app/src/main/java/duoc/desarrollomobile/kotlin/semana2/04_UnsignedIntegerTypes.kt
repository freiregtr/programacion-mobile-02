package duoc.desarrollomobile.kotlin.semana2

// documentacion Unsigned integer types
// https://kotlinlang.org/docs/unsigned-integer-types.html

fun main(){
    // Unsigned integer types nos permite el mismo rango que variables numericas anteriores
    // sin embargo, esto nos permite recorrer desde 0 hasta los numeros positivos

    // la edad de uno jamas va a ser negativo, por ende es un buen ejemplo
    // para que sea parseado de forma exitosa, debe anadirse una u igual que en float
    val miEdad : UShort = 36u
    println(miEdad)

    // otro muy buen ejemplo para usar este tipo de datos es para representar
    // constantes hexadecimales como colores

    data class Color(val representation: UInt)
    val yellow = Color(0xFFCC00CCu)
}