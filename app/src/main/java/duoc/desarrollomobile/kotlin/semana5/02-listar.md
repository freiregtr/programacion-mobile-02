# tutorial 02: listar tareas con lazycolumn

tiempo estimado: 15 minutos

## por que esto importa

gmail muestra emails en lista, instagram muestra posts en lista, twitter muestra tweets en lista. el 95% de las apps usan listas para mostrar contenido porque el scroll vertical es natural para los usuarios.

lazycolumn es el equivalente de recyclerview en compose pero con 70% menos codigo.

## paso 1: crear el archivo filehelper.kt

necesitamos guardar las tareas en un archivo de texto para que no se pierdan cuando cierres la app.

1. click derecho en la carpeta `duoc.desarrollomobile.todoproject`
2. selecciona new > kotlin class/file
3. escribe `FileHelper`
4. presiona enter

### agregar imports

5. abre FileHelper.kt
6. despues de `package duoc.desarrollomobile.todoproject` agrega:

```kotlin
import android.content.Context
import androidx.compose.runtime.snapshots.SnapshotStateList
import java.io.FileNotFoundException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
```

### crear constante para nombre del archivo

para que sirve: necesitamos guardar las tareas en un archivo en el celular. este archivo se llama "todolist.dat" y vive en la memoria interna de la app. cuando escribas una tarea y cierres la app, las tareas siguen ahi porque estan guardadas en este archivo.

usamos una constante para no tener que escribir "todolist.dat" muchas veces. si despues quieres cambiar el nombre del archivo, solo cambias este valor y listo.

7. despues de los imports agrega:

```kotlin
const val FILE_NAME = "todolist.dat"
```

### crear funcion para guardar tareas

por que snapshotstatelist: es una lista especial de compose que avisa automaticamente cuando agregas o quitas elementos. cuando cambias la lista, compose ve el cambio y actualiza la pantalla al instante. es como mutablestateof pero para listas enteras.

usamos arraylist adentro porque objectoutputstream no sabe como guardar snapshotstatelist directamente. entonces convertimos a arraylist normal, guardamos, y listo.

8. despues de la constante agrega:

```kotlin
fun writeData(items: SnapshotStateList<String>, context: Context) {
    // fos significa file output stream: abre el archivo para escribir
    val fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
    // oas significa object output stream: convierte objetos a bytes para guardarlos
    val oas = ObjectOutputStream(fos)
    // crea una lista normal porque objectoutputstream no entiende snapshotstatelist
    val itemList = ArrayList<String>()
    // copia todas las tareas a la lista normal
    itemList.addAll(items)
    // guarda la lista en el archivo
    oas.writeObject(itemList)
    // cierra el archivo
    oas.close()
}
```

### crear funcion para leer tareas

para que sirve: cuando abres la app, necesitamos leer el archivo donde guardamos las tareas y mostrarlas en la pantalla. esta funcion busca el archivo, lee todas las tareas y las devuelve en una snapshotstatelist.

por que el try catch: la primera vez que abres la app, el archivo todavia no existe porque no has agregado ninguna tarea. sin el try catch, la app crashearia. con el try catch, si el archivo no existe, simplemente devolvemos una lista vacia.

9. despues de la funcion writeData agrega:

```kotlin
fun readData(context: Context): SnapshotStateList<String> {
    // variable donde guardaremos las tareas leidas del archivo
    var itemList: ArrayList<String>

    try {
        // fis significa file input stream: abre el archivo para leer
        val fis = context.openFileInput(FILE_NAME)
        // ois significa object input stream: convierte bytes a objetos
        val ois = ObjectInputStream(fis)
        // lee el archivo y lo convierte a arraylist de strings
        itemList = ois.readObject() as ArrayList<String>
    } catch (e: FileNotFoundException) {
        // si el archivo no existe, crea una lista vacia
        itemList = ArrayList()
    }

    // crea una snapshotstatelist vacia
    val items = SnapshotStateList<String>()
    // copia todas las tareas del arraylist a la snapshotstatelist
    items.addAll(itemList)

    // devuelve la lista para usarla en compose
    return items
}
```

## paso 2: agregar lazycolumn a mainpage

### agregar imports necesarios

1. abre MainActivity.kt
2. despues de los imports existentes agrega:

```kotlin
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
```

### crear lista de tareas

que es context: context es como la identificacion de tu app en android. necesitas el context para acceder a cosas del sistema como archivos, base de datos, preferencias, etc. localcontext.current te da el context actual de donde estas.

para que sirve itemlist: esta es la lista de todas las tareas. cuando abres la app, readdata busca el archivo y carga todas las tareas guardadas. esta lista es especial porque cuando le agregas o quitas algo, compose actualiza la pantalla automaticamente.

3. dentro de mainpage, despues de `val todoName` agrega:

```kotlin
// obtiene el context de android para acceder al sistema de archivos
val context = LocalContext.current
// lee todas las tareas guardadas del archivo
val itemList = readData(context)
```

### modificar el onclick del boton agregar

por que cambiamos el onclick: antes solo borraba el texto. ahora necesitamos agregar la tarea a la lista y guardarla en el archivo para que no se pierda.

4. busca el boton que dice "agregar" que creamos en el tutorial anterior

codigo antes (asi esta ahora):
```kotlin
Button(
    onClick = {
        if (todoName.value.isNotEmpty()) {
            todoName.value = ""
        }
    },
    modifier = Modifier.height(56.dp)
) {
    Text(text = "agregar")
}
```

5. reemplaza todo el contenido del onclick por esto:

codigo despues (asi debe quedar):
```kotlin
Button(
    onClick = {
        // verifica que el texto no este vacio
        if (todoName.value.isNotEmpty()) {
            // agrega la tarea a la lista
            itemList.add(todoName.value)
            // guarda la lista en el archivo
            writeData(itemList, context)
            // borra el texto del textfield
            todoName.value = ""
        }
    },
    modifier = Modifier.height(56.dp)
) {
    Text(text = "agregar")
}
```

ahora cuando presiones agregar, la tarea se guarda en la lista y en el archivo.

### agregar column para organizar

por que column: necesitamos poner dos cosas en vertical: el row del textfield arriba y la lazycolumn con las tareas abajo. column organiza elementos en vertical, uno arriba del otro.

6. busca el Row que contiene el textfield
7. antes del Row agrega `Column(modifier = Modifier.fillMaxSize()) {`
8. al final de mainpage, despues del Row, agrega la llave de cierre `}`

### agregar lazycolumn para mostrar tareas

que es lazycolumn: es una lista vertical con scroll automatico. se llama lazy porque solo dibuja las tareas que ves en la pantalla, no todas. si tienes 1000 tareas, solo dibuja las 10 que caben en la pantalla. cuando haces scroll, va dibujando las siguientes. esto hace que la app sea rapida sin importar cuantas tareas tengas.

para que nos sirve: necesitamos mostrar todas las tareas una debajo de la otra. lazycolumn hace exactamente eso: toma la lista de tareas y las muestra en orden, con scroll automatico.

9. despues del Row con el boton y antes de la llave de cierre del Column agrega el lazycolumn:

codigo antes (asi esta ahora):
```kotlin
Column(modifier = Modifier.fillMaxSize()) {
    Row(
        // ... todo el row con textfield y boton
    ) {
        // ...
    }

} // aqui cierra el column
```

codigo despues (asi debe quedar):
```kotlin
Column(modifier = Modifier.fillMaxSize()) {
    Row(
        // ... todo el row con textfield y boton
    ) {
        // ...
    }

    // lazycolumn crea una lista vertical con scroll
    LazyColumn {
        // items recorre cada tarea de la lista
        items(count = itemList.size) { index ->
            // obtiene la tarea actual
            val item = itemList[index]

            // card crea una tarjeta con fondo de color
            Card(
                modifier = Modifier
                    // fillmaxwidth hace que la tarjeta ocupe todo el ancho
                    .fillMaxWidth()
                    // padding agrega espacio entre tarjetas
                    .padding(8.dp),
                colors = CardDefaults.cardColors(
                    // color morado para el fondo de la tarjeta
                    containerColor = Color(0xFF6200EE)
                )
            ) {
                // texto que muestra la tarea
                Text(
                    text = item,
                    modifier = Modifier.padding(16.dp),
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }
    }

} // aqui cierra el column
```

### verificar que compile

10. presiona ctrl + f9 para compilar
11. ejecuta la app con shift + f10

### que debes ver

cuando agregues una tarea, debe aparecer abajo en una tarjeta morada. si cierras la app y la vuelves a abrir, las tareas siguen ahi.

## problemas comunes

### problema 1: cannot resolve symbol snapshotstatelist

solucion:
1. verifica el import androidx.compose.runtime.snapshots.SnapshotStateList
2. sync project with gradle files

### problema 2: las tareas no se guardan

solucion:
1. verifica que llames a writeData despues de agregar a itemList
2. verifica que el context sea correcto

### problema 3: la app crashea al abrir

solucion:
1. verifica que el try catch este en readData
2. el archivo no existe la primera vez y es normal

### problema 4: el textfield queda debajo de la lista

solucion:
1. verifica que el Row este dentro del Column
2. verifica que el LazyColumn este despues del Row

## codigo completo hasta ahora

mainactivity.kt:

```kotlin
package duoc.desarrollomobile.todoproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import duoc.desarrollomobile.todoproject.ui.theme.TODOProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TODOProjectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainPage(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainPage(modifier: Modifier = Modifier) {
    val todoName = remember {
        mutableStateOf("")
    }

    val context = LocalContext.current
    val itemList = readData(context)

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            TextField(
                value = todoName.value,
                onValueChange = { todoName.value = it },
                label = { Text(text = "ingresa tu tarea") },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if (todoName.value.isNotEmpty()) {
                        itemList.add(todoName.value)
                        writeData(itemList, context)
                        todoName.value = ""
                    }
                },
                modifier = Modifier.height(56.dp)
            ) {
                Text(text = "agregar")
            }
        }

        LazyColumn {
            items(count = itemList.size) { index ->
                val item = itemList[index]

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF6200EE)
                    )
                ) {
                    Text(
                        text = item,
                        modifier = Modifier.padding(16.dp),
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}
```

filehelper.kt:

```kotlin
package duoc.desarrollomobile.todoproject

import android.content.Context
import androidx.compose.runtime.snapshots.SnapshotStateList
import java.io.FileNotFoundException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

const val FILE_NAME = "todolist.dat"

fun writeData(items: SnapshotStateList<String>, context: Context) {
    val fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
    val oas = ObjectOutputStream(fos)
    val itemList = ArrayList<String>()
    itemList.addAll(items)
    oas.writeObject(itemList)
    oas.close()
}

fun readData(context: Context): SnapshotStateList<String> {
    var itemList: ArrayList<String>

    try {
        val fis = context.openFileInput(FILE_NAME)
        val ois = ObjectInputStream(fis)
        itemList = ois.readObject() as ArrayList<String>
    } catch (e: FileNotFoundException) {
        itemList = ArrayList()
    }

    val items = SnapshotStateList<String>()
    items.addAll(itemList)

    return items
}
```

---

checkpoint 04 completado
