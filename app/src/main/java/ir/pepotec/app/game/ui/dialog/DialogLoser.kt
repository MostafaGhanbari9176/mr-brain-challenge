package ir.pepotec.app.game.ui.dialog

import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Animatable
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import ir.pepotec.app.game.R
import ir.pepotec.app.game.ui.App
import ir.pepotec.app.game.ui.activityMain.ActivityMain
import kotlinx.android.synthetic.main.dialog_loser.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class DialogLoser(
    private val message: String,
    private val listener: ResualtDialogResponse
) {
    private var v: View
    private val d: Dialog
    private val ctx = App.instance

    init {
        val li = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
        v = (li as LayoutInflater).inflate(R.layout.dialog_loser, null, false)
        d = Dialog(ctx)
        initViews()
        d.setContentView(v)
        d.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        d.setCancelable(false)
        d.show()
        startAnim()
        playSound()
    }

    private fun playSound() {
        ActivityMain.musicService?.loseSound()
    }

    private fun initViews() {
        // v.txtDialogLoser.text = message
        v.btnMenuLoserDialog.setOnClickListener {
            listener.prevMenu()
            d.cancel()
        }
        v.btnReplayLoserDialog.setOnClickListener {
            listener.replay()
            d.cancel()
        }
    }

    private fun startAnim() {
        (v.imgDialogLoser.drawable as Animatable).stop()
        (v.imgDialogLoser.drawable as Animatable).start()

        showMessage()
    }

    private fun showMessage() {
        doAsync {
            Thread.sleep(800)
            uiThread { (ObjectAnimator.ofFloat(v.txtDialogLoser,View.ALPHA,0f,1f).start())}
        }
    }
}