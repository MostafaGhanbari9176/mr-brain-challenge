package ir.pepotec.app.game.ui.gameModes.mode_a

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import ir.pepotec.app.game.R
import ir.pepotec.app.game.ui.App
import ir.pepotec.app.game.ui.uses.ButtonEvent
import kotlinx.android.synthetic.main.help_mode_a.view.*
import org.jetbrains.anko.toast

@SuppressLint("ViewConstructor")
class HelpModeA(
    private val ctx: Context,
    private val puzzle: View,
    private val space: View,
    private val txtAlpha: TextView,
    @ColorRes private val parentColor: Int,
    private val listener: OnClickListener
) : RelativeLayout(ctx, null), View.OnTouchListener {

    companion object {
        lateinit var c: Canvas
    }

    private lateinit var presenterView: RelativeLayout
    private var presentChildNumber: Int = 0
    private val parentDuration = 500
    private val childDuration = 400L
    private val childDelay = 200
    private var presentChildDelay = 500L
    private val parentPaint = Paint()
    private val transPaint = Paint()
    private var puzzleAnimator: ValueAnimator? = null
    private var spaceAnimator: ValueAnimator? = null
    private var alphaAnimator: ValueAnimator? = null

    init {
        setOnClickListener {

        }
        bringToFront()
        setWillNotDraw(false)
        setLayerType(View.LAYER_TYPE_HARDWARE, null)
        transPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
        parentAnimating()
        startPuzzleAnimate()
        startSpaceAnimate()
        startAlphaAnimate()
        initPresenterView()
    }

    private fun parentAnimating() {
        ObjectAnimator.ofFloat(this, View.ALPHA, 0f, 1f).apply {
            duration = parentDuration.toLong()
            start()
        }

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        c = canvas
        drawParent()
        if (puzzleAnimator != null)
            drawPuzzle()
        if (spaceAnimator != null)
            drawSpace()
        if (alphaAnimator != null)
            drawAlpha()

    }

    private fun startPuzzleAnimate() {
        val max = Math.max(puzzle.width, puzzle.height)
        puzzleAnimator = ValueAnimator.ofFloat(max / 2f, 0f, -50f, -20f).apply {
            startDelay = childDelay.toLong()
            duration = childDuration.toLong()
            start()
            addUpdateListener {
                this@HelpModeA.invalidate()
            }
        }
    }

    private fun startSpaceAnimate() {
        val max = Math.max(space.width, space.height)
        spaceAnimator = ValueAnimator.ofFloat(max / 2f, 0f, -50f, -20f).apply {
            startDelay = childDelay.toLong()
            duration = childDuration.toLong()
            start()
            addUpdateListener {
                this@HelpModeA.invalidate()
            }
        }
    }

    private fun startAlphaAnimate() {
        val r = txtAlpha.width / 2f
        alphaAnimator = ValueAnimator.ofFloat(0f, r + 30f, r + 10f).apply {
            startDelay = childDelay.toLong()
            duration = childDuration.toLong()
            start()
            addUpdateListener {
                this@HelpModeA.invalidate()
            }
        }
    }

    private fun drawPuzzle() {
        val v = puzzleAnimator?.animatedValue as Float
        c.drawRoundRect(
            puzzle.x + v,
            puzzle.y + v,
            (puzzle.x + puzzle.width) - v,
            (puzzle.y + puzzle.height) - v,
            20f,
            20f,
            transPaint
        )

    }

    private fun drawSpace() {
        val v = spaceAnimator?.animatedValue as Float
        c.drawRoundRect(
            space.x + v,
            space.y + v,
            (space.x + space.width) - v,
            (space.y + space.height) - v,
            20f,
            20f,
            transPaint
        )

    }

    private fun drawAlpha() {
        val v = alphaAnimator?.animatedValue as Float
        c.drawCircle(
            txtAlpha.x + txtAlpha.width / 2,
            txtAlpha.y + txtAlpha.height / 2,
            v,
            transPaint
        )

    }

    private fun drawParent() {
        parentPaint.color = ContextCompat.getColor(ctx, parentColor)
        parentPaint.style = Paint.Style.FILL
        parentPaint.alpha = 240
        c.drawRect(0f, 0f, width.toFloat(), height.toFloat(), parentPaint)
    }

    private fun initPresenterView() {
        when (presentChildNumber) {
            0 -> {
                presenterView = LayoutInflater.from(ctx).inflate(R.layout.help_mode_a, null, false) as RelativeLayout
                presentChildNumber++
                addPresenterChildes(
                    presenterView.LL1HelpMA,
                    puzzle.x + puzzle.width / 2,
                    puzzle.y + puzzle.height + 30f
                )
            }
            1 -> {
                presentChildDelay = 200
                presentChildNumber++
                addPresenterChildes(
                    presenterView.LL2HelpMA,
                    space.x + space.width / 2,
                    space.y - space.height - 30f
                )
            }
            2 -> {
                presentChildDelay = 200
                presentChildNumber++
                addPresenterChildes(
                    presenterView.LL3HelpMA,
                    txtAlpha.x + txtAlpha.width / 2,
                    txtAlpha.y + txtAlpha.height + 10f
                )
            }
            3 -> {
                presentChildDelay = 200
                presentChildNumber++
                val v = presenterView.txtHelpMA
                v.setShadowLayer(10f, 7f, 7f, R.color.dark)
                if (puzzle.y > 0) {
                    v.layoutParams.width = width - LL2HelpMA.width
                    addPresenterChildes(
                        v,
                        LL2HelpMA.width.toFloat(),
                        0f
                    )
                }else
                {
                    addPresenterChildes(
                        v,
                        0f,
                        height/2f - 2* App.getBlockHeight()
                    )
                }
            }
            4 -> {
                presentChildDelay = 1000
                presentChildNumber++
                val v = presenterView.btnHelpMA
                v.setOnClickListener {
                    listener.onClick(v)
                }
                v.setOnTouchListener(this)
                addPresenterChildes(
                    v,
                    txtHelpMA.x + txtHelpMA.width / 2,
                    txtHelpMA.y + txtHelpMA.height + 40
                )
            }
        }


    }

    private fun addPresenterChildes(v: View, x: Float, y: Float) {
        presenterView.removeView(v)
        addView(v)
        v.alpha = 0f
        ObjectAnimator.ofFloat(v, View.ALPHA, 0f, 1f).apply {
            startDelay = presentChildDelay
            duration = childDuration
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    initPresenterView()
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {

                    v.x = x - (if (presentChildNumber != 4) v.width / 2 else 0)
                    v.y = y

                }
            })

            start()
        }

    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (v?.id) {
            R.id.btnHelpMA -> ButtonEvent(v, event, R.raw.sound_primary)
        }
        return false
    }
}

