# Semana 3 – Clases en Kotlin (Guía paso a paso)

En esta guía iremos construyendo el código **paso a paso**, línea por línea, para aprender cómo funcionan las **clases en Kotlin**.

El objetivo es que los estudiantes entiendan:
- Qué es una clase y qué es un objeto.
- Qué significa instanciar una clase.
- Cómo usar el bloque `init`.
- Cómo crear un método dentro de una clase.

---

## 1. Comentario inicial y concepto de clases

```kotlin
// documentacion de clases en kotlin
// https://kotlinlang.org/docs/classes.html

/*
Las clases permiten representar los planos de objetos o instancias
 del mundo real al mundo del desarrollo de software, es parte del
 paradigma de programacion orientado a objetos (POO) o tambien conocido
 como Object Oriented Programming (OOP).

En la medida que avancen con estas secciones veran aspectos mas
 avanzados como herencia, polimorfismo, abstraccion, etc.
*/
```

Aquí dejamos claro qué es una clase: **un plano o molde** para crear objetos en el código que representan elementos del mundo real.

---

## 2. Crear la clase `Persona`

```kotlin
// Para empezar, el modificador para iniciar una clase es class
class Persona{
}
```

Esto crea una clase vacía llamada `Persona`. Todavía no hace nada, pero ya tenemos el plano.

---

## 3. Explicar el bloque `init`

En Kotlin existe el bloque `init`, que se ejecuta automáticamente cuando la clase es instanciada (cuando se crea un objeto). Es similar a un **constructor** en Java o C++.

```kotlin
class Persona{
    init{
        caminar()
    }
}
```

Por ahora, dentro del `init` llamaremos a un método llamado `caminar()`, que crearemos en el siguiente paso.

---

## 4. Crear un método dentro de la clase

Un **método** es simplemente una función que pertenece a una clase. Aquí agregamos un método llamado `caminar()`.

```kotlin
class Persona{
    init{
        caminar()
    }

    fun caminar(){
        println("Mueve pierna izquierda")
        println("Mueve pierna derecha")
    }
}
```

Ahora, cada vez que creemos un objeto de tipo `Persona`, se ejecutará automáticamente el bloque `init`, que a su vez llamará al método `caminar()`.

---

## 5. Crear la función `main` y una instancia de la clase

Para usar la clase debemos crear un **objeto** o **instancia** de ella. Esto se hace con el operador `new` en algunos lenguajes, pero en Kotlin basta con llamar al nombre de la clase seguido de paréntesis.

```kotlin
fun main(){
    var juanito = Persona()
}
```

- Aquí creamos una variable `juanito` (instancia de la clase `Persona`).
- Como la clase tiene un `init`, al instanciarse automáticamente se ejecutará `caminar()`.

La salida en consola será:
```
Mueve pierna izquierda
mueve pierna derecha
```

---

## 6. Resumen

Con este ejemplo aprendimos:
1. Qué es una clase en Kotlin.
2. Cómo usar `init` como bloque de inicialización.
3. Cómo crear un método dentro de una clase.
4. Cómo instanciar una clase en el `main`.

Este es el primer paso en la **Programación Orientada a Objetos (POO)**. Más adelante agregaremos **atributos**, **constructores con parámetros** y conceptos como **herencia** y **polimorfismo**.

---

## Código completo

```kotlin
package duoc.desarrollomobile.kotlin.semana3.code

class Persona{
    init{
        caminar()
    }

    fun caminar(){
        println("Mueve pierna izquierda")
        println("mueve pierna derecha")
    }
}

fun main(){
    var juanito = Persona()
}
```
