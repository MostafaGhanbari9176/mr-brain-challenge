package ir.pepotec.app.game.ui.gameModes.mode_d

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.Animatable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import ir.pepotec.app.game.R
import ir.pepotec.app.game.presenter.PModeDLevel
import ir.pepotec.app.game.ui.App
import ir.pepotec.app.game.ui.gameModes.ActivityGame
import ir.pepotec.app.game.ui.uses.MyFragment
import kotlinx.android.synthetic.main.dialog_mode_d.view.*
import kotlinx.android.synthetic.main.fragment_mode_d.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.lang.Exception
import kotlin.math.abs


class FragmentModeD : MyFragment(),PModeDLevel.PModeDInterface {

    override var levelId: Int = 0
    private var gameSpeed = 15L
    private var addRefrense = 50
    private var score = 0
    private val startY: Float
        get() = LLPuzzleModeD.y - LLPuzzleModeD.height
    private val endY: Float
        get() = LLPuzzleModeD.y + LLPuzzleModeD.height
    private var rVStep = 0
    private var itemHeight = 0
    private val ctx: Context = App.instance
    private val p = Point()
    private var delta = 0
    private var lastScore = 0
    private lateinit var blockView: View
    private val blockY: Float
        get() = arr[1].toFloat()
    private val arr = IntArray(2)
    private val puzzleLength: Int
        get() = LLPuzzleModeD.width
    private val blockLength: Int
        get() = blockView.width
    private var position: Int = 1
        set(value) {
            field = value
            initBlock()
            setScore()
        }
    private var gameRuning = false
    private var delCount = 5
    private var gameStoped = false
    private var puzzleIsGhost = false
    private var puzzleIsHide = false
    private var handlerGhost: Handler = Handler()
    private var handlerRun: Handler = Handler()
    @SuppressLint("SetTextI18n")
    private fun setScore() {
        if(position > addRefrense)
        {
            delCount++
            addRefrense+=50
        }
        gameSpeed = when (position) {
            in 0..50 -> 15L
            in 50..150 -> 10L
            in 150..500 -> 5L
            else -> 2L
        }
        score += 10
        val scX = ObjectAnimator.ofFloat(txtScoreModeD, View.SCALE_X, 1f, 1.2f, 0.8f, 1f)
        val scY = ObjectAnimator.ofFloat(txtScoreModeD, View.SCALE_Y, 1f, 1.2f, 0.8f, 1f)
        AnimatorSet().apply {
            playTogether(scX, scY)
            duration = 300
            interpolator = DecelerateInterpolator()
            start()
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mode_d, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        scoreIncrement()
        delta = (p.x / 6) - (p.x / 8)
        itemHeight = p.y / 2
    }

    private fun scoreIncrement() {
        doAsync {
            while (true) {
                if (lastScore < score) {
                    lastScore++
                    Thread.sleep(10)
                    uiThread { txtScoreModeD.text = "امتیاز : $lastScore" }
                }
            }
        }
    }

    private fun initView() {

        txtDel.text = "$delCount"
        (App.instance as ActivityGame).windowManager.defaultDisplay.getRealSize(p)
        imgDel.layoutParams.height = p.x
        LLEventsModeD.setOnTouchListener { v, event -> !((App.instance as ActivityGame).onTouchEvent(event)) }
        LLEventsModeD.setOnClickListener {
            myClickListener()
        }
        initPuzzle()
        initRV()
    }

    private fun initRV() {
        RVModeD.layoutManager = LinearLayoutManager(ctx)
        RVModeD.adapter = AdapterModeD(p) { runRV() }
    }

    private fun initPuzzle() {
        LLPuzzleParentModeD.layoutParams.height = p.y / 2
        LLPuzzleRightModeD.layoutParams.width = p.x / 8
        LLPuzzleLeftModeD.layoutParams.width = p.x / 8
    }

    private fun initBlock() {
        try {
            blockView = RVModeD.layoutManager?.findViewByPosition(position)!!
            blockView = (blockView as LinearLayout).getChildAt(2)
        } catch (e: Exception) {
            gameRuning = false
            onResume()
        }
    }

    private fun runRV() {
        gameRuning = true
        val r = Runnable {
            while (gameRuning) {
                handlerRun.post {
                    try {
                        if (position != rVStep / itemHeight)
                            position = rVStep / itemHeight

                        blockView.getLocationInWindow(arr)

                        if (blockY < endY && blockY > startY) {
                            if (abs(puzzleLength - blockLength) > 2 * delta && !puzzleIsGhost) {
                                gameStoped = true
                                gameRuning = false
                                accident()
                            }
                        }
                        RVModeD.requestLayout()
                        RVModeD.scrollBy(0, 10)
                    } catch (e: Exception) {
                    }
                }
                rVStep += 10
                Thread.sleep(gameSpeed)
            }
        }
        Thread(r).start()

    }

    private fun accident() {
        if (delCount == 0) {
            PModeDLevel(this).saveData(score, position, delCount)
        }
        else {
            txtDel.text = "${--delCount}"
            animateDel()
            ghostingPuzzle()
        }

    }

    private fun ghostingPuzzle() {
        puzzleIsGhost = true
        val r = Runnable {
            for (i in 1..30) {
                handlerGhost.post {
                    try {
                        if (puzzleIsHide) {
                            LLPuzzleParentModeD.alpha = 1f
                            puzzleIsHide = false
                        } else {
                            LLPuzzleParentModeD.alpha = 0f
                            puzzleIsHide = true
                        }
                    } catch (e: Exception) {
                    }
                }
                Thread.sleep(100)
            }
            puzzleIsGhost = false
        }
        Thread(r).apply { start() }

    }

    private fun animateDel() {
        delAnimate = (imgDel.drawable as Animatable).apply { start() }
        doAsync {
            Thread.sleep(1100)
            uiThread {
                delAnimate.stop()
                gameStoped = false
            }
        }
    }

    private lateinit var delAnimate: Animatable

    private var step: Int = 0

    override fun myTouchListener(dx: Int, dy: Int) {
        if (gameStoped)
            return
        step += dx
        if (abs(step) >= (p.x / 100)) {
            var newWidth = LLPuzzleModeD.width + if (step > 0) (p.x / 30) else -(p.x / 30)
            if (newWidth + p.x / 4 > p.x)
                newWidth = p.x - p.x / 4
            val param = LinearLayout.LayoutParams(newWidth, App.getBlockHeight().toInt())
            LLPuzzleModeD.layoutParams = param
            step = 0
        }
    }

    override fun myClickListener() {
        if (!gameStoped && !gameRuning)
            runRV()
    }

    override fun runHelper() {
    }

    override fun onDestroy() {
        gameRuning = false
        super.onDestroy()
    }

    override fun result(lScore: Int, lBlock: Int, lDel: Int, isHScore: Boolean) {
        val d = Dialog(ctx)
        val v = LayoutInflater.from(ctx).inflate(R.layout.dialog_mode_d, null, false)
        d.setContentView(v)
        d.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        v.CVDialogModeD.layoutParams.width = p.x - 40
        v.txtScoreDialogD.text = "$score"
        v.txtDelDialogD.text = "$delCount"
        v.txtBlockDialogD.text = "$position"
        v.txtHSDialogD.text = "$lScore"
        v.txtHBModeD.text = "$lBlock"
        v.txtHDDialogD.text = "$lDel"
        v.LLHighScore.visibility = if(isHScore) View.VISIBLE else View.GONE
        v.btnBackModeD.setOnClickListener {
            d.cancel()
            (ctx as ActivityGame).apply {
                setResult(Activity.RESULT_OK, intent.putExtra("mode_id", "d"))
                finish()
            }
        }
        v.btnRepeatModeD.setOnClickListener {
            d.cancel()
            (ctx as ActivityGame).startGame("d", levelId)
            App.fullScreen(ctx as ActivityGame)
        }
        d.setCancelable(false)
        d.show()
    }

}