package ir.pepotec.app.game.ui.gameModes.mode_d

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import ir.pepotec.app.game.R
import ir.pepotec.app.game.ui.App
import ir.pepotec.app.game.ui.gameModes.ActivityGame
import ir.pepotec.app.game.ui.uses.MyFragment
import kotlinx.android.synthetic.main.fragment_mode_d.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import kotlin.math.abs


class FragmentModeD : MyFragment() {

    override var levelId: Int = 0
    private var score = 0
    private var gameRuning = false
    private val startY:Float
    get() = LLPuzzleModeD.y - LLPuzzleModeD.height
    private val endY:Float
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

    @SuppressLint("SetTextI18n")
    private fun setScore() {
        score+=10
        val scX = ObjectAnimator.ofFloat(txtScoreModeD, View.SCALE_X, 1f,1.2f,0.8f,1f)
        val scY = ObjectAnimator.ofFloat(txtScoreModeD, View.SCALE_Y, 1f,1.2f,0.8f,1f)
        AnimatorSet().apply {
            playTogether(scX,scY)
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
            while (true)
            {
                if(lastScore < score) {
                    lastScore++
                    Thread.sleep(10)
                    uiThread { txtScoreModeD.text = "امتیاز : $lastScore" }
                }
            }
        }
    }

    private fun initView() {
        (App.instance as ActivityGame).windowManager.defaultDisplay.getRealSize(p)

        LLEventsModeD.setOnTouchListener { v, event -> !((App.instance as ActivityGame).onTouchEvent(event)) }
        LLEventsModeD.setOnClickListener {
            runRV()
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
        blockView = RVModeD.layoutManager?.findViewByPosition(position)!!
        blockView = (blockView as LinearLayout).getChildAt(2)
    }

    private fun runRV() {
        if (gameRuning)
            return
        gameRuning = true
        doAsync {
            while (gameRuning) {
                uiThread {
                if (position != rVStep / itemHeight)
                    position = rVStep / itemHeight

                blockView.getLocationInWindow(arr)

                if (blockY < endY && blockY > startY) {
                    if (abs(puzzleLength - blockLength) > 2 * delta) {
                        gameRuning = false
                        toast("Accident")
                    }
                }

                    RVModeD.scrollBy(0, 10)
                }
                rVStep += 10
                Thread.sleep(10)
            }
        }

    }

    private var step: Int = 0

    override fun myTouchListener(dx: Int, dy: Int) {
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
     //   runRV()
    }

    override fun runHelper() {
    }

    override fun onDestroy() {
        gameRuning = false
        super.onDestroy()
    }

}