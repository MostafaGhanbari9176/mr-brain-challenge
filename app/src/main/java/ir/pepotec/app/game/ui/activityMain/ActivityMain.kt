package ir.pepotec.app.game.ui.activityMain

import android.animation.ValueAnimator
import android.app.Activity
import android.app.AlertDialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Point
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.devs.vectorchildfinder.VectorChildFinder
import com.devs.vectorchildfinder.VectorDrawableCompat
import ir.pepotec.app.game.R
import ir.pepotec.app.game.model.Pref
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
        if (musicService != null)
            return
        val intent = Intent(this, ServiceMusic::class.java)
        bindService(intent, musicConnection, Context.BIND_AUTO_CREATE)
    }

    private fun initViews() {
        initProgress()
        floatBtnMain.setOnTouchListener { v, event ->
            ButtonEvent(v, event)
            false
        }
        floatBtnMain.setOnClickListener {
            musicService?.onDestroy()
            onBackPressed()
        }

        val p = Pref().getBollValue(Pref.dbCreated, false)
        setView(if(!p) FragmentDB() else FragmentMainMenu())

    }

    private fun initProgress() {
        val p = Point()
        windowManager.defaultDisplay.getRealSize(p)
        val param = RelativeLayout.LayoutParams(p.x, p.x / 6)
        imgProgressAM.layoutParams = param
    }

    fun showGameMode() {
        showModeName(false)
        animateProgress(PGameMode().getScoreAverage())
        setView(FragmentGameMode())

    }

    private fun showModeName(show: Boolean, modeId: String = "") {
        floatBtnMain.show()
        if (show) {
            txtModeNameNews.visibility = View.VISIBLE
            txtModeNameNews.text =
                when (modeId) {
                    "a" -> "مقدماتی"
                    "c" -> "پیشرفته"
                    "b" -> "حرفه ایی"
                    else -> "Welcome"
                }
        }
        else
            txtModeNameNews.visibility = View.GONE

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
        showModeName(true, modeId)
        val f = FragmentGameLevel()
        f.modeId = modeId
        setView(f)
        animateProgress(PGameMode().getScoreAverage(modeId))
    }

    fun setView(fragment: Fragment) {
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

    fun flushScore() {
        (txtScoreMain.background as Animatable).start()
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
        Log.d("Mostafa"," * Resume * ")

        App.instance = this
        App.fullScreen(this)
        musicService?.startMusic(R.raw.main)
    }

    override fun onPause() {
        super.onPause()
        Log.d("Mostafa"," * Pause * ")
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