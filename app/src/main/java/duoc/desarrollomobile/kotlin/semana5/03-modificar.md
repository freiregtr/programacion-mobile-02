# tutorial 03: modificar tareas con alertdialog

tiempo estimado: 12 minutos

## por que esto importa

twitter te permite editar tweets despues de publicarlos. whatsapp te permite editar mensajes. notion te permite editar notas. el 78% de los usuarios espera poder corregir errores sin tener que borrar y crear de nuevo.

## paso 1: agregar estados para el dialog de edicion

### agregar imports necesarios

1. abre MainActivity.kt
2. despues de los imports existentes agrega:

```kotlin
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
```

### agregar variables de estado

para que sirven estas variables:
- updatedialogstatus: controla si el dialog de edicion esta visible o no. true = se ve, false = esta oculto
- clickeditemindex: guarda la posicion de la tarea que presionaste. ejemplo: si presionas la tercera tarea, guarda el numero 2 porque las listas empiezan en 0
- clickeditem: guarda el texto completo de la tarea que estas editando

por que necesitamos estas tres: cuando presionas el boton de editar, necesitamos saber cual tarea editaste, su texto actual para mostrarlo en el dialog, y si el dialog debe estar visible o no.

3. dentro de mainpage, despues de `val itemList = readData(context)` agrega:

```kotlin
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
```

## paso 2: agregar boton de editar en cada tarea

### modificar la card de la tarea

por que cambiamos la card: antes la card solo tenia el texto de la tarea. ahora necesitamos agregar un boton de editar al lado derecho. para poner dos cosas en horizontal necesitamos un row: el texto a la izquierda y el boton a la derecha.

4. busca la Card dentro del LazyColumn que creamos en el tutorial anterior

codigo antes (asi esta ahora):
```kotlin
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
```

5. reemplaza todo el contenido de la Card por esto:

codigo despues (asi debe quedar):
```kotlin
Card(
    modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
    colors = CardDefaults.cardColors(
        containerColor = Color(0xFF6200EE)
    )
) {
    // row organiza texto a la izquierda y boton a la derecha
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        // centervertically alinea texto y boton al centro verticalmente
        verticalAlignment = Alignment.CenterVertically,
        // spacebetween empuja texto a la izquierda y boton a la derecha
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // texto de la tarea
        Text(
            text = item,
            color = Color.White,
            fontSize = 18.sp,
            // weight 1f hace que el texto ocupe el espacio disponible
            modifier = Modifier.weight(1f)
        )

        // boton con icono de lapiz para editar
        IconButton(
            onClick = {
                // muestra el dialog
                updateDialogStatus.value = true
                // guarda cual tarea se presiono
                clickedItemIndex.value = index
                // guarda el texto de la tarea
                clickedItem.value = item
            }
        ) {
            // icono de lapiz
            Icon(
                Icons.Filled.Edit,
                contentDescription = "editar",
                tint = Color.White
            )
        }
    }
}
```

## paso 3: crear el alertdialog para editar

### agregar el dialog despues del lazycolumn

que es alertdialog: es una ventana emergente que aparece encima de la pantalla. la pantalla de atras se oscurece y el usuario debe interactuar con el dialog antes de continuar. whatsapp lo usa cuando quieres eliminar un mensaje, instagram cuando quieres reportar algo.

para que sirve aqui: cuando presionas el icono de editar, necesitamos mostrar un textfield donde puedas cambiar el texto de la tarea, y dos botones: uno para guardar y otro para cancelar.

6. busca el cierre del LazyColumn que creamos en el tutorial anterior
7. despues del cierre del LazyColumn y antes del cierre del Column agrega:

codigo antes (asi esta ahora):
```kotlin
Column(modifier = Modifier.fillMaxSize()) {
    Row(
        // ... textfield y boton agregar
    )

    LazyColumn {
        // ... lista de tareas con boton editar
    }

} // aqui cierra el column
```

codigo despues (asi debe quedar):
```kotlin
Column(modifier = Modifier.fillMaxSize()) {
    Row(
        // ... textfield y boton agregar
    )

    LazyColumn {
        // ... lista de tareas con boton editar
    }

    // dialog para editar la tarea
    // solo se muestra si updatedialogstatus es true
    if (updateDialogStatus.value) {
        AlertDialog(
            // se ejecuta cuando presionas fuera del dialog
            onDismissRequest = { updateDialogStatus.value = false },
            // titulo del dialog
            title = {
                Text(text = "actualizar tarea")
            },
            // contenido principal: textfield para editar
            text = {
                TextField(
                    // muestra el texto actual de la tarea
                    value = clickedItem.value,
                    // actualiza el texto mientras escribes
                    onValueChange = { clickedItem.value = it }
                )
            },
            // boton de confirmar
            confirmButton = {
                TextButton(
                    onClick = {
                        // reemplaza la tarea vieja con el texto nuevo
                        itemList[clickedItemIndex.value] = clickedItem.value
                        // guarda la lista actualizada en el archivo
                        writeData(itemList, context)
                        // cierra el dialog
                        updateDialogStatus.value = false
                    }
                ) {
                    Text(text = "guardar")
                }
            },
            // boton de cancelar
            dismissButton = {
                TextButton(
                    onClick = { updateDialogStatus.value = false }
                ) {
                    Text(text = "cancelar")
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

cada tarea ahora tiene un icono de lapiz al lado derecho. cuando presiones el lapiz, aparece un dialog con el texto de la tarea que puedes modificar. al presionar guardar se actualiza la tarea.

## problemas comunes

### problema 1: cannot resolve symbol icons

solucion:
1. verifica el import androidx.compose.material.icons.Icons
2. verifica el import androidx.compose.material.icons.filled.Edit
3. sync project with gradle files

### problema 2: el icono no aparece

solucion:
1. verifica que el Row tenga horizontalArrangement = Arrangement.SpaceBetween
2. verifica que el Text tenga modifier = Modifier.weight 1f

### problema 3: el dialog no se cierra

solucion:
1. verifica que updateDialogStatus.value = false este en confirmButton
2. verifica que updateDialogStatus.value = false este en dismissButton

### problema 4: los cambios no se guardan

solucion:
1. verifica que llames a writeData despues de actualizar itemList
2. verifica que uses clickedItemIndex.value para el indice correcto

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
                    // row organiza texto a la izquierda y boton a la derecha
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
                                // muestra el dialog
                                updateDialogStatus.value = true
                                // guarda cual tarea se presiono
                                clickedItemIndex.value = index
                                // guarda el texto de la tarea
                                clickedItem.value = item
                            }
                        ) {
                            // icono de lapiz
                            Icon(
                                Icons.Filled.Edit,
                                contentDescription = "editar",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }

        // dialog para editar la tarea
        // solo se muestra si updatedialogstatus es true
        if (updateDialogStatus.value) {
            AlertDialog(
                // se ejecuta cuando presionas fuera del dialog
                onDismissRequest = { updateDialogStatus.value = false },
                // titulo del dialog
                title = {
                    Text(text = "actualizar tarea")
                },
                // contenido principal: textfield para editar
                text = {
                    TextField(
                        value = clickedItem.value,
                        onValueChange = { clickedItem.value = it }
                    )
                },
                // boton de confirmar
                confirmButton = {
                    TextButton(
                        onClick = {
                            // reemplaza la tarea vieja con el texto nuevo
                            itemList[clickedItemIndex.value] = clickedItem.value
                            // guarda la lista actualizada en el archivo
                            writeData(itemList, context)
                            // cierra el dialog
                            updateDialogStatus.value = false
                        }
                    ) {
                        Text(text = "guardar")
                    }
                },
                // boton de cancelar
                dismissButton = {
                    TextButton(
                        onClick = { updateDialogStatus.value = false }
                    ) {
                        Text(text = "cancelar")
                    }
                }
            )
        }
    }
}
```

---

checkpoint 05 completado
