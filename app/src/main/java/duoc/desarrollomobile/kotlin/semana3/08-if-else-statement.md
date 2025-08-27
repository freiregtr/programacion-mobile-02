# Semana 2 - If Else en Kotlin (Guía paso a paso)

En esta guía iremos construyendo el código **paso a paso**, como si fuera documentación de un proyecto. Cada sección incluye:
- **Explicación** → qué vamos a hacer.
- **Construcción progresiva** → añadimos líneas poco a poco.
- **Bloque completo hasta el momento** → así, si te pierdes, puedes copiarlo y seguir.
- **Comentarios y reflexiones** → para aprender la lógica detrás.
- **Desafío final** → para pensar en mejoras.

---

## 1. Ejemplo simple: If Else Statement

### Paso 1: Crear una condición
```kotlin
val condicion = true
```

### Paso 2: Agregar el If Else
```kotlin
val condicion = true

if(condicion){
    println("la condicion es verdadera")
}else{
    println("la condicion es falsa")
}
```

### Explicación
Con `if else` podemos usar operadores de comparación:
- `==` Igual a
- `>` Mayor que
- `<` Menor que
- `>=` Mayor o igual que
- `<=` Menor o igual que

---

## 2. Ejercicio: Saber la edad de una persona

Queremos saber la edad de una persona preguntando el año de nacimiento y si ya cumplió años.

### Paso 1: Variables iniciales
```kotlin
fun main(){
    var edadUsuario = 0
    val anioActual = 2025
}
```

### Paso 2: Pedir el año de nacimiento
```kotlin
fun main(){
    var edadUsuario = 0
    val anioActual = 2025

    println("por favor ingrese su anio de nacimiento")
    var anioNascimiento = readLine()
}
```

### Paso 3: Convertir el dato a Int
```kotlin
fun main(){
    var edadUsuario = 0
    val anioActual = 2025

    println("por favor ingrese su anio de nacimiento")
    var anioNascimiento = readLine()

    var anioComparacion = anioNascimiento?.toInt()!!
}
```

### Paso 4: Calcular la edad
```kotlin
fun main(){
    var edadUsuario = 0
    val anioActual = 2025

    println("por favor ingrese su anio de nacimiento")
    var anioNascimiento = readLine()
    var anioComparacion = anioNascimiento?.toInt()!!

    edadUsuario = anioActual - anioComparacion
}
```

### Paso 5: Preguntar si cumplió años
```kotlin
fun main(){
    var edadUsuario = 0
    val anioActual = 2025

    println("por favor ingrese su anio de nacimiento")
    var anioNascimiento = readLine()
    var anioComparacion = anioNascimiento?.toInt()!!

    edadUsuario = anioActual - anioComparacion

    print("usted ya ha estado de cumpleanios? true | false: ")
    var isCumpleanios = readLine().toBoolean()
}
```

### Paso 6: Usar If Else para calcular edad final
```kotlin
fun main(){
    var edadUsuario = 0
    val anioActual = 2025

    println("por favor ingrese su anio de nacimiento")
    var anioNascimiento = readLine()
    var anioComparacion = anioNascimiento?.toInt()!!

    edadUsuario = anioActual - anioComparacion

    print("usted ya ha estado de cumpleanios? true | false: ")
    var isCumpleanios = readLine().toBoolean()

    if(isCumpleanios){
        println("usted tiene $edadUsuario anios")
    }else{
        println("usted tiene ${edadUsuario - 1} anios")
    }
}
```

### Reflexión
- Kotlin no tiene primitivos: todo son objetos.
- `!!` fuerza un valor, pero si es `null` explota.
- `toBoolean()` convierte el texto ingresado a `true/false`.

---

## 3. Sistema de calificación de notas

### Paso 1: Leer nota del alumno
```kotlin
println("Por favor, ingrese la nota de su ultimo examen")
val notaUsuario = readLine()!!.toDouble()
```

### Paso 2: If Else para validar nota
```kotlin
if(notaUsuario >= 6.0){
    println("su nota $notaUsuario es EXCELENTE")
}else if(notaUsuario >= 4.0){
    println("su nota $notaUsuario esta APROBADO")
}else{
    println("su nota $notaUsuario esta REPROBADO")
}
```

### Reflexión
- El último `else if` (`nota < 4.0`) puede ser solo un `else`.
- Kotlin permite concatenar strings con `+` o con `$variable`.

---

## 4. Sistema para saber si un alumno pasa el curso

Queremos evaluar **nota y asistencia** con distintas reglas.

### Paso 1: Pedir nota y asistencia
```kotlin
println("Ingrese su nota por favor, Ej: 6.4")
val notaDeAlumno = readLine()!!.toDouble()
println("Ingrese su porcentaje de asistencia sin el % al final. Ej: 85")
val asistenciaAlumno = readLine()!!.toInt()
```

### Paso 2: Condiciones de aprobación
```kotlin
if(notaDeAlumno >= 4.0 && asistenciaAlumno >= 70){
    println("el alumno ha pasado el semestre")
}else if (notaDeAlumno >= 4.0 && (asistenciaAlumno >= 50 && asistenciaAlumno < 70)){
    println("el alumno debe ser evaluado por el profesor")
}else if(notaDeAlumno == 7.0 || asistenciaAlumno == 100){
    println("el alumno se gana una estrellita por cumplir una de las dos condiciones")
}else{
    println("el alumno ha reprobado el curso")
}
```

### Reflexión
- `&&` exige cumplir **dos condiciones a la vez**.
- `||` se cumple con **una de las dos condiciones**.
- El programa puede tener un **bug lógico**: alguien con 7.0 pero 40% asistencia igual obtiene estrellita. Esto lo dejamos como desafío.

---

## 5. Desafío final

El ejercicio de **SISTEMA PARA SABER SI UN ALUMNO PASA EL CURSO** tiene un **bug lógico**.

### Preguntas
1. ¿Qué pasa si un alumno tiene 7.0 de nota pero solo 40% de asistencia?
2. ¿Debería obtener la estrellita?
3. ¿Cómo se podría mejorar la lógica para que sea más justa?