package duoc.desarrollomobile.kotlin.semana2

// documentacion tipos de numeros
// https://kotlinlang.org/docs/numbers.html#integer-types

fun main(){
    // kotlin almacena todos los numeros por interger, salvo que se indique lo contrario

    // si yo defino mi edad con var, al pasar el mouse se va a ver que lo guarda como Integer
    var miEdad = 654654
    println(miEdad)

    // ahora, si quisiera decirle explicitamente que pueda guardarlo como Byte, si puedo hacerlo
    var miEdad2 : Byte = 35
    println(miEdad2)
    // en pocas palabras lo que estamos haciendo, es un override

    // como kotlin es capaz de detectar de forma automatica estos numeros, entonces
    // los cambia por si solo, por ejemplo en el caso de long
    var miedad3 =  654654654564
    println(miedad3)

    // lo que no se puede hacer es reasignar el tipo de dato cuando este ya tiene definido
    // por ejemplo en este caso, un Integer, no podemos cambiarlo simplemente reasignando el valor
    var miEdad4 = 6546546

    // forma incorrecta
    // miEdad4 = 654654654654654654654 // descomentar
}