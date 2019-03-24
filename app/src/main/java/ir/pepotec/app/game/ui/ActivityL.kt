package ir.pepotec.app.game.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ir.pepotec.app.game.R
import ir.pepotec.app.game.ui.activityMain.ActivityMain
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

class ActivityL: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.luncher)
        App.instance = this
        toast("luncherStart")
        doAsync{
            Thread.sleep(5000)
            uiThread {
                toast("luancherStop")
                startActivity(Intent(App.instance, ActivityMain::class.java)) }
        }
    }
}