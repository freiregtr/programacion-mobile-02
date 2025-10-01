# tutorial 04: eliminar tareas con confirmacion

tiempo estimado: 10 minutos

## por que esto importa

gmail pregunta si estas seguro antes de eliminar correos importantes. instagram confirma antes de borrar publicaciones. slack confirma antes de eliminar mensajes. el 92% de apps profesionales piden confirmacion porque evita borrados accidentales que generan frustracion en los usuarios.

## paso 1: agregar estado para el dialog de eliminacion

### agregar import necesario

1. abre MainActivity.kt
2. despues de los imports existentes agrega:

```kotlin
import androidx.compose.material.icons.filled.Delete
```

### agregar variable de estado

para que sirve deletedialogstatus: controla si el dialog de confirmacion para eliminar esta visible o no. true = se ve, false = esta oculto. es igual que updatedialogstatus pero para eliminar en vez de editar.

por que necesitamos otra variable: porque podemos tener dos dialogs abiertos al mismo tiempo en teoria, entonces cada dialog necesita su propia variable para saber si esta visible o no. en la practica solo uno puede estar visible a la vez.

3. dentro de mainpage, despues de las variables existentes de estado (despues de clickeditem) agrega:

```kotlin
// controla si el dialog de eliminacion esta visible
val deleteDialogStatus = remember {
    mutableStateOf(false)
}
```

## paso 2: agregar boton de eliminar en cada tarea

### modificar el row dentro de la card

por que agregar otro iconbutton: ya tenemos el boton de editar, ahora necesitamos el boton de eliminar al lado. los dos botones van a estar juntos a la derecha de cada tarea.

4. busca el Row dentro de la Card que tiene el icono de editar (lo agregamos en el tutorial anterior)

codigo antes (asi esta ahora):
```kotlin
Row(
    modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
) {
    Text(
        text = item,
        color = Color.White,
        fontSize = 18.sp,
        modifier = Modifier.weight(1f)
    )

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
}
```

5. despues del IconButton de editar y antes de que cierre el Row agrega:

codigo despues (asi debe quedar):
```kotlin
Row(
    modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
) {
    Text(
        text = item,
        color = Color.White,
        fontSize = 18.sp,
        modifier = Modifier.weight(1f)
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
            // muestra el dialog de confirmacion
            deleteDialogStatus.value = true
            // guarda cual tarea se presiono
            clickedItemIndex.value = index
        }
    ) {
        // icono de basurero
        Icon(
            Icons.Filled.Delete,
            contentDescription = "eliminar",
            tint = Color.White
        )
    }
}
```

## paso 3: crear el alertdialog de confirmacion

### agregar el dialog despues del dialog de editar

por que dialog de confirmacion: eliminar es permanente, no hay deshacer. si el usuario presiona el basurero por accidente, perderia la tarea para siempre. el dialog pregunta si estas seguro antes de borrar, evitando errores.

diferencia con el dialog de editar: el dialog de editar tiene un textfield para cambiar el texto. el dialog de eliminar solo tiene un mensaje de confirmacion y dos botones: si y no.

6. busca el cierre del if del updateDialogStatus (el dialog de editar que agregamos en el tutorial anterior)
7. despues del cierre de ese if y antes del cierre del Column agrega:

codigo antes (asi esta ahora):
```kotlin
Column(modifier = Modifier.fillMaxSize()) {
    // ... row con textfield y boton

    // ... lazycolumn con lista de tareas

    // dialog para editar
    if (updateDialogStatus.value) {
        AlertDialog(
            // ... dialog de editar
        )
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
        AlertDialog(
            // ... dialog de editar
        )
    }

    // dialog para confirmar eliminacion
    // solo se muestra si deletedialogstatus es true
    if (deleteDialogStatus.value) {
        AlertDialog(
            // se ejecuta cuando presionas fuera del dialog
            onDismissRequest = { deleteDialogStatus.value = false },
            // titulo del dialog
            title = {
                Text(text = "eliminar tarea")
            },
            // mensaje de confirmacion
            text = {
                Text(text = "estas seguro que quieres eliminar esta tarea?")
            },
            // boton de confirmar
            confirmButton = {
                TextButton(
                    onClick = {
                        // elimina la tarea de la lista usando su posicion
                        itemList.removeAt(clickedItemIndex.value)
                        // guarda la lista actualizada en el archivo
                        writeData(itemList, context)
                        // cierra el dialog
                        deleteDialogStatus.value = false
                    }
                ) {
                    Text(text = "si")
                }
            },
            // boton de cancelar
            dismissButton = {
                TextButton(
                    onClick = { deleteDialogStatus.value = false }
                ) {
                    Text(text = "no")
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

cada tarea ahora tiene dos iconos: lapiz para editar y basurero para eliminar. cuando presiones el basurero, aparece un dialog preguntando si estas seguro. al presionar si se elimina la tarea permanentemente.

## problemas comunes

### problema 1: cannot resolve symbol delete

solucion:
1. verifica el import androidx.compose.material.icons.filled.Delete
2. sync project with gradle files

### problema 2: se borran tareas incorrectas

solucion:
1. verifica que clickedItemIndex.value se actualice en el onclick del boton eliminar
2. verifica que uses clickedItemIndex.value en removeAt

### problema 3: la app crashea al eliminar

solucion:
1. verifica que el indice sea valido
2. verifica que llames a writeData despues de removeAt

### problema 4: los iconos estan muy juntos

solucion:
1. esto es normal por ahora
2. puedes agregar espaciado con Spacer si quieres

## codigo completo hasta ahora

```kotlin
package duoc.desarrollomobile.todoproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
                        // texto de la tarea
                        Text(
                            text = item,
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier.weight(1f)
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
                            // elimina la tarea de la lista
                            itemList.removeAt(clickedItemIndex.value)
                            // guarda la lista actualizada
                            writeData(itemList, context)
                            // cierra el dialog
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
    }
}
```

---

checkpoint 06 completado
