package ir.pepotec.app.game.ui

import android.app.Application
import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import ir.pepotec.app.game.R
import ir.pepotec.app.game.model.GameModeData
import ir.pepotec.app.game.model.ModeAData
import ir.pepotec.app.game.model.local_data_base.GameModeDb
import ir.pepotec.app.game.model.local_data_base.ModeADb
import org.jetbrains.anko.toast

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        addDataToDb()
        overrideFont("SERIF", "fonts/Far_Tanab.ttf")
    }

    private fun addDataToDb() {
        ModeADb(this).deleteDb()
        GameModeDb(this).deleteDb()

        var jsonString = this.assets.open("mode_a_level.json").bufferedReader().use {
            it.readText()
        }
        var jsonArr: JsonArray = JsonParser().parse(jsonString) as JsonArray
        for (x in jsonArr) {
            val jsonObject: JsonObject = x as JsonObject
            with(jsonObject) {
                ModeADb(this@App).save(
                    ModeAData(
                        get(ModeADb.levelId).asInt,
                        get(ModeADb.subject).asString,
                        get(ModeADb.score).asInt,
                        get(ModeADb.lock).asInt,
                        get(ModeADb.alpha).asFloat,
                        get(ModeADb.spaceX).asInt,
                        get(ModeADb.puzzleX).asInt,
                        get(ModeADb.puzzleY).asInt
                    )
                )
            }
        }

        jsonString = this.assets.open("game_mode.json").bufferedReader().use {
            it.readText()
        }
        jsonArr = JsonParser().parse(jsonString) as JsonArray
        for (x in jsonArr) {
            val jsonObject: JsonObject = x as JsonObject
            with(jsonObject) {
                GameModeDb(this@App).save(
                    GameModeData(
                        get(GameModeDb.modeId).asString,
                        get(GameModeDb.subject).asString,
                        get(GameModeDb.scoreAverage).asInt,
                        get(GameModeDb.lock).asInt
                    )
                )
            }
        }
    }

    companion object {
        lateinit var instance: Context
        val baseUrl = "http://localhost:8080/game/"
        fun loadImage(img: ImageView, iUrl: String, iError: Int) {
            return
            val rOption = RequestOptions().error(iError).placeholder(R.drawable.progress)
            Glide.with(instance!!)
                .load("http://pepotec.ir/images/arduino-icon.png")
                .apply(rOption)
                .into(img)
        }

        fun fullScreen(context: AppCompatActivity) {
            instance = context
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
    }

    fun overrideFont(defaultFontNameToOverride: String, customFontFileNameInAssets: String) {
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