import models.*
import java.io.File
import java.io.RandomAccessFile
import java.util.*

const val FILE_TXT = "FicheroClientes.txt"
const val FILE_BIN = "FicheroClientes.dat"
const val FILE_COPY = "_copia"

val FILE:String = FILE_BIN
val modeFitxer : FileModes  = FileModes.RANDOM_ACCESS_MODE

fun main() {

    var opcio = menuClients()
    while (opcio!=0) {
        when (opcio) {
            1 -> altaClient()
            2 -> consultaPerPosicio()
            3 -> consultaPerCodi()
            4 -> modificarClient()
            5 -> esborrarClient()
            6 -> llistarClients()
            98 -> esborrarFitxers()
            99 -> generarDadesClients()
            else -> println("Opció incorrecta")
        }
        opcio = menuClients()
    }
}

fun menuClients() : Int{

    println(
        """
        ** MENU CLIENTS **
        1. Alta client
        2. Consulta per posició
        3. Consulta per codi
        4. Modificar client
        5. Esborrar client
        6. Llistar clients
        98. Esborrar fitxers
        99. Generar dades de clients
        0. Sortir
    """.trimIndent()
    )

    return demanarOpcio()
}

fun demanarOpcio() : Int {
    var scan = Scanner(System.`in`)
    print("Introdueix opció: ")
    return scan.nextInt()
}

fun altaClient() {
    var c: Client = Client(false)
    var f: Fichero = Fichero(FILE,mode=modeFitxer)
    c.guardar(f)
    f.close()
}

fun consultaPerPosicio() {
    var scan = Scanner(System.`in`)
    var f: Fichero = Fichero(FILE,mode=modeFitxer)
    print("Introdueix posició a consultar: ")
    var p = scan.nextInt()
    var c: Client = Client(true)
    c.leerPosicion(f,p)
    if (c.getNom()!="") {
        println(c.toString())
    }
    else {
        println("No s'ha trobat el client")
    }
    f.close()
}

fun consultaPerCodi() {
    var scan = Scanner(System.`in`)
    var f: Fichero = Fichero(FILE, mode=modeFitxer)
    print("Introdueix codi a consultar: ")
    var codi = scan.nextInt()
    var c: Client = Client(true)
    c.leerCodigo(f,codi)
    if (c.getNom()!="") {
        println(c.toString())
    }
    else {
        println("No s'ha trobat el client")
    }
    f.close()
}

fun esborrarClient() {
    copiarFitxer(true)
}

fun modificarClient() {
    copiarFitxer(false)
}

fun copiarFitxer(esborrar:Boolean) {
    var scan = Scanner(System.`in`)
    var f: Fichero = Fichero(FILE, mode=modeFitxer)
    var accio : String = if (esborrar) "esborrar" else "modificar"
    print("Introdueix codi a $accio :")
    var codi = scan.nextInt()

    if (modeFitxer == FileModes.RANDOM_ACCESS_MODE) {
        var c:Client = Client(esborrar)
        c.modificarDirecte(f, codi, esborrar)
    } else {
        copiarFitxerNoDirecte(f, codi, esborrar)
    }

}


fun copiarFitxerNoDirecte(f:Fichero, codi:Int, esborrar:Boolean) {
    var f2: Fichero = Fichero(FILE+FILE_COPY, mode=modeFitxer)
    var c: Client = Client(true)
    c.leer(f)
    while (c.getCodi() != codi && !f.eof) {
        c.guardar(f2)
        c.leer(f)
    }
    //Demanar les dades del client, si cal
    if (!esborrar && !f.eof) {
        var c2: Client = Client(false)
        //Guardar los datos del cliente
        c2.guardar(f2)
    }
    //Leer el resto de clientes
    c.leer(f)
    while (!f.eof) {
        c.guardar(f2)
        c.leer(f)
    }

    f.close()
    f2.close()
    renombrarFicheros(FILE, FILE+FILE_COPY)
}

fun renombrarFicheros(nom1: String, nom2: String) {
    var f1 = File(nom1)
    f1.delete()
    var f2 = File(nom2)
    f2.renameTo(f1)
}

fun llistarClients() {

    if (modeFitxer == FileModes.RANDOM_ACCESS_MODE) {
        llistarClientsDirecte()
    } else {
        llistarClientsNoDirecte()
    }
}

fun llistarClientsDirecte() {
    //Listar clientes
    println("** Llistat de clients ***")
    val index : MutableList<IndexEntry> = mutableListOf()
    val indexFile:RandomAccessFile = RandomAccessFile(FILE + ".idx", "r")
    val f: Fichero = Fichero(FILE, mode=modeFitxer)

    while (indexFile.length() > indexFile.filePointer) {
        var codi = indexFile.readInt()
        var posicio = indexFile.readLong()
        if (codi != -1) {
            //Entrada de l'index no esborrada
            index.add(IndexEntry(codi, posicio))
        }
    }

    //Ordenació dels indexos
    index.sortBy { it.codi }

    //Recorrer els indexos per mostrar els clients
    var c: Client = Client(true)
    for (i in index) {
        f.getRaf()!!.seek(i.posicio)
        c.leer(f)
        println(c.toString())
    }

    f.close()
    indexFile.close()
}


fun llistarClientsNoDirecte() {
    //Listar clientes
    println("** Llistat de clients ***")
    var f :  Fichero = Fichero(FILE, mode=modeFitxer)
    var c: Client = Client(true)
    c.leer(f)
    while (c.getNom() != "") {
        println(c.toString())
        c.leer(f)
    }
    f.close()
}

fun esborrarFitxers() {
    var f1 = File(FILE)
    f1.delete()
    var f2 = File(FILE+FILE_COPY)
    f2.delete()
    var f3 = File(FILE + ".idx")
    f3.delete()
}

fun generarDadesClients() {
    var c1 = Client(111, "Primer", "Primera", 1, 1, 2001, "C/ Major, 11", "111@gmail.com", false)
    var c2 = Client(222, "Segon", "Segona", 2, 2, 2002, "C/ Pi, 222", "222@gmail.com", true)
    var c3 = Client(333, "Tercer", "Tercera", 3, 3, 2003, "C/ Moli, 333", "333@gmail.com", false)
    var c4 = Client(444, "Quart", "Quarta", 4, 4, 2004, "C/ Baix, 444", "444@gmail.com", true)
    var c5 = Client(555, "Cinque", "Cinquena", 5, 5, 2005, "C/ Dalt, 555", "555@gmail.com", false)

    var f: Fichero = Fichero(FILE, mode=modeFitxer)
    c1.guardar(f)
    c2.guardar(f)
    c3.guardar(f)
    c4.guardar(f)
    c5.guardar(f)

    f.close()
}