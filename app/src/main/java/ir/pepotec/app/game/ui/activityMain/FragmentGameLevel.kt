package ir.pepotec.app.game.ui.activityMain

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.pepotec.app.game.R
import ir.pepotec.app.game.model.ItemData
import ir.pepotec.app.game.presenter.PGameLevel
import ir.pepotec.app.game.ui.App
import ir.pepotec.app.game.ui.activityMain.adapters.AdapterGameLevel
import ir.pepotec.app.game.ui.gameModes.ActivityGame
import kotlinx.android.synthetic.main.fragment_game_level.*
import kotlinx.android.synthetic.main.main_activity.*

class FragmentGameLevel : Fragment(), PGameLevel.PGameLevelListener {

    private val ctx: Context = App.instance
    var modeId: String = "error"
    private lateinit var act:ActivityMain
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_game_level, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        act = activity as ActivityMain
        init()
    }

    private fun init() {
        PGameLevel(this).getLevelData(modeId)
    }

    override fun levelData(data: ArrayList<ItemData>) {
        RVGameLevel.layoutManager = GridLayoutManager(ctx, 2)
        val adapter = AdapterGameLevel(data) { levelId -> gotoNextActivity(levelId) }
        RVGameLevel.adapter = adapter

        RVGameLevel.addOnScrollListener(object:RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if(dy > 0 && act.floatBtnMain.visibility == View.VISIBLE)
                    act.floatBtnMain.hide()
                else if(dy <0 && act.floatBtnMain.visibility == View.GONE)
                    act.floatBtnMain.show()
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    private fun gotoNextActivity(levelId: Int) {
            val intent = Intent(ctx as ActivityMain, ActivityGame::class.java)
            intent.putExtra("levelId", levelId)
            intent.putExtra("modeId", modeId)
            activity?.startActivityForResult(intent, 1)
    }
}