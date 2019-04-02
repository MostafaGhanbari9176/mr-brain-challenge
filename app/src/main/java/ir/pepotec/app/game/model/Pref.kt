package ir.pepotec.app.game.model

import android.content.Context.MODE_PRIVATE
import ir.pepotec.app.game.ui.App

class Pref {


    companion object {
        const val help_a = "help_a"
        const val mute = "mute"
    }

    private val p = App.instance.getSharedPreferences("MostafaGhanbari", MODE_PRIVATE)

    fun getBollValue(name: String, defValue: Boolean): Boolean {
        return p.getBoolean(name, defValue)
    }

    fun saveBollValue(name: String, value: Boolean): Boolean {
        val editor = p.edit()
        editor.putBoolean(name, value)
        return editor.commit()
    }

    fun getStringValue(name: String, defValue: String): String {
        return p.getString(name, defValue)!!
    }

    fun saveStringValue(name: String, value: String): Boolean {
        val editor = p.edit()
        editor.putString(name, value)
        return editor.commit()
    }

    fun getIntegerValue(name: String, defValue: Int): Int {
        return p.getInt(name, defValue)
    }

    fun saveIntegerValue(name: String, value: Int): Boolean {
        val editor = p.edit()
        editor.putInt(name, value)
        return editor.commit()
    }


    fun getLongValue(name: String, defValue: Long): Long? {
        return p.getLong(name, defValue)
    }

    fun saveLongValue(name: String, value: Long): Boolean {
        val editor = p.edit()
        editor.putLong(name, value)
        return editor.commit()
    }

    fun removeValue(name: String): Boolean {
        val editor = p.edit()
        editor.remove(name)
        return editor.commit()
    }
}