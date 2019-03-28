package ir.pepotec.app.game.presenter

import android.content.Context
import ir.pepotec.app.game.model.GameModeData
import ir.pepotec.app.game.model.local_data_base.GameModeDb
import ir.pepotec.app.game.ui.App

class PGameMode(private val listener: PGameModeListener) {

    private val ctx :Context = App.instance

    interface PGameModeListener {
        fun gameModeData(data: ArrayList<GameModeData>)
    }

    fun getGameModeData() {
        listener.gameModeData(GameModeDb(ctx).get())
    }
}