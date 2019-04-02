package ir.pepotec.app.game.presenter

import android.content.Context
import ir.pepotec.app.game.model.ModeCData
import ir.pepotec.app.game.model.local_data_base.GameModeDb
import ir.pepotec.app.game.model.local_data_base.ModeCDb
import ir.pepotec.app.game.ui.App

class PModeCLevel(private val listener: PModeCLevelInterface? = null) {

    val ctx: Context = App.instance

    interface PModeCLevelInterface {
        fun levelData(data: ModeCData)
    }

    fun getLevelData(levelId: Int) {
        listener?.levelData(ModeCDb(ctx).get(levelId))
    }

    fun saveScore(levelId: Int, score: Int) {
        val db = ModeCDb(ctx)
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
        GameModeDb(ctx).saveScoreAverage("c", scoreAve)

    }
}