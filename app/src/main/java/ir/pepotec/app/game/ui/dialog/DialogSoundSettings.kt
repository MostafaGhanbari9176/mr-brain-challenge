package ir.pepotec.app.game.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import ir.pepotec.app.game.R
import ir.pepotec.app.game.model.Pref
import ir.pepotec.app.game.ui.App
import ir.pepotec.app.game.ui.activityMain.ActivityMain
import kotlinx.android.synthetic.main.dialog_sound_setting.view.*

class DialogSoundSettings : Dialog(App.instance) {
    private val p = Pref()
    private val v: View = LayoutInflater.from(context).inflate(R.layout.dialog_sound_setting, null, false)
    val point = Point()
    init {
        (App.instance as ActivityMain).windowManager.defaultDisplay.getRealSize(point)
        initView()
        setContentView(v)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        v.layoutParams.width = point.x
        v.layoutParams.height = (App.instance as ActivityMain).resources.getDimension(R.dimen.item_main_menu_height).toInt()
        show()
    }

    private fun initView() {
        val mute =  p.getBollValue(Pref.mute, false)
        initText(!mute)
        v.swcSoundSetting.isChecked = !mute
        v.swcSoundSetting.setOnCheckedChangeListener { buttonView, isChecked ->
            ActivityMain.musicService?.muteMusic(!isChecked)
            initText(isChecked)
        }

    }

    private fun initText(soundOn: Boolean) {
        v.txtSoundSetting.apply {
            text = if(soundOn) "روشن" else "خاموش"
            setTextColor(ContextCompat.getColor(context, if(soundOn) R.color.secondaryGreen else R.color.red))
        }
    }
}