package ir.pepotec.app.game.ui.dialog

import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import ir.pepotec.app.game.R
import ir.pepotec.app.game.ui.App
import kotlinx.android.synthetic.main.dialog_loser.view.*
import kotlinx.android.synthetic.main.dialog_winner.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class DialogWinner(
    private val message: String,
    private val listener: ResualtDialogResponse
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
        // v.txtDialogLoser.text = message
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
        (v.imgDialogWinner.drawable as AnimatedVectorDrawable).stop()
        (v.imgDialogWinner.drawable as AnimatedVectorDrawable).start()

        showMessage()
    }

    private fun showMessage() {
        doAsync() {
            Thread.sleep(800)
            uiThread { (ObjectAnimator.ofFloat(v.txtDialogWinner,View.ALPHA,0f,1f).start())}
        }
    }
}