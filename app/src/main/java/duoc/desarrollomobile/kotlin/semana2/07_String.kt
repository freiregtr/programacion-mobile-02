package duoc.desarrollomobile.kotlin.semana2

import kotlin.text.iterator

// documentacion
// https://kotlinlang.org/docs/strings.html

fun main(){
    // Por definicion, un string es una coleccion de chars

    val str = "monchito"
    // para probar que esto es una coleccion de caracteres, vamos a recorrerlo con un loop
    for (c in str) {
        println(c)
    }

    // cuando imprimimos nada en una cadena string, quiere decir que esta vacia, pero
    // no NULL, esto es muy importante porque NULL significa no existe
    val cadenaVacia = ""
    println(cadenaVacia)

    // tambien podemos acceder a funciones ya que una cadena string se trata como un objeto
    // y como es un objeto, tiene metodos
    val cadenaDeTexto = "Esto es una cadena de texto lArGa"
    // minusculas
    println(cadenaDeTexto.toLowerCase())
    // MAYUSCULAS
    println(cadenaDeTexto.toUpperCase())

    // tambien podemos concatenar texto, por ejemplo nombre y apellido
    var primerNombre = "Juan"
    var primerApellido = "Perez"
    println(primerNombre + " " + primerApellido)

    // tambien podemos usar quiebres de linea, por ejemplo
    println(primerNombre + "\n" + primerApellido)

    // tambien pudemos usar un espacio como tabulador
    println(primerNombre + "\t" + primerApellido)


}