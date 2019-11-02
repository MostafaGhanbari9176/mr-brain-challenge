package ir.pepotec.app.game.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Animatable
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.view.LayoutInflater
import ir.pepotec.app.game.R
import ir.pepotec.app.game.ui.App
import kotlinx.android.synthetic.main.dialog_level_present.view.*

class DialogLevelPresent(private val levelNumber: Int, private val modeSubject: String, ctx: Context = App.instance) :
    Dialog(ctx) {
    private val v = LayoutInflater.from(ctx).inflate(R.layout.dialog_level_present, null, false)

    init {
        v.txtModeSubjectLevelPresent.text = modeSubject
        v.txtLevelNumberLevelPresent.text = "$levelNumber"
        v.parentLevelPresent.alpha = 0f
        v.parentLevelPresent.setOnClickListener {
            cancel()
        }
        setContentView(v)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        show()
        setCancelable(false)
        startAnimation()

    }

    private fun startAnimation() {
        val h = Handler()
        Thread(
            Runnable {
                for (i in 0..100 step 10) {
                    if (!isShowing)
                        break
                    h.post { v.parentLevelPresent.alpha = i / 100f }
                    Thread.sleep(10)
                }
                Thread.sleep(1000)
                for (i in 100 downTo 0 step 10) {
                    if (!isShowing)
                        break
                    h.post { v.parentLevelPresent.alpha = i / 100f }
                    Thread.sleep(10)
                }
                if (isShowing)
                    h.post { cancel() }
            }

        ).start()
    }
}