package ir.pepotec.app.game.ui.dialog

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Outline
import android.graphics.Rect
import android.graphics.drawable.*
import android.view.LayoutInflater
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.devs.vectorchildfinder.VectorChildFinder
import com.devs.vectorchildfinder.VectorDrawableCompat
import ir.pepotec.app.game.R
import ir.pepotec.app.game.ui.App
import kotlinx.android.synthetic.main.dialog_loser.view.*
import kotlinx.android.synthetic.main.dialog_winner.view.*
import kotlinx.android.synthetic.main.morph.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

class DialogWinner(
    private val message: String,
    private val listener: ResualtDialogResponse,
    private val score: Int
) {

    private var v: View
    private val d: Dialog
    private val ctx = App.instance

    init {
        val li = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
        v = (li as LayoutInflater).inflate(R.layout.dialog_winner, null, false)
        d = Dialog(ctx)
        initViews()
        d.setContentView(v)
        d.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        d.setCancelable(false)
        d.show()
        startAnim()
    }

    private fun initViews() {
        v.txtDialogWinner.text = score.toString()
        v.txtDialogWinner.setShadowLayer(1f, 0f, 6f, R.color.primaryColor)
        v.btnMenuDialogWinner.setOnClickListener {
            listener.prevMenu()
            d.cancel()
        }
        v.btnReplayDialogWinner.setOnClickListener {
            listener.replay()
            d.cancel()
        }
        v.btnNextDialogWinner.setOnClickListener {
            listener.nextLevel()
            d.cancel()
        }
    }

    private fun startAnim() {

        val d = (v.imgDialogWinner.drawable as Animatable )
        d.start()
        v.imgDialogWinner.requestLayout()
        doAsync {
            while (true) {
                Thread.sleep(100)
                if (!(d.isRunning))
                    break
            }
            uiThread {
                setResults()
            }
        }

    }

    private fun setResults() {
        ValueAnimator.ofFloat(20f,100f).apply {
            duration = 300
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                val value = it.animatedValue as Float
                v.txtDialogWinner.alpha = value/100
                if(value <= score)
                {
                    val vector = VectorChildFinder(ctx, R.drawable.winner_message, v.imgDialogWinner)
                    val path = vector.findPathByName("progress") as VectorDrawableCompat.VFullPath
                    path.trimPathEnd = value/100
                }
            }
            start()
        }
    }

}