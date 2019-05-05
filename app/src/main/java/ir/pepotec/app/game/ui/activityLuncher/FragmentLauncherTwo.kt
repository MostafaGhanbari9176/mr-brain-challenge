package ir.pepotec.app.game.ui.activityLuncher

import android.content.Intent
import android.graphics.Point
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ir.pepotec.app.game.R
import ir.pepotec.app.game.ui.App
import ir.pepotec.app.game.ui.activityMain.ActivityMain
import kotlinx.android.synthetic.main.fragment_launcher_one.*
import kotlinx.android.synthetic.main.fragment_launcher_two.*

class FragmentLauncherTwo:Fragment() {

    private val p = Point()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_launcher_two, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        (imgAnimLauncherTwo.drawable as Animatable).start()
        (App.instance as ActivityLauncher).windowManager.defaultDisplay.getRealSize(p)
        startAnimation()
        imgAnimLauncherTwo.setOnClickListener {
            (App.instance as ActivityLauncher).setView(FragmentLauncherTwo())
        }
    }

    private fun startAnimation() {
        val h = Handler()
        val imgWidth = resources.getDimension(R.dimen.launcher_two_txt_powered_height)
        val to = (p.x - imgWidth).toInt()
        Thread(
            Runnable {
                for (i in 0..to step 5) {
                    Thread.sleep(5)
                    h.post {
                        LLAndroid.translationX = i.toFloat()
                    }
                }
                Thread.sleep(2000)
                h.post { nextActivity() }
            }
        ).start()
    }
    private fun nextActivity() {
        val i = Intent(App.instance, ActivityMain::class.java)
        (App.instance as ActivityLauncher).apply {
            startActivity(i)
            finish()
        }
    }
}