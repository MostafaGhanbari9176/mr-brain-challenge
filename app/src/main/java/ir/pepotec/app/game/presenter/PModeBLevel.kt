package ir.pepotec.app.game.presenter

import android.content.Context
import ir.pepotec.app.game.model.ModeBData
import ir.pepotec.app.game.model.local_data_base.GameModeDb
import ir.pepotec.app.game.model.local_data_base.ModeBDb
import ir.pepotec.app.game.ui.App

class PModeBLevel(private val listener: PModeBLevelInterface? = null) {

    val ctx: Context = App.instance

    interface PModeBLevelInterface {
        fun levelData(data: ModeBData)
    }

    fun getLevelData(levelId: Int) {
        listener?.levelData(ModeBDb(ctx).get(levelId))
    }

    fun saveScore(levelId: Int, score: Int) {
        val db = ModeBDb(ctx)
        db.saveScore(levelId, score)
        db.unLockLevel(levelId + 1)
        val count = db.getCount()
        var scoreAve = 0
        val scoreList = db.getScore()
        for (s in scoreList)
        {
            scoreAve+=s
        }
        scoreAve /= count
        GameModeDb(ctx).saveScoreAverage("b", scoreAve)
    }
}