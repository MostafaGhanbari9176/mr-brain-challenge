package ir.pepotec.app.game.ui.activityGame

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Point
import android.support.v4.content.ContextCompat
import android.view.Display
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import ir.pepotec.app.game.R
import ir.pepotec.app.game.model.DGame
import ir.pepotec.app.game.presenter.GamePresent
import ir.pepotec.app.game.ui.App
import org.jetbrains.anko.toast
import kotlin.random.Random

class GameCreator(
    private val gId: Int,
    private val parentView: RelativeLayout,
    private val p: Point,
    private val listener: GameCreatorInterface
) : GamePresent.GamePresentInterface {

    interface GameCreatorInterface {
        fun gameCreated(puzzle: LinearLayout, space: Int, alpha: Float)
    }

    private val ctx: Context = App.instance
    private var blockLength: Int = 0
    private var space: Int = 0

    fun createGame() {
        GamePresent(this).getGameData(gId)
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

    fun getHeightPX(): Int {
        return (ctx as ActivityGame).resources.getDimension(R.dimen.puzzle_height).toInt()
    }

    override fun gameData(data: DGame) {
        initBlockLength(data.alpha)
        for (e in data.block) {
            createViews(
                getBlockAlign(e.location),
                RelativeLayout.ALIGN_PARENT_START,
                if (e.rLength == 0) 0 else blockLength,
                R.color.secondaryLightGreen
            )
            createViews(
                getBlockAlign(e.location),
                RelativeLayout.ALIGN_PARENT_END,
                if (e.lLength == 0) 0 else blockLength,
                R.color.secondaryLightGreen
            )
        }
        addGuide()
        listener.gameCreated(
            createViews(
                getPuzzleAlign(data.pLocation),
                RelativeLayout.ALIGN_PARENT_TOP,
                getPuzzleLength(data.alpha),
                R.color.secondaryDarkYellow
            ), space, data.alpha
        )
    }

    private fun initBlockLength(alpha: Float) {
        val max = if (alpha > 1)
            (p.x / alpha).toInt()
        else
            (p.x * 0.8).toInt()
        val min = (p.x * 0.2).toInt()
        space = (min..max).random()
        blockLength = (p.x - space) / 2
    }

    private fun addGuide(){
        val ll = LinearLayout(ctx)
        val param = LinearLayout.LayoutParams(5,p.y)
        ll.background = ContextCompat.getDrawable(ctx, R.color.secondaryLightOrange)
        ll.layoutParams = param
        parentView.addView(ll)
        ll.translationX = -((p.x - blockLength).toFloat())

        val ll2 = LinearLayout(ctx)
        ll2.layoutParams = param
        ll2.background = ContextCompat.getDrawable(ctx, R.color.secondaryLightOrange)
        parentView.addView(ll2)
        ll2.translationX = -((p.x - blockLength - space).toFloat())
    }

    private fun getPuzzleLength(alpha: Float): Int =
        when (alpha) {
            1f -> (p.x) - blockLength
            else -> (p.x) - 2 * blockLength
        }

    private fun getPuzzleAlign(location: Int): Int =
        when (location) {
            0 -> (RelativeLayout.ALIGN_PARENT_START)
            1 -> (RelativeLayout.CENTER_IN_PARENT)
            2 -> (RelativeLayout.ALIGN_PARENT_END)
            else -> (RelativeLayout.CENTER_IN_PARENT)
        }


    private fun getBlockAlign(location: Int): Int =
        when (location) {
            0 -> (RelativeLayout.ALIGN_PARENT_TOP)
            1 -> (RelativeLayout.CENTER_IN_PARENT)
            2 -> (RelativeLayout.ALIGN_PARENT_BOTTOM)
            else -> (RelativeLayout.ALIGN_PARENT_BOTTOM)
        }

}