package duoc.desarrollomobile.kotlin.semana3.code

fun main(){

    // __________________________________________
    // WHILE
    /*
    los while son trozos de codigo que se ejecutan de forma indefinida
    hasta que la condicion cambia, usualmente siempre se compara
    con un numero y el resultado de esa comparacion da un true o false
    de esta forma sabremos si sigue en el loop o mas bien sale del ciclo
    */

    var contar = 0
    while (contar < 3){
        // mientras la condicion SEA TRUE, el loop continua
        // cuando pasa a FALSE, termina el ciclo
        println("se ejecuta este print")

        // durante el ciclo, podemos ver el estado del incremento por ejemplo
        println("el contador va en $contar")

        contar++
        // la version larga de contar++ seria contar = contar + 1
        // tambien puede usarse contar += 1


        // __________________________________________
        // QUIZ CUANtO SABES DE POKEMONES?
        /*
        Vamos a hacer un quiz por consola que permita hacer 4 preguntas
         -> Cuantas evoluciones tiene Evee en la primera generacion?
         -> Cual era el nombre del persaonaje humano principal de Pokemon?
         -> Cual es el nombre del pokemon mas famoso del mundo?
         -> Cuantos pokemones habian en la primera generacion?
        Para eso, una forma de validar que los inputs por teclado cumplan
        criterios de condicion son con While
        */

        println("Bienvenido al quiz mas pulento de pokemon")
        // guardamos la respuesta en una cadena string
        var respuestaQuiz = ""


        // tambien es bueno guardar las veces que el usuario se equivoco
        // al final del quiz
        var intentosFallidos = 0;

        // primera pregunta
        println("La pregunta 1 indica, cuantos tipos de evoluciones tiene eeve? 1, 2, 3?")
        respuestaQuiz = readLine().toString()
        while(respuestaQuiz.lowercase() != "3"){
            // recuerda, mientras la condicion SEA TRUE, el loop continua
            // cuando pasa a FALSE, termina el ciclo
            // el operador != es distinto de, es otro operador ademas de && y ||
            println("Incorrecto, eeve no tiene la cantidad de $respuestaQuiz evoluciones")
            println("nuevamente indica, cuantos tipos de evoluciones tiene eeve? 1, 2, 3?")

            // contamos el intento de error
            intentosFallidos++
        }
        println("Felicitaciones, eeve tiene $respuestaQuiz evoluciones distintas")
        respuestaQuiz = ""

        // segunda pregunta
        println("Vamos con la sgte pregunta! Cual era el nombre del persaonaje humano principal de Pokemon?")
        respuestaQuiz = readLine().toString()
        while (respuestaQuiz.lowercase() != "ash"){
            // recuerda, mientras la condicion SEA TRUE, el loop continua
            // cuando pasa a FALSE, termina el ciclo
            println("Nope, $respuestaQuiz no es el protagonista de Pokemon la serie")
            println("Nuevamente, Cual era el nombre del persaonaje humano principal de Pokemon?")

            // contamos el intento de error
            intentosFallidos++

        }
        println("Felicidades, $respuestaQuiz es el protagonista de pokemon la serie, super!")
        respuestaQuiz = ""

        // tercera pregunta
        println("tercera pregunta: Cual es el nombre del pokemon mas famoso del mundo?")
        respuestaQuiz = readLine().toString()
        while (respuestaQuiz.lowercase() != "pikachu"){
            // recuerda, mientras la condicion SEA TRUE, el loop continua
            // cuando pasa a FALSE, termina el ciclo
            println("NEIN, el pokemon $respuestaQuiz no es el mas famoso del mundo mundial")
            println("Nuevamente, Cual es el nombre del pokemon mas famoso del mundo?")

            // contamos el intento de error
            intentosFallidos++
        }
        println("Fantastico, $respuestaQuiz es el pokemon mas famoso del mundo")
        respuestaQuiz = ""

        // la ultima pregunta
        println("Ultima pregunta: Cuantos pokemones habian en la primera generacion?")
        respuestaQuiz = readLine().toString()
        while (respuestaQuiz.lowercase() != "151"){
            // recuerda, mientras la condicion SEA TRUE, el loop continua
            // cuando pasa a FALSE, termina el ciclo

            println("jaja, te equivocaste de temporada, LOL")
            println("Nuevamente, Cuantos pokemones habian en la primera generacion?")

            // contamos los errores
            intentosFallidos++
        }
        println("Asi es, la primera generacion de pokenon tuvo 151 pokemones, usted sabe")

        // validar datos, premiar si no obtuvo fallos, si los tuvo, felicitar pero con menos energias
        //
        if(intentosFallidos == 0){
            println("Felicitaciones, usted no ha tenido fallas, es una enciclopedia")
        }else{
            println("felicitaciones, a pesar de los errores, ha logrado cumplir la encuesta")
        }

    }

}