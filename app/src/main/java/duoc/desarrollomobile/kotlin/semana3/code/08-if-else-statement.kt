package duoc.desarrollomobile.kotlin.semana3.code

fun main(){

    // if else statement
    println("ejemplo simple de uso de If Else Statement")
    val condicion = true

    if(condicion){
        println("la condicion es verdadera")
    }else{
        println("la condicion es falsa")
    }

    /*
        Con If Else statements podemos usar varios tipos de operadores de comparacion
         - == Equals to o Igual a
         - > Greater than o Mayor que
         - < Less than o Menor que
         - >= Greater than or equal to o Mayor o igual que
         - <= Less than or Equal to o menor o igual que
    */

    // __________________________________________
    // Vamos a hacer un ejercicio de Saber el cumpleanos de una persona
    // para eso, usaremos if else para poder determinar la edad de la persona
    // preguntando el anio que nacio y luego si ya estuvo de cumpleanios

    // variables de inicializacion
    var edadUsuario = 0
    val anioActual = 2025

    // ahora ingresamos por teclado el anio de nascimiento
    println("por favor ingrese su anio de nacimiento")
    var anioNascimiento = readLine()
    /*
        en este ejemplo se puede evidenciar una diferencia clara entre java y kotlin
        -> Java se usa Tipos de Datos Primitivos como int, bool, etc
        -> en Kotlin todo es un Objeto, no existen datos primitivos
           todo es un String, Int, Boolean, Double
        -> tambien existen los objetos nullable, pueden contener datos y un null
           los objetos de tupo nullable terminan con un ? permiten un null
    */

    // para efectos del ejercicio, vamos a forzar la respuesta como segura con !!
    // pero en la vida real debemos usar toIntOrNull para estar mas seguros
    // ahora le pasamos el valor a un dato Int, ya que no podemos usar anioNascimiento
    // al ser una variable nullable
    var anioComparacion = anioNascimiento?.toInt()!!

    // ahora edadUsuario le asignamos el valor entre la fecha actual menos el anio que nacio
    edadUsuario = anioActual - anioComparacion

    // ahora con un if else validamos las opciones si ha cumplido anios aun
    print("usted ya ya estado de cumpleanios? true | false")
    var isCumpleanios = readLine().toBoolean()

    if(isCumpleanios){
        println("usted tiene " + edadUsuario + " anios")
    }else{
        println("usted tiene " + (edadUsuario - 1) + " anios")
    }

    /*
    de esta forma estamos validando y usando if else para saber
    la edad de una persona, pero ademas preguntamos al usuario
    si ha estado de cumpleanios, con esto, podemos saber
    con certeza el anio exacto de su nacimiento
    */

    // __________________________________________
    // SISTEMA DE CALIFICACION DE NOTAS
    /*
    Aca el enunciado es simple, saber si un estudiante tiene
     -> Igual o sobre un 6.0: EXCELENTE
     -> Igual o sobre un 4.0: APROBADO
     -> si no cumple con el resto: REPROBADO
    */
    // primero, declaramos las variables del usuario
    println("Por favor, ingrese la nota de su ultimo examen")
    val notaUsuario = readLine()!!.toDouble()

    // luego, validamos si la nota es igual o superior a
    if(notaUsuario >= 6.0){
        println("su nota " + notaUsuario + " es EXCELENTE")
    }else if(notaUsuario >= 4.0){
        println("su nota " + notaUsuario + " esta APROBADO")
    }else if( notaUsuario < 4.0){
        println("su nota " + notaUsuario + " esta REPROBADO")
        // en este caso tambien puede ser un ELSE, tambien sirve
    }

    // en el ultimo caso, el else-if pude tambien ser un solo else
    // ejemplo mas eficiente y con otra forma de concatenar
    if (notaUsuario >= 6.0) {
        println("su nota $notaUsuario es EXCELENTE")
    } else if (notaUsuario >= 4.0) {
        println("su nota $notaUsuario esta APROBADO")
    } else {
        println("su nota $notaUsuario esta REPROBADO")
    }
    // __________________________________________
    // SISTEMA PARA SABER SI UN ALUMNO PASA EL CURSO
    /*
    en este ejercicio debemos saber no solamente si un alumno
    pasa con nota aprobada, si no tambien debemos saber si
    tiene el porcentsaje de asistencia adecuado
    */

    // primero, le pedimos por consola los datos como nota y asistencia
    println("Ingrese su nota por favor, Ej: 6.4")
    val notaDeAlumno = readLine()!!.toDouble()
    println("ingrese su porcentaje de asistencia sin el % al final. Ej: 85")
    val asistenciaAlumno = readLine()!!.toInt()

    /*
    Ahora evaluamos primero
     -> si la nota es igual o sobre 4.0 y ademas (&&) se cumplen las dos condiciones
     -> si la nota es igual o sobre 4.0 pero la asistencia no es buena, evaluar si pasa o no
     -> si el alumno obtiene 7 o un 100% de asistencia, se gana una estrellita en la mano
     -> si no se cumple la nota y asistencia, reprobado
    De esta forma estamos ocupando operadores logicos para evaluar
    */

    if(notaDeAlumno >= 4.0 && asistenciaAlumno >= 70){
        println("el alumno ha pasado el semestre")
    }else if (notaDeAlumno >= 4.0 && (asistenciaAlumno >= 50 && asistenciaAlumno < 70)){
        println("el alumno debe ser evaluado por el profesor")
    }else if(notaDeAlumno == 7.0 || asistenciaAlumno == 100){
        println("el alumno se gana una estrellita por cumplir una de las dos condiciones")
    }else{
        println("el alumno ha reprobado el curso")
    }

    /*
    Con esto queda demostrado que
     - para comprobar que el alumno ha pasado bien las dos condiciones, se usa &&, o sea
       se debe cumplir las dos condiciones
     - en la segunda instancia, si cumple la nota pero la asistencia esta entre un 50 y 69%
       entonces debe haber una revision por parte del profesor, pero se deben cumplir los dos
     - en la ultima parte, para ganarse la estrellita en la mano, debe cumplir o una nota
       perfecta o una asistencia perfecta, o sea solo uno con ||
     - finalmente, si no se cumple la condicion de asistencia y nota, no pasa
    */

    // EJERCICIO: el ejercicio de SISTEMA PARA SABER SI UN ALUMNO PASA EL CURSO tiene
    // un bug logico, identificar cual es y como se podria mejorar
    // __________________________________________

}