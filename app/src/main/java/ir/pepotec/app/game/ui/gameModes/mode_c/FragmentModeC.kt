package ir.pepotec.app.game.ui.gameModes.mode_c

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import android.widget.RelativeLayout
import ir.pepotec.app.game.R
import ir.pepotec.app.game.model.local_data_base.ModeCDb
import ir.pepotec.app.game.presenter.PGameMode
import ir.pepotec.app.game.presenter.PModeCLevel
import ir.pepotec.app.game.ui.App
import ir.pepotec.app.game.ui.dialog.DialogFinishMode
import ir.pepotec.app.game.ui.dialog.DialogLoser
import ir.pepotec.app.game.ui.dialog.DialogWinner
import ir.pepotec.app.game.ui.dialog.ResualtDialogResponse
import ir.pepotec.app.game.ui.gameModes.ActivityGame
import ir.pepotec.app.game.ui.uses.MyFragment
import kotlinx.android.synthetic.main.fragment_mode_c.*
import org.jetbrains.anko.doAsync
import kotlin.math.abs

class FragmentModeC : MyFragment(), GameCreatorC.GameCreatorInterface, ResualtDialogResponse {

    override var levelId: Int = 0
    private var loseNumber:Int = 0
    private val ctx: Context = App.instance
    private var gameStarted = false
    private var firstTap = false
    private val p = Point()
    private var step = 0
    private var space = 0
    private var alpha = 0f
    private var isFinally = false
    private lateinit var guideBus: LinearLayout
    private lateinit var guidePuzzle: LinearLayout
    private lateinit var guideSpace: LinearLayout
    private var scaleX: ValueAnimator? = null
    private var lose = false
    private var animRun = true
    private var puzzlePosition = RelativeLayout.CENTER_HORIZONTAL
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mode_c, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (ctx as ActivityGame).windowManager.defaultDisplay.getRealSize(p)
        (GameParentC.background as AnimationDrawable).apply {
            setEnterFadeDuration(2000)
            setExitFadeDuration(4000)
            start()
        }
        createGame()
    }

    private fun createGame() {
        GameCreatorC(
            levelId,
            GameParentC,
            LLRightSeatC,
            LLLeftSeatC,
            LLSeatC,
            LLRightBusC,
            LLLeftBusC,
            LLBusC,
            LLPuzzleC,
            this
        ).createGame()
    }

    override fun gameCreated(
        space: Int,
        alpha: Float,
        guideSpace: LinearLayout,
        guideBus: LinearLayout,
        guidePuzzle: LinearLayout,
        isFinally: Boolean,
        puzzlePosition: Int,
        loseNumber:Int
    ) {
        this.puzzlePosition = puzzlePosition
        this.isFinally = isFinally
        this.space = space
        this.alpha = alpha
        this.guidePuzzle = guidePuzzle
        this.guideSpace = guideSpace
        this.guideBus = guideBus
        txtAlphaC.text = alpha.toString()
        guideBus.x = LLBusC.x + (LLBusC.width - guideBus.width) / 2
    }

    override fun myTouchListener(dx: Int, dy: Int) {

        if (!gameStarted) {
            changePuzzleLength(dx)
        } else {
            changePuzzlePosition(dx)
        }

    }

    private fun changePuzzlePosition(dx: Int) {

        step += dx
        if (abs(step) >= (p.x / 80)) {
            if (dx > 0 && (LLPuzzleC.x + LLPuzzleC.width) < p.x) {
                if (LLPuzzleC.x + LLPuzzleC.width + dx > p.x) {
                    LLPuzzleC.x = p.x - LLPuzzleC.width.toFloat()
                } else
                    LLPuzzleC.x += if (step > 0) (p.x / 80) else -(p.x / 80)
            } else if (dx < 0 && LLPuzzleC.x > 0) {
                if (LLPuzzleC.x + dx < 0) {
                    LLPuzzleC.x = 0f
                } else
                    LLPuzzleC.x += if (step > 0) (p.x / 80) else -(p.x / 80)
            }
            step = 0
        }

    }

    private fun changePuzzleLength(dx: Int) {
        guideBus.x = LLBusC.x + (LLBusC.width - guideBus.width) / 2
        step += dx
        if (abs(step) >= (p.x / 20)) {
            firstTap = false
            LLPuzzleC.requestLayout()
            val newWidth = LLPuzzleC.width + if (step > 0) (p.x / 40) else -(p.x / 40)
            LLPuzzleC.layoutParams.width = newWidth
            changeGuideVisibility(newWidth)
            step = 0
        }
    }

    private fun changeGuideVisibility(newWidth: Int) {
        if (alpha != 1f) {
            guidePuzzle.visibility = if (newWidth == space) View.VISIBLE else View.GONE
            guideSpace.visibility = if (newWidth == space) View.VISIBLE else View.GONE
            guideBus.visibility = if (newWidth == space) View.VISIBLE else View.GONE
        }
    }

    override fun myClickListener() {
        if (!firstTap)
            runStartTapTimer()
        else if (!gameStarted)
            startGame()
    }

    private fun startGame() {
        gameStarted = true
        step = 0
        runPuzzle()
    }

    private fun runPuzzle() {
        val h = Handler()
        val to = LLSeatC.y
        Thread(
            Runnable {
                var v = 0f
                while (v <= to && animRun) {
                    v += 5
                    if (v > to) {
                        v = to
                        animRun = false
                    }
                    h.post { LLPuzzleC.translationY = v }
                    checkAccident()
                    if (lose) {
                        h.post { showLoserDialog() }
                        break
                    }
                    Thread.sleep(10)
                }
                if (!lose)
                    h.post{showDialogWinner()}
            }
        ).start()
        scaleX = ValueAnimator.ofFloat(LLPuzzleC.width.toFloat(), LLPuzzleC.width / alpha).apply {
            duration = 100
            addUpdateListener {
                LLPuzzleC.requestLayout()
                LLPuzzleC.layoutParams.width = (it.animatedValue as Float).toInt()
            }
            start()
        }

    }

    private fun showDialogWinner() {
        val score: Int =
            when (val v = ((LLPuzzleC.width.toFloat() / LLSeatC.width.toFloat()) * 100).toInt()) {
                in 100..150 -> 100
                else -> v
            }
        PModeCLevel().saveScore(levelId, score)
        if (!isFinally)
            DialogWinner("", this, score)
        else {
            toast("امتیاز این مرحله : $score")
            DialogFinishMode(PGameMode().getModeSubject("c"), PGameMode().getScoreAverage("c"), this)
        }
    }

    private fun checkAccident() {
        if (LLPuzzleC.y > (LLBusC.y - LLPuzzleC.height) && LLPuzzleC.y < (LLBusC.y + LLBusC.height)) {
            if (LLPuzzleC.x + 5 > LLBusC.x && (LLPuzzleC.x + LLPuzzleC.width - 5 < LLBusC.x + LLBusC.width)) {

            } else {
                animRun = false
                lose = true
            }
        } else if (LLPuzzleC.y > (LLSeatC.y - LLSeatC.height)) {
            if (abs(LLPuzzleC.x - LLSeatC.x) > 20 || abs(LLPuzzleC.width - LLSeatC.width) > 20) {
                animRun = false
                lose = true
            }
        }
    }

    private fun showLoserDialog() {
        ModeCDb(ctx).incrementLose(levelId)
        DialogLoser("", this, loseNumber >= 2, levelId, "c", isFinally)
    }

    private fun runStartTapTimer() {
        firstTap = true
        doAsync {
            Thread.sleep(250)
            firstTap = false
        }
    }

    override fun runHelper() {

    }

    override fun onDestroy() {
        animRun = false
        scaleX?.cancel()
        super.onDestroy()
    }

    override fun prevMenu() {
        (ctx as ActivityGame).apply {
            setResult(Activity.RESULT_OK, intent.putExtra("mode_id", "c"))
            finish()
        }
    }

    override fun replay() {
        (ctx as ActivityGame).startGame("c", levelId)
        App.fullScreen(ctx as ActivityGame)
    }

    override fun nextLevel() {
        if (isFinally) {
            (ctx as ActivityGame).startGame("b", 1)
        } else {
            levelId++
            (ctx as ActivityGame).startGame("c", levelId)
        }
        App.fullScreen(ctx as ActivityGame)

    }
}