package ir.pepotec.app.game.ui.uses

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import androidx.annotation.RawRes
import ir.pepotec.app.game.R
import ir.pepotec.app.game.model.Pref
import org.jetbrains.anko.toast
import java.lang.Exception

class ServiceMusic : Service() {

    val isPlay: Boolean?
        get() = mp?.isPlaying

    private var effectVolume = 0.6f
    private var volume: Float = 0.5f
        set(value) {
            field = value
            mp?.setVolume(value, value)
        }
    private var mp: MediaPlayer? = null

    override fun onCreate() {
        super.onCreate()
        val mute = Pref().getBollValue(Pref.mute, false)
        volume = if (mute) 0f else 0.5f
    }

    fun startMusic(@RawRes id: Int, loop: Boolean = true) {
        try {
            mp?.stop()
            mp = MediaPlayer.create(this, id).apply {
                isLooping = loop
                setVolume(volume, volume)
                start()
            }
        } catch (e: Exception) {
            toast("MusicCrashed")
        }
    }

    fun stopMusic() {
        mp?.stop()
    }

    fun pauseMusic() {
        mp?.pause()
    }

    fun resumeMusic() {
        mp?.start()
    }

    fun decreaseVolume() {
        if (volume == 0f)
            return
        volume = 0.1f
    }

    fun increaseVolume() {
        if (volume == 0f)
            return
        volume = 0.5f
    }

    fun muteMusic(mute: Boolean) {
        Pref().saveBollValue(Pref.mute, mute)
        volume = if (mute) 0f else 0.5f
        effectVolume = if (mute) 0f else 0.6f
    }

    fun winSound() {
        decreaseVolume()
        MediaPlayer.create(this, R.raw.winner).apply {
            start()
            setVolume(effectVolume, effectVolume)
            setOnCompletionListener {
                increaseVolume()
            }
        }
    }

    fun loseSound() {
        decreaseVolume()
        MediaPlayer.create(this, R.raw.loser_sound).apply {
            start()
            setVolume(effectVolume, effectVolume)
            setOnCompletionListener {
                increaseVolume()
            }
        }
    }

    fun eatSound()
    {
        decreaseVolume()
        MediaPlayer.create(this, R.raw.eat).apply {
            start()
            setVolume(effectVolume, effectVolume)
            setOnCompletionListener {
                increaseVolume()
            }
        }
    }

    fun accident()
    {
        decreaseVolume()
        MediaPlayer.create(this, R.raw.accident).apply {
            start()
            setVolume(effectVolume, effectVolume)
            setOnCompletionListener {
                increaseVolume()
            }
        }
    }

    fun stop()
    {
        decreaseVolume()
        MediaPlayer.create(this, R.raw.stop).apply {
            start()
            setVolume(effectVolume, effectVolume)
            setOnCompletionListener {
                increaseVolume()
            }
        }
    }

    fun addDel()
    {
        decreaseVolume()
        MediaPlayer.create(this, R.raw.add_del).apply {
            start()
            setVolume(effectVolume, effectVolume)
            setOnCompletionListener {
                increaseVolume()
            }
        }
    }

    fun finishMode()
    {
        decreaseVolume()
        MediaPlayer.create(this, R.raw.finisho_mode).apply {
            start()
            setVolume(effectVolume, effectVolume)
            setOnCompletionListener {
                increaseVolume()
            }
        }
    }

    private val binder = BinderMusic()

    inner class BinderMusic : Binder() {
        fun getService(): ServiceMusic {
            return this@ServiceMusic
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

}