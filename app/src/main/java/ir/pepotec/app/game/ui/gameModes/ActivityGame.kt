package ir.pepotec.app.game.ui.gameModes

import android.app.Activity
import android.graphics.drawable.Animatable
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ir.pepotec.app.game.R
import ir.pepotec.app.game.model.Pref
import ir.pepotec.app.game.ui.App
import ir.pepotec.app.game.ui.gameModes.mode_a.FragmentModeA
import ir.pepotec.app.game.ui.gameModes.mode_b.FragmentModeB
import ir.pepotec.app.game.ui.gameModes.mode_c.FragmentModeC
import ir.pepotec.app.game.ui.gameModes.mode_d.FragmentModeD
import ir.pepotec.app.game.ui.uses.MyFragment
import kotlinx.android.synthetic.main.activity_game.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

class ActivityGame : AppCompatActivity() {

    private var levelId = -1
    private var modeId = "error"
    private var mLastTouchX: Int = 0
    private var mLastTouchY: Int = 0
    private lateinit var f: MyFragment
    private var mute: Boolean
        get() = Pref().getBollValue(Pref.mute, false)
        set(value) {
            Pref().saveBollValue(Pref.mute, value)
        }
    private var closeGame = false
    private var menuIsShow = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        initView()
        if (intent?.extras?.isEmpty == false) {

            levelId = intent.getIntExtra("levelId", -1)
            modeId = intent.getStringExtra("modeId")

        }
        startGame(modeId, levelId)
    }

    private fun initView() {
        App.fullScreen(this)
        //mute = preferences
        btnMenuAG.setOnClickListener {
            openMenu()
        }

        btnVolumeAG.setOnClickListener {
            mute = !mute
            closeMenu()
        }

        btnBackMenuAG.setOnClickListener {
            closeGame = true
            closeMenu()
        }

        btnHelpAG.setOnClickListener {
            f.runHelper()
            closeMenu()
        }

        parentAG.setOnTouchListener { v, event -> !onTouchEvent(event) }

        parentAG.setOnClickListener {
            if (menuIsShow)
                closeMenu()
            else
                f.myClickListener()
        }
    }


    private fun closeMenu() {
        menuIsShow = false
        imgMenuAG.setImageDrawable(null)
        imgMenuAG.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                if (mute) R.drawable.menu_close_animate_mute else R.drawable.menu_close_animate
            )
        )
        val d = (imgMenuAG.drawable as Animatable)
        btnBackMenuAG.visibility = View.GONE
        btnVolumeAG.visibility = View.GONE
        btnHelpAG.visibility = View.GONE
        btnMenuAG.visibility = View.VISIBLE
        doAsync {
            Thread.sleep(100)
            uiThread { d.start() }
        }
        doAsync {
            while (true) {
                if (!d.isRunning)
                    break
            }
            uiThread {
                if (closeGame)
                    onBackPressed()

            }
        }

    }

    private fun openMenu() {
        menuIsShow = true
        imgMenuAG.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                if (mute) R.drawable.menu_animate_mute else R.drawable.menu_animate
            )
        )
        btnBackMenuAG.visibility = View.VISIBLE
        btnVolumeAG.visibility = View.VISIBLE
        btnHelpAG.visibility = View.VISIBLE
        btnMenuAG.visibility = View.GONE
        (imgMenuAG.drawable as Animatable).start()
    }

    override fun onResume() {
        super.onResume()
        App.instance = this
    }

    fun startGame(modeId: String, levelId: Int) {
        this.modeId = modeId
        this.levelId = levelId
        when (modeId) {
            "a" -> f = FragmentModeA()
            "b" -> f = FragmentModeB()
            "c" -> f = FragmentModeC()
            "d" -> f = FragmentModeD()
            else -> {
                toast("mode $modeId notFound")
                return
            }
        }

        //  supportFragmentManager.beginTransaction().remove(f)
        f.levelId = levelId
        supportFragmentManager.beginTransaction().replace(R.id.ContainerGame, f).commit()
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        var dx = 0
        var dy = 0
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                mLastTouchX = ev.x.toInt()
                mLastTouchY = ev.y.toInt()
            }

            MotionEvent.ACTION_MOVE -> {
                val x = ev.x.toInt()
                val y = ev.y.toInt()
                dx = x - mLastTouchX
                dy = y - mLastTouchY
                mLastTouchX = x
                mLastTouchY = y
            }

            MotionEvent.ACTION_UP -> {
/*                if (!actionMove)
                    f.myClickListener()*/
            }//mPosX = 0;

            MotionEvent.ACTION_CANCEL -> {
            }

            MotionEvent.ACTION_POINTER_UP -> {
            }
        }
        f.myTouchListener(dx, dy)
        return true
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_OK, intent.putExtra("mode_id", modeId))
        this.finish()
        super.onBackPressed()
    }

}