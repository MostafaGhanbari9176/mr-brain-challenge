package ir.pepotec.app.game.presenter

import android.content.Context
import ir.pepotec.app.game.model.GameModeData
import ir.pepotec.app.game.model.local_data_base.GameModeDb
import ir.pepotec.app.game.ui.App

class PGameMode(private val listener: PGameModeListener? = null) {

    private val ctx: Context = App.instance

    interface PGameModeListener {
        fun gameModeData(data: ArrayList<GameModeData>)
    }

    fun getGameModeData() {
        listener?.gameModeData(GameModeDb(ctx).get())
    }

    fun getModeSubject(modeId: String): String {
        return GameModeDb(ctx).getSubject(modeId)
    }

    fun getScoreAverage(modeId: String): Int {
        return GameModeDb(ctx).getScoreAverage(modeId)
    }

    fun getScoreAverage(): Int {
        var score = 0
        val db = GameModeDb(ctx)
        val scoreList = db.getScoreAverage()
        for (s in scoreList) {
            score += s
        }
        score/=3
       /* val count = db.getCount()*/
        if(score > 25)
            db.unLock("c")
        if(score > 55) {
            db.unLock("b")
            db.unLock("d")
        }
        return score
    }

}