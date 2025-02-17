package models

import interfaces.IGuardable
import utils.ajustarAncho
import java.io.*
import java.util.*

class Client : IGuardable, Cloneable {
    private var esborrat: Boolean = false
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

        when (f.getMode()) {
            FileModes.TEXT_MODE -> guardarTexto(f)
            FileModes.BINARY_MODE -> guardarBinario(f)
            FileModes.RANDOM_ACCESS_MODE -> guardarDirecto(f)
        }

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

    private fun guardarDirecto(f: Fichero) {

        //Posición al final del fichero
        val posicion : Long  = f.getRaf()!!.length()
        f.getRaf()!!.seek(posicion)

        //Guardar los datos del cliente
        f.getRaf()!!.writeBoolean(esborrat)
        f.getRaf()!!.writeInt(codi)
        f.getRaf()!!.writeUTF(nom)
        f.getRaf()!!.writeUTF(cognoms)
        f.getRaf()!!.writeInt(diaNaixement)
        f.getRaf()!!.writeInt(mesNaixement)
        f.getRaf()!!.writeInt(anyNaixement)
        f.getRaf()!!.writeUTF(adrecaPostal)
        f.getRaf()!!.writeUTF(eMail)
        f.getRaf()!!.writeBoolean(esVIP)

        //Guardar los datos en el fichero índice
        var rafIdx : RandomAccessFile = RandomAccessFile(f.getNomFitxer()+".idx", "rw")
        rafIdx.seek(rafIdx.length())
        rafIdx.writeInt(codi)
        rafIdx.writeLong(posicion)
        rafIdx.close()

    }

    fun leerPosicion(f: Fichero, pos: Int) {

        if (f.getMode() == FileModes.RANDOM_ACCESS_MODE) {
          leerPosicionDirecto(f, pos)
        }
        else {
            repeat(pos) {
                leer(f)
            }
        }
    }

    private fun leerPosicionDirecto(f: Fichero, pos: Int) {
        //Obrir el fitxer d'índex
        var rafIdx : RandomAccessFile = RandomAccessFile(f.getNomFitxer()+".idx", "r")
        rafIdx.seek(0)
        var posicio : Long
        var trobat : Boolean = false
        var contador : Int = 1
        //Recòrrer el fitxer d'índex fins a trobar la posició  (cal vigilar que no es superi el final del fitxer)
        while (!trobat && rafIdx.filePointer < rafIdx.length()) {
            rafIdx.readInt()  //Ignorem el codi
            posicio = rafIdx.readLong()
            if (contador == pos) {
                //Accedir al fitxer de dades a la posició indicada
                f.getRaf()!!.seek(posicio)
                leer(f)
                trobat = true
            }
            contador++
        }
        if (!trobat) {
            nom = ""
        }
        rafIdx.close()
    }

    fun leerCodigo(f: Fichero, codi: Int) {
        var trobat:Boolean = false
        if (f.getMode() == FileModes.RANDOM_ACCESS_MODE) {
            while (!f.eof && !trobat) {
                leer(f)
                if (this.codi == codi) {
                    trobat = true
                }
            }
        }
        else {
            leerCodigoDirecto(f, codi)
        }
    }
    private fun leerCodigoDirecto(f: Fichero, codiBuscat: Int) {
        //Obrir el fitxer d'índex
        var rafIdx : RandomAccessFile = RandomAccessFile(f.getNomFitxer()+".idx", "r")
        rafIdx.seek(0)
        var posicio : Long
        var codi : Int
        var trobat : Boolean = false
        //Recòrrer el fitxer d'índex fins a trobar el codi  (cal vigilar que no es superi el final del fitxer)
        while (!trobat && rafIdx.filePointer < rafIdx.length()) {
            codi = rafIdx.readInt()
            posicio = rafIdx.readLong()
            if (codi == codiBuscat) {
                //Accedir al fitxer de dades a la posició indicada
                f.getRaf()!!.seek(posicio)
                leer(f)
                trobat = true
            }
        }
        if (!trobat) {
            nom = ""
        }
        rafIdx.close()
    }

    override fun leer(f: Fichero) {
        when (f.getMode()) {
            FileModes.TEXT_MODE -> leerTexto(f)
            FileModes.BINARY_MODE -> leerBinario(f)
            FileModes.RANDOM_ACCESS_MODE -> leerDirecto(f)
        }
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

    private fun leerDirecto(f: Fichero) {
        var raf: RandomAccessFile = f.getRaf()!!

        try {
            esborrat = raf.readBoolean()
            codi = raf.readInt()
            nom = raf.readUTF()
            cognoms = raf.readUTF()
            diaNaixement = raf.readInt()
            mesNaixement = raf.readInt()
            anyNaixement = raf.readInt()
            adrecaPostal = raf.readUTF()
            eMail = raf.readUTF()
            esVIP = raf.readBoolean()

            if (esborrat) {
                nom = ""
            }
        } catch (e: Exception) {
            // Control cutre de excepción por final de fichero
            nom = ""
            f.eof = true
        }

    }

    fun modificarDirecte(f: Fichero, codiBuscat: Int, esborrar: Boolean) {
        var rafIdx : RandomAccessFile = RandomAccessFile(f.getNomFitxer()+".idx", "rw")
        rafIdx.seek(0)
        var posicio : Long
        var codi : Int
        var trobat : Boolean = false
        //Recòrrer el fitxer d'índex fins a trobar el codi (cal vigilar que no es superi el final del fitxer)
        while (!trobat && rafIdx.filePointer < rafIdx.length()) {
            codi = rafIdx.readInt()
            posicio = rafIdx.readLong()
            if (codi == codiBuscat) {
                //Accedir al fitxer de dades a la posició indicada
                f.getRaf()!!.seek(posicio)
                //Marcar la posició de dades com a esborrada
                f.getRaf()!!.writeBoolean(true)
                //Marcar l'índex com a esborrat
                rafIdx.seek(rafIdx.filePointer-12)
                rafIdx.writeInt(-1)
                if (!esborrar) {
                    //Guardar les dades del client
                    guardarDirecto(f)
                }
                trobat = true
            }
        }
        rafIdx.close()
    }

    // Overrides de Cloneable

    override public fun clone(): Any {
        return Client(codi, nom, cognoms, diaNaixement, mesNaixement, anyNaixement, adrecaPostal, eMail, esVIP)
    }

}
