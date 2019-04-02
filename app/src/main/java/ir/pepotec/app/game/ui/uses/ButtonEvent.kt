package ir.pepotec.app.game.ui.uses

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.media.MediaPlayer
import android.view.MotionEvent
import android.view.SoundEffectConstants
import android.view.View
import androidx.annotation.RawRes
import ir.pepotec.app.game.R
import ir.pepotec.app.game.model.Pref
import ir.pepotec.app.game.ui.App

class ButtonEvent(private val v: View, e: MotionEvent?, @RawRes private val soundId: Int = R.raw.sound_primary) {

    private var mp: MediaPlayer? = null

    companion object {
        var animatorSet = AnimatorSet()
    }

    init {
        if (!(Pref().getBollValue(Pref.mute, false)))
           mp = MediaPlayer.create(App.instance, soundId)
        v.isSoundEffectsEnabled = false
        when (e?.action) {
            MotionEvent.ACTION_DOWN -> actionDown()
            MotionEvent.ACTION_UP -> actionUp()
            else -> resetView()
        }
    }

    private fun resetView() {
        animatorSet.cancel()
        v.scaleX = 1f
        v.scaleY = 1f
    }

    private fun actionUp() {
        animatorSet.cancel()
        animatorSet = AnimatorSet()
        mp?.start()
        val sX = ObjectAnimator.ofFloat(v, View.SCALE_X, 0.9f, 1.1f, 1f)
        val sY = ObjectAnimator.ofFloat(v, View.SCALE_Y, 0.9f, 1.1f, 1f)
        v.playSoundEffect(SoundEffectConstants.NAVIGATION_LEFT)
        animatorSet.apply {
            playTogether(sX, sY)
            duration = 100
            start()
        }
    }

    private fun actionDown() {
        animatorSet.cancel()
        animatorSet = AnimatorSet()
        val sX = ObjectAnimator.ofFloat(v, View.SCALE_X, 1f, 0.85f)
        val sY = ObjectAnimator.ofFloat(v, View.SCALE_Y, 1f, 0.85f)
        animatorSet.apply {
            playTogether(sX, sY)
            duration = 100
            start()
        }
    }
}