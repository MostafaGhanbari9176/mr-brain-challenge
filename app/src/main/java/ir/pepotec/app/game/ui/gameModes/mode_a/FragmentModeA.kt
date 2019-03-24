package ir.pepotec.app.game.ui.gameModes.mode_a

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.BounceInterpolator
import android.widget.LinearLayout
import ir.pepotec.app.game.R
import ir.pepotec.app.game.ui.App
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


    private lateinit var helperView: HelpModeA
    private var parentView: ViewGroup? = null
    private lateinit var puzzle: LinearLayout
    private var alpha: Float = 0f
    private var space: Int = 0
    private val p = Point()
    private var gameResult: Int = 0
    private lateinit var gameCreatorA: GameCreatorA
    var levelId: Int = -1
    private var firstTap = false
    private var gameStarted = false
    private lateinit var guideSpace: LinearLayout
    private lateinit var guidePuzzle: LinearLayout
    private val ctx: Context = App.instance
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mode_a, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        createGame(levelId)
    }

    private fun initViews() {

        puzzle = LLPuzzleA
        //GameParentA.setOnDragListener(this)

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
        DialogLoser("txt", this)

    }

    private fun runWinnerGame() {
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
        DialogWinner("", this, gameResult)

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

    override fun gameCreated(space: Int, alpha: Float, guideSpace: LinearLayout, guidePuzzle: LinearLayout) {
        this.space = space
        this.alpha = alpha
        this.guidePuzzle = guidePuzzle
        this.guideSpace = guideSpace
        txtAlphaA.text = alpha.toString()
    }

    private var step = 0

    override fun myTouchListener(dx: Int, dy: Int) {
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

        if (parentView == null) {
            parentView = (context as Activity).window.decorView as ViewGroup
            helperView = HelpModeA(ctx, puzzle, LLSpaceA, txtAlphaA, R.color.blue, this@FragmentModeA)
        }
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
        guidePuzzle.visibility = if (newWidth == space) View.VISIBLE else View.GONE
        guideSpace.visibility = if (newWidth == space) View.VISIBLE else View.GONE
    }

    override fun prevMenu() {
        (ctx as ActivityGame).finish()
    }

    override fun replay() {
        (ctx as ActivityGame).recreate()
    }

    override fun nextLevel() {
        if (levelId == 3)
            toast("notExist")
        else {
            (ctx as ActivityGame).finish()
            val intent = Intent(ctx, FragmentModeA::class.java)
            intent.putExtra("levelId", levelId + 1)
            startActivity(intent)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnHelpMA -> {
                removeHelperView()

            }
        }
    }

    private fun removeHelperView() {

        ObjectAnimator.ofFloat(helperView, View.ALPHA, 1f, 0f).apply {
            duration = 500
            addListener(object :Animator.AnimatorListener{
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