# README PARTE 2 - Bebida.kt (Herencia y Polimorfismo)

## paso 2.1: crear el archivo Bebida.kt

**crear archivo:** click derecho -> new -> kotlin class/file -> selecciona "class" -> nombre: "Bebida"

este es el corazon del sistema. aqui definimos que es una bebida y sus tipos.

### 2.1.1 por que usar una clase abstracta?

necesitamos una clase abstracta porque todas las bebidas comparten cosas comunes (nombre, precio) pero cada tipo de bebida calcula su precio final de forma diferente. un cafe suma extra si es arabica, un frappuccino suma si tiene crema. no queremos repetir codigo pero tampoco queremos una logica generica que no sirva para todos.

```kotlin
abstract class Bebida(
    val nombre: String,
    val precioBase: Double,
    val tiempoPreparacion: Int = 4
)
```

### 2.1.2 por que el metodo calcularPrecio es abstracto?

cada bebida tiene su propia forma de calcular el precio. no podemos definir una sola logica que funcione para todos. es como decir "toda bebida debe saber calcular su precio, pero cada una lo hace a su manera". por eso declaramos:

```kotlin
abstract fun calcularPrecio(tamano: String): Double
```

esto obliga a cada clase hija a implementar su propia version del metodo.

**pero por que obtenerPrecioTamano NO es abstracto?**

porque obtenerPrecioTamano funciona IGUAL para todas las bebidas. no importa si es cafe o frappuccino, el tamano grande siempre cuesta 700 mas, el venti siempre cuesta 1200 mas. es una logica comun que no cambia segun el tipo de bebida. cuando algo funciona igual para todos, lo defines una vez en la clase padre y listo, no necesitas que cada hijo lo reimplemente.

### 2.1.3 codigo completo de la clase abstracta Bebida

```kotlin
// Bebida.kt
package duoc.desarrollomobile.myapplication  // cambia esto por tu package!

// abstract significa que esta clase es como un molde general
// nunca vamos a crear una bebida generica, siempre sera un cafe o frappe especifico
abstract class Bebida(
    val nombre: String,             // toda bebida tiene nombre
    val precioBase: Double,         // toda bebida tiene precio base
    val tiempoPreparacion: Int = 4  // por defecto 4 segundos
) {
    // 'abstract fun' es como un contrato o promesa
    // estamos diciendo toda bebida DEBE saber calcular su precio, pero no voy a decir como"
    // es como la funcion hablar() en humanos: todos los humanos pueden hablar
    // pero un mexicano habla espanol, un americano ingles, un chino mandarin
    // la funcion hablar() existe en todos, pero cada uno la implementa diferente
    abstract fun calcularPrecio(tamano: String): Double
    
    // este metodo es comun para todas las bebidas
    fun obtenerPrecioTamano(tamano: String): Double {
        // when es como el switch de java pero mas poderoso
        return when(tamano.lowercase()) {
            "tall" -> 0.0      // tall es el tamano base, no cobra extra
            "grande" -> 700.0   // grande cuesta 700 mas
            "venti" -> 1200.0   // venti cuesta 1200 mas
            else -> 0.0         // si pone algo raro, no cobramos extra
        }
    }
}
```

### 2.1.4 por que Cafe y Frappuccino heredan de Bebida?

usamos herencia para no repetir codigo. tanto cafe como frappuccino necesitan nombre y precio, eso ya lo tiene bebida. pero cada uno calcula precios diferente: cafe suma por tipo de grano, frappuccino suma por crema. la herencia nos deja compartir lo comun y personalizar lo especifico.

```kotlin
// aqui es donde aplicamos herencia
// el ': Bebida' despues del nombre significa cafe hereda de cebida
// es como decir cafe ES UNA bebida
// cuando heredas, no solo recibes los atributos (nombre, precioBase)
// tambien heredas los metodos que luego VAS a implementar con tu propia logica
// Cafe recibe todo de Bebida y DEBE implementar calcularPrecio() a su manera
class Cafe(
    nombre: String,         // nota que aqui NO ponemos 'val' porque lo pasamos al padre
    precioBase: Double,    
    val tipoGrano: String,  // este SI tiene val porque es propio de Cafe
    val intensidad: Int     // este tambien es exclusivo de Cafe
) : Bebida(nombre, precioBase) {  // aqui le pasamos los parametros al constructor padre
    
    // 'override' significa que estamos sobrescribiendo el metodo abstracto del padre
    override fun calcularPrecio(tamano: String): Double {
        var precio = precioBase  
        
        // el arabica es mas caro
        if (tipoGrano.lowercase() == "arabica") {
            precio += 500  
        }
        
        precio += obtenerPrecioTamano(tamano)
        return precio
    }
}

// segunda clase hija
class Frappuccino(
    nombre: String,
    precioBase: Double,
    val tieneCream: Boolean,  // boolean = true o false
    val sabor: String
) : Bebida(nombre, precioBase) {  
    
    override fun calcularPrecio(tamano: String): Double {
        var precio = precioBase
        
        if (tieneCream) {
            precio += 800  // la crema es cara
        }
        
        // listOf crea una lista temporal solo para verificar si el sabor esta ahi
        if (sabor.lowercase() in listOf("caramelo", "mocha")) {
            precio += 300  // sabores premium
        }
        
        precio += obtenerPrecioTamano(tamano)
        return precio
    }
}
```
## Codigo completo de la primera seccion

```kotlin

// Bebida.kt
package duoc.desarrollomobile.myapplication  // cambia esto por tu package

// 'abstract' significa que esta clase es como un molde general
// nunca vamos a crear una "bebida" generica, siempre sera un cafe o frappe especifico
abstract class Bebida(
    val nombre: String,           // toda bebida tiene nombre
    val precioBase: Double,        // toda bebida tiene precio base
    val tiempoPreparacion: Int = 4 // por defecto 4 segundos
) {
    // 'abstract fun' es como un contrato o promesa
    // estamos diciendo "toda bebida DEBE saber calcular su precio, pero no voy a decir como"
    // es como la funcion hablar() en humanos: todos los humanos pueden hablar
    // pero un mexicano habla espanol, un americano ingles, un chino mandarin
    // la funcion hablar() existe en todos, pero cada uno la implementa diferente
    abstract fun calcularPrecio(tamano: String): Double
    
    // este metodo es comun para todas las bebidas
    fun obtenerPrecioTamano(tamano: String): Double {
        // when es como el switch de java pero mas poderoso
        return when(tamano.lowercase()) {
            "tall" -> 0.0      // tall es el tamano base, no cobra extra
            "grande" -> 700.0   // grande cuesta 700 mas
            "venti" -> 1200.0   // venti cuesta 1200 mas
            else -> 0.0         // si pone algo raro, no cobramos extra
        }
    }
}

// AQUI VIENE LA HERENCIA!
// el ': Bebida' despues del nombre significa "Cafe hereda de Bebida"
// es como decir "Cafe ES UNA Bebida"
// cuando heredas, no solo recibes los atributos (nombre, precioBase)
// tambien heredas los metodos que luego VAS a implementar con tu propia logica
// Cafe recibe todo de Bebida y DEBE implementar calcularPrecio() a su manera
class Cafe(
    nombre: String,        // nota que aqui NO ponemos 'val' porque lo pasamos al padre
    precioBase: Double,    
    val tipoGrano: String, // este SI tiene 'val' porque es propio de Cafe
    val intensidad: Int    // este tambien es exclusivo de Cafe
) : Bebida(nombre, precioBase) {  // aqui le pasamos los parametros al constructor padre
    
    // 'override' significa que estamos sobrescribiendo el metodo abstracto del padre
    override fun calcularPrecio(tamano: String): Double {
        var precio = precioBase  
        
        // el arabica es mas caro
        if (tipoGrano.lowercase() == "arabica") {
            precio += 500  
        }
        
        precio += obtenerPrecioTamano(tamano)
        return precio
    }
}

// segunda clase hija
class Frappuccino(
    nombre: String,
    precioBase: Double,
    val tieneCream: Boolean,  // boolean = true o false
    val sabor: String
) : Bebida(nombre, precioBase) {  
    
    override fun calcularPrecio(tamano: String): Double {
        var precio = precioBase
        
        if (tieneCream) {
            precio += 800  // la crema es cara
        }
        
        // listOf crea una lista temporal solo para verificar si el sabor esta ahi
        if (sabor.lowercase() in listOf("caramelo", "mocha")) {
            precio += 300  // sabores premium
        }
        
        precio += obtenerPrecioTamano(tamano)
        return precio
    }
}

```

### 2.1.5 resumen de por que lo hicimos asi

**por que usamos herencia?**
imaginate que tienes 50 tipos de bebidas. sin herencia, tendrias que repetir el codigo de nombre y precio en cada una. con herencia, lo escribes una vez en la clase padre y ya.

**por que polimorfismo?**
porque cada bebida calcula su precio de forma diferente. el cafe suma si es arabica, el frappuccino suma si tiene crema. con polimorfismo cada clase implementa calcularPrecio() a su manera pero todas responden al mismo metodo.

**por que abstract?**
porque no tiene sentido crear una bebida generica, ademas que no se puede instanciar. siempre sera un cafe o un frappuccino especifico. la clase abstracta nos obliga a crear clases concretas en base a algo, ademas, nos ayuda a generar menos lineas de codigo reutilizando

## que sigue?

ve al **README PARTE 3** para crear EstadoOrden.kt y aprender sobre sealed classes