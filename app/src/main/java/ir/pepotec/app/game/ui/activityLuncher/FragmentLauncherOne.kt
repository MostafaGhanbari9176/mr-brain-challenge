package ir.pepotec.app.game.ui.activityLuncher

import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import ir.pepotec.app.game.R
import ir.pepotec.app.game.ui.App
import ir.pepotec.app.game.ui.activityMain.ActivityMain
import kotlinx.android.synthetic.main.fragment_launcher_one.*

class FragmentLauncherOne : Fragment() {

    private val s = "PePoTec.iR"
    private val p = Point()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_launcher_one, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        (App.instance as ActivityLauncher).windowManager.defaultDisplay.getRealSize(p)
        txtLauncherOne.translationX = p.x/2f + 50
        startAnimation()
    }

    private fun startAnimation() {
        val h = Handler()
        Thread(
            Runnable {
                Thread.sleep(1000)
                for (i in p.x/2 + 50 downTo 0 step 10) {
                    Thread.sleep(5)
                    h.post {txtLauncherOne.translationX = i.toFloat() }
                }
                for (i in 1..10) {
                    Thread.sleep(25)
                    h.post { txtLauncherOne.text = s.substring(0, i) }
                }
                Thread.sleep(500)
                for(i in 100 downTo 0)
                {
                    Thread.sleep(10)
                    h.post { txtLauncherOne.alpha = i/100f }
                }
                h.post{ (App.instance as ActivityLauncher).setView(FragmentLauncherTwo()) }
            }
        ).start()
    }

}