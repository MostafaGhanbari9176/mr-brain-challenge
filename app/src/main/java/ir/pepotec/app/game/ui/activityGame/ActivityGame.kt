package ir.pepotec.app.game.ui.activityGame

import android.content.res.Resources
import android.graphics.Point
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import ir.pepotec.app.game.R
import ir.pepotec.app.game.model.DGame
import ir.pepotec.app.game.presenter.GamePresent
import ir.pepotec.app.game.ui.App
import kotlinx.android.synthetic.main.activity_game.*
import org.jetbrains.anko.toast

class ActivityGame : AppCompatActivity(), View.OnDragListener, GameCreator.GameCreatorInterface {

    var gId: Int = -1
    private var mLastTouchX: Int = 0
    lateinit var puzzle: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        App.instance = this
        if ((intent?.extras?.isEmpty) == false) {
            gId = intent.getIntExtra("gId", -1)
            createGame()
        } else
            toast("Error")

        initViews()

    }

    override fun onResume() {
        super.onResume()
        App.instance = this
    }

    private fun initViews() {
        App.fullScreen(this)
        GameParent.setOnDragListener(this)
    }


    private fun createGame() {
        val p = Point()
        (windowManager.defaultDisplay).getRealSize(p)
        GameCreator(gId, GameParent, p,this).createGame()
    }


    override fun gameCreated(puzzle: LinearLayout) {
        this.puzzle = puzzle
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
        }// view.setX(dragEvent.getX());
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

    private fun changePuzzleSize(dx: Int) {
        puzzle.requestLayout()
        // puzzle.setBackgroundResource(R.color.secondaryGreen)
        puzzle.layoutParams.width = puzzle.width + (dx / 1.5).toInt()
        //toast((puzzle.width + (dx / 1.5)).toString())
    }


}