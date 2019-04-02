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
import android.view.animation.AccelerateInterpolator
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import ir.pepotec.app.game.R
import ir.pepotec.app.game.ui.App
import kotlinx.android.synthetic.main.dialog_finish_mode.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class DialogFinishMode(
    private val modeName: String,
    private val score: Int,
    private val listener: ResualtDialogResponse,
    ctx: Context = App.instance
) : Dialog(ctx) {


    private val message:String
    get() = "تبریک شما مراحل $modeName رو با امتیاز کل $score به پایان رساندید"
    private val v: View = LayoutInflater.from(ctx).inflate(R.layout.dialog_finish_mode, null, false)

    init {
        setContentView(v)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        v.btnNextDialogFinishMode.setOnClickListener {
            cancel()
            listener.nextLevel()
        }
        v.btnMenuDialogFinishMode.setOnClickListener {
            cancel()
            listener.prevMenu()
        }
        v.txtDialogFinishMode.text = message
        setCancelable(false)
        show()
        start()

    }

    private fun start() {
        val d = v.imgDialogFinishMode.drawable as Animatable
        d.start()
        v.imgDialogFinishMode.requestLayout()
        doAsync {
            while (true) {
                Thread.sleep(100)
                if (!(d.isRunning))
                {
                    uiThread {
                        animText()
                    }
                    break
                }

            }

        }
    }

    private fun animText() {
        ObjectAnimator.ofFloat(v.txtDialogFinishMode, View.ALPHA, 0f,1f).apply {
            duration = 100
            interpolator = AccelerateInterpolator()
            start()
        }
    }
}