package ir.pepotec.app.game.ui.activityMain

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.pepotec.app.game.R
import ir.pepotec.app.game.model.DMainMenu
import ir.pepotec.app.game.presenter.MainMenuPresent
import ir.pepotec.app.game.ui.App
import ir.pepotec.app.game.ui.adapters.AdapterMainMenu
import kotlinx.android.synthetic.main.fragment_main_menu.*

class FragmentMainMenu : Fragment() , MainMenuPresent.MainMenuInterface {

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
        MainMenuPresent(this).getData()
    }


    override fun getMainMenuList(data: ArrayList<DMainMenu>) {
        RVMainMenu.layoutManager = LinearLayoutManager(ctx)
        val adapter = AdapterMainMenu(data){act.animateImageToHome() }
        RVMainMenu.adapter = adapter
    }


}
