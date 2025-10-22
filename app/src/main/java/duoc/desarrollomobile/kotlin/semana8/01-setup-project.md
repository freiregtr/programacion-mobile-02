# Setup: Proyecto Android con Kotlin, Jetpack Compose, Room y Material3

**Basado en documentación oficial:**
- [Android Developers - Jetpack Compose](https://developer.android.com/develop/ui/compose)
- [Material Design 3](https://m3.material.io/develop/android/jetpack-compose)
- [Architecture Components - ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [Room Persistence Library](https://developer.android.com/training/data-storage/room)
- [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)
- [Consuming flows safely in Jetpack Compose](https://medium.com/androiddevelopers/consuming-flows-safely-in-jetpack-compose-cde014d0d5a3)
- [A safer way to collect flows from Android UIs](https://medium.com/androiddevelopers/a-safer-way-to-collect-flows-from-android-uis-23080b1f8bda)

---

## Introducción

### ¿Qué es Material3?

**Material3** es el sistema de diseño de Google con componentes modernos, temas dinámicos y mejor accesibilidad.

### ¿Qué es MVVM?

**MVVM** (Model-View-ViewModel) es un patrón arquitectónico que separa la lógica de negocio de la interfaz de usuario.

| Capa | Responsabilidad |
|------|-----------------|
| **Model** | Datos y lógica de negocio |
| **ViewModel** | Estado y lógica de presentación |
| **View** | Interfaz de usuario (Compose) |

**Flujo de datos:**
```
View → ViewModel → Model
  ↑         ↓
  ←─────────┘
(Estado actualizado)
```

### ¿Qué es Room?

**Room** es una biblioteca de persistencia que proporciona una capa de abstracción sobre SQLite. Room facilita el trabajo con bases de datos locales mediante anotaciones y validaciones en tiempo de compilación.

**Beneficios de Room:**
- Type-safe queries validadas en compilación
- Menos código boilerplate que SQLite directo
- Integración perfecta con LiveData y Flow
- Soporte para corrutinas de Kotlin

---

## ¿Qué vamos a construir?

En esta serie de documentos crearemos **TiendApp**, una aplicación de contacto moderna para Android. La app tendrá una pantalla principal (Home) con un carrusel de imágenes y un menú de navegación. Desde el Home, el usuario podrá acceder a un formulario de contacto completo donde ingresará su nombre, teléfono, correo electrónico, seleccionará una región de Chile desde un menú desplegable, y escribirá un mensaje de hasta 200 caracteres. Todos los datos serán validados en tiempo real usando expresiones regulares, y al guardar, se almacenarán localmente en una base de datos SQLite mediante Room. La aplicación usará arquitectura MVVM para separar la lógica de negocio de la interfaz, Jetpack Compose para crear una UI moderna y declarativa, Navigation Compose para la navegación entre pantallas, y animaciones suaves en las transiciones entre pantallas y botones para mejorar la experiencia del usuario.

### Características de la app:

- **Pantalla Home** con carrusel de imágenes y navegación
- **Formulario de Contacto completo** con 5 campos validados
- **Validaciones en tiempo real** usando expresiones regulares
- **Dropdown de regiones** cargadas desde archivo JSON
- **Contador de caracteres** para el campo de mensaje (máximo 200)
- **Base de datos local** con Room y SQLite
- **Navegación fluida** entre pantallas con Navigation Compose
- **Animaciones suaves** en botones y transiciones
- **Diseño responsivo** con Material3

---

## Setup Paso a Paso

### Paso 1: Crear proyecto en Android Studio

**En Windows:**

1. Abrir **Android Studio**
2. Click en **"New Project"**
3. Seleccionar **"Empty Activity"** (con Compose)
4. Configurar:
   - **Name:** TiendApp
   - **Package name:** duoc.desarrollomobile.tiendapp
   - **Save location:** `C:\Users\user\Desktop\TiendApp`
   - **Language:** Kotlin
   - **Minimum SDK:** API 30 (Android 11)
   - **Build configuration language:** Kotlin DSL (build.gradle.kts)
5. Click en **"Finish"**

Android Studio creará el proyecto y descargará las dependencias necesarias (puede tardar unos minutos).

---

### Paso 2: Verificar configuración inicial

#### A) Verificar build.gradle.kts (Module: app)

Abre el archivo `app/build.gradle.kts` y verifica que tenga estas configuraciones:

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "duoc.desarrollomobile.tiendapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "duoc.desarrollomobile.tiendapp"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
}
```

---

### Paso 3: Agregar dependencias necesarias

Necesitamos agregar las dependencias de Room, Navigation Compose, ViewModel y Coil para imágenes.

#### Editar app/build.gradle.kts

Abre `app/build.gradle.kts` y en la sección `plugins` **agrega** esta línea:

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp") version "2.0.21-1.0.25"  // Agregar esta línea
}
```

Luego, en la sección `dependencies` **agrega** estas líneas:

```kotlin
dependencies {
    // ... dependencias existentes ...

    // ViewModel para Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.4")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.9.4")

    // Material Icons Extended (para iconos)
    implementation("androidx.compose.material:material-icons-extended:1.7.7")

    // Room Database
    implementation("androidx.room:room-runtime:2.7.0-alpha12")
    implementation("androidx.room:room-ktx:2.7.0-alpha12")
    ksp("androidx.room:room-compiler:2.7.0-alpha12")

    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.9.0-alpha03")

    // Coil para cargar imágenes
    implementation("io.coil-kt:coil-compose:2.5.0")

    // Kotlinx Serialization para JSON
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
}
```

**¿Para qué sirve cada dependencia?**

| Dependencia | Propósito |
|-------------|-----------|
| `lifecycle-viewmodel-compose` | Integración de ViewModel con Compose |
| `lifecycle-runtime-compose` | Ciclo de vida para Compose |
| `material-icons-extended` | Iconos adicionales de Material Design |
| `room-runtime` | Core de Room Database |
| `room-ktx` | Extensiones de Kotlin para Room (Flow, Coroutines) |
| `room-compiler` | Procesador de anotaciones de Room (genera código) |
| `navigation-compose` | Sistema de navegación para Jetpack Compose |
| `coil-compose` | Librería para cargar imágenes desde URL o recursos |
| `kotlinx-serialization-json` | Para parsear archivos JSON |
| `ksp` | Kotlin Symbol Processing (reemplazo de kapt, más rápido) |

**Referencias de la documentación oficial:**
- [Room Release Notes](https://developer.android.com/jetpack/androidx/releases/room) - Versiones de Room
- [Navigation Compose Release Notes](https://developer.android.com/jetpack/androidx/releases/navigation) - Versiones de Navigation
- [Lifecycle Release Notes](https://developer.android.com/jetpack/androidx/releases/lifecycle) - Versiones de ViewModel
- [Coil Documentation](https://coil-kt.github.io/coil/compose/) - Carga de imágenes con Compose
- [KSP Documentation](https://kotlinlang.org/docs/ksp-overview.html) - Kotlin Symbol Processing

---

### Paso 4: Agregar plugin de serialización

Para poder usar `kotlinx-serialization-json` necesitamos agregar el plugin en el archivo raíz del proyecto.

#### Editar build.gradle.kts (Project level)

Abre el archivo `build.gradle.kts` del **nivel de proyecto** (no el de app) y **agrega** esta línea en la sección `plugins`:

```kotlin
plugins {
    // ... plugins existentes ...
    kotlin("plugin.serialization") version "2.1.0" apply false
}
```

Luego vuelve al archivo `app/build.gradle.kts` y en la sección `plugins` **agrega**:

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp") version "2.0.21-1.0.25"
    kotlin("plugin.serialization")  // Agregar esta línea
}
```

---

### Paso 5: Sincronizar proyecto con Gradle

**En Android Studio:**

1. Después de modificar `build.gradle.kts`, aparecerá un banner amarillo arriba
2. Click en **"Sync Now"**
3. O ve a `File` → `Sync Project with Gradle Files`
4. Espera a que termine la sincronización, verás la barra de progreso abajo

**Salida esperada en Build:**
```
BUILD SUCCESSFUL in 15s
```

---

### Paso 6: Crear estructura de carpetas (Arquitectura MVVM)

Ahora crearemos la estructura de paquetes para organizar el código según MVVM con Room.

**En Android Studio:**

1. Ve al panel izquierdo (Project)
2. Navega a `app/src/main/java/duoc/desarrollomobile/tiendapp/`
3. Click derecho en `tiendapp` → `New` → `Package`

Crea estos **6 paquetes** uno por uno:

```
tiendapp/
├── data           (Database, DAO, Entities de Room)
├── model          (Data classes, Repository)
├── viewmodel      (ViewModels con lógica de presentación)
├── view           (Pantallas con Jetpack Compose)
├── navigation     (Setup de navegación)
└── utils          (Utilidades y validaciones)
```

**Resultado final:**

```
app/src/main/java/duoc/desarrollomobile/tiendapp/
├── data/
├── model/
├── viewmodel/
├── view/
├── navigation/
├── utils/
├── ui/theme/
└── MainActivity.kt
```

---

### Paso 7: Crear carpeta assets para JSON

Necesitamos crear la carpeta `assets` para almacenar el archivo JSON de regiones.

**En Android Studio:**

1. Panel izquierdo → `app/src/main/`
2. Click derecho en `main` → `New` → `Directory`
3. Selecciona `assets` de las opciones sugeridas
4. Click **OK**

**Resultado:**
```
app/src/main/
├── java/
├── res/
└── assets/  ← Nueva carpeta
```

---

### Paso 8: Verificar que el proyecto funciona

Antes de empezar a programar, asegúrate de que el proyecto base funciona correctamente.

**Opciones:**
1. Ejecutar en emulador Android
2. Ejecutar en dispositivo físico
3. O simplemente verificar que no hay errores de compilación en Build

---

## Estructura del Proyecto (Antes de programar)

```
TiendApp/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/duoc/desarrollomobile/tiendapp/
│   │   │   │   ├── data/
│   │   │   │   ├── model/
│   │   │   │   ├── viewmodel/
│   │   │   │   ├── view/
│   │   │   │   ├── navigation/
│   │   │   │   ├── utils/
│   │   │   │   ├── ui/theme/
│   │   │   │   └── MainActivity.kt
│   │   │   ├── assets/
│   │   │   ├── res/
│   │   │   └── AndroidManifest.xml
│   │   └── res/
│   └── build.gradle.kts
├── gradle/
├── build.gradle.kts
└── settings.gradle.kts
```

---

## Conceptos Importantes

### ¿Qué es KSP?

**KSP** (Kotlin Symbol Processing) es el sucesor moderno de KAPT (Kotlin Annotation Processing Tool). KSP es hasta 2 veces más rápido que KAPT porque entiende la estructura de Kotlin nativamente.

**¿Por qué usamos KSP con Room?**
- Room genera código automáticamente basado en anotaciones
- KSP procesa estas anotaciones más rápido que KAPT
- Reduce tiempos de compilación significativamente

### ¿Qué es Navigation Compose?

**Navigation Compose** es el sistema de navegación oficial para Jetpack Compose. Permite navegar entre pantallas (composables) de forma declarativa.

**Características:**
- Type-safe navigation (navegación segura de tipos)
- Manejo automático del back stack
- Paso de argumentos entre pantallas
- Deep linking support
- Integración con ViewModel

### ¿Qué es Coil?

**Coil** (Coroutine Image Loader) es una librería moderna para cargar imágenes en Android usando corrutinas de Kotlin.

**Ventajas:**
- Optimizado para Kotlin y Coroutines
- Liviano (menos de 2000 métodos)
- Soporte nativo para Jetpack Compose
- Cache automático de imágenes
- Carga desde URL, recursos locales, assets, etc.

---

## Código Final: build.gradle.kts (Project level)

```kotlin
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    kotlin("plugin.serialization") version "2.1.0" apply false
}
```

---

## Código Final: build.gradle.kts (Module: app)

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp") version "2.0.21-1.0.25"
    kotlin("plugin.serialization") version "2.0.21"
}

android {
    namespace = "duoc.desarrollomobile.tiendapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "duoc.desarrollomobile.tiendapp"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // ViewModel para Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.4")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.9.4")

    // Material Icons Extended
    implementation("androidx.compose.material:material-icons-extended:1.7.7")

    // Room Database
    implementation("androidx.room:room-runtime:2.7.0-alpha12")
    implementation("androidx.room:room-ktx:2.7.0-alpha12")
    ksp("androidx.room:room-compiler:2.7.0-alpha12")

    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.9.0-alpha03")

    // Coil para cargar imágenes
    implementation("io.coil-kt:coil-compose:2.5.0")

    // Kotlinx Serialization para JSON
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
```

---
