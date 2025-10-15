# Capa Model: Datos y Lógica de Negocio (MVVM)

En este documento crearemos la **capa Model** de nuestra arquitectura MVVM. Esta capa contiene los modelos de datos, validaciones y repositorios.

---

## ¿Qué es la Capa Model?

La **capa Model** es responsable de:
- Definir las estructuras de datos (data classes)
- Manejar la lógica de negocio (validaciones, transformaciones)
- Acceder a fuentes de datos (API, base de datos local, etc.)

### Componentes que crearemos:

| Archivo | Tipo | Propósito |
|---------|------|-----------|
| `User.kt` | Data Class | Modelo de usuario |
| `LoginResult.kt` | Sealed Class | Estados del resultado de login |
| `ValidationUtils.kt` | Object | Validaciones de email y password |
| `LoginRepository.kt` | Class | Lógica de negocio del login |

---

## Paso 1: Crear el modelo User

El modelo `User` representa los datos de un usuario en nuestra app.

### ¿Dónde crear el archivo?

**En Android Studio:**

1. Panel izquierdo → `app/src/main/java/duoc/desarrollomobile/carrocompra/`
2. Click derecho en el paquete `model` - `New` - `Kotlin Class/File`
3. Nombre: `User`
4. Tipo: selecciona **"File"**, no Class
5. Click **OK**

### Código de User.kt

Copia este contenido en `model/User.kt`:

```kotlin
package duoc.desarrollomobile.carrocompra.model

/**
 * Data class que representa el modelo de usuario
 * @param email email del usuario
 * @param password contraseña del usuario
 */
data class User(
    val email: String = "",
    val password: String = ""
)

/**
 * Sealed class que representa los posibles resultados de la operación de login
 * Sealed class es como un enum avanzado que permite diferentes tipos con datos asociados
 * vamos a crear 4 sub clases y estas heredaran de LoginResult() con : LoginResult
 * delimitando solo estas 4 subclases puedan ser de tipo LoginResult()
 * esto lo hacemos para que el compilador evite que se creen estados inesperados
 */
sealed class LoginResult {
    // Success y Error contienen datos, por ende seran de tipo data class
    // Login exitoso con datos del usuario
    data class Success(val user: User) : LoginResult()

    // Login fallido con mensaje de error
    data class Error(val message: String) : LoginResult()

    // aqui no guardamos datos, por ende crearemos objects
    // Login en progreso o cargando 
    object Loading : LoginResult()

    // Estado inicial, o sin accion
    object Idle : LoginResult()
}
```

### ¿Qué es una data class?

**Data class** es una clase especial de Kotlin diseñada para almacenar datos. Kotlin genera automáticamente:
- `equals()` y `hashCode()`
- `toString()` para debugging
- `copy()` para crear copias modificadas


### ¿Qué es una sealed class?

**Sealed class** representa un conjunto cerrado de subtipos. Es perfecta para estados porque el compilador sabe todos los casos posibles.

**Beneficio:** Cuando usas `when` con sealed class, el compilador te obliga a manejar todos los casos.

```kotlin
when (result) {
    is LoginResult.Success -> // manejar éxito
    is LoginResult.Error -> // manejar error
    is LoginResult.Loading -> // mostrar loading
    is LoginResult.Idle -> // estado inicial
    // El compilador te avisará si falta un caso
}
```

---

## Paso 2: Crear las validaciones o ValidationUtils

Las validaciones son funciones que verifican si los datos son correctos antes de procesarlos.

### Crear el archivo

**En Android Studio:**

1. Click derecho en `utils` - `New` - `Kotlin Class/File`
2. Nombre: `ValidationUtils`
3. Tipo: **"File"**
4. Click **OK**

### Código de ValidationUtils.kt

```kotlin
package duoc.desarrollomobile.carrocompra.utils

/**
 * La palabra object automaticamente crea un singleton
 * https://kotlinlang.org/docs/object-declarations.html
 * ventaja fundamental, es thread safe
 * esto nos permite que en una sola instancia, podamos tener todas las funciones de validacion
 * asi no creamos multiples instancias de este objeto de validacion
 * En Kotlin, 'object' crea un singleton, en otras palabras, solo existe una instancia
 */
object ValidationUtils {

    /**
     * Valida que el email tenga un formato correcto
     * @param email Email a validar
     * @return true si el email es válido, false si no
     */
    fun isValidEmail(email: String): Boolean {
        // Verificar que no esté vacío
        if (email.isBlank()) return false

        /*
         * Descomposición del regex:
        * 
        * ^                      = Inicio de la cadena
        * [A-Za-z0-9+_.-]+       = Uno o más caracteres válidos antes del @ (letras, números, +, _, ., -)
        * @                      = Arroba literal
        * [A-Za-z0-9.-]+         = Uno o más caracteres para el dominio (letras, números, ., -)
        * \\.                    = Punto literal (escapado con \\)
        * [A-Za-z]{2,}           = Al menos 2 letras para la extensión (.com, .cl, etc.)
        * $                      = Fin de la cadena
        */

        // Regex para validar formato: usuario@dominio.extension
        // .toRegex() convierte el String en un objeto Regex que puede validar
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()

        // como ahora convertimos el emailRegex en un objeto, podemos validar la cadena email
        return emailRegex.matches(email)
    }

    /**
     * Valida que la contraseña tenga al menos 6 caracteres
     * @param password Contraseña a validar
     * @return true si la contraseña es válida, false si no
     */
    fun isValidPassword(password: String): Boolean {
        // el metodo length devuelve un int, pero al usar comparador, devuelve un booleano
        return password.length >= 6
    }

    /**
     * Obtiene el mensaje de error para email
     * @param email Email a validar
     * @return Mensaje de error o null si es válido
     */
    fun getEmailErrorMessage(email: String): String? {
        // recuerden que cuando devolvemos un when (switch case) es obligatorio agregar un else
        return when {
            // validamos si el email esta en blanco
            email.isBlank() -> "El email es requerido"

            // ahora validamos con isValidEmail() pero tenemos que usarlo con negacion
            // porque de lo contrario no entraria en el caso de error
            !isValidEmail(email) -> "Email inválido"
            else -> null
        }
    }

    /**
     * Obtiene el mensaje de error para contraseña
     * @param password Contraseña a validar
     * @return Mensaje de error o null si es válida
     */
    fun getPasswordErrorMessage(password: String): String? {
        return when {
            password.isBlank() -> "La contraseña es requerida"
            !isValidPassword(password) -> "La contraseña debe tener al menos 6 caracteres"
            else -> null
        }
    }
}
```

### Explicación de la regex de email

```kotlin
"^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
```

| Parte | Significado |
|-------|-------------|
| `^` | Inicio de la cadena |
| `[A-Za-z0-9+_.-]+` | Uno o más caracteres válidos antes del @ |
| `@` | Arroba literal |
| `[A-Za-z0-9.-]+` | Uno o más caracteres para el dominio |
| `\\.` | Punto literal (escapado) |
| `[A-Za-z]{2,}` | Al menos 2 letras para la extensión (.com, .cl, etc.) |
| `$` | Fin de la cadena |

**Ejemplos válidos:**
- `usuario@ejemplo.com`
- `test.user@dominio.cl`
- `admin+tag@empresa.com.ar`

**Ejemplos inválidos:**
- `usuario` (sin @)
- `usuario@dominio` (sin extensión)
- `usuario@` (sin dominio)

---

## Paso 3: Crear el repositorio de login

El **Repository** es el encargado de manejar la lógica de negocio y acceso a datos. En una app real, aquí se harían llamadas a una API REST

### Crear el archivo

**En Android Studio:**

1. Click derecho en `model` - `New` - `Kotlin Class/File`
2. Nombre: `LoginRepository`
3. Tipo: **"File"**
4. Click **OK**

### Código de LoginRepository.kt

```kotlin
package duoc.desarrollomobile.carrocompra.model

import kotlinx.coroutines.delay

/**
 * Repositorio que maneja la lógica de negocio del login
 *
 * En una aplicación real, este repositorio haría llamadas a una api rest
 * Para este ejemplo, usamos datos locales y simulamos un delay de red
 * tal cual como en la prueba 1
 */
class LoginRepository {

    /**
     * vamos a crear una lista de usuarios válidos para testing
     * En una app real, esto debe de una base de datos o api, pero esto es una prueba
     listOf usaremos listOf(vararg elements: T) para pasar cantidades variables de argumentos
     */
    private val validUsers = listOf(
        User(email = "admin@carrocompra.com", password = "123456"),
        User(email = "usuario@test.com", password = "password"),
        User(email = "demo@demo.com", password = "demo123")
    )

    /**
     * Intenta hacer login con las credenciales proporcionadas
     * Esta función es 'suspend' porque realiza una operación asíncrona
     * Simula una llamada a API con un delay de 1.5 segundos
     * ademas devuelve loginReturn
     * @param email Email del usuario
     * @param password Contraseña del usuario
     * @return LoginResult con el resultado de la operación
     */
    suspend fun login(email: String, password: String): LoginResult {
        return try {
            // Simulamos delay de red, como si fuera una llamada http request
            delay(1500)

            // buscamos el usuario en la lista de usuarios validos
            // para eso vamos a recorrer esta lista e it es la forma en la que la recorremos
            val user = validUsers.find {
                // comparamos el email e ignoreCase indica que no importa mayusculas y minusculas
                it.email.equals(email, ignoreCase = true) &&
                // y ademas && comparamos la contrasena exacta
                it.password == password
            }

            // retornamos el resultado segun si encontramos el usuario
            if (user != null) {
                LoginResult.Success(user)
            } else {
                LoginResult.Error("email o contraseña incorrectos")
            }
        } catch (e: Exception) {
            // en caso de error, retornamos un LoginResult.Error
            LoginResult.Error("Error al iniciar sesión: ${e.message}")
        }
    }
}
```

### ¿Qué es una suspend function?

**Suspend function** es una función que puede "pausarse" sin bloquear el hilo principal. Es como `async/await` en javascript

### Por que usar suspend?

En Android, todas las operaciones de red o base de datos **deben** ejecutarse en un hilo secundario para no bloquear la UI. Las suspend functions hacen esto facil y seguro

---

## Paso 4: Verificar la estructura

Después de crear todos los archivos, tu estructura debería verse así:

```
app/src/main/java/duoc/desarrollomobile/carrocompra/
├── model/
│   ├── User.kt
│   └── LoginRepository.kt 
├── utils/
│   └── ValidationUtils.kt 
├── viewmodel/     
├── view/  
└── ui/theme/
```

---

## Resumen de Conceptos Kotlin

### 1. Data Class vs Class normal

```kotlin
// Data class o para datos
data class User(val email: String, val password: String)

// Class normal o para logica
class LoginRepository {
    fun login() { /* lógica */ }
}
```

### 2. Object Singleton

```kotlin
// Solo existe una instancia de ValidationUtils
object ValidationUtils {
    fun isValidEmail(email: String): Boolean { ... }
}

// Uso:
ValidationUtils.isValidEmail("test@test.com")
```

### 3. Sealed Class o Estados cerrados

```kotlin
sealed class LoginResult {
    data class Success(val user: User) : LoginResult()
    data class Error(val message: String) : LoginResult()
    object Loading : LoginResult()
    object Idle : LoginResult()
}
```

### 4. Suspend Function u Operación asincrona

```kotlin
suspend fun login(email: String, password: String): LoginResult {
    // no bloquea la UI
    delay(1500) 
    return LoginResult.Success(user)
}
```

### 5. When Expression o como switch mejorado

```kotlin
when {
    email.isBlank() -> "El email es requerido"
    !isValidEmail(email) -> "Email inválido"
    else -> null
}
```




---

## Código Final

### User.kt

```kotlin
package duoc.desarrollomobile.carrocompra.model

data class User(
    val email: String = "",
    val password: String = ""
)

sealed class LoginResult {
    data class Success(val user: User) : LoginResult()
    data class Error(val message: String) : LoginResult()
    object Loading : LoginResult()
    object Idle : LoginResult()
}
```

### ValidationUtils.kt

```kotlin
package duoc.desarrollomobile.carrocompra.utils

object ValidationUtils {

    fun isValidEmail(email: String): Boolean {
        if (email.isBlank()) return false
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$".toRegex()
        return emailRegex.matches(email)
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    fun getEmailErrorMessage(email: String): String? {
        return when {
            email.isBlank() -> "El email es requerido"
            !isValidEmail(email) -> "Email inválido"
            else -> null
        }
    }

    fun getPasswordErrorMessage(password: String): String? {
        return when {
            password.isBlank() -> "La contraseña es requerida"
            !isValidPassword(password) -> "La contraseña debe tener al menos 6 caracteres"
            else -> null
        }
    }
}
```

### LoginRepository.kt

```kotlin
package duoc.desarrollomobile.carrocompra.model

import kotlinx.coroutines.delay

class LoginRepository {

    private val validUsers = listOf(
        User(email = "admin@carrocompra.com", password = "123456"),
        User(email = "usuario@test.com", password = "password"),
        User(email = "demo@demo.com", password = "demo123")
    )

    suspend fun login(email: String, password: String): LoginResult {
        return try {
            delay(1500)

            val user = validUsers.find {
                it.email.equals(email, ignoreCase = true) &&
                it.password == password
            }

            if (user != null) {
                LoginResult.Success(user)
            } else {
                LoginResult.Error("Email o contraseña incorrectos")
            }
        } catch (e: Exception) {
            LoginResult.Error("Error al iniciar sesión: ${e.message}")
        }
    }
}
```

---
