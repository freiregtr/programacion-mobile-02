package duoc.desarrollomobile.kotlin.semana2

fun main(){
    // la funcion main es un entrypoint o un punto de entrada al aplicativo
    // un entry point les permite conoces cuando un archivo puede ejecutar codigo
    // con la flecha verde al lado de los bricket
    println("hola mundo")

    // DIFERENCIA ENTRE VAL Y VAR
    // VAL: se usa para valores que no se van a cambiar, parecido a constantes,
    // algo asi como READ ONLY
    // en java, esto es parecido a Final y son valores inmutalbles
    // pi es un buen ejemplo de valor inmutable, pi es una constante, jamas cambia
    val pi = 3.1415926
    print(pi)
    val numero = 1
    //numero = 2 // si descomentan el codigo, no van a poder cambiar el valor

    // en Var, si podemos definir este espacio en memoria para por ejemplo, cambiar la edad
    // ya que la referencia en memoria si puede ser mutable
    var miEdad = 36
    println(miEdad)
    // si la variable de abajo no esta, va a sugerir cambiar a val, pero como esta, la sugerencia desapaqrece
    miEdad = 37
    println(miEdad)


}