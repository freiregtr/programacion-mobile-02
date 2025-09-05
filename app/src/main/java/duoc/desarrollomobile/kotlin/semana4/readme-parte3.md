# README PARTE 3 - EstadoOrden y GestorOrdenes

## paso 2.2: crear EstadoOrden.kt

**crear archivo:** click derecho -> new -> kotlin class/file -> selecciona "sealed class" -> nombre: "EstadoOrden"

### 2.2.1 por que necesitamos estados?

cuando un cliente ordena, la bebida no aparece magicamente. pasa por un proceso: primero recibimos la orden, luego la preparamos, y finalmente esta lista. necesitamos saber en que parte del proceso estamos. sin estados, no sabriamos si la bebida esta preparandose o ya esta lista. es como un paquete de amazon: necesitas saber si esta en el almacen, en camino o entregado

```kotlin
object Recibida : EstadoOrden()
object Preparando : EstadoOrden()
object Lista : EstadoOrden()
```

### 2.2.2 por que usar sealed class para los estados?

podriamos usar strings ("recibida", "preparando", "lista") pero que pasa si alguien escribe "recivida" con falta ortografica? el programa no sabria que hacer. sealed class nos garantiza que SOLO existen los estados que definimos, no hay forma de crear un estado invalido. es seguridad en tiempo de compilacion.
https://kotlinlang.org/docs/sealed-classes.html

```kotlin
sealed class EstadoOrden {
    // aqui van los estados permitidos
}
```

### 2.2.3 por que el estado Error necesita llevar informacion extra?

porque no todos los errores son iguales. puede fallar porque no hay leche, porque la maquina esta descompuesta, o porque el cliente cancelo. si solo tenemos un estado "Error" sin mas info, no sabemos que paso. con data class podemos guardar el mensaje especifico del problema.

```kotlin
data class Error(val mensaje: String) : EstadoOrden()
```

### 2.2.4 codigo completo de EstadoOrden.kt

```kotlin
// EstadoOrden.kt
package duoc.desarrollomobile.myapplication  

// 'sealed class' es como un enum pero con superpoderes
// una orden SOLO puede estar en uno de estos estados
sealed class EstadoOrden {
    // 'object' significa que solo hay UNA instancia de este estado
    object Recibida : EstadoOrden()
    object Preparando : EstadoOrden()
    object Lista : EstadoOrden()
    
    // 'data class' puede llevar informacion extra
    data class Error(val mensaje: String) : EstadoOrden()
}

// por que sealed class y no enum?
// porque Error necesita un mensaje personalizado
// con enum no podrias hacer eso
```

## paso 2.3: crear GestorOrdenes.kt

**crear archivo:** click derecho -> new -> kotlin class/file -> selecciona "class" -> nombre: "GestorOrdenes"

este archivo tiene toda la logica del negocio:

### 2.3.1 por que usamos listOf y agregamos objetos directamente?

listOf crea una lista inmutable, significa que nadie puede agregar o quitar bebidas por accidente. agregamos los objetos directamente en la lista porque es nuestro menu fijo. cada linea crea una bebida nueva con sus propias caracteristicas. estamos creando objetos reales de nuestras clases, no son strings ni numeros, son bebidas completas con todos sus metodos.
https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/list-of.html

```kotlin
Cafe("Cappuccino", 4200.0, "Robusta", 3),
Frappuccino("Frapp Caramelo", 5500.0, true, "Caramelo"),
```

### 2.3.2 por que usamos forEachIndexed?

necesitamos recorrer toda la lista del menu para mostrarle al usuario todas las bebidas disponibles. usamos forEachIndexed porque necesitamos dos cosas de cada bebida en el menu: su posicion (para que el usuario pueda elegir con un numero) y la bebida en si (para mostrar su nombre y precio). estamos recorriendo la coleccion menu que tiene todas nuestras bebidas. por cada bebida en esa lista, vamos a mostrar una linea en pantalla.
https://stackoverflow.com/questions/46311279/kotlin-difference-between-foreachindexed-and-for-in-loop

```kotlin
menu.forEachIndexed { index, bebida ->
```

### 2.3.3 por que calculamos precio y extra por separado?

necesitamos calcular el precio diferente segun el tipo de bebida. cafe suma si es arabica, frappuccino suma si tiene crema. no podemos calcular todo igual. lo mismo con extra: cafe muestra el tipo de grano, frappuccino muestra si tiene crema. son informaciones diferentes que necesitamos mostrar de forma diferente.

```kotlin
val precio = when(bebida) {
    is Cafe -> { /* logica para cafe */ }
    is Frappuccino -> { /* logica para frappuccino */ }
}
val extra = when(bebida) {
    is Cafe -> "(${bebida.tipoGrano})"
    is Frappuccino -> if(bebida.tieneCream) "(con crema)" else ""
}
```

### 2.3.4 por que construimos la linea asi?

estamos armando lo que el usuario va a ver en el menu. index + 1 porque los humanos cuentan desde 1, no desde 0. concatenamos todo para formar una linea legible como "1. Espresso (Arabica) - $3000".

```kotlin
val linea = (index + 1).toString() + ". " + bebida.nombre + " " + extra + " - $" + precio
println(linea)
```

### 2.3.5 por que obtenerBebida retorna Bebida? con signo de pregunta?

el signo ? significa que puede retornar null. si el usuario pone 99 y solo hay 7 bebidas, no podemos retornar una bebida que no existe. retornamos null para indicar "no encontre nada". el if verifica si el numero esta entre 1 y el tamano del menu. "in 1..menu.size" es como decir "si indice es mayor o igual a 1 Y menor o igual al tamano del menu".

```kotlin
fun obtenerBebida(indice: Int): Bebida?

if (indice in 1..menu.size)
```

### 2.3.6 por que procesarOrden es suspend y simula preparacion?

suspend significa que esta funcion puede pausarse sin congelar todo el programa. es asincrono: mientras esperamos que la bebida se prepare, el programa puede hacer otras cosas. usamos delay(4000) para simular que el barista tarda 4 segundos preparando la bebida. en la vida real, no queremos que el sistema se congele mientras preparan el cafe. retornamos EstadoOrden.Lista porque despues de esperar, la bebida ya esta lista.
https://kotlinlang.org/docs/composing-suspending-functions.html

```kotlin
suspend fun procesarOrden(bebida: Bebida, tamano: String): EstadoOrden {
    delay(4000)  // simula 4 segundos de preparacion
    return EstadoOrden.Lista
}
```

### 2.3.7 por que generarResumen recibe parametros y analiza tipos de bebida?

generarResumen es una funcion que recibe parametros, NO es un constructor. los parametros son variables temporales que solo existen dentro de la funcion. necesitamos analizar si es Cafe o Frappuccino otra vez porque queremos mostrar informacion diferente en el resumen (como una boleta). mostrarMenu es para elegir, generarResumen es para confirmar lo que compraste con todos los detalles y calculos.

```kotlin
fun generarResumen(
    bebida: Bebida,      // parametro, no propiedad
    tamano: String,      // solo existe en esta funcion
    tipoCliente: String,
    estadoFinal: EstadoOrden
)
```

### 2.3.8 por que mostramos informacion diferente segun el tipo de bebida?

estamos creando una boleta detallada. cada tipo de bebida tiene informacion especifica que mostrar. para cafe mostramos el tipo de grano y si es arabica el recargo. para frappuccino mostramos si tiene crema. es como en una boleta real donde cada producto muestra sus caracteristicas especiales.

```kotlin
when(bebida) {
    is Cafe -> {
        println("- " + bebida.nombre + " (" + bebida.tipoGrano + ") " + tamano + ": $" + precioFinal.toInt())
        println("  Precio base: $" + bebida.precioBase.toInt())
        if (bebida.tipoGrano == "Arabica") {
            println("  Grano Arabica: +500")
        }
    }
    is Frappuccino -> {
        println("- " + bebida.nombre + " " + tamano + ": $" + precioFinal.toInt())
        println("  Precio base: $" + bebida.precioBase.toInt())
        if (bebida.tieneCream) {
            println("  Crema batida: +800")
        }
    }
    else -> {
        println("- " + bebida.nombre + " " + tamano + ": $" + precioFinal.toInt())
    }
}
```

### 2.3.9 por que solo mostramos el tamano si no es tall?

tall es el tamano base, no cobra extra. seria redundante mostrar "Tamano tall: +0". solo mostramos el recargo del tamano cuando realmente hay un costo adicional (grande o venti). es como en una boleta real: solo muestras los extras que cuestan, no los que son gratis.

```kotlin
if (tamano != "tall") {
    println("  Tamano " + tamano + ": +" + bebida.obtenerPrecioTamano(tamano).toInt())
}
```

### 2.3.10 por que calculamos descuentos e impuestos en ese orden?

seguimos el orden logico de una boleta: primero el precio, luego el descuento si eres miembro, luego el iva sobre el monto con descuento. guardamos cada calculo en una variable para poder mostrarlo paso a paso. solo mostramos el descuento si es mayor a 0 porque no tiene sentido mostrar "Descuento regular (0%): -$0".

```kotlin
val descuento = calcularDescuento(tipoCliente)
val montoDescuento = precioFinal * descuento
val subtotalConDescuento = precioFinal - montoDescuento
val iva = subtotalConDescuento * 0.19
val total = subtotalConDescuento + iva

println("\nSubtotal: $" + precioFinal.toInt())

if (descuento > 0) {
    val porcentaje = (descuento * 100).toInt()
    println("Descuento " + tipoCliente + " (" + porcentaje + "%): -$" + montoDescuento.toInt())
    println("Subtotal con descuento: $" + subtotalConDescuento.toInt())
}

println("IVA (19%): $" + iva.toInt())
println("TOTAL: $" + total.toInt())
```

### 2.3.11 codigo completo de GestorOrdenes.kt

```kotlin
// GestorOrdenes.kt
package duoc.desarrollomobile.myapplication

import kotlinx.coroutines.delay

class GestorOrdenes {
    
    // el menu con todas las bebidas
    private val menu = listOf(
        Cafe("Espresso", 2500.0, "Arabica", 5),
        Cafe("Americano", 3200.0, "Robusta", 3),
        Cafe("Latte", 4500.0, "Arabica", 2),
        Cafe("Cappuccino", 4200.0, "Robusta", 3),
        Frappuccino("Frapp Caramelo", 5500.0, true, "Caramelo"),
        Frappuccino("Frapp Mocha", 5800.0, true, "Mocha"),
        Frappuccino("Frapp Vainilla", 4900.0, false, "Vainilla")
    )
    
    fun mostrarMenu() {
        println("\n=== SISTEMA STARBREW ===\n")
        println("Menu disponible:")
        
        menu.forEachIndexed { index, bebida ->
            // when es como el switch de java pero mas poderoso
            // IMPORTANTE: el when necesita else para que kotlin este feliz
            val precio = when(bebida) {
                is Cafe -> {
                    bebida.precioBase.toInt() + if(bebida.tipoGrano == "Arabica") 500 else 0
                }
                is Frappuccino -> {
                    bebida.precioBase.toInt() + if(bebida.tieneCream) 800 else 0
                }
                else -> bebida.precioBase.toInt()  // siempre pon else aunque no se use
            }
            
            val extra = when(bebida) {
                is Cafe -> "(" + bebida.tipoGrano + ")"
                is Frappuccino -> if(bebida.tieneCream) "(con crema)" else ""
                else -> ""  // otro else necesario
            }
            
            // mostramos cada bebida numerada
            val linea = (index + 1).toString() + ". " + bebida.nombre + " " + extra + " - $" + precio
            println(linea)
        }
    }
    
    fun obtenerBebida(indice: Int): Bebida? {
        return if (indice in 1..menu.size) {
            menu[indice - 1]  // -1 porque las listas empiezan en 0
        } else {
            null  // si pone un numero invalido
        }
    }
    
    fun calcularDescuento(tipoCliente: String): Double {
        return when(tipoCliente.lowercase()) {  // when = switch de java
            "regular" -> 0.0
            "miembro" -> 0.10
            "gold" -> 0.15
            "partner" -> 0.30
            else -> 0.0
        }
    }
    
    // 'suspend' permite usar delay sin bloquear el programa
    suspend fun procesarOrden(bebida: Bebida, tamano: String): EstadoOrden {
        println("\nProcesando orden...")
        println("Estado: Preparando bebida...")
        delay(4000)  // espera 4 segundos
        return EstadoOrden.Lista
    }
    
    fun generarResumen(
        bebida: Bebida, 
        tamano: String, 
        tipoCliente: String,
        estadoFinal: EstadoOrden
    ) {
        println("\n=== RESUMEN DE LA ORDEN ===")
        
        val precioFinal = bebida.calcularPrecio(tamano)
        
        // mostramos diferente info segun el tipo de bebida
        when(bebida) {  // when = switch de java
            is Cafe -> {
                println("- " + bebida.nombre + " (" + bebida.tipoGrano + ") " + tamano + ": $" + precioFinal.toInt())
                println("  Precio base: $" + bebida.precioBase.toInt())
                if (bebida.tipoGrano == "Arabica") {
                    println("  Grano Arabica: +500")
                }
            }
            is Frappuccino -> {
                println("- " + bebida.nombre + " " + tamano + ": $" + precioFinal.toInt())
                println("  Precio base: $" + bebida.precioBase.toInt())
                if (bebida.tieneCream) {
                    println("  Crema batida: +800")
                }
            }
            else -> {
                println("- " + bebida.nombre + " " + tamano + ": $" + precioFinal.toInt())
            }
        }
        
        if (tamano != "tall") {
            println("  Tamano " + tamano + ": +" + bebida.obtenerPrecioTamano(tamano).toInt())
        }
        
        // calculamos descuentos e impuestos
        val descuento = calcularDescuento(tipoCliente)
        val montoDescuento = precioFinal * descuento
        val subtotalConDescuento = precioFinal - montoDescuento
        val iva = subtotalConDescuento * 0.19
        val total = subtotalConDescuento + iva
        
        println("\nSubtotal: $" + precioFinal.toInt())
        
        if (descuento > 0) {
            val porcentaje = (descuento * 100).toInt()
            println("Descuento " + tipoCliente + " (" + porcentaje + "%): -$" + montoDescuento.toInt())
            println("Subtotal con descuento: $" + subtotalConDescuento.toInt())
        }
        
        println("IVA (19%): $" + iva.toInt())
        println("TOTAL: $" + total.toInt())
        
        println("\nEstado final: Lista")
    }
}
```

## puntos importantes del codigo:

### por que when necesita else?
kotlin quiere estar seguro de que cubres TODOS los casos posibles. aunque nosotros sabemos que solo hay cafe y frappuccino, kotlin no puede garantizarlo, por eso pide else

### por que suspend fun?
la palabra `suspend` permite que la funcion use `delay()` sin bloquear todo el programa. es como pausar solo esa parte mientras el resto sigue funcionando

### por que listOf?
creamos una lista inmutable con todas las bebidas del menu. inmutable significa que no se puede cambiar, es mas seguro

## que sigue?

ve al **README PARTE 4** para crear Main.kt y ejecutar el programa