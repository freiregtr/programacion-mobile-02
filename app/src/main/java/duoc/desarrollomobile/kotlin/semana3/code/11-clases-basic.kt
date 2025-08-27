package duoc.desarrollomobile.kotlin.semana3.code

// documentacion de clases en kotlin
// https://kotlinlang.org/docs/classes.html

/*
Las clases permiten representar los planos de objetos o instancias
del mundo real al mundo del desarrollo de software, es parte del
paradigma de programacion orientado a objetos (POO) io tambien conocido
como Oriented Object Programming (OOP)

En la medida que avancen con estas secciones veran aspectos mas
avanzados como herencia, polimosfirmo, abstraccion, etc
*/

// Para empezar, el modificador para iniciar una clase es claqss
class Persona{
    // toda clase tiene su constructor, o al menos como en Java o C++
    // se conoce, aqui se llama init(), es un metodo constructor
    // y lo que hace es iniciar segun los parametros que tenga
    // o que les sean asignados
    // vamos a crear un metodo simple que simule que una persona camine
    init{
        caminar()
    }

    // podemos anadir varios metodos y atributos, por ahora, solo
    // anadiremos un metodo que se llama caminar
    fun caminar(){
        println("Mueve pierna izquierda")
        println("mueve pierna derecha")
    }
}

// Por ultimo en nuestro main podemos crear el objeto
// o la instancia de la clase persona

fun main(){
    //
    var Juanito = Persona()
    // como Persona() no recibe parametros, solo construye ek objeto
    // con el metodo caminar, simulamos un comportamiento de la vida real
}