package ir.pepotec.app.game.ui.gameModels.mode_a

import android.content.Context
import android.graphics.Point
import android.support.v4.content.ContextCompat
import android.widget.LinearLayout
import android.widget.RelativeLayout
import ir.pepotec.app.game.R
import ir.pepotec.app.game.model.DA
import ir.pepotec.app.game.presenter.APresent
import ir.pepotec.app.game.ui.App

class GameCreatorA(
    private val gId: Int,
    private val parentView: RelativeLayout,
    private val LLRight: LinearLayout,
    private val LLLeft: LinearLayout,
    private val LLSpace: LinearLayout,
    private val LLPuzzle: LinearLayout,
    private val p: Point,
    private val listener: GameCreatorInterface
) : APresent.GamePresentInterface {

    interface GameCreatorInterface {
        fun gameCreated(space: Int, alpha: Float)
    }

    private val ctx: Context = App.instance
    private var space: Int = 0
    private var blockLengthR: Int = 0
    private var blockLengthL: Int = 0

    fun createGame() {
        APresent(this).getGameData(gId)
    }

    private fun createViews(location: Int, defaultLocation: Int, length: Int, colorId: Int): LinearLayout {
        val ll = LinearLayout(ctx)
        ll.background = ContextCompat.getDrawable(ctx, colorId)
        val params = RelativeLayout.LayoutParams(length, getHeightPX())
        params.addRule(defaultLocation)
        params.addRule(location)
        ll.layoutParams = params
        parentView.addView(ll)
        return ll
    }

    private fun getHeightPX(): Int {
        return (ctx as ActivityModeA).resources.getDimension(R.dimen.puzzle_height).toInt()
    }

    override fun gameData(data: DA) {
        initSpaceLength(data.alpha)
        initBlocksLength(data.sLocation)
        //addGuide()
        initPuzzle(data.alpha, data.pLocation)
        listener.gameCreated(space, data.alpha)
    }

    private fun initPuzzle(alpha: Float, location: Int) {
        val params = RelativeLayout.LayoutParams(getPuzzleLength(alpha), getHeightPX())
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP)
        params.addRule(getAlign(location))
        LLPuzzle.layoutParams = params
    }

    private fun initBlocksLength(location: Int) {
        when (location) {
            0 -> {
                blockLengthR = 0
                blockLengthL = p.x - space
            }
            1 -> {
                blockLengthL = (p.x - space) / 2
                blockLengthR = (p.x - space) / 2
            }
            2 -> {
                blockLengthR = p.x - space
                blockLengthL = 0
            }
        }

        var params = LinearLayout.LayoutParams(blockLengthR, getHeightPX())
        LLRight.layoutParams = params
        params = LinearLayout.LayoutParams(blockLengthL, getHeightPX())
        LLLeft.layoutParams = params

    }

    private fun initSpaceLength(alpha: Float) {
        val step = (p.x)/40
        val max = if (alpha > 1)
            (p.x / alpha).toInt()
        else
            (p.x * 0.8).toInt()
        val min = (p.x * 0.2).toInt()
        space = (min..max).random()
        if(space % step != 0){
         space = if(space > step) space+(space%step) else space-(space%step)
        }
        val params = LinearLayout.LayoutParams(space, getHeightPX())
        LLSpace.layoutParams = params
    }

    private fun addGuide() {
        val param = LinearLayout.LayoutParams(5, p.y)
        if (blockLengthR != 0) {
            val ll = LinearLayout(ctx)
            ll.background = ContextCompat.getDrawable(ctx, R.color.secondaryLightOrange)
            ll.layoutParams = param
            parentView.addView(ll)
            ll.translationX = -(blockLengthR.toFloat())
        }
        if (blockLengthL != 0) {
            val ll2 = LinearLayout(ctx)
            ll2.layoutParams = param
            ll2.background = ContextCompat.getDrawable(ctx, R.color.secondaryLightOrange)
            parentView.addView(ll2)
            ll2.translationX = -((blockLengthR + space).toFloat())
        }
    }

    private fun getPuzzleLength(alpha: Float): Int =
        when (alpha) {
            1f -> space / 2
            else -> space
        }

    private fun getAlign(location: Int): Int =
        when (location) {
            0 -> (RelativeLayout.ALIGN_PARENT_START)
            1 -> (RelativeLayout.CENTER_IN_PARENT)
            2 -> (RelativeLayout.ALIGN_PARENT_END)
            else -> (RelativeLayout.CENTER_IN_PARENT)
        }


}