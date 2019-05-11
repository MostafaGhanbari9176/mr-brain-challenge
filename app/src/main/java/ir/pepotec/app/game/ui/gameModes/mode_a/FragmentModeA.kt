package ir.pepotec.app.game.ui.gameModes.mode_a

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.graphics.Point
import android.graphics.drawable.Animatable
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.BounceInterpolator
import android.widget.LinearLayout
import android.widget.Toast
import ir.pepotec.app.game.R
import ir.pepotec.app.game.model.Pref
import ir.pepotec.app.game.model.local_data_base.ModeADb
import ir.pepotec.app.game.presenter.PGameMode
import ir.pepotec.app.game.presenter.PModeALevel
import ir.pepotec.app.game.ui.App
import ir.pepotec.app.game.ui.activityMain.ActivityMain
import ir.pepotec.app.game.ui.dialog.DialogFinishMode
import ir.pepotec.app.game.ui.dialog.DialogLoser
import ir.pepotec.app.game.ui.dialog.DialogWinner
import ir.pepotec.app.game.ui.dialog.ResualtDialogResponse
import ir.pepotec.app.game.ui.gameModes.ActivityGame
import ir.pepotec.app.game.ui.uses.MyFragment
import kotlinx.android.synthetic.main.fragment_mode_a.*
import org.jetbrains.anko.doAsync
import kotlin.math.abs

class FragmentModeA : MyFragment(), GameCreatorA.GameCreatorInterface,
    ResualtDialogResponse, View.OnClickListener {


    private var isFinally = false
    private var loseNumber = 0
    private lateinit var helperView: HelpModeA
    private var parentView: ViewGroup? = null
    private lateinit var puzzle: LinearLayout
    private var alpha: Float = 0f
    private var space: Int = 0
    private val p = Point()
    private var gameResult: Int = 0
    private lateinit var gameCreatorA: GameCreatorA
    override var levelId: Int = 0
    private var firstTap = false
    private var gameStarted = false
    private lateinit var guideSpace: LinearLayout
    private lateinit var guidePuzzle: LinearLayout
    private val ctx: Context = App.instance
    private var fingerVector: Animatable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mode_a, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        createGame(levelId)

    }

    private fun initViews() {
        (GameParentA.background as AnimationDrawable).apply {
            setEnterFadeDuration(2000)
            setExitFadeDuration(4000)
            start()
        }
        puzzle = LLPuzzleA
        startImgBackAnimate()
    }

    private fun startImgBackAnimate() {
        val h = Handler()
        Thread(
            Runnable {
                while (true) {
                    for (i in 0..40) {
                        Thread.sleep(100)
                        h.post { imgBackA?.alpha = i / 100f }
                    }
                    for (i in 40 downTo 0) {
                        Thread.sleep(100)
                        h.post { imgBackA?.alpha = i / 100f }
                    }
                }
            }
        ).start()
    }

    private fun startGame() {
        gameStarted = true
        gameResult = result()
        when (gameResult) {
            0 -> runLoserGame()
            else -> runWinnerGame()
        }
    }

    private fun runLoserGame() {
        val xTransTo = (LLSpaceA.x + (LLSpaceA.width) / 2) - (puzzle.x + (puzzle.width) / 2)
        val yTransTo = (LLSpaceA.y) - (puzzle.y)
        val tranY =
            if (((puzzle.width) / alpha) < space)
                ObjectAnimator.ofFloat(puzzle, View.TRANSLATION_Y, 0f, yTransTo)
            else
                ObjectAnimator.ofFloat(puzzle, View.TRANSLATION_Y, 0f, yTransTo - puzzle.height)
        val tranX = ObjectAnimator.ofFloat(puzzle, View.TRANSLATION_X, 0f, xTransTo)
        val scX = ObjectAnimator.ofFloat(puzzle, View.SCALE_X, 1f, 1 / alpha)
        val set = AnimatorSet()
        set.playTogether(tranY, tranX, scX)
        set.duration = 1000
        set.interpolator = BounceInterpolator()
        set.start()
        set.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onAnimationEnd(animation: Animator?) {
                showLoserDialog()
            }

            override fun onAnimationCancel(animation: Animator?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onAnimationStart(animation: Animator?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    private fun showLoserDialog() {
        ModeADb(ctx).incrementLose(levelId)
        DialogLoser("txt", this, loseNumber >= 2, levelId, "a", isFinally)

    }

    private fun runWinnerGame() {
        PModeALevel().saveScore(levelId, gameResult)
        val xTransTo = (LLSpaceA.x + (LLSpaceA.width) / 2) - (puzzle.x + (puzzle.width) / 2)
        val yTransTo = (LLSpaceA.y) - (puzzle.y)
        val tranY = ObjectAnimator.ofFloat(puzzle, View.TRANSLATION_Y, 0f, yTransTo)
        val tranX = ObjectAnimator.ofFloat(puzzle, View.TRANSLATION_X, 0f, xTransTo)
        val scX = ObjectAnimator.ofFloat(puzzle, View.SCALE_X, 1f, 1 / alpha)
        val set = AnimatorSet()
        set.playTogether(tranY, tranX, scX)
        set.duration = 450
        set.start()
        set.interpolator = AccelerateInterpolator()
        set.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onAnimationEnd(animation: Animator?) {
                showWinnerDialog()
            }

            override fun onAnimationCancel(animation: Animator?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onAnimationStart(animation: Animator?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    private fun showWinnerDialog() {
        if (!isFinally)
            DialogWinner("ایول !", this, gameResult)
        else {
            toast("امتیاز این مرحله : ${result()}", Toast.LENGTH_LONG)
            DialogFinishMode(PGameMode().getModeSubject("a"), PGameMode().getScoreAverage("a"), this)
        }

    }

    private fun result(): Int =
        when (val v = (((puzzle.width) / alpha) / space) * 100) {
            in 90f..95f -> v.toInt()
            in 95f..105f -> 100
            in 105f..110f -> 90
            else -> 0

        }

    private fun createGame(gId: Int) {
        ((ctx as ActivityGame).windowManager.defaultDisplay).getRealSize(p)
        gameCreatorA = GameCreatorA(gId, GameParentA, LLRightA, LLLeftA, LLSpaceA, puzzle, p, this)
        gameCreatorA.createGame()
    }

    override fun gameCreated(
        space: Int,
        alpha: Float,
        guideSpace: LinearLayout,
        guidePuzzle: LinearLayout,
        isFinally: Boolean,
        loseNumber: Int
    ) {
        this.isFinally = isFinally
        this.space = space
        this.alpha = alpha
        this.guidePuzzle = guidePuzzle
        this.guideSpace = guideSpace
        this.loseNumber = loseNumber
        txtAlphaA.text = alpha.toString()
        if (levelId == 1) {
            imgFingerHelperA.visibility = View.VISIBLE
            fingerVector = imgFingerHelperA.drawable as Animatable
            fingerVector?.start()
        }
    }

    private var step = 0

    override fun myTouchListener(dx: Int, dy: Int) {
        if (fingerVector?.isRunning == true) {
            fingerVector?.stop()
            imgFingerHelperA.visibility = View.GONE
            ObjectAnimator.ofFloat(txtStartNews, View.ALPHA, 0f, 1f).apply {
                duration = 300
                interpolator = AccelerateInterpolator()
                start()
            }
        }
        step += dx
        if (abs(step) >= (p.x / 20)) {
            firstTap = false
            puzzle.requestLayout()
            val newWidth = puzzle.width + if (step > 0) (p.x / 40) else -(p.x / 40)
            puzzle.layoutParams.width = newWidth
            changeGuideVisibility(newWidth)
            step = 0
        }
    }

    override fun myClickListener() {
        if (!firstTap)
            runStartTapTimer()
        else if (!gameStarted)
            startGame()
    }

    override fun runHelper() {
        ActivityMain.musicService?.decreaseVolume()
        if (parentView == null)
            parentView = (context as Activity).window.decorView as ViewGroup

        helperView = HelpModeA(ctx, puzzle, LLSpaceA, txtAlphaA, R.color.primaryColor, this@FragmentModeA)
        parentView?.addView(helperView)

    }

    private fun runStartTapTimer() {
        firstTap = true
        doAsync {
            Thread.sleep(250)
            firstTap = false
        }
    }

    private fun changeGuideVisibility(newWidth: Int) {
        if (alpha != 1f) {
            guidePuzzle.visibility = if (newWidth == space) View.VISIBLE else View.GONE
            guideSpace.visibility = if (newWidth == space) View.VISIBLE else View.GONE
        }
    }

    override fun prevMenu() {
        (ctx as ActivityGame).apply {
            setResult(RESULT_OK, intent.putExtra("modeId", "a"))
            finish()
        }
    }

    override fun replay() {
        (ctx as ActivityGame).startGame("a", levelId)
        App.fullScreen(ctx as ActivityGame)
    }

    override fun nextLevel() {
        if (isFinally) {
            (ctx as ActivityGame).startGame("c", 1)
        } else {
            levelId++
            (ctx as ActivityGame).startGame("a", levelId)
        }
        App.fullScreen(ctx as ActivityGame)

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnHelpMA -> {
                removeHelperView()
                ActivityMain.musicService?.increaseVolume()
            }
        }
    }

    private fun removeHelperView() {

        ObjectAnimator.ofFloat(helperView, View.ALPHA, 1f, 0f).apply {
            duration = 500
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    parentView?.removeView(helperView)
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }
            })

            start()
        }


    }

}