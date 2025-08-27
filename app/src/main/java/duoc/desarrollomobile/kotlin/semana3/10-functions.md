# Semana 3 – Funciones en Kotlin (Guía paso a paso con Starbucks)

En esta guía iremos construyendo el código **paso a paso**, línea por línea, para aprender cómo funcionan las **funciones en Kotlin**.

El objetivo es que entiendas:
- Qué es una función.
- Qué significa que una función no reciba nada ni devuelva nada.
- Qué significa pasar un valor como parámetro.
- Qué significa que una función devuelva un valor.
- Cómo usar `when` en Kotlin (en lugar de `switch` en Java o C++).
- Cómo manejar entradas inválidas con el operador Elvis `?:`.

---

## 1. Crear la función `main`

Toda aplicación en Kotlin empieza desde la función `main`. Es el punto de entrada.

```kotlin
fun main(){
    // aqui empezamos nuestro programa
}
```

---

## 2. Primera función: sin parámetros ni retorno

Una **función** es un bloque de código que tiene un nombre y que podemos ejecutar varias veces sin tener que volver a escribirlo.

Cuando decimos que no tiene **parámetros** significa que no recibe datos desde afuera. Y cuando decimos que no tiene **retorno**, significa que no devuelve ningún resultado, solo ejecuta instrucciones.

```kotlin
fun hacerCafeStarbucks(){
    println("____________ INIT PROCESO CAFE STARBUCKS ____________")
    println("primero, el cliente pasa por la caja y elige su cafe")
    println("A continuacion, el cliente paga con su medio de pago")
    println("el operador recibe el pedido y prepara el cafe")
    println("el operador verifica el estado del pedido como tamanio, estilo de cafe, etc")
    println("Una vez el operador termina el cafe, procede a llamar al usuario por su nombre")
    println("al usuario se le indica que en la mesa tiene azucar y endulzante y se le entrega su cafe")
    println("____________ END PROCESO CAFE STARBUCKS ____________")
}
```

Para usarla dentro de `main` basta con escribir su nombre:

```kotlin
fun main(){
    hacerCafeStarbucks()
}
```

---

## 3. Segunda función: con parámetros

Un **parámetro** es un dato que una función necesita para funcionar. En este caso queremos que la función pueda recibir el nombre de un cliente, para llamarlo al entregar el café.

Aquí declaramos un parámetro llamado `nombreCliente` de tipo `String`.

```kotlin
fun hacerCafeStarbucksConNombre(nombreCliente: String){
    println("____________ INIT PROCESO CAFE STARBUCKS ____________")
    println("primero, el cliente pasa por la caja y elige su cafe")
    println("A continuacion, el cliente paga con su medio de pago")
    println("el operador recibe el pedido y prepara el cafe")
    println("el operador verifica el estado del pedido como tamanio, estilo de cafe, etc")
    println("Una vez el operador termina el cafe, procede a llamar a $nombreCliente")
    println("al usuario se le indica que en la mesa tiene azucar y endulzante y se le entrega su cafe")
    println("____________ END PROCESO CAFE STARBUCKS ____________")
}
```

Y en `main` ahora podemos pasarle un valor real como argumento:

```kotlin
fun main(){
    hacerCafeStarbucks()
    hacerCafeStarbucksConNombre("Juanito")
}
```

En este caso, **argumento** es el valor real que le pasamos al parámetro.

---

## 4. Tercera función: con retorno

Hasta ahora las funciones solo imprimían mensajes. Pero una función también puede **devolver un valor** al final. A esto se le llama **retorno**.

Declaramos el tipo de retorno después de los paréntesis. En este caso será un `String`.

```kotlin
fun consultarPrecioCafe(numeroCafe: Int): String {
    val cafeEspresso = "1 - Cafe expresso .................. 2.990"
    val cafeCortado = "2 - Cafe cortado ................... 3.490"
    val cafeLungo = "3 - Cafe lungo ..................... 2.990"
    val cafeCapuccino = "4 - Cafe Capuccino ................. 5.990"
    val cafeCapuccinoVainilla = "5 - Cafe Capuccino Vainilla ........ 6.450"

    return when (numeroCafe) {
        1 -> cafeEspresso
        2 -> cafeCortado
        3 -> cafeLungo
        4 -> cafeCapuccino
        5 -> cafeCapuccinoVainilla
        else -> "ese cafe no lo tenemos, quizas en Juan Valdes si"
    }
}
```

---

## 5. Usar la función con retorno en `main`

En `main` pedimos al usuario que escriba un número y lo convertimos a `Int`. Como la entrada puede ser inválida, usamos el **operador Elvis `?:`** para dar un valor seguro si algo falla.

```kotlin
println("Seleccione el cafe que desea consultar precio -> ")
val cafeSeleccionado = readLine()?.toIntOrNull() ?: 0
println("El cafe seleccionado cuesta ${consultarPrecioCafe(cafeSeleccionado)}")
```

- `readLine()` devuelve un texto que el usuario escribió.
- `toIntOrNull()` intenta convertirlo a número, o devuelve `null` si no puede.
- `?: 0` significa “si es nulo, usa cero por defecto”.

---

## 6. Diferencia de estilos en `when`

En Java o C++ usamos `switch case`. En Kotlin usamos `when`.

### Versión con `return` en cada rama

```kotlin
when (numeroCafe) {
    1 -> return cafeEspresso
    2 -> return cafeCortado
    3 -> return cafeLungo
    4 -> return cafeCapuccino
    5 -> return cafeCapuccinoVainilla
    else -> return "ese cafe no lo tenemos"
}
```

### Versión más eficiente en Kotlin (return when unificado)

```kotlin
return when (numeroCafe) {
    1 -> cafeEspresso
    2 -> cafeCortado
    3 -> cafeLungo
    4 -> cafeCapuccino
    5 -> cafeCapuccinoVainilla
    else -> "ese cafe no lo tenemos"
}
```

Ejercicio: **borra los `return` línea por línea** de la primera versión y reemplázalo por `return when`. Verás cómo el código queda más limpio y expresivo.

---

## Programa completo

Con este ejemplo ahora conoces:
1. Funciones sin parámetros ni retorno.
2. Funciones con parámetros.
3. Funciones con retorno.
4. Uso de `when` como expresión en Kotlin.
5. Manejo de entradas inválidas con el operador Elvis `?:`.


## 7. Tarea para los estudiantes


Crea una nueva función llamada `validarAcceso` que simule un sistema de inicio de sesión:


- La función debe recibir como parámetros un **usuario** y una **clave** (ambos tipo `String`).
- La función debe devolver un **Boolean** indicando si el acceso es correcto o no.
- Considera las siguientes reglas usando operadores lógicos:
- El acceso es válido si el usuario es `admin` y la clave es `1234`. Usa `&&`.
- El acceso también es válido si el usuario es `root` aunque la clave esté vacía. Usa `||`.
- Si el usuario no es válido, usa `!=` para rechazarlo.
- Recuerda usar `.lowercase()` para comparar usuarios, como si estuviéramos normalizando datos en una base de datos.


Ejemplo de uso esperado:


```kotlin
println(validarAcceso("Admin", "1234")) // true
println(validarAcceso("root", "")) // true
println(validarAcceso("pepe", "1234")) // false
```


---


## Programa completo


Con este ejemplo ahora conoces:
1. Funciones sin parámetros ni retorno.
2. Funciones con parámetros.
3. Funciones con retorno.
4. Uso de `when` como expresión en Kotlin.
5. Manejo de entradas inválidas con el operador Elvis `?:`.
6. Ejercicio práctico de validación de acceso con parámetros, retorno y operadores lógicos.


Este flujo muestra cómo Kotlin te ayuda a escribir código más modular, reutilizable y seguro.