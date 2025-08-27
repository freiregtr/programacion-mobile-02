package duoc.desarrollomobile.kotlin.semana3.code

///////////// HERENCIA EN KOTLIN /////////////
/*
//La herencia es uno de los pilares fundamentales de la POO (Programacion Orientada a Objetos)
Permite que una clase "hija" herede propiedades y metodos de una clase "padre"

Piensalo asi: todos los animales tienen caracteristicas comunes como nombre, edad, comer()
Pero un Perro ademas puede ladrar() y un Gato puede maullar()
En lugar de repetir codigo, hacemos que Perro y Gato hereden de Animal

En terminos tecnicos:
-> CLASE PADRE (o Base/Super): la clase de la que se hereda
-> CLASE HIJA (o Derivada): la clase que hereda
-> HERENCIA: el proceso de extender una clase padre
-> OVERRIDE: cuando modificamos un metodo heredado

SUPER IMPORTANTE en Kotlin:
- Por defecto las clases son FINAL (no se pueden heredar)
- Debes marcarlas como 'open' para permitir herencia
- En Java todas las clases son heredables por defecto
*/

// __________________________________________
// ///////////// EJEMPLO 1 - ANIMALES /////////////
// __________________________________________

/*
Vamos a hacer un ejemplo simple con animales para entender
el concepto antes de ir al ejercicio principal
*/

// La clase padre debe ser 'open' para permitir herencia
open class Animal(
    val nombre: String,
    val edad: Int
) {
    // metodo basico que todos los animales tienen
    open fun hacerSonido() {
        println("$nombre hace un sonido generico")
    }

    fun comer() {
        println("$nombre esta comiendo")
    }

    fun dormir() {
        println("$nombre esta durmiendo... zzz")
    }
}

// Clase Perro hereda de Animal usando : Animal
class Perro(
    nombre: String,
    edad: Int,
    val raza: String  // propiedad adicional solo de Perro
) : Animal(nombre, edad) {  // llamamos al constructor padre

    // sobrescribimos el metodo hacerSonido
    override fun hacerSonido() {
        println("$nombre ladra: Guau guau!")
    }

    // metodo exclusivo de Perro
    fun moverCola() {
        println("$nombre mueve la cola felizmente")
    }
}

// Clase Gato hereda de Animal
class Gato(
    nombre: String,
    edad: Int,
    val esCallejero: Boolean
) : Animal(nombre, edad) {

    override fun hacerSonido() {
        println("$nombre maulla, el gato dice miau")
    }

    // metodo exclusivo de Gato
    fun ronronear() {
        println("$nombre el gato ronrronea")
    }
}

fun main() {
    println("____________ EJEMPLO DE HERENCIA CON ANIMALES ____________")

    // creamos un animal generico
    val animalGenerico = Animal("Bicho", 5)
    animalGenerico.hacerSonido()
    animalGenerico.comer()

    println("\n--- Perro ---")
    val miPerro = Perro("Firulais", 3, "Labrador")
    miPerro.hacerSonido()  // metodo sobrescrito
    miPerro.comer()        // metodo heredado
    miPerro.moverCola()    // metodo exclusivo de Perro
    println("La raza de ${miPerro.nombre} es ${miPerro.raza}")

    println("\n--- Gato ---")
    val miGato = Gato("Michi", 2, false)
    miGato.hacerSonido()   // metodo sobrescrito
    miGato.dormir()        // metodo heredado
    miGato.ronronear()     // metodo exclusivo de Gato

    // polimorfismo: podemos tratar a perros y gatos como animales
    println("\n--- Polimorfismo ---")
    val listaAnimales: List<Animal> = listOf(animalGenerico, miPerro, miGato)

    println("Todos los animales hacen sonidos:")
    listaAnimales.forEach { animal ->
        animal.hacerSonido()  // cada uno hace su sonido especifico
    }
}

// __________________________________________
// ///////////// EJEMPLO 2 - PERSONA Y EMPLEADO /////////////
// __________________________________________

/*
EJERCICIO PRINCIPAL - HERENCIA CON PERSONA Y EMPLEADO

Ahora vamos al ejercicio real. Vamos a crear:
1. Una clase Persona (clase padre)
2. Una clase Empleado que herede de Persona (clase hija)

El Empleado tendra todo lo de Persona mas cosas adicionales como
cargo, sueldo y un metodo para calcular bonificacion

PARA EJECUTAR ESTE EJEMPLO, COMENTA EL MAIN ANTERIOR Y DESCOMENTA ESTE
*/

// Clase padre - debe ser 'open' para permitir herencia
open class Persona(
    val nombre: String,
    val rut: String,
    val fechaNacimiento: String
) {
    // calculamos la edad basandonos en el anio
    val edad: Int
        get() {
            val partesFecha = fechaNacimiento.split("/")
            val anioNacimiento = partesFecha[2].toInt()
            return 2025 - anioNacimiento
        }

    // metodo que puede ser sobrescrito
    open fun mostrarInfo() {
        println("===== INFORMACION PERSONAL =====")
        println("Nombre: $nombre")
        println("RUT: $rut")
        println("Fecha Nacimiento: $fechaNacimiento")
        println("Edad: $edad anios")
    }

    fun esMayorDeEdad(): Boolean {
        return edad >= 18
    }
}

// Clase hija - Empleado hereda de Persona
class Empleado(
    nombre: String,        // parametros para el constructor padre
    rut: String,
    fechaNacimiento: String,
    val cargo: String,     // propiedades adicionales de Empleado
    val sueldo: Double,
    val fechaContratacion: String
) : Persona(nombre, rut, fechaNacimiento) {  // llamamos al constructor de Persona

    // propiedad calculada para anios de antiguedad
    val aniosAntiguedad: Int
        get() {
            val partes = fechaContratacion.split("/")
            val anioContrato = partes[2].toInt()
            return 2025 - anioContrato
        }

    // sobrescribimos mostrarInfo para agregar info del empleado
    override fun mostrarInfo() {
        super.mostrarInfo()  // llamamos al metodo padre primero
        println("===== INFORMACION LABORAL =====")
        println("Cargo: $cargo")
        println("Sueldo: $$sueldo")
        println("Fecha Contratacion: $fechaContratacion")
        println("Antiguedad: $aniosAntiguedad anios")
    }

    // metodo especifico de Empleado para calcular bonificacion
    fun calcularBonificacion(): Double {
        // bonificacion base del 10%
        var bonificacion = sueldo * 0.10

        // bonus adicional por antiguedad
        // por cada anio de antiguedad, 2% adicional hasta maximo 20%
        val bonusAntiguedad = (aniosAntiguedad * 0.02).coerceAtMost(0.20)
        bonificacion += sueldo * bonusAntiguedad

        return bonificacion
    }

    // otro metodo exclusivo de Empleado
    fun mostrarLiquidacion() {
        val bonificacion = calcularBonificacion()
        val totalPago = sueldo + bonificacion

        println("\n===== LIQUIDACION DE SUELDO =====")
        println("Empleado: $nombre")
        println("Cargo: $cargo")
        println("--------------------------------")
        println("Sueldo Base:      $ ${String.format("%.2f", sueldo)}")
        println("Bonificacion:     $ ${String.format("%.2f", bonificacion)}")
        println("--------------------------------")
        println("TOTAL A PAGAR:    $ ${String.format("%.2f", totalPago)}")
        println("=================================")
    }
}

/*
fun main() {
    println("____________ SISTEMA DE PERSONAS Y EMPLEADOS ____________")

    // creamos una persona normal
    val persona1 = Persona(
        nombre = "Juan Carlos Perez",
        rut = "15.234.567-8",
        fechaNacimiento = "10/05/1992"
    )

    println("--- Persona Normal ---")
    persona1.mostrarInfo()

    // creamos varios empleados
    val empleado1 = Empleado(
        nombre = "Maria Gonzalez Silva",
        rut = "18.765.432-1",
        fechaNacimiento = "22/07/1985",
        cargo = "Desarrolladora Senior",
        sueldo = 2500000.0,
        fechaContratacion = "15/03/2018"
    )

    val empleado2 = Empleado(
        nombre = "Pedro Martinez Lopez",
        rut = "20.123.456-7",
        fechaNacimiento = "08/11/2000",
        cargo = "Analista Junior",
        sueldo = 1200000.0,
        fechaContratacion = "01/06/2023"
    )

    val empleado3 = Empleado(
        nombre = "Ana Rodriguez Castro",
        rut = "17.890.123-4",
        fechaNacimiento = "30/01/1990",
        cargo = "Jefa de Proyecto",
        sueldo = 3200000.0,
        fechaContratacion = "10/09/2015"
    )

    // mostramos info de cada empleado
    println("\n--- Empleado 1 ---")
    empleado1.mostrarInfo()

    println("\n--- Empleado 2 ---")
    empleado2.mostrarInfo()

    println("\n--- Empleado 3 ---")
    empleado3.mostrarInfo()

    // calculamos bonificaciones
    println("\n____________ CALCULO DE BONIFICACIONES ____________")

    println("\n${empleado1.nombre} (${empleado1.cargo}):")
    println("Antiguedad: ${empleado1.aniosAntiguedad} anios")
    println("Bonificacion: $${empleado1.calcularBonificacion()}")

    println("\n${empleado2.nombre} (${empleado2.cargo}):")
    println("Antiguedad: ${empleado2.aniosAntiguedad} anios")
    println("Bonificacion: $${empleado2.calcularBonificacion()}")

    println("\n${empleado3.nombre} (${empleado3.cargo}):")
    println("Antiguedad: ${empleado3.aniosAntiguedad} anios")
    println("Bonificacion: $${empleado3.calcularBonificacion()}")

    // mostramos liquidaciones completas
    println("\n____________ LIQUIDACIONES ____________")
    empleado1.mostrarLiquidacion()
    empleado2.mostrarLiquidacion()
    empleado3.mostrarLiquidacion()

    // ejemplo de polimorfismo
    println("\n____________ POLIMORFISMO ____________")

    // podemos tratar a los empleados como personas
    val listaPersonas: List<Persona> = listOf(persona1, empleado1, empleado2, empleado3)

    println("Verificando mayoria de edad de todas las personas:")
    listaPersonas.forEach { persona ->
        val tipo = if (persona is Empleado) "Empleado" else "Persona"
        println("${persona.nombre} ($tipo) es mayor de edad? ${persona.esMayorDeEdad()}")
    }

    // usando 'is' para verificar tipos
    println("\n--- Filtrando solo empleados ---")
    listaPersonas.forEach { persona ->
        if (persona is Empleado) {
            println("${persona.nombre} trabaja como ${persona.cargo}")
        }
    }
}
*/

// __________________________________________
// ///////////// NOTAS Y CONCEPTOS /////////////
// __________________________________________

/*
CONCEPTOS CLAVE DE HERENCIA:

1. OPEN vs FINAL
   - En Kotlin las clases son 'final' por defecto (no heredables)
   - Debes marcarlas como 'open' para permitir herencia
   - Los metodos tambien deben ser 'open' para poder sobrescribirse

2. SUPER
   - 'super' se usa para llamar al constructor o metodos de la clase padre
   - super.mostrarInfo() llama al metodo del padre

3. OVERRIDE
   - Se usa para sobrescribir un metodo del padre
   - El metodo padre debe ser 'open'

4. POLIMORFISMO
   - Puedes tratar objetos hijos como si fueran del tipo padre
   - Util para listas mixtas y procesamiento generico

DIFERENCIAS CON JAVA:
- En Java todas las clases son heredables por defecto
- En Java usas 'extends' para herencia: class Empleado extends Persona
- En Kotlin usas ':' para herencia: class Empleado : Persona
- En Java el constructor se llama con super() dentro del constructor
- En Kotlin lo llamas directo en la declaracion de la clase

CUANDO USAR HERENCIA:
- Cuando hay una relacion "ES UN" (Empleado ES UNA Persona)
- Para reutilizar codigo y evitar repeticion
- Cuando necesitas jerarquias de clases

CUANDO NO USAR HERENCIA:
- Si la relacion es "TIENE UN" (mejor usar composicion)
- Si solo necesitas reutilizar un par de metodos (usa interfaces)
- Si la jerarquia se vuelve muy compleja (considera otro patron)

EJERCICIOS PARA PRACTICAR:

1. Crea una jerarquia de Vehiculos:
   - Clase base: Vehiculo (marca, modelo, anio)
   - Clases hijas: Auto (cantidadPuertas), Moto (cilindrada), Camion (capacidadCarga)
   - Cada uno con su propio metodo acelerar() y consumoCombustible()

2. Sistema de Escuela:
   - Clase base: PersonaEscuela (nombre, rut, email)
   - Estudiante: hereda y agrega (curso, promedio)
   - Profesor: hereda y agrega (asignatura, horasSemanales)
   - Director: hereda y agrega (aniosExperiencia)

3. Jerarquia de Productos:
   - Clase base: Producto (codigo, nombre, precio)
   - ProductoElectronico: agrega (garantiaMeses, voltaje)
   - ProductoAlimenticio: agrega (fechaVencimiento, calorias)
   - Metodo calcularDescuento() diferente para cada tipo

CHALLENGE AVANZADO:
Implementa una jerarquia de Cuentas Bancarias:
- CuentaBancaria base (numero, titular, saldo)
- CuentaAhorro (tasaInteres, calcularIntereses())
- CuentaCorriente (limiteCredito, comisionMantencion)
- CuentaVista (maximoRetiroDiario)
Con metodos para depositar, retirar y transferir entre cuentas
*/