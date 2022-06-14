package com.azavalacoria.myapplication.utils

import android.content.Context
import android.util.Log
import com.azavalacoria.myapplication.config.AppPrefs
import java.io.File
import java.util.*

class FileHelper() {

    private var context: Context? = null
    private var file: File? = null

    constructor(context: Context) : this() {
        this.context = context
    }

    fun makeTextFile(name: String, internal: Boolean) {
        if (internal) {
            this.file = File("${context!!.dataDir.absolutePath}/$name.txt")
            if (!file!!.exists()) {
                file!!.createNewFile()
            }
            val prefs = AppPrefs(context!!)
            prefs.setKeyString(AppPrefs.PRIVATE_KEY, file!!.absolutePath)
            Log.d("TAG", file!!.absolutePath)
        } else {
            if (this.file != null && !this.file!!.exists()) {
                if (name != null && name.isNotEmpty()) {
                    this.file = File.createTempFile(name, ".txt")
                } else {
                    this.file = File.createTempFile(
                        UUID.randomUUID().toString().uppercase(),
                        ".txt")
                }
            }
        }
    }

    fun writeText(text: String) {
        if (file != null && text != null) {
            file!!.writeText(text)
            Log.d("TAG", file!!.readText())

        }
    }

}