package models

import interfaces.IGuardable
import utils.ajustarAncho
import java.io.*
import java.util.*

class Client : IGuardable, Cloneable {
    private var codi: Int = 0
    private var nom: String = ""
    private var cognoms: String = ""
    private var diaNaixement: Int = 0
    private var mesNaixement: Int = 0
    private var anyNaixement: Int = 0
    private var adrecaPostal: String = ""
    private var eMail: String = ""
    private var esVIP: Boolean = false


    constructor(vacio:Boolean) {
        if (!vacio)
            pedirDatos(this)
    }

    constructor(codi:Int, nom:String,cognoms: String,diaNaixement: Int,mesNaixement: Int,anyNaixement: Int,adrecaPostal: String ,eMail: String,esVIP: Boolean ){
        this.codi = codi
        this.nom = nom
        this.cognoms = cognoms
        this.diaNaixement = diaNaixement
        this.mesNaixement = mesNaixement
        this.anyNaixement = anyNaixement
        this.adrecaPostal = adrecaPostal
        this.eMail = eMail
        this.esVIP = esVIP
    }

    fun getCodi() : Int {
        return this.codi
    }

    fun getNom() : String {
        return this.nom
    }

    fun setNom(valor:String) {
        this.nom = valor
    }

    override fun toString() : String {

        return  "Client ( " + codi + ", " +
            nom + ", " +
            cognoms + ", " +
            diaNaixement + "/" +
            mesNaixement + "/" +
            anyNaixement + ", " +
            adrecaPostal + ", " +
            eMail + ", " +
            esVIP + ")"
    }

    private fun pedirDatos(c:Client) {
        var scan: Scanner = Scanner(System.`in`)

        var codi: Int
        var nom: String
        var cognoms: String
        var diaNaixement: Int
        var mesNaixement: Int
        var anyNaixement: Int
        var adrecaPostal: String
        var eMail: String
        var esVIP: Boolean

        print("Codi:"); c.codi = scan.nextInt(); scan.nextLine()
        print("Nom:"); c.nom = scan.nextLine()
        print("Cognoms:"); c.cognoms = scan.nextLine()
        print("diaNaixement:"); c.diaNaixement = scan.nextInt(); scan.nextLine()
        print("mesNaixement:"); c.mesNaixement = scan.nextInt(); scan.nextLine()
        print("anyNaixement:"); c.anyNaixement = scan.nextInt(); scan.nextLine()
        print("AdreçaPostal:"); c.adrecaPostal = scan.nextLine()
        print("eMail:"); c.eMail = scan.nextLine()
        print("esVIP:"); c.esVIP = (scan.nextLine() == "S")
    }

    //Overrides de IGuardable

    override fun guardar(f: Fichero) {
        if (f.getEsBinari())
            guardarBinario(f)
        else
            guardarTexto(f)
    }
    private fun guardarTexto(f: Fichero) {

        var pw : PrintWriter = f.getPw()!!
        var fw : FileWriter = f.getFw()!!

        var clienteAGuardar : String = ""

        clienteAGuardar += ajustarAncho(codi.toString(),6)
        clienteAGuardar += ajustarAncho(nom, 20, false)
        clienteAGuardar += ajustarAncho(cognoms, 30, false)
        clienteAGuardar += ajustarAncho(diaNaixement.toString(), 2,true,'0')
        clienteAGuardar += ajustarAncho(mesNaixement.toString(), 2,true,'0')
        clienteAGuardar += ajustarAncho(anyNaixement.toString(), 4,true,'0')
        clienteAGuardar += ajustarAncho(adrecaPostal, 40, false)
        clienteAGuardar += ajustarAncho(eMail, 30, false)

        pw.println(clienteAGuardar)

        pw.flush()
    }
    private fun guardarBinario(f: Fichero) {
        f.getDos()!!.writeInt(codi)
        f.getDos()!!.writeUTF(nom)
        f.getDos()!!.writeUTF(cognoms)
        f.getDos()!!.writeInt(diaNaixement)
        f.getDos()!!.writeInt(mesNaixement)
        f.getDos()!!.writeInt(anyNaixement)
        f.getDos()!!.writeUTF(adrecaPostal)
        f.getDos()!!.writeUTF(eMail)
        f.getDos()!!.writeBoolean(esVIP)

        f.getDos()!!.flush()
    }

    override fun leer(f: Fichero) {
        if (f.getEsBinari())
            leerBinario(f)
        else
            leerTexto(f)
    }
    private fun leerTexto(f: Fichero) {
        var fos : FileReader = f.getFr()!!
        var dos : BufferedReader = f.getBr()!!

        try {
            var clienteALeer: String = dos.readLine()

            codi = clienteALeer.substring(0,6).trim().toInt()
            nom = clienteALeer.substring(6,26).trim()
            cognoms = clienteALeer.substring(26,56).trim()
            diaNaixement = clienteALeer.substring(56,58).toInt()
            mesNaixement = clienteALeer.substring(58,60).toInt()
            anyNaixement = clienteALeer.substring(60,64).toInt()
            adrecaPostal = clienteALeer.substring(64,104).trim()
            eMail = clienteALeer.substring(104,134).trim()
        }
        catch (e:Exception) {
            // Control cutre de excepción por final de fichero
            nom = ""
            f.eof = true
        }

        fos.close()
    }
    private fun leerBinario(f: Fichero) {
        var fis: FileInputStream = f.getFis()!!
        var dis: DataInputStream = f.getDis()!!

        try {

            codi = dis.readInt()
            nom = dis.readUTF()
            cognoms = dis.readUTF()
            diaNaixement = dis.readInt()
            mesNaixement = dis.readInt()
            anyNaixement = dis.readInt()
            adrecaPostal = dis.readUTF()
            eMail = dis.readUTF()
            esVIP = dis.readBoolean()
        } catch (e: Exception) {
            // Control cutre de excepción por final de fichero
            nom = ""
            f.eof = true
        }

    }


    // Overrides de Cloneable

    override public fun clone(): Any {
        return Client(codi, nom, cognoms, diaNaixement, mesNaixement, anyNaixement, adrecaPostal, eMail, esVIP)
    }

}
