package ir.pepotec.app.game.ui.gameModes.mode_d

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Path
import android.graphics.Point
import android.graphics.drawable.Animatable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import ir.pepotec.app.game.R
import ir.pepotec.app.game.model.Pref
import ir.pepotec.app.game.presenter.PModeDLevel
import ir.pepotec.app.game.ui.App
import ir.pepotec.app.game.ui.activityMain.ActivityMain
import ir.pepotec.app.game.ui.gameModes.ActivityGame
import ir.pepotec.app.game.ui.uses.MyFragment
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.dialog_mode_d.view.*
import kotlinx.android.synthetic.main.dialog_start_infinite.*
import kotlinx.android.synthetic.main.dialog_start_infinite.view.*
import kotlinx.android.synthetic.main.fragment_mode_d.*
import kotlinx.android.synthetic.main.item_mode_d.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.lang.Exception
import kotlin.math.abs


class FragmentModeD : MyFragment(), PModeDLevel.PModeDInterface, AdapterModeD.InterFaceAdapterD {

    override var levelId: Int = 0
    private var brainView: View? = null
    private var viewList = HashMap<Int, View>()
    private var dialogShowing = false
    private val pref = Pref()
    private var gameSpeed = 15L
    private var gameSpeed2 = 900
    private var addRefrense = 50
    private var score = 0
    private val startY: Float
        get() = LLPuzzleModeD.y - LLPuzzleModeD.height + 20
    private val endY: Float
        get() = LLPuzzleModeD.y + LLPuzzleModeD.height - 20
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
    private var eatPosition: Int = -1
    private var position: Int = 1
        set(value) {
            field = value
            initBlock()
            setScore()
        }
    private var gameRuning = false
    private var delCount = 5
    private var puzzleIsGhost = false
    private var puzzleIsHide = false
    private var handlerGhost: Handler = Handler()
    private var handlerRun: Handler = Handler()
    private var lastLength: Int = 0

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mode_d, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        delta = (p.x / 6) - (p.x / 8)
        itemHeight = p.y / 2
    }

    private fun setScore() {
        if (position > addRefrense) {
            ActivityMain.musicService?.addDel()
            txtDel.text = "${++delCount}"
            addRefrense += 30
        }
        if (position == 0)
            lastLength = blockLength
        else {
            score += (abs(lastLength - blockLength) * 6 / p.x) * 10
            lastLength = blockLength
        }
        //  handlerRun.post {
        val scX = ObjectAnimator.ofFloat(txtScoreModeD, View.SCALE_X, 1f, 1.2f, 0.8f, 1f)
        val scY = ObjectAnimator.ofFloat(txtScoreModeD, View.SCALE_Y, 1f, 1.2f, 0.8f, 1f)
        AnimatorSet().apply {
            playTogether(scX, scY)
            duration = 100
            interpolator = DecelerateInterpolator()
            start()
        }
        //   }

    }

    private fun scoreIncrement() {
        val h = Handler()
        Thread(
            Runnable {
                while (gameRuning) {
                    if (lastScore < score) {
                        lastScore++
                        Thread.sleep(10)
                        h.post {
                            txtScoreModeD.text = "امتیاز : $lastScore"
                        }
                    }
                }
            }
        ).start()
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
        showStartDialog()
    }

    private fun showStartDialog() {
        val d = Dialog(ctx)
        val v = LayoutInflater.from(ctx).inflate(R.layout.dialog_start_infinite, null, false)
        v.txtHSDialogStart.text = "${pref.getIntegerValue(Pref.score, 0)}"
        v.txtHBDialogStart.text = "${pref.getIntegerValue(Pref.block, 0)}"
        v.btnEasyInfinite.setOnClickListener {
            initRV(true)
            d.cancel()
        }
        v.btnDificuktInfinite.setOnClickListener {
            initRV(false)
            d.cancel()
        }
        v.btnBackStartDialogInfinite.setOnClickListener {
            d.cancel()
            back()
        }
        d.setContentView(v)
        d.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        d.CVDialogStartInfinite.layoutParams.width = p.x - 40
        d.setCancelable(false)
        d.show()
    }

    private fun initRV(easy: Boolean) {
        RVModeD.layoutManager = LinearLayoutManager(ctx)
        RVModeD.adapter = AdapterModeD(p, easy, this)
    }

    private fun startMusic() {
        ActivityMain.musicService?.apply {
            startMusic(R.raw.infinite)
        }
    }

    private fun initPuzzle() {
        LLPuzzleParentModeD.layoutParams.height = p.y / 2
        LLPuzzleRightModeD.layoutParams.width = p.x / 8
        LLPuzzleLeftModeD.layoutParams.width = p.x / 8
    }

    private fun initBlock() {
        try {
            (viewList.getValue(position)).apply {
                brainView = this.imgBrainItemModeD
                blockView = this.LLBlockItemModeD
            }
        } catch (e: Exception) {
            Log.d("Mostafa", e.message)
            e.message
            gameRuning = false
            onResume()
        }
    }

    private fun runRV() {
        startMusic()
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
                                gameSpeed = 15L
                                accident()
                            } else if (brainView?.scaleX == 1f && !puzzleIsGhost && position != eatPosition) {
                                Log.d("Mostafa", "** EAT **")
                                eatPosition = position
                                eatBrain()
                            }
                        }

                        //  handlerRun.post {
                        RVModeD.requestLayout()
                        RVModeD.scrollBy(0, 10)
                        // }
                    } catch (e: Exception) {
                        e.message
                    }
                }
                rVStep += 10
                Thread.sleep(gameSpeed, gameSpeed2)
                changeSpeed()
            }
        }
        Thread(r).start()
        scoreIncrement()
    }

    private fun eatBrain() {
        ActivityMain.musicService?.eatSound()
        val current = Pref().getIntegerValue(Pref.brain, 100)
        Pref().saveIntegerValue(Pref.brain, current + 5)
        val h = Handler()
        Thread(
            Runnable {
                h.post {
                    val x = ObjectAnimator.ofFloat(brainView, View.SCALE_X, 1f, 1.5f, 0f)
                    val y = ObjectAnimator.ofFloat(brainView, View.SCALE_Y, 1f, 1.5f, 0f)
                    AnimatorSet().apply {
                        playTogether(x, y)
                        duration = 100
                        start()
                    }
                    (ctx as ActivityGame).txtBarinNumberMain.text="${current+5}"
                }
            }
        ).start()

    }

    private fun changeSpeed() {
        if (gameSpeed2 > 0)
            gameSpeed2 -= 2
        else if (gameSpeed > 5) {
            gameSpeed2 = 900
            gameSpeed--
        }
    }

    private fun accident() {
        ActivityMain.musicService?.accident()
        if (delCount == 0) {
            gameRuning = false
            PModeDLevel(this).saveData(score, position)
        } else {
            //  handlerRun.post {
            txtDel.text = "${--delCount}"
            animateDel()
            //   }
            ghostingPuzzle()
        }

    }

    private fun ghostingPuzzle() {
        puzzleIsGhost = true
        val r = Runnable {
            for (i in 1..25) {
                if (!gameRuning)
                    break
                try {
                    if (puzzleIsHide) {
                        handlerGhost.post { LLPuzzleParentModeD.alpha = 1f }
                        puzzleIsHide = false
                    } else {
                        handlerGhost.post { LLPuzzleParentModeD.alpha = 0f }
                        puzzleIsHide = true
                    }
                } catch (e: Exception) {
                }
                Thread.sleep(100)
            }
            handlerGhost.post { LLPuzzleParentModeD.alpha = 1f }
            puzzleIsGhost = false
        }
        Thread(r).apply { start() }

    }

    private fun animateDel() {
        (imgDel.drawable as Animatable).apply {
            stop()
            start()
        }
    }

    private var step: Int = 0

    override fun myTouchListener(dx: Int, dy: Int) {
        step += dx
        if (abs(step) >= (p.x / 100)) {
            myClickListener()
            var newWidth = LLPuzzleModeD.width + if (step > 0) (p.x / 28) else -(p.x / 28)
            if (newWidth + p.x / 4 > p.x)
                newWidth = p.x - p.x / 4
            val param = LinearLayout.LayoutParams(newWidth, App.getBlockHeight().toInt())
            LLPuzzleModeD.layoutParams = param
            step = 0
        }
    }

    override fun myClickListener() {
        if (!gameRuning)
            runRV()
    }

    override fun runHelper() {
    }

    override fun onDestroy() {
        gameRuning = false
        super.onDestroy()
    }

    override fun result(lScore: Int, lBlock: Int) {
        if (dialogShowing)
            return
        dialogShowing = true
        // handlerRun.post {
        showStopDialog(true, lScore, lBlock)
        // }
    }

    private fun back() {
        (ctx as ActivityGame).apply {
            setResult(Activity.RESULT_OK, intent.putExtra("mode_id", "d"))
            finish()
        }
    }

    fun stopGame() {
        puzzleIsGhost = true
        gameRuning = false
        ActivityMain.musicService?.stop()
        showStopDialog()

    }

    private fun showStopDialog(
        gameFinished: Boolean = false,
        lScore: Int = pref.getIntegerValue(Pref.score, 0),
        lBlock: Int = pref.getIntegerValue(Pref.block, 0)
    ) {
        ActivityMain.musicService?.stopMusic()
        val d = Dialog(ctx)
        val v = LayoutInflater.from(ctx).inflate(R.layout.dialog_mode_d, null, false)
        d.setContentView(v)
        d.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        v.CVDialogModeD.layoutParams.width = p.x - 40
        v.txtScoreDialogD.text = "$score"
        v.txtBlockDialogD.text = "$position"
        v.txtHSDialogD.text = "$lScore"
        v.txtHBModeD.text = "$lBlock"
        v.LLHighScore.visibility = if (score > lScore) View.VISIBLE else View.GONE
        v.btnContinueInfinite.visibility = if (gameFinished) View.GONE else View.VISIBLE
        v.btnBackModeD.setOnClickListener {
            d.cancel()
            if (!gameFinished) {
                dialogShowing = true
                PModeDLevel(this).saveData(score, position)
            }
            back()
        }
        v.btnRepeatModeD.setOnClickListener {
            d.cancel()
            dialogShowing = true
            PModeDLevel(this).saveData(score, position)
            (ctx as ActivityGame).startGame("d", levelId)
            App.fullScreen(ctx as ActivityGame)
        }
        v.btnContinueInfinite.setOnClickListener {
            d.cancel()
            runRV()
            ghostingPuzzle()
        }
        d.setCancelable(false)
        d.show()
    }

    override fun currentView(v: View, position: Int) {
        if (viewList.size >= 7)
            viewList.remove(position - 7)
        viewList[position] = v
    }

}