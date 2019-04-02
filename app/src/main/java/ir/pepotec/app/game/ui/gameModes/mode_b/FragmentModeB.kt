package ir.pepotec.app.game.ui.gameModes.mode_b

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import ir.pepotec.app.game.R
import ir.pepotec.app.game.presenter.PGameMode
import ir.pepotec.app.game.presenter.PModeBLevel
import ir.pepotec.app.game.ui.App
import ir.pepotec.app.game.ui.dialog.DialogFinishMode
import ir.pepotec.app.game.ui.dialog.DialogLoser
import ir.pepotec.app.game.ui.dialog.DialogWinner
import ir.pepotec.app.game.ui.dialog.ResualtDialogResponse
import ir.pepotec.app.game.ui.gameModes.ActivityGame
import ir.pepotec.app.game.ui.uses.MyFragment
import kotlinx.android.synthetic.main.fragment_mode_b.*
import org.jetbrains.anko.doAsync
import kotlin.math.abs

class FragmentModeB:MyFragment(), GameCreatorB.GameCreatorInterface, ResualtDialogResponse{

    override var levelId: Int = 0
    private  var blockHeight = 0
    private var isFinally = false
    private lateinit var guidePuzzle:LinearLayout
    private lateinit var guideSeat:LinearLayout
    private val ctx: Context = App.instance
    private var gameStarted: Boolean = false
    private var firstTap: Boolean = false
    private val p = Point()
    private var lose = false
    private var space = 0
    private var alpha = 1f
    private var transY :ObjectAnimator? = null
    private var scaleX: ValueAnimator? = null
    private var set: AnimatorSet? = null
    private lateinit var seatAnimator :ValueAnimator
    private var limit = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mode_b, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        GameCreatorB(levelId, ParentModeB, LLPuzzleModeB,LLSeatModeB, LLRightB, LLLeftB, p, this).createGame()
    }

    private fun initView() {
        animateBackgrounds()
        blockHeight = App.getBlockHeight().toInt()
        (ctx as  ActivityGame).windowManager.defaultDisplay.getRealSize(p)
        limit = p.y - 2 * blockHeight
    }

    private fun animateBackgrounds() {
        (ParentModeB.background as AnimationDrawable).apply {
            setExitFadeDuration(6000)
            setEnterFadeDuration(2000)
           start()
        }
/*        (LLPuzzleModeB.background as AnimationDrawable).apply {
            setExitFadeDuration(4000)
            setEnterFadeDuration(2000)
            start()
        }
        (LLRightB.background as AnimationDrawable).apply {
            setExitFadeDuration(4000)
            setEnterFadeDuration(2000)
            start()
        }
        (LLLeftB.background as AnimationDrawable).apply {
            setExitFadeDuration(4000)
            setEnterFadeDuration(2000)
            start()
        }*/
    }

    private var step = 0

    override fun myTouchListener(dx: Int, dy: Int) {
        if(gameStarted)
            return
        step += dx
        if (abs(step) >= (p.x / 20)) {
            firstTap = false
            LLPuzzleModeB.requestLayout()
            val newWidth = LLPuzzleModeB.width + if (step > 0) (p.x / 40) else -(p.x / 40)
            LLPuzzleModeB.layoutParams.width = newWidth
            changeGuideVisibility(newWidth)
            step = 0
        }
    }

    private fun changeGuideVisibility(newWidth: Int) {
        if (alpha != 1f) {
            guidePuzzle.visibility = if (newWidth == space) View.VISIBLE else View.GONE
            guideSeat.visibility = if (newWidth == space) View.VISIBLE else View.GONE
        }
    }

    override fun myClickListener() {
        if (!firstTap)
            runStartTapTimer()
        else if (!gameStarted)
            startGame()
    }

    override fun runHelper() {

    }

    private fun startGame() {
        gameStarted = true
        runPuzzle()
    }

    private fun runPuzzle() {
        val transTo : Float = LLSeatModeB.y - LLPuzzleModeB.y
        transY = ObjectAnimator.ofFloat(LLPuzzleModeB, View.TRANSLATION_Y, 0f, transTo).apply {
            duration = 300
            addListener(object : Animator.AnimatorListener{
                override fun onAnimationRepeat(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    seatAnimator.cancel()
                    if(!lose)
                        showDialogWinner()
                }

                override fun onAnimationCancel(animation: Animator?) {

                }

                override fun onAnimationStart(animation: Animator?) {
                }
            })
            addUpdateListener {
                val value = it.animatedValue as Float
                if(value > limit)
                {
                    if (abs(LLPuzzleModeB.x - LLSeatModeB.x) > 40)
                    {
                        lose = true
                        seatAnimator.cancel()
                        set?.cancel()
                        showDialogLoser()
                    }

                }
            }
        }

        scaleX = ValueAnimator.ofFloat(LLPuzzleModeB.width.toFloat(), LLPuzzleModeB.width / alpha).apply {
            duration = 50
            addUpdateListener {
                LLPuzzleModeB.requestLayout()
                LLPuzzleModeB.layoutParams.width = (it.animatedValue as Float).toInt()
            }
        }

       set =  AnimatorSet().apply {
            playTogether(transY, scaleX)
            start()
        }
    }

    private fun showDialogWinner() {
        val score:Int = ((LLPuzzleModeB.width.toFloat() / LLSeatModeB.width.toFloat())*100).toInt()
        PModeBLevel().saveScore(levelId, score)
        if(!isFinally)
        DialogWinner("", this, score)
        else{
            toast("امتیاز این مرحله : $score")
            DialogFinishMode(PGameMode().getModeSubject("b"), PGameMode().getScoreAverage("b"), this)
        }
    }

    private fun showDialogLoser() {
        DialogLoser("", this)
    }

    private fun runStartTapTimer() {
        firstTap = true
        doAsync {
            Thread.sleep(250)
            firstTap = false
        }
    }

    override fun gameCreated(
        space: Int,
        alpha: Float,
        guideSeat: LinearLayout,
        guidePuzzle: LinearLayout,
        isFinally: Boolean
    ) {
        this.space = space
        this.alpha = alpha
        this.guidePuzzle = guidePuzzle
        this.guideSeat = guideSeat
        this.isFinally = isFinally
        txtAlphaB.text = "$alpha"
        runSeat()
    }

    private fun runSeat() {
        val from = 0
        val to = (p.x - space)
       seatAnimator = ValueAnimator.ofInt(from, to).apply{
            duration = 2000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            addUpdateListener {
                val value = it.animatedValue as Int
                LLRightB.requestLayout()
                LLRightB.layoutParams.width = value
                LLLeftB.requestLayout()
                LLLeftB.layoutParams.width = to - value
            }
            start()

        }

    }

    override fun onDestroy() {
        set?.cancel()
        seatAnimator.cancel()
        super.onDestroy()
    }

    override fun prevMenu() {
        (ctx as ActivityGame).apply {
            setResult(Activity.RESULT_OK, intent.putExtra("mode_id", "b"))
            finish()
        }
    }

    override fun replay() {
        (ctx as ActivityGame).startGame("b", levelId)
        App.fullScreen(ctx as ActivityGame)
    }

    override fun nextLevel() {
        if (isFinally) {
            (ctx as ActivityGame).startGame("c", 1)
        } else {
            levelId++
            (ctx as ActivityGame).startGame("b", levelId)
        }
        App.fullScreen(ctx as ActivityGame)

    }

}