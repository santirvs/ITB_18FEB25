import models.Client
import models.FicheroClientes
import java.io.File
import java.util.*

val FILE_TXT = "FicheroClientes.txt"
val FILE_BIN = "FicheroClientes.dat"
val FILE_TXT2 = "./FicheroClientes2.txt"
val FILE_BIN2 = "FicheroClientes2.dat"

fun main() {

    var scan : Scanner = Scanner(System.`in`)
    var f: FicheroClientes = FicheroClientes(FILE_BIN ,true)

    //Alta del cliente
    var c: Client = Client()
    c = pedirDatosCliente()
    c.guardar(f)


    // Consulta per posició
    print("Introdueix posició a consultar: ")
    var p = scan.nextInt()
    repeat(p) {
        c.leer(f)
    }
    println(c.toString())

    //Consulta per codi
    f = FicheroClientes(FILE_BIN, true)
    print("Introdueix codi a consultar: ")
    var codi = scan.nextInt()
    c = Client()
    while (c.getCodi() != codi) {
        c.leer(f)
    }
    println(c.toString())

    //Modificar client que hem trobat per codi
    //var c2 = pedirDatosCliente()
    var c2:Client = c.clonar()
    c2.setNom( c2.getNom().trim() + "x")

    var f2: FicheroClientes = FicheroClientes(FILE_BIN2, false)
    f = FicheroClientes(FILE_BIN, false)
    c.leer(f)
    while (c.getCodi() != codi) {
        c.guardar(f2)
        c.leer(f)
    }
    c2.guardar(f2)
    c.leer(f)
    while (c.getNom() != "") {
        c.guardar(f2)
        c.leer(f)
    }

    var f1= File("FicheroClientes.txt")
    f1.delete()
    var f12 = File("./FicheroClientes2.txt")
    f12.renameTo(File("FicheroClientes.txt"))

    //Listar clientes
    println("** Llistat de clients ***")
    f = FicheroClientes("FicheroClientes.txt", false)
    c.leer(f)
    while (c.getNom() != "") {
        println(c.toString())
        c.leer(f)
    }

}


fun pedirDatosCliente() : Client {
    var scan : Scanner = Scanner (System.`in`)

    var codi: Int
    var nom: String
    var cognoms: String
    var diaNaixement: Int
    var mesNaixement: Int
    var anyNaixement: Int
    var adreçaPostal: String
    var eMail: String
    var esVIP: Boolean

    print("Codi:"); codi = scan.nextInt(); scan.nextLine()
    print("Nom:"); nom = scan.nextLine()
    print("Cognoms:"); cognoms = scan.nextLine()
    print("diaNaixement:"); diaNaixement = scan.nextInt(); scan.nextLine()
    print("mesNaixement:"); mesNaixement = scan.nextInt(); scan.nextLine()
    print("anyNaixement:"); anyNaixement = scan.nextInt(); scan.nextLine()
    print("AdreçaPostal:"); adreçaPostal = scan.nextLine()
    print("eMail:"); eMail = scan.nextLine()
    print("esVIP:"); esVIP = (scan.nextLine() == "S")

    return Client(codi, nom, cognoms, diaNaixement, mesNaixement, anyNaixement, adreçaPostal, eMail, esVIP)
}
