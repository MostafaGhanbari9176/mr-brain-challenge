package ir.pepotec.app.game.ui.gameModes.mode_c

import android.content.Context
import android.graphics.Point
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import ir.pepotec.app.game.R
import ir.pepotec.app.game.model.ModeCData
import ir.pepotec.app.game.presenter.PModeCLevel
import ir.pepotec.app.game.ui.App
import ir.pepotec.app.game.ui.gameModes.ActivityGame

class GameCreatorC(
    private val levelId: Int,
    private val parentView: RelativeLayout,
    private val rightSeat: LinearLayout,
    private val leftSeat: LinearLayout,
    private val LLSeat: LinearLayout,
    private val rightBus: LinearLayout,
    private val leftBus: LinearLayout,
    private val LLPuzzle: LinearLayout,
    private val listener: GameCreatorInterface
) : PModeCLevel.PModeCLevelInterface {

    interface GameCreatorInterface {
        fun gameCreated(
            space: Int,
            alpha: Float,
            guideSpace: LinearLayout,
            guideBus: LinearLayout,
            guidePuzzle: LinearLayout,
            isFinally: Boolean,
            puzzlePosition:Int,
            loseNumber:Int
        )
    }

    private val ctx: Context = App.instance
    private var space: Int = 0
    private val p = Point()

    fun createGame() {
        (ctx as ActivityGame).windowManager.defaultDisplay.getRealSize(p)
        PModeCLevel(this).getLevelData(levelId)
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

    override fun levelData(data: ModeCData) {
        with(data) {
            initSpaceLength(alpha)
            initSeatBlocksLength(spaceX)
            initBusBlockLength(busX)
            initPuzzle(alpha, puzzleX, 0)

            listener.gameCreated(
                space,
                alpha,
                createGuide(spaceX, 0, alpha, (p.y - getHeightPX()).toFloat()),
                createGuide(busX, 1, alpha, getHeightPX()/2f),
                createGuide(puzzleX, 0, alpha, (getHeightPX() + 10f)),
                isFinally == 1,
                getXAlign(puzzleX),
                loseNumber
            )
        }
    }

    private fun initBusBlockLength(busX: Int) {
        var blockLengthR = 0
        var blockLengthL = 0
        when (busX) {
            0 -> {
                blockLengthR = 0
                blockLengthL = p.x - space -40
            }
            1 -> {
                blockLengthL = (p.x - space) / 2 -40
                blockLengthR = (p.x - space) / 2 -40
            }
            2 -> {
                blockLengthR = p.x - space -40
                blockLengthL = 0
            }
        }

        var params = LinearLayout.LayoutParams(blockLengthR, getHeightPX())
        rightBus.layoutParams = params
        params = LinearLayout.LayoutParams(blockLengthL, getHeightPX())
        leftBus.layoutParams = params
    }

    private fun initPuzzle(alpha: Float, pX: Int, pY: Int) {
        val params = RelativeLayout.LayoutParams(getPuzzleLength(alpha), getHeightPX())
        params.addRule(getYAlign(pY))
        params.addRule(getXAlign(pX))
        LLPuzzle.layoutParams = params
    }

    private fun initSeatBlocksLength(location: Int) {
        var blockLengthR = 0
        var blockLengthL = 0
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
        rightSeat.layoutParams = params
        params = LinearLayout.LayoutParams(blockLengthL , getHeightPX())
        leftSeat.layoutParams = params

    }

    private fun initSpaceLength(alpha: Float) {
        val step = (p.x) / 40
        val min = 15
        val max: Int = if(alpha<=1) 32 else  (40f / alpha).toInt()
        space = ((min..max).random() * step)
        LLSeat.layoutParams.width = space
    }

    private fun getPuzzleLength(alpha: Float): Int =
        when (alpha) {
            1f -> space / 2
            else -> space
        }

    private fun getXAlign(location: Int): Int =
        when (location) {
            0 -> (RelativeLayout.ALIGN_PARENT_START)
            1 -> (RelativeLayout.CENTER_HORIZONTAL)
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