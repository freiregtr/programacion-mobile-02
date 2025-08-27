# Semana 3 - Properties vs Parameters en Kotlin

En este ejercicio veremos la **diferencia entre usar `properties` y `parameters` en una clase de Kotlin**.
La idea es que el estudiante entienda paso a paso cómo se comportan en el acceso a datos, la encapsulación y la reutilización.

---

## 1. Clase con Properties

En Kotlin, si usamos `val` o `var` directamente en el constructor, esos argumentos se convierten en **atributos de la clase** (properties).

```kotlin
class PersonaConProperties(val nombre: String, val apellido: String) {

    init {
        decirNombre()
    }

    fun decirNombre() {
        println("Mi nombre es $nombre")
        println("Mi apellido es $apellido")
    }
}
```

**Explicación línea por línea:**

* `class PersonaConProperties(val nombre: String, val apellido: String)` → al usar `val`, Kotlin convierte estos parámetros en **propiedades** accesibles en todo el objeto.
* `init { ... }` → bloque constructor que se ejecuta al crear la instancia.
* `decirNombre()` → método que imprime los valores usando directamente `nombre` y `apellido`, sin necesidad de variables adicionales.

Aquí no tuvimos que declarar variables internas adicionales.
El compilador de Kotlin crea automáticamente las **propiedades accesibles desde el objeto** (`juanito.nombre`, `juanito.apellido`).

---

## 2. Clase con Parameters

En cambio, si en el constructor **no ponemos `val` ni `var`**, los argumentos son **solo parámetros locales** que se pierden después de construido el objeto.

```kotlin
class PersonaConParametros(nombre: String, apellido: String) {

    private val nombreGuardado: String = nombre
    private val apellidoGuardado: String = apellido

    init {
        decirNombre()
    }

    fun decirNombre() {
        println("Mi nombre es $nombreGuardado")
        println("Mi apellido es $apellidoGuardado")
    }
}
```

**Explicación línea por línea:**

* `class PersonaConParametros(nombre: String, apellido: String)` → aquí solo son parámetros locales.
* `private val nombreGuardado: String = nombre` → guardamos manualmente el valor en una variable de la clase.
* `private val apellidoGuardado: String = apellido` → lo mismo para apellido.
* `init { ... }` → constructor que ejecuta `decirNombre()`.
* `decirNombre()` → accede a las variables privadas ya guardadas.

Aquí fue necesario declarar variables privadas (`nombreGuardado`, `apellidoGuardado`) y asignarles el valor de los parámetros.
Esto da más **encapsulación**, pero más trabajo de código.

---

## 3. Probando las diferencias en `main`

Ahora veremos cómo cambia el acceso cuando creamos objetos de cada clase.

```kotlin
fun main() {
    // Con PROPERTIES
    var juanito = PersonaConProperties("Juanito", "Perez")
    println("El nombre es ${juanito.nombre}")     // Acceso directo
    println("El apellido es ${juanito.apellido}") // Acceso directo

    // Con PARAMETERS
    var pepito = PersonaConParametros("Pepito", "Perez")
    // println("El nombre es ${pepito.nombre}")  //  ERROR, no existe
    // println("El apellido es ${pepito.apellido}") //  ERROR, no existe

    // En este caso, solo podemos llamar al método de la clase:
    pepito.decirNombre()
}
```

**Comparación directa:**

* `juanito.nombre` → funciona porque es una **property**.
* `pepito.nombre` → da error porque solo era un **parámetro local** y no existe como propiedad.
* En el caso de `PersonaConParametros`, debemos usar el método `decirNombre()` para acceder a los datos.

---

## Conclusión

* **Properties (`val/var`)** → más accesibles, ideales cuando queremos exponer atributos directamente.
* **Parameters (sin `val/var`)** → más encapsulados, útiles cuando queremos controlar mejor la visibilidad y seguridad de los datos.

Ambos enfoques son válidos y dependen del diseño que quieras aplicar en tu clase.
