package ir.pepotec.app.game.ui.gameModels.mode_a

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Point
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.widget.LinearLayout
import ir.pepotec.app.game.R
import ir.pepotec.app.game.ui.App
import kotlinx.android.synthetic.main.activity_a.*
import org.jetbrains.anko.toast
import kotlin.math.abs

class ActivityModeA : AppCompatActivity(), View.OnDragListener, GameCreatorA.GameCreatorInterface {

    private var mLastTouchX: Int = 0
    lateinit var puzzle: LinearLayout
    var alpha: Float = 0f
    var space: Int = 0
    val p = Point()
    var gameResult: Int = 0
    lateinit var gameCreatorA: GameCreatorA

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a)
        App.instance = this
        if ((intent?.extras?.isEmpty) == false) {
            val gId = intent.getIntExtra("gId", -1)
            initViews()
            createGame(gId)
        } else
            toast("Error")


    }

    override fun onResume() {
        super.onResume()
        App.instance = this
    }

    private fun initViews() {
        puzzle = LLPuzzleA
        App.fullScreen(this)
        GameParent.setOnDragListener(this)
        btnStartGame.setOnClickListener {
            startGame()
        }
    }

    private fun startGame() {
        gameResult = result()
        when (gameResult) {
            0 -> runLoserGame()
            else -> runWinnerGame()
        }
    }

    private fun runLoserGame() {
        val transTo = (LLSpaceA.x + (LLSpaceA.width) / 2) - (puzzle.x + (puzzle.width) / 2)
        val tranY =
        if(((puzzle.width) / alpha) < space)
            ObjectAnimator.ofFloat(puzzle, View.TRANSLATION_Y, puzzle.y, LLSpaceA.y)
        else
            ObjectAnimator.ofFloat(puzzle, View.TRANSLATION_Y, puzzle.y, LLSpaceA.y - puzzle.height)
        val tranX = ObjectAnimator.ofFloat(puzzle, View.TRANSLATION_X, 0f, transTo)
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
                showResult()
                showLoseMessage()
            }

            override fun onAnimationCancel(animation: Animator?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onAnimationStart(animation: Animator?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    private fun runWinnerGame() {
        val transTo = (LLSpaceA.x + (LLSpaceA.width) / 2) - (puzzle.x + (puzzle.width) / 2)
        val tranY = ObjectAnimator.ofFloat(puzzle, View.TRANSLATION_Y, puzzle.y, LLSpaceA.y)
        val tranX = ObjectAnimator.ofFloat(puzzle, View.TRANSLATION_X, 0f, transTo)
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
                (imgAnim.drawable as Animatable).start()
                showResult()
            }

            override fun onAnimationCancel(animation: Animator?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onAnimationStart(animation: Animator?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    private fun showResult() {
        toast(gameResult.toString())
    }

    private fun result(): Int =
        when (val v = (((puzzle.width) / alpha) / space) * 100) {
            in 90f..95f -> v.toInt()
            in 95f..105f -> 100
            in 105f..110f -> 90
            else -> 0

        }

    private fun showLoseMessage() {
        val scX = ObjectAnimator.ofFloat(loseMessage, View.SCALE_X, 0f, 1.5f, 0.6f, 1f)
        val scY = ObjectAnimator.ofFloat(loseMessage, View.SCALE_Y, 0f, 1.8f, 0.8f, 1f)
        val op = ObjectAnimator.ofFloat(loseMessage, View.ALPHA, 0.5f, 1f)
        val op2 = ObjectAnimator.ofFloat(loseMessage, View.ALPHA, 1f, 0f)
        op2.startDelay = 500
        val set = AnimatorSet()
        set.playTogether(scX, scY, op)
        set.duration = 250
        set.start()
    }

    private fun createGame(gId: Int) {
        (windowManager.defaultDisplay).getRealSize(p)
        gameCreatorA = GameCreatorA(gId, GameParent, LLRightA, LLLeftA, LLSpaceA, puzzle, p, this)
        gameCreatorA.createGame()
    }

    override fun gameCreated(space: Int, alpha: Float) {
        this.space = space
        this.alpha = alpha
        txtAlpha.text = alpha.toString()
    }

    override fun onDrag(v: View, dragEvent: DragEvent): Boolean {
        val view = dragEvent.localState as View
        when (dragEvent.action) {

            DragEvent.ACTION_DRAG_STARTED -> {
                view.x = dragEvent.x - view.width / 2
                view.y = dragEvent.y - view.height / 2
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
            }
            DragEvent.ACTION_DRAG_EXITED -> {
            }
            DragEvent.ACTION_DRAG_LOCATION -> {
                view.x = dragEvent.x - view.width / 2
                view.y = dragEvent.y - view.height / 2
            }
            DragEvent.ACTION_DRAG_ENDED -> {
            }
            DragEvent.ACTION_DROP -> {
                view.x = dragEvent.x - view.width / 2
                view.y = dragEvent.y - view.height / 2
            }
        } // view.setX(dragEvent.getX());
        // view.setY(dragEvent.getY());
        //  view.setX(dragEvent.getX());
        //  view.setY(dragEvent.getY());
        // view.setX(dragEvent.getX());
        // view.setY(dragEvent.getY());
        return true
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        var dx: Int = 0
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mLastTouchX = ev.x.toInt()
            }

            MotionEvent.ACTION_MOVE -> {
                val x = ev.x.toInt()
                dx = x - mLastTouchX
                mLastTouchX = x
            }

            MotionEvent.ACTION_UP -> {
            }//mPosX = 0;

            MotionEvent.ACTION_CANCEL -> {
            }

            MotionEvent.ACTION_POINTER_UP -> {
            }
        }
        changePuzzleSize(dx)

        return true
    }

    private var step = 0

    private fun changePuzzleSize(dx: Int) {
        step+=dx
        if(abs(step) >= (p.x / 20))
        {
            puzzle.requestLayout()
            puzzle.layoutParams.width = puzzle.width + (step / 2)
            step = 0
        }
    }


}