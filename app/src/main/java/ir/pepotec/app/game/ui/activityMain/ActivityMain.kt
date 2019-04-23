package ir.pepotec.app.game.ui.activityMain

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Point
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.devs.vectorchildfinder.VectorChildFinder
import com.devs.vectorchildfinder.VectorDrawableCompat
import ir.pepotec.app.game.R
import ir.pepotec.app.game.presenter.PGameMode
import ir.pepotec.app.game.ui.App
import ir.pepotec.app.game.ui.uses.ButtonEvent
import ir.pepotec.app.game.ui.uses.ServiceMusic
import kotlinx.android.synthetic.main.main_activity.*
import org.jetbrains.anko.toast


class ActivityMain : AppCompatActivity() {

   private val musicConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            toast("rebindMusic")
            connectToMusicService()
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            musicService = (service as ServiceMusic.BinderMusic).getService()
            musicService?.startMusic(R.raw.main)
        }
    }

    companion object {
        var musicService: ServiceMusic? = null
    }

    private lateinit var f: Fragment
    private var progressValue = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        App.instance = this
        initViews()
        connectToMusicService()
    }

    private fun connectToMusicService() {
        if(musicService != null)
            return
        val intent = Intent(this, ServiceMusic::class.java)
        bindService(intent, musicConnection, Context.BIND_AUTO_CREATE)
    }

    private fun initViews() {
        App.fullScreen(this)
        initProgress()
        floatBtnMain.setOnTouchListener { v, event ->
            ButtonEvent(v, event)
            false
        }
        floatBtnMain.setOnClickListener {
            musicService?.onDestroy()
            onBackPressed()
        }
        setView(FragmentMainMenu())
    }

    private fun initProgress() {
        val p = Point()
        windowManager.defaultDisplay.getRealSize(p)
        val param = RelativeLayout.LayoutParams(p.x, p.x / 6)
        imgProgressAM.layoutParams = param
    }

    fun showGameMode() {
        animateProgress(PGameMode().getScoreAverage())
        setView(FragmentGameMode())

    }

    private fun animateProgress(to: Int) {
        ValueAnimator.ofInt(progressValue, to).apply {
            duration = 500
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                val vector = VectorChildFinder(this@ActivityMain, R.drawable.big_progress, imgProgressAM)
                val path = vector.findPathByName("progress") as VectorDrawableCompat.VFullPath
                val v = it.animatedValue as Int
                path.trimPathEnd = v / 100f
                txtScoreMain.text = "$v"
            }
            start()
        }
        progressValue = to
    }

    fun showGameLevel(modeId: String) {
        val f = FragmentGameLevel()
        f.modeId = modeId
        setView(f)
        animateProgress(PGameMode().getScoreAverage(modeId))
    }

    private fun setView(fragment: Fragment) {
        if (fragment is FragmentMainMenu) {
            floatBtnMain.hide()
            imgProgressAM.visibility = View.GONE
            txtScoreMain.visibility = View.GONE
        } else if (fragment is FragmentGameMode && f is FragmentMainMenu) {
            floatBtnMain.show()
            imgProgressAM.visibility = View.VISIBLE
            txtScoreMain.visibility = View.VISIBLE
        }
        f = fragment
        supportFragmentManager.beginTransaction().setCustomAnimations(
            R.anim.abc_fade_in,
            R.anim.abc_fade_out
        ).replace(R.id.containerGameLevels, fragment).commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        App.instance = this
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            val modeId = data!!.getStringExtra("modeId")
            if (modeId == "d")
                showGameMode()
            else
                showGameLevel(modeId)
        }
    }

    override fun onResume() {
        super.onResume()
        App.instance = this
        musicService?.startMusic(R.raw.main)
    }

    override fun onPause() {
        super.onPause()
        musicService?.stopMusic()
    }

    override fun onBackPressed() {
        when (f) {
            is FragmentGameMode -> setView(FragmentMainMenu())
            is FragmentGameLevel -> showGameMode()
            else -> {
                this.finish()
                super.onBackPressed()
            }
        }
    }

}