package ir.pepotec.app.game.ui.dialog

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.Animatable
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import ir.pepotec.app.game.R
import ir.pepotec.app.game.model.Pref
import ir.pepotec.app.game.model.local_data_base.ModeADb
import ir.pepotec.app.game.model.local_data_base.ModeBDb
import ir.pepotec.app.game.model.local_data_base.ModeCDb
import ir.pepotec.app.game.ui.App
import ir.pepotec.app.game.ui.activityMain.ActivityMain
import ir.pepotec.app.game.ui.gameModes.ActivityGame
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.dialog_loser.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class DialogLoser(
    private val message: String,
    private val listener: ResualtDialogResponse,
    private val showBtn:Boolean,
    private val levelId:Int,
    private val modeId:String,
    private val isFinnaly:Boolean
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
        v.btnMenuLoserDialog.setOnClickListener {
            listener.prevMenu()
            d.cancel()
        }
        v.btnReplayLoserDialog.setOnClickListener {
            listener.replay()
            d.cancel()
        }
        v.btnRejectLevel.setOnClickListener {
            rejectLevel()
            d.cancel()
        }
    }

    private fun rejectLevel() {
        val currentVal = Pref().getIntegerValue(Pref.brain, 100)
        if(currentVal < 20)
            showDialogLowBrain()
        else
        {
            Pref().saveIntegerValue(Pref.brain, currentVal - 20)
            (ctx as ActivityGame).txtBarinNumberMain.text = "${currentVal - 20 }"
            when (modeId)
            {
                "a" -> ModeADb(ctx).unLockLevel(levelId + 1)
                "b" -> ModeBDb(ctx).unLockLevel(levelId + 1)
                "c" -> ModeCDb(ctx).unLockLevel(levelId + 1)
            }
            listener.nextLevel()
        }

    }

    private fun showDialogLowBrain() {
        val d = AlertDialog.Builder(ctx)
        d.setTitle("تعداد نشان کافی نیست")
        d.setMessage("برای کسب نشان بیشتر بازی بی نهایت را انجام دهید")
        d.setPositiveButton("انجام بازی بی نهایت") { dialog, which ->
            (ctx as ActivityGame).startGame("d", 1)
            dialog.cancel()
        }
        d.setNegativeButton("سیع مجدد"){dialog, which ->
            listener.replay()
            dialog.cancel()
        }
        d.setNeutralButton("بازگشت به منو"){dialog, which ->
            listener.prevMenu()
            dialog.cancel()
        }
        d.setCancelable(false)
        d.show()
    }

    private fun startAnim() {
        (v.imgDialogLoser.drawable as Animatable).start()
        val h = Handler()
        Thread(
            Runnable {
                Thread.sleep(1100)
                h.post {
                    showMessage()
                    if(showBtn && !isFinnaly)
                        v.btnRejectLevel.visibility = View.VISIBLE
                }
                Thread.sleep(500)
                h.post { v.imgDialogLoser.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.loser_message_vector)) }
            }
        ).start()
    }

    private fun showMessage() {
        ObjectAnimator.ofFloat(v.txtDialogLoser, View.ALPHA, 0f, 1f).start()
    }
}