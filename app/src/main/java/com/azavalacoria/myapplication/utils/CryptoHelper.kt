package com.azavalacoria.myapplication.utils

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.interfaces.RSAKey
import java.util.*
import javax.crypto.Cipher
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class CryptoHelper {
    private val algorithm: String = "AES/CBC/PKCS5Padding"
    private val baseKey: String = "1234567890123456"

    fun encode(text: String): String {
        val ivSpec = IvParameterSpec(ByteArray(16))
        val key = SecretKeySpec(baseKey.toByteArray(), "AES")
        return try {
            val cipher = Cipher.getInstance(algorithm)
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec)
            val cipherText = cipher.doFinal(text.toByteArray())
            Base64.getEncoder().encodeToString(cipherText)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            ""
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
            ""
        }
    }

    fun toSha256(text: String): String {
        val digest: MessageDigest = MessageDigest.getInstance("SHA-256")
        val hash: ByteArray = digest.digest(text.encodeToByteArray())
        val hexString: StringBuilder = StringBuilder(2 * hash.size)
        for (element in hash) {
            val hex = Integer.toHexString(0xff and element.toInt())
            if (hex.length == 1) {
                hexString.append('0')
            }
            hexString.append(hex)
        }
        return hexString.toString()
    }
}