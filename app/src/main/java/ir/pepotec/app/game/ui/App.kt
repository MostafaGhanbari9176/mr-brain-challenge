package ir.pepotec.app.game.ui

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ir.pepotec.app.game.R
import org.jetbrains.anko.custom.async

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
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

}