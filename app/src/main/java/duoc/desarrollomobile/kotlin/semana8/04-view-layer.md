# Capa View: Interfaz de Usuario con Jetpack Compose (MVVM)

En este documento crearemos la **capa View** de nuestra arquitectura MVVM. Esta capa contiene las pantallas (Composables), la navegación y la MainActivity.

---

## ¿Qué es la Capa View?

La **capa View** es responsable de:
- Mostrar la interfaz de usuario
- Observar el estado del ViewModel
- Reaccionar a interacciones del usuario
- Enviar eventos al ViewModel
- NO contiene lógica de negocio

### Componentes que crearemos:

| Archivo | Ubicación | Propósito |
|---------|-----------|-----------|
| `HomeScreen.kt` | view/ | Pantalla principal con carrusel |
| `ContactFormScreen.kt` | view/ | Formulario de contacto |
| `AppNavigation.kt` | navigation/ | Setup de navegación y rutas |
| `MainActivity.kt` | raíz | Activity principal |

**Nota importante:** Crearemos primero las pantallas (HomeScreen y ContactFormScreen) antes de crear AppNavigation, para evitar errores de compilación al intentar importar pantallas que aún no existen.

---

## ¿Qué es un Composable?

Un **Composable** es una función que define parte de la interfaz de usuario en Jetpack Compose.

**Características:**
- Anotada con `@Composable`
- Declarativa (describes qué mostrar, no cómo)
- Se recompone automáticamente cuando el estado cambia
- No retorna nada (void/Unit)

**Ejemplo:**
```kotlin
@Composable
fun Greeting(name: String) {
    Text("Hola $name")
}
```

---

## Paso 1: Crear HomeScreen

La **HomeScreen** muestra el carrusel de imágenes y el botón para ir al formulario.

### Crear el archivo

**En Android Studio:**

1. Click derecho en el paquete `view` → `New` → `Kotlin Class/File`
2. Nombre: `HomeScreen`
3. Tipo: **"File"**
4. Click **OK**

### Código de HomeScreen.kt

```kotlin
package duoc.desarrollomobile.tiendapp.view

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import duoc.desarrollomobile.tiendapp.viewmodel.HomeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Pantalla principal (Home) con navbar, carrusel y botón al formulario.
 *
 * @param homeViewModel ViewModel con la lógica del Home
 * @param onNavigateToContact Callback para navegar al formulario
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    onNavigateToContact: () -> Unit
) {
    /**
     * Scaffold proporciona estructura Material Design: topBar, contenido, bottomBar, etc.
     */
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    // Logo como texto
                    Text(
                        text = "TiendApp",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        /**
         * Column organiza elementos verticalmente.
         */
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            /**
             * Spacer separa elementos verticalmente.
             */
            Spacer(modifier = Modifier.height(16.dp))

            /**
             * Texto de bienvenida.
             */
            Text(
                text = "Bienvenido a TiendApp",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(24.dp))

            /**
             * Carrusel de imágenes con auto-scroll.
             */
            ImageCarousel(homeViewModel)

            Spacer(modifier = Modifier.height(24.dp))

            /**
             * Botón animado navega al formulario.
             */
            AnimatedContactButton(onNavigateToContact)

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

/**
 * Carrusel de imágenes con HorizontalPager, swipe y auto-scroll cada 3 segundos.
 *
 * @param homeViewModel ViewModel con las imágenes
 */
@Composable
fun ImageCarousel(homeViewModel: HomeViewModel) {
    val images = homeViewModel.getCarouselImages()

    /**
     * PagerState maneja página actual, scroll, etc. pageCount es total de páginas.
     */
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { images.size }
    )

    /**
     * LaunchedEffect ejecuta auto-scroll. Key Unit = se ejecuta una vez al crear.
     */
    LaunchedEffect(Unit) {
        // Loop infinito para auto-scroll
        while (true) {
            delay(3000)  // Esperar 3 segundos
            val nextPage = (pagerState.currentPage + 1) % images.size  // Circular
            pagerState.animateScrollToPage(nextPage)
        }
    }

    /**
     * Card da estilo y elevación al carrusel.
     */
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        /**
         * HorizontalPager muestra una página a la vez, permite swipe.
         */
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            /**
             * Contenido de cada página.
             */
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                /**
                 * Cargar imagen desde URL con Coil de forma asíncrona.
                 */
                Image(
                    painter = rememberAsyncImagePainter(images[page]),
                    contentDescription = "Imagen ${page + 1}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop  // Recorta para llenar
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    /**
     * Indicadores de página (dots).
     */
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(images.size) { index ->
            /**
             * Dot representa una página. Página actual es más grande y con color diferente.
             */
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(if (pagerState.currentPage == index) 12.dp else 8.dp)
                    .background(
                        color = if (pagerState.currentPage == index)
                            MaterialTheme.colorScheme.primary
                        else
                            Color.Gray,
                        shape = MaterialTheme.shapes.small
                    )
            )
        }
    }
}

/**
 * Botón animado con efecto scale al presionar para navegar al formulario.
 *
 * @param onClick Callback al presionar
 */
@Composable
fun AnimatedContactButton(onClick: () -> Unit) {
    /**
     * Estado controla si el botón está presionado.
     */
    var isPressed by remember { mutableStateOf(false) }

    /**
     * Animación scale: isPressed true = 0.95 (pequeño), false = 1.0 (normal).
     */
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1.0f,
        animationSpec = tween(durationMillis = 100),
        label = "button_scale"
    )

    /**
     * Button con animación scale aplicada.
     */
    Button(
        onClick = {
            isPressed = true
            onClick()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .height(56.dp)
            .scale(scale),  // Aplicar animación
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(
            text = "Ir a Formulario de Contacto",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }

    /**
     * Resetear isPressed después de la animación.
     */
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(100)
            isPressed = false
        }
    }
}
```

### ¿Qué es HorizontalPager?

**HorizontalPager** es un componente que muestra contenido en páginas horizontales con swipe.

**Características:**
- Swipe gesture nativo
- Animaciones suaves
- Estado persistente entre páginas
- Soporte para infinite scroll

### ¿Qué hace LaunchedEffect?

**LaunchedEffect** ejecuta una corrutina cuando el Composable entra en la composición.

**Uso común:**
- Llamadas a APIs
- Animaciones automáticas
- Operaciones asíncronas

```kotlin
LaunchedEffect(key) {
    // Esta corrutina se cancela cuando el Composable sale de la composición
    delay(1000)
    doSomething()
}
```

### ¿Qué es animateFloatAsState?

**animateFloatAsState** anima un valor Float cuando cambia.

**Ejemplo:**
```kotlin
var enabled by remember { mutableStateOf(false) }
val alpha by animateFloatAsState(if (enabled) 1f else 0.5f)

Box(modifier = Modifier.alpha(alpha))
```

---

## Paso 2: Crear ContactFormScreen

La **ContactFormScreen** muestra el formulario completo con validaciones.

### Crear el archivo

**En Android Studio:**

1. Click derecho en el paquete `view` → `New` → `Kotlin Class/File`
2. Nombre: `ContactFormScreen`
3. Tipo: **"File"**
4. Click **OK**

### Código de ContactFormScreen.kt

```kotlin
package duoc.desarrollomobile.tiendapp.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import duoc.desarrollomobile.tiendapp.viewmodel.ContactViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * Formulario de contacto con 5 campos, validaciones en tiempo real, dropdown regiones y botón con loading.
 *
 * @param contactViewModel ViewModel con la lógica del formulario
 * @param onNavigateBack Callback para volver al Home
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactFormScreen(
    contactViewModel: ContactViewModel,
    onNavigateBack: () -> Unit
) {
    /**
     * Observar estados del ViewModel. collectAsStateWithLifecycle convierte StateFlow en State lifecycle-aware (detiene en background).
     */
    val nombre by contactViewModel.nombre.collectAsStateWithLifecycle()
    val telefono by contactViewModel.telefono.collectAsStateWithLifecycle()
    val correo by contactViewModel.correo.collectAsStateWithLifecycle()
    val regionSeleccionada by contactViewModel.regionSeleccionada.collectAsStateWithLifecycle()
    val mensaje by contactViewModel.mensaje.collectAsStateWithLifecycle()

    val nombreError by contactViewModel.nombreError.collectAsStateWithLifecycle()
    val telefonoError by contactViewModel.telefonoError.collectAsStateWithLifecycle()
    val correoError by contactViewModel.correoError.collectAsStateWithLifecycle()
    val regionError by contactViewModel.regionError.collectAsStateWithLifecycle()
    val mensajeError by contactViewModel.mensajeError.collectAsStateWithLifecycle()

    val regiones by contactViewModel.regiones.collectAsStateWithLifecycle()
    val isLoading by contactViewModel.isLoading.collectAsStateWithLifecycle()
    val guardadoExitoso by contactViewModel.guardadoExitoso.collectAsStateWithLifecycle()

    /**
     * SnackbarHostState muestra mensajes tipo toast.
     */
    val snackbarHostState = remember { SnackbarHostState() }

    /**
     * Scope para corrutinas.
     */
    val scope = rememberCoroutineScope()

    /**
     * Muestra Snackbar al guardar exitosamente, resetea flag y vuelve al Home.
     */
    LaunchedEffect(guardadoExitoso) {
        if (guardadoExitoso) {
            snackbarHostState.showSnackbar(
                message = "Contacto guardado exitosamente",
                duration = SnackbarDuration.Short
            )
            contactViewModel.resetGuardadoExitoso()
            kotlinx.coroutines.delay(1000)  // Esperar 1 segundo
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Formulario de Contacto") },
                navigationIcon = {
                    /**
                     * Botón back en navbar.
                     */
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        /**
         * Column con scroll vertical para el formulario.
         */
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            /**
             * Título del formulario.
             */
            Text(
                text = "Complete sus datos",
                style = MaterialTheme.typography.headlineSmall
            )

            /**
             * Campo Nombre.
             */
            OutlinedTextField(
                value = nombre,
                onValueChange = { contactViewModel.onNombreChange(it) },
                label = { Text("Nombre") },
                placeholder = { Text("Ingrese su nombre completo") },
                isError = nombreError != null,
                supportingText = {
                    nombreError?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            /**
             * Campo Teléfono.
             */
            OutlinedTextField(
                value = telefono,
                onValueChange = { contactViewModel.onTelefonoChange(it) },
                label = { Text("Teléfono") },
                placeholder = { Text("+56912345678") },
                isError = telefonoError != null,
                supportingText = {
                    telefonoError?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            /**
             * Campo Email.
             */
            OutlinedTextField(
                value = correo,
                onValueChange = { contactViewModel.onCorreoChange(it) },
                label = { Text("Correo Electrónico") },
                placeholder = { Text("ejemplo@correo.com") },
                isError = correoError != null,
                supportingText = {
                    correoError?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            /**
             * Dropdown de Regiones.
             */
            RegionDropdown(
                regiones = regiones,
                selectedRegion = regionSeleccionada,
                onRegionSelected = { contactViewModel.onRegionChange(it) },
                error = regionError
            )

            /**
             * Campo Mensaje (TextArea con contador).
             */
            OutlinedTextField(
                value = mensaje,
                onValueChange = { contactViewModel.onMensajeChange(it) },
                label = { Text("Mensaje") },
                placeholder = { Text("Escriba su mensaje aquí...") },
                isError = mensajeError != null,
                supportingText = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        /**
                         * Mensaje de error o vacío.
                         */
                        Text(
                            text = mensajeError ?: "",
                            color = MaterialTheme.colorScheme.error
                        )
                        /**
                         * Contador de caracteres.
                         */
                        Text(
                            text = contactViewModel.getMensajeCounter(),
                            color = if (mensaje.length > 180)
                                MaterialTheme.colorScheme.error
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                maxLines = 6
            )

            Spacer(modifier = Modifier.height(8.dp))

            /**
             * Botón Guardar con loading.
             */
            Button(
                onClick = { contactViewModel.saveContact() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    /**
                     * Loading indicator.
                     */
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Guardar Contacto", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

/**
 * Dropdown de regiones con ExposedDropdownMenuBox. Regiones cargadas desde JSON.
 *
 * @param regiones Lista de regiones disponibles
 * @param selectedRegion Región seleccionada
 * @param onRegionSelected Callback al seleccionar
 * @param error Mensaje de error o null
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegionDropdown(
    regiones: List<duoc.desarrollomobile.tiendapp.model.Region>,
    selectedRegion: String,
    onRegionSelected: (String) -> Unit,
    error: String?
) {
    /**
     * Estado controla si dropdown está expandido.
     */
    var expanded by remember { mutableStateOf(false) }

    /**
     * ExposedDropdownMenuBox crea TextField con menú desplegable.
     */
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        /**
         * TextField muestra región seleccionada. menuAnchor conecta con dropdown. Read-only.
         */
        OutlinedTextField(
            value = selectedRegion,
            onValueChange = {},  // Read-only
            readOnly = true,
            label = { Text("Región") },
            placeholder = { Text("Seleccione una región") },
            trailingIcon = {
                /**
                 * Icono dropdown.
                 */
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            isError = error != null,
            supportingText = {
                error?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),  // Conecta con dropdown
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
        )

        /**
         * Menú dropdown con opciones.
         */
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            /**
             * Item por cada región.
             */
            regiones.forEach { region ->
                DropdownMenuItem(
                    text = { Text(region.nombre) },
                    onClick = {
                        onRegionSelected(region.nombre)
                        expanded = false
                    }
                )
            }
        }
    }
}
```

### ¿Qué es collectAsStateWithLifecycle?

**collectAsStateWithLifecycle()** convierte un Flow en un State observable por Compose de forma lifecycle-aware.

```kotlin
// StateFlow en ViewModel
val nombre: StateFlow<String> = _nombre.asStateFlow()

// Observar en Compose (forma recomendada 2025)
val nombre by viewModel.nombre.collectAsStateWithLifecycle()
```

**Ventajas:**
- El Composable se recompone automáticamente cuando el valor cambia
- Detiene la colección cuando la app va a background (ahorra batería y CPU)
- Es la forma recomendada oficialmente por Google

**Nota:** En el archivo `03-viewmodel-layer.md` se explica en detalle por qué usar `collectAsStateWithLifecycle()` en lugar de `collectAsState()`.

### ¿Qué es OutlinedTextField?

**OutlinedTextField** es un campo de texto con borde outline de Material Design.

**Propiedades importantes:**
- `value`: texto actual
- `onValueChange`: callback cuando cambia el texto
- `label`: etiqueta flotante
- `isError`: muestra estilo de error
- `supportingText`: texto de ayuda o error
- `keyboardOptions`: configuración del teclado

### ¿Qué es ExposedDropdownMenuBox?

**ExposedDropdownMenuBox** crea un TextField con menú desplegable.

**Componentes:**
1. `ExposedDropdownMenuBox` - Contenedor
2. `OutlinedTextField` con `menuAnchor()` - Campo de texto
3. `ExposedDropdownMenu` - Menú desplegable
4. `DropdownMenuItem` - Cada opción

---

## Paso 3: Crear AppNavigation

La **navegación** define las rutas y cómo navegar entre pantallas.

**Nota:** Este paso lo hacemos después de crear las pantallas para evitar errores de compilación al intentar importar pantallas que aún no existen.

### Crear el archivo

**En Android Studio:**

1. Panel izquierdo → `app/src/main/java/duoc/desarrollomobile/tiendapp/`
2. Click derecho en el paquete `navigation` → `New` → `Kotlin Class/File`
3. Nombre: `AppNavigation`
4. Tipo: selecciona **"File"**, no Class
5. Click **OK**

**Nota:** El paquete `navigation` ya fue creado en el archivo 01-setup-project.md.

### Código de AppNavigation.kt

```kotlin
package duoc.desarrollomobile.tiendapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import duoc.desarrollomobile.tiendapp.view.ContactFormScreen
import duoc.desarrollomobile.tiendapp.view.HomeScreen
import duoc.desarrollomobile.tiendapp.viewmodel.ContactViewModel
import duoc.desarrollomobile.tiendapp.viewmodel.HomeViewModel

/**
 * Rutas de navegación. Sealed class garantiza validación exhaustiva en when del compilador.
 */
sealed class Screen(val route: String) {
    /**
     * Pantalla Home.
     */
    object Home : Screen("home")

    /**
     * Pantalla Formulario de Contacto.
     */
    object ContactForm : Screen("contact_form")
}

/**
 * Navegación principal. NavHost maneja navegación entre pantallas. Cada composable define pantalla y ruta.
 *
 * @param navController Controlador de navegación
 */
@Composable
fun AppNavigation(navController: NavHostController) {
    /**
     * NavHost define grafo de navegación. startDestination es pantalla inicial.
     */
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        /**
         * Pantalla Home. route = identificador único, content = Composable a mostrar.
         */
        composable(route = Screen.Home.route) {
            val homeViewModel: HomeViewModel = viewModel()

            HomeScreen(
                homeViewModel = homeViewModel,
                onNavigateToContact = {
                    navController.navigate(Screen.ContactForm.route)
                }
            )
        }

        /**
         * Pantalla Formulario. ContactViewModel necesita Application (AndroidViewModel), usa AndroidViewModelFactory.
         */
        composable(route = Screen.ContactForm.route) {
            val context = LocalContext.current.applicationContext as android.app.Application

            val contactViewModel: ContactViewModel = viewModel(
                factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(context)
            )

            ContactFormScreen(
                contactViewModel = contactViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
```

### ¿Qué es NavHost?

**NavHost** es el contenedor principal de navegación en Jetpack Compose.

**Características:**
- Maneja el back stack automáticamente
- Transiciones entre pantallas
- Gestión del estado de cada pantalla
- Deep linking support

### ¿Por qué usar Sealed Class para rutas?

**Sealed Class** garantiza type-safety y evita errores de strings.

**Sin Sealed Class:**
```kotlin
navController.navigate("contact_form")  // Typo: "contac_form" → Error en runtime
```

**Con Sealed Class:**
```kotlin
navController.navigate(Screen.ContactForm.route)  // Validado en compilación
```

### ¿Qué hace viewModel()?

`viewModel()` obtiene o crea una instancia del ViewModel asociada al ciclo de vida del Composable.

**Ventajas:**
- El ViewModel sobrevive a recomposiciones
- Se destruye automáticamente cuando la pantalla se elimina del back stack
- Scope correcto para cada pantalla

---

## Paso 4: Actualizar MainActivity

Finalmente, actualizamos la **MainActivity** para usar nuestro sistema de navegación.

### Leer MainActivity.kt actual

Primero leemos el archivo actual para ver qué cambiar:

**En Android Studio:**

1. Panel izquierdo → `app/src/main/java/duoc/desarrollomobile/tiendapp/`
2. Abrir `MainActivity.kt`

### Código de MainActivity.kt

Reemplaza todo el contenido de `MainActivity.kt` con:

```kotlin
package duoc.desarrollomobile.tiendapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import duoc.desarrollomobile.tiendapp.navigation.AppNavigation
import duoc.desarrollomobile.tiendapp.ui.theme.TiendAppTheme

/**
 * Activity principal. ComponentActivity es base para Activities con Compose, gestiona ciclo de vida.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * enableEdgeToEdge dibuja contenido detrás de barras del sistema (diseño inmersivo).
         */
        enableEdgeToEdge()

        /**
         * setContent define contenido de Activity con Compose. Todo dentro es UI declarativa.
         */
        setContent {
            /**
             * TiendAppTheme aplica tema Material3 (colores, tipografía, shapes).
             */
            TiendAppTheme {
                /**
                 * Surface es contenedor Material Design con color de fondo del tema.
                 */
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    /**
                     * NavController gestiona navegación. rememberNavController mantiene estado entre recomposiciones.
                     */
                    val navController = rememberNavController()

                    /**
                     * AppNavigation es el grafo de navegación con todas las pantallas.
                     */
                    AppNavigation(navController = navController)
                }
            }
        }
    }
}
```

### ¿Qué es ComponentActivity?

**ComponentActivity** es la clase base para Activities modernas de Android.

**Características:**
- Soporte para Jetpack Compose con setContent()
- Integración con ViewModels
- Lifecycle-aware
- SavedStateRegistry support

### ¿Qué hace enableEdgeToEdge?

**enableEdgeToEdge()** permite que el contenido se extienda detrás de las barras del sistema.

**Beneficios:**
- Diseño más moderno e inmersivo
- Aprovecha toda la pantalla
- Material Design 3 recomienda este enfoque

### ¿Qué es rememberNavController?

**rememberNavController()** crea y recuerda una instancia de NavController.

**Características:**
- Sobrevive a recomposiciones
- Maneja el back stack
- Estado persistente

---

## Paso 5: Agregar Permiso de Internet

Antes de ejecutar la app, necesitamos agregar el permiso de Internet para que Coil pueda cargar las imágenes del carrusel desde URLs.

### Abrir AndroidManifest.xml

**En Android Studio:**

1. Panel izquierdo → `app` → `src` → `main`
2. Abrir `AndroidManifest.xml`

### Agregar el permiso

Agrega esta línea **ANTES** de la etiqueta `<application>`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

### AndroidManifest.xml completo debería verse así:

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <uses-permission android:name="android.permission.INTERNET" />

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.TiendApp"
        tools:targetApi="31">
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:label="@string/app_name"
            android:theme="@style/Theme.TiendApp">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

### ¿Por qué es necesario este permiso?

**Internet Permission** es obligatorio para:
- Cargar imágenes desde URLs (Coil/Glide)
- Hacer llamadas a APIs
- Cualquier acceso a la red

**Importante:** Sin este permiso, la app crasheará al intentar acceder a Internet.

---

## Paso 6: Verificar la estructura final

Después de crear todos los archivos, tu estructura debería verse así:

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
│   ├── HomeViewModel.kt
│   └── ContactViewModel.kt
├── view/
│   ├── HomeScreen.kt ✓
│   └── ContactFormScreen.kt ✓
├── navigation/
│   └── AppNavigation.kt ✓
├── utils/
│   └── ValidationUtils.kt
├── ui/theme/
└── MainActivity.kt ✓
```

---

## Resumen de Conceptos

### 1. Composable básico

```kotlin
@Composable
fun MyScreen() {
    Column {
        Text("Hello")
        Button(onClick = {}) { Text("Click") }
    }
}
```

### 2. Observar StateFlow en Compose (Lifecycle-aware)

```kotlin
val nombre by viewModel.nombre.collectAsStateWithLifecycle()
Text(nombre)  // Se actualiza automáticamente y detiene colección en background
```

### 3. Navegación entre pantallas

```kotlin
// Definir rutas
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Detail : Screen("detail")
}

// Navegar
navController.navigate(Screen.Detail.route)

// Volver
navController.popBackStack()
```

### 4. TextField con validación

```kotlin
OutlinedTextField(
    value = nombre,
    onValueChange = { viewModel.onNombreChange(it) },
    isError = nombreError != null,
    supportingText = {
        nombreError?.let { Text(it, color = Error) }
    }
)
```

### 5. Animaciones simples

```kotlin
val scale by animateFloatAsState(
    targetValue = if (pressed) 0.95f else 1.0f
)
Box(modifier = Modifier.scale(scale))
```

### 6. Loading state

```kotlin
Button(
    onClick = { viewModel.save() },
    enabled = !isLoading
) {
    if (isLoading) {
        CircularProgressIndicator()
    } else {
        Text("Guardar")
    }
}
```

---

## Código Final

Todos los archivos finales ya están en las secciones anteriores. La aplicación está completa y lista para ejecutar.

---

## ¿Cómo ejecutar la app?

1. **Sincronizar Gradle:** File → Sync Project with Gradle Files
2. **Conectar dispositivo** o iniciar emulador
3. **Run:** Click en el botón verde "Run" o Shift + F10
4. **Probar:**
   - Ver el carrusel automático en Home
   - Presionar botón "Ir a Formulario de Contacto"
   - Completar el formulario
   - Ver validaciones en tiempo real
   - Guardar contacto
   - Ver Snackbar de éxito
   - Volver automáticamente al Home

---

## Funcionalidades implementadas

- Pantalla Home con carrusel automático de imágenes
- Navegación fluida entre pantallas
- Formulario completo con 5 campos validados
- Validaciones en tiempo real con regex
- Dropdown de regiones desde JSON
- Contador de caracteres en mensaje
- Loading state al guardar
- Snackbar de éxito
- Guardado en Room Database SQLite
- Animaciones suaves en botones
- Diseño Material3 moderno
- Arquitectura MVVM completa

---
