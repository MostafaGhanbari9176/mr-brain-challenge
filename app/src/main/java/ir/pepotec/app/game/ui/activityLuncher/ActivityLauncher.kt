package ir.pepotec.app.game.ui.activityLuncher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ir.pepotec.app.game.R
import ir.pepotec.app.game.ui.App

class ActivityLauncher:AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.instance = this
        setContentView(R.layout.activity_launcher)
        setView(FragmentLauncherOne())
    }

     fun setView(f: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.LauncherContent, f).commit()
    }

    override fun onResume() {
        super.onResume()
        App.instance = this
        App.fullScreen(this)
    }
}