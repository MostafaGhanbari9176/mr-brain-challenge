package ir.pepotec.app.game.ui

import android.app.Application
import android.content.Context

class App:Application(){


    companion object {
        var instance:Context? = null
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}