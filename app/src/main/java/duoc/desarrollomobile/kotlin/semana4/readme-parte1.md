# README PARTE 1 - Configuración Inicial

## que vamos a hacer?

bueno, vamos a crear un sistema para una cafeteria tipo starbucks pero en consola. la idea es aprender conceptos de programacion orientada a objetos, un poquito de asincronia con coroutines y algunas cosas mas que te voy a ir explicando mientras lo hacemos.

## antes de empezar - que necesitas saber?

no te preocupes si no sabes todo esto, se va a explicar aca:
- que es una clase (como un molde para crear objetos)
- que es herencia (cuando una clase "hereda" cosas de otra)
- funciones basicas de kotlin
- como leer errores (todos nos equivocamos)

## contexto del problema

la cafeteria Starbucks tiene un problema: sus baristas anotan las ordenes en papel y calculan los precios con calculadora. esto causa errores en los calculos, demoras en las ordenes y no tienen forma de aplicar descuentos automaticamente. necesitan un sistema que:

- gestione el menu de bebidas con sus precios
- calcule automaticamente el precio segun el tamano
- aplique descuentos segun el tipo de cliente
- simule el tiempo de preparacion
- genere un resumen detallado como boleta

el sistema debe ser de consola porque es para uso interno de los empleados, no necesita interfaz grafica

## empezemos paso a paso

### paso 1: crear el proyecto en android studio

**ojo: vamos a hacer una aplicacion de consola, NO una app android con pantallas**

1. abre android studio
2. click en "new project"
3. **importante**: selecciona "No Activity" (esta hasta abajo)
4. ponle nombre: "StarBrewSystem"
5. package name: "duoc.desarrollomobile.myapplication" (o lo que quieras)
6. language: **kotlin**
7. minimum sdk: API 21 (o cualquiera)
8. click en "finish"

la estructura de tu proyecto se vera asi:

```
StarBrewSystem/
├── app/
│   ├── src/
│   │   └── main/
│   │       └── java/           <- aunque dice java, aqui van los .kt
│   │           └── duoc/
│   │               └── desarrollomobile/
│   │                   └── myapplication/
│   │                       ├── Main.kt
│   │                       ├── Bebida.kt
│   │                       ├── EstadoOrden.kt
│   │                       └── GestorOrdenes.kt
│   └── build.gradle.kts       <- aqui agregas dependencias si necesitas
```

### paso 1.5: agregar dependencia de coroutines (solo si marca error)

si te marca error con `delay()`, abre `app/build.gradle.kts` y en la seccion `dependencies` agrega:

```kotlin
dependencies {
    // lo que ya esta ahi...
    
    // agrega esta linea para coroutines:
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
}
```

despues dale "sync now" cuando aparezca arriba.

### paso 2: crear los archivos kotlin

**como crear archivos en android studio:**

1. en la vista de proyecto (lado izquierdo), busca tu carpeta del package
2. click derecho en esa carpeta
3. selecciona "new" -> "kotlin class/file"
4. elige el tipo correcto para cada archivo

## que sigue?

ve al **README PARTE 2** para crear el archivo Bebida.kt con toda la explicacion de herencia y polimorfismo