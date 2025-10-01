# tutorial 05: ver texto completo y agregar icono

tiempo estimado: 15 minutos

## parte 1: ver texto completo cuando es largo

### por que esto importa

cuando escribes una tarea larga, no cabe completa en la tarjeta. twitter corta tweets largos con ..., reddit corta posts largos, youtube corta descripciones largas. el 85% de apps usan este patron: mostrar preview corto y expandir al hacer click.

## paso 1: limitar lineas y agregar clickable

### agregar imports necesarios

1. abre MainActivity.kt
2. despues de los imports existentes agrega:

```kotlin
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.style.TextOverflow
```

### agregar estado para el dialog de texto

para que sirve textdialogstatus: controla si el dialog que muestra el texto completo esta visible o no. cuando el usuario hace click en una tarea larga, este dialog se abre y muestra todo el texto sin limites.

3. dentro de mainpage, despues de las variables de estado existentes (despues de deletedialogstatus) agrega:

```kotlin
// controla si el dialog de texto completo esta visible
val textDialogStatus = remember {
    mutableStateOf(false)
}
```

### modificar el text de la tarea

que hacen estos parametros nuevos:
- maxlines 2: limita el texto a solo 2 lineas. si el texto tiene mas de 2 lineas, corta el resto
- overflow textoverflow.ellipsis: cuando el texto se corta, agrega tres puntos ... al final para indicar que hay mas texto
- clickable: hace que puedas hacer click en el texto. cuando clickeas, ejecuta el codigo dentro de las llaves

por que hacemos esto: si una tarea dice "comprar leche, pan, huevos, mantequilla, queso, jamon y cereales", ocuparia mucho espacio en la lista. mejor mostrar "comprar leche, pan, huevos..." y al hacer click mostrar todo.

4. busca el Text dentro del Row de la Card (donde esta el texto de la tarea)

codigo antes (asi esta ahora):
```kotlin
Text(
    text = item,
    color = Color.White,
    fontSize = 18.sp,
    modifier = Modifier.weight(1f)
)
```

5. reemplaza el Text por este:

codigo despues (asi debe quedar):
```kotlin
Text(
    text = item,
    color = Color.White,
    fontSize = 18.sp,
    // maxlines limita el texto a 2 lineas maximo
    maxLines = 2,
    // overflow.ellipsis agrega ... al final si el texto es largo
    overflow = TextOverflow.Ellipsis,
    modifier = Modifier
        .weight(1f)
        // clickable permite hacer click en el texto
        .clickable {
            // guarda el texto completo
            clickedItem.value = item
            // muestra el dialog
            textDialogStatus.value = true
        }
)
```

## paso 2: crear dialog para mostrar texto completo

### agregar el dialog despues del dialog de eliminar

diferencia con los otros dialogs: este dialog es mas simple. no tiene boton de cancelar, solo un boton de cerrar. tampoco tiene textfield ni pregunta de confirmacion, solo muestra el texto completo de la tarea.

6. busca el cierre del if del deleteDialogStatus (el dialog de eliminar que agregamos en el tutorial anterior)
7. despues del cierre de ese if y antes del cierre del Column agrega:

codigo antes (asi esta ahora):
```kotlin
Column(modifier = Modifier.fillMaxSize()) {
    // ... row con textfield y boton

    // ... lazycolumn con lista de tareas

    // dialog para editar
    if (updateDialogStatus.value) {
        // ...
    }

    // dialog para eliminar
    if (deleteDialogStatus.value) {
        // ...
    }

} // aqui cierra el column
```

codigo despues (asi debe quedar):
```kotlin
Column(modifier = Modifier.fillMaxSize()) {
    // ... row con textfield y boton

    // ... lazycolumn con lista de tareas

    // dialog para editar
    if (updateDialogStatus.value) {
        // ...
    }

    // dialog para eliminar
    if (deleteDialogStatus.value) {
        // ...
    }

    // dialog para mostrar texto completo
    // solo se muestra si textdialogstatus es true
    if (textDialogStatus.value) {
        AlertDialog(
            // se ejecuta cuando presionas fuera del dialog
            onDismissRequest = { textDialogStatus.value = false },
            // titulo del dialog
            title = {
                Text(text = "tarea completa")
            },
            // muestra el texto completo de la tarea sin limites
            text = {
                Text(text = clickedItem.value)
            },
            // solo tiene boton de cerrar, no necesita cancelar
            confirmButton = {
                TextButton(
                    onClick = {
                        // cierra el dialog
                        textDialogStatus.value = false
                    }
                ) {
                    Text(text = "cerrar")
                }
            }
        )
    }

} // aqui cierra el column
```

### verificar que compile

8. presiona ctrl + f9 para compilar
9. ejecuta la app con shift + f10

### que debes ver

cuando agregues una tarea larga, se muestra solo 2 lineas con ... al final. al hacer click en el texto se abre un dialog que muestra todo el texto completo.

## parte 2: agregar icono personalizado a la app

### por que esto importa

todas las apps profesionales tienen icono personalizado. tu app actualmente usa el icono generico de android. el 100% de apps en google play tienen icono personalizado porque es lo primero que ven los usuarios.

## paso 1: preparar imagen para el icono

### requisitos de la imagen

1. busca o crea una imagen cuadrada
2. puede ser png, jpg o cualquier formato
3. idealmente 512x512 pixeles o mas
4. fondo solido o transparente

## paso 2: generar iconos con android studio

### usar image asset studio

1. click derecho en la carpeta `res`
2. selecciona new > image asset
3. en la ventana que se abre:
   - icon type: launcher icons
   - name: ic_launcher
   - asset type: image
   - path: click en el icono de carpeta y selecciona tu imagen
4. ajusta el tamaño con el slider trim y resize
5. puedes cambiar shape: circle, square, rounded square
6. background layer: selecciona color de fondo si quieres
7. click en next
8. click en finish

android studio genera automaticamente todos los tamaños necesarios en las carpetas mipmap.

### verificar los archivos generados

9. verifica que se crearon archivos en estas carpetas:
   - mipmap-mdpi
   - mipmap-hdpi
   - mipmap-xhdpi
   - mipmap-xxhdpi
   - mipmap-xxxhdpi

### ejecutar la app

10. desinstala la app anterior del dispositivo o emulador
11. ejecuta la app con shift + f10
12. ahora tu app tiene icono personalizado

### que debes ver

en la lista de apps del telefono, tu app ya no tiene el icono verde de android. ahora tiene tu icono personalizado.

## problemas comunes

### problema 1: el texto no muestra ...

solucion:
1. verifica que maxLines = 2
2. verifica que overflow = TextOverflow.Ellipsis
3. escribe una tarea bien larga para probarlo

### problema 2: no se abre el dialog al hacer click

solucion:
1. verifica que el modifier tenga .clickable
2. verifica que textDialogStatus.value = true este dentro del clickable

### problema 3: new image asset no aparece

solucion:
1. click derecho especificamente en la carpeta res
2. si no aparece, usa file > new > image asset

### problema 4: el icono no cambia

solucion:
1. desinstala completamente la app anterior
2. vuelve a instalar desde android studio
3. el sistema operativo cachea los iconos

### problema 5: la imagen se ve mal

solucion:
1. usa imagen cuadrada, no rectangular
2. ajusta con el slider de resize
3. prueba con shape rounded square que es mas moderno

## comparacion de usabilidad

## codigo completo final

mainactivity.kt:

```kotlin
package duoc.desarrollomobile.todoproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
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
    // variable que guarda el texto que el usuario escribe
    val todoName = remember {
        mutableStateOf("")
    }

    // obtiene el context de android para acceder al sistema de archivos
    val context = LocalContext.current
    // lee todas las tareas guardadas del archivo
    val itemList = readData(context)

    // controla si el dialog de edicion esta visible
    val updateDialogStatus = remember {
        mutableStateOf(false)
    }

    // guarda la posicion de la tarea en la lista
    val clickedItemIndex = remember {
        mutableStateOf(0)
    }

    // guarda el texto de la tarea que estas editando
    val clickedItem = remember {
        mutableStateOf("")
    }

    // controla si el dialog de eliminacion esta visible
    val deleteDialogStatus = remember {
        mutableStateOf(false)
    }

    // controla si el dialog de texto completo esta visible
    val textDialogStatus = remember {
        mutableStateOf(false)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // row organiza los elementos en horizontal: textfield a la izquierda, boton a la derecha
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // textfield es el campo donde el usuario escribe la tarea
            TextField(
                value = todoName.value,
                onValueChange = { todoName.value = it },
                label = { Text(text = "ingresa tu tarea") },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // boton para agregar la tarea a la lista
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

        // lazycolumn crea una lista vertical con scroll
        LazyColumn {
            items(count = itemList.size) { index ->
                val item = itemList[index]

                // card crea una tarjeta con fondo de color
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF6200EE)
                    )
                ) {
                    // row organiza texto a la izquierda y botones a la derecha
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // texto de la tarea con limite de 2 lineas
                        Text(
                            text = item,
                            color = Color.White,
                            fontSize = 18.sp,
                            // maxlines limita el texto a 2 lineas maximo
                            maxLines = 2,
                            // overflow.ellipsis agrega ... al final si el texto es largo
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .weight(1f)
                                // clickable permite hacer click en el texto
                                .clickable {
                                    // guarda el texto completo
                                    clickedItem.value = item
                                    // muestra el dialog de texto completo
                                    textDialogStatus.value = true
                                }
                        )

                        // boton con icono de lapiz para editar
                        IconButton(
                            onClick = {
                                updateDialogStatus.value = true
                                clickedItemIndex.value = index
                                clickedItem.value = item
                            }
                        ) {
                            Icon(
                                Icons.Filled.Edit,
                                contentDescription = "editar",
                                tint = Color.White
                            )
                        }

                        // boton con icono de basurero para eliminar
                        IconButton(
                            onClick = {
                                deleteDialogStatus.value = true
                                clickedItemIndex.value = index
                            }
                        ) {
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription = "eliminar",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }

        // dialog para editar la tarea
        if (updateDialogStatus.value) {
            AlertDialog(
                onDismissRequest = { updateDialogStatus.value = false },
                title = {
                    Text(text = "actualizar tarea")
                },
                text = {
                    TextField(
                        value = clickedItem.value,
                        onValueChange = { clickedItem.value = it }
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            itemList[clickedItemIndex.value] = clickedItem.value
                            writeData(itemList, context)
                            updateDialogStatus.value = false
                        }
                    ) {
                        Text(text = "guardar")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { updateDialogStatus.value = false }
                    ) {
                        Text(text = "cancelar")
                    }
                }
            )
        }

        // dialog para confirmar eliminacion
        if (deleteDialogStatus.value) {
            AlertDialog(
                onDismissRequest = { deleteDialogStatus.value = false },
                title = {
                    Text(text = "eliminar tarea")
                },
                text = {
                    Text(text = "estas seguro que quieres eliminar esta tarea?")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            itemList.removeAt(clickedItemIndex.value)
                            writeData(itemList, context)
                            deleteDialogStatus.value = false
                        }
                    ) {
                        Text(text = "si")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { deleteDialogStatus.value = false }
                    ) {
                        Text(text = "no")
                    }
                }
            )
        }

        // dialog para mostrar texto completo
        if (textDialogStatus.value) {
            AlertDialog(
                onDismissRequest = { textDialogStatus.value = false },
                title = {
                    Text(text = "tarea completa")
                },
                text = {
                    Text(text = clickedItem.value)
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            textDialogStatus.value = false
                        }
                    ) {
                        Text(text = "cerrar")
                    }
                }
            )
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

// nombre del archivo donde se guardan las tareas
const val FILE_NAME = "todolist.dat"

// funcion para guardar la lista de tareas en el archivo
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

// funcion para leer la lista de tareas del archivo
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

---

checkpoint 07 completado

## felicitaciones

completaste una app funcional de tareas con:
- agregar tareas
- listar tareas con persistencia
- editar tareas existentes
- eliminar tareas con confirmacion
- ver texto completo de tareas largas
- icono personalizado

esta app usa las mismas tecnicas que apps profesionales como google keep, todoist y any.do que tienen millones de usuarios.
