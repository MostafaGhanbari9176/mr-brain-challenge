package ir.pepotec.app.game.ui.activityMain

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import ir.pepotec.app.game.R
import ir.pepotec.app.game.model.GameModeData
import ir.pepotec.app.game.presenter.PGameMode
import ir.pepotec.app.game.ui.App
import ir.pepotec.app.game.ui.activityMain.adapters.AdapterGameMode
import ir.pepotec.app.game.ui.gameModes.ActivityGame
import kotlinx.android.synthetic.main.fragment_game_mode.*

class FragmentGameMode : Fragment(), PGameMode.PGameModeListener {

    private val ctx: Context = App.instance
    private lateinit var act: ActivityMain

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
        PGameMode(this).getGameModeData()
    }

    override fun gameModeData(data: ArrayList<GameModeData>) {
        RVGameMode.layoutManager = GridLayoutManager(ctx, 2)
        val adapter = AdapterGameMode(data) { modeId ->
            if(modeId == "d")
                startD()
            else
            act.animateImageToBack(modeId) }
        RVGameMode.adapter = adapter
    }

    private fun startD() {
        val intent = Intent(ctx as ActivityMain, ActivityGame::class.java)
        intent.putExtra("levelId", 0)
        intent.putExtra("modeId", "d")
        activity?.startActivityForResult(intent, 1)
    }
}