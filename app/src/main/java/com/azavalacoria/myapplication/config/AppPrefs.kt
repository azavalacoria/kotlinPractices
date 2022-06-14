package com.azavalacoria.myapplication.config

import android.content.Context
import android.content.SharedPreferences

class AppPrefs(context: Context) {
    var preferences: SharedPreferences = context.getSharedPreferences(AppPrefs::class.java.canonicalName, 0)
    private var editor: SharedPreferences.Editor

    init {
        editor = preferences.edit()
    }

    fun getKeyString(key: String): String? = preferences.getString(key, null)

    fun setKeyString(key: String, value: String) {
        editor.putString(key, value)
        editor.commit()
    }

    companion object {
        const val PUBLIC_KEY = "rsaKeyPublic"
        const val PRIVATE_KEY = "rsaKeyPrivate"
    }
}