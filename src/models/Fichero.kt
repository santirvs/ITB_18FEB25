package models

import java.io.*

class Fichero {

    private var file : File
    private var mode : FileModes = FileModes.TEXT_MODE

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

    //** Accés directe
    private var raf : RandomAccessFile? = null

    public var eof: Boolean = false

    constructor(path: String, append:Boolean = true, mode:FileModes = FileModes.TEXT_MODE) {
        this.file = File(path)
        this.mode = mode

        if (!file.exists()) {
            file.createNewFile()
        }

        if (mode == FileModes.BINARY_MODE) {
            //Crear los objetos de acceso a ficheros binarios
            this.fos = FileOutputStream(file,append)
            this.dos = DataOutputStream(fos)

            this.fis = FileInputStream(file)
            this.dis = DataInputStream(fis)
        }
        else if (mode == FileModes.RANDOM_ACCESS_MODE) {
            //Crear los objetos de acceso a ficheros de text
            this.fw = FileWriter(file, append)
            this.pw = PrintWriter(fw)

            this.fr = FileReader(this.file)
            this.br = BufferedReader(this.fr)
        }
        else if (mode == FileModes.BINARY_MODE) {
            this.raf = RandomAccessFile(file, "rw")
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
    fun getRaf() : RandomAccessFile? {
        return raf
    }

    fun getMode() : FileModes {
        return mode
    }

    fun close() {
        if (mode == FileModes.BINARY_MODE) {
            dos!!.flush()
            fos!!.close()
            dis!!.close()
            fis!!.close()
        }
        else if (mode == FileModes.TEXT_MODE) {
            pw!!.flush()
            fw!!.close()
            fr!!.close()
            br!!.close()
        }
        else if (mode == FileModes.RANDOM_ACCESS_MODE) {
            raf!!.close()
        }
    }
}