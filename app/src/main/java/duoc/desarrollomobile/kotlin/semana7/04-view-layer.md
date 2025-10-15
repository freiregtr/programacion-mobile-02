# Capa View: Interfaz de Usuario con Jetpack Compose y Material3

En este documento crearemos la **capa View** de nuestra arquitectura MVVM usando Jetpack Compose y Material3.

---

## Que es Jetpack Compose?

**Jetpack Compose** es el toolkit moderno de Android para crear interfaces de usuario de forma declarativa, similar a React

### Declarativo vs Imperativo

**Imperativo (XML tradicional):**
```kotlin
// tienes que decir el cómo hacerlo paso a paso
val textView = findViewById<TextView>(R.id.textView)
textView.text = "Hola"
textView.setTextColor(Color.BLUE)
```

**Declarativo (Compose):**
```kotlin
// Solo describes el que quieres
Text(
    text = "Hola",
    color = Color.Blue
)
```

---

## Componentes que crearemos

| Componente | Propósito |
|------------|-----------|
| `LoginScreen` | Pantalla principal con todo el login |
| `LoginHeader` | Logo y títulos |
| `LoginForm` | Formulario con inputs y botones |
| `ErrorMessage` | Mensaje de error animado |
| `TestUsersInfo` | Información de usuarios de prueba |

---

## Paso 1: Crear LoginScreen.kt

Este sera nuestro componente principal de UI

### Crear el archivo

**En Android Studio:**

1. Click derecho en el paquete `view` - `New` - `Kotlin Class/File`
2. Nombre: `LoginScreen`
3. Tipo: **"File"**
4. Click **OK**

### Codigo completo de LoginScreen.kt, imports y componente principal

```kotlin
package duoc.desarrollomobile.carrocompra.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.collectAsState
import duoc.desarrollomobile.carrocompra.model.LoginResult
import duoc.desarrollomobile.carrocompra.viewmodel.LoginViewModel

/**
 * Pantalla principal de Login con Jetpack Compose y Material3
 *
 * @param viewModel ViewModel que maneja el estado y lógica de presentación
 * @param modifier Modificador para personalizar el layout (tamaño, padding, etc.)
 * @param onLoginSuccess Callback (función) que se ejecuta cuando el login es exitoso
 *
 * ¿Qué es @Composable?
 * - Marca funciones que pueden generar UI (elementos visuales)
 * - Solo pueden ser llamadas desde otras funciones @Composable
 * - Se "recomponen" (vuelven a ejecutarse) cuando cambia el estado que observan
 */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,          // ViewModel que contiene el estado y la lógica
    modifier: Modifier = Modifier,      // Modificador con valor por defecto (vacío)
    onLoginSuccess: () -> Unit = {}     // Lambda opcional con implementación vacía por defecto
) {
    // Observamos el estado del ViewModel usando collectAsState()
    // ¿Qué hace collectAsState()?
    // - Convierte StateFlow en State de Compose
    // - Cada vez que el ViewModel actualiza uiState, esta función se recompone
    // - "by" es una delegación: accedemos directamente a uiState sin .value
    val uiState by viewModel.uiState.collectAsState()

    // rememberScrollState() recuerda la posición del scroll entre recomposiciones
    // Sin "remember", la posición se resetearía cada vez que se recompone
    val scrollState = rememberScrollState()

    // LaunchedEffect ejecuta código con efectos secundarios (side effects)
    // Se ejecuta cuando cambia la clave especificada (uiState.loginResult)
    //
    // ¿Qué es un side effect?
    // - Una operación que afecta algo fuera de la función (navegar, mostrar Toast, etc.)
    // - No debe ejecutarse en el cuerpo principal de @Composable
    LaunchedEffect(uiState.loginResult) {
        // Este bloque se ejecuta cada vez que loginResult cambia
        when (uiState.loginResult) {
            is LoginResult.Success -> {
                // Login exitoso: ejecutamos el callback
                onLoginSuccess()  // Aquí MainActivity mostrará un Toast
            }
            else -> {
                // Para otros estados (Loading, Error, Idle) no hacemos nada
            }
        }
    }

    // UI principal: Column organiza elementos verticalmente (uno debajo del otro)
    Column(
        // Modifier: permite encadenar modificaciones visuales y de comportamiento
        modifier = modifier                      // Aplicamos el modifier recibido por parámetro
            .fillMaxSize()                       // Ocupa todo el tamaño disponible (ancho y alto)
            .padding(24.dp)                      // Padding interno de 24dp en todos los lados
            .verticalScroll(scrollState),        // Permite hacer scroll vertical
        horizontalAlignment = Alignment.CenterHorizontally, // Centra horizontalmente los hijos
        verticalArrangement = Arrangement.Center             // Centra verticalmente los hijos
    ) {

        // Componente: Logo y título de la app
        LoginHeader()

        // Spacer crea espacio vacío entre componentes
        Spacer(modifier = Modifier.height(40.dp))

        // Componente: Formulario con campos de email y password
        // Este componente es STATELESS: no tiene estado propio, recibe todo por parámetros
        LoginForm(
            // Pasamos datos del estado
            email = uiState.email,
            password = uiState.password,
            emailError = uiState.emailError,
            passwordError = uiState.passwordError,
            passwordVisible = uiState.passwordVisible,
            isLoading = uiState.isLoading,
            // Pasamos callbacks (funciones) del ViewModel usando referencias (::)
            // viewModel::onEmailChange es lo mismo que { email -> viewModel.onEmailChange(email) }
            onEmailChange = viewModel::onEmailChange,
            onPasswordChange = viewModel::onPasswordChange,
            onPasswordVisibilityToggle = viewModel::togglePasswordVisibility,
            onLoginClick = viewModel::onLoginClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        // AnimatedVisibility muestra/oculta contenido con animación
        // visible: controla si el contenido es visible o no
        // enter: animación al aparecer (fadeIn = desvanecerse entrando)
        // exit: animación al desaparecer (fadeOut = desvanecerse saliendo)
        AnimatedVisibility(
            visible = uiState.loginResult is LoginResult.Error, // Visible solo si hay error
            enter = fadeIn(),    // Animación suave al aparecer
            exit = fadeOut()     // Animación suave al desaparecer
        ) {
            // Mensaje de error que aparece solo cuando hay error
            ErrorMessage(
                // as? LoginResult.Error intenta convertir (cast seguro)
                // Si no es Error, devuelve null, por eso usamos ?: "" como fallback
                message = (uiState.loginResult as? LoginResult.Error)?.message ?: "",
                onDismiss = viewModel::resetLoginResult  // Callback para cerrar el mensaje
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Información de usuarios de prueba para facilitar el testing
        TestUsersInfo()
    }
}
```

---

## PARTE 2: Componente LoginHeader

```kotlin
/**
 * Encabezado con logo y títulos
 *
 * Función PRIVADA: solo se usa dentro de este archivo
 * Componente STATELESS: no tiene estado propio, solo muestra UI estática
 */
@Composable
private fun LoginHeader() {
    // Icon: componente de Compose para mostrar iconos de Material Design
    Icon(
        imageVector = Icons.Default.ShoppingCart,      // Icono vectorial de Material Icons
        contentDescription = "Logo",                   // Para accesibilidad (lectores de pantalla)
        modifier = Modifier.size(80.dp),               // Tamaño del icono: 80dp x 80dp
        tint = MaterialTheme.colorScheme.primary       // Color del tema (cambia según tema claro/oscuro)
    )
    // ¿Qué es MaterialTheme.colorScheme?
    // - Sistema de colores de Material3
    // - primary: color principal del tema
    // - Se adapta automáticamente a modo claro/oscuro

    Spacer(modifier = Modifier.height(16.dp))

    // Text: componente de Compose para mostrar texto
    Text(
        text = "CarroCompra",                          // Texto a mostrar
        style = MaterialTheme.typography.headlineLarge.copy(
            // MaterialTheme.typography: estilos de texto predefinidos de Material3
            // headlineLarge: estilo para títulos grandes
            // .copy() permite modificar el estilo base
            fontWeight = FontWeight.Bold               // Hacemos el texto en negrita
        ),
        color = MaterialTheme.colorScheme.primary      // Color del tema principal
    )

    Spacer(modifier = Modifier.height(8.dp))

    // Subtítulo: texto más pequeño
    Text(
        text = "Bienvenido de nuevo",
        style = MaterialTheme.typography.bodyLarge,    // bodyLarge: texto de cuerpo grande
        color = MaterialTheme.colorScheme.onSurfaceVariant  // Color para texto secundario
    )
    // onSurfaceVariant: color para texto que no es el principal (menos contraste)
}
```

### Explicación del código

**Icon:**
```kotlin
Icon(
    imageVector = Icons.Default.ShoppingCart, // Icono de Material Icons
    contentDescription = "Logo",               // Para accesibilidad
    modifier = Modifier.size(80.dp),          // Tamaño de 80dp
    tint = MaterialTheme.colorScheme.primary  // Color del tema
)
```

**Text con estilo:**
```kotlin
Text(
    text = "CarroCompra",
    style = MaterialTheme.typography.headlineLarge.copy(
        fontWeight = FontWeight.Bold  // Modificamos el estilo base
    ),
    color = MaterialTheme.colorScheme.primary
)
```

**Spacer** crea espacio vertical:
```kotlin
Spacer(modifier = Modifier.height(16.dp))
```

---

## PARTE 3: Componente LoginForm

```kotlin
/**
 * Formulario de login con campos de email y contraseña
 *
 * Este componente recibe TODOS los datos y callbacks como parámetros
 * No tiene estado propio → STATELESS COMPONENT
 *
 * ¿Por qué stateless?
 * - Facilita la reutilización
 * - Fácil de testear (solo pasas parámetros y verificas el resultado)
 * - El estado vive en el ViewModel (single source of truth)
 */
@Composable
private fun LoginForm(
    // Datos del estado (valores actuales)
    email: String,
    password: String,
    emailError: String?,           // null = sin error, String = mensaje de error
    passwordError: String?,
    passwordVisible: Boolean,      // true = mostrar texto, false = mostrar asteriscos
    isLoading: Boolean,            // true = mostrando spinner, false = botón normal
    // Callbacks (funciones que se ejecutan cuando el usuario interactúa)
    onEmailChange: (String) -> Unit,              // Recibe el nuevo texto del email
    onPasswordChange: (String) -> Unit,           // Recibe el nuevo texto del password
    onPasswordVisibilityToggle: () -> Unit,       // Sin parámetros, solo alterna visibilidad
    onLoginClick: () -> Unit                      // Sin parámetros, ejecuta el login
) {
    // Card: componente de Material3 para tarjetas con elevación (sombra)
    Card(
        modifier = Modifier
            .fillMaxWidth()                   // Ocupa todo el ancho disponible
            .padding(vertical = 8.dp),        // Padding solo arriba y abajo (no a los lados)
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), // Sombra de 4dp
        shape = RoundedCornerShape(16.dp)     // Esquinas redondeadas con radio de 16dp
    ) {
        // Column dentro del Card para organizar los campos verticalmente
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)               // Padding interno de la Card
        ) {

            // Campo de Email con OutlinedTextField
            // OutlinedTextField es un componente de Material3 para entrada de texto con borde
            OutlinedTextField(
                value = email,                    // Valor actual del campo (del estado)
                onValueChange = onEmailChange,    // Se ejecuta cada vez que el usuario escribe
                                                  // onEmailChange recibe el nuevo texto completo
                label = { Text("Email") },        // Etiqueta flotante que sube al escribir
                placeholder = { Text("ejemplo@correo.com") },  // Texto de ayuda (gris claro)
                leadingIcon = {                   // Icono al inicio del campo (izquierda)
                    Icon(
                        imageVector = Icons.Default.Email,  // Icono de sobre de Material Icons
                        contentDescription = "Email"        // Para accesibilidad
                    )
                },
                modifier = Modifier.fillMaxWidth(),   // Campo ocupa todo el ancho
                isError = emailError != null,         // true = borde rojo, false = borde normal
                supportingText = {                    // Texto debajo del campo (mensajes de error/ayuda)
                    if (emailError != null) {
                        Text(
                            text = emailError,        // Muestra el mensaje de error
                            color = MaterialTheme.colorScheme.error  // Color rojo del tema
                        )
                    }
                    // Si emailError es null, no muestra nada
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,   // Teclado optimizado para email (@, .com, etc.)
                    imeAction = ImeAction.Next           // Botón "Siguiente" en el teclado
                ),
                singleLine = true,             // Solo una línea (no permite Enter)
                enabled = !isLoading           // Si está loading, el campo se deshabilita (gris)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Contraseña (similar al email, pero con características especiales)
            OutlinedTextField(
                value = password,                     // Valor actual del password
                onValueChange = onPasswordChange,     // Se ejecuta al escribir
                label = { Text("Contraseña") },       // Etiqueta flotante
                placeholder = { Text("Mínimo 6 caracteres") },  // Hint de longitud mínima
                leadingIcon = {                       // Icono al inicio (candado)
                    Icon(
                        imageVector = Icons.Default.Lock,    // Icono de candado
                        contentDescription = "Contraseña"
                    )
                },
                trailingIcon = {                      // Icono al final (ojo para mostrar/ocultar)
                    // IconButton: botón clickeable que contiene un icono
                    IconButton(onClick = onPasswordVisibilityToggle) {
                        Icon(
                            // Cambia el icono según passwordVisible
                            imageVector = if (passwordVisible)
                                Icons.Default.Visibility      // Ojo abierto = contraseña visible
                            else
                                Icons.Default.VisibilityOff,  // Ojo tachado = contraseña oculta
                            contentDescription = if (passwordVisible)
                                "Ocultar contraseña"
                            else
                                "Mostrar contraseña"
                        )
                    }
                },
                // visualTransformation controla cómo se muestra el texto
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None              // Muestra texto normal: "abc123"
                else
                    PasswordVisualTransformation(),        // Muestra asteriscos: "******"
                modifier = Modifier.fillMaxWidth(),
                isError = passwordError != null,           // Borde rojo si hay error
                supportingText = {                         // Mensaje de error debajo
                    if (passwordError != null) {
                        Text(
                            text = passwordError,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,  // Teclado optimizado para contraseñas
                    imeAction = ImeAction.Done             // Botón "Listo" (último campo)
                ),
                singleLine = true,
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de Login con estado de loading
            // Button: componente de Material3 para botones con elevación
            Button(
                onClick = onLoginClick,            // Se ejecuta al hacer click
                modifier = Modifier
                    .fillMaxWidth()                // Botón ocupa todo el ancho
                    .height(56.dp),                // Altura fija de 56dp (tamaño recomendado)
                enabled = !isLoading,              // Deshabilitado mientras carga (no clickeable)
                shape = RoundedCornerShape(12.dp)  // Esquinas redondeadas
            ) {
                // Contenido del botón cambia según el estado isLoading
                if (isLoading) {
                    // Mientras está cargando, muestra un spinner circular
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),           // Tamaño del spinner
                        color = MaterialTheme.colorScheme.onPrimary  // Color blanco (contrasta con el botón)
                    )
                    // onPrimary = color que contrasta con primary (texto sobre el botón)
                } else {
                    // Cuando NO está cargando, muestra el texto normal
                    Text(
                        text = "Iniciar Sesión",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold  // Texto en negrita para resaltar
                        )
                    )
                }
            }
        }
    }
}
```

### Explicación de OutlinedTextField

```kotlin
OutlinedTextField(
    value = email,                    // Valor actual
    onValueChange = onEmailChange,    // Se ejecuta al escribir
    label = { Text("Email") },        // Etiqueta flotante
    placeholder = { Text("...") },    // Texto de ayuda
    leadingIcon = { Icon(...) },      // Icono al inicio
    trailingIcon = { IconButton... }, // Icono al final (ej: ver password)
    modifier = Modifier.fillMaxWidth(), // Ocupa todo el ancho
    isError = emailError != null,     // Modo error (rojo)
    supportingText = {                // Mensaje debajo del campo
        if (emailError != null) {
            Text(emailError)
        }
    },
    keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Email, // Teclado de email
        imeAction = ImeAction.Next         // Botón "Siguiente"
    ),
    singleLine = true,               // Una sola línea
    enabled = !isLoading             // Deshabilitado si está loading
)
```

### PasswordVisualTransformation

Oculta el texto del password con asteriscos:

```kotlin
visualTransformation = if (passwordVisible)
    VisualTransformation.None          // Texto visible
else
    PasswordVisualTransformation()     // Asteriscos ******
```

---

## PARTE 4: Componente ErrorMessage

```kotlin
/**
 * Mensaje de error con animación de entrada/salida
 *
 * Este componente se muestra cuando hay un error de login
 * Aparece con una animación fadeIn y desaparece con fadeOut
 */
@Composable
private fun ErrorMessage(
    message: String,           // Mensaje de error a mostrar
    onDismiss: () -> Unit      // Callback para cerrar el mensaje
) {
    // Card con color de error del tema Material3
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            // errorContainer: color de fondo para errores (rojo claro en tema claro)
            containerColor = MaterialTheme.colorScheme.errorContainer
        ),
        shape = RoundedCornerShape(12.dp)  // Esquinas redondeadas
    ) {
        // Row: organiza elementos horizontalmente (uno al lado del otro)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            // Distribución horizontal: un elemento a la izquierda, otro a la derecha
            horizontalArrangement = Arrangement.SpaceBetween,
            // Alineación vertical: centrados verticalmente
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Texto del mensaje de error
            Text(
                text = message,
                // onErrorContainer: color que contrasta con errorContainer (texto oscuro sobre fondo claro)
                color = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.weight(1f)  // weight(1f) = ocupa el espacio disponible
                                                // Esto empuja el botón hacia la derecha
            )

            // TextButton: botón de solo texto (sin fondo)
            TextButton(onClick = onDismiss) {
                Text("OK")  // Al hacer click, llama onDismiss que resetea el loginResult
            }
        }
    }
}
```

---

## PARTE 5: Componente TestUsersInfo

```kotlin
/**
 * Card con información de usuarios de prueba
 *
 * Este componente muestra los usuarios de prueba disponibles
 * Facilita el testing durante el desarrollo
 */
@Composable
private fun TestUsersInfo() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            // secondaryContainer: color secundario del tema
            // .copy(alpha = 0.5f) hace el color semi-transparente (50% de opacidad)
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Título de la sección
            Text(
                text = "Usuarios de prueba:",
                style = MaterialTheme.typography.titleSmall.copy(
                    // titleSmall: estilo para títulos pequeños
                    fontWeight = FontWeight.Bold  // Negrita para resaltar
                ),
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Lista de usuarios de prueba
            // Cada uno es un componente TestUserItem
            TestUserItem("admin@carrocompra.com", "123456")
            TestUserItem("usuario@test.com", "password")
            TestUserItem("demo@demo.com", "demo123")
        }
    }
}

/**
 * Item individual de usuario de prueba
 *
 * Componente reutilizable para mostrar credenciales de prueba
 * Formato: email / password
 */
@Composable
private fun TestUserItem(email: String, password: String) {
    Text(
        text = "$email / $password",           // Interpolación de strings con $
        style = MaterialTheme.typography.bodySmall,  // bodySmall: texto pequeño
        color = MaterialTheme.colorScheme.onSecondaryContainer,
        fontSize = 12.sp,                      // sp = scale-independent pixels (respeta ajustes de accesibilidad)
        modifier = Modifier.padding(vertical = 2.dp)  // Pequeño padding vertical entre items
    )
}
```

---

## Paso 2: Actualizar MainActivity.kt

Ahora integramos todo en la MainActivity.

### Código completo de MainActivity.kt

Abre `MainActivity.kt` y reemplaza todo el contenido con esto:

```kotlin
package duoc.desarrollomobile.carrocompra

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import duoc.desarrollomobile.carrocompra.ui.theme.CarroCompraTheme
import duoc.desarrollomobile.carrocompra.view.LoginScreen
import duoc.desarrollomobile.carrocompra.viewmodel.LoginViewModel

/**
 * Activity principal de la aplicación
 * Punto de entrada de la app
 *
 * ComponentActivity es la clase base para Activities que usan Jetpack Compose
 */
class MainActivity : ComponentActivity() {
    // onCreate se ejecuta cuando se crea la Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // enableEdgeToEdge() hace que la app use todo el espacio de la pantalla
        // Incluye las áreas de la barra de estado y navegación (edge-to-edge display)
        enableEdgeToEdge()

        // setContent define el contenido de la Activity usando Compose
        // Todo lo que está dentro es código declarativo de UI
        setContent {
            // CarroCompraTheme aplica el tema Material3 de la app
            // Define colores, tipografía, formas, etc.
            CarroCompraTheme {
                MainScreen()  // Pantalla principal
            }
        }
    }
}

/**
 * Pantalla principal que contiene el Scaffold y LoginScreen
 *
 * Scaffold proporciona la estructura básica de Material3
 * (barra superior, barra inferior, FAB, etc.)
 */
@Composable
fun MainScreen(
    // viewModel() crea o recupera el ViewModel
    // Sobrevive a cambios de configuración (rotación)
    viewModel: LoginViewModel = viewModel()
) {
    // LocalContext.current obtiene el Context actual
    // Necesario para Toast, navegación, acceso a recursos, etc.
    val context = LocalContext.current

    // Scaffold: estructura base de Material3
    // Maneja el padding para barras del sistema automáticamente
    Scaffold(
        modifier = Modifier.fillMaxSize()  // Ocupa toda la pantalla
    ) { innerPadding ->
        // innerPadding: padding calculado automáticamente por Scaffold
        // Evita que el contenido quede detrás de las barras del sistema

        LoginScreen(
            viewModel = viewModel,
            modifier = Modifier.padding(innerPadding),  // Aplicamos el innerPadding
            onLoginSuccess = {
                // Callback que se ejecuta cuando el login es exitoso
                // Aquí podrías navegar a otra pantalla con NavController

                // Toast: mensaje temporal que aparece en la pantalla
                Toast.makeText(
                    context,                          // Context de la app
                    "¡Login exitoso! Bienvenido",    // Mensaje a mostrar
                    Toast.LENGTH_LONG                 // Duración (LONG = ~3.5 segundos)
                ).show()                              // Muestra el Toast
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    CarroCompraTheme {
        MainScreen()
    }
}
```

### Explicación del código

**enableEdgeToEdge():**
```kotlin
enableEdgeToEdge() // La app usa todo el espacio de la pantalla
```

**Scaffold:**
```kotlin
Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
    // innerPadding respeta las barras del sistema
    LoginScreen(modifier = Modifier.padding(innerPadding))
}
```

**viewModel():**
```kotlin
viewModel: LoginViewModel = viewModel()
// Crea o recupera el ViewModel
// Sobrevive a rotaciones de pantalla
```

**Toast:**
```kotlin
Toast.makeText(context, "Mensaje", Toast.LENGTH_LONG).show()
// Muestra un mensaje temporal
```

---

## Paso 3: Ejecutar la app

### Sincronizar con Gradle

1. Ve a `File` → `Sync Project with Gradle Files`
2. Espera a que termine la sincronización

### Ejecutar en el emulador

1. Selecciona tu dispositivo/emulador
2. Click en el botón Play verde ▶️
3. Espera a que compile e instale

### ¿Qué deberías ver?

Una pantalla moderna de login con:
- Logo de carrito de compras
- Título "CarroCompra"
- Campo de email con icono
- Campo de password con botón para mostrar/ocultar
- Botón "Iniciar Sesión"
- Card con usuarios de prueba

---

## Probando la App

### Caso 1: Validación de errores

1. **Deja los campos vacíos** y haz click en "Iniciar Sesión"
   - Deberías ver mensajes de error en rojo debajo de cada campo

2. **Escribe un email inválido** (`test`) y haz click
   - Debería mostrar "Email inválido"

3. **Escribe un password corto** (`123`) y haz click
   - Debería mostrar "La contraseña debe tener al menos 6 caracteres"

### Caso 2: Login exitoso

1. **Usa un usuario de prueba:**
   - Email: `admin@carrocompra.com`
   - Password: `123456`

2. **Haz click en "Iniciar Sesión"**
   - El botón se convierte en un spinner circular
   - Los campos se deshabilitan
   - Después de 1.5 segundos, aparece un Toast con "¡Login exitoso! Bienvenido"

### Caso 3: Credenciales incorrectas

1. **Escribe credenciales inválidas:**
   - Email: `test@test.com`
   - Password: `wrongpass`

2. **Haz click en "Iniciar Sesión"**
   - Aparece un Card rojo con "Email o contraseña incorrectos"
   - Puedes hacer click en "OK" para cerrar el mensaje

---

## Conceptos de Compose Explicados en Profundidad

### 1. @Composable - Funciones que generan UI

**¿Qué es?** Anotación que marca funciones que pueden generar elementos visuales.

**Características:**
- Solo pueden ser llamadas desde otras funciones @Composable
- Se "recomponen" (vuelven a ejecutarse) cuando cambia el estado que observan
- Son declarativas: describes QUÉ quieres, no CÓMO hacerlo

**Ejemplo:**
```kotlin
@Composable
fun MiComponente() {
    Text("Hola")  // Genera un elemento de texto en la UI
}
```

**Recomposición:**
```kotlin
@Composable
fun Contador(count: Int) {
    Text("Clicks: $count")
    // Cuando count cambia, esta función se recompone
    // Solo se actualiza este Text, no toda la pantalla
}
```

### 2. Modifier - Modificaciones encadenadas

**¿Qué es?** Sistema para aplicar modificaciones visuales y de comportamiento a los componentes.

**Características:**
- Se encadenan en orden (el orden importa)
- Inmutables: cada modificación devuelve un nuevo Modifier
- Pueden modificar tamaño, padding, color, click handlers, etc.

**Ejemplo:**
```kotlin
Modifier
    .fillMaxWidth()           // 1. Ocupa todo el ancho disponible
    .height(56.dp)            // 2. Altura fija de 56dp
    .padding(16.dp)           // 3. Padding de 16dp DENTRO del tamaño anterior
    .background(Color.Blue)   // 4. Fondo azul
    .clickable { }            // 5. Hace el elemento clickeable
```

**¿Por qué el orden importa?**
```kotlin
// Caso 1: Padding ANTES del background
Modifier
    .padding(16.dp)    // Crea espacio transparente alrededor
    .background(Color.Blue)  // Fondo azul solo en el contenido

// Caso 2: Padding DESPUÉS del background
Modifier
    .background(Color.Blue)  // Fondo azul en todo el espacio
    .padding(16.dp)    // Padding DENTRO del fondo azul

// Resultado: visualmente diferentes
```

### 3. Column vs Row - Layouts lineales

**Column:** Organiza elementos verticalmente (uno debajo del otro)
```kotlin
Column {
    Text("1")  // Arriba
    Text("2")  // Medio
    Text("3")  // Abajo
}
```

**Row:** Organiza elementos horizontalmente (uno al lado del otro)
```kotlin
Row {
    Text("A")  // Izquierda
    Text("B")  // Centro
    Text("C")  // Derecha
}
```

**Con parámetros de alineación:**
```kotlin
Column(
    horizontalAlignment = Alignment.CenterHorizontally,  // Centra horizontalmente
    verticalArrangement = Arrangement.SpaceBetween       // Espacio entre elementos
) {
    Text("Top")
    Text("Middle")
    Text("Bottom")
}
```

### 4. remember y collectAsState - Gestión de estado

**remember:** Recuerda valores entre recomposiciones
```kotlin
// Sin remember: el valor se recrea cada vez (pierde el estado)
val scrollState = rememberScrollState()  // Correcto: mantiene la posición

// remember guarda el valor y lo reutiliza en recomposiciones
// Solo se crea UNA VEZ, luego se reutiliza
```

**collectAsState:** Observa StateFlow y convierte a State de Compose
```kotlin
// Convierte StateFlow<LoginUiState> → State<LoginUiState>
val uiState by viewModel.uiState.collectAsState()

// "by" es una delegación: accedemos a uiState directamente
// Sin "by" sería: val uiState = viewModel.uiState.collectAsState()
//                 uiState.value.email  (necesitamos .value)

// Con "by":      val uiState by viewModel.uiState.collectAsState()
//                 uiState.email  (acceso directo, más limpio)
```

**¿Qué hace collectAsState()?**
- Se suscribe al StateFlow
- Cuando el ViewModel actualiza el estado, esta función se recompone
- Cancela la suscripción automáticamente cuando el componente se destruye

### 5. LaunchedEffect - Efectos secundarios

**¿Qué es un side effect?** Una operación que afecta algo FUERA de la función @Composable.

**Ejemplos de side effects:**
- Navegar a otra pantalla
- Mostrar un Toast
- Iniciar una animación
- Llamar a una API

**¿Por qué LaunchedEffect?**
- Los side effects NO deben ejecutarse en el cuerpo de @Composable
- LaunchedEffect los ejecuta de forma controlada
- Se cancela automáticamente cuando la clave cambia o el componente se destruye

**Ejemplo:**
```kotlin
LaunchedEffect(uiState.loginResult) {
    // Esta clave es: uiState.loginResult
    // Se ejecuta cuando loginResult cambia
    // Si loginResult cambia de nuevo, cancela la ejecución anterior

    when (uiState.loginResult) {
        is LoginResult.Success -> {
            onLoginSuccess()  // Side effect: navegar o mostrar Toast
        }
        else -> {}
    }
}
```

**¿Qué pasa sin LaunchedEffect?**
```kotlin
// MAL: Se ejecuta en cada recomposición (muchas veces)
@Composable
fun MiComponente(viewModel: LoginViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    // Esto se ejecuta en CADA recomposición (puede ser muchas veces)
    if (uiState.loginResult is LoginResult.Success) {
        onLoginSuccess()  // Se llama múltiples veces (bug)
    }
}

// BIEN: Solo se ejecuta cuando loginResult cambia
@Composable
fun MiComponente(viewModel: LoginViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.loginResult) {
        // Solo se ejecuta cuando loginResult cambia (correcto)
        if (uiState.loginResult is LoginResult.Success) {
            onLoginSuccess()
        }
    }
}
```

---

## Comparación React vs Compose

### React

```javascript
function LoginScreen() {
    const [email, setEmail] = useState("");

    return (
        <div>
            <input
                value={email}
                onChange={(e) => setEmail(e.target.value)}
            />
        </div>
    );
}
```

### Compose

```kotlin
@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Column {
        TextField(
            value = uiState.email,
            onValueChange = viewModel::onEmailChange
        )
    }
}
```

---

## Estructura Final Completa

```
CarroCompra/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/duoc/desarrollomobile/carrocompra/
│   │   │   │   ├── model/
│   │   │   │   │   ├── User.kt
│   │   │   │   │   └── LoginRepository.kt
│   │   │   │   ├── viewmodel/
│   │   │   │   │   └── LoginViewModel.kt
│   │   │   │   ├── view/
│   │   │   │   │   └── LoginScreen.kt
│   │   │   │   ├── utils/
│   │   │   │   │   └── ValidationUtils.kt
│   │   │   │   ├── ui/theme/
│   │   │   │   │   ├── Color.kt
│   │   │   │   │   ├── Theme.kt
│   │   │   │   │   └── Type.kt
│   │   │   │   └── MainActivity.kt
│   │   │   ├── res/
│   │   │   └── AndroidManifest.xml
│   │   └── build.gradle.kts
└── documentation/
    ├── 01-setup-android-project.md
    ├── 02-model-layer.md
    ├── 03-viewmodel-layer.md
    └── 04-view-layer.md
```

---

## Resumen Final

Has completado una app Android completa con:

### Arquitectura MVVM
- **Model:** User, LoginRepository, ValidationUtils
- **ViewModel:** LoginViewModel con StateFlow
- **View:** LoginScreen con Jetpack Compose

### Tecnologías Modernas
- **Kotlin:** Lenguaje moderno y conciso
- **Jetpack Compose:** UI declarativa
- **Material3:** Diseño moderno de Google
- **Coroutines:** Programación asíncrona

### Buenas Prácticas
- Separación de responsabilidades (MVVM)
- Estado inmutable (copy())
- Componentes reutilizables (stateless)
- Validaciones centralizadas
- Manejo de errores

---

## ¡Felicitaciones!

Has creado una aplicación Android profesional con arquitectura MVVM, Jetpack Compose y Material3. Esta app incluye:

- Login con validaciones
- Manejo de estados (loading, error, success)
- UI moderna y responsiva
- Código limpio y mantenible

---
