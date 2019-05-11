package ir.pepotec.app.game.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import ir.pepotec.app.game.R
import ir.pepotec.app.game.ui.App
import ir.pepotec.app.game.ui.activityMain.ActivityMain

class DialogAboutUs(ctx: Context = App.instance) : Dialog(ctx) {


    init {
        val p = Point()
        (ctx as ActivityMain).windowManager.defaultDisplay.getRealSize(p)
        val dialogSize = p.x * 0.9
        val v = LayoutInflater.from(ctx).inflate(R.layout.dialog_us, null, false)
        setContentView(v)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        v.layoutParams.apply {
            width = dialogSize.toInt()
            height = dialogSize.toInt()
        }

        show()
    }

}