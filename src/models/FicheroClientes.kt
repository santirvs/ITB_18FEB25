package models

import java.io.*

class FicheroClientes {

    private var file : File

    //** Lectura texto
    private var fr : FileReader
    private var br : BufferedReader

    //** Escritura texto
    private var fw : FileWriter
    private var pw : PrintWriter

    //** Escritura binaria
    private var fos : FileOutputStream
    private var dos : DataOutputStream

    //** Lectura binaria
    private var fis : FileInputStream
    private var dis : DataInputStream

    constructor(path: String, append:Boolean) {
        this.file = File(path)
        this.fw = FileWriter(file,append)
        this.pw = PrintWriter(fw)

        this.fr = FileReader(this.file)
        this.br = BufferedReader(this.fr)

        this.fos = FileOutputStream(file,append)
        this.dos = DataOutputStream(fos)

        this.fis = FileInputStream(file)
        this.dis = DataInputStream(fis)
    }

    fun getFr() : FileReader {
        return fr
    }

    fun getBr() : BufferedReader {
        return br
    }

    fun getFw() : FileWriter {
        return fw
    }
    fun getPw() : PrintWriter {
        return pw
    }

    fun getDos() : DataOutputStream {
        return dos
    }
    fun getFos() : FileOutputStream {
        return fos
    }

    fun getDis() : DataInputStream {
        return dis
    }
    fun getFis() : FileInputStream {
        return fis
    }
}