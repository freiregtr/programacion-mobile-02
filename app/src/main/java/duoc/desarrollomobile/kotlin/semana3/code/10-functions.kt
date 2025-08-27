package duoc.desarrollomobile.kotlin.semana3.code

fun main(){

    // __________________________________________
    // FUNCTIONS
    /*
    Las funciones sirven principalmente para separar responsabilidades
    y encapsular codigo, de esta forma nuestro codigo se vuelve
    mas modular y de esta forma es mas mantenible y escalable

     -> Las funciones pueden devolver algo con retur
     -> las funciones pueden recibir parametros como argumentos
    */

    // PRIMERA FUNCION
    // Crearemos la primera funcion que sera el proceso de hacer el cafe en Starbucks
    // para eso, llamamos a la primera funcion llamada hacerCafeStarbucks
    hacerCafeStarbucks()
    // este proceso no recibe parametros ni devuelve algo, solo procesa informacion
    // que veremos por la consola o terminal

    // SEGUNDA FUNCION
    // ahora, que pasa si el operador necesita que el cafe tenga el nombre explicito
    // asi de esta forma puede llamarlo sin tener que revisar la boleta
    // para eso hacemos uso de parametros, o tambien que reciba argumentos, en este caso, nombre
    hacerCafeStarbucksConNombre("Juanito")

    // TERCERA FUNCION
    // que pasa si ahora el cliente solo quiere preguntar el precio, entonces
    // necesitamos que el operador en caja solo devuelva el precion, para ello,
    // simplemente ejecutamos la funcion, indicamos el tipo de cafe y luego devolvemos el precio
    println("____________ INIT CONSULTA PRECIO STARBUCKS ____________")
    println("Valores Cafes Starbucks")
    println("1 - Cafe expresso")
    println("2 - Cafe cortado")
    println("3 - Cafe lungo")
    println("4 - Cafe Capuccino")
    println("5 - Cafe Capuccino Vainilla")
    println("Seleccione el cafe que desea consultar precio -> ")
    var cafeSeleccionado = readLine()?.toIntOrNull() ?: 0
    println("El cafe seleccionado cuesta ${consultarprecioCafe(cafeSeleccionado)}")

    // la diferencia aca a comparacion en otros ejercicios es que estamos usando el operador elvis
    // eso quiere decir que estamos manejando errores en caso que no venga un numero y entregamos
    // uno por defecto. Por ejemplo, antes cuando liamos con readLine(), con !! le decimos al
    // sistema que "no te preocupes, esto viene si o si" pero no es lo recomendado
    // con el operador elvis ?: 0 le estamos diciendo al programa "si algo pasa, devuelve cero"
    // de esta forma estamos resolviendo excepciones segun mejores practicas

}
// PRIMERA FUNCION
fun hacerCafeStarbucks(){
    // en esta primera funcion, vamos a replicar el proceso completo de hacer cafe
    println("____________ INIT PROCESO CAFE STARBUCKS ____________")
    println("primero, el cliente pasa pro la caja y elige su cafe")
    println("A continuacion, el cliente paga con su medio de pago")
    println("el operador recibe el pedido y prepara el cafe")
    println("el operador verifica el estado del pedido como tamanio, estilo de cafe, etc")
    println("Una vez el operador termina el cafe, procede a llamar al usuario por su nuombre")
    println("al usuario se le indica que en la mesa tiene azucar y endulzante y se le entrega su cafe")
    println("____________ END PROCESO CAFE STARBUCKS ____________")
}

// SEGUNDA FUNCION
fun hacerCafeStarbucksConNombre(nombreCliente : String){
    // en esta primera funcion, vamos a replicar el proceso completo de hacer cafe
    println("____________ INIT PROCESO CAFE STARBUCKS ____________")
    println("primero, el cliente pasa pro la caja y elige su cafe")
    println("A continuacion, el cliente paga con su medio de pago")
    println("el operador recibe el pedido y prepara el cafe")
    println("el operador verifica el estado del pedido como tamanio, estilo de cafe, etc")
    println("Una vez el operador termina el cafe, procede a llamar a $nombreCliente")
    println("al usuario se le indica que en la mesa tiene azucar y endulzante y se le entrega su cafe")
    println("____________ END PROCESO CAFE STARBUCKS ____________")
}

// TERCERA FUNCION
fun consultarprecioCafe(numeroCafe : Int): String{

    // En Java se tenia que declarar al principio cuando una funcion devolvia algo
    // en este caso, como es de tipado fuerte, se declaraba el tipo de dato del retorno
    // e incluso si no devolvia datos como por ejemplo <ArrayList>  o void, pero en este caso
    // lo hacemos despues de los argumentos, como devolvemos un string, lo declaramos como
    // : String: (numeroCafe : Int): String

    val cafeEspresso = "1 - Cafe expresso .................. 2.990"
    val cafeCortado = "2 - Cafe cortado ................... 3.490"
    val cafeLungo = "3 - Cafe lungo ..................... 2.990"
    val cafeCapuccino = "4 - Cafe Capuccino ................. 5.990"
    val cafeCappucinoVainilla = "5 - Cafe Capuccino Vainilla ........ 6.450"

    // en Java o C++ existe el switch case que permite ser mas estructurado
    // con las elecciones, pero en Koptlin existe algo llamado when, muy parecido
    return when (numeroCafe) {
        1 -> cafeEspresso
        2 -> cafeCortado
        3 -> cafeLungo
        4 -> cafeCapuccino
        5 -> cafeCappucinoVainilla
        else -> "ese cafe no lo tenemos, quizas en Juan Valdes si"
    }
}