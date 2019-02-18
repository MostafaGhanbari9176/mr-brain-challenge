package ir.pepotec.app.game.ui.activityMain

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.pepotec.app.game.R
import ir.pepotec.app.game.model.DGameLevel
import ir.pepotec.app.game.presenter.GameLevelPresent
import ir.pepotec.app.game.ui.App
import ir.pepotec.app.game.ui.gameModels.a.ActivityA
import ir.pepotec.app.game.ui.adapters.AdapterGameLevel
import kotlinx.android.synthetic.main.fragment_game_level.*

class FragmentGameLevel : Fragment(), GameLevelPresent.GameLevelInterface {


    val ctx: Context = App.instance

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_game_level, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        GameLevelPresent(this).getData()
    }

    override fun GameLevelData(data: ArrayList<DGameLevel>) {
       RVGameLevel.layoutManager = GridLayoutManager(ctx, 2)
        val adapter = AdapterGameLevel(data) {gotoNextActivity(it) }
        RVGameLevel.adapter = adapter

    }

    private fun gotoNextActivity(data:DGameLevel) {
        val intent = Intent(ctx, ActivityA::class.java)
        with(data) {
            intent.putExtra("gId", gId)
        }
        startActivity(intent)
    }
}