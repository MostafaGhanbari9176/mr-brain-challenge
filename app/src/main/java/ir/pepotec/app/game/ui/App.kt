package ir.pepotec.app.game.ui

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Typeface
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import ir.pepotec.app.game.R
import ir.pepotec.app.game.model.*
import ir.pepotec.app.game.model.local_data_base.GameModeDb
import ir.pepotec.app.game.model.local_data_base.ModeADb
import ir.pepotec.app.game.model.local_data_base.ModeBDb
import ir.pepotec.app.game.model.local_data_base.ModeCDb
import ir.pepotec.app.game.ui.uses.ServiceMusic

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        overrideFont("SERIF", "fonts/Far_Tanab.ttf")

    }
    companion object {
        lateinit var instance: Context
        fun fullScreen(context: AppCompatActivity) {
            val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            if (Build.VERSION.SDK_INT < 16) {
                context.window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                context.window.decorView.systemUiVisibility = flags
                // Code below is to handle presses of Volume up or Volume down.
                // Without this, after pressing volume buttons, the navigation bar will
                // show up and won't hide
                val decorView = context.window.decorView
                decorView
                    .setOnSystemUiVisibilityChangeListener { visibility ->
                        if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                            decorView.systemUiVisibility = flags
                        }
                    }
            }
        }
        fun getBlockHeight(): Float {
            return App.instance.resources.getDimension(R.dimen.puzzle_height)
        }
    }

    private fun overrideFont(defaultFontNameToOverride: String, customFontFileNameInAssets: String) {
        try {
            val customFontTypeface = Typeface.createFromAsset(this.assets, customFontFileNameInAssets)

            val defaultFontTypefaceField = Typeface::class.java.getDeclaredField(defaultFontNameToOverride)
            defaultFontTypefaceField.isAccessible = true
            defaultFontTypefaceField.set(null, customFontTypeface)
        } catch (e: Exception) {
            /*  Log.e("Can not set custom font " + customFontFileNameInAssets + " instead of " + defaultFontNameToOverride);*/
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }

    }

}