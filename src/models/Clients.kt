package models

import utils.ajustarAncho
import java.io.*

class Client {
    private var codi: Int
    private var nom: String
    private var cognoms: String
    private var diaNaixement: Int
    private var mesNaixement: Int
    private var anyNaixement: Int
    private var adreçaPostal: String
    private var eMail: String
    private var esVIP: Boolean

    private var br : BufferedReader? = null

    constructor() {
        codi = 0
        nom = ""
        cognoms=""
        diaNaixement =0
        mesNaixement = 0
        anyNaixement = 0
        adreçaPostal=""
        eMail=""
        esVIP= false
    }
    constructor(f:FicheroClientes) : this() {
        leer(f)
    }
    constructor(codi:Int, nom:String,cognoms: String,diaNaixement: Int,mesNaixement: Int,anyNaixement: Int,adreçaPostal: String ,eMail: String,esVIP: Boolean ):this() {
        this.codi = codi
        this.nom = nom
        this.cognoms = cognoms
        this.diaNaixement = diaNaixement
        this.mesNaixement = mesNaixement
        this.anyNaixement = anyNaixement
        this.adreçaPostal = adreçaPostal
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

    fun guardarEnModoTexto(f: FicheroClientes) {

        var clienteAGuardar : String = ""

        clienteAGuardar += ajustarAncho(codi.toString(),6)
        clienteAGuardar += ajustarAncho(nom, 20, false)
        clienteAGuardar += ajustarAncho(cognoms, 30, false)
        clienteAGuardar += ajustarAncho(diaNaixement.toString(), 2,true,'0')
        clienteAGuardar += ajustarAncho(mesNaixement.toString(), 2,true,'0')
        clienteAGuardar += ajustarAncho(anyNaixement.toString(), 4,true,'0')
        clienteAGuardar += ajustarAncho(adreçaPostal, 40, false)
        clienteAGuardar += ajustarAncho(eMail, 30, false)

        f.getPw().println(clienteAGuardar)

        f.getPw().flush()
        f.getFw().close()
    }

    fun guardar(f: FicheroClientes) {
        f.getDos().writeInt(codi)
        f.getDos().writeUTF(nom)
        f.getDos().writeUTF(cognoms)
        f.getDos().writeInt(diaNaixement)
        f.getDos().writeInt(mesNaixement)
        f.getDos().writeInt(anyNaixement)
        f.getDos().writeUTF(adreçaPostal)
        f.getDos().writeUTF(eMail)
        f.getDos().writeBoolean(esVIP)

        f.getDos().flush()
        f.getFos().close()
    }


    fun leerEnModoTexto(f: FicheroClientes) {
        var fos : FileReader = f.getFr()
        var dos : BufferedReader = f.getBr()

        try {
            var clienteALeer: String = dos.readLine()

            codi = clienteALeer.substring(0,6).trim().toInt()
            nom = clienteALeer.substring(6,26)
            cognoms = clienteALeer.substring(26,56)
            diaNaixement = clienteALeer.substring(56,58).toInt()
            mesNaixement = clienteALeer.substring(58,60).toInt()
            anyNaixement = clienteALeer.substring(60,64).toInt()
            adreçaPostal = clienteALeer.substring(64,104)
            eMail = clienteALeer.substring(104,134)
        }
        catch (e:Exception) {
            // Control cutre de excepción por final de fichero
            nom = ""
        }


        fos.close()
    }

    fun leer(f: FicheroClientes) {
        var fis : FileInputStream = f.getFis()
        var dis : DataInputStream = f.getDis()

        try {

            codi = dis.readInt()
            nom = dis.readUTF()
            cognoms = dis.readUTF()
            diaNaixement = dis.readInt()
            mesNaixement = dis.readInt()
            anyNaixement = dis.readInt()
            adreçaPostal = dis.readUTF()
            eMail = dis.readUTF()
            esVIP = dis.readBoolean()
        }
        catch (e:Exception) {
            // Control cutre de excepción por final de fichero
            nom = ""
        }

    }

    override fun toString() : String {

        return  "Client ( " + codi + ", " +
            nom + ", " +
            cognoms + ", " +
            diaNaixement + "/" +
            mesNaixement + "/" +
            anyNaixement + ", " +
            adreçaPostal + ", " +
            eMail + ", " +
            esVIP + ")"
    }


    fun clonar() : Client {
        return Client(codi,nom,cognoms, diaNaixement, mesNaixement, anyNaixement, adreçaPostal, eMail, esVIP)
    }
}
