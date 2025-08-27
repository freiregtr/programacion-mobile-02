package duoc.desarrollomobile.kotlin.semana2

// documentacion float
// https://kotlinlang.org/docs/numbers.html#floating-point-types

fun main(){

    // podemos usar cadenas de numeros docimales como double o float
    // por defecto, kotlin nuevamente va a guardar todos los datos decimales en double
    val pi = 3.14
    println(pi)

    // pero podemos decirle explicitamente que sea float con la letra f al final del numero
    val piFloat = 3.14f
    println(piFloat)

    // tambien se puede hacer parseando (o cambiando el tipo de dato del archivo) pero se debe
    // anadir la f de float al final del numero igual
    val piFloat2 : Float = 3.14f
    println(piFloat2)

}