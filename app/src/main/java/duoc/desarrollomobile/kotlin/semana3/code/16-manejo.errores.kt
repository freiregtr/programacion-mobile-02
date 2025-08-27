package duoc.desarrollomobile.kotlin.semana3.code

// __________________________________________
// MANEJO DE ERRORES - TRY CATCH
/*
En el mundo real, las cosas salen mal. Los usuarios ingresan datos incorrectos,
las conexiones a internet fallan, los archivos no existen, etc.
Por eso necesitamos manejar errores de forma elegante sin que la app explote

El manejo de errores permite que tu programa continue funcionando aunque
algo salga mal. Es como tener un plan B cuando las cosas no salen como esperabas

Conceptos basicos:
-> TRY: intenta ejecutar codigo que puede fallar
-> CATCH: atrapa el error si algo sale mal
-> FINALLY: se ejecuta siempre, haya error o no (opcional)
-> THROW: lanza un error manualmente

En Kotlin tambien existe Result que es mas moderno que try-catch
*/

// __________________________________________
// ///////////// EJEMPLO 1 - CONVERSION DE NUMEROS /////////////
// __________________________________________

/*
El ejemplo mas comun es cuando el usuario ingresa texto y queremos
convertirlo a numero. Que pasa si escribe "abc" en vez de "123"?
Sin manejo de errores, la app crashea. Con try-catch, la controlamos
*/

fun convertirTextoANumero(texto: String) {
    println("\nIntentando convertir: '$texto'")

    try {
        // intentamos convertir el texto a numero
        val numero = texto.toInt()
        println("Exito! El numero es: $numero")
        println("El doble del numero es: ${numero * 2}")
    } catch (e: NumberFormatException) {
        // si falla, atrapamos el error
        println("Error: '$texto' no es un numero valido")
        println("Por favor ingrese solo numeros")
    }
}

// funcion que devuelve un valor o null si hay error
fun convertirSeguro(texto: String): Int? {
    return try {
        texto.toInt()
    } catch (e: Exception) {
        null
    }
}

fun main() {
    println("____________ MANEJO DE ERRORES BASICO ____________")

    // probamos con diferentes valores
    convertirTextoANumero("123")    // funciona bien
    convertirTextoANumero("456")    // funciona bien
    convertirTextoANumero("abc")    // falla pero no crashea
    convertirTextoANumero("12.5")   // falla pero no crashea
    convertirTextoANumero("")       // falla pero no crashea

    println("\n____________ CONVERSION SEGURA ____________")

    val valores = listOf("100", "200", "abc", "300", "xyz")

    valores.forEach { valor ->
        val resultado = convertirSeguro(valor)
        if (resultado != null) {
            println("'$valor' se convirtio a: $resultado")
        } else {
            println("'$valor' no se pudo convertir")
        }
    }

    // __________________________________________
    // TRY-CATCH CON FINALLY
    println("\n____________ TRY-CATCH-FINALLY ____________")

    fun leerArchivo(nombreArchivo: String) {
        println("\nIntentando leer: $nombreArchivo")
        try {
            // simulamos leer un archivo
            if (nombreArchivo == "existe.txt") {
                println("Leyendo contenido del archivo...")
                println("Contenido: Hola mundo desde el archivo")
            } else {
                throw Exception("Archivo no encontrado")
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
        } finally {
            // esto siempre se ejecuta
            println("Cerrando conexion con el archivo")
        }
    }

    leerArchivo("existe.txt")
    leerArchivo("noexiste.txt")

    // __________________________________________
    // MULTIPLES CATCH
    println("\n____________ MULTIPLES CATCH ____________")

    fun procesarDato(dato: String) {
        try {
            when {
                dato.isEmpty() -> throw IllegalArgumentException("El dato esta vacio")
                dato.length > 10 -> throw IllegalStateException("El dato es muy largo")
                !dato.all { it.isDigit() } -> throw NumberFormatException("No es un numero")
                else -> {
                    val numero = dato.toInt()
                    println("Procesando numero: $numero")
                }
            }
        } catch (e: IllegalArgumentException) {
            println("Error de argumento: ${e.message}")
        } catch (e: IllegalStateException) {
            println("Error de estado: ${e.message}")
        } catch (e: NumberFormatException) {
            println("Error de formato: ${e.message}")
        } catch (e: Exception) {
            // catch general para cualquier otro error
            println("Error desconocido: ${e.message}")
        }
    }

    procesarDato("123")          // ok
    procesarDato("")             // error: vacio
    procesarDato("12345678901")  // error: muy largo
    procesarDato("12abc")        // error: no es numero
}

// __________________________________________
// ///////////// EJEMPLO 2 - RESULT DE KOTLIN /////////////
// __________________________________________

/*
Result es una forma mas moderna de manejar errores en Kotlin
Es mas funcional y permite encadenar operaciones facilmente

Result puede ser:
- Success: la operacion fue exitosa, contiene el valor
- Failure: la operacion fallo, contiene la excepcion

PARA EJECUTAR ESTE EJEMPLO, COMENTA EL MAIN ANTERIOR Y DESCOMENTA ESTE
*/

// funcion que valida un numero y devuelve Result
fun validarNumeroPositivo(numero: Int): Result<Int> {
    return if (numero >= 0) {
        Result.success(numero)
    } else {
        Result.failure(IllegalArgumentException("El numero $numero es negativo"))
    }
}

// funcion para dividir con Result
fun dividirSeguro(dividendo: Int, divisor: Int): Result<Double> {
    return try {
        if (divisor == 0) {
            Result.failure(ArithmeticException("No se puede dividir por cero"))
        } else {
            Result.success(dividendo.toDouble() / divisor)
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}

// funcion para procesar edad
fun procesarEdad(edadTexto: String): Result<String> {
    return try {
        val edad = edadTexto.toInt()
        when {
            edad < 0 -> Result.failure(IllegalArgumentException("La edad no puede ser negativa"))
            edad < 18 -> Result.success("Es menor de edad")
            edad < 65 -> Result.success("Es adulto")
            else -> Result.success("Es adulto mayor")
        }
    } catch (e: NumberFormatException) {
        Result.failure(Exception("'$edadTexto' no es una edad valida"))
    }
}

/*
fun main() {
    println("____________ MANEJO CON RESULT ____________")

    // Ejercicio 1: Validacion de numeros positivos
    println("\n--- Validacion de Numeros ---")
    val numeros = listOf(10, -5, 0, 25, -10)

    numeros.forEach { numero ->
        val resultado = validarNumeroPositivo(numero)

        if (resultado.isSuccess) {
            println("$numero es valido: ${resultado.getOrNull()}")
        } else {
            println("$numero es invalido: ${resultado.exceptionOrNull()?.message}")
        }
    }

    // Ejercicio 2: Division segura
    println("\n--- Division Segura ---")
    val divisiones = listOf(
        Pair(10, 2),
        Pair(15, 0),
        Pair(20, 4),
        Pair(8, 0)
    )

    divisiones.forEach { (dividendo, divisor) ->
        val resultado = dividirSeguro(dividendo, divisor)

        resultado.fold(
            onSuccess = { valor ->
                println("$dividendo / $divisor = $valor")
            },
            onFailure = { error ->
                println("$dividendo / $divisor = Error: ${error.message}")
            }
        )
    }

    // Ejercicio 3: Procesamiento de edades
    println("\n--- Procesamiento de Edades ---")
    val edades = listOf("25", "abc", "17", "70", "-5", "35")

    edades.forEach { edad ->
        procesarEdad(edad)
            .onSuccess { categoria ->
                println("Edad $edad: $categoria")
            }
            .onFailure { error ->
                println("Error con '$edad': ${error.message}")
            }
    }

    // __________________________________________
    // ENCADENANDO RESULTS
    println("\n____________ ENCADENANDO OPERACIONES ____________")

    fun calcularDescuento(precioTexto: String, descuentoTexto: String): Result<Double> {
        return try {
            val precio = precioTexto.toDouble()
            val descuento = descuentoTexto.toDouble()

            when {
                precio < 0 -> Result.failure(Exception("El precio no puede ser negativo"))
                descuento < 0 || descuento > 100 -> {
                    Result.failure(Exception("El descuento debe estar entre 0 y 100"))
                }
                else -> {
                    val precioFinal = precio * (1 - descuento / 100)
                    Result.success(precioFinal)
                }
            }
        } catch (e: NumberFormatException) {
            Result.failure(Exception("Valores invalidos ingresados"))
        }
    }

    val compras = listOf(
        Pair("1000", "10"),   // ok: 1000 con 10% descuento
        Pair("abc", "20"),    // error: precio invalido
        Pair("500", "150"),   // error: descuento invalido
        Pair("2000", "25"),   // ok: 2000 con 25% descuento
        Pair("-100", "10")    // error: precio negativo
    )

    compras.forEach { (precio, descuento) ->
        calcularDescuento(precio, descuento)
            .onSuccess { final ->
                println("Precio: $$precio - Descuento: $descuento% = $$final")
            }
            .onFailure { error ->
                println("Error en calculo: ${error.message}")
            }
    }
}
*/

// __________________________________________
// ///////////// EJEMPLO 3 - MANEJO REAL EN APPS /////////////
// __________________________________________

/*
En apps reales, el manejo de errores es critico
Veamos ejemplos practicos que usarias en una app de verdad
*/

// Sistema de login con manejo de errores
class SistemaLogin {
    private val usuariosValidos = mapOf(
        "admin" to "1234",
        "usuario" to "pass123",
        "demo" to "demo"
    )

    fun login(usuario: String, password: String): Result<String> {
        return try {
            // validaciones basicas
            if (usuario.isEmpty() || password.isEmpty()) {
                return Result.failure(Exception("Usuario y password son requeridos"))
            }

            if (usuario.length < 3) {
                return Result.failure(Exception("Usuario debe tener al menos 3 caracteres"))
            }

            if (password.length < 4) {
                return Result.failure(Exception("Password debe tener al menos 4 caracteres"))
            }

            // verificar credenciales
            val passwordGuardado = usuariosValidos[usuario]
            if (passwordGuardado == null) {
                Result.failure(Exception("Usuario no existe"))
            } else if (passwordGuardado != password) {
                Result.failure(Exception("Password incorrecto"))
            } else {
                Result.success("Bienvenido $usuario!")
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error en el sistema: ${e.message}"))
        }
    }
}

/*
fun main() {
    println("____________ SISTEMA DE LOGIN ____________")

    val sistema = SistemaLogin()

    // casos de prueba
    val intentosLogin = listOf(
        Pair("admin", "1234"),      // ok
        Pair("admin", "wrong"),     // password incorrecto
        Pair("noexiste", "1234"),   // usuario no existe
        Pair("", "1234"),          // usuario vacio
        Pair("ab", "1234"),        // usuario muy corto
        Pair("admin", "123")       // password muy corto
    )

    intentosLogin.forEach { (usuario, password) ->
        println("\nIntentando login: usuario='$usuario', password='$password'")

        sistema.login(usuario, password)
            .onSuccess { mensaje ->
                println("✓ Login exitoso: $mensaje")
            }
            .onFailure { error ->
                println("✗ Login fallido: ${error.message}")
            }
    }
}
*/

// __________________________________________
// ///////////// NOTAS Y CONCEPTOS /////////////
// __________________________________________

/*
CUANDO USAR TRY-CATCH:
- Conversion de tipos (String a Int, etc)
- Lectura de archivos
- Conexiones de red
- Operaciones que pueden fallar

CUANDO USAR RESULT:
- Cuando quieres un estilo mas funcional
- Para encadenar operaciones
- Cuando necesitas devolver exito o fallo
- En arquitecturas modernas de Android

DIFERENCIAS CON JAVA:
- Java: try-catch es obligatorio para checked exceptions
- Kotlin: no hay checked exceptions, try-catch es opcional
- Java: no tiene Result nativo (hasta Java 8 con Optional)
- Kotlin: Result es parte del lenguaje

BUENAS PRACTICAS:
1. Siempre maneja los errores esperados
2. No uses try-catch para control de flujo normal
3. Se especifico con los tipos de excepcion
4. Loguea los errores para debugging
5. Da mensajes claros al usuario

ERRORES COMUNES EN KOTLIN:
- NullPointerException: acceder a un null
- NumberFormatException: convertir texto invalido a numero
- IndexOutOfBoundsException: acceder a indice que no existe
- IllegalArgumentException: argumentos invalidos
- IllegalStateException: estado invalido del objeto

EJERCICIOS PARA PRACTICAR:

1. Calculadora Segura:
   - Crea funciones para sumar, restar, multiplicar, dividir
   - Usa Result para manejar division por cero
   - Maneja entrada de texto invalido con try-catch

2. Validador de Email:
   - Funcion que valide formato de email
   - Debe tener @ y .
   - Usa Result para devolver exito o error especifico

3. Lector de Configuracion:
   - Lee valores de un Map simulando un archivo config
   - Maneja keys que no existen
   - Convierte valores a diferentes tipos con manejo de errores

CHALLENGE EXTRA:
Sistema de Transferencias Bancarias con:
- Validacion de montos (no negativos, limite maximo)
- Verificacion de saldo suficiente
- Manejo de cuentas inexistentes
- Log de intentos fallidos
- Uso de Result para cada operacion
*/