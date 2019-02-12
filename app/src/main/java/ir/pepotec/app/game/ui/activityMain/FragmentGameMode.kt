package ir.pepotec.app.game.ui.activityMain

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.pepotec.app.game.R
import ir.pepotec.app.game.model.DGameMode
import ir.pepotec.app.game.presenter.GameModePresent
import ir.pepotec.app.game.ui.App
import ir.pepotec.app.game.ui.adapters.AdapterGameMode
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_game_mode.*

class FragmentGameMode : Fragment(), GameModePresent.ModeGameInterface {

    private val ctx: Context = App.instance
    lateinit private var  act : ActivityMain
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_game_mode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        act = activity as ActivityMain
        init()
    }

    private fun init() {
        //ctx.toast("loaded")
        GameModePresent(this).getData()
    }

    override fun modeMenuData(data: ArrayList<DGameMode>) {
        RVGameMode.layoutManager = GridLayoutManager(ctx, 2)
        val adapter = AdapterGameMode(data) { act.animateImageToBack() }
        RVGameMode.adapter = adapter
    }
}