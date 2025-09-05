# README PARTE 4 - Main.kt y Ejecucion

## paso 2.4: crear Main.kt 

**crear archivo:** click derecho -> new -> kotlin class/file -> selecciona "file" -> nombre: "Main"

### 2.4.1 por que usamos runBlocking?

runBlocking crea un puente entre el codigo normal y el codigo asincrono. necesitamos esto porque procesarOrden es una funcion suspend (asincrona) y main es una funcion normal. runBlocking bloquea el thread principal hasta que todo el codigo asincrono termine. sin esto, no podriamos usar delay ni funciones suspend.

```kotlin
fun main() = runBlocking {
```

### 2.4.2 por que creamos val gestor = GestorOrdenes()?

estamos creando una instancia (un objeto) de la clase GestorOrdenes. no es una coleccion, es UN objeto que contiene toda la logica del negocio. lo ponemos al principio porque vamos a usar sus metodos durante todo el programa: mostrarMenu, obtenerBebida, procesarOrden, etc.

```kotlin
val gestor = GestorOrdenes()
```

### 2.4.3 por que usamos try-catch aqui?

el usuario puede hacer cosas inesperadas: escribir letras donde van numeros, cancelar el programa, etc. el try-catch evita que el programa explote. si algo sale mal, en vez de cerrar todo, mostramos un mensaje de error y el programa termina de forma controlada.

```kotlin
try {
    // todo el codigo que puede fallar
} catch (e: Exception) {
    // que hacer si algo falla
}
```

### 2.4.4 por que val bebida obtiene el valor de gestor.obtenerBebida?

estamos siguiendo una secuencia logica: primero mostramos el menu, el usuario elige un numero, y ese numero lo usamos para buscar la bebida correspondiente en el menu. gestor.obtenerBebida busca en la lista del menu y nos retorna el objeto bebida completo o null si no existe.

```kotlin
val bebida = gestor.obtenerBebida(opcionBebida)
```

### 2.4.5 por que verificamos si bebida es null?

porque el usuario pudo escribir 99 cuando solo hay 7 bebidas. si bebida es null, significa que no encontramos nada. return@runBlocking sale de la funcion runBlocking (no del main completo). es como decir "termina aqui, no sigas con el resto del codigo".

```kotlin
if (bebida == null) {
    println("Opcion invalida!")
    return@runBlocking  // sale del bloque runBlocking
}
```

### 2.4.6 por que mostramos las opciones de tamano?

el usuario ya eligio la bebida, ahora necesita elegir el tamano. mostramos las opciones con sus precios para que sepa cuanto cuesta cada tamano. es parte de la experiencia de usuario: mostrar claramente las opciones disponibles.

```kotlin
println("\nSeleccione tamano:")
println("1. Tall (12 oz) - Precio base")
println("2. Grande (16 oz) - +$700")
println("3. Venti (20 oz) - +$1,200")
print("Seleccion: ")
```

### 2.4.7 por que leemos el tamano y lo convertimos con when?

readLine lee lo que el usuario escribe en la terminal. toIntOrNull intenta convertirlo a numero (si falla, retorna null). el when convierte el numero (1, 2, 3) en el string que necesitamos ("tall", "grande", "venti"). si el usuario pone algo invalido, usamos tall por defecto.

```kotlin
val opcionTamano = readLine()?.toIntOrNull() ?: 0
val tamano = when(opcionTamano) {  // when = switch de java
    1 -> "tall"
    2 -> "grande"
    3 -> "venti"
    else -> {
        println("Tamano invalido! Usando Tall")
        "tall"
    }
}
```

### 2.4.8 por que preguntamos si quiere agregar otra bebida?

en un sistema real, los clientes piden varias cosas. preguntamos para simular esa funcionalidad, pero por simplicidad del ejercicio, solo procesamos una bebida. es para mostrar que sabemos que existe esa necesidad pero la limitamos por ahora.

```kotlin
print("\n¿Desea agregar otra bebida? (s/n): ")
val respuesta = readLine()?.lowercase() ?: "n"
if (respuesta == "s") {
    println("Por ahora solo procesamos una bebida")
}
```

### 2.4.9 por que preguntamos el tipo de cliente?

necesitamos saber el tipo de cliente para aplicar el descuento correcto. calcularDescuento en GestorOrdenes retorna un Double con el porcentaje de descuento (0.0, 0.10, 0.15, 0.30) segun el tipo. si el usuario no escribe nada o escribe mal, usamos "regular" por defecto.

```kotlin
print("\nTipo de cliente (regular/miembro/gold/partner): ")
val tipoCliente = readLine()?.lowercase() ?: "regular"
```

### 2.4.10 por que procesamos la orden aqui?

este es el momento donde simulamos la preparacion de la bebida. procesarOrden es asincrona, tarda 4 segundos y retorna el estado final. durante esos 4 segundos, el programa esta "esperando" que el barista prepare la bebida.

```kotlin
// procesamos la orden (tarda 4 segundos)
val estadoFinal = gestor.procesarOrden(bebida, tamano)
```

### 2.4.11 por que llamamos a generarResumen?

despues de procesar la orden, mostramos el resumen completo como una boleta: que bebida, que tamano, cuanto cuesta, descuentos aplicados, impuestos y total. es la confirmacion final de la compra con todos los detalles.

```kotlin
// mostramos el resumen
gestor.generarResumen(bebida, tamano, tipoCliente, estadoFinal)
```

### 2.4.12 por que las gracias al final?

es cortesia basica y buena experiencia de usuario. indica que el programa termino exitosamente y agradece al usuario por usar el sistema. es como cuando sales de una tienda real

```kotlin
println("\n Gracias por usar StarBrew!")
```

### 2.4.13 por que catch (e: Exception)?

Exception es la clase padre de todos los errores en kotlin. al atrapar Exception, atrapamos cualquier tipo de error. la variable 'e' contiene informacion del error. e.message tiene el mensaje descriptivo de que salio mal. lo mostramos para que el usuario sepa que paso

```kotlin
catch (e: Exception) {
    println("Error: ${e.message}")  // e.message explica el error
    println("Por favor intente nuevamente")
}
```

### 2.4.14 codigo completo de Main.kt

```kotlin
// Main.kt
package duoc.desarrollomobile.myapplication

import kotlinx.coroutines.*

// runBlocking es necesario para usar funciones suspend
fun main() = runBlocking {
    
    val gestor = GestorOrdenes()
    
    try {
        gestor.mostrarMenu()
        
        print("\nSeleccione bebida (numero): ")
        val opcionBebida = readLine()?.toIntOrNull() ?: 0
        
        val bebida = gestor.obtenerBebida(opcionBebida)
        if (bebida == null) {
            println("Opcion invalida!")
            return@runBlocking
        }
        
        println("\nSeleccione tamano:")
        println("1. Tall (12 oz) - Precio base")
        println("2. Grande (16 oz) - +$700")
        println("3. Venti (20 oz) - +$1,200")
        print("Seleccion: ")
        
        val opcionTamano = readLine()?.toIntOrNull() ?: 0
        val tamano = when(opcionTamano) {  // when = switch de java
            1 -> "tall"
            2 -> "grande"
            3 -> "venti"
            else -> {
                println("Tamano invalido! Usando Tall")
                "tall"
            }
        }
        
        print("\n¿Desea agregar otra bebida? (s/n): ")
        val respuesta = readLine()?.lowercase() ?: "n"
        if (respuesta == "s") {
            println("Por ahora solo procesamos una bebida")
        }
        
        print("\nTipo de cliente (regular/miembro/gold/partner): ")
        val tipoCliente = readLine()?.lowercase() ?: "regular"
        
        // procesamos la orden (tarda 4 segundos)
        val estadoFinal = gestor.procesarOrden(bebida, tamano)
        
        // mostramos el resumen
        gestor.generarResumen(bebida, tamano, tipoCliente, estadoFinal)
        
        println("\n☕ Gracias por usar StarBrew!")
        
    } catch (e: Exception) {
        println("Error: ${e.message}")
        println("Por favor intente nuevamente")
    }
}
```

## como ejecutar el programa en android studio

1. abre el archivo `Main.kt`
2. busca el triangulo verde al lado de `fun main()`
3. dale click al triangulo
4. selecciona "run 'MainKt'"
5. la consola aparece abajo en la pestana "run"

**donde escribir:** cuando el programa te pida algo, escribe en la consola y presiona enter

## problemas comunes y soluciones

### error: "unresolved reference: delay"
**solucion:** agrega `import kotlinx.coroutines.delay` arriba del archivo

### error: "'when' expression must be exhaustive"
**solucion:** agrega `else` a todos los `when`:
```kotlin
when(algo) {
    caso1 -> // codigo
    caso2 -> // codigo
    else -> // codigo por defecto
}
```

### error: comillas raras o caracteres extraños
**solucion:** borra la linea y escribela a mano, no copies y pegues

### la consola no aparece
**solucion:** ve a view -> tool windows -> run

## ejemplo de ejecucion

```
=== SISTEMA STARBREW ===

Menu disponible:
1. Espresso (Arabica) - $3000
2. Americano (Robusta) - $3200
3. Latte (Arabica) - $5000
4. Cappuccino (Robusta) - $4200
5. Frapp Caramelo (con crema) - $6600
6. Frapp Mocha (con crema) - $6900
7. Frapp Vainilla  - $4900

Seleccione bebida (numero): 3

Seleccione tamano:
1. Tall (12 oz) - Precio base
2. Grande (16 oz) - +$700
3. Venti (20 oz) - +$1,200
Seleccion: 2

¿Desea agregar otra bebida? (s/n): n

Tipo de cliente (regular/miembro/gold/partner): miembro

Procesando orden...
Estado: Preparando bebida...

=== RESUMEN DE LA ORDEN ===
- Latte (Arabica) grande: $5700
  Precio base: $4500
  Grano Arabica: +500
  Tamano grande: +700

Subtotal: $5700
Descuento miembro (10%): -$570
Subtotal con descuento: $5130
IVA (19%): $974
TOTAL: $6104

Estado final: Lista

Gracias por usar StarBrew!
```
