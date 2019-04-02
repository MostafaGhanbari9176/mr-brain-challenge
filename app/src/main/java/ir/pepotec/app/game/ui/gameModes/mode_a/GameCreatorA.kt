package ir.pepotec.app.game.ui.gameModes.mode_a

import android.content.Context
import android.graphics.Point
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import ir.pepotec.app.game.R
import ir.pepotec.app.game.model.ModeAData
import ir.pepotec.app.game.presenter.PModeALevel
import ir.pepotec.app.game.ui.App
import ir.pepotec.app.game.ui.gameModes.ActivityGame

class GameCreatorA(
    private val gId: Int,
    private val parentView: RelativeLayout,
    private val LLRight: LinearLayout,
    private val LLLeft: LinearLayout,
    private val LLSpace: LinearLayout,
    private val LLPuzzle: LinearLayout,
    private val p: Point,
    private val listener: GameCreatorInterface
) : PModeALevel.PModeALevelInterface {

    interface GameCreatorInterface {
        fun gameCreated(space: Int, alpha: Float, guideSpace: LinearLayout, guidePuzzle: LinearLayout, isFinally:Boolean)
    }

    private val ctx: Context = App.instance
    private var space: Int = 0
    private var blockLengthR: Int = 0
    private var blockLengthL: Int = 0

    fun createGame() {
        PModeALevel(this).getLevelData(gId)
    }

    private fun createGuide(pX: Int, pY: Int, alpha: Float, transY: Float): LinearLayout {
        val ll = LinearLayout(ctx)
        ll.setBackgroundResource(R.drawable.guide_back_mode_a)
        val params = RelativeLayout.LayoutParams(space, 20)
        params.addRule(getXAlign(pX))
        params.addRule(getYAlign(pY))
        ll.layoutParams = params
        ll.translationY = transY
        if (alpha == 1f)
            ll.visibility = View.GONE
        parentView.addView(ll)
        return ll
    }

    fun getHeightPX(): Int {
        return (ctx as ActivityGame).resources.getDimension(R.dimen.puzzle_height).toInt()
    }

    override fun levelData(data: ModeAData) {
        initSpaceLength(data.alpha)
        initBlocksLength(data.spaceX)
        //addGuide()
        initPuzzle(data.alpha, data.puzzleX, data.puzzleY)

        listener.gameCreated(
            space,
            data.alpha,
            createGuide(data.spaceX, 0, data.alpha, (p.y - getHeightPX()).toFloat()),
            createGuide(data.puzzleX, data.puzzleY, data.alpha, (getHeightPX() + 10f)),
            data.isFinally == 1
        )
    }

    private fun initPuzzle(alpha: Float, pX: Int, pY: Int) {
        val params = RelativeLayout.LayoutParams(getPuzzleLength(alpha), getHeightPX())
        params.addRule(getYAlign(pY))
        params.addRule(getXAlign(pX))
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

        LLRight.layoutParams.width = blockLengthR
        LLLeft.layoutParams.width = blockLengthL

    }

    private fun initSpaceLength(alpha: Float) {
        val step = (p.x) / 40
        val min = 15
        val max: Int = if(alpha<=1) 32 else  (40f / alpha).toInt()
        space = ((min..max).random() * step)
        LLSpace.layoutParams.width = space
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