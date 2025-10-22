# Capa ViewModel: Lógica de Presentación y Estado (MVVM)

En este documento crearemos la **capa ViewModel** de nuestra arquitectura MVVM. Esta capa maneja el estado de la UI, la lógica de presentación y la comunicación entre la View y el Model.

---

## ¿Qué es la Capa ViewModel?

La **capa ViewModel** es responsable de:
- Mantener el estado de la UI (datos que se muestran en pantalla)
- Manejar la lógica de presentación (validaciones, transformaciones)
- Exponer datos al View de forma reactiva
- Sobrevivir a cambios de configuración (rotación de pantalla)
- Ejecutar operaciones asíncronas sin bloquear la UI

### Componentes que crearemos:

| Archivo | Ubicación | Propósito |
|---------|-----------|-----------|
| `HomeViewModel.kt` | viewmodel/ | Estado y lógica del Home (carrusel) |
| `ContactViewModel.kt` | viewmodel/ | Estado y lógica del Formulario de Contacto |

---

## ¿StateFlow vs LiveData?

Antes de empezar, es importante entender la diferencia entre StateFlow y LiveData.

| Característica | LiveData | StateFlow |
|---------------|----------|-----------|
| **Parte de** | Architecture Components | Kotlin Coroutines |
| **Lifecycle-aware** | Sí (automático) | Requiere collectAsState() |
| **Valor inicial** | Opcional | Obligatorio |
| **Thread-safe** | Sí | Sí |
| **Operadores** | Limitados (map, switchMap) | Todos los de Flow |
| **Jetpack Compose** | Soporte vía observeAsState() | Soporte nativo con collectAsState() |
| **Recomendado para** | Views XML | Jetpack Compose |

**En este tutorial usamos StateFlow** porque:
- Es la opción recomendada para Jetpack Compose
- Más poderoso y flexible que LiveData
- Integración perfecta con corrutinas de Kotlin

---

## ¿Qué es StateFlow?

**StateFlow** es un observable state-holder que emite el estado actual y todos los cambios posteriores a sus colectores.

**Características:**
- Siempre tiene un valor (nunca es null)
- Es hot stream (siempre activo, aunque no tenga observadores)
- Solo emite cuando el valor cambia
- Es thread-safe

**Ejemplo básico:**
```kotlin
// Crear StateFlow con valor inicial
private val _counter = MutableStateFlow(0)
val counter: StateFlow<Int> = _counter.asStateFlow()

// Actualizar valor
_counter.value = 1

// Observar en Compose
val count by viewModel.counter.collectAsState()
Text("Count: $count")
```

---

## ¿Por qué usar StateFlow privado y público?

Es una **best practice** exponer el estado como `StateFlow` inmutable y mantener una versión `MutableStateFlow` privada.

```kotlin
// PRIVADO: Solo el ViewModel puede modificarlo
private val _nombre = MutableStateFlow("")

// PÚBLICO: El View solo puede leerlo
val nombre: StateFlow<String> = _nombre.asStateFlow()
```

**Ventajas:**
- Encapsulación: el View no puede modificar el estado directamente
- Single Source of Truth: solo el ViewModel controla el estado
- Testability: fácil de testear sin side effects

---

## Paso 1: Crear HomeViewModel

El **HomeViewModel** maneja el estado de la pantalla Home, incluyendo el carrusel de imágenes.

### Crear el archivo

**En Android Studio:**

1. Panel izquierdo → `app/src/main/java/duoc/desarrollomobile/tiendapp/`
2. Click derecho en el paquete `viewmodel` → `New` → `Kotlin Class/File`
3. Nombre: `HomeViewModel`
4. Tipo: selecciona **"File"**, no Class
5. Click **OK**

### Código de HomeViewModel.kt

```kotlin
package duoc.desarrollomobile.tiendapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel para la pantalla Home
 *
 * ViewModel es una clase de Architecture Components que:
 * - Sobrevive a cambios de configuración (rotación de pantalla)
 * - Se destruye cuando la Activity/Fragment se destruye permanentemente
 * - Debe contener solo lógica de presentación, no referencias a Views
 */
class HomeViewModel : ViewModel() {

    /**
     * Lista de URLs de imágenes para el carrusel
     * En una app real, estas imágenes vendrían de una API
     */
    private val carouselImages = listOf(
        "https://picsum.photos/800/400?random=1",
        "https://picsum.photos/800/400?random=2"
    )

    /**
     * Paso 1: Necesitamos guardar qué imagen está mostrando el carrusel actualmente.
     *
     * Usamos MutableStateFlow para poder cambiar el índice cuando el usuario
     * navegue entre imágenes (siguiente/anterior). Lo inicializamos en 0 porque
     * queremos empezar mostrando la primera imagen.
     *
     * StateFlow es un contenedor observable - cuando cambia su valor, la UI
     * se actualiza automáticamente (similar a LiveData).
     *
     * Lo hacemos privado (_) para que solo el ViewModel pueda modificarlo.
     */
    private val _currentImageIndex = MutableStateFlow(0)

    /**
     * Paso 2: Exponemos el índice actual para que la UI pueda observarlo.
     *
     * Usamos asStateFlow() para convertirlo a solo lectura. Así la UI puede
     * VER el índice actual pero NO puede modificarlo directamente.
     * Solo el ViewModel controla cuándo cambia el índice (mediante nextImage(), etc).
     */
    val currentImageIndex: StateFlow<Int> = _currentImageIndex.asStateFlow()

    /**
     * Paso 3: La UI necesita la URL de la imagen actual, no solo el índice.
     *
     * Este StateFlow siempre contiene la URL de la primera imagen por ahora.
     * En la UI usamos getCarouselImages() para obtener todas las URLs.
     */
    val currentImageUrl: StateFlow<String> = MutableStateFlow(carouselImages[0])

    /**
     * La UI necesita acceder a todas las imágenes del carrusel.
     * Esta función retorna la lista completa de URLs.
     */
    fun getCarouselImages(): List<String> {
        return carouselImages
    }

    /**
     * Cuando el usuario swipea o después de 3 segundos, queremos mostrar
     * la siguiente imagen. Esta función incrementa el índice.
     *
     * El operador % hace que sea circular: después de la última imagen
     * vuelve a la primera (ej: si tenemos 2 imágenes, índice 2 se convierte en 0).
     */
    fun nextImage() {
        _currentImageIndex.value = (_currentImageIndex.value + 1) % carouselImages.size
    }

    /**
     * Si el usuario swipea hacia atrás, mostramos la imagen anterior.
     * Si está en la primera (índice 0), vamos a la última.
     */
    fun previousImage() {
        val newIndex = _currentImageIndex.value - 1
        _currentImageIndex.value = if (newIndex < 0) {
            carouselImages.size - 1  // Volver a la última imagen
        } else {
            newIndex
        }
    }

    /**
     * Si el usuario toca un indicador (dot) específico, saltamos a esa imagen.
     * Validamos que el índice esté dentro del rango válido.
     */
    fun goToImage(index: Int) {
        if (index in carouselImages.indices) {
            _currentImageIndex.value = index
        }
    }
}
```

### ¿Por qué extender ViewModel?

**ViewModel** es una clase de Architecture Components que proporciona:

1. **Lifecycle awareness:** Sobrevive a cambios de configuración
2. **Scope:** Se destruye automáticamente cuando ya no se necesita
3. **viewModelScope:** Coroutine scope que se cancela automáticamente

**Sin ViewModel:**
```kotlin
// Se pierde al rotar la pantalla
class HomeScreen {
    var currentIndex = 0  // Se resetea a 0 al rotar
}
```

**Con ViewModel:**
```kotlin
// Sobrevive a la rotación
class HomeViewModel : ViewModel() {
    private val _currentIndex = MutableStateFlow(0)  // Se mantiene al rotar
}
```

### ¿Qué es el operador módulo?

El **operador módulo** (%) retorna el residuo de una división.

**Uso en carrusel circular:**
```kotlin
// Si tenemos 2 imágenes (índices 0 y 1)
0 % 2 = 0  // Primera imagen
1 % 2 = 1  // Segunda imagen
2 % 2 = 0  // Vuelve a la primera (circular)
3 % 2 = 1  // Vuelve a la segunda
```

---

## Paso 2: Crear ContactViewModel

El **ContactViewModel** maneja todo el estado y la lógica del formulario de contacto.

### Crear el archivo

**En Android Studio:**

1. Click derecho en el paquete `viewmodel` → `New` → `Kotlin Class/File`
2. Nombre: `ContactViewModel`
3. Tipo: **"File"**
4. Click **OK**

### Código de ContactViewModel.kt

```kotlin
package duoc.desarrollomobile.tiendapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import duoc.desarrollomobile.tiendapp.data.AppDatabase
import duoc.desarrollomobile.tiendapp.data.Contact
import duoc.desarrollomobile.tiendapp.model.ContactRepository
import duoc.desarrollomobile.tiendapp.model.Region
import duoc.desarrollomobile.tiendapp.utils.ValidationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de Formulario de Contacto
 *
 * Maneja la lógica del formulario: validación, estados y guardado en SQLite.
 *
 * Extiende AndroidViewModel porque necesitamos Application context para:
 * - Inicializar Room Database (acceso a SQLite)
 * - Leer regiones.json desde assets
 *
 * @param application Application context
 */
class ContactViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * Repository centraliza acceso a datos (Room y JSON).
     * Separa lógica de presentación (ViewModel) de acceso a datos (Repository).
     */
    private val repository: ContactRepository

    init {
        val database = AppDatabase.getDatabase(application)
        val contactDao = database.contactDao()
        repository = ContactRepository(contactDao, application)
    }

    /**
     * Estados de los 5 campos del formulario.
     * StateFlow permite que la UI se actualice automáticamente al escribir.
     * Patrón: MutableStateFlow privado, StateFlow público (solo lectura para UI).
     */
    private val _nombre = MutableStateFlow("")
    val nombre: StateFlow<String> = _nombre.asStateFlow()

    private val _telefono = MutableStateFlow("")
    val telefono: StateFlow<String> = _telefono.asStateFlow()

    private val _correo = MutableStateFlow("")
    val correo: StateFlow<String> = _correo.asStateFlow()

    private val _regionSeleccionada = MutableStateFlow("")
    val regionSeleccionada: StateFlow<String> = _regionSeleccionada.asStateFlow()

    private val _mensaje = MutableStateFlow("")
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()

    /**
     * Estados de error: null si es válido, String con mensaje si es inválido.
     * La UI muestra estos mensajes debajo de cada campo en tiempo real.
     */
    private val _nombreError = MutableStateFlow<String?>(null)
    val nombreError: StateFlow<String?> = _nombreError.asStateFlow()

    private val _telefonoError = MutableStateFlow<String?>(null)
    val telefonoError: StateFlow<String?> = _telefonoError.asStateFlow()

    private val _correoError = MutableStateFlow<String?>(null)
    val correoError: StateFlow<String?> = _correoError.asStateFlow()

    private val _regionError = MutableStateFlow<String?>(null)
    val regionError: StateFlow<String?> = _regionError.asStateFlow()

    private val _mensajeError = MutableStateFlow<String?>(null)
    val mensajeError: StateFlow<String?> = _mensajeError.asStateFlow()

    /**
     * Regiones de Chile cargadas desde regiones.json de forma asíncrona.
     * Inicia vacía, se llena al crear el ViewModel. Usada por el dropdown.
     */
    private val _regiones = MutableStateFlow<List<Region>>(emptyList())
    val regiones: StateFlow<List<Region>> = _regiones.asStateFlow()

    /**
     * isLoading: muestra spinner mientras guarda en SQLite.
     * guardadoExitoso: muestra Snackbar de éxito y vuelve al Home.
     */
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _guardadoExitoso = MutableStateFlow(false)
    val guardadoExitoso: StateFlow<Boolean> = _guardadoExitoso.asStateFlow()

    /**
     * Cargamos las regiones inmediatamente para que el dropdown las tenga disponibles.
     */
    init {
        loadRegiones()
    }

    /**
     * Carga regiones desde JSON de forma asíncrona usando corrutinas.
     * viewModelScope.launch evita bloquear el hilo principal mientras lee el archivo.
     */
    private fun loadRegiones() {
        viewModelScope.launch {
            _regiones.value = repository.loadRegiones()
        }
    }

    /**
     * Actualiza el nombre y valida en tiempo real.
     * ValidationUtils retorna null si es válido, o mensaje de error si no.
     */
    fun onNombreChange(value: String) {
        _nombre.value = value
        _nombreError.value = ValidationUtils.getNombreErrorMessage(value)
    }

    /**
     * Actualiza el teléfono y valida formato chileno (+56912345678).
     */
    fun onTelefonoChange(value: String) {
        _telefono.value = value
        _telefonoError.value = ValidationUtils.getTelefonoErrorMessage(value)
    }

    /**
     * Actualiza el correo y valida formato email.
     */
    fun onCorreoChange(value: String) {
        _correo.value = value
        _correoError.value = ValidationUtils.getEmailErrorMessage(value)
    }

    /**
     * Actualiza la región seleccionada y valida que no esté vacía.
     */
    fun onRegionChange(value: String) {
        _regionSeleccionada.value = value
        _regionError.value = ValidationUtils.getRegionErrorMessage(value)
    }

    /**
     * Actualiza el mensaje solo si no excede 200 caracteres.
     * Esto previene que el usuario escriba más del límite.
     */
    fun onMensajeChange(value: String) {
        if (value.length <= 200) {
            _mensaje.value = value
            _mensajeError.value = ValidationUtils.getMensajeErrorMessage(value)
        }
    }

    /**
     * Retorna el contador en formato "X/200" para mostrar debajo del campo mensaje.
     */
    fun getMensajeCounter(): String {
        return "${_mensaje.value.length}/200"
    }

    /**
     * Valida todos los campos antes de guardar.
     * Retorna true si todos son válidos (errores = null), false si hay errores.
     */
    private fun validateForm(): Boolean {
        _nombreError.value = ValidationUtils.getNombreErrorMessage(_nombre.value)
        _telefonoError.value = ValidationUtils.getTelefonoErrorMessage(_telefono.value)
        _correoError.value = ValidationUtils.getEmailErrorMessage(_correo.value)
        _regionError.value = ValidationUtils.getRegionErrorMessage(_regionSeleccionada.value)
        _mensajeError.value = ValidationUtils.getMensajeErrorMessage(_mensaje.value)

        return _nombreError.value == null &&
                _telefonoError.value == null &&
                _correoError.value == null &&
                _regionError.value == null &&
                _mensajeError.value == null
    }

    /**
     * Guarda el contacto en SQLite.
     * Valida primero, muestra loading, guarda en Room, limpia formulario y notifica éxito.
     * try-catch-finally asegura que el loading se detenga siempre.
     */
    fun saveContact() {
        if (!validateForm()) {
            return
        }

        _isLoading.value = true

        viewModelScope.launch {
            try {
                val contact = Contact(
                    id = 0,
                    nombre = _nombre.value.trim(),
                    telefono = _telefono.value.trim(),
                    correo = _correo.value.trim(),
                    region = _regionSeleccionada.value,
                    mensaje = _mensaje.value.trim()
                )

                repository.insertContact(contact)
                _guardadoExitoso.value = true
                clearForm()

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Limpia todos los campos y errores del formulario.
     */
    private fun clearForm() {
        _nombre.value = ""
        _telefono.value = ""
        _correo.value = ""
        _regionSeleccionada.value = ""
        _mensaje.value = ""

        _nombreError.value = null
        _telefonoError.value = null
        _correoError.value = null
        _regionError.value = null
        _mensajeError.value = null
    }

    /**
     * Resetea el estado de guardado exitoso después de mostrar el Snackbar.
     */
    fun resetGuardadoExitoso() {
        _guardadoExitoso.value = false
    }
}
```

### ¿ViewModel vs AndroidViewModel?

| Clase | Cuándo usar |
|-------|-------------|
| **ViewModel** | Cuando no necesitas contexto de Android |
| **AndroidViewModel** | Cuando necesitas Application context (Room, SharedPreferences, Resources) |

**Importante:** Nunca guardes Activity o View context en el ViewModel (memory leaks). Solo Application context es seguro.

```kotlin
// INCORRECTO - Memory leak
class MyViewModel(private val activity: Activity) : ViewModel()

// CORRECTO - Application context es seguro
class MyViewModel(application: Application) : AndroidViewModel(application)
```

### ¿Qué es viewModelScope?

**viewModelScope** es un CoroutineScope vinculado al ciclo de vida del ViewModel.

**Características:**
- Se cancela automáticamente cuando el ViewModel se destruye
- Usa Dispatchers.Main por defecto
- Perfecto para operaciones asíncronas en el ViewModel

**Ejemplo:**
```kotlin
viewModelScope.launch {
    // Esta corrutina se cancela automáticamente si el ViewModel se destruye
    val data = repository.getData()  // Operación asíncrona
    _state.value = data
}
```

### ¿Por qué validar en tiempo real?

La **validación en tiempo real** mejora la experiencia de usuario al dar feedback inmediato.

**Ventajas:**
- Usuario sabe inmediatamente si hay errores
- Reduce frustración al enviar el formulario
- Ayuda a corregir errores mientras escribe

**Implementación:**
```kotlin
fun onNombreChange(value: String) {
    _nombre.value = value
    // Valida inmediatamente después de cada cambio
    _nombreError.value = ValidationUtils.getNombreErrorMessage(value)
}
```

### ¿Por qué usar try-catch-finally en saveContact?

El bloque **try-catch-finally** garantiza que siempre detenemos el loading, incluso si hay error.

```kotlin
_isLoading.value = true

try {
    // Operación que podría fallar
    repository.insertContact(contact)
} catch (e: Exception) {
    // Manejar error
    e.printStackTrace()
} finally {
    // SIEMPRE se ejecuta (éxito o error)
    _isLoading.value = false
}
```

**Sin finally:**
```kotlin
_isLoading.value = true
repository.insertContact(contact)  // Si falla, loading nunca se detiene
_isLoading.value = false  // Esta línea no se ejecuta si hay error
```

---

## Paso 3: Verificar la estructura

Después de crear los ViewModels, tu estructura debería verse así:

```
app/src/main/java/duoc/desarrollomobile/tiendapp/
├── data/
│   ├── Contact.kt
│   ├── ContactDao.kt
│   └── AppDatabase.kt
├── model/
│   ├── Region.kt
│   └── ContactRepository.kt
├── viewmodel/
│   ├── HomeViewModel.kt ✓
│   └── ContactViewModel.kt ✓
├── utils/
│   └── ValidationUtils.kt
├── view/
├── navigation/
└── ui/theme/
```

---

## Importante: Observar StateFlow en Compose

### collectAsState vs collectAsStateWithLifecycle

Cuando observamos StateFlow en Jetpack Compose, hay dos funciones disponibles:

| Función | Lifecycle-aware | Ahorra recursos | Uso recomendado |
|---------|----------------|-----------------|------------------|
| `collectAsState()` | No | No | Solo casos especiales |
| `collectAsStateWithLifecycle()` | Sí | Sí | **Recomendado en producción** |

### ¿Por qué usar collectAsStateWithLifecycle?

**collectAsStateWithLifecycle** es la forma recomendada oficialmente por Google para observar Flows en Compose porque:

1. **Lifecycle-aware:** Detiene automáticamente la colección cuando la app va a background
2. **Ahorra recursos:** No desperdicia batería ni CPU en segundo plano
3. **Previene memory leaks:** Se integra correctamente con el ciclo de vida
4. **Es el estándar 2025:** Usado en el sample oficial "Now in Android" de Google

**Ejemplo de uso correcto:**

```kotlin
@Composable
fun MyScreen(viewModel: MyViewModel) {
    // CORRECTO: Lifecycle-aware, se detiene en background
    val nombre by viewModel.nombre.collectAsStateWithLifecycle()

    Text(nombre)
}
```

**Ejemplo de uso incorrecto:**

```kotlin
@Composable
fun MyScreen(viewModel: MyViewModel) {
    // INCORRECTO: Sigue colectando en background, desperdicia recursos
    val nombre by viewModel.nombre.collectAsState()

    Text(nombre)
}
```

### ¿Cuándo usar collectAsState?

Usa `collectAsState()` solo en casos muy específicos donde **necesites** que la colección continúe en background, lo cual es raro en apps de producción.

### Referencia oficial

Según la documentación oficial de Android Developers:
- [Consuming flows safely in Jetpack Compose](https://medium.com/androiddevelopers/consuming-flows-safely-in-jetpack-compose-cde014d0d5a3)
- [A safer way to collect flows from Android UIs](https://medium.com/androiddevelopers/a-safer-way-to-collect-flows-from-android-uis-23080b1f8bda)

**Nota:** En el archivo `04-view-layer.md` verás ejemplos de uso de `collectAsStateWithLifecycle()` en todas las pantallas.

---

## Resumen de Conceptos

### 1. StateFlow básico

```kotlin
// Privado mutable
private val _nombre = MutableStateFlow("")

// Público inmutable
val nombre: StateFlow<String> = _nombre.asStateFlow()

// Actualizar
_nombre.value = "Juan"

// Observar en Compose
val name by viewModel.nombre.collectAsState()
```

### 2. ViewModel lifecycle-aware

```kotlin
class MyViewModel : ViewModel() {
    // Se mantiene durante rotaciones de pantalla
    private val _data = MutableStateFlow("")

    // Se limpia automáticamente cuando ya no se necesita
    override fun onCleared() {
        super.onCleared()
        // Opcional: limpieza manual
    }
}
```

### 3. viewModelScope para corrutinas

```kotlin
fun saveData() {
    viewModelScope.launch {
        // Operación asíncrona
        repository.save(data)
    }
    // Se cancela automáticamente si el ViewModel se destruye
}
```

### 4. Validación en tiempo real

```kotlin
fun onFieldChange(value: String) {
    _field.value = value
    _fieldError.value = ValidationUtils.validate(value)
}
```

### 5. Estados de UI

```kotlin
private val _isLoading = MutableStateFlow(false)
val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

fun load() {
    _isLoading.value = true
    viewModelScope.launch {
        try {
            // Operación
        } finally {
            _isLoading.value = false
        }
    }
}
```

---

## Código Final

### HomeViewModel.kt

```kotlin
package duoc.desarrollomobile.tiendapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {

    private val carouselImages = listOf(
        "https://picsum.photos/800/400?random=1",
        "https://picsum.photos/800/400?random=2"
    )

    private val _currentImageIndex = MutableStateFlow(0)
    val currentImageIndex: StateFlow<Int> = _currentImageIndex.asStateFlow()

    val currentImageUrl: StateFlow<String> = MutableStateFlow(carouselImages[0])

    fun getCarouselImages(): List<String> {
        return carouselImages
    }

    fun nextImage() {
        _currentImageIndex.value = (_currentImageIndex.value + 1) % carouselImages.size
    }

    fun previousImage() {
        val newIndex = _currentImageIndex.value - 1
        _currentImageIndex.value = if (newIndex < 0) {
            carouselImages.size - 1
        } else {
            newIndex
        }
    }

    fun goToImage(index: Int) {
        if (index in carouselImages.indices) {
            _currentImageIndex.value = index
        }
    }
}
```

### ContactViewModel.kt

```kotlin
package duoc.desarrollomobile.tiendapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import duoc.desarrollomobile.tiendapp.data.AppDatabase
import duoc.desarrollomobile.tiendapp.data.Contact
import duoc.desarrollomobile.tiendapp.model.ContactRepository
import duoc.desarrollomobile.tiendapp.model.Region
import duoc.desarrollomobile.tiendapp.utils.ValidationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ContactViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ContactRepository

    init {
        val database = AppDatabase.getDatabase(application)
        val contactDao = database.contactDao()
        repository = ContactRepository(contactDao, application)
    }

    private val _nombre = MutableStateFlow("")
    val nombre: StateFlow<String> = _nombre.asStateFlow()

    private val _telefono = MutableStateFlow("")
    val telefono: StateFlow<String> = _telefono.asStateFlow()

    private val _correo = MutableStateFlow("")
    val correo: StateFlow<String> = _correo.asStateFlow()

    private val _regionSeleccionada = MutableStateFlow("")
    val regionSeleccionada: StateFlow<String> = _regionSeleccionada.asStateFlow()

    private val _mensaje = MutableStateFlow("")
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()

    private val _nombreError = MutableStateFlow<String?>(null)
    val nombreError: StateFlow<String?> = _nombreError.asStateFlow()

    private val _telefonoError = MutableStateFlow<String?>(null)
    val telefonoError: StateFlow<String?> = _telefonoError.asStateFlow()

    private val _correoError = MutableStateFlow<String?>(null)
    val correoError: StateFlow<String?> = _correoError.asStateFlow()

    private val _regionError = MutableStateFlow<String?>(null)
    val regionError: StateFlow<String?> = _regionError.asStateFlow()

    private val _mensajeError = MutableStateFlow<String?>(null)
    val mensajeError: StateFlow<String?> = _mensajeError.asStateFlow()

    private val _regiones = MutableStateFlow<List<Region>>(emptyList())
    val regiones: StateFlow<List<Region>> = _regiones.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _guardadoExitoso = MutableStateFlow(false)
    val guardadoExitoso: StateFlow<Boolean> = _guardadoExitoso.asStateFlow()

    init {
        loadRegiones()
    }

    private fun loadRegiones() {
        viewModelScope.launch {
            _regiones.value = repository.loadRegiones()
        }
    }

    fun onNombreChange(value: String) {
        _nombre.value = value
        _nombreError.value = ValidationUtils.getNombreErrorMessage(value)
    }

    fun onTelefonoChange(value: String) {
        _telefono.value = value
        _telefonoError.value = ValidationUtils.getTelefonoErrorMessage(value)
    }

    fun onCorreoChange(value: String) {
        _correo.value = value
        _correoError.value = ValidationUtils.getEmailErrorMessage(value)
    }

    fun onRegionChange(value: String) {
        _regionSeleccionada.value = value
        _regionError.value = ValidationUtils.getRegionErrorMessage(value)
    }

    fun onMensajeChange(value: String) {
        if (value.length <= 200) {
            _mensaje.value = value
            _mensajeError.value = ValidationUtils.getMensajeErrorMessage(value)
        }
    }

    fun getMensajeCounter(): String {
        return "${_mensaje.value.length}/200"
    }

    private fun validateForm(): Boolean {
        _nombreError.value = ValidationUtils.getNombreErrorMessage(_nombre.value)
        _telefonoError.value = ValidationUtils.getTelefonoErrorMessage(_telefono.value)
        _correoError.value = ValidationUtils.getEmailErrorMessage(_correo.value)
        _regionError.value = ValidationUtils.getRegionErrorMessage(_regionSeleccionada.value)
        _mensajeError.value = ValidationUtils.getMensajeErrorMessage(_mensaje.value)

        return _nombreError.value == null &&
                _telefonoError.value == null &&
                _correoError.value == null &&
                _regionError.value == null &&
                _mensajeError.value == null
    }

    fun saveContact() {
        if (!validateForm()) {
            return
        }

        _isLoading.value = true

        viewModelScope.launch {
            try {
                val contact = Contact(
                    id = 0,
                    nombre = _nombre.value.trim(),
                    telefono = _telefono.value.trim(),
                    correo = _correo.value.trim(),
                    region = _regionSeleccionada.value,
                    mensaje = _mensaje.value.trim()
                )

                repository.insertContact(contact)
                _guardadoExitoso.value = true
                clearForm()

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun clearForm() {
        _nombre.value = ""
        _telefono.value = ""
        _correo.value = ""
        _regionSeleccionada.value = ""
        _mensaje.value = ""
        _nombreError.value = null
        _telefonoError.value = null
        _correoError.value = null
        _regionError.value = null
        _mensajeError.value = null
    }

    fun resetGuardadoExitoso() {
        _guardadoExitoso.value = false
    }
}
```

---
