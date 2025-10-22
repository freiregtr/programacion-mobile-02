# Inspección de Base de Datos SQLite (OPCIONAL)

Este documento es **opcional** y muestra cómo inspeccionar la base de datos SQLite creada con Room para verificar que los contactos se están guardando correctamente.

---

## ¿Por qué inspeccionar la base de datos?

Inspeccionar la base de datos es útil para:
- Verificar que los datos se guardan correctamente
- Debuggear problemas de persistencia
- Aprender cómo funciona SQLite internamente
- Validar que las queries de Room funcionan como se espera

---

## Método 1: Database Inspector (Recomendado)

**Database Inspector** es una herramienta integrada en Android Studio que permite ver y modificar la base de datos en tiempo real.

### Paso 1: Ejecutar la app

1. **Ejecuta la app** en un emulador o dispositivo físico
2. **Guarda al menos un contacto** usando el formulario
3. **Deja la app en ejecución** (no la cierres)

### Paso 2: Abrir Database Inspector

**En Android Studio:**

1. Menú: `View` → `Tool Windows` → `App Inspection`
2. En la pestaña inferior, selecciona **"Database Inspector"**
3. Espera a que cargue (puede tardar unos segundos)

### Paso 3: Ver la tabla de contactos

**En Database Inspector:**

1. En el panel izquierdo, verás tu app: `duoc.desarrollomobile.tiendapp`
2. Expande el árbol: `duoc.desarrollomobile.tiendapp` → `contact_database`
3. Verás la tabla: `contacts`
4. **Click en `contacts`** para ver todos los registros

### ¿Qué verás en la tabla?

| Columna | Tipo | Descripción |
|---------|------|-------------|
| `id` | INTEGER | ID autoincremental (Primary Key) |
| `nombre` | TEXT | Nombre del contacto |
| `telefono` | TEXT | Teléfono formato chileno |
| `correo` | TEXT | Email del contacto |
| `region` | TEXT | Región seleccionada |
| `mensaje` | TEXT | Mensaje del contacto (max 200 chars) |
| `fecha` | INTEGER | Timestamp de cuando se guardó |

### Características de Database Inspector

**Vista en tiempo real:**
- Guarda un nuevo contacto en la app
- La tabla se actualiza automáticamente
- No necesitas refrescar manualmente

**Editar datos:**
- Doble click en cualquier celda
- Modifica el valor
- Presiona Enter para guardar

**Filtrar datos:**
- Usa el campo de búsqueda arriba de la tabla
- Filtra por cualquier columna

**Refrescar:**
- Si no se actualiza automáticamente, usa el botón de refresh

---

## Método 2: Ejecutar Queries SQL

Database Inspector permite ejecutar consultas SQL personalizadas.

### Abrir el editor de queries

1. En Database Inspector, con la base de datos `contact_database` seleccionada
2. Click en la pestaña **"Query"** (arriba a la derecha)
3. Escribe tu consulta SQL

### Queries útiles

**Ver todos los contactos:**
```sql
SELECT * FROM contacts;
```

**Ver solo nombre y región:**
```sql
SELECT nombre, region FROM contacts;
```

**Contar contactos por región:**
```sql
SELECT region, COUNT(*) as total
FROM contacts
GROUP BY region
ORDER BY total DESC;
```

**Ver los últimos 5 contactos guardados:**
```sql
SELECT * FROM contacts
ORDER BY fecha DESC
LIMIT 5;
```

**Buscar contactos de una región específica:**
```sql
SELECT * FROM contacts
WHERE region = 'Metropolitana de Santiago';
```

**Buscar contactos por parte del nombre:**
```sql
SELECT * FROM contacts
WHERE nombre LIKE '%Juan%';
```

**Ver contactos guardados hoy:**
```sql
SELECT * FROM contacts
WHERE fecha > strftime('%s', 'now', 'start of day') * 1000;
```

**Eliminar todos los contactos (CUIDADO):**
```sql
DELETE FROM contacts;
```

**Nota:** Después de ejecutar una query, presiona el botón **"Run"** o usa `Ctrl+Enter`.

---

## Método 3: Device File Explorer

Si necesitas exportar el archivo de base de datos para analizarlo externamente.

### Paso 1: Abrir Device File Explorer

**En Android Studio:**

1. Menú: `View` → `Tool Windows` → `Device File Explorer`
2. Se abrirá un panel lateral con el sistema de archivos del dispositivo

### Paso 2: Navegar al archivo de base de datos

**Ruta:**
```
data/data/duoc.desarrollomobile.tiendapp/databases/contact_database
```

**Pasos:**
1. Expande la carpeta `data`
2. Expande la segunda carpeta `data`
3. Busca y expande `duoc.desarrollomobile.tiendapp`
4. Expande la carpeta `databases`
5. Verás el archivo `contact_database`

### Paso 3: Exportar el archivo

1. Click derecho en `contact_database`
2. Selecciona **"Save As..."**
3. Guarda el archivo en tu computadora

### Paso 4: Abrir con herramientas externas

Una vez exportado, puedes abrirlo con:

**DB Browser for SQLite (Recomendado):**
- Descargar: https://sqlitebrowser.org/
- Gratis, open source, multiplataforma
- Interfaz gráfica intuitiva

**SQLiteStudio:**
- Descargar: https://sqlitestudio.pl/
- Gratis, portable
- Soporta plugins

**DBeaver:**
- Descargar: https://dbeaver.io/
- Universal database tool
- Más pesado pero muy completo

---

## Método 4: Usando ADB (Línea de comandos)

Para usuarios avanzados que prefieren la terminal.

### Conectarse a la base de datos

```bash
# Conectar al dispositivo/emulador
adb shell

# Ir al directorio de la app
cd /data/data/duoc.desarrollomobile.tiendapp/databases/

# Abrir SQLite
sqlite3 contact_database
```

### Ejecutar comandos SQLite

```sql
-- Ver todas las tablas
.tables

-- Ver estructura de la tabla contacts
.schema contacts

-- Ver todos los contactos
SELECT * FROM contacts;

-- Salir
.quit
```

### Exportar la base de datos con ADB

```bash
# Exportar desde el dispositivo a tu computadora
adb pull /data/data/duoc.desarrollomobile.tiendapp/databases/contact_database ./contact_database.db

# Ahora puedes abrirlo con cualquier herramienta SQLite
```

**Nota:** Este método requiere que el dispositivo esté en modo depuración o que uses un emulador.

---

## Troubleshooting

### No veo la base de datos en Database Inspector

**Posibles causas:**
1. La app no está en ejecución
2. El dispositivo no está conectado
3. La app no está en modo debug

**Solución:**
- Asegúrate de ejecutar la app en modo Debug
- Espera unos segundos a que cargue
- Reinicia Android Studio si es necesario

### "Permission denied" al acceder con Device File Explorer

**Causa:**
Los archivos en `/data/data/` están protegidos en dispositivos físicos no rooteados.

**Soluciones:**
- Usa Database Inspector en su lugar (no requiere acceso al sistema de archivos)
- Usa un emulador (no tiene restricciones)
- Usa un dispositivo rooteado

### La tabla está vacía

**Posibles causas:**
1. No has guardado ningún contacto todavía
2. Hubo un error al guardar (revisa Logcat)
3. Estás viendo una instancia diferente de la app

**Solución:**
- Guarda un contacto usando el formulario
- Verifica que aparezca el Snackbar de "Contacto guardado exitosamente"
- Refresca la vista en Database Inspector

### Los datos no se actualizan en tiempo real

**Solución:**
- Click en el botón de **Refresh** (icono de flechas circulares)
- O cierra y vuelve a abrir Database Inspector

---

## Ver logs de Room Database

Para ver las queries SQL que Room ejecuta automáticamente:

### Habilitar logs de Room

Agrega esto en tu `AppDatabase.kt`:

```kotlin
companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "contact_database"
            )
                .setQueryCallback({ sqlQuery, bindArgs ->
                    // Imprimir queries en Logcat
                    android.util.Log.d("RoomDatabase", "Query: $sqlQuery | Args: $bindArgs")
                }, {})
                .build()
            INSTANCE = instance
            instance
        }
    }
}
```

### Ver en Logcat

1. Abre **Logcat**: `View` → `Tool Windows` → `Logcat`
2. En el filtro, escribe: `RoomDatabase`
3. Ejecuta acciones en la app (guardar contacto)
4. Verás las queries SQL que Room ejecuta

**Ejemplo de log:**
```
D/RoomDatabase: Query: INSERT INTO contacts (nombre, telefono, correo, region, mensaje, fecha) VALUES (?, ?, ?, ?, ?, ?) | Args: [Juan Pérez, +56912345678, juan@correo.com, Metropolitana de Santiago, Hola!, 1729620000000]
```

---

## Ejercicios prácticos

### Ejercicio 1: Verificar datos

1. Guarda 3 contactos diferentes con regiones distintas
2. Abre Database Inspector
3. Verifica que los 3 aparezcan en la tabla
4. Confirma que todos los campos están correctos

### Ejercicio 2: Consultas SQL

1. Ejecuta una query para contar cuántos contactos hay
2. Ejecuta una query para ver solo los nombres
3. Filtra contactos por una región específica

### Ejercicio 3: Modificar datos

1. Abre Database Inspector
2. Edita el nombre de un contacto directamente en la tabla
3. Vuelve a la app y verifica si el cambio se refleja

### Ejercicio 4: Exportar y analizar

1. Exporta la base de datos usando Device File Explorer
2. Ábrela con DB Browser for SQLite
3. Explora la estructura de la tabla
4. Ejecuta queries personalizadas

---

## Resumen

**Mejor método para principiantes:** Database Inspector
- No requiere instalación adicional
- Integrado en Android Studio
- Vista en tiempo real
- Fácil de usar

**Mejor método para desarrollo:** Database Inspector + Logcat
- Database Inspector para ver datos
- Logcat para ver queries SQL
- Debugging completo

**Mejor método para análisis profundo:** Exportar con Device File Explorer
- Abre con herramientas externas
- Queries complejas
- Análisis de rendimiento

---

## Recursos adicionales

**Documentación oficial:**
- [Android Database Inspector](https://developer.android.com/studio/inspect/database)
- [Room Database](https://developer.android.com/training/data-storage/room)
- [SQLite Syntax](https://www.sqlite.org/lang.html)

**Herramientas:**
- [DB Browser for SQLite](https://sqlitebrowser.org/)
- [SQLiteStudio](https://sqlitestudio.pl/)
- [DBeaver](https://dbeaver.io/)

**Tutoriales:**
- [SQLite Tutorial](https://www.sqlitetutorial.net/)
- [SQL básico](https://www.w3schools.com/sql/)

---
