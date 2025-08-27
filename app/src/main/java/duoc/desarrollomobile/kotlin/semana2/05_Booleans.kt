package duoc.desarrollomobile.kotlin.semana2

// documentacion boolean
// https://kotlinlang.org/docs/booleans.html

fun main(){
    //

    /*
        || – disjunction (logical OR) u operador O logico
        Para viajar gratis en el metro basta con ser estudiante o ser adulto mayor
         - Si eres estudiante -> Viajas gratis
         - Si eres tercera edad -> Viajas gratis
         - si eres un adulto -> NO viajas gratis
         - si eres los dos -> Viajas gratis

        && – conjunction (logical AND) o Y Logico
        Para aprobar el curso necesitas entregar el proyecto y dar la prueba final
         - Si entregas el proyecto y das la prueba final -> APROBADO
         - Si falta cualquiera - REPROBADO


        ! – negation (logical NOT)
        Si no tienes entrada, no puedes entrar al cine
         - Entrada en mano = true -> Puedes entrar
         - Entrada en mano = false -> No puedes entrar
     */

    val myTrue: Boolean = true
    val myFalse: Boolean = false
    val boolNull: Boolean? = null  // el ? significa que puede ser true, false o null

    println(myTrue || myFalse) // operador OR
    // true
    // porque "||" es "O" lógico: basta que uno sea true para que todo sea true.
    // aquí tenemos true O false → resultado = true

    println(myTrue && myFalse) // operador AND
    // false
    // porque "&&" es "Y" lógico: los dos deben ser true para que todo sea true.
    // aquí tenemos true Y false → resultado = false

    println(!myTrue)
    // false
    // "!" significa negación (lo contrario).
    // myTrue = true → !true = false

    println(boolNull)
    // null
    // porque declaramos un Boolean? (nullable).
    // no tiene valor true ni false asignado, así que imprime null.
}
