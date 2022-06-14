package com.azavalacoria.myapplication.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import com.azavalacoria.myapplication.config.AppPrefs
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey

class RSAHelper(context: Context) {
    private val context: Context
    private val privateKey: PrivateKey
    private val publicKey: PublicKey
    private val preferences: AppPrefs

    init {
        val keyGen = KeyPairGenerator.getInstance("RSA")
        keyGen.initialize(1024)
        val pair = keyGen.generateKeyPair()
        this.privateKey = pair.private
        this.publicKey = pair.public
        this.context = context
        this.preferences = AppPrefs(context)
    }

    fun store() {
        val baseDir = "${context.dataDir}/keys"
        val dirFile = File(baseDir)
        if (!dirFile.exists()) {
            try {
                dirFile.mkdir()
            } catch (e: Exception) {
                Log.d("TAG", "No se pudo crear el directorio: ${e.localizedMessage}")
            }
        }
        val privateKeyPath = "$baseDir/secret.key"
        saveKeyFile(privateKeyPath, AppPrefs.PRIVATE_KEY)
        val publicKeyPath = "$baseDir/public.key"
        saveKeyFile(publicKeyPath, AppPrefs.PUBLIC_KEY)
    }

    private fun saveKeyFile(path: String, keyType: String) {
        val privateKeyFile = File(path)
        try {
            privateKeyFile.createNewFile()
            val fos = FileOutputStream(privateKeyFile)
            fos.write(this.privateKey.encoded)
            fos.flush()
            preferences.setKeyString(keyType, path)
        } catch (e: Exception) {
            Log.e("TAG", "Error al guardar llave privada: ${e.localizedMessage}" )
        }
    }

    fun exportPublicKey(destination: String) {
        val base = File(preferences.getKeyString(AppPrefs.PUBLIC_KEY))
        val fileDestination = File(destination)
        val outputStream = FileOutputStream(fileDestination)
        Files.copy(base.toPath(), outputStream)
    }

    val pKey: String get() { return this.publicKey.format }
}