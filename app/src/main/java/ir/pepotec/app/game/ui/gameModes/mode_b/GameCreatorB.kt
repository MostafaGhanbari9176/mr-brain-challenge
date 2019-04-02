package ir.pepotec.app.game.ui.gameModes.mode_b

import android.content.Context
import android.graphics.Point
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import ir.pepotec.app.game.R
import ir.pepotec.app.game.model.ModeBData
import ir.pepotec.app.game.presenter.PModeBLevel
import ir.pepotec.app.game.ui.App
import ir.pepotec.app.game.ui.gameModes.ActivityGame

class GameCreatorB(
    private val levelId: Int,
    private val parentView: RelativeLayout,
    private val LLPuzzle: LinearLayout,
    private val LLSeat: LinearLayout,
    private val LLRight: LinearLayout,
    private val LLLeft: LinearLayout,
    private val p: Point,
    private val listener: GameCreatorInterface
) : PModeBLevel.PModeBLevelInterface {

    interface GameCreatorInterface {
        fun gameCreated(
            space: Int,
            alpha: Float,
            guideSeat: LinearLayout,
            guidePuzzle: LinearLayout,
            isFinally: Boolean
        )
    }

    private val ctx: Context = App.instance
    private var space: Int = 0

    fun createGame() {
        PModeBLevel(this).getLevelData(levelId)
    }

    private fun createGuide(parentChild:Boolean, pY: Int, alpha: Float, transY: Float): LinearLayout {
        val ll = LinearLayout(ctx)
        ll.setBackgroundResource(R.drawable.guide_back_mode_a)
        val params = RelativeLayout.LayoutParams(space, 20)
        params.addRule(getXAlign(1))
        params.addRule(getYAlign(pY))
        ll.layoutParams = params
        ll.translationY = transY
        if (alpha == 1f)
            ll.visibility = View.GONE
        if(parentChild)
        parentView.addView(ll)
        else
            LLSeat.addView(ll)
        return ll
    }

    fun getHeightPX(): Int {
        return (ctx as ActivityGame).resources.getDimension(R.dimen.puzzle_height).toInt()
    }

    override fun levelData(data: ModeBData) {
        initSpaceLength(data.alpha)
        initBlocksLength()
        initPuzzle(data.alpha, data.puzzleY)
        listener.gameCreated(
            space,
            data.alpha,
            createGuide(false, 0, data.alpha, 0f),
            createGuide(true, data.puzzleY, data.alpha, (getHeightPX() + 10f)),
            data.isFinally == 1
        )
    }

    private fun initPuzzle(alpha: Float, pY: Int) {
        val params = RelativeLayout.LayoutParams(getPuzzleLength(alpha), getHeightPX())
        params.addRule(getYAlign(pY))
        params.addRule(getXAlign(1))
        LLPuzzle.layoutParams = params
    }

    private fun initSpaceLength(alpha: Float) {
        val step = (p.x) / 40
        val min = 20
        val max: Int = if(alpha<=1) 32 else  (40f / alpha).toInt()
        space = ((min..max).random() * step)
    }

    private fun initBlocksLength() {
        LLRight.layoutParams.width = (p.x - space) / 2
        LLLeft.layoutParams.width = (p.x - space) / 2

    }

    private fun getPuzzleLength(alpha: Float): Int =
        when (alpha) {
            1f -> space / 2
            else -> space
        }

    private fun getXAlign(location: Int): Int =
        when (location) {
            0 -> (RelativeLayout.ALIGN_PARENT_START)
            1 -> (RelativeLayout.CENTER_IN_PARENT)
            2 -> (RelativeLayout.ALIGN_PARENT_END)
            else -> (RelativeLayout.CENTER_HORIZONTAL)
        }

    private fun getYAlign(location: Int): Int =
        when (location) {
            0 -> RelativeLayout.ALIGN_PARENT_TOP
            1 -> RelativeLayout.CENTER_IN_PARENT
            else -> RelativeLayout.ALIGN_PARENT_TOP
        }


}