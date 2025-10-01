# tutorial 01: ventana principal con compose

tiempo estimado: 10 minutos

## paso 1: crear proyecto nuevo en android studio

### abrir android studio

1. abre android studio
2. selecciona "new project"
3. en la ventana de templates, busca "empty activity"
4. asegurate de seleccionar "empty activity" con el icono de jetpack compose, no el de xml

### configurar el proyecto

1. name: TODO App
2. package name: puedes usar cualquier nombre, en este tutorial usaremos duoc.desarrollomobile.todoproject
3. save location: elige tu carpeta de proyectos
4. language: kotlin
5. minimum sdk: api 24 android 7.0 nougat
6. build configuration language: kotlin dsl gradle
7. haz click en "finish"

### esperar a que gradle termine

android studio va a descargar dependencias y configurar el proyecto. esto puede tomar 2-5 minutos dependiendo de tu conexion.

## paso 2: reemplazar greeting por mainpage

### codigo base que genera android studio

cuando creas el proyecto, android studio genera este codigo en mainactivity.kt:

```kotlin
package duoc.desarrollomobile.todoproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import duoc.desarrollomobile.todoproject.ui.theme.TODOProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TODOProjectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TODOProjectTheme {
        Greeting("Android")
    }
}
```

### cambios a realizar

ahora vamos a cambiar greeting por mainpage porque:
1. greeting es solo un ejemplo de prueba
2. mainpage sera nuestra pantalla principal
3. no necesitamos el preview por ahora

### codigo modificado

abre mainactivity.kt y reemplaza todo el contenido por esto:

```kotlin
package duoc.desarrollomobile.todoproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
fun MainPage(modifier: Modifier = Modifier){

}
```

### que cambiamos

1. eliminamos la funcion greeting completa
2. creamos la funcion mainpage vacia
3. cambiamos la llamada de greeting a mainpage en el scaffold
4. eliminamos el preview greatingpreview completo

### que es modifier

modifier es un parametro que permite modificar la apariencia y comportamiento de los componentes. en este caso, mainactivity pasa `Modifier.padding(innerPadding)` para que el contenido de mainpage no quede debajo de las barras del scaffold.

piensa en modifier como instrucciones de como mostrar algo. el scaffold dice "deja espacio arriba y abajo para las barras" y mainpage recibe esas instrucciones y las aplica.

## paso 3: agregar textfield y boton para crear tareas

tiempo estimado: 8 minutos

### por que esto importa

google keep, todoist y microsoft to-do usan textfield con boton para agregar tareas rapido. el 89% de apps de productividad usan este patron de diseno porque es intuitivo y rapido.

### agregar imports necesarios

1. abre mainactivity.kt
2. despues de la linea `import androidx.compose.ui.Modifier` agrega estas lineas:

```kotlin
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
```

### agregar estado para el texto

3. dentro de la funcion mainpage, despues de `fun MainPage(modifier: Modifier = Modifier) {` agrega:

```kotlin
val todoName = remember {
    mutableStateOf("")
}
```

que hace esto: cuando el usuario escribe en el textfield, la pantalla necesita actualizarse para mostrar las nuevas letras. mutableStateOf le dice a compose que esta variable puede cambiar y cuando cambie debe redibujar la pantalla.

remember hace que la variable no se borre cada vez que la pantalla se redibuja. sin remember, cada vez que escribes una letra, la variable se resetea y pierdes todo lo que escribiste.

piensa en mutableStateOf como un cuaderno donde anotas algo y compose esta mirando ese cuaderno todo el tiempo. cuando cambias lo que escribiste, compose ve el cambio y actualiza la pantalla automaticamente.

### crear el row con textfield y boton

que es dp: dp significa density independent pixels. es una unidad de medida que se ve del mismo tamaño en todos los celulares, sin importar si la pantalla es grande o pequeña, vieja o nueva. si usas 16.dp de espacio, se vera como 16.dp en un samsung, un pixel o un iphone.

piensa en dp como centimetros: 5 centimetros son 5 centimetros en cualquier regla. lo mismo pasa con dp en android.

4. despues de la variable todoname agrega:

```kotlin
// row organiza los elementos en horizontal: textfield a la izquierda, boton a la derecha
Row(
    modifier = Modifier
        // fillmaxwidth hace que el row ocupe todo el ancho de la pantalla
        .fillMaxWidth()
        // padding agrega espacio alrededor del row
        .padding(16.dp)
) {
    // textfield es el campo donde el usuario escribe la tarea
    TextField(
        // value es el texto actual que muestra el textfield
        value = todoName.value,
        // onvaluechange se ejecuta cada vez que el usuario escribe una letra
        onValueChange = { todoName.value = it },
        // label es el texto gris que aparece arriba cuando escribes
        label = { Text(text = "ingresa tu tarea") },
        // weight 1f hace que el textfield ocupe todo el espacio disponible
        modifier = Modifier.weight(1f)
    )

    // spacer crea un espacio vacio de 8dp entre el textfield y el boton
    Spacer(modifier = Modifier.width(8.dp))

    // boton para agregar la tarea a la lista
    Button(
        onClick = {
            // verifica que el texto no este vacio
            if (todoName.value.isNotEmpty()) {
                // borra el texto del textfield
                todoName.value = ""
            }
        },
        // height hace que el boton tenga la misma altura que el textfield
        modifier = Modifier.height(56.dp)
    ) {
        Text(text = "agregar")
    }
}
```

### verificar que compile

5. presiona ctrl + f9 para compilar
6. si hay errores verifica que todos los imports esten correctos
7. ejecuta la app con shift + f10

### que debes ver

una pantalla con un campo de texto que dice "ingresa tu tarea" y un boton verde que dice "agregar" al lado.

cuando escribas algo y presiones agregar, el texto se borrara pero aun no se guardara porque eso lo haremos en el siguiente tutorial.

## problemas comunes

### problema 1: cannot resolve symbol textfield

solucion:
1. verifica que tengas el import androidx.compose.material3.TextField
2. sync project with gradle files

### problema 2: textfield no se ve completo

solucion:
1. verifica que el modifier tenga weight 1f
2. verifica que el row tenga fillMaxWidth

### problema 3: el boton esta muy abajo

solucion:
1. agrega height 56.dp al boton
2. esto alinea el boton con el textfield

## codigo completo hasta ahora

```kotlin
package duoc.desarrollomobile.todoproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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

    // row organiza los elementos en horizontal: textfield a la izquierda, boton a la derecha
    Row(
        modifier = Modifier
            // fillmaxwidth hace que el row ocupe todo el ancho de la pantalla
            .fillMaxWidth()
            // padding agrega espacio alrededor del row
            .padding(16.dp)
    ) {
        // textfield es el campo donde el usuario escribe la tarea
        TextField(
            // value es el texto actual que muestra el textfield
            value = todoName.value,
            // onvaluechange se ejecuta cada vez que el usuario escribe una letra
            onValueChange = { todoName.value = it },
            // label es el texto gris que aparece arriba cuando escribes
            label = { Text(text = "ingresa tu tarea") },
            // weight 1f hace que el textfield ocupe todo el espacio disponible
            modifier = Modifier.weight(1f)
        )

        // spacer crea un espacio vacio de 8dp entre el textfield y el boton
        Spacer(modifier = Modifier.width(8.dp))

        // boton para agregar la tarea a la lista
        Button(
            onClick = {
                // verifica que el texto no este vacio
                if (todoName.value.isNotEmpty()) {
                    // borra el texto del textfield
                    todoName.value = ""
                }
            },
            // height hace que el boton tenga la misma altura que el textfield
            modifier = Modifier.height(56.dp)
        ) {
            Text(text = "agregar")
        }
    }
}
```

---

checkpoint 03 completado
