# Semana 3 - While en Kotlin (Guía paso a paso con Quiz Pokémon)

En esta guía iremos construyendo el código **paso a paso**, línea por línea, para aprender el ciclo `while` en Kotlin. El objetivo es que después de cada bloque el estudiante pueda **probar el código** y ver qué ocurre.

---

## 1. Crear la función `main`
```kotlin
fun main(){
    // aqui empezamos nuestro programa
}
```

---

## 2. Definir un contador y un `while` simple
Queremos que el programa imprima un mensaje 3 veces.

```kotlin
fun main(){
    var contar = 0
    while (contar < 3){
        println("se ejecuta este print")
        contar++
    }
}
```

Este programa imprime el mensaje 3 veces y luego termina porque `contar` pasa de 0 → 1 → 2 → 3, y la condición `contar < 3` se vuelve falsa.

---

## 3. Mostrar el estado del contador
Agregamos un `println` dentro del ciclo para ver el valor de `contar` en cada iteración.

```kotlin
fun main(){
    var contar = 0
    while (contar < 3){
        println("se ejecuta este print")
        println("el contador va en $contar")
        contar++
    }
}
```

Ahora la salida muestra la evolución del contador en cada vuelta.

---

## 4. Introducir un Quiz con `while`
Después del ejemplo del contador, haremos un **quiz de Pokémon** en consola. Antes de iniciar el quiz, damos el contexto de las preguntas (esto es importante, porque igual que en ingeniería de requerimientos, el enunciado define lo que vamos a programar).

```kotlin
    // __________________________________________
    // QUIZ CUANTO SABES DE POKEMONES?
    /*
    Vamos a hacer un quiz por consola que permita hacer 4 preguntas:
     -> Cuantas evoluciones tiene Eevee en la primera generacion?
     -> Cual era el nombre del personaje humano principal de Pokemon?
     -> Cual es el nombre del pokemon mas famoso del mundo?
     -> Cuantos pokemones habian en la primera generacion?
    Para eso, una forma de validar que los inputs por teclado cumplan
    criterios de condicion son con While.
    */

    println("Bienvenido al quiz mas pulento de Pokémon!")
    var respuestaQuiz = ""
    var intentosFallidos = 0
```

Con esto preparamos las variables y el enunciado de lo que haremos.

---

## 5. Primera pregunta (explicación paso a paso)
La primera pregunta la desarrollamos línea por línea para entender cómo funciona el `while` en validaciones.

```kotlin
    // primera pregunta
    println("La pregunta 1 indica, ¿cuántos tipos de evoluciones tiene Eevee en la primera generación?")
    respuestaQuiz = readLine().toString()
```
Aquí mostramos la pregunta y capturamos lo que escribe el usuario en `respuestaQuiz`.

```kotlin
    while(respuestaQuiz.lowercase() != "3"){
        println("Incorrecto, Eevee no tiene la cantidad de $respuestaQuiz evoluciones")
        println("Nuevamente, ¿cuántos tipos de evoluciones tiene Eevee?")
        intentosFallidos++
        respuestaQuiz = readLine().toString()
    }
```
Aquí usamos el `while`:
- La condición `respuestaQuiz.lowercase() != "3"` significa que mientras la respuesta no sea "3", se seguirá repitiendo.
- Dentro del ciclo mostramos un mensaje de error y volvemos a pedir la respuesta.
- Contamos cada error sumando a `intentosFallidos`.

```kotlin
    println("Felicitaciones, Eevee tiene $respuestaQuiz evoluciones distintas")
```
Finalmente, cuando el usuario escribe "3", el `while` termina y mostramos el mensaje de acierto.

---

## 6. Segunda pregunta
Preguntamos por el personaje principal humano de Pokémon. Aquí repetimos el mismo esquema de la pregunta 1.

```kotlin
    // segunda pregunta
    println("Vamos con la siguiente pregunta! ¿Cuál era el nombre del personaje humano principal de Pokémon?")
    respuestaQuiz = readLine().toString()
    while (respuestaQuiz.lowercase() != "ash"){
        println("Nope, $respuestaQuiz no es el protagonista de Pokémon la serie")
        println("Nuevamente, ¿cuál era el nombre del personaje humano principal de Pokémon?")
        intentosFallidos++
        respuestaQuiz = readLine().toString()
    }
    println("Felicidades, $respuestaQuiz es el protagonista de Pokémon la serie, ¡super!")
```

---

## 7. Tercera pregunta
Validamos el Pokémon más famoso del mundo.

```kotlin
    // tercera pregunta
    println("Tercera pregunta: ¿Cuál es el nombre del Pokémon más famoso del mundo?")
    respuestaQuiz = readLine().toString()
    while (respuestaQuiz.lowercase() != "pikachu"){
        println("NEIN, el Pokémon $respuestaQuiz no es el más famoso del mundo mundial")
        println("Nuevamente, ¿cuál es el nombre del Pokémon más famoso del mundo?")
        intentosFallidos++
        respuestaQuiz = readLine().toString()
    }
    println("Fantástico, $respuestaQuiz es el Pokémon más famoso del mundo")
```

---

## 8. Cuarta pregunta
Preguntamos por el número total de Pokémon en la primera generación.

```kotlin
    // cuarta pregunta
    println("Última pregunta: ¿Cuántos Pokémon había en la primera generación?")
    respuestaQuiz = readLine().toString()
    while (respuestaQuiz.lowercase() != "151"){
        println("jaja, te equivocaste de temporada, LOL")
        println("Nuevamente, ¿cuántos Pokémon había en la primera generación?")
        intentosFallidos++
        respuestaQuiz = readLine().toString()
    }
    println("Así es, la primera generación tuvo 151 Pokémon, usted sabe!")
```

---

## 9. Mensaje final con `if else`
Usamos un condicional para premiar al usuario según sus errores.

```kotlin
    if(intentosFallidos == 0){
        println("Felicitaciones, usted no ha tenido fallas, es una enciclopedia")
    }else{
        println("Felicitaciones, a pesar de los errores, ha logrado cumplir el ciclo")
    }
```

Dependiendo de los intentos fallidos, se muestra un mensaje distinto.

---

## Programa completo
El programa combina:
1. Un `while` simple con un contador.
2. Un quiz de Pokémon con validaciones de entrada usando `while`.
3. Un cierre con `if else`.

De esta forma, se demuestra que `while` sirve tanto para **repetir procesos controlados por un contador** como para **validar entradas del usuario**.
