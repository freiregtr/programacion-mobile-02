package duoc.desarrollomobile.kotlin.semana3.code

///////////// CLASES Y OBJETOS - MAS EN PROFUNDIDAD /////////////
/*
Ya vimos lo basico de clases en el archivo 11, ahora vamos a profundizar
mas en el concepto de POO (Programacion Orientada a Objetos) y como
se relacionan las clases con los objetos del mundo real

Una clase es como el PLANO o MOLDE para crear objetos. Piensa en una clase
como el plano de una casa, y los objetos son las casas construidas con ese plano
puedes hacer muchas casas con el mismo plano, pero cada una puede tener
caracteristicas distintas como color, numero de ventanas, etc

En terminos tecnicos:
-> CLASE: es la definicion, la estructura, el template
-> OBJETO: es la instancia de una clase, es lo que creamos en memoria
-> PROPIEDADES: son las caracteristicas del objeto (variables)
-> METODOS: son las acciones que puede realizar el objeto (funciones)
*/

// __________________________________________
// ///////////// EJEMPLO 1 - CLASE AUTO /////////////
// __________________________________________

/*
NOTA RAPIDA - CONSTRUCTOR PRIMARIO
En Java para crear una clase Auto necesitarias:
- Declarar variables privadas
- Crear constructor
- Crear getters y setters
- Total: como 20 lineas de codigo

En Kotlin todo eso es UNA linea:
class Auto(val marca: String, val color: String, val anio: Int)

La clave:
- 'val' en constructor = propiedad + getter automatico
- 'var' en constructor = propiedad + getter + setter automatico
- sin val/var = solo parametro (como vimos en archivo 12)
*/

class Auto(val marca: String, val color: String, val anio: Int) {

    // metodo para mostrar los detalles del auto
    fun mostrarDetalles() {
        println("Auto: $marca | Color: $color | Año: $anio")
    }

    // metodos para acciones del auto
    fun acelerar() {
        println("El $marca $color esta acelerando... vroom vroom!")
    }

    fun frenar() {
        println("El $marca $color esta frenando... screech!")
    }

    fun tocarBocina() {
        println("El $marca $color hace: beep beep!")
    }
}

fun main() {
    // EJEMPLO BASICO - CLASE AUTO
    /*
    Vamos a crear una clase Auto para entender bien los conceptos
    Un auto tiene caracteristicas (marca, color, anio) y acciones (acelerar, frenar)
    */

    // creamos varios objetos Auto con diferentes caracteristicas
    val miPrimerAuto = Auto("Toyota", "Rojo", 2020)
    val autoDeMinHermano = Auto("Nissan", "Azul", 2018)
    val autoDelVecino = Auto("Chevrolet", "Blanco", 2023)

    // cada objeto es independiente y tiene sus propias caracteristicas
    println("____________ MIS AUTOS ____________")
    miPrimerAuto.mostrarDetalles()
    autoDeMinHermano.mostrarDetalles()
    autoDelVecino.mostrarDetalles()

    // podemos hacer que los autos realicen acciones
    println("\n____________ ACCIONES DE LOS AUTOS ____________")
    miPrimerAuto.acelerar()
    autoDeMinHermano.frenar()
    autoDelVecino.tocarBocina()

    // tambien podemos guardar autos en una lista
    val listaAutos = listOf(miPrimerAuto, autoDeMinHermano, autoDelVecino)

    println("\n____________ RECORRIENDO LISTA DE AUTOS ____________")
    listaAutos.forEach { auto ->
        println("El ${auto.marca} de color ${auto.color} es del año ${auto.anio}")
    }
}

// __________________________________________
// ///////////// EJEMPLO 2 - CLASE PERSONA /////////////
// __________________________________________
/*
EJERCICIO PRACTICO - CLASE PERSONA
Ahora vamos con el ejercicio principal. Crearemos una clase Persona
que tenga las siguientes caracteristicas:
- nombre
- rut
- fechaNacimiento
- edad (calculada automaticamente)

Y el siguiente metodo:
- mostrarInfo() que muestre toda la info de forma legible

Esto es muy util cuando trabajas con apps reales, siempre necesitas
manejar informacion de usuarios, clientes, empleados, etc

PARA EJECUTAR ESTE EJEMPLO, CREA UN NUEVO ARCHIVO O COMENTA EL MAIN ANTERIOR
*/

class Persona(
    val nombre: String,
    val rut: String,
    val fechaNacimiento: String
) {
    // la edad la calculamos basandonos en el año de nacimiento
    // para simplificar, asumimos que estamos en 2025 y solo usamos el año
    val edad: Int
        get() {
            // extraemos el año de la fecha (asumiendo formato DD/MM/YYYY)
            val partesFecha = fechaNacimiento.split("/")
            val anioNacimiento = partesFecha[2].toInt()
            val anioActual = 2025
            return anioActual - anioNacimiento
        }

    // metodo para mostrar toda la informacion de forma legible
    fun mostrarInfo() {
        println("╔════════════════════════════════════════")
        println("║ INFORMACION PERSONAL")
        println("╠════════════════════════════════════════")
        println("║ Nombre    : $nombre")
        println("║ RUT       : $rut")
        println("║ F.Nac     : $fechaNacimiento")
        println("║ Edad      : $edad años")
        println("╚════════════════════════════════════════")
    }

    // metodo adicional para verificar si es mayor de edad
    fun esMayorDeEdad(): Boolean {
        return edad >= 18
    }

    // metodo para obtener las iniciales (util para avatares, por ejemplo)
    fun obtenerIniciales(): String {
        val partes = nombre.split(" ")
        var iniciales = ""
        partes.forEach { parte ->
            if (parte.isNotEmpty()) {
                iniciales += parte[0].uppercase()
            }
        }
        return iniciales
    }
}

// Comenta el main anterior y descomenta este para probar
/*
fun main() {
    println("____________ SISTEMA DE PERSONAS ____________")

    // creamos varias personas para probar nuestra clase
    val persona1 = Persona(
        nombre = "Juan Perez Gonzalez",
        rut = "12.345.678-9",
        fechaNacimiento = "15/03/1990"
    )

    val persona2 = Persona(
        nombre = "Maria Rodriguez Silva",
        rut = "18.765.432-1",
        fechaNacimiento = "22/07/1985"
    )

    val persona3 = Persona(
        nombre = "Pedro Martinez Lopez",
        rut = "20.123.456-7",
        fechaNacimiento = "08/11/2000"
    )

    // mostramos la informacion de cada persona
    println("\n--- Informacion de Personas Registradas ---")
    persona1.mostrarInfo()
    println() // linea en blanco para separar
    persona2.mostrarInfo()
    println()
    persona3.mostrarInfo()

    // verificamos si son mayores de edad
    println("\n--- Verificacion de Mayoria de Edad ---")
    println("${persona1.nombre} es mayor de edad? ${persona1.esMayorDeEdad()}")
    println("${persona2.nombre} es mayor de edad? ${persona2.esMayorDeEdad()}")
    println("${persona3.nombre} es mayor de edad? ${persona3.esMayorDeEdad()}")

    // mostramos las iniciales
    println("\n--- Iniciales para Avatares ---")
    println("${persona1.nombre} -> ${persona1.obtenerIniciales()}")
    println("${persona2.nombre} -> ${persona2.obtenerIniciales()}")
    println("${persona3.nombre} -> ${persona3.obtenerIniciales()}")

    // BONUS - LISTA DE PERSONAS
    println("\n____________ LISTA DE PERSONAS ____________")

    // creamos una lista mutable de personas (se puede modificar)
    val listaPersonas = mutableListOf<Persona>()

    // agregamos las personas a la lista
    listaPersonas.add(persona1)
    listaPersonas.add(persona2)
    listaPersonas.add(persona3)

    // tambien podemos crear y agregar directamente
    listaPersonas.add(
        Persona("Carlos Diaz Fuentes", "15.888.999-K", "30/12/1995")
    )

    // recorremos la lista con un forEach (mas elegante que un for tradicional)
    println("Total de personas registradas: ${listaPersonas.size}")
    println("\n--- Reporte Completo del Sistema ---")
    listaPersonas.forEachIndexed { index, persona ->
        println("========== Persona #${index + 1} ==========")
        persona.mostrarInfo()
        println()
    }

    // BUSQUEDA EN LA LISTA
    println("____________ BUSQUEDA DE PERSONAS ____________")
    print("Ingrese el RUT a buscar (con puntos y guion): ")
    val rutBuscar = readLine() ?: ""

    // buscamos la persona con ese RUT
    val personaEncontrada = listaPersonas.find { it.rut == rutBuscar }

    // verificamos si encontramos a alguien
    if (personaEncontrada != null) {
        println("\n✓ Persona encontrada:")
        personaEncontrada.mostrarInfo()
    } else {
        println("\n✗ No se encontro ninguna persona con el RUT: $rutBuscar")
        println("Tip: Los RUTs registrados son:")
        listaPersonas.forEach { println(" - ${it.rut}") }
    }
}
*/

// __________________________________________
// ///////////// NOTAS Y DIFERENCIAS /////////////
// __________________________________________

/*
NOTAS IMPORTANTES:
- En Kotlin, el constructor primario va directo en la definicion de la clase
- Si usas 'val' en los parametros del constructor, automaticamente se vuelven propiedades
- El 'get()' es un getter personalizado, permite calcular valores dinamicamente
- forEachIndexed es mas moderno que un for tradicional y te da el indice y el elemento
- El operador '?:' (Elvis) nos ayuda a manejar nulls de forma segura

DIFERENCIAS CON JAVA:
- En Java necesitarias escribir getters y setters para todo
- En Java el constructor seria un metodo separado con el mismo nombre de la clase
- En Kotlin todo es mas conciso y menos verboso

EJERCICIO PARA PRACTICAR:
1. Intenta agregar estos metodos a la clase Persona:
   - Un metodo que valide si el RUT es valido (con el digito verificador)
   - Un metodo que calcule cuantos dias faltan para el cumpleaños
   - Una propiedad que indique el signo zodiacal segun la fecha de nacimiento

2. Crea una nueva clase llamada Empleado con:
   - nombre, cargo, sueldo
   - Un metodo para calcular el bono (10% del sueldo)
   - Un metodo para mostrar ficha del empleado

CHALLENGE EXTRA:
Crea un sistema de biblioteca con:
- Clase Libro (titulo, autor, isbn, disponible)
- Clase Usuario (nombre, rut, librosPrestados)
- Metodos para prestar y devolver libros
*/