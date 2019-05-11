package ir.pepotec.app.game.model

import android.content.Context.MODE_PRIVATE
import ir.pepotec.app.game.ui.App

class Pref {


    companion object {
        const val help_a = "help_a"
        const val mute = "mute"
        const val score = "score"
        const val block = "block"
        const val brain = "brain"
        const val dbCreated = "db"
        const val helpMovie = "help_movie"
        const val helpD = "help_d"
        const val helpC = "help_c"
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

    fun getIntegerValue(name: String, defValue: Int): Int {
        return p.getInt(name, defValue)
    }

    fun saveIntegerValue(name: String, value: Int): Boolean {
        val editor = p.edit()
        editor.putInt(name, value)
        return editor.commit()
    }

    fun removeValue(name: String): Boolean {
        val editor = p.edit()
        editor.remove(name)
        return editor.commit()
    }
}