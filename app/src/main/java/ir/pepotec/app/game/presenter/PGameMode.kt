package ir.pepotec.app.game.presenter

import android.content.Context
import ir.pepotec.app.game.model.GameModeData
import ir.pepotec.app.game.model.local_data_base.GameModeDb
import ir.pepotec.app.game.ui.App

class PGameMode(private val listener: PGameModeListener? = null) {

    private val ctx: Context = App.instance
    val minForC = 25
    val minForB = 55
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
        if(score >= minForC)
            db.unLock("c")
        if(score >= minForB) {
            db.unLock("b")
            db.unLock("d")
        }
        return score
    }

}