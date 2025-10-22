# Capa Model y Data: Datos, Room Database y Lógica de Negocio (MVVM)

En este documento crearemos la **capa Model y Data** de nuestra arquitectura MVVM. Esta capa contiene los modelos de datos, validaciones, la base de datos Room, DAOs y repositorios.

---

## ¿Qué es la Capa Model?

La **capa Model** es responsable de:
- Definir las estructuras de datos (data classes)
- Manejar la lógica de negocio (validaciones, transformaciones)
- Acceder a fuentes de datos (Room Database, API, archivos locales)

## ¿Qué es la Capa Data?

La **capa Data** específicamente maneja:
- Definición de Entities (tablas de Room)
- DAOs (Data Access Objects) para queries
- Database Room para gestionar SQLite
- Abstracción del acceso a datos

### Componentes que crearemos:

| Archivo | Ubicación | Tipo | Propósito |
|---------|-----------|------|-----------|
| `Contact.kt` | data/ | Entity | Tabla de contactos en Room |
| `ContactDao.kt` | data/ | Interface | Queries de acceso a datos |
| `AppDatabase.kt` | data/ | Abstract Class | Base de datos Room |
| `Region.kt` | model/ | Data Class | Modelo de región |
| `ValidationUtils.kt` | utils/ | Object | Validaciones regex |
| `ContactRepository.kt` | model/ | Class | Lógica de negocio |
| `regiones.json` | assets/ | JSON | Listado de regiones de Chile |

---

## Paso 1: Crear el modelo Region

Comenzamos con el modelo más simple: Region. Este modelo representa una región de Chile que será cargada desde JSON.

### ¿Dónde crear el archivo?

**En Android Studio:**

1. Panel izquierdo → `app/src/main/java/duoc/desarrollomobile/tiendapp/`
2. Click derecho en el paquete `model` → `New` → `Kotlin Class/File`
3. Nombre: `Region`
4. Tipo: selecciona **"File"**, no Class
5. Click **OK**

### Código de Region.kt

Copia este contenido en `model/Region.kt`:

```kotlin
package duoc.desarrollomobile.tiendapp.model

import kotlinx.serialization.Serializable

/**
 * Data class que representa una región de Chile
 * Serializable permite convertir esta clase desde/hacia JSON
 * @param id Identificador único de la región
 * @param nombre Nombre de la región
 */
@Serializable
data class Region(
    val id: Int,
    val nombre: String
)
```

### ¿Qué es @Serializable?

**@Serializable** es una anotación de `kotlinx.serialization` que indica que esta clase puede ser convertida automáticamente desde y hacia JSON.

**Sin @Serializable:**
```kotlin
// Tendrías que parsear manualmente
val json = """{"id":1,"nombre":"Arica y Parinacota"}"""
// Manual parsing complejo...
```

**Con @Serializable:**
```kotlin
// Automático y type-safe
val region = Json.decodeFromString<Region>(json)
```

---

## Paso 2: Crear el Entity Contact (Room)

El Entity `Contact` representa la tabla de contactos en nuestra base de datos SQLite mediante Room.

### Crear el archivo

**En Android Studio:**

1. Click derecho en el paquete `data` → `New` → `Kotlin Class/File`
2. Nombre: `Contact`
3. Tipo: **"File"**
4. Click **OK**

### Código de Contact.kt

```kotlin
package duoc.desarrollomobile.tiendapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity de Room que representa la tabla de contactos en SQLite
 * Cada instancia de esta clase es una fila en la tabla
 *
 * @Entity indica que esta clase es una tabla de Room
 * tableName define el nombre de la tabla en la base de datos
 * "data class" genera automáticamente equals(), hashCode(), toString() y copy()
 * funciones útiles para comparar y manipular objetos de base de datos
 */
@Entity(tableName = "contacts")
data class Contact(
    /**
     * @PrimaryKey indica que este campo es la clave primaria
     * autoGenerate = true hace que Room genere automáticamente un ID único
     * cuando insertamos un nuevo contacto con id = 0
     * Inicializamos con 0 porque Room usa 0 como señal para generar un nuevo ID.
     * Room lo reemplazará automáticamente con el siguiente ID disponible como 1, 2, 3... etc
     */
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    /**
     * Nombre del contacto
     * Este campo se convierte en una columna llamada "nombre" en la tabla
     */
    val nombre: String,

    /**
     * Teléfono del contacto (formato chileno, mas adelante veremos un regex)
     */
    val telefono: String,

    /**
     * Correo electrónico del contacto (mismo formato que regex de la clase pasada)
     */
    val correo: String,

    /**
     * Región seleccionada del contacto
     */
    val region: String,

    /**
     * Mensaje del contacto (máximo 200 caracteres)
     */
    val mensaje: String
)
```

### ¿Qué es una Entity de Room?

**Entity** es una clase que representa una tabla en la base de datos SQLite. Room convierte automáticamente cada instancia de la clase en una fila de la tabla.

**Conversión automática:**

| Clase Kotlin | Base de Datos SQLite |
|--------------|---------------------|
| `@Entity` class Contact | Tabla `contacts` |
| `val nombre: String` | Columna `nombre TEXT` |
| `val id: Int` | Columna `id INTEGER PRIMARY KEY` |

### ¿Qué hace autoGenerate?

Cuando usamos `autoGenerate = true` en el `@PrimaryKey`, Room asigna automáticamente un ID único incremental cada vez que insertamos un nuevo registro.

**Ejemplo:**
```kotlin
// Al insertar, pasamos id = 0
val contact = Contact(
    id = 0,  // Room lo reemplazará con el siguiente ID disponible
    nombre = "Juan Pérez",
    telefono = "+56912345678",
    // ...
)
// Room lo guarda con id = 1, luego id = 2, etc.
```

---

## Paso 3: Crear el DAO (Data Access Object)

El **DAO** define las operaciones que podemos hacer con la base de datos: insertar, consultar, actualizar, eliminar.

### Crear el archivo

**En Android Studio:**

1. Click derecho en `data` → `New` → `Kotlin Class/File`
2. Nombre: `ContactDao`
3. Tipo: **"File"**
4. Click **OK**

### Código de ContactDao.kt

```kotlin
package duoc.desarrollomobile.tiendapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) define las operaciones de acceso a la base de datos
 * @Dao indica que esta interface contiene métodos de acceso a datos
 * Room implementará automáticamente esta interface en tiempo de compilación
 * Doc: https://developer.android.com/training/data-storage/room/accessing-data
 */
@Dao
interface ContactDao {

    /**
     * Inserta un nuevo contacto en la base de datos
     * @Insert genera automáticamente el query INSERT
     *
     * onConflict = OnConflictStrategy.REPLACE significa que si ya existe
     * un contacto con el mismo ID, lo reemplazará en lugar de generar error
     *
     * suspend indica que esta función es asíncrona y debe ejecutarse
     * en un contexto de corrutina (no bloquea el hilo principal)
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: Contact)

    /**
     * Obtiene todos los contactos de la base de datos
     * @Query permite escribir queries SQL personalizadas
     *
     * Flow<List<Contact>> es un stream reactivo que emite automáticamente
     * la lista actualizada cada vez que la tabla cambia
     *
     * No necesita suspend porque Flow ya es asíncrono por naturaleza
     */
    @Query("SELECT * FROM contacts ORDER BY id DESC")
    fun getAllContacts(): Flow<List<Contact>>

    /**
     * Obtiene un contacto específico por su ID
     * :id es un parámetro que se reemplaza con el valor que pasemos
     */
    @Query("SELECT * FROM contacts WHERE id = :id")
    suspend fun getContactById(id: Int): Contact?

    /**
     * Elimina un contacto de la base de datos
     * @Delete genera automáticamente el query DELETE
     * Room identifica el registro por su Primary Key (id)
     */
    @Delete
    suspend fun deleteContact(contact: Contact)

    /**
     * Elimina todos los contactos de la base de datos
     * Útil para testing o resetear datos
     */
    @Query("DELETE FROM contacts")
    suspend fun deleteAllContacts()

    /**
     * Obtiene el número total de contactos guardados
     */
    @Query("SELECT COUNT(*) FROM contacts")
    suspend fun getContactCount(): Int
}
```

### ¿Qué es un DAO?

**DAO** (Data Access Object) es un patrón de diseño que abstrae el acceso a la base de datos. Define qué operaciones podemos hacer sin preocuparnos de cómo se ejecutan.

**Ventajas:**
- Room valida las queries SQL en tiempo de compilación
- Si hay un error en el SQL, el proyecto no compilará
- Genera código optimizado automáticamente

### ¿Por qué usar Flow?

**Flow** es un stream asíncrono de Kotlin que emite valores múltiples en el tiempo.

**Comparación:**

| Método | Características |
|--------|----------------|
| `suspend fun getContacts(): List<Contact>` | Obtiene datos una sola vez, debes llamarlo manualmente para actualizar |
| `fun getContacts(): Flow<List<Contact>>` | Emite automáticamente cada vez que la tabla cambia, reactivo |

**Ejemplo de uso:**
```kotlin
// En el ViewModel
contactDao.getAllContacts().collect { contacts ->
    // Este bloque se ejecuta automáticamente cada vez que se inserta/elimina
    println("Contactos actualizados: ${contacts.size}")
}
```

### ¿Qué es OnConflictStrategy?

**OnConflictStrategy** define qué hacer cuando intentamos insertar un registro que ya existe (mismo Primary Key).

| Estrategia | Comportamiento |
|-----------|----------------|
| `REPLACE` | Reemplaza el registro existente |
| `IGNORE` | Ignora la inserción, mantiene el existente |
| `ABORT` | Cancela la operación con error |

---

## Paso 4: Crear la Database de Room

La **Database** es la clase principal que gestiona SQLite y provee acceso a los DAOs.

### Crear el archivo

**En Android Studio:**

1. Click derecho en `data` → `New` → `Kotlin Class/File`
2. Nombre: `AppDatabase`
3. Tipo: **"File"**
4. Click **OK**

### Código de AppDatabase.kt

```kotlin
package duoc.desarrollomobile.tiendapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Clase principal de la base de datos Room
 *
 * @Database indica que esta clase es una base de datos Room
 * entities = listado de todas las entidades (tablas) de la base de datos
 * version = número de versión del esquema (incrementar al cambiar estructura)
 * exportSchema = false evita exportar el esquema a archivos JSON
 */
@Database(
    entities = [Contact::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Función abstracta que provee acceso al DAO de contactos
     * Room implementará esta función automáticamente
     */
    abstract fun contactDao(): ContactDao

    /**
     * Companion object para implementar el patrón Singleton
     * Esto asegura que solo exista una instancia de la base de datos
     * en toda la aplicación, evitando múltiples conexiones
     */
    companion object {
        /**
         * @Volatile garantiza que los cambios en INSTANCE sean visibles
         * inmediatamente para todos los threads (hilos)
         */
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Obtiene la instancia única de la base de datos
         * Si no existe, la crea. Si ya existe, la retorna
         *
         * @param context Contexto de Android necesario para crear la DB
         * @return Instancia única de AppDatabase
         */
        fun getDatabase(context: Context): AppDatabase {
            // Si INSTANCE no es null, retornamos la instancia existente
            return INSTANCE ?: synchronized(this) {
                // synchronized evita que múltiples threads creen la DB simultáneamente

                // Construimos la base de datos con Room.databaseBuilder
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tiendapp_database"  // Nombre del archivo SQLite
                )
                .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
```

### ¿Qué es RoomDatabase?

**RoomDatabase** es la clase base abstracta que maneja la conexión a SQLite y provee acceso a los DAOs.

**Responsabilidades:**
- Crear y actualizar el esquema de la base de datos
- Validar las queries en tiempo de compilación
- Proveer instancias de los DAOs
- Manejar transacciones y migraciones

### ¿Qué es el patrón Singleton?

**Singleton** es un patrón de diseño que garantiza que solo exista una instancia de una clase en toda la aplicación.

**¿Por qué es importante para Room?**
- Abrir múltiples conexiones a SQLite es costoso en memoria
- Puede causar inconsistencias en los datos
- Una sola instancia mejora el rendimiento

**Flujo del Singleton:**
```
Primera llamada → getDatabase() → Crea instancia → INSTANCE = nueva DB
Segunda llamada → getDatabase() → Retorna INSTANCE existente (no crea nueva)
```

### ¿Qué hace synchronized?

**synchronized** bloquea el acceso concurrente para evitar que dos threads creen la base de datos al mismo tiempo.

**Sin synchronized:**
```
Thread 1: ¿INSTANCE es null? Sí → Crear DB
Thread 2: ¿INSTANCE es null? Sí → Crear DB  // Error: dos instancias
```

**Con synchronized:**
```
Thread 1: ¿INSTANCE es null? Sí → BLOQUEO → Crear DB → DESBLOQUEO
Thread 2: Espera el desbloqueo → ¿INSTANCE es null? No → Retornar existente
```

### ¿Qué es version en @Database?

La **version** indica el número de versión del esquema de la base de datos.

**¿Cuándo incrementarla?**
- Cuando agregues o elimines tablas
- Cuando cambies columnas (agregar, eliminar, renombrar)
- Cuando cambies tipos de datos

**Ejemplo de migración:**
```kotlin
// Versión 1: tabla contacts con 5 campos
@Database(entities = [Contact::class], version = 1)

// Versión 2: agregaste campo "fechaCreacion"
@Database(entities = [Contact::class], version = 2)
// Necesitarás crear una Migration de 1 a 2
```

---

## Paso 5: Crear ValidationUtils

Las **validaciones** verifican que los datos cumplan con los formatos requeridos antes de guardarlos.

### Crear el archivo

**En Android Studio:**

1. Click derecho en `utils` → `New` → `Kotlin Class/File`
2. Nombre: `ValidationUtils`
3. Tipo: **"File"**
4. Click **OK**

### Código de ValidationUtils.kt

```kotlin
package duoc.desarrollomobile.tiendapp.utils

/**
 * Object que contiene funciones de validación para el formulario
 *
 * object crea un Singleton automáticamente
 * Esto permite usar las funciones sin crear instancias:
 * ValidationUtils.isValidEmail("test@test.com")
 */
object ValidationUtils {

    /**
     * Valida que el nombre solo contenga letras y espacios
     * Mínimo 2 caracteres, máximo 50
     *
     * @param nombre Nombre a validar
     * @return true si el nombre es válido, false si no
     */
    fun isValidNombre(nombre: String): Boolean {
        // Verificar que no esté vacío
        if (nombre.isBlank()) return false

        // Verificar longitud
        if (nombre.length < 2 || nombre.length > 50) return false

        /**
         * Regex para validar nombre:
         * ^              = Inicio de la cadena
         * [a-zA-ZáéíóúÁÉÍÓÚñÑ]  = Una letra (incluye acentos y ñ)
         * [a-zA-ZáéíóúÁÉÍÓÚñÑ ]*  = Cero o más letras o espacios
         * $              = Fin de la cadena
         *
         * Esto permite: "Juan", "María José", "José Luis"
         * No permite: "Juan123", "Juan@", "123"
         */
        val nombreRegex = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ][a-zA-ZáéíóúÁÉÍÓÚñÑ ]*$".toRegex()
        return nombreRegex.matches(nombre)
    }

    /**
     * Valida que el teléfono tenga formato chileno
     * Formatos aceptados:
     * - +56912345678
     * - 56912345678
     * - 912345678
     *
     * @param telefono Teléfono a validar
     * @return true si el teléfono es válido, false si no
     */
    fun isValidTelefono(telefono: String): Boolean {
        if (telefono.isBlank()) return false

        /**
         * Regex para validar teléfono chileno:
         * ^              = Inicio
         * (\\+56|56)?    = Opcional: +56 o 56 (código de país)
         * 9              = Obligatorio: 9 (celulares en Chile empiezan con 9)
         * [0-9]{8}       = Exactamente 8 dígitos más
         * $              = Fin
         *
         * Ejemplos válidos:
         * +56912345678 (9 dígitos con código país)
         * 56912345678
         * 912345678 (9 dígitos sin código)
         */
        val telefonoRegex = "^(\\+56|56)?9[0-9]{8}$".toRegex()
        return telefonoRegex.matches(telefono)
    }

    /**
     * Valida que el email tenga un formato correcto
     * @param email Email a validar
     * @return true si el email es válido, false si no
     */
    fun isValidEmail(email: String): Boolean {
        if (email.isBlank()) return false

        /**
         * Regex para validar email:
         * ^                      = Inicio de la cadena
         * [A-Za-z0-9+_.-]+       = Uno o más caracteres válidos antes del @
         * @                      = Arroba literal
         * [A-Za-z0-9.-]+         = Uno o más caracteres para el dominio
         * \\.                    = Punto literal (escapado)
         * [A-Za-z]{2,}           = Al menos 2 letras para la extensión
         * $                      = Fin de la cadena
         *
         * Ejemplos válidos: usuario@ejemplo.com, test@test.cl
         * Ejemplos inválidos: usuario, usuario@, @ejemplo.com
         */
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return emailRegex.matches(email)
    }

    /**
     * Valida que la región no esté vacía
     * @param region Región a validar
     * @return true si la región es válida, false si no
     */
    fun isValidRegion(region: String): Boolean {
        return region.isNotBlank()
    }

    /**
     * Valida que el mensaje no esté vacío y no exceda 200 caracteres
     * @param mensaje Mensaje a validar
     * @return true si el mensaje es válido, false si no
     */
    fun isValidMensaje(mensaje: String): Boolean {
        return mensaje.isNotBlank() && mensaje.length <= 200
    }

    /**
     * Obtiene el mensaje de error para el campo nombre
     * @param nombre Nombre a validar
     * @return Mensaje de error o null si es válido
     */
    fun getNombreErrorMessage(nombre: String): String? {
        return when {
            nombre.isBlank() -> "El nombre es requerido"
            nombre.length < 2 -> "El nombre debe tener al menos 2 caracteres"
            nombre.length > 50 -> "El nombre no puede exceder 50 caracteres"
            !isValidNombre(nombre) -> "El nombre solo puede contener letras y espacios"
            else -> null
        }
    }

    /**
     * Obtiene el mensaje de error para el campo teléfono
     * @param telefono Teléfono a validar
     * @return Mensaje de error o null si es válido
     */
    fun getTelefonoErrorMessage(telefono: String): String? {
        return when {
            telefono.isBlank() -> "El teléfono es requerido"
            !isValidTelefono(telefono) -> "Formato inválido. Use: +56912345678 o 912345678"
            else -> null
        }
    }

    /**
     * Obtiene el mensaje de error para el campo email
     * @param email Email a validar
     * @return Mensaje de error o null si es válido
     */
    fun getEmailErrorMessage(email: String): String? {
        return when {
            email.isBlank() -> "El correo electrónico es requerido"
            !isValidEmail(email) -> "Correo electrónico inválido"
            else -> null
        }
    }

    /**
     * Obtiene el mensaje de error para el campo región
     * @param region Región a validar
     * @return Mensaje de error o null si es válida
     */
    fun getRegionErrorMessage(region: String): String? {
        return when {
            region.isBlank() -> "Debe seleccionar una región"
            else -> null
        }
    }

    /**
     * Obtiene el mensaje de error para el campo mensaje
     * @param mensaje Mensaje a validar
     * @return Mensaje de error o null si es válido
     */
    fun getMensajeErrorMessage(mensaje: String): String? {
        return when {
            mensaje.isBlank() -> "El mensaje es requerido"
            mensaje.length > 200 -> "El mensaje no puede exceder 200 caracteres"
            else -> null
        }
    }
}
```

### Descomposición de las Regex

#### Regex de Nombre

| Parte | Significado | Ejemplo |
|-------|-------------|---------|
| `^` | Inicio de la cadena | |
| `[a-zA-ZáéíóúÁÉÍÓÚñÑ]` | Primera letra obligatoria | "J" en "Juan" |
| `[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*` | Cero o más letras o espacios | "uan Pérez" |
| `$` | Fin de la cadena | |

**Válidos:** Juan, María José, José Luis Pérez
**Inválidos:** Juan123, @Juan, 123

#### Regex de Teléfono Chileno

| Parte | Significado | Ejemplo |
|-------|-------------|---------|
| `^` | Inicio | |
| `(\\+56|56)?` | Opcional: +56 o 56 | "+56" o "56" o nada |
| `9` | Obligatorio: 9 | "9" |
| `[0-9]{8}` | Exactamente 8 dígitos | "12345678" |
| `$` | Fin | |

**Válidos:** +56912345678, 56912345678, 912345678
**Inválidos:** 12345678, 812345678, +56812345678

#### Regex de Email

| Parte | Significado | Ejemplo |
|-------|-------------|---------|
| `^` | Inicio | |
| `[A-Za-z0-9+_.-]+` | Uno o más caracteres válidos | "usuario" |
| `@` | Arroba literal | "@" |
| `[A-Za-z0-9.-]+` | Dominio | "ejemplo" |
| `\\.` | Punto literal | "." |
| `[A-Za-z]{2,}` | Extensión (mínimo 2 letras) | "com" |
| `$` | Fin | |

**Válidos:** usuario@ejemplo.com, test@test.cl
**Inválidos:** usuario@, @ejemplo.com, usuario

---

## Paso 6: Crear ContactRepository

El **Repository** maneja la lógica de negocio y coordina el acceso a los datos (Room Database y archivos JSON).

### Crear el archivo

**En Android Studio:**

1. Click derecho en `model` → `New` → `Kotlin Class/File`
2. Nombre: `ContactRepository`
3. Tipo: **"File"**
4. Click **OK**

### Código de ContactRepository.kt

```kotlin
package duoc.desarrollomobile.tiendapp.model

import android.content.Context
import duoc.desarrollomobile.tiendapp.data.Contact
import duoc.desarrollomobile.tiendapp.data.ContactDao
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json
import java.io.IOException

/**
 * Repository que maneja la lógica de negocio de contactos
 *
 * En MVVM, el Repository es el intermediario entre el ViewModel y las fuentes de datos
 * Puede combinar datos de múltiples fuentes: Room, API REST, archivos locales, etc.
 *
 * @param contactDao DAO para acceder a la base de datos de contactos
 * @param context Contexto de Android para acceder a assets
 */
class ContactRepository(
    // pasamos por parametro contactDao para operaciones CRUD en SQLite
    private val contactDao: ContactDao,

    // Necesario para leer regiones.json desde assets
    private val context: Context
) {

    /**
      * Flow es un stream reactivo que emite valores automáticamente cuando cambian
      * Este Flow emite la lista de contactos cada vez que se inserta, actualiza o elimina
      * un contacto en la base de datos. El ViewModel puede observar estos cambios y
      * actualizar la UI automáticamente sin necesidad de refrescar manualmente.
     */
    val allContacts: Flow<List<Contact>> = contactDao.getAllContacts()

    /**
     * Inserta un nuevo contacto en la base de datos
     * suspend function porque es una operación asíncrona de base de datos
     *
     * @param contact Contacto a insertar
     */
    suspend fun insertContact(contact: Contact) {
        contactDao.insertContact(contact)
    }

    /**
     * Obtiene un contacto específico por su ID
     * o tambien devuelve null, ya que es nullable
     * @param id ID del contacto
     * @return Contacto encontrado o null si no existe
     */
    suspend fun getContactById(id: Int): Contact? {
        return contactDao.getContactById(id)
    }

    /**
     * Elimina un contacto de la base de datos, pide como argumento el contacto a eliminar
     * @param contact Contacto a eliminar
     */
    suspend fun deleteContact(contact: Contact) {
        contactDao.deleteContact(contact)
    }

    /**
     * Elimina todos los contactos de la base de datos
     */
    suspend fun deleteAllContacts() {
        contactDao.deleteAllContacts()
    }

    /**
     * Obtiene el número total de contactos guardados, nos devuelve un int como cantidad
     * @return Cantidad de contactos
     */
    suspend fun getContactCount(): Int {
        return contactDao.getContactCount()
    }

    /**
     * Carga las regiones de Chile desde el archivo JSON en assets
     *
     * Esta función lee el archivo regiones.json, lo parsea y retorna
     * una lista de objetos Region
     *
     * @return Lista de regiones o lista vacía si hay error
     */
    fun loadRegiones(): List<Region> {
        return try {
            // Abrir el archivo desde assets
            val jsonString = context.assets.open("regiones.json")
                .bufferedReader()
                .use { it.readText() }

            // Parsear el JSON a lista de Region
            // Json.decodeFromString es type-safe gracias a @Serializable
            Json.decodeFromString<List<Region>>(jsonString)

        } catch (e: IOException) {
            // Si hay error al leer el archivo, retornamos lista vacía
            e.printStackTrace()
            emptyList()
        } catch (e: Exception) {
            // Si hay error al parsear JSON, retornamos lista vacía
            e.printStackTrace()
            emptyList()
        }
    }
}
```

### ¿Por qué usar Repository?

El **Repository Pattern** abstrae el origen de los datos. El ViewModel no sabe si los datos vienen de Room, de una API o de un archivo local.

**Ventajas:**
- Centraliza la lógica de acceso a datos
- Facilita el testing (puedes usar un Repository fake)
- Permite combinar múltiples fuentes de datos
- Facilita cambiar la fuente de datos sin afectar el ViewModel

**Ejemplo sin Repository:**
```kotlin
// ViewModel tiene que saber de Room
class ContactViewModel(private val dao: ContactDao) {
    fun saveContact() {
        dao.insertContact(contact)  // Acoplado a Room
    }
}
```

**Ejemplo con Repository:**
```kotlin
// ViewModel solo conoce el Repository
class ContactViewModel(private val repository: ContactRepository) {
    fun saveContact() {
        repository.insertContact(contact)  // Desacoplado, puede venir de cualquier fuente
    }
}
```

### ¿Cómo funciona loadRegiones()?

La función `loadRegiones()` lee el archivo JSON desde la carpeta `assets` y lo convierte en una lista de objetos Kotlin.

**Paso a paso:**
1. `context.assets.open("regiones.json")` - Abre el archivo
2. `.bufferedReader()` - Crea un lector eficiente
3. `.use { it.readText() }` - Lee todo el contenido y cierra automáticamente
4. `Json.decodeFromString<List<Region>>(jsonString)` - Parsea JSON a objetos Kotlin

**Manejo de errores:**
- `IOException` - Si el archivo no existe o no se puede leer
- `Exception` - Si el JSON está mal formado
- En ambos casos, retorna lista vacía en lugar de crashear la app

---

## Paso 7: Crear archivo regiones.json

Ahora crearemos el archivo JSON con las 16 regiones de Chile.

### Crear el archivo

**En Android Studio:**

1. Panel izquierdo → `app/src/main/assets/`
2. Click derecho en `assets` → `New` → `File`
3. Nombre: `regiones.json`
4. Click **OK**

### Contenido de regiones.json

```json
[
  {
    "id": 1,
    "nombre": "Arica y Parinacota"
  },
  {
    "id": 2,
    "nombre": "Tarapacá"
  },
  {
    "id": 3,
    "nombre": "Antofagasta"
  },
  {
    "id": 4,
    "nombre": "Atacama"
  },
  {
    "id": 5,
    "nombre": "Coquimbo"
  },
  {
    "id": 6,
    "nombre": "Valparaíso"
  },
  {
    "id": 7,
    "nombre": "Metropolitana de Santiago"
  },
  {
    "id": 8,
    "nombre": "Libertador General Bernardo O'Higgins"
  },
  {
    "id": 9,
    "nombre": "Maule"
  },
  {
    "id": 10,
    "nombre": "Ñuble"
  },
  {
    "id": 11,
    "nombre": "Biobío"
  },
  {
    "id": 12,
    "nombre": "La Araucanía"
  },
  {
    "id": 13,
    "nombre": "Los Ríos"
  },
  {
    "id": 14,
    "nombre": "Los Lagos"
  },
  {
    "id": 15,
    "nombre": "Aysén del General Carlos Ibáñez del Campo"
  },
  {
    "id": 16,
    "nombre": "Magallanes y de la Antártica Chilena"
  }
]
```

### ¿Por qué usar JSON para las regiones?

**Ventajas:**
- Fácil de modificar sin recompilar la app
- Puede ser actualizado dinámicamente desde una API
- Formato estándar y legible
- Fácil de parsear con kotlinx.serialization

**Alternativas:**
- Hardcodeado en código (difícil de mantener)
- Base de datos Room (overkill para datos estáticos)
- String resources (limitado para estructuras complejas)

---

## Paso 8: Verificar la estructura

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
├── utils/
│   └── ValidationUtils.kt
├── viewmodel/
├── view/
├── navigation/
└── ui/theme/

app/src/main/assets/
└── regiones.json
```

---

## Resumen de Conceptos

### 1. Entity vs Data Class

```kotlin
// Entity (tabla de Room)
@Entity(tableName = "contacts")
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String
)

// Data class simple (no es tabla)
data class Region(
    val id: Int,
    val nombre: String
)
```

### 2. DAO Interface

```kotlin
@Dao
interface ContactDao {
    @Insert
    suspend fun insertContact(contact: Contact)

    @Query("SELECT * FROM contacts")
    fun getAllContacts(): Flow<List<Contact>>
}
```

### 3. RoomDatabase Singleton

```kotlin
@Database(entities = [Contact::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tiendapp_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
```

### 4. Repository Pattern

```kotlin
class ContactRepository(
    private val contactDao: ContactDao,
    private val context: Context
) {
    val allContacts: Flow<List<Contact>> = contactDao.getAllContacts()

    suspend fun insertContact(contact: Contact) {
        contactDao.insertContact(contact)
    }
}
```

### 5. Flow vs Suspend Function

```kotlin
// Flow para datos reactivos (se actualiza automáticamente)
fun getAllContacts(): Flow<List<Contact>>

// Suspend para operaciones únicas asíncronas
suspend fun insertContact(contact: Contact)
```

### 6. JSON Deserialization

```kotlin
@Serializable
data class Region(val id: Int, val nombre: String)

val jsonString = """[{"id":1,"nombre":"Arica y Parinacota"}]"""
val regiones = Json.decodeFromString<List<Region>>(jsonString)
```

---

## Código Final

### Contact.kt

```kotlin
package duoc.desarrollomobile.tiendapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val telefono: String,
    val correo: String,
    val region: String,
    val mensaje: String
)
```

### ContactDao.kt

```kotlin
package duoc.desarrollomobile.tiendapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: Contact)

    @Query("SELECT * FROM contacts ORDER BY id DESC")
    fun getAllContacts(): Flow<List<Contact>>

    @Query("SELECT * FROM contacts WHERE id = :id")
    suspend fun getContactById(id: Int): Contact?

    @Delete
    suspend fun deleteContact(contact: Contact)

    @Query("DELETE FROM contacts")
    suspend fun deleteAllContacts()

    @Query("SELECT COUNT(*) FROM contacts")
    suspend fun getContactCount(): Int
}
```

### AppDatabase.kt

```kotlin
package duoc.desarrollomobile.tiendapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Contact::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tiendapp_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
```

### Region.kt

```kotlin
package duoc.desarrollomobile.tiendapp.model

import kotlinx.serialization.Serializable

@Serializable
data class Region(
    val id: Int,
    val nombre: String
)
```

### ValidationUtils.kt

```kotlin
package duoc.desarrollomobile.tiendapp.utils

object ValidationUtils {

    fun isValidNombre(nombre: String): Boolean {
        if (nombre.isBlank()) return false
        if (nombre.length < 2 || nombre.length > 50) return false
        val nombreRegex = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ][a-zA-ZáéíóúÁÉÍÓÚñÑ ]*$".toRegex()
        return nombreRegex.matches(nombre)
    }

    fun isValidTelefono(telefono: String): Boolean {
        if (telefono.isBlank()) return false
        val telefonoRegex = "^(\\+56|56)?9[0-9]{8}$".toRegex()
        return telefonoRegex.matches(telefono)
    }

    fun isValidEmail(email: String): Boolean {
        if (email.isBlank()) return false
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return emailRegex.matches(email)
    }

    fun isValidRegion(region: String): Boolean {
        return region.isNotBlank()
    }

    fun isValidMensaje(mensaje: String): Boolean {
        return mensaje.isNotBlank() && mensaje.length <= 200
    }

    fun getNombreErrorMessage(nombre: String): String? {
        return when {
            nombre.isBlank() -> "El nombre es requerido"
            nombre.length < 2 -> "El nombre debe tener al menos 2 caracteres"
            nombre.length > 50 -> "El nombre no puede exceder 50 caracteres"
            !isValidNombre(nombre) -> "El nombre solo puede contener letras y espacios"
            else -> null
        }
    }

    fun getTelefonoErrorMessage(telefono: String): String? {
        return when {
            telefono.isBlank() -> "El teléfono es requerido"
            !isValidTelefono(telefono) -> "Formato inválido. Use: +56912345678 o 912345678"
            else -> null
        }
    }

    fun getEmailErrorMessage(email: String): String? {
        return when {
            email.isBlank() -> "El correo electrónico es requerido"
            !isValidEmail(email) -> "Correo electrónico inválido"
            else -> null
        }
    }

    fun getRegionErrorMessage(region: String): String? {
        return when {
            region.isBlank() -> "Debe seleccionar una región"
            else -> null
        }
    }

    fun getMensajeErrorMessage(mensaje: String): String? {
        return when {
            mensaje.isBlank() -> "El mensaje es requerido"
            mensaje.length > 200 -> "El mensaje no puede exceder 200 caracteres"
            else -> null
        }
    }
}
```

### ContactRepository.kt

```kotlin
package duoc.desarrollomobile.tiendapp.model

import android.content.Context
import duoc.desarrollomobile.tiendapp.data.Contact
import duoc.desarrollomobile.tiendapp.data.ContactDao
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json
import java.io.IOException

class ContactRepository(
    private val contactDao: ContactDao,
    private val context: Context
) {
    val allContacts: Flow<List<Contact>> = contactDao.getAllContacts()

    suspend fun insertContact(contact: Contact) {
        contactDao.insertContact(contact)
    }

    suspend fun getContactById(id: Int): Contact? {
        return contactDao.getContactById(id)
    }

    suspend fun deleteContact(contact: Contact) {
        contactDao.deleteContact(contact)
    }

    suspend fun deleteAllContacts() {
        contactDao.deleteAllContacts()
    }

    suspend fun getContactCount(): Int {
        return contactDao.getContactCount()
    }

    fun loadRegiones(): List<Region> {
        return try {
            val jsonString = context.assets.open("regiones.json")
                .bufferedReader()
                .use { it.readText() }

            Json.decodeFromString<List<Region>>(jsonString)

        } catch (e: IOException) {
            e.printStackTrace()
            emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
```

### regiones.json

```json
[
  {"id": 1, "nombre": "Arica y Parinacota"},
  {"id": 2, "nombre": "Tarapacá"},
  {"id": 3, "nombre": "Antofagasta"},
  {"id": 4, "nombre": "Atacama"},
  {"id": 5, "nombre": "Coquimbo"},
  {"id": 6, "nombre": "Valparaíso"},
  {"id": 7, "nombre": "Metropolitana de Santiago"},
  {"id": 8, "nombre": "Libertador General Bernardo O'Higgins"},
  {"id": 9, "nombre": "Maule"},
  {"id": 10, "nombre": "Ñuble"},
  {"id": 11, "nombre": "Biobío"},
  {"id": 12, "nombre": "La Araucanía"},
  {"id": 13, "nombre": "Los Ríos"},
  {"id": 14, "nombre": "Los Lagos"},
  {"id": 15, "nombre": "Aysén del General Carlos Ibáñez del Campo"},
  {"id": 16, "nombre": "Magallanes y de la Antártica Chilena"}
]
```

---
