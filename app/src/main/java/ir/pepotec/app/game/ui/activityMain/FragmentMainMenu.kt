package ir.pepotec.app.game.ui.activityMain

import android.content.Intent
import android.graphics.drawable.Animatable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import ir.pepotec.app.game.R
import ir.pepotec.app.game.ui.App
import ir.pepotec.app.game.ui.dialog.DialogAboutUs
import ir.pepotec.app.game.ui.dialog.DialogSoundSettings
import ir.pepotec.app.game.ui.uses.ButtonEvent
import kotlinx.android.synthetic.main.fragment_main_menu.*

class FragmentMainMenu : Fragment() {

    private val act:ActivityMain = (App.instance as ActivityMain)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        startAnim()
        startGame.setOnTouchListener { v, event ->
            ButtonEvent(v, event)
            false
        }
        startGame.setOnClickListener { act.showGameMode() }

        gameOut.setOnTouchListener { v, event ->
            ButtonEvent(v, event)
            false
        }
        gameOut.setOnClickListener { act.finish() }

        aboutUs.setOnTouchListener { v, event ->
            ButtonEvent(v, event)
            false
        }
        aboutUs.setOnClickListener {
            DialogAboutUs()
        }

        setting.setOnTouchListener { v, event ->
            ButtonEvent(v, event)
            false
        }
        setting.setOnClickListener {
            DialogSoundSettings()
        }

        helpMovie.setOnTouchListener { v, event ->
            ButtonEvent(v, event)
            false
        }
        helpMovie.setOnClickListener {
           act.setView(FragmentHelpMovie())
        }

        comment.setOnTouchListener { v, event ->
            ButtonEvent(v, event)
            false
        }
        comment.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_EDIT)
                intent.data = Uri.parse("http://cafebazaar.ir/app/?id=${act.packageName}")
                startActivity(intent)
            }catch (e:Exception){
                Toast.makeText(App.instance, "???????? ?????? ??????,???????? ???????????? ???????? ?????????? ???? ?????? ????????.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun startAnim() {
        (startGame.background as Animatable).start()
        (helpMovie.background as Animatable).start()
        (setting.background as Animatable).start()
        (comment.background as Animatable).start()
        (aboutUs.background as Animatable).start()
        (gameOut.background as Animatable).start()
    }

}
