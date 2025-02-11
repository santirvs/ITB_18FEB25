package models

import java.io.*

class Fichero {

    private var file : File
    private var binari : Boolean = false

    //** Lectura texto
    private var fr : FileReader? = null
    private var br : BufferedReader? = null

    //** Escritura texto
    private var fw : FileWriter? = null
    private var pw : PrintWriter? = null

    //** Escritura binaria
    private var fos : FileOutputStream? = null
    private var dos : DataOutputStream? = null

    //** Lectura binaria
    private var fis : FileInputStream? = null
    private var dis : DataInputStream? = null

    public var eof: Boolean = false

    constructor(path: String, append:Boolean = true, binari:Boolean = false) {
        this.file = File(path)
        this.binari = binari

        if (!file.exists()) {
            file.createNewFile()
        }

        if (binari) {
            //Crear los objetos de acceso a ficheros binarios
            this.fos = FileOutputStream(file,append)
            this.dos = DataOutputStream(fos)

            this.fis = FileInputStream(file)
            this.dis = DataInputStream(fis)
        }
        else {
            //Crear los objetos de acceso a ficheros de
            this.fw = FileWriter(file, append)
            this.pw = PrintWriter(fw)

            this.fr = FileReader(this.file)
            this.br = BufferedReader(this.fr)
        }


    }

    fun getFr() : FileReader? {
        return fr
    }

    fun getBr() : BufferedReader? {
        return br
    }

    fun getFw() : FileWriter? {
        return fw
    }
    fun getPw() : PrintWriter? {
        return pw
    }

    fun getDos() : DataOutputStream? {
        return dos
    }
    fun getFos() : FileOutputStream? {
        return fos
    }

    fun getDis() : DataInputStream? {
        return dis
    }
    fun getFis() : FileInputStream? {
        return fis
    }
    fun getEsBinari() : Boolean {
        return binari
    }

    fun close() {
        if (binari) {
            dos!!.flush()
            fos!!.close()
            dis!!.close()
            fis!!.close()
        }
        else {
            pw!!.flush()
            fw!!.close()
            fr!!.close()
            br!!.close()
        }
    }
}