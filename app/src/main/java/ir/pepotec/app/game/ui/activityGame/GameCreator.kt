package ir.pepotec.app.game.ui.activityGame

import android.content.Context
import android.content.res.Resources
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
    private val parentView:RelativeLayout,
    private val p: Point,
    private val listener: GameCreatorInterface
) : GamePresent.GamePresentInterface {


    interface GameCreatorInterface {

        fun gameCreated(puzzle: LinearLayout)
    }

    private val ctx: Context = App.instance
    private var blockLength:Int = 0
    fun createGame() {
        initBlockLength()
        GamePresent(this).getGameData(gId)
    }

    private fun createViews(location: Int, defaultLocation:Int, length:Int):LinearLayout {
        val ll = LinearLayout(ctx)
        ll.background = ContextCompat.getDrawable(ctx, R.color.secondaryYellow)
        val params = RelativeLayout.LayoutParams(length, getHeightPX())
        params.addRule(defaultLocation)
        params.addRule(location)
        ll.layoutParams = params
        parentView.addView(ll)
        return ll
    }


    private fun getHeightPX(): Int {
        return (ctx as ActivityGame).resources.getDimension(R.dimen.puzzle_height).toInt()
    }

    override fun gameData(data: DGame) {
        for(e in data.block){
            createViews(getBlockAlign(e.location), RelativeLayout.ALIGN_PARENT_START, if(e.rLength == 0) 0 else blockLength)
            createViews(getBlockAlign(e.location), RelativeLayout.ALIGN_PARENT_END, if(e.lLength == 0) 0 else blockLength)
        }
        listener.gameCreated(createViews(getPuzzleAlign(data.pLocation), RelativeLayout.ALIGN_PARENT_TOP, getPuzzleLength(data.alpha)))
    }

    private fun getPuzzleLength(alpha:Float): Int =
        when (alpha) {
            1f -> {
                (p.x) - blockLength
            }
            else -> {
                (p.x) - 2 * blockLength
            }
        }


    private fun initBlockLength() {
        val max = (p.x * 0.4).toInt()
        val min = (p.x * 0.1).toInt()
        blockLength =  (min..max).random()
    }

    private fun getPuzzleAlign(location:Int): Int =
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