package duoc.desarrollomobile.kotlin.semana3.code

// __________________________________________
// POLIMORFISMO E INTERFACES
/*
El polimorfismo es otro pilar fundamental de la POO. Significa que un mismo
metodo puede comportarse de forma diferente segun el objeto que lo ejecute

Piensalo asi: si le dices "habla" a diferentes personas, cada una hablara
en su idioma. El metodo es el mismo (hablar) pero el comportamiento es diferente

Las INTERFACES son como contratos que las clases deben cumplir. Si una clase
implementa una interfaz, DEBE tener todos los metodos que la interfaz define

En terminos simples:
-> POLIMORFISMO: muchas formas, mismo metodo diferente comportamiento
-> INTERFAZ: un contrato que dice que metodos debes tener
-> IMPLEMENTAR: cuando una clase cumple con una interfaz

DIFERENCIA ENTRE HERENCIA E INTERFACES:
- Herencia: una clase ES ALGO (Empleado ES una Persona)
- Interfaz: una clase PUEDE HACER ALGO (Persona PUEDE informar)
- En Kotlin puedes heredar de UNA clase pero implementar MUCHAS interfaces
*/

// __________________________________________
// ///////////// EJEMPLO 1 - FORMAS GEOMETRICAS /////////////
// __________________________________________

/*
Primero un ejemplo simple para entender el concepto
Todas las formas geometricas pueden calcular su area, pero cada una
lo hace de forma diferente
*/

// Interfaz que define que toda forma debe poder calcular area
interface Forma {
    fun calcularArea(): Double
    fun describir()
}

/*
RECORDATORIO DE FORMULAS MATEMATICAS:
- Area del CIRCULO = π × radio²  (pi por radio al cuadrado)
- Area del CUADRADO = lado × lado  (o lado²)
- Area del RECTANGULO = base × altura
- Area del TRIANGULO = (base × altura) / 2

Usaremos 3.14159 como valor de PI para simplificar
*/

// Clase Circulo implementa Forma
class Circulo(val radio: Double) : Forma {
    override fun calcularArea(): Double {
        // area del circulo es pi * radio al cuadrado
        // ejemplo: radio 5 -> 3.14159 * 5 * 5 = 78.54
        return 3.14159 * radio * radio
    }

    override fun describir() {
        println("Soy un circulo con radio $radio")
    }
}

// Clase Cuadrado implementa Forma
class Cuadrado(val lado: Double) : Forma {
    override fun calcularArea(): Double {
        // area del cuadrado es lado por lado
        // ejemplo: lado 4 -> 4 * 4 = 16
        return lado * lado
    }

    override fun describir() {
        println("Soy un cuadrado con lado $lado")
    }
}

// Clase Rectangulo implementa Forma
class Rectangulo(val base: Double, val altura: Double) : Forma {
    override fun calcularArea(): Double {
        // area del rectangulo es base por altura
        // ejemplo: base 3, altura 6 -> 3 * 6 = 18
        return base * altura
    }

    override fun describir() {
        println("Soy un rectangulo de base $base y altura $altura")
    }
}

fun main() {
    println("____________ EJEMPLO DE POLIMORFISMO CON FORMAS ____________")

    // creamos diferentes formas
    val circulo = Circulo(5.0)
    val cuadrado = Cuadrado(4.0)
    val rectangulo = Rectangulo(3.0, 6.0)

    // cada forma describe quien es
    circulo.describir()
    cuadrado.describir()
    rectangulo.describir()

    println("\n--- Calculando areas ---")
    println("Area del circulo: ${circulo.calcularArea()}")
    println("Area del cuadrado: ${cuadrado.calcularArea()}")
    println("Area del rectangulo: ${rectangulo.calcularArea()}")

    // polimorfismo: todas son Formas pero cada una calcula diferente
    println("\n--- Polimorfismo en accion ---")
    val listaFormas: List<Forma> = listOf(circulo, cuadrado, rectangulo)

    listaFormas.forEach { forma ->
        forma.describir()
        println("Mi area es: ${forma.calcularArea()}")
        println()
    }

    // calculamos el area total de todas las formas
    val areaTotal = listaFormas.sumOf { it.calcularArea() }
    println("El area total de todas las formas es: $areaTotal")
}

// __________________________________________
// ///////////// EJEMPLO 2 - PERSONA Y EMPLEADO CON INTERFAZ /////////////
// __________________________________________

/*
EJERCICIO PRINCIPAL - INTERFAZ INFORMABLE

Ahora vamos al ejercicio real. Crearemos:
1. Una interfaz Informable con metodo informar()
2. Persona implementa Informable
3. Empleado (que hereda de Persona) tambien implementa Informable
4. Cada uno informa de forma diferente

PARA EJECUTAR ESTE EJEMPLO, COMENTA EL MAIN ANTERIOR Y DESCOMENTA ESTE
*/

// Interfaz que pueden implementar diferentes clases
interface Informable {
    fun informar()
    fun obtenerTipo(): String
}

// Clase Persona implementa Informable
open class Persona(
    val nombre: String,
    val rut: String,
    val fechaNacimiento: String
) : Informable {

    val edad: Int
        get() {
            val partes = fechaNacimiento.split("/")
            val anioNacimiento = partes[2].toInt()
            return 2025 - anioNacimiento
        }

    // implementamos el metodo de la interfaz
    override fun informar() {
        println("--- Informe de Persona ---")
        println("Hola, soy $nombre")
        println("Mi RUT es $rut")
        println("Naci el $fechaNacimiento")
        println("Tengo $edad anios")
    }

    override fun obtenerTipo(): String {
        return "Persona Regular"
    }

    open fun mostrarInfo() {
        println("Nombre: $nombre, RUT: $rut")
    }
}

// Clase Empleado hereda de Persona e implementa Informable de forma diferente
class Empleado(
    nombre: String,
    rut: String,
    fechaNacimiento: String,
    val cargo: String,
    val sueldo: Double,
    val departamento: String
) : Persona(nombre, rut, fechaNacimiento) {

    // sobreescribimos el metodo informar con comportamiento de empleado
    override fun informar() {
        println("--- Informe de Empleado ---")
        println("Empleado: $nombre")
        println("Cargo: $cargo")
        println("Departamento: $departamento")
        println("Sueldo actual: $$sueldo")
        println("Edad: $edad anios")
    }

    override fun obtenerTipo(): String {
        return "Empleado de $departamento"
    }

    fun trabajar() {
        println("$nombre esta trabajando en $departamento")
    }
}

// Otra clase que tambien implementa Informable
class Cliente(
    val nombreCliente: String,
    val numeroCliente: Int,
    val tipoMembresia: String
) : Informable {

    override fun informar() {
        println("--- Informe de Cliente ---")
        println("Cliente numero: $numeroCliente")
        println("Nombre: $nombreCliente")
        println("Tipo de membresia: $tipoMembresia")
    }

    override fun obtenerTipo(): String {
        return "Cliente $tipoMembresia"
    }

    fun comprar() {
        println("$nombreCliente esta comprando")
    }
}

/*
fun main() {
    println("____________ SISTEMA DE INFORMES POLIMORFICOS ____________")

    // creamos diferentes tipos de objetos
    val persona1 = Persona(
        nombre = "Juan Perez",
        rut = "12.345.678-9",
        fechaNacimiento = "15/05/1990"
    )

    val empleado1 = Empleado(
        nombre = "Maria Silva",
        rut = "15.678.901-2",
        fechaNacimiento = "22/08/1985",
        cargo = "Desarrolladora",
        sueldo = 1800000.0,
        departamento = "Tecnologia"
    )

    val empleado2 = Empleado(
        nombre = "Pedro Gonzalez",
        rut = "18.234.567-8",
        fechaNacimiento = "10/01/1992",
        cargo = "Contador",
        sueldo = 1500000.0,
        departamento = "Finanzas"
    )

    val cliente1 = Cliente(
        nombreCliente = "Ana Martinez",
        numeroCliente = 1001,
        tipoMembresia = "Premium"
    )

    val cliente2 = Cliente(
        nombreCliente = "Carlos Rojas",
        numeroCliente = 1002,
        tipoMembresia = "Basico"
    )

    // cada uno informa de forma individual
    println("--- Informes Individuales ---")
    persona1.informar()
    println()
    empleado1.informar()
    println()
    cliente1.informar()
    println()

    // __________________________________________
    // POLIMORFISMO EN ACCION
    println("\n____________ POLIMORFISMO - TODOS SON INFORMABLES ____________")

    // todos pueden ir en la misma lista porque todos son Informables
    val listaInformables: List<Informable> = listOf(
        persona1,
        empleado1,
        empleado2,
        cliente1,
        cliente2
    )

    // recorremos la lista y cada uno informa segun su tipo
    println("--- Generando todos los informes ---")
    listaInformables.forEach { informable ->
        println("\nTipo: ${informable.obtenerTipo()}")
        informable.informar()
        println("------------------------")
    }

    // __________________________________________
    // FILTRANDO POR TIPO
    println("\n____________ FILTRANDO POR TIPO ____________")

    // solo informes de empleados
    println("--- Solo Empleados ---")
    listaInformables.filterIsInstance<Empleado>().forEach { empleado ->
        empleado.informar()
        empleado.trabajar()  // podemos usar metodos especificos
        println()
    }

    // solo informes de clientes
    println("--- Solo Clientes ---")
    listaInformables.filterIsInstance<Cliente>().forEach { cliente ->
        cliente.informar()
        cliente.comprar()  // metodo especifico de Cliente
        println()
    }

    // __________________________________________
    // CONTANDO POR TIPO
    println("____________ ESTADISTICAS ____________")

    val totalPersonas = listaInformables.count { it is Persona }
    val totalEmpleados = listaInformables.count { it is Empleado }
    val totalClientes = listaInformables.count { it is Cliente }

    println("Total de Personas (incluye empleados): $totalPersonas")
    println("Total de Empleados: $totalEmpleados")
    println("Total de Clientes: $totalClientes")
    println("Total de Informables: ${listaInformables.size}")

    // usando when para procesar segun tipo
    println("\n--- Procesamiento especial segun tipo ---")
    listaInformables.forEach { informable ->
        when (informable) {
            is Empleado -> println("${informable.nombre} trabaja en ${informable.departamento}")
            is Cliente -> println("${informable.nombreCliente} es cliente ${informable.tipoMembresia}")
            is Persona -> println("${informable.nombre} es una persona normal")
        }
    }
}
*/

// __________________________________________
// ///////////// NOTAS Y CONCEPTOS /////////////
// __________________________________________

/*
CONCEPTOS IMPORTANTES:

1. INTERFAZ vs CLASE ABSTRACTA
   - Interfaz: solo define QUE hacer, no COMO
   - Clase abstracta: puede tener implementacion parcial
   - Una clase puede implementar multiples interfaces

2. POLIMORFISMO
   - El mismo metodo se comporta diferente segun el objeto
   - Permite codigo mas flexible y reutilizable
   - Facilita agregar nuevos tipos sin modificar codigo existente

3. CUANDO USAR INTERFACES
   - Cuando diferentes clases necesitan el mismo comportamiento
   - Cuando quieres definir un contrato sin implementacion
   - Para lograr bajo acoplamiento entre clases

DIFERENCIAS CON JAVA:
- En Java las interfaces usan 'implements': class Persona implements Informable
- En Kotlin usas ':' igual que herencia: class Persona : Informable
- En Kotlin las interfaces pueden tener implementaciones por defecto
- En Java (antes de Java 8) las interfaces solo tenian metodos abstractos

VENTAJAS DEL POLIMORFISMO:
- Codigo mas limpio y organizado
- Facil de extender (agregar nuevos tipos)
- Permite programar contra abstracciones, no implementaciones
- Reduce duplicacion de codigo

EJERCICIOS PARA PRACTICAR:

1. Sistema de Pagos:
   - Interface: Pagable con metodo procesarPago()
   - Implementar en: TarjetaCredito, Efectivo, Transferencia
   - Cada uno procesa el pago de forma diferente

2. Sistema de Notificaciones:
   - Interface: Notificable con metodo enviarNotificacion()
   - Implementar en: Email, SMS, PushNotification
   - Procesar lista de notificaciones polimorficamente

3. Sistema de Transporte:
   - Interface: Transportable con metodos mover() y calcularTiempo()
   - Implementar en: Avion, Auto, Bicicleta, Caminando
   - Calcular tiempos para la misma distancia con diferentes medios

CHALLENGE EXTRA:
Crea un sistema de RPG con:
- Interface Combatiente con metodos atacar() y recibirDanio()
- Clases: Guerrero, Mago, Arquero (cada uno ataca diferente)
- Interface Hechizable con metodo lanzarHechizo()
- Solo el Mago implementa Hechizable
- Simula una batalla usando polimorfismo
*/