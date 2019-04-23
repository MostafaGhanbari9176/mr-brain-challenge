package ir.pepotec.app.game.ui.activityMain

import android.content.Context
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ir.pepotec.app.game.R
import ir.pepotec.app.game.model.ItemData
import ir.pepotec.app.game.ui.App
import ir.pepotec.app.game.ui.activityMain.adapters.AdapterMainMenu
import ir.pepotec.app.game.ui.uses.ButtonEvent
import kotlinx.android.synthetic.main.fragment_main_menu.*

class FragmentMainMenu : Fragment() {

    private val ctx:Context = App.instance
    lateinit private var act:ActivityMain
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        act = activity as ActivityMain
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
        aboutUs.setOnClickListener {  }

    }

    private fun startAnim() {
        (startGame.background as Animatable).start()
        (setting.background as Animatable).start()
        (comment.background as Animatable).start()
        (aboutUs.background as Animatable).start()
        (gameOut.background as Animatable).start()
    }

}
