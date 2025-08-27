package duoc.desarrollomobile.kotlin.semana3.code

// documentacion properties
// https://kotlinlang.org/docs/properties.html#declaring-properties

/*

En Kotilin, existe un elemento diferenciador de Java que permite ser mas accesible
mediante el uso de properties, estos se declaran cuando se pasan argumentos y
al mismo tiempo tienen acceso global

con Parameters, al solicitar los datos por argumentos no es necesario declarar
el tipo de variable, esto es malo porque no poodemos acceder de forma automatica
pero es bueno porque permite que la operacion de los datos sea segura y prevalece \
el concepto de ENCAPSULACION

Este es un ejemplo de como se construian antes ej Java
String nombre: "juan";
int telefono = 56978472154;
char inicial = 'j';

Luego, en el constructor lo que habia que hacer era simplemente inicializarlo

// Constructor
public Persona(String nombre, long telefono, char inicial) {
    this.nombre = nombre;
    this.telefono = telefono;
    this.inicial = inicial;
}

como consecuencia, se repetia codigo al inicializar y crear el constructor, con
Properties, esto ya no es necesario, se declaran las variables como parametros

*/

fun main(){

    // vamos a crear la instancia de la clase PersonaConParametros
    var juanito = PersonaConProperties("Juanito", "Perez")

    // como estas variables fueron inicializadas durante el constructor,
    // las variables tienen el contexto global durante toda la clase
    println("el nombre es ${juanito.nombre}")
    println("el apellido es ${juanito.apellido}")

    // vamos a construir un objeto pero con parameters
    var pepito = PersonaConParametros("pepito", "perez")
    // La GRAN DIFERENCIA entre Kotlin y Java es que mediante properties,
    // puedes acceder a parametros como nombres de manera global
    // en cambio con Parametros, no puedes, esto va a depender del uso
    // porque cada uso tiene sus razones
    // println("el nombre de la persona es ${pepito.}")

}
// si declaramos los tipos de datos en los parametros, entonces no tenemos
// que declarar variables
class PersonaConProperties (val nombre: String, val apellido : String){

    // aca ya no se declaran variables, estamos usando properties
    // constructor
    init{
        decirNombre()
    }

    fun decirNombre(){
        println("Mi nombre es $nombre")
        println("mi apellido es $apellido")
    }
}

class PersonaConParametros(nombre : String, apellido : String){

    // como no usamos Properties, debemos declarar variables privadas
    private val nombreGuardado : String = nombre
    private val apellidoGuardado : String = apellido

    // podemos asignar las variables directamente que vienen por
    // parametros, de esta forma no es necesario usar un constructor
    // explicito en el caso de declarar variables, mas si metodos

    init {
        decirNombre()
    }

    fun decirNombre(){

        println("Mi nombre es $nombreGuardado")
        println("mi apellido es $apellidoGuardado")
    }

}