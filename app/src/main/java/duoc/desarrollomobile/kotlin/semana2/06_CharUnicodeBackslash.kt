package duoc.desarrollomobile.kotlin.semana2

// documentacion
// https://kotlinlang.org/docs/characters.html

fun main(){
    // un char nos permite almacenar solo un caracter
    // quizas en un programa pequeno esto no es relevante, pero en
    // millones de datos, esto es muy relevante, sobre todo en bases de datos
    // whatsapp ahora almacena el char del nombre y apellido y lo muestra
    val myChar = 'a'
    println(myChar)

    // tambien nos permite operar UNICODEs
    // https://en.wikipedia.org/wiki/Unicode
    // https://en.wikipedia.org/wiki/List_of_Unicode_characters
    // con backslash podemos escapar y usar cualquer unicode que queramos
    // U+005E 	^ 	94 	0136 	Circumflex accent
    val charCircumflex = '\u005E'
    println(charCircumflex)


}